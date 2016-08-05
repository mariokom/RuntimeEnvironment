/*

Copyright (C) 2006-2013  Board of Regents of the University of Wisconsin System (Univ. of Wisconsin-Madison, Trace R&D Center).

This piece of the software package, developed by the Trace Center - University of Wisconsin is released to the public domain with only the following restrictions:

1) That the following acknowledgement be included in the source code and documentation for the program or package that use this code:

"Parts of this program were based on software developed by the Trace Center, University of Wisconsin-Madison under funding from NIDRR / US Dept of Education."

2) That this program not be modified unless it is plainly marked as modified from the original distributed by Trace.

NOTE: This license agreement does not cover third-party components bundled with this software, which have their own license agreement with them. A list of included third-party components with references to their license files is provided with this distribution. 

This software was developed under funding from NIDRR / US Dept of Education under grant # H133E030012.

THIS SOFTWARE IS EXPERIMENTAL/DEMONSTRATION IN NATURE. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD PARTY RIGHTS. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR HOLDERS INCLUDED IN THIS NOTICE BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL INDIRECT OR CONSEQUENTIAL DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

*/

package edu.wisc.trace.uch;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Logger;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide methods to start UCH Zero COnf Service.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class UchZeroConfService {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private static final long SLEET_TIME = 5*1000;
	private static final String SERVICE_TYPE = "_http._tcp.local.";
	
	
	private String hostName;
	private String serviceName;
	
	private String path;
	
	private JmDNS jmdns;
	private ServiceInfo service;
	
	
	/**
	 * Constructor.
	 * 
	 * 
	 * @param address an Object of InetAddress
	 * @param hostName a String value of HostName
	 * @param serviceName a String value of Service Name
	 * 
	 * @param presentationURL a String value of Zero Conf Server presentation URL
	 */
	UchZeroConfService(InetAddress address, String hostName, int portNo, String serviceName, String presentationURL) {

		try {
			
			if ( address == null ) {
				logger.severe("InetAddress is null.");
				return;
			}
			
			if ( hostName == null ) {
				logger.severe("Host Name is null.");
				return;
			}
			
			if ( serviceName == null ) {
				logger.severe("Service Name is null.");
				return;
			}
			
			try {
				jmdns = JmDNS.create(address, hostName);
			} catch (IOException e) {
				logger.severe("IOException : Creating JMDNS.");
				return;
			}
			
			try {
				Thread.sleep(SLEET_TIME);
			} catch (InterruptedException e) {
				logger.warning("InterruptedException : Unable to sleep for "+SLEET_TIME+".");
			}
			
			service = ServiceInfo.create(SERVICE_TYPE, serviceName, portNo, 0, 0, "path="+presentationURL);
	         
	         try {
				jmdns.registerService(service);
			} catch (IOException e) {
				logger.severe("IOException : Unable to register Service '"+serviceName+"'.");
				return;
			}
			
			this.path = presentationURL;
			this.hostName = jmdns.getHostName();
			this.serviceName = service.getName();
			
	        logger.info("Zero Conf Service '"+this.serviceName+"' with Host Name '"+this.hostName+"' started.\nService : "+service);
	       
		} catch(Exception e) {
			logger.severe("Exception : Unable to start zero conf service.");
			//e.printStackTrace();
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
		
		//logger.info("Finalize Called");
		if ( jmdns != null ) {
	        jmdns.unregisterAllServices();
	        jmdns.close();
	        logger.info("Unregister all Zero Conf Services.");
		}
		
		try {
			super.finalize();
		} catch (Throwable e) {

		}
	}
	
}
