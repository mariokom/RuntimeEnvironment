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

import java.util.List;
import java.util.Map;

import org.openurc.uch.IProfile;
import org.openurc.uch.ITA;
import org.openurc.uch.ITAListener;
import org.openurc.uch.IUCHStore;
import org.openurc.uch.UCHException;
import org.openurc.uch.UCHNotImplementedException;
/**
 * 
 * @author Lukas Smirek
 *
 */
public class TAListenerTest implements ITAListener {

	@Override
	public void abortSession(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addUriServiceContexts(ITA arg0, String arg1, List<Map<String, IProfile>> arg2) throws UCHException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDocument(String arg0, String arg1, Map<String, IProfile> arg2) throws UCHException {
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
	public List<List<Map<String, List<String>>>> getResources(String arg0, List<Map<String, List<String>>> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, IProfile>> getTargetContexts(String arg0) {
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
	public void removeTA(ITA arg0) throws UCHNotImplementedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeUriServiceContexts(ITA arg0, String arg1, List<Map<String, IProfile>> arg2) throws UCHException {
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionForwardRequest(String arg0, Map<String, String> arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean setValidation(ITA arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String startUriService(ITA arg0, String arg1, int arg2, boolean arg3, String arg4, boolean arg5,
			List<Map<String, IProfile>> arg6) throws UCHException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stopUriService(ITA arg0, String arg1) throws UCHException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateDynRes(List<String> arg0, List<String> arg1, List<Map<String, Object>> arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateValues(List<String> arg0, List<String> arg1, List<String> arg2, List<String> arg3,
			List<Map<String, String>> arg4) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Map<String, String>> uploadResources(List<Map<String, List<String>>> arg0, List<String> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
