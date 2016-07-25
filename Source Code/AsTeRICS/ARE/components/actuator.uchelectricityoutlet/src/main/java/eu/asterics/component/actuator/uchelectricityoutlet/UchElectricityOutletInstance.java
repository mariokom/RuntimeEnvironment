

/*
 *    AsTeRICS - Assistive Technology Rapid Integration and Construction Set
 * 
 * 
 *        d8888      88888888888       8888888b.  8888888 .d8888b.   .d8888b. 
 *       d88888          888           888   Y88b   888  d88P  Y88b d88P  Y88b
 *      d88P888          888           888    888   888  888    888 Y88b.     
 *     d88P 888 .d8888b  888   .d88b.  888   d88P   888  888         "Y888b.  
 *    d88P  888 88K      888  d8P  Y8b 8888888P"    888  888            "Y88b.
 *   d88P   888 "Y8888b. 888  88888888 888 T88b     888  888    888       "888
 *  d8888888888      X88 888  Y8b.     888  T88b    888  Y88b  d88P Y88b  d88P
 * d88P     888  88888P' 888   "Y8888  888   T88b 8888888 "Y8888P"   "Y8888P" 
 *
 *
 *                    homepage: http://www.asterics.org 
 *
 *       This project has been partly funded by the European Commission, 
 *                      Grant Agreement Number 247730
 *  
 *  
 *    License: GPL v3.0 (GNU General Public License Version 3.0)
 *                 http://www.gnu.org/licenses/gpl.html
 * 
 */

package eu.asterics.component.actuator.uchelectricityoutlet;


import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.UchCommunicator;
import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.UchDiscovery;
import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.UchSession;
import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.session.requests.SetValuesRequest;
import eu.asterics.component.actuator.uchelectricityoutlet.uchCommunication.session.xmlElements.SetElement;
import eu.asterics.mw.data.ConversionUtils;
import eu.asterics.mw.model.runtime.AbstractRuntimeComponentInstance;
import eu.asterics.mw.model.runtime.IRuntimeInputPort;
import eu.asterics.mw.model.runtime.IRuntimeOutputPort;
import eu.asterics.mw.model.runtime.IRuntimeEventListenerPort;
import eu.asterics.mw.model.runtime.IRuntimeEventTriggererPort;
import eu.asterics.mw.model.runtime.impl.DefaultRuntimeOutputPort;
import eu.asterics.mw.model.runtime.impl.DefaultRuntimeInputPort;
import eu.asterics.mw.model.runtime.impl.DefaultRuntimeEventTriggererPort;
import eu.asterics.mw.services.AstericsErrorHandling;
import eu.asterics.mw.services.AREServices;

/**
 * 
 * Communication with electricity outlet device, using the URC protocol.
 * The component expects to find a UCH server in the local network.
 * The UCH server ip could be either entered directly by setting the corresponding
 * component property, or enable the auto-discovery feature that finds the UCH server IP (in the local network)
 *  
 * @author Marios Komodromos [mkomod05@cs.ucy.ac.cy]
 *         Date: 20/07/2016
 *         
 */
public class UchElectricityOutletInstance extends AbstractRuntimeComponentInstance
{
	// Usage of an output port e.g.: opMyOutPort.sendData(ConversionUtils.intToBytes(10)); 

	// Usage of an event trigger port e.g.: etpMyEtPort.raiseEvent();

	String propIp = "localhost";
	int propPort = 8080;
	String propUIid = "undefined";
	boolean propAutoDiscovery = false;
	boolean propState = false;

	// declare member variables here
	UchCommunicator uchCommunicator;
	UchSession uchSession;
	String sessionId;
  
	
	public static final String ELECTRICITY_OUTLET_CHANNEL_1 = "electricity_outlet_1";
	public static final String ELECTRICITY_OUTLET_CHANNEL_2 = "electricity_outlet_2";
	public static final String ELECTRICITY_OUTLET_CHANNEL_3 = "electricity_outlet_3";
	
   /**
    * The class constructor.
    */
    public UchElectricityOutletInstance()
    {
        // empty constructor
    }

   /**
    * returns an Input Port.
    * @param portID   the name of the port
    * @return         the input port or null if not found
    */
    public IRuntimeInputPort getInputPort(String portID)
    {

		return null;
	}

    /**
     * returns an Output Port.
     * @param portID   the name of the port
     * @return         the output port or null if not found
     */
    public IRuntimeOutputPort getOutputPort(String portID)
	{

		return null;
	}

    /**
     * returns an Event Listener Port.
     * @param eventPortID   the name of the port
     * @return         the EventListener port or null if not found
     */
    public IRuntimeEventListenerPort getEventListenerPort(String eventPortID)
    {
		if ("electricity_outlet_1".equalsIgnoreCase(eventPortID)) {
			return elpSwitch1;
		}
		else if ("electricity_outlet_2".equalsIgnoreCase(eventPortID)) {
			return elpSwitch2;
		}
		else if ("electricity_outlet_3".equalsIgnoreCase(eventPortID)) {
			return elpSwitch3;
		}
        return null;
    }

    /**
     * returns an Event Triggerer Port.
     * @param eventPortID   the name of the port
     * @return         the EventTriggerer port or null if not found
     */
    public IRuntimeEventTriggererPort getEventTriggererPort(String eventPortID)
    {

        return null;
    }
		
