package hu.bme.mit.incqueryd.rete.nodes.helpers;

import hu.bme.mit.incqueryd.rete.dataunits.Tuple;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonParser {
	public static Gson getGsonParser() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Tuple.class, new TupleDeserializer());
		Gson gson = gsonBuilder.create();
		return gson;
	}
}
