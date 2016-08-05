package org.github.roqs23.restws;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import de.hdm.todoservice.NetworkUtil;

class ZeroConfStarter extends Thread{
//	Zero-Conf(Bonjour) Server's Host Name
	private String zeroConfHostName;
	
	// Zero-Conf(Bonjour) Server's Service Name
	private String zeroConfServiceName;
	
	
	
	
	private TodoZeroConfService zeroConfservice;
	private InetAddress address;
	private String presentationURL = "/todo-service";
	private String DEFAULT_ZERO_CONF_HOST_NAME = "todo-service";
	private String DEFAULT_ZERO_CONF_SERVICE_NAME = "TodoZeroConfService";
	private int portNo = 8383;
	
	public ZeroConfStarter(){
		InetAddress inetAddress = NetworkUtil.getInetAddress();
		if (inetAddress == null){
			try {
				inetAddress = 	InetAddress.getLocalHost();
			System.out.println("Only on local device available");
			} catch (UnknownHostException e) {
							e.printStackTrace();
			} 
		}
		
		this.address = inetAddress;
		}
	
	public void run(){
		try{	
		this.zeroConfservice = new TodoZeroConfService(address, DEFAULT_ZERO_CONF_HOST_NAME, portNo, DEFAULT_ZERO_CONF_SERVICE_NAME, presentationURL);
	
this.zeroConfHostName = zeroConfservice.getHostName();
this.zeroConfServiceName = zeroConfservice.getServiceName();
} catch (Exception e) {
}
}

public void finalize(){
	this.zeroConfservice.finalize();
}
}