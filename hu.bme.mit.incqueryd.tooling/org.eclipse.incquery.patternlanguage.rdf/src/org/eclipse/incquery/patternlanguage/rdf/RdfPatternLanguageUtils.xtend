package org.eclipse.incquery.patternlanguage.rdf

import hu.bme.mit.incqueryd.rdf.RdfUtils
import java.net.URL
import org.eclipse.emf.ecore.EObject
import org.eclipse.incquery.patternlanguage.rdf.rdfPatternLanguage.Iri
import org.eclipse.incquery.patternlanguage.rdf.rdfPatternLanguage.Label
import org.eclipse.incquery.patternlanguage.rdf.rdfPatternLanguage.RdfPatternModel
import org.eclipse.incquery.patternlanguage.rdf.rdfPatternLanguage.TypeId
import org.eclipse.incquery.patternlanguage.rdf.rdfPatternLanguage.Vocabulary
import org.openrdf.model.Model
import org.openrdf.model.Resource
import org.openrdf.model.impl.LiteralImpl
import org.openrdf.model.impl.URIImpl

import static org.eclipse.emf.common.util.URI.*

import static extension org.eclipse.xtext.EcoreUtil2.*

class RdfPatternLanguageUtils {

	static def String asString(TypeId typeId) {
		switch typeId {
			Iri: typeId.asString
			Label: '''with label «typeId.label»'''
		}
	}

	static def String asString(Iri iri) {
		val prefix = iri.prefix?.value ?: iri.getContainerOfType(RdfPatternModel)?.baseIriValue
		'''«prefix»«iri.value»'''
	}

	static def Resource toRdfResource(TypeId typeId) {
		switch typeId {
			Iri: typeId.toRdfResource
			Label: {
				val predicate = new URIImpl("http://www.w3.org/2000/01/rdf-schema#label")
				val object = new LiteralImpl(typeId.label)
				typeId.vocabulary.filter(null, predicate, object).subjects.head
			}
		}
	}
	
	static def Resource toRdfResource(Iri iri) {
		iri.asString.toRdfResource
	}

	static def Resource toRdfResource(String iriString) {
		new URIImpl(iriString)
	}

	static def Model getVocabulary(EObject object) {
		val patternModel = object.getContainerOfType(RdfPatternModel)
		RdfUtils.load(patternModel.vocabularies.map[url].toSet)
	}

	def static getUrl(Vocabulary vocabulary) {
		val uri = createURI(vocabulary.location)
		val containingResource = vocabulary.eResource
		val resolvedUri = if (!uri.relative || (containingResource == null)) {
			uri
		} else {
			uri.resolve(containingResource.URI)
		}
		new URL(resolvedUri.toString)
	}

}