package hu.bme.mit.incqueryd.rete.nodes.helpers;

import hu.bme.mit.incqueryd.rete.comparison.ComparisonOperator;
import hu.bme.mit.incqueryd.rete.comparison.ConditionExpression;
import hu.bme.mit.incqueryd.rete.dataunits.ChangeSet;
import hu.bme.mit.incqueryd.rete.dataunits.ChangeType;
import hu.bme.mit.incqueryd.rete.dataunits.Tuple;
import hu.bme.mit.incqueryd.rete.dataunits.Tuple;
import hu.bme.mit.incqueryd.rete.nodes.data.TermEvaluatorNodeTestData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author szarnyasg
 *
 */
public class TermEvaluatorTestHelper {

    public static TermEvaluatorNodeTestData data1() {
        final Set<Tuple> tuples = new HashSet<>();
        tuples.add(new Tuple(1, 4));
        tuples.add(new Tuple(1, 6));
        tuples.add(new Tuple(3, 4));
        tuples.add(new Tuple(3, 6));
        final ChangeSet changeSet = new ChangeSet(tuples, ChangeType.POSITIVE);

        final Collection<ConditionExpression> conditionExpressions = new HashSet<>();
        // t[0] >= 2
        conditionExpressions.add(new ConditionExpression(0, ComparisonOperator.GREATER_THAN_OR_EQUAL, 2));
        // t[1] <= 5
        conditionExpressions.add(new ConditionExpression(1, ComparisonOperator.LESS_THAN_OR_EQUAL, 5));

        final Set<Tuple> expectedTuples = new HashSet<>();
        expectedTuples.add(new Tuple(3, 4));
        final ChangeSet expectedResults = new ChangeSet(expectedTuples, ChangeType.POSITIVE);      

        final TermEvaluatorNodeTestData data = new TermEvaluatorNodeTestData(changeSet, conditionExpressions, expectedResults);
        return data;
    }
    
}
