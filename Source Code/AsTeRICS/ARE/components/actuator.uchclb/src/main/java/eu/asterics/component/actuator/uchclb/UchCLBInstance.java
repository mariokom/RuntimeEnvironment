

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

package eu.asterics.component.actuator.uchclb;


import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import eu.asterics.component.actuator.uchclb.uchCommunication.UchCommunicator;
import eu.asterics.component.actuator.uchclb.uchCommunication.UchDiscovery;
import eu.asterics.component.actuator.uchclb.uchCommunication.UchSession;
import eu.asterics.component.actuator.uchclb.uchCommunication.session.requests.SetValuesRequest;
import eu.asterics.component.actuator.uchclb.uchCommunication.session.xmlElements.SetElement;
import eu.asterics.mw.are.BundleManager;
import eu.asterics.mw.are.DeploymentManager;
import eu.asterics.mw.are.exceptions.BundleManagementException;
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
import eu.asterics.mw.services.AstericsThreadPool;

/**
 * 
 * Communication with Philips' colored light bulb device, using the URC protocol.
 * The component expects to find a UCH server in the local network.
 * The UCH server ip could be either entered directly by setting the corresponding
 * component property, or enable the auto-discovery feature that finds the UCH server IP (in the local network)
 *  
 * @author Marios Komodromos [mkomod05@cs.ucy.ac.cy]
 *         Date: 02/05/2016
 */
public class UchCLBInstance extends AbstractRuntimeComponentInstance
{
	// Usage of an output port e.g.: opMyOutPort.sendData(ConversionUtils.intToBytes(10)); 

	// Usage of an event trigger port e.g.: etpMyEtPort.raiseEvent();

	String propIp = "localhost";
	int propPort = 8080;
	String propUIid = "undefined";
	int propTimeInterval;
	boolean propAutoDiscovery = false;
	
	
	// declare member variables here
	UchCommunicator uchCommunicator;
	UchSession uchSession;
	String sessionId;
	ScheduledExecutorService executor;
  
	int propHue = 0;
	int propBrightness = 0;
	int propSaturation = 50;
	
	boolean switchState = true; 
	
	boolean isPaused;
    
	
	
   /**
    * The class constructor.
    */
    public UchCLBInstance()
    {
    	isPaused = false;
    	AstericsErrorHandling.instance.getLogger().fine("\nUchCLB Component concstructor\n");
    }

   /**
    * returns an Input Port.
    * @param portID   the name of the port
    * @return         the input port or null if not found
    */
    public IRuntimeInputPort getInputPort(String portID)
    {
		if ("hue".equalsIgnoreCase(portID))
		{
			return ipHue;
		}
		if ("brightness".equalsIgnoreCase(portID))
		{
			return ipBrightness;
		}
		if ("saturation".equalsIgnoreCase(portID))
		{
			return ipSaturation;
		}

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
		if ("triggerSwitch".equalsIgnoreCase(eventPortID))
		{
			return elpSwitch;
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
		if ("timeInterval".equalsIgnoreCase(propertyName))
		{
			return propTimeInterval;
		}
		if ("autoDiscovery".equalsIgnoreCase(propertyName))
		{
			return propAutoDiscovery;
		}
		if ("hue".equalsIgnoreCase(propertyName))
		{
			return propHue;
		}
		if ("saturation".equalsIgnoreCase(propertyName))
		{
			return propSaturation;
		}
		if ("brightness".equalsIgnoreCase(propertyName))
		{
			return propBrightness;
		}
		
		
        return "no value";
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
		if ("timeInterval".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propTimeInterval;
			propTimeInterval = Integer.parseInt(newValue.toString());
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
		if ("hue".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propHue;
			propHue = Integer.parseInt(newValue.toString());
			return oldValue;
		}
		if ("saturation".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propSaturation;
			propSaturation = Integer.parseInt(newValue.toString());
			return oldValue;
		}
		if ("brightness".equalsIgnoreCase(propertyName))
		{
			final Object oldValue = propBrightness;
			propBrightness = Integer.parseInt(newValue.toString());
			return oldValue;
		}

        return null;
    }

    
     /**
      * Input Ports for receiving values.
      */
	private final IRuntimeInputPort ipHue  = new DefaultRuntimeInputPort()
	{
		public void receiveData(byte[] data)
		{
			if (uchSession != null) {
				propHue = ConversionUtils.intFromBytes(data);
			}
		}
	};
	private final IRuntimeInputPort ipBrightness  = new DefaultRuntimeInputPort()
	{
		public void receiveData(byte[] data)
		{
			if (uchSession != null) {
				propBrightness = ConversionUtils.intFromBytes(data);
			}
		}
	};
	private final IRuntimeInputPort ipSaturation  = new DefaultRuntimeInputPort()
	{
		public void receiveData(byte[] data)
		{
			if (uchSession != null) {
				propSaturation = ConversionUtils.intFromBytes(data);
			}
		}
	};


     /**
      * TODO wait for response and check if the response its ok, then change the switch state
      * Event Listerner Ports.
      */
	final IRuntimeEventListenerPort elpSwitch = new IRuntimeEventListenerPort()
	{
		public void receiveEvent(final String data)
		{
			SetValuesRequest setValuesRequest = new SetValuesRequest();
			setValuesRequest.addSetElement( new SetElement("LightSwitch", !switchState + "") );
			switchState = !switchState;
			uchSession.setValues(setValuesRequest);

		}
	};

	

     /**
      * called when model is started.
      */
      @Override
      public void start()
      {
    	  
  		AstericsErrorHandling.instance.getLogger().fine("\nUCH colored light bulb component is starting\n\n");
  		executor = null;
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
  			
  			
  			executor = Executors.newScheduledThreadPool(1);
  			executor.scheduleAtFixedRate(valueDiscpatcher, 0, propTimeInterval, TimeUnit.MILLISECONDS);
  		} catch (Exception e) {
  			e.printStackTrace();
  			uchSession = null;
  			AstericsErrorHandling.instance.getLogger().warning("\n\nUCH colored light bulb Component threw error on start\n\n");
  		}
  		super.start();
      }

     /**
      * called when model is paused.
      */
      @Override
      public void pause()
      {
    	  isPaused = true;
          super.pause();
      }

     /**
      * called when model is resumed.
      */
      @Override
      public void resume()
      {
    	  isPaused = false;
          super.resume();
      }

     /**
      * called when model is stopped.
      */
      @Override
      public void stop()
      {
    	  if (executor != null) {
    		  executor.shutdown();
    	  }
          super.stop();
      }
      
      
      Runnable valueDiscpatcher = new Runnable() {
			public void run() {
				if ( (!isPaused) && (uchSession != null) ) {
					SetValuesRequest setValuesRequest = new SetValuesRequest();
					setValuesRequest.addSetElement( new SetElement("Saturation", propSaturation+"") );
					setValuesRequest.addSetElement( new SetElement("Brightness", propBrightness+"") );
					setValuesRequest.addSetElement( new SetElement("HueDegree", propHue+"") );
					
					//send the request to change the URC component values
					uchSession.setValues(setValuesRequest);
				}
			}
      };
      
}