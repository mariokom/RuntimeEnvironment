package de.hdm.uch.todoservice.tdm;

import java.io.IOException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import org.openurc.tdmframework.SuperDiscovery;
import org.openurc.tdmframework.SuperTDM;
import org.openurc.uch.ITDMListener;

public abstract class ZeroConfDiscovery<T extends SuperTDM> extends SuperDiscovery {				
	String mdnsServiceType = "_http._tcp.local.";
	protected JmDNS mdnsService;
	protected ServiceListener mdnsServiceListener = new ServiceListener() {
			
			
			  public void serviceAdded(ServiceEvent serviceEvent) {
			    // Test service is discovered. requestServiceInfo() will trigger serviceResolved() callback. 
			    mdnsService.requestServiceInfo(mdnsServiceType, serviceEvent.getName());
			    String hostName = "";
			    newServiceAdded(hostName);
			  }
			
			  public void serviceRemoved(ServiceEvent serviceEvent) {
			    // Test service is disappeared. 
			  }
		
			  public void serviceResolved(ServiceEvent serviceEvent) {
			    // Test service info is resolved. 
			    String serviceUrl = serviceEvent.getInfo().getURL();
			    // serviceURL is usually something like http://192.168.11.2:6666/my-service-name
			  }
	};
	
	

	public ZeroConfDiscovery(T tdm, ITDMListener tdmListener){
	super(tdm,tdmListener);		
	}



	public void run() {
	try {
		mdnsService = JmDNS.create();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	mdnsService.addServiceListener("my-service-type", mdnsServiceListener);
	ServiceInfo[] infos = mdnsService.list(mdnsServiceType);
	// Retrieve service info from either ServiceInfo[] returned here or listener callback method above. 
	
			
	
	
	
	
	
				}



	public void newServiceAdded(String hostName) {
		registerTarget(hostName);
			
	}



	public abstract void registerTarget(String hostName);
	}
