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

package org.grycap.relevantdata.core.test;

import static org.grycap.coreutils.logging.LogManager.getLogManager;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.concurrent.TimeUnit;

import org.grycap.coreutils.test.category.FunctionalTests;
import org.grycap.coreutils.test.rules.TestPrinter;
import org.grycap.coreutils.test.rules.TestWatcher2;
import org.grycap.relevantdata.core.instance.RdFactory;
import org.grycap.relevantdata.core.instance.RdInstance;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.TestRule;

/**
 * Tests boot process.
 * @author Erik Torres <etserrano@gmail.com>
 * @since 0.0.1
 */
@Category(FunctionalTests.class)
public class BootTest {

	@Rule
	public TestPrinter pw = new TestPrinter();

	@Rule
	public TestRule watchman = new TestWatcher2(pw);

	@BeforeClass
	public static void setup() {
		// install bridges to logging APIs in order to capture Hazelcast messages
		getLogManager().init();
	}

	@Test
	public void testBoot() throws Exception {
		// create new instance
		final RdInstance rdInstance = RdFactory.newRelevantdataInstance();
		assertThat("Relevantdata instance was created", rdInstance, notNullValue());

		// await until the services are started or fail with timeout
		rdInstance.awaitHealthy(30l, TimeUnit.SECONDS);

		// await until the services are stopped or fail with timeout
		rdInstance.awaitStopped(30l, TimeUnit.SECONDS);
	}

}