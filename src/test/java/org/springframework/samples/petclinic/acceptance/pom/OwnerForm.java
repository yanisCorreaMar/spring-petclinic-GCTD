package org.springframework.samples.petclinic.acceptance.pom;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.acceptance.selenium.Browser;
import org.springframework.stereotype.Service;

@Service
public class OwnerForm {

	private static final String FIELD_FIRST_NAME = "id:firstName";

	private static final String FIELD_LAST_NAME = "id:lastName";

	private static final String FIELD_ADDRESS = "id:address";

	private static final String FIELD_CITY = "id:city";

	private static final String FIELD_TELEPHONE = "id:telephone";

	private static final String BTN_ADD_OWNER = "css:#add-owner-form button";

	private static final String FIELD_ERROR_MESSAGE = "xpath://input[@id='%s']/../../span[@class='help-inline']";

	private final Browser browser;

	@Autowired
	public OwnerForm(Browser wb) {
		this.browser = wb;
	}

	public void setFirstName(String value) {
		this.browser.write(FIELD_FIRST_NAME, value);
	}

	public void setLastName(String value) {
		this.browser.write(FIELD_LAST_NAME, value);
	}

	public void setAddress(String value) {
		this.browser.write(FIELD_ADDRESS, value);
	}

	public void setCity(String value) {
		this.browser.write(FIELD_CITY, value);
	}

	public void setTelephone(String value) {
		this.browser.write(FIELD_TELEPHONE, value);
	}

	public void addOwner() {
		this.browser.click(BTN_ADD_OWNER);
	}

	public String getFieldErrorMessage(String field) {
		return this.browser.text(FIELD_ERROR_MESSAGE, labelToId(field));
	}

	public void checkFieldErrorMessage(String field, String expected) {
		assertThat(this.getFieldErrorMessage(field)).contains(expected);
	}

	private static String labelToId(String lbl) {
		final String[] strings = lbl != null ? lbl.split(" ") : new String[] { "" };
		return strings[0].toLowerCase() + Arrays.stream(strings, 1, strings.length).collect(Collectors.joining());
	}

}
