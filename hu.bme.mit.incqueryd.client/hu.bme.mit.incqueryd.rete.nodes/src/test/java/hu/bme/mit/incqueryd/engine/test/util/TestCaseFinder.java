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
package hu.bme.mit.incqueryd.engine.test.util;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.filefilter.WildcardFileFilter;

public class TestCaseFinder {

	public static File[] getTestCases(final String wildcard) {
		final File dir = new File(TestConstants.TEST_CASES_DIRECTORY);
		final FileFilter fileFilter = new WildcardFileFilter(wildcard);
		final File[] files = dir.listFiles(fileFilter);
		return files;
	}
	
}
