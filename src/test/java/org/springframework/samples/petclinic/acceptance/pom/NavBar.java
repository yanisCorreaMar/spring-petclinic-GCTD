package org.springframework.samples.petclinic.acceptance.pom;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.acceptance.selenium.Browser;
import org.springframework.stereotype.Service;

@Service
public class NavBar {

	private static final String MENU_HOME = "xpath://a[@title=\"home page\"]";

	private static final String MENU_FIND_OWNERS = "xpath://a[@title=\"find owners\"]";

	private static final String MENU_VETERINARIANS = "xpath://a[@title=\"veterinarians\"]";

	private static final String MENU_ERROR = "xpath://a[@title=\"trigger a RuntimeException to see how it is handled\"]";

	private static final String PAGE_TITLE = "css:.container-fluid h2";

	private final Browser browser;

	@Autowired
	public NavBar(Browser wb) {
		this.browser = wb;
	}

	public void findOwners() {
		this.browser.click(MENU_FIND_OWNERS);
	}

	public void veterinarians() {
		this.browser.click(MENU_VETERINARIANS);
	}

	public void error() {
		this.browser.click(MENU_ERROR);
	}

	public void home() {
		this.browser.click(MENU_HOME);
	}

	/**
	 * Verifica si el título del contenido de la página es igual al esperado
	 * @param expected Título
	 */
	public void checkTitle(String expected) {
		assertThat(browser.text(PAGE_TITLE)).isEqualTo(expected);
	}

}
