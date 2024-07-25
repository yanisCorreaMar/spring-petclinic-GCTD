package org.springframework.samples.petclinic.acceptance.selenium;

import java.time.Duration;
import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * <pre>
 * Configuración
 * -Dwd.type=chrome|firefox
 * -Dwd.headless="1920,1080"
 * -Dwd.timeout=20
 * -Dwd.poll=2
 * -Dwd.delay=0
 * </pre>
 */
@Service
public class Browser {

	private static final Logger LOGGER = LoggerFactory.getLogger(Browser.class);

	private final String type;

	private final String language;

	private final String headless;

	private final Duration timeout;

	private final Duration poll;

	private final long delay;

	protected WebDriver driver;

	/**
	 * @param wdType Indica el tipo de web browser a emplear, ej: chrome, firefox
	 * @param wdLang Indica el idioma que prefiere el cliente
	 * @param wdHeadless Indica el tamaño de la ventana cuando se ejecuta sin modo gráfico
	 * @param wdTimeout Indica en segundos el tiempo máximo que se debe buscar un elemento
	 * HTML en la página actual, antes de fallar por no encontrarlo
	 * @param wdPoll Indica en milisegundos cada cuanto tiempo se debe insistir en la
	 * busqueda de un elemento HTML en la página actual. Debe ser menor a wbTimeout, ej:
	 * wbTimeout = 20 seg, wdPoll = 500 ms
	 * @param wdDelay Indica en milisegundos la pausa que se debe hacer antes de realizar
	 * una acción en la interfaz, útil para observar las acciones de prueba sobre la
	 * interfaz
	 */
	public Browser(@Value("${wd.type:chrome}") String wdType, @Value("${wd.lang:es}") String wdLang,
			@Value("${wd.headless:}") String wdHeadless, @Value("${wd.timeout:20}") long wdTimeout,
			@Value("${wd.poll:2000}") long wdPoll, @Value("${wd.delay:0}") long wdDelay) {
		this.type = wdType;
		this.language = wdLang;
		this.headless = wdHeadless;
		this.timeout = Duration.ofSeconds(wdTimeout);
		this.poll = Duration.ofMillis(wdPoll);
		this.delay = wdDelay;

		setUpWebDriver(this.type);
	}

	/**
	 * @param type Nombre del navegador para el que se debe construir un WebDriver
	 * @throws IllegalStateException Si existe una instancia de webdriver
	 * @throws IllegalStateException Si browser no es un navegador soportado
	 */
	public void open(String url) {
		open(url, headless);
	}

	/**
	 * @param type Nombre del navegador para el que se debe construir un WebDriver
	 * @param headless Indica el tamaño de la ventana cuando se ejecuta sin modo gráfico
	 * @throws IllegalStateException Si existe una instancia de webdriver
	 * @throws IllegalStateException Si browser no es un navegador soportado
	 */
	public void open(String url, String headless) {

		if (driver != null) {
			throw new IllegalStateException("No se debe invocar dos veces consecutivas open, sin llamar close");
		}

		this.driver = build(this.type, this.headless, this.language);

		if (headless.isBlank()) {
			this.driver.manage().window().maximize();
		}

		this.go(url);
	}

	/**
	 * @param url Ubicación a la que debe navegar el WebDriver
	 * @throws IllegalStateException Si no existe una instancia de webdriver
	 */
	public void go(String url) {
		if (driver == null) {
			throw new IllegalStateException("Antes de llamar a go, debe llamar open");
		}

		driver.get(url);
	}

	/**
	 * @return String Título de la página actual
	 */
	public String title() {
		return this.driver.getTitle();
	}

	/**
	 * Cierra el WebDriver
	 */
	public void close() {
		if (driver != null) {
			driver.quit();
		}

		driver = null;
	}

	/**
	 * Escribe value en el elemento ubicado con selector
	 * @param selector Selector de tipo id, css o xpath
	 * @param value Texto que se escribirá en el elemento ubicado con selector
	 * @param params Valores a usar en la interpolación
	 * @return void
	 * @throws IllegalStateException si selector no indica el tipo
	 * @throws IllegalStateException si el tipo de selector no está soportado
	 */
	public void write(String selector, String value, Object... params) {
		find(selector, params).sendKeys(value);
	}

	/**
	 * Realiza click en el elemento ubicado con selector
	 * @param selector Selector de tipo id, css o xpath
	 * @param params Valores a usar en la interpolación
	 * @return void
	 * @throws IllegalStateException si selector no indica el tipo
	 * @throws IllegalStateException si el tipo de selector no está soportado
	 */
	public void click(String selector, Object... params) {
		find(selector, params).click();
	}

