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
import java.util.List;
import java.util.Map;

import org.openurc.uch.IProfile;
import org.openurc.uch.ITDM;
import org.openurc.uch.ITDMListener;
import org.openurc.uch.IUCHStore;
import org.openurc.uch.UCHException;
import org.openurc.uch.UCHNotImplementedException;
import static org.junit.Assert.assertEquals;
/**
 * @author Lukas Smirek 
 */
public class TestITDMListener implements ITDMListener {

	private Map<String, String> props;

	public TestITDMListener(Map<String, String> props) {
		System.out.println("construct: " + props);
		this.props = props;
	}

	@Override
	public void addUriServiceContexts(ITDM arg0, String arg1,
			List<Map<String, IProfile>> arg2) throws UCHException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Map<String, IProfile>> getContexts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDocument(String arg0, String arg1,
			Map<String, IProfile> arg2) throws UCHException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIpAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IUCHStore getLocalUCHStore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<List<Map<String, List<String>>>> getResources(String arg0,
			List<Map<String, List<String>>> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getUCHProps() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isImplemented(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeTDM(ITDM arg0) throws UCHNotImplementedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeUriServiceContexts(ITDM arg0, String arg1,
			List<Map<String, IProfile>> arg2) throws UCHException {
		// TODO Auto-generated method stub

	}

	@Override
	public String startUriService(ITDM arg0, String arg1, int arg2,
			boolean arg3, String arg4, boolean arg5,
			List<Map<String, IProfile>> arg6) throws UCHException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stopUriService(ITDM arg0, String arg1) throws UCHException {
		// TODO Auto-generated method stub

	}

	@Override
	public void targetContextsAdded(String arg0,
			List<Map<String, IProfile>> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void targetContextsRemoved(String arg0,
			List<Map<String, IProfile>> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void targetDiscarded(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public String targetDiscovered(ITDM arg0, Map<String, Object> targetProps,
			Map<String, String> taProps, List<Map<String, IProfile>> arg3)  {
		System.out.println("New target discovered");
		System.out.println("props:   " + props);
		
		System.out.println("\n taProps, .rprop");
		
		for (String k : taProps.keySet()){
			System.out.print(k+ " ");
			
			System.out.println(taProps.get(k).equals(props.get(k) ));
//			System.out.println(taProps.get(k) + " " + props.get(k) );
//			assertEquals(taProps.get(k), props.get(k) );
			
		}
		System.out.println("All props from the taProp are contained in the .rprop file");
		
		for (String k : targetProps.keySet()){
	System.out.print(k+ " ");
	
	if (k.contains("instanceId"))
	continue;
	
	
			System.out.println(targetProps.get(k).equals(props.get(k) ));
//		
		}
		
		return null;
	}

	@Override
	public List<Map<String, String>> uploadResources(
			List<Map<String, List<String>>> arg0, List<String> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
