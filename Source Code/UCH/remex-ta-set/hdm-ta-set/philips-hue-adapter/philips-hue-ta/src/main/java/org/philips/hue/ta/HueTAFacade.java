package org.philips.hue.ta;
/** 
 * @author Marcel Reuss
 * @author Marcel Heisler
 **/
import org.openurc.taframework.SuperTAFacade;

public class HueTAFacade extends SuperTAFacade<HueTA> {

	public HueTAFacade(String taName) {
		super(taName);
	}

	public HueTAFacade() {
		super("HueTA");
	}

	/**
	 * This method is called whenever a new session is opened. It instantiates a
	 * new TA object. 
	 * @param superTaFacade makes the calling TAFacade
	 * known to the TA implementation.
	 */
	@Override
	public HueTA createSpezificTA(SuperTAFacade<HueTA> facade) {
		return new HueTA(facade);
	}

}
