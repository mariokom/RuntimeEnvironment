package de.ableitner.vlcuch.ta;

import org.openurc.taframework.SuperTAFacade;

public class VlcTAFacade extends SuperTAFacade<VlcTA>{

	
	public VlcTAFacade() {
		super("VlcTAFacade");
	}

	@Override
	public VlcTA createSpezificTA(SuperTAFacade<VlcTA> facade) {
		return new VlcTA(facade);
	}

}
