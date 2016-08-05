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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.openurc.uch.IProfile;
import org.openurc.uch.TAException;
import org.openurc.uch.TAFatalException;
import org.openurc.util.NewConstants;
/**
 * 
 * @author Lukas Smirek
 *
 */
public class TAFrameworkTester {
private static Map<String, IProfile> context = null;
private static List<Map<String, IProfile>> contexts = null;
private static Map<String, Object> targetProps;
private static String targetId = "testTargetID";
private static String sessionId = "testSessionId";
private static String socketName = "TestSocket";
private static Map<String, String> clientProps;
private static Map<String, String> uchProps;
private static Map<String, String> taProps;

public static void main(String[] args) throws TAException, TAFatalException, URISyntaxException {
	targetProps	= new HashMap<String, Object>();
	uchProps	= new HashMap<String, String>();
			taProps = new HashMap<>();
			
			
			
			String str = "file:///" + System.getProperty("user.dir").replace("\\", "/") + "/src/test/resources/TestTarget/TestTarget.td";

			System.out.println(str);
			
			targetProps.put(NewConstants.TARGET_PROPS_TD_URI , str);
			
	TAFacadeTest taFacade = new TAFacadeTest();
	taFacade.init(new TAListenerTest(), taProps, uchProps);
	System.out.println("init" );
taFacade.registerTarget(targetId, targetProps, contexts);
System.out.println("registered Taget");

context = new HashMap<>();
clientProps = new HashMap<>();
System.out.println(taFacade.openSessionRequest(targetId, socketName, clientProps, context));
taFacade.sessionOpened(targetId, sessionId, socketName, clientProps, context);
System.out.println("session opened");

List<String> paths = new LinkedList<String>();
List<String> operations = new LinkedList<String>();
List<String> reqValues = new LinkedList<String>();
paths.add("/test/test");
paths.add("/testCommand");

operations.add("S");
		operations.add("S");
				
		reqValues.add("");
		reqValues.add("");

taFacade.setValuesRequest(sessionId, true, paths, operations, reqValues);
System.out.println("vales requst set");
}
}
