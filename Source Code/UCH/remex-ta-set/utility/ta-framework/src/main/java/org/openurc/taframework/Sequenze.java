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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author Lukas Smirek
 * nt
 * 
 */
public class Sequenze {
	List<String> paths;
	List<String> operations;
	List<String> values;
	List<Map<String,String>> props;

	public Sequenze() {
		paths = new LinkedList<String>();
		operations = new LinkedList<String>();
		values = new LinkedList<String>();
		props = new LinkedList<Map<String,String>>();
	}
		public void add(String path, String operation, String value) {
			paths.add(path);
			operations.add(operation);
			values.add(value);
			
		}

	

}
