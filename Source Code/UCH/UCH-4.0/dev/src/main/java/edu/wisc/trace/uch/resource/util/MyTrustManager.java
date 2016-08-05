/*

Copyright (C) 2006-2008  Board of Regents of the University of Wisconsin System (Univ. of Wisconsin-Madison, Trace R&D Center).

This piece of the software package, developed by the Trace Center - University of Wisconsin is released to the public domain with only the following restrictions:

1) That the following acknowledgement be included in the source code and documentation for the program or package that use this code:

"Parts of this program were based on software developed by the Trace Center, University of Wisconsin-Madison under funding from NIDRR / US Dept of Education."

2) That this program not be modified unless it is plainly marked as modified from the original distributed by Trace.

NOTE: This license agreement does not cover third-party components bundled with this software, which have their own license agreement with them. A list of included third-party components with references to their license files is provided with this distribution. 

This software was developed under funding from NIDRR / US Dept of Education under grant # H133E030012.

THIS SOFTWARE IS EXPERIMENTAL/DEMONSTRATION IN NATURE. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD PARTY RIGHTS. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR HOLDERS INCLUDED IN THIS NOTICE BE LIABLE FOR ANY CLAIM, OR ANY SPECIAL INDIRECT OR CONSEQUENTIAL DAMAGES, OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.

*/

package edu.wisc.trace.uch.resource.util;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * MyTrustManager is responsible for managing the trust material that is used when making trust decisions, 
 * and for deciding whether credentials presented by a peer should be accepted.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class MyTrustManager implements X509TrustManager {
	
	/**
	 * Given the partial or complete certificate chain provided by the peer, 
	 * build a certificate path to a trusted root and return if it can be validated and is 
	 * trusted for client SSL authentication based on the authentication type.
	 *      
	 *  @param chain the peer certificate chain
	 *  @param authType the authentication type based on the client certificate
	 *  
	 */
     public void checkClientTrusted(X509Certificate[] chain, String authType) {
    	
     }
     
     /**
      * Given the partial or complete certificate chain provided by the peer, 
      * build a certificate path to a trusted root and return if it can be validated and is 
      * trusted for server SSL authentication based on the authentication type.
      * 
      * @param chain the peer certificate chain
      * @param authType the key exchange algorithm used
      */
     public void checkServerTrusted(X509Certificate[] chain, String authType) {
    	 for(int i=0;i<chain.length;i++){
    		
    		 try{
    			 chain[i].checkValidity();
    		 }catch(CertificateExpiredException cee){
    			// cee.printStackTrace();
    		 }catch(CertificateNotYetValidException cnye){
    			// cnye.printStackTrace();
    		 }
    		
    	 }
    	
     }
     
     /**
      *  Return an array of certificate authority certificates which are trusted for authenticating peers.
      */
     public X509Certificate[] getAcceptedIssuers() {
    
         return null;
    	
     }

}