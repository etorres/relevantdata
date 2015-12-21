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

import static java.util.Objects.requireNonNull;

import com.google.common.util.concurrent.AbstractIdleService;
import com.typesafe.config.Config;

/**
 * Allows publish-subscribe-style communication between components.
 * @author Erik Torres <etserrano@gmail.com>
 * @since 0.0.1
 */
public abstract class RdService extends AbstractIdleService {

	protected Config config;

	protected RdService(final Config config) {
		setConfig(config);
	}

	/**
	 * Gets the configuration properties.
	 * @return The configuration properties of this service.
	 */
	Config getConfig() {
		return config;
	}

	/**
	 * Sets the configuration properties for this service.
	 * @param config - the configuration to set
	 */
	void setConfig(final Config config) {
		requireNonNull(config, "Valid configuration expected");
		this.config = config;
	}
	
	/**
	 * Reloads (gracefully and hopefully) the service configuration after changing configuration properties.
	 * This operation is unsupported by default. Services where this functionality is required must override
	 * this method.
	 * @param config - the configuration to set
	 */
	void reload(final Config config) {
		throw new UnsupportedOperationException("Service reloading is not supported by default");
	}

}