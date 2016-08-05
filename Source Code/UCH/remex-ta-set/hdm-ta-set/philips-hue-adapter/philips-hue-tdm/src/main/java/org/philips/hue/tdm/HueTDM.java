package org.philips.hue.tdm;

/** 
 * @author Marcel Reuss
 * @author Marcel Heisler
 *  */

import java.util.List;
import java.util.Map;

import org.openurc.tdmframework.SuperTDM;
import org.openurc.uch.IProfile;
import org.openurc.uch.ITDM;
import org.openurc.uch.ITDMListener;
import org.openurc.uch.TDMFatalException;
import org.philips.hue.api.HueDiscoveryModule;


public class HueTDM extends SuperTDM implements ITDM, HueDiscoveryModule {
	
	Hue hue;
	HueDiscovery<HueTDM> discovery;
	
	public HueTDM() {
		super();	
	}

	@Override
	public void startDiscovery() throws TDMFatalException {
		discovery.start();
	}

	@Override
	public void stopDiscovery() throws TDMFatalException {
		// Not needed.
	}

	@Override
	public void registerNewTarget() {
		hue.searchBridge();
	}

	/** initialize hue and discovery to new objects and make them known to each other */
	@Override
	public void init(ITDMListener tdmListener, Map<String, String> tdmProps, Map<String, String> uchProps,
			List<Map<String, IProfile>> contexts) throws TDMFatalException {
		super.init(tdmListener, tdmProps, uchProps, contexts);
		hue = new Hue();
		discovery = new HueDiscovery<>(this, this.tdmListener, hue);
		hue.setDiscovery(discovery);
	}
}
