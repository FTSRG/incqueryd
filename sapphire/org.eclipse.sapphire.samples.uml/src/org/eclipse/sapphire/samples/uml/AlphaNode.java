/******************************************************************************
 * Copyright (c) 2013 Oracle
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Konstantin Komissarchik - initial implementation and ongoing maintenance
 ******************************************************************************/

package org.eclipse.sapphire.samples.uml;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ElementList;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.ListProperty;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.DefaultValue;
import org.eclipse.sapphire.modeling.annotations.EnumSerialization;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Required;
import org.eclipse.sapphire.modeling.annotations.Type;

/**
 * @author <a href="mailto:konstantin.komissarchik@oracle.com">Konstantin
 *         Komissarchik</a>
 */

public interface AlphaNode extends Element {
	ElementType TYPE = new ElementType(AlphaNode.class);

	// *** Name ***

	@Label(standard = "name")
	@Required
	ValueProperty PROP_NAME = new ValueProperty(TYPE, "Name");

	Value<String> getName();

	void setName(String value);

	// *** Kind ***

	enum Kind {
		@Label(standard = "trimmer")
		@EnumSerialization(primary = "trimmer")
		TRIMMER,

		@Label(standard = "predicateevaluator")
		@EnumSerialization(primary = "predicateevaluator")
		PREDICATEEVALUATOR,
		
		@Label(standard = "equality")
		@EnumSerialization(primary = "equality")
		EQUALITY,
		
		@Label(standard = "inequality")
		@EnumSerialization(primary = "inequality")
		INEQUALITY,		
	}

	@Type(base = Kind.class)
	@Label(standard = "kind")
	@DefaultValue(text = "")
	ValueProperty PROP_KIND = new ValueProperty(TYPE, "Kind");

	Value<Kind> getKind();

	void setKind(String value);

	void setKind(Kind value);

	// *** Fields ***

	@Type(base = Field.class)
	@Label(standard = "fields")
	ListProperty PROP_FIELDS = new ListProperty(TYPE, "Fields");

	ElementList<Field> getFields();

	// *** Methods ***

	@Type(base = Method.class)
	@Label(standard = "methods")
	ListProperty PROP_METHODS = new ListProperty(TYPE, "Methods");

	ElementList<Method> getMethods();

	// *** ExtendedEntities ***

	@Type(base = AlphaNodeRef.class)
	@Label(standard = "extended entities")
	ListProperty PROP_EXTENDED_NODES = new ListProperty(TYPE,
			"ExtendedNodes");

	ElementList<AlphaNodeRef> getExtendedNodes();

}