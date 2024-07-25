package org.springframework.samples.petclinic.acceptance.pom;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.acceptance.selenium.Browser;
import org.springframework.stereotype.Service;

@Service
public class Site {

	private final Browser browser;

	public final NavBar navBar;

	public final OwnerList ownerList;

	public final OwnerDetails ownerDetails;

	public final OwnerForm ownerForm;

	@Autowired
	public Site(Browser wb, NavBar n, OwnerList ol, OwnerDetails od, OwnerForm of) {
		this.browser = wb;
		this.navBar = n;
		this.ownerList = ol;
		this.ownerDetails = od;
		this.ownerForm = of;
	}

	/**
	 * Abre el navegador e ingresa a URL
	 * @param url
	 */
	public void open(String url) {
		browser.open(url);
	}

	/**
	 * Cierra el navegador
	 */
	public void close() {
		browser.close();
	}

	/**
	 * Verifica si el título de la ventana es igual al esperado
	 * @param expected Título esperado
	 */
	public void checkTitle(String expected) {
		assertThat(browser.title()).isEqualTo(expected);
	}

}
