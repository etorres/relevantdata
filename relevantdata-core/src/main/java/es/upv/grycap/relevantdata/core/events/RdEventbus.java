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

package es.upv.grycap.relevantdata.core.events;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.typesafe.config.Config;

import es.upv.grycap.relevantdata.core.instance.RdService;

/**
 * Allows publish-subscribe-style communication between components.
 * @author Erik Torres <etserrano@gmail.com>
 * @since 0.0.1
 */
public class RdEventbus extends RdService {

	private static final Logger LOGGER = getLogger(RdEventbus.class);

	private HazelcastInstance hazelcast;	

	public RdEventbus(final Config config) {
		super(config);
	}

	@Override
	protected void startUp() throws Exception {
		// configure and start the in-memory datagrid
		final com.hazelcast.config.Config hazelcastConfig = new com.hazelcast.config.Config();
		hazelcastConfig.setInstanceName("relevantdata-cluster");
		final NetworkConfig hazelcastNetwork = hazelcastConfig.getNetworkConfig();
		hazelcastNetwork.setPublicAddress(config.getString("relevantdata.cluster.public-address"));
		final JoinConfig hazelcastJoin = hazelcastNetwork.getJoin();
		hazelcastJoin.getMulticastConfig().setEnabled(false);
		hazelcastJoin.getTcpIpConfig().addMember(config.getString("relevantdata.cluster.network")).setEnabled(true);
		hazelcastNetwork.getInterfaces().addInterface(config.getString("relevantdata.cluster.network")).setEnabled(true);
		final GroupConfig hazelcastGroup = hazelcastConfig.getGroupConfig();
		hazelcastGroup.setName(config.getString("relevantdata.cluster.name")).setPassword(config.getString("relevantdata.cluster.secret"));
		hazelcast = Hazelcast.newHazelcastInstance(hazelcastConfig);
		LOGGER.info("Startup succeeded.");
	}

	@Override
	protected void shutDown() throws Exception {
		Hazelcast.shutdownAll();
		LOGGER.info("Shutdown succeeded.");
	}

	/*
		// TODO

	    Map<Integer, String> customers = hazelcast.getMap( "customers" );
	    customers.put( 1, "Joe" );
	    customers.put( 2, "Ali" );
	    customers.put( 3, "Avi" );

	    System.out.println( "Customer with key 1: " + customers.get(1) );
	    System.out.println( "Map Size:" + customers.size() );

	    Queue<String> queueCustomers = hazelcast.getQueue( "customers" );
	    queueCustomers.offer( "Tom" );
	    queueCustomers.offer( "Mary" );
	    queueCustomers.offer( "Jane" );
	    System.out.println( "First customer: " + queueCustomers.poll() );
	    System.out.println( "Second customer: "+ queueCustomers.peek() );
	    System.out.println( "Queue size: " + queueCustomers.size() );

	    // TODO
	 */
}