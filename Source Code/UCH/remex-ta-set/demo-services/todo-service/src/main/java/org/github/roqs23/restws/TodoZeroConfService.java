package org.github.roqs23.restws;

import java.io.IOException;
import java.net.InetAddress;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * Provide methods to start UCH Zero COnf Service.
 */
class TodoZeroConfService {
private static Logger logger = LogManager.getLogger("todoLogger");	
	private static final long SLEET_TIME = 5*1000;
	private static final String SERVICE_TYPE = "_http._tcp.local.";
	
	private String hostName;
	private String serviceName;
	private String path;
	
	private JmDNS jmdns;
	private ServiceInfo service;
	
	
TodoZeroConfService(InetAddress address, String hostName, int portNo, String serviceName, String presentationURL) {
System.out.println("construct zeroConfService");
System.out.println(address);
System.out.println(hostName);
System.out.println(portNo);
System.out.println(serviceName);
System.out.println(presentationURL);
		try {
			if ( address == null ) {
				logger.error("InetAddress is null.");
				return;
			}
			
			if ( hostName == null ) {
				logger.error("Host Name is null.");
				return;
			}
					if ( serviceName == null ) {
logger.error("Service Name is null.");
				return;
			}
			
			try{
				System.out.println("Create JMDNS with hostname: " + hostName + " and address: " + address);
				jmdns = JmDNS.create(address, hostName);
			} catch (IOException e) {
System.out.println("IOException: Exception was thrown when Creating JMDNS.");
				return;
			}
			
			try {
				Thread.sleep(SLEET_TIME);
			} catch (InterruptedException e) {
				logger.warn("InterruptedException : Unable to sleep for "+SLEET_TIME+".");
			}
			
						service = ServiceInfo.create(SERVICE_TYPE, serviceName, portNo, 0, 0, "path="+presentationURL);
						System.out.println("Created: " + SERVICE_TYPE + " " + serviceName + " " + portNo);
						
	         try {
	        	 				jmdns.registerService(service);
				System.out.println(service + "successfuly registered" );
				
			} catch (IOException e) {
System.out.println("IOException : Unable to register Service '"+serviceName+"'.");
				return;
			}
			
			this.path = presentationURL;
			this.hostName = jmdns.getHostName();
			this.serviceName = service.getName();
			
	        System.out.println("Zero Conf Service '"+this.serviceName+"' with Host Name '"+this.hostName+"' started.\nService : "+service);
	       
		} catch(Exception e) {
			System.out.println("Exception : Unable to start zero conf service.");
e.printStackTrace();
		}
	}

	/**
	 * Get the value of Host Name
	 * 
	 * @return a String value of Host Name
	 */
	String getHostName() {
		return hostName;
	}

	/**
	 * Get the value of Service Name.
	 * 
	 * @return a String value of Service Name
	 */
	String getServiceName() {
		return serviceName;
	}

	/**
	 * Get the value of Path that represent Presentation URL.
	 * 
	 * @return a String value of Path
	 */
	String getPath() {
		return path;
	}

	/**
	 * Stop Zero Conf Service.
	 */
	public void finalize() {
		
		logger.info("Finalize Called");
		if ( jmdns != null ) {
	        jmdns.unregisterAllServices();
	        try {
				jmdns.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        logger.info("Unregister all Zero Conf Services.");
		}
		
		try {
			super.finalize();
		} catch (Throwable e) {

		}
	}
	
}
