package cy.ac.ucy.cs.seit.uchCommunication;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

/**
 * Class created to discover the UCH server in the local network.
 * This class contains static methods.
 * 
 * @author Marios Komodromos
 *
 */
public class UchDiscovery {
	public static final String APACHE_SERVER = "_http._tcp.local.";
	public static final String SSH_SERVER = "_ssh._tcp.local.";
	
	public static final String DEFAULT_UCH_SERVICE_QNAME = "UCH Zero Conf Service." + APACHE_SERVER;
	
	static class UchListener implements ServiceListener {
	    @Override
        public void serviceAdded(ServiceEvent event) {
            System.err.println("Uch Discovery Event --> Server added: " + event.getName());
	    }
	    
	    @Override
	    public void serviceRemoved(ServiceEvent event) {
	        System.err.println("Uch Discovery Event --> Server removed: " + event.getName());
	    }
	
	    @Override
	    public void serviceResolved(ServiceEvent event) {
	        System.err.println("Uch Discovery Event --> Server resolved: " + event.getInfo());
	    }
	}
	
	/**
	 * Returns the UCH server IP(s) in the local network.
	 * To determine that a device in the network contains a UCH server, the 'UCH Zero Conf' Service
	 * must be running over TCP/HTTP protocols.
	 * 
	 * @return - A set with the IP(s) of the UCH server
	 * @throws IOException
	 */
	public static HashSet<String> getUchServerIPs() throws IOException {
		return getUchServerIPs(DEFAULT_UCH_SERVICE_QNAME);
	}
	
	
	/**
	 * Returns the server IP(s) in the local network which run a service with the 
	 * given qualified service name. The service must be running
	 * over HTTP/TCP protocols.
	 * 
	 * @param qualifiedServiceName - the qualified service name of the service. An example of a qualified service
	 * name would be: <serviceName>._http._tcp.local.
	 * 
	 * @return - the IP address(es) of the server
	 * 
	 * @throws IOException
	 */
	public static HashSet<String> getUchServerIPs(String qualifiedServiceName) throws IOException {
		HashSet<String> uchServersIPs = new HashSet<String>();
		
		//Use Jmdns library to discover UCH servers
		UchListener uchListener = new UchListener();
		String bonjourServiceType = "_http._tcp.local.";
		JmDNS bonjourService = JmDNS.create();
		bonjourService.addServiceListener(bonjourServiceType, uchListener);
		ServiceInfo[] serviceInfos = bonjourService.list(bonjourServiceType);
		
		//add the discovered addresses to the map
		for (ServiceInfo info : serviceInfos) {
			String qName = info.getQualifiedName();
			if ( qName.equalsIgnoreCase(qualifiedServiceName) ) {
				
				//when the service is running on the same device the IP address is not
				//discovered. We assume that when the IP array is empty (but the name exists), it is because
				//the service runs on localhost
				if (info.getHostAddresses().length < 1) {
					uchServersIPs.add("localhost");
				}
				else {
					uchServersIPs.addAll(Arrays.asList(info.getHostAddresses()));
				}
			}
		}
		bonjourService.close();
		
		return uchServersIPs;
	}
	
	
	/**
	 * Returns the devices IP(s) in the local network which run any bonjour service
	 * over HTTP/TCP protocols and the service qualified name contains
	 * the string given as a parameter.
	 * 
	 * @return - the IP address(es) of the devices that run bonjour services
	 * 
	 * @throws IOException
	 */
	public static HashSet<String> getAllBonjourServicesIPs(String serviceNamePart) throws IOException {
		HashSet<String> uchServersIPs = new HashSet<String>();
		
		//Use Jmdns library to discover UCH servers
		UchListener uchListener = new UchListener();
		String bonjourServiceType = "_http._tcp.local.";
		JmDNS bonjourService = JmDNS.create();
		bonjourService.addServiceListener(bonjourServiceType, uchListener);
		ServiceInfo[] serviceInfos = bonjourService.list(bonjourServiceType);
		
		//add the discovered addresses to the map
		for (ServiceInfo info : serviceInfos) {
			String qName = info.getQualifiedName();
			if ( qName.contains(serviceNamePart) ) {
				
				//when the service is running on the same device the IP address is not
				//discovered. We assume that when the IP array is empty (but the name exists), it is because
				//the service runs on localhost
				if (info.getHostAddresses().length < 1) {
					uchServersIPs.add("localhost");
				}
				else {
					uchServersIPs.addAll(Arrays.asList(info.getHostAddresses()));
				}
			}
		}
		bonjourService.close();
		
		return uchServersIPs;
	}
	
	
	/**
	 * Returns the service info (in the local network) with the given qualified service name.
	 * CAUTION: When the server and the client run on the same device, the
	 * service will be discovered but the IP address in the {@link ServiceInfo}
	 * object may be null.
	 * The service must be running over HTTP/TCP protocols.
	 * If no such service exists, null will be returned.
	 * 
	 * @param name - the qualified service name of the service. An example of a qualified service
	 * name would be: <serviceName>._http._tcp.local.
	 * 
	 * @return - the service information
	 * 
	 * @throws IOException
	 */
	public static ServiceInfo getUchServerInfo(String name) throws IOException {
		//Use Jmdns library to discover UCH servers
		UchListener uchListener = new UchListener();
		String bonjourServiceType = "_http._tcp.local.";
		JmDNS bonjourService = JmDNS.create();
		bonjourService.addServiceListener(bonjourServiceType, uchListener);
		ServiceInfo[] serviceInfos = bonjourService.list(bonjourServiceType);
		
		for (ServiceInfo info : serviceInfos) {
			String qualifiedName = info.getQualifiedName();
			if (qualifiedName.equalsIgnoreCase(name) ) {
				return info;
			}
		}
		bonjourService.close();
		
		return null;
	}
	
	
	/**
	 * Returns the UCH service info (in the local network).
	 * CAUTION: When the UCH server and the UCH client run on the same device, the
	 * service will be discovered but the IP address in the {@link ServiceInfo}
	 * object may be null.
	 * The service must be running over HTTP/TCP protocols.
	 * If no such service exists, null will be returned.
	 * 
	 * @return - the service information
	 * 
	 * @throws IOException
	 */
	public static ServiceInfo getUchServerInfo() throws IOException {
		return getUchServerInfo(UchDiscovery.DEFAULT_UCH_SERVICE_QNAME);
	}
	
}
