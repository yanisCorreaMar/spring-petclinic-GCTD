package org.springframework.samples.petclinic.acceptance.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.samples.petclinic.acceptance.pom.Site;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OwnerSteps {

	private static String OWNER_FORM_TITLE = "Owner";

	private static String OWNER_DETAILS_TITLE = "Owner Information";

	@LocalServerPort
	protected int port;

	@Value("${sut.host:http://localhost:%d/}")
	private String host;

	@Autowired
	private Site site;

	String getBaseURL() {
		return String.format(this.host, this.port);
	}

	@Before
	public void before() {
		site.open(getBaseURL());
		site.checkTitle("PetClinic :: a Spring Framework demonstration");
	}

	@After
	public void after() {
		site.close();
	}

	/**
	 * Se dirige al formulario de alta de propietario
	 */

	@Given("el veterinario está en la página de registro de dueños de mascotas")
	public void goToFormAddOwner() {
		site.navBar.findOwners();
		site.ownerList.addOwner();
		site.navBar.checkTitle(OWNER_FORM_TITLE);
	}

	/**
	 * Carga los campos de datos en el formulario
	 */

	@When("el veterinario ingresa {string} en el campo First Name")
	public void setFirstName(String firstname) {
		site.ownerForm.setFirstName(firstname);
	}

	@When("el veterinario ingresa {string} en el campo Last Name")
	public void setLastName(String lastname) {
		site.ownerForm.setLastName(lastname);
	}

	@When("el veterinario ingresa {string} en el campo Address")
	public void setAddress(String address) {
		site.ownerForm.setAddress(address);
	}

	@When("el veterinario ingresa {string} en el campo City")
	public void setCity(String city) {
		site.ownerForm.setCity(city);
	}

	@When("el veterinario ingresa {string} en el campo Telephone")
	public void setTelephone(String telephone) {
		site.ownerForm.setTelephone(telephone);
	}

	@When("el veterinario hace clic en el botón Add Owner")
	public void addOwner() {
		site.ownerForm.addOwner();
	}

	/**
	 * Mensaje de error bajo un campo del formulario
	 */
	@Then("el sistema debería mostrar bajo el campo {string} el mensaje de error {string}")
	public void checkFieldErrorMessage(String field, String errorMessage) {
		site.ownerForm.checkFieldErrorMessage(field, errorMessage);
	}

	/**
	 * Mensaje de confirmación sobre los datos, y tabla de datos que fueron ingresados
	 */
	@Then("el sistema debería mostrar el mensaje {string} y los datos del dueño agregado")
	public void checkSuccessMessage(String successMessage) {
		site.ownerDetails.checkSuccessMessage(successMessage);
	}

	@Then("muestra el texto {string} en el campo Name")
	public void checkName(String fullname) {
		site.ownerDetails.checkName(fullname);
	}

	@Then("muestra el texto {string} en el campo Address")
	public void checkAddress(String address) {
		site.ownerDetails.checkAddress(address);
	}

	@Then("muestra el texto {string} en el campo City")
	public void checkCity(String city) {
		site.ownerDetails.checkCity(city);
	}

	@Then("muestra el texto {string} en el campo Telephone")
	public void checkTelephone(String telephone) {
		site.ownerDetails.checkTelephone(telephone);
	}

	@Given("el veterinario está en la página de búsqueda de dueños de mascotas")
	public void goToFindOwner() {
		site.navBar.findOwners();
	}

	@When("el veterinario hace clic en el botón Find Owner")
	public void findOwner() {
		site.ownerList.findOwner();
	}

	@Then("el sistema debería dirigir a la pantalla de detalles del dueño de mascota")
	public void owmerInfoCheckTitle() {
		site.navBar.checkTitle(OWNER_DETAILS_TITLE);
	}

}
