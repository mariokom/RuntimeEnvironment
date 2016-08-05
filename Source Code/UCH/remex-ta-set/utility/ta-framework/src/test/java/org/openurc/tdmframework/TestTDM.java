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


package org.openurc.tdmframework;
import java.util.HashMap;

import org.openurc.tdmframework.SuperTDM;
import org.openurc.uch.TDMFatalException;

/**
 * 
 * @author Lukas Smirek
 *
 */
public class TestTDM extends SuperTDM {
public TestTDM(){

	this.uchProps = new HashMap<String, String>();
	uchProps.put("http://purl.org/dc/terms/conformsTo", "http://openurc.org/TR/uch1.0-20121022/"   );
}

@Override
public void startDiscovery() throws TDMFatalException {
	// TODO Auto-generated method stub
	
}

@Override
public void stopDiscovery() throws TDMFatalException {
	// TODO Auto-generated method stub
	
}
}
