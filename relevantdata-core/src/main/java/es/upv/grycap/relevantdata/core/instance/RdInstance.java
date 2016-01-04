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

import static com.google.common.base.Preconditions.checkState;
import static com.typesafe.config.ConfigRenderOptions.concise;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ServiceManager;
import com.google.common.util.concurrent.ServiceManager.Listener;
import com.typesafe.config.Config;

import es.upv.grycap.coreutils.common.config.Configurer;
import es.upv.grycap.relevantdata.core.events.RdEventbus;
import es.upv.grycap.relevantdata.core.ingestion.RdDataIngestor;

/**
 * Entry point to RelevantData.
 * @author Erik Torres <etserrano@gmail.com>
 * @since 0.0.1
 */
public class RdInstance {

	private static final Logger LOGGER = getLogger(RdInstance.class);

	private Config config;
	private ServiceManager serviceManager;

	/**
	 * Convenient constructor which is available inside the package, allowing factory methods to create new instances of this class,
	 * optionally passing a path to an external file with the configuration properties.
	 * @param confname - configuration filename
	 */
	RdInstance(final @Nullable String confname) {
		// load configuration properties
		config = new Configurer().loadConfig(confname, "relevantdata");
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace(config.root().render());
		} else {
			LOGGER.info("Configuration: " + config.getObject("relevantdata").render(concise()));
		}
		// create services
		final List<RdService> services = config.getStringList("relevantdata.services").stream()
				.map(StringUtils::trimToNull).filter(Objects::nonNull).collect(Collectors.toSet()).stream()
				.map(this::newService).collect(Collectors.toList());
		checkState(services != null && !services.isEmpty(), "At least one service expected");
		serviceManager = new ServiceManager(services);
		serviceManager.addListener(new Listener() {
			@Override
			public void healthy() {
				final double startupTime = serviceManager.startupTimes().entrySet().stream().mapToDouble(Map.Entry::getValue).sum();
				LOGGER.info("Services started in: " + ((long)startupTime/1000l) + " seconds.");
			}
		}, MoreExecutors.directExecutor());
		// add termination logic
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					serviceManager.stopAsync().awaitStopped(5, TimeUnit.SECONDS);
					LOGGER.info("Service manager was stopped.");
				} catch (TimeoutException timeout) {
					LOGGER.info("Stopping timed out.");
				}
			}
		});
		// start services
		serviceManager.startAsync();
	}

	/**
	 * Description copied from the method {@link ServiceManager#awaitHealthy(long, TimeUnit)}: Waits for this instance to become 
	 * {@link ServiceManager#isHealthy() healthy} for no more than the given time.
	 * @param timeout - the maximum time to wait
	 * @param unit - the time unit of the timeout argument
	 * @throws TimeoutException if not all of the services have finished starting within the deadline
	 */
	public void awaitHealthy(final long timeout, final TimeUnit unit) throws TimeoutException {
		serviceManager.awaitHealthy(timeout, unit);
	}

	/**
	 * Description copied from the method {@link ServiceManager#awaitStopped(long, TimeUnit)}: Waits for the all the services to 
	 * reach a terminal state for no more than the given time.
	 * @param timeout - the maximum time to wait
	 * @param unit - the time unit of the timeout argument
	 * @throws TimeoutException if not all of the services have stopped within the deadline
	 */
	public void awaitStopped(final long timeout, final TimeUnit unit) throws TimeoutException {
		serviceManager.stopAsync().awaitStopped(timeout, unit);
	}

	private RdService newService(final String name) {
		RdService service = null;
		switch (name) {
		case "eventbus":
			service = new RdEventbus(config);
			break;
		case "ingestion":
			service = new RdDataIngestor(config);
			break;
		default:
			throw new IllegalArgumentException("Unknown service: " + name);
		}
		return service;
	}

}