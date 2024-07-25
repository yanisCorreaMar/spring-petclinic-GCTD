package org.springframework.samples.petclinic.acceptance.pom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.acceptance.selenium.Browser;
import org.springframework.stereotype.Service;

@Service
public class OwnerList {

	private static String FIELD_FIND_BY_NAME = "id:lastName";

	private static String BTN_FIND_OWNER = "xpath://button[contains(normalize-space(text()), 'Find Owner')]";

	private static String BTN_ADD_OWNER = "xpath://a[contains(normalize-space(text()), 'Add Owner')]";

	private final Browser browser;

	@Autowired
	public OwnerList(Browser wb) {
		this.browser = wb;
	}

	public void setLastNameToFind(String value) {
		this.browser.write(FIELD_FIND_BY_NAME, value);
	}

	public void findOwner() {
		this.browser.click(BTN_FIND_OWNER);
	}

	public void addOwner() {
		this.browser.click(BTN_ADD_OWNER);
	}

}
