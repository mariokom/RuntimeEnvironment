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

package edu.wisc.trace.uch.resource.retrievalmanager;

import java.util.List;
import java.util.Map;

/**
 * Maintain Details related to Resource Server Resource like globalAt, resourceLocalAt, modifiedAt, resourceType etc.
 * Also provide methods to retrieve these properties.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class ResourceDetails {

	private String globalAt;
	
	private String resourceLocalAt;
	
	private String modifiedAt;
	
	private String resourceType;
	
	private boolean downloaded;
	
	private Map<String, List<String>> properties;
	
	/**
	 * Get the value of globalAt.
	 *  
	 * @return a String value of globalAt
	 */
	public String getGlobalAt() {
		return globalAt;
	}
	
	/**
	 * Set the value of globalAt.
	 *  
	 * @param globalAt a String value of globalAt
	 */
	public void setGlobalAt(String globalAt) {
		this.globalAt = globalAt;
	}
	
	/**
	 * Get the value of resourceLocalAt.
	 * 
	 * @return a String value of resourceLocalAt
	 */
	public String getResourceLocalAt() {
		return resourceLocalAt;
	}

	/**
	 * Set the value of resourceLocalAt.
	 * 
	 * @param resourceLocalAt a String value of resourceLocalAt
	 */
	public void setResourceLocalAt(String resourceLocalAt) {
		this.resourceLocalAt = resourceLocalAt;
	}

	/**
	 * Get the value of modifiedAt.
	 * 
	 * @return a String value of modifiedAt
	 */
	public String getModifiedAt() {
		return modifiedAt;
	}

	/**
	 * Set the value of modifiedAt
	 * 
	 * @param modifiedAt a String value of modifiedAt
	 */
	public void setModifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	/**
	 * Get the value of resourceType.
	 * 
	 * @return a String value of resourceType
	 */
	public String getResourceType() {
		return resourceType;
	}

	/**
	 * Set the value of resourceType.
	 * 
	 * @param resourceType a String value of resourceType
	 */
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	/**
	 * Get the value of downloaded.
	 * 
	 * @return a boolean value of downloaded
	 */
	public boolean isDownloaded() {
		return downloaded;
	}
	
	/**
	 * Set the value of downloaded.
	 * 
	 * @param downloaded a boolean value of downloaded
	 */
	public void setDownloaded(boolean downloaded) {
		this.downloaded = downloaded;
	}
	
	/**
	 * Get the Properties Map.
	 * 
	 * @return an Object of Map&lt;String, List&lt;String&gt;&gt; 
	 */
	public Map<String, List<String>> getProperties() {
		return properties;
	}
	
	/**
	 * Set the Properties Map.
	 * 
	 * @param properties an Object of Map&lt;String, List&lt;String&gt;&gt; 
	 */
	public void setProperties(Map<String, List<String>> properties) {
		this.properties = properties;
	}
	
	
}
