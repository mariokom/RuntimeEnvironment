/*
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

*/


package org.openurc.taframework;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import edu.wisc.trace.uch.util.LoggerUtil;
import edu.wisc.trace.uch.util.Session;
/**
 * 
 * @author LukasSmirek
 *
 */
public class Updater {
	public Logger logger = LoggerUtil.getSdkLogger();
	Map<Set<Session>,Sequenze> sessionSet;
	private SuperTAFacade taFacade;
	
	public Updater(SuperTAFacade taFacade) {
		sessionSet = new HashMap<Set<Session>,Sequenze>();
	this.taFacade = taFacade; 
	}


		public void add(Session session, String path, String string, String value, Map<String, String> prop) {
		Set<Session> s = new HashSet<Session>();
		s.add(session);
		add(s, path, string, value,prop);
	}


	public void add(Set<Session> sessions, String path, String operation, String value,Map<String,String> props) {
		Sequenze sequenze		 = null;
		if ((sequenze = sessionSet.get(sessions)) == null){
			sequenze = new Sequenze();
		}
sequenze.paths.add(path);
sequenze.operations.add(operation);
sequenze.values.add(value);
			sequenze.props.add(props);
		sessionSet.put(sessions, sequenze);	
		}

	public void sendUpdates() {
logger.info("sessionSet" + sessionSet   );
		int i = 0;
		for (Set<Session> s : sessionSet.keySet()){
logger.info("" + i);
			Sequenze seq = sessionSet.get(s);
		taFacade.sendUpdatedValues(s, seq.paths, seq.operations, seq.values, seq.props);	
		}
		
	}
	
}