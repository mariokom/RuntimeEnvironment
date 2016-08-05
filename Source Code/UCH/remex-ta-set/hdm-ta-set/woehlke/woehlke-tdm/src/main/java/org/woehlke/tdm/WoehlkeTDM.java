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


package org.woehlke.tdm;

import java.util.List;
import java.util.Map;

import org.openurc.tdmframework.SuperTDM;
import org.openurc.uch.IProfile;
import org.openurc.uch.ITDM;
import org.openurc.uch.ITDMListener;
import org.openurc.uch.TDMFatalException;

import de.hdm.woehlke.api.DiscoveryModule;
import de.hdm.woehlke.api.WoehlkeConstants;
/**
 * 
 * @author Lukas Smirek
 *
 */
public class WoehlkeTDM extends SuperTDM implements ITDM, DiscoveryModule {
	public Discovery discovery;	
	public WoehlkeTDM() {
		super();
		
	}
	
	
@Override
	public void startDiscovery() throws TDMFatalException {
		
		discovery.start();
		}

	public void stopDiscovery() throws TDMFatalException {
		// TODO Auto-generated method stub
		discovery.stop();
	}

	@Override
	public void registerNewTarget(Map<String, String> targetMap) {
		String ipadr = targetMap.get(WoehlkeConstants.IpAddress);
discovery.discoverWebSteckdose(ipadr,true);		
		
	}

	@Override
	public void init(ITDMListener tdmListener, Map<String, String> tdmProps, Map<String, String> uchProps,
			List<Map<String, IProfile>> contexts) throws TDMFatalException {
		super.init(tdmListener, tdmProps, uchProps, contexts);
discovery = new Discovery(this, this.tdmListener);
	}

	
}
