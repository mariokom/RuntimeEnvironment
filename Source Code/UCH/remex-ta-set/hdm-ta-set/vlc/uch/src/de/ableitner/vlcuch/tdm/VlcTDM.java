package de.ableitner.vlcuch.tdm;

import java.util.List;
import java.util.Map;

import org.openurc.tdmframework.SuperTDM;
import org.openurc.uch.IProfile;
import org.openurc.uch.ITDM;
import org.openurc.uch.ITDMListener;
import org.openurc.uch.TDMFatalException;

import de.ableitner.vlcuch.api.DiscoveryModule;
import de.ableitner.vlcuch.api.VlcConstants;

public class VlcTDM extends SuperTDM implements ITDM, DiscoveryModule{
	
	public Discovery discovery;
	
	
	
	public VlcTDM(){
		super();
	}
	
	
	
	
	@Override
	public void startDiscovery() throws TDMFatalException {
		this.discovery.start();
		
	}

	@Override
	public void stopDiscovery() throws TDMFatalException {
		this.discovery.stop();
	}
	

	@Override
	public void registerNewTarget(Map<String, String> targetMap) {
		String ipadr = targetMap.get(VlcConstants.IpAddress);
		discovery.discoverVLC(ipadr,true);			
	}
	
	@Override
	public void init(ITDMListener tdmListener, Map<String, String> tdmProps, Map<String, String> uchProps, List<Map<String, IProfile>> contexts) throws TDMFatalException {
		super.init(tdmListener, tdmProps, uchProps, contexts);
		this.discovery = new Discovery(this, this.tdmListener);
	}

}
