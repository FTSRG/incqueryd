/*
 * generated by Xtext
 */
package org.eclipse.incquery.patternlanguage.rdf;

import org.eclipse.incquery.patternlanguage.rdf.conversion.RdfPatternLanguageValueConverterService;
import org.eclipse.xtext.conversion.IValueConverterService;

import com.google.inject.Binder;

/**
 * Use this class to register components to be used at runtime / without the
 * Equinox extension registry.
 */
public class RdfPatternLanguageRuntimeModule extends org.eclipse.incquery.patternlanguage.rdf.AbstractRdfPatternLanguageRuntimeModule {

	@Override
	public Class<? extends IValueConverterService> bindIValueConverterService() {
		return RdfPatternLanguageValueConverterService.class;
	}

	@Override
	public void configure(Binder binder) {
		super.configure(binder);
//		binder.bind(ITypeProvider.class).to(XbaseBatchTypeProvider.class); // XXX IncQuery workaround to avoid "No implementation for org.eclipse.xtext.xbase.typing.ITypeProvider was bound"
	}

}
