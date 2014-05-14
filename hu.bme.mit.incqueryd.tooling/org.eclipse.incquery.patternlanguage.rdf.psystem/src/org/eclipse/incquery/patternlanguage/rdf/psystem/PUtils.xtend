package org.eclipse.incquery.patternlanguage.rdf.psystem

import java.util.List
import org.eclipse.incquery.patternlanguage.patternLanguage.Annotation
import org.eclipse.incquery.patternlanguage.patternLanguage.BoolValue
import org.eclipse.incquery.patternlanguage.patternLanguage.DoubleValue
import org.eclipse.incquery.patternlanguage.patternLanguage.IntValue
import org.eclipse.incquery.patternlanguage.patternLanguage.ListValue
import org.eclipse.incquery.patternlanguage.patternLanguage.ParameterRef
import org.eclipse.incquery.patternlanguage.patternLanguage.StringValue
import org.eclipse.incquery.patternlanguage.patternLanguage.ValueReference
import org.eclipse.incquery.patternlanguage.patternLanguage.Variable
import org.eclipse.incquery.patternlanguage.patternLanguage.VariableReference
import org.eclipse.incquery.patternlanguage.patternLanguage.VariableValue
import org.eclipse.incquery.runtime.matchers.psystem.PBody
import org.eclipse.incquery.runtime.matchers.psystem.PVariable
import org.eclipse.incquery.runtime.matchers.psystem.annotations.PAnnotation
import org.eclipse.incquery.runtime.matchers.psystem.queries.PParameter
import org.eclipse.incquery.runtime.matchers.tuple.FlatTuple
import org.eclipse.incquery.runtime.matchers.tuple.Tuple

class PUtils { // TODO most of this code exists in EPMToBody, move it to generic pattern language project

	static def Variable resolve(VariableReference variableReference) {
		// TODO
	}

	static def PVariable toPVariable(Variable variable, PBody pBody) {
		switch variable {
			ParameterRef: variable.referredParam.toPVariable(pBody)
			default: pBody.getOrCreateVariableByName(variable.name)
		}
	}

	static def PVariable toPVariable(ValueReference valueReference, PBody pBody) {
		// TODO
	}

	static def PAnnotation toPAnnotation(Annotation annotation) {
		new PAnnotation(annotation.name) => [
			for (parameter : annotation.parameters) {
				addAttribute(parameter.name, parameter.value.value)
			}
		]
    }

    static def Object getValue(ValueReference it) {
    	switch it {
    		BoolValue: value
    		DoubleValue: value
    		IntValue: value
    		StringValue: value
    		VariableReference: ^var
    		VariableValue: value.^var
    		ListValue: values.map[value]
    		default: throw new IllegalArgumentException('''Unhandled case «it»''')
    	}
    }

	static def PParameter toPParameter(Variable parameter) {
		new PParameter(parameter.name, parameter.type.typename)
	}

	static def Tuple toTuple(List<ValueReference> valueReferences, PBody pBody) {
        val elements = valueReferences.map[toPVariable(pBody)]
        new FlatTuple(elements)
    }

}