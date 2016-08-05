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


package lusm.woehlke.ta;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.openurc.taframework.SuperTA;
import org.openurc.taframework.SuperTAFacade;
import org.openurc.uch.IProfile;
import org.openurc.uch.ITA;
import org.openurc.uch.TAException;
import org.openurc.uch.TAFatalException;
import org.openurc.uch.TANotImplementedException;

import edu.wisc.trace.uch.util.LoggerUtil;
import edu.wisc.trace.uch.util.socket.TargetDescription;
import lusm.whoelke.electricityoutlet.ElectricityOutletHandler;

/**
 * 
 * @author Lukas Smirek
 *
 */
public class WoehlkeTAFacade extends SuperTAFacade<WoehlkeTA>  {
	
	public WoehlkeTAFacade() {
		super("WoehlkeTAFacade");
	}

	@Override
	public WoehlkeTA createSpezificTA(SuperTAFacade<WoehlkeTA> facade) {

return new WoehlkeTA(facade);
	}
	
				}