    /**
     * returns the value of the given property.
     * @param propertyName   the name of the property
     * @return               the property value or null if not found
     */
    public Object getRuntimePropertyValue(String propertyName)
    {
		if ("ip".equalsIgnoreCase(propertyName))
		{
			return propIp;
		}
		if ("port".equalsIgnoreCase(propertyName))
		{
			return propPort;
		}
		if ("uIid".equalsIgnoreCase(propertyName))
		{
			return propUIid;
		}
		if ("autoDiscovery".equalsIgnoreCase(propertyName))
		{
			return propAutoDiscovery;
		}
		if ("state".equalsIgnoreCase(propertyName))
		{
			return propState;
		}

        return null;
    }

    /**
     * sets a new value for the given property.
     * @param propertyName   the name of the property
     * @param newValue       the desired property value or null if not found
     */
    public Object setRuntimePropertyValue(String propertyName, Object newValue)
    {
		if ("ip".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propIp;
			propIp = (String)newValue;
			return oldValue;
		}
		if ("port".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propPort;
			propPort = Integer.parseInt(newValue.toString());
			return oldValue;
		}
		if ("uIid".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propUIid;
			propUIid = (String)newValue;
			return oldValue;
		}
		if ("autoDiscovery".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propAutoDiscovery;
			if("true".equalsIgnoreCase((String)newValue))
			{
				propAutoDiscovery = true;
			}
			else if("false".equalsIgnoreCase((String)newValue))
			{
				propAutoDiscovery = false;
			}
			return oldValue;
		}
		if ("state".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propState;
			if("true".equalsIgnoreCase((String)newValue))
			{
				propState = true;
			}
			else if("false".equalsIgnoreCase((String)newValue))
			{
				propState = false;
			}
			return oldValue;
		}

        return null;
    }

     /**
      * Input Ports for receiving values.
      */


     /**
      * Event Listerner Ports.
      */
	final IRuntimeEventListenerPort elpSwitch1 = new IRuntimeEventListenerPort()
	{
		public void receiveEvent(final String data)
		{
			SetValuesRequest setValuesRequest = new SetValuesRequest();
			setValuesRequest.addSetElement( new SetElement(ELECTRICITY_OUTLET_CHANNEL_1, !propState+"") );
			propState = !propState;
			uchSession.setValues(setValuesRequest);
		}
	};
	
	final IRuntimeEventListenerPort elpSwitch2 = new IRuntimeEventListenerPort()
	{
		public void receiveEvent(final String data)
		{
			SetValuesRequest setValuesRequest = new SetValuesRequest();
			setValuesRequest.addSetElement( new SetElement(ELECTRICITY_OUTLET_CHANNEL_2, !propState+"") );
			propState = !propState;
			uchSession.setValues(setValuesRequest);
		}
	};
	
	final IRuntimeEventListenerPort elpSwitch3 = new IRuntimeEventListenerPort()
	{
		public void receiveEvent(final String data)
		{
			SetValuesRequest setValuesRequest = new SetValuesRequest();
			setValuesRequest.addSetElement( new SetElement(ELECTRICITY_OUTLET_CHANNEL_3, !propState+"") );
			propState = !propState;
			uchSession.setValues(setValuesRequest);
		}
	};

	

     /**
      * called when model is started.
      */
      @Override
      public void start()
      {
    	  
  		AstericsErrorHandling.instance.getLogger().fine("\nUCH electricity outlet component is starting\n\n");
  		try {
  			String uchIP = "";
  			
  			if (propAutoDiscovery) {
	  			Set<String> ipSet = UchDiscovery.getUchServerIPs();
	  			if (ipSet.size() < 1) {
	  				AstericsErrorHandling.instance.getLogger().warning("\nCould not detect a UCH server in the local network\n");
	  			}
	  			else {
	  				//get the first ip (usually it would be only one)
	  				for (String ip: ipSet) {
	  					uchIP = ip;
	  					break;
	  				}
	  				AstericsErrorHandling.instance.getLogger().fine("\n\n UCH auto-discovery detected server's ip: '"+uchIP+"'\n\n");
	  			}
  			}
  			else {
  				uchIP = propIp;
  			}

  			if ((uchIP != null) && !uchIP.isEmpty()) {
  				UchCommunicator uchCommunicator = new UchCommunicator(uchIP, propPort, false);

  				//retrieve URC-HTTP uri
  				String urcHttpUri = uchCommunicator.getProtocolURI(propUIid, UchCommunicator.URC_HTTP);

  				//The IP address of URC-HTTP uri is  always to localhost, even if the application which requested the uri is not running on localhost.
  				//To overcome this issue, when the UCH IP address (given by the model designer) is not a local IP,
  				//replace the 'propUrcHttpURI''s ip with the UCH ip
  				String urcHttpUri_corrected = urcHttpUri;
  				if ( !(uchIP.equals("localhost") || uchIP.equals("127.0.0.1") || uchIP.equals("0.0.0.0")) ) {
  					String regex = "";
  					if ( uchIP.equals("localhost") ) {
  						regex = "localhost";
  					}
  					else {
  						regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
  					}
  					urcHttpUri_corrected = urcHttpUri.replaceFirst(regex, uchIP);
  				}

  	  			sessionId = uchCommunicator.createSession_getRequest(urcHttpUri_corrected);
  	  			uchSession = uchCommunicator.getSession(sessionId);
  			}
  		} catch (Exception e) {
  			e.printStackTrace();
  			uchSession = null;
  			AstericsErrorHandling.instance.getLogger().warning("\n\nUCH electricity outlet component threw error on start\n\n");
  		}
          super.start();
      }

     /**
      * called when model is paused.
      */
      @Override
      public void pause()
      {
          super.pause();
      }

     /**
      * called when model is resumed.
      */
      @Override
      public void resume()
      {
          super.resume();
      }

     /**
      * called when model is stopped.
      */
      @Override
      public void stop()
      {
          super.stop();
      }
}