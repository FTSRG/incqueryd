/*******************************************************************************
 * Copyright (c) 2010-2014, Gabor Szarnyas, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Gabor Szarnyas - initial API and implementation
 *******************************************************************************/
package hu.bme.mit.incqueryd.core.test.util;

import hu.bme.mit.incqueryd.core.rete.dataunits.Tuple;

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