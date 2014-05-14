package org.eclipse.incquery.patternlanguage.rdf.psystem

import org.eclipse.incquery.patternlanguage.patternLanguage.CompareConstraint
import org.eclipse.incquery.patternlanguage.patternLanguage.Constraint
import org.eclipse.incquery.patternlanguage.patternLanguage.Pattern
import org.eclipse.incquery.patternlanguage.patternLanguage.PatternCompositionConstraint
import org.eclipse.incquery.patternlanguage.patternLanguage.Variable
import org.eclipse.incquery.patternlanguage.rdf.rdfPatternLanguage.RdfCheckConstraint
import org.eclipse.incquery.patternlanguage.rdf.rdfPatternLanguage.RdfClass
import org.eclipse.incquery.patternlanguage.rdf.rdfPatternLanguage.RdfClassConstraint
import org.eclipse.incquery.patternlanguage.rdf.rdfPatternLanguage.RdfProperty
import org.eclipse.incquery.patternlanguage.rdf.rdfPatternLanguage.RdfPropertyConstraint
import org.eclipse.incquery.runtime.matchers.psystem.PBody
import org.eclipse.incquery.runtime.matchers.psystem.PConstraint
import org.eclipse.incquery.runtime.matchers.psystem.basicdeferred.Equality
import org.eclipse.incquery.runtime.matchers.psystem.basicdeferred.Inequality
import org.eclipse.incquery.runtime.matchers.psystem.basicdeferred.NegativePatternCall
import org.eclipse.incquery.runtime.matchers.psystem.basicenumerables.BinaryTransitiveClosure
import org.eclipse.incquery.runtime.matchers.psystem.basicenumerables.PositivePatternCall
import org.eclipse.incquery.runtime.matchers.psystem.basicenumerables.TypeBinary
import org.eclipse.incquery.runtime.matchers.psystem.basicenumerables.TypeUnary
import org.eclipse.incquery.runtime.matchers.psystem.queries.PQuery

import static org.eclipse.incquery.patternlanguage.patternLanguage.CompareFeature.*

import static extension org.eclipse.incquery.patternlanguage.rdf.psystem.PUtils.*

class RdfPConstraint {

	static def PConstraint create(Constraint constraint, PBody pBody, RdfPatternMatcherContext context) {
		switch constraint {
			PatternCompositionConstraint: {
				createPatternCompositionConstraint(constraint, pBody)
			}
			CompareConstraint: {
				createCompareConstraint(constraint, pBody)
			}
			RdfClassConstraint: {
				createClassConstraint(constraint, pBody, context)
			}
			RdfPropertyConstraint: {
				createPropertyConstraint(constraint, pBody, context)
			}
			RdfCheckConstraint: {
				createCheckConstraint(constraint)
			}
			default: throw new IllegalArgumentException('''Unhandled case «constraint»''')
		}
	}

	static def PConstraint createPatternCompositionConstraint(PatternCompositionConstraint constraint, PBody pBody) { // based on EPMToPBody
		val call = constraint.call
        val patternRef = call.patternRef
        val calledQuery = findQuery(patternRef)
        val tuple = call.parameters.toTuple(pBody)
        if (!call.transitive) {
            if (constraint.negative) {
                new NegativePatternCall(pBody, tuple, calledQuery)
            } else {
				new PositivePatternCall(pBody, tuple, calledQuery)
			}
        } else {
            if (tuple.size != 2) {
                throw new RuntimeException("Transitive closure only supported for binary patterns")
            } else if (constraint.negative) {
                throw new RuntimeException("Unsupported negated transitive closure")
            } else {
                new BinaryTransitiveClosure(pBody, tuple, calledQuery)
            }
        }
	}

	static def PQuery findQuery(Pattern pattern) {
		// TODO
	}

	static def PConstraint createCompareConstraint(CompareConstraint constraint, PBody pBody) {
		val left = constraint.leftOperand.toPVariable(pBody)
        val right = constraint.rightOperand.toPVariable(pBody)
        switch (constraint.feature) {
			case EQUALITY: new Equality(pBody, left, right)
			case INEQUALITY: new Inequality(pBody, left, right, false)
		}
	}

	static def TypeUnary createClassConstraint(RdfClassConstraint constraint, PBody pBody, RdfPatternMatcherContext context) {
		val Variable variable = constraint.variable.resolve
		variable.toTypeConstraint(pBody, context)
	}

	static def PConstraint createPropertyConstraint(RdfPropertyConstraint constraint, PBody pBody, RdfPatternMatcherContext context) {
		switch refType : constraint.refType {
			RdfProperty: {
				val source = constraint.source.resolve.toPVariable(pBody)
				val target = constraint.target.toPVariable(pBody)
				val typeObject = refType.property
				val typeString = context.printType(typeObject)
				new TypeBinary(pBody, context, source, target, typeObject, typeString)
			}
			default: throw new IllegalArgumentException('''Constraint's reference must be «RdfProperty»''')
		}
	}

	static def PConstraint createCheckConstraint(RdfCheckConstraint constraint) {
		// TODO
	}

	static def TypeUnary toTypeConstraint(Variable parameter, PBody pBody, RdfPatternMatcherContext context) {
		switch type : parameter.type {
			RdfClass: {
				val pVariable = parameter.toPVariable(pBody)
				new TypeUnary(pBody, pVariable, type, context.printType(type))
			}
			default: throw new IllegalArgumentException('''Parameter's type must be «RdfClass»''')
		}
	}

}