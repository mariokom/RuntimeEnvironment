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

import java.util.Map;
import java.util.logging.Logger;

import org.openurc.taframework.SuperTA;
import org.openurc.taframework.SuperTAFacade;

import de.hdm.woehlke.api.WoehlkeConstants;
import edu.wisc.trace.uch.util.LoggerUtil;
import edu.wisc.trace.uch.util.Session;
import lusm.whoelke.electricityoutlet.ElectricityOutletHandler;
/**
 * 
 * @author Lukas Smirek
 *
 */
public class WoehlkeTA extends SuperTA<ElectricityOutletHandler> {

	private Logger logger = LoggerUtil.getSdkLogger();
 final static String ELECTRICITY_OUTLET_1 = "/electricity_outlet_1";
	 final static String ELECTRICITY_OUTLET_2 = "/electricity_outlet_2";
	 final static String ELECTRICITY_OUTLET_3 = "/electricity_outlet_3";
	
	 public WoehlkeTA(SuperTAFacade facade){
		super(facade);
			}
	@Override	
				public void executeCommand(String commandName, Session session,String value){


		switch (commandName){
	case ELECTRICITY_OUTLET_1:		
setVariable(session, commandName, value);	
		handler.setSocket1(value.equals("true"));
				break;
	case ELECTRICITY_OUTLET_2:
		setVariable(session, commandName,	value);
		handler.setSocket2(value.equals("true"));
		break;
case ELECTRICITY_OUTLET_3:
	setVariable(session, commandName,	value);
	handler.setSocket3(value.equals("true"));
	break;
default:
		logger.info("Command is not valide");
	}
}

	@Override		
public void initSessionValues(Session session) {

	session.setValue(ELECTRICITY_OUTLET_1, "" + handler.getSocket1() );
	session.setValue(ELECTRICITY_OUTLET_2,""  + handler.getSocket2()   );
	session.setValue(ELECTRICITY_OUTLET_3, ""  + handler.getSocket3()  );
session.setValue("/temperature", "23.0");

}	

@Override
public void useTargetProps(Map<String, Object> targetProps) {
	String ipAdr = (String) targetProps.get(WoehlkeConstants.IpAddress   );
	handler.setHost(ipAdr);
	}


@Override
public void update(Session session){
updateVariable(session, ELECTRICITY_OUTLET_1, "" +handler.getSocket1());
updateVariable(session, ELECTRICITY_OUTLET_2, "" +handler.getSocket2());
updateVariable(session, ELECTRICITY_OUTLET_3, "" +handler.getSocket3());
	}
}