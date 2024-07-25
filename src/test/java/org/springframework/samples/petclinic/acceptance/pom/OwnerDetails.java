package org.springframework.samples.petclinic.acceptance.pom;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.acceptance.selenium.Browser;
import org.springframework.stereotype.Service;

@Service
public class OwnerDetails {

	private static final String SUCCESS_MESSAGE = "css:#success-message span";

	private static final String CELL_WITH_VALUE = "xpath://table/tbody/tr/th[text()='%s']/following-sibling::td";

	private static final String NAME_ROW_TITLE = "Name";

	private static final String ADDRESS_ROW_TITLE = "Address";

	private static final String CITY_ROW_TITLE = "City";

	private static final String TELEPHONE_ROW_TITLE = "Telephone";

	private final Browser browser;

	@Autowired
	public OwnerDetails(Browser wb) {
		this.browser = wb;
	}

	public String getSuccessMessage() {
		return this.browser.text(SUCCESS_MESSAGE);
	}

	public void checkSuccessMessage(String expected) {
		assertThat(this.getSuccessMessage()).isEqualTo(expected);
	}

	public String getName() {
		return this.browser.text(CELL_WITH_VALUE, NAME_ROW_TITLE);
	}

	public void checkName(String expected) {
		assertThat(this.getName()).isEqualTo(expected);
	}

	public String getAddress() {
		return this.browser.text(CELL_WITH_VALUE, ADDRESS_ROW_TITLE);
	}

	public void checkAddress(String expected) {
		assertThat(this.getAddress()).isEqualTo(expected);
	}

	public String getCity() {
		return this.browser.text(CELL_WITH_VALUE, CITY_ROW_TITLE);
	}

	public void checkCity(String expected) {
		assertThat(this.getCity()).isEqualTo(expected);
	}

	public String getTelephone() {
		return this.browser.text(CELL_WITH_VALUE, TELEPHONE_ROW_TITLE);
	}

	public void checkTelephone(String expected) {
		assertThat(this.getTelephone()).isEqualTo(expected);
	}

}