	/**
	 * @param selector Selector de tipo id, css o xpath
	 * @param params Valores a usar en la interpolación
	 * @return String Con el texto del elemento ubicado con selector
	 * @throws IllegalStateException si selector no indica el tipo
	 * @throws IllegalStateException si el tipo de selector no está soportado
	 */
	public String text(String selector, Object... params) {
		return find(selector, params).getText();
	}

	/**
	 * @param selector Selector de tipo id, css o xpath
	 * @param params Valores a usar en la interpolación
	 * @return WebElement Ubicado con el selector
	 * @throws IllegalStateException si selector no indica el tipo
	 * @throws IllegalStateException si el tipo de selector no está soportado
	 */
	private WebElement find(String selector, Object... params) {

		waitUntil(this.delay);

		LOGGER.info("Busca la presencia del elemento '{}' con parámetros '{}', durante {} seg, con intervalos de {} ms",
				new Object[] { selector, Arrays.toString(params), timeout.toSeconds(), poll.toMillis() });

		final By by = getBy(selector, params);
		final WebDriverWait wdw = new WebDriverWait(driver, timeout, poll);
		return wdw.until(ExpectedConditions.presenceOfElementLocated(by));
	}

	/**
	 * @param selector Selector de tipo id, css o xpath
	 * @param params Valores a usar en la interpolación
	 * @return By
	 * @throws IllegalStateException si selector no indica el tipo
	 * @throws IllegalStateException si el tipo de selector no está soportado
	 */
	private By getBy(String selector, Object... params) {
		final int separator = selector.indexOf(':');
		if (separator == -1) {
			throw new IllegalStateException("Se esperaba un selector indicando stragegy:selector, " + selector);
		}

		final String byType = selector.substring(0, separator);
		final String bySelector = String.format(selector.substring(separator + 1), params);

		switch (byType) {
			case "id":
				return By.id(bySelector);
			case "css":
				return By.cssSelector(bySelector);
			case "xpath":
				return By.xpath(bySelector);
		}

		throw new IllegalStateException(
				"Se esperaba un selector indicando stragegy:selector, la estrategia no existe " + selector);
	}

	private static void setUpWebDriver(String browser) {

		switch (browser) {
			case "chrome":
				WebDriverManager.chromedriver().setup();
				break;
			case "firefox":
				WebDriverManager.firefoxdriver().setup();
				break;
			default:
				throw new IllegalStateException("No es posible construir el browser " + browser);
		}

	}

	/**
	 * @param browser Nombre del navegador para el que se debe construir un WebDriver
	 * @param headless Indica el tamaño de la ventana cuando se ejecuta sin modo gráfico
	 * @param lang Indica el idioma que prefiere el cliente
	 * @return WebDriver
	 * @throws IllegalStateException Si browser no es un navegador soportado
	 */
	private static WebDriver build(String browser, String headless, String lang) {
		switch (browser) {
			case "chrome":
				return buildChrome(headless, lang);
			case "firefox":
				return buildFirefox(headless, lang);
		}

		throw new IllegalStateException("No es posible construir el browser " + browser);
	}

	/**
	 * @see https://www.selenium.dev/documentation/webdriver/browsers/chrome/
	 * @see https://github.com/GoogleChrome/chrome-launcher/blob/main/docs/chrome-flags-for-tools.md
	 * @param headless Indica el tamaño de la ventana cuando se ejecuta sin modo gráfico
	 * @param lang Indica el idioma que prefiere el cliente
	 * @return ChromeDriver
	 */
	private static WebDriver buildChrome(String headless, String lang) {
		final ChromeOptions options = new ChromeOptions();

		options.addArguments("--lang=" + lang);

		if (!headless.isBlank()) {
			options.addArguments("--headless");
			options.addArguments("--window-size=" + headless);
		}
		else {
			options.addArguments("--start-maximized");
		}

		return new ChromeDriver(options);
	}

	/**
	 * @see https://www.selenium.dev/documentation/webdriver/browsers/firefox/
	 * @param headless Indica el tamaño de la ventana cuando se ejecuta sin modo gráfico
	 * @param lang Indica el idioma que prefiere el cliente
	 * @return FirefoxDriver
	 */
	private static WebDriver buildFirefox(String headless, String lang) {
		final FirefoxOptions options = new FirefoxOptions();
		final FirefoxProfile profile = new FirefoxProfile();

		profile.setPreference("intl.accept_languages", lang);

		if (!headless.isBlank()) {
			final String[] size = headless.split(",");
			options.addArguments("-headless");
			options.addArguments("-width=" + size[0]);
			options.addArguments("-height=" + size[1]);
		}

		options.setProfile(profile);

		return new FirefoxDriver(options);
	}

	/**
	 * Detiene el hilo de ejecución durante w milisegundos
	 * @param w Tiempo en milisegundos
	 */
	private static void waitUntil(long w) {
		if (w > 0) {
			try {
				Thread.sleep(w);
			}
			catch (InterruptedException e) {
			}
		}
	}

}
