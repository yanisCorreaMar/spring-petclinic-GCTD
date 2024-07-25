package org.springframework.samples.petclinic.acceptance;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import io.cucumber.junit.platform.engine.Constants;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("acceptance-features")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME,
		value = "org/springframework/samples/petclinic/acceptance/steps")
@ConfigurationParameter(key = Constants.EXECUTION_DRY_RUN_PROPERTY_NAME, value = "false")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME,
		value = "pretty, html:target/site/cucumber-pretty.html, json:target/cucumber.json")
public class CucumberImplTests {

}
