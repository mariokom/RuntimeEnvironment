package de.hdm.todoservice;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetworkUtil {
	private static Logger logger = LogManager.getLogger("todoLogger");
	
public static InetAddress getInetAddress() {
		
		try {
			
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			
			if ( networkInterfaces == null ) {
				logger.info("Unable to get Network Interfaces.");
				return null;
			}
			
			int i = 0;
			while ( networkInterfaces.hasMoreElements() ) {
				
				NetworkInterface ni = networkInterfaces.nextElement();
//				System.out.print("i" + i++ +": ");
//				System.out.println(ni);
				if ( ni == null ) {
					logger.error("Network Interface is null.");
					continue;
				}
				System.out.println("Name="+ni.getName()+"   Display Name="+ni.getDisplayName());
				
				Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
				
				while ( inetAddresses.hasMoreElements() ) {
					
					InetAddress iNetAddress = inetAddresses.nextElement();
					
					if ( iNetAddress == null ) {
						logger.error("Inet Address is null.");
						continue;
					}
					
					if ( !(iNetAddress instanceof Inet4Address) ) {
						logger.error(iNetAddress +  "is not a valide IPv4 Address.");
						continue;
					}
					
					if ( !iNetAddress.isLoopbackAddress() && iNetAddress.isSiteLocalAddress() )
						return iNetAddress;
					/*
					logger.info("\n\n\t Host Name="+iNetAddress.getHostName()+
							"\n\t Host Address="+iNetAddress.getHostAddress()+
							"\n\t isLoopbackAddress="+iNetAddress.isLoopbackAddress()+
							"\n\t isSiteLocalAddress="+iNetAddress.isSiteLocalAddress()+
							"\n\t Canonical Host Name="+iNetAddress.getCanonicalHostName()+
							"\n\t isAnyLocalAddress="+iNetAddress.isAnyLocalAddress()+
							"\n\t isLinkLocalAddress="+iNetAddress.isLinkLocalAddress()+
							"\n\t isMCGlobal="+iNetAddress.isMCGlobal()+
							"\n\t isMCLinkLocal="+iNetAddress.isMCLinkLocal()+
							"\n\t isMCNodeLocal="+iNetAddress.isMCNodeLocal()+
							"\n\t isMCOrgLocal="+iNetAddress.isMCOrgLocal()+
							"\n\t isMCSiteLocal="+iNetAddress.isMCSiteLocal()+
							"\n\t isMulticastAddress="+iNetAddress.isMulticastAddress() );
					*/
				}
			}
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	logger.error("Unable to get Ethernet Local Address.");
		return null;
	}

}
