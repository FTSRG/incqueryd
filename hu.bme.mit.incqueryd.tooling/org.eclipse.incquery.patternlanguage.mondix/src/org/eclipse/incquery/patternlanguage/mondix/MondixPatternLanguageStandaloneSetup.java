/*
* generated by Xtext
*/
package org.eclipse.incquery.patternlanguage.mondix;

/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class MondixPatternLanguageStandaloneSetup extends MondixPatternLanguageStandaloneSetupGenerated{

	public static void doSetup() {
		new MondixPatternLanguageStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}
