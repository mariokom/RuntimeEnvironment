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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Logger;

import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide methods to get Mac-Address and InetAddress.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class NetworkUtil {

	private static Logger logger = LoggerUtil.getSdkLogger();
	
	/**
	 * Get MacAddress of this machine.
	 * 
	 * @return a String value of MacAddress
	 */
	static String getMacAddress() {
		
		return getMacAddress( getInetAddress() );
	}

	/**
	 * Get InaetAddress of this machine.
	 * 
	 * @return an Object of InetAddress
	 */
	static InetAddress getInetAddress() {
		
		try {
			
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			
			if ( networkInterfaces == null ) {
				logger.info("Unable to get Network Interfaces.");
				return null;
			}
			
			while ( networkInterfaces.hasMoreElements() ) {
				
				NetworkInterface ni = networkInterfaces.nextElement();
				
				if ( ni == null ) {
					logger.info("Network Interface is null.");
					continue;
				}
			//	logger.info("Name="+ni.getName()+"   Display Name="+ni.getDisplayName());
				
				Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
				
				while ( inetAddresses.hasMoreElements() ) {
					
					InetAddress iNetAddress = inetAddresses.nextElement();
					
					if ( iNetAddress == null ) {
						logger.info("Inet Address is null.");
						continue;
					}
					
					if ( !(iNetAddress instanceof Inet4Address) ) {
						logger.info(iNetAddress +  "is not a valide IPv4 Address.");
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
		
		logger.warning("Unable to get Ethernet Local Address.");
		return null;
	}

	/**
	 * Get MacAddress of specified InetAddress.
	 * 
	 * @param inetAddress an Object of InetAddress
	 * 
	 * @return a String value of MacAddress
	 */
	static String getMacAddress(InetAddress inetAddress) {
		
		if ( inetAddress == null )
			return null;
		
		try {
			
			NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);
	        
			if ( networkInterface == null ) {
				return null;
			}
			
			byte[] mac = networkInterface.getHardwareAddress();
			
			if ( mac == null )
				return null;
			
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < mac.length; i++) {      
				sb.append( String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "") );
			}

			return sb.toString();
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
		return null;
	}
}
