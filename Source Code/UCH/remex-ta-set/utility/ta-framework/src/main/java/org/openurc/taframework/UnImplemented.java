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
/**
 * @author Lukas Smirek 
 */
import java.util.Map;

import org.openurc.uch.TAException;
import org.openurc.uch.TAFatalException;
import org.openurc.uch.TANotImplementedException;

public class UnImplemented {
	public Map<String, String> getLocators(String targetName) {
		return null;
	}
	
	public void finalize() {
		
	}
	public void sessionResumed(String sessionId) throws TAException,
	TAFatalException, TANotImplementedException {
throw new TANotImplementedException();
}

}
