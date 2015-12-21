/*
 * Relevant Data - Core Components.
 * Copyright 2015-2016 GRyCAP (Universitat Politecnica de Valencia)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * 
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 */

package es.upv.grycap.relevantdata.core.instance;

import javax.annotation.Nullable;

/**
 * RelevantData factory.
 * @author Erik Torres <etserrano@gmail.com>
 * @since 0.0.1
 */
public final class RdFactory {

	/**
	 * Creates a new instance of RelevantData using the default configuration.
	 * @return A new instance of RelevantData configured with the default properties.
	 */
	public static RdInstance newRelevantdataInstance() {
		return new RdInstance(null);
	}

	/**
	 * Creates a new instance of RelevantData, optionally loading the configuration properties from an external
	 * configuration file. When no file is provided (<tt>null</tt> value), the default configuration is used.
	 * @param confname - configuration filename
	 * @return A new instance of RelevantData configured with the properties loaded from an external configuration file.
	 */
	public static RdInstance newRelevantdataInstance(final @Nullable String confname) {
		return new RdInstance(confname);
	}
	
}