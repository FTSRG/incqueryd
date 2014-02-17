package hu.bme.mit.incqueryd.rete.nodes;

import static org.junit.Assert.assertTrue;
import hu.bme.mit.incqueryd.rete.dataunits.ChangeSet;
import hu.bme.mit.incqueryd.rete.nodes.data.AlphaTestData;
import hu.bme.mit.incqueryd.test.util.GsonParser;
import hu.bme.mit.incqueryd.test.util.TestCaseFinder;
import hu.bme.mit.incqueryd.util.RecipeDeserializer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.incquery.runtime.rete.recipes.EqualityFilterRecipe;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * Test cases for the {@link EqualityNode} class.
 * 
 * @author szarnyasg
 * 
 */
public class EqualityNodeTest {

	@Test
	public void test() throws JsonSyntaxException, JsonIOException, IOException {
		final File[] files = TestCaseFinder.getTestCases("filter-test-*.json");

		for (final File testFile : files) {
			final String recipeFile = testFile.getPath().replace("-test-", "-recipe-");
			final Gson gson = GsonParser.getGsonParser();

			final AlphaTestData data = gson.fromJson(new FileReader(testFile), AlphaTestData.class);
			final EqualityFilterRecipe recipe = (EqualityFilterRecipe) (RecipeDeserializer.deserializeFromFile(recipeFile));
			filterEquality(data, recipe);
		}
	}

	public void filterEquality(final AlphaTestData data, final EqualityFilterRecipe recipe) {
		final EqualityNode filterNode = new EqualityNode(recipe);
		final ChangeSet resultChangeSet = filterNode.update(data.getIncomingChangeSet());

		assertTrue(resultChangeSet.equals(data.getExpectedChangeSet()));
	}

}