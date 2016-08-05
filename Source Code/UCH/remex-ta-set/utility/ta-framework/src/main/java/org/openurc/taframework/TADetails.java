/*
Copyright 2006-2013  Board of Regents of the University of Wisconsin System (Univ. of Wisconsin-Madison, Trace R&D Center).  
Copyright 2015 Hochschule der Medien (HDM) / Stuttgart Media University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 

"Parts of this program were based on software developed by the Trace Center, University of Wisconsin-Madison under funding from NIDRR / US Dept of Education."

*/
package org.openurc.taframework;

import java.util.Map;

import edu.wisc.trace.uch.util.Session;
import edu.wisc.trace.uch.util.socket.TargetDescription;


/**
 * Maintain Details related to TargetAdapter like targetId, Target Properties, Target Description etc.
 * Also provide methods to retrieve these properties.
 *  
 * @author Parikshit Thakur & Team, Trace R&D Center, extended by Lukas Smirek
 * @version $Revision: 808 $
 */

class TADetails<TA extends SuperTA > {

private String targetId;
	
	private Map<String, Object> targetProps;
	
	private TargetDescription targetDescription;
	
	private TA e_handler;

	private Map<String, Session> socketNameSessionMap;
	/**
	 * 
	 * Assign reference of targetId, Target Properties, Target Description,
	 * 
	 * @param targetId a String value of TargetId
	 * @param targetProps a Map&ltString, Object;&gt; of targetProperties
	 * @param targetDescription an object of TargetDescription
	 * @param thermostatTA  an object of the ThermostatTA
	 */
	TADetails(String targetId, Map<String, Object> targetProps,TargetDescription targetDescription, TA e_handler) {
		
		this.targetId = targetId;
		this.targetProps = targetProps;
		this.targetDescription = targetDescription;
		this.e_handler = e_handler;
		
	}

	
	/**
	 * Get the Object of TargetDescription.
	 * 
	 * @return an Object of TargetDescription
	 */
	TargetDescription getTargetDescription() {
		return targetDescription;
	}

	
	/**
	 * Get a String value of targetId.
	 * 
	 * @return a String of targetId
	 */
	String getTargetId() {
		return targetId;
	}

	
	/**
	 * Get the Map&lt;String, String&gt; of target Properties.
	 * 
	 * @return a Map&lt;String, String&gt; of target Properties
	 */
	Map<String, Object> getTargetProps() {
		return targetProps;
	}

	/**
	 * Get an Object of ThermostatTA.
	 * 
	 * @return an Object of ThermostatTA 
	 */
	public TA getTA() {
		return e_handler;
	}

	}
