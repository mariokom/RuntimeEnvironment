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
package edu.wisc.trace.uch.resource.localresourcemanager;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide methods to validate properties.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version Revision: 1.0
 */
public class PropertyValidator {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	/**
	 * Default Constructor.
	 */
	PropertyValidator() {
		
	}
	
	/**
	 * Validate specified properties.
	 * 
	 * @param props an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return a boolean value specifies whether properties validated successfully.
	 */
	boolean validateProps(Map<String, List<String>> props) {
		
		if ( props == null ) {
			logger.warning("Property Map is null.");
			return false;
		}
		
		if ( !checkForResourceName(props) )
			return false;
		
		if ( !checkForResourceType(props) )
			return false;
		
		if ( !checkForResourceUri(props) && !checkForResourceLocalAt(props) )
			return false;
		
		return true;
	}
	
	/**
	 * Check whether property resource name exists in property map.
	 * If exists then check is there only one value of it.
	 * 
	 * @param props an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return whether property resource name is exists in property map or not.
	 */
	private boolean checkForResourceName(Map<String, List<String>> props) {
		
		List<String> names = props.get(Constants.PROPERTY_RES_NAME);
		
		if ( names == null ) {
			logger.warning("Property Map doesn't contain property '"+Constants.PROPERTY_RES_NAME+"'.");
			return false;
		}
		
		if ( names.size() != 1 ) {
			logger.warning("'"+Constants.PROPERTY_RES_NAME+"' must have only one value.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check whether property resource type exists in property map.
	 * 
	 * @param props an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return whether property resource type is exists in property map or not.
	 */
	private boolean checkForResourceType(Map<String, List<String>> props) {
		
		List<String> resTypes = props.get(Constants.PROPERTY_RES_TYPE);
		
		if ( (resTypes == null) || (resTypes.size() == 0) ) {
			logger.warning("Property Map doesn't contain property '"+Constants.PROPERTY_RES_TYPE+"'.");
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * Check whether property resource name exists in property map.
	 * If exists then check is there only one value of it.
	 * 
	 * @param props an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return whether property resource name is exists in property map or not.
	 */
	private boolean checkForResourceUri(Map<String, List<String>> props) {
		
		List<String> names = props.get(Constants.PROPERTY_RESOURCE_URI);
		
		if ( names == null ) {
			logger.warning("Property Map doesn't contain property '"+Constants.PROPERTY_RESOURCE_URI+"'.");
			return false;
		}
		
		if ( names.size() != 1 ) {
			logger.warning("'"+Constants.PROPERTY_RESOURCE_URI+"' must have only one value.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check whether property resource type exists in property map.
	 * 
	 * @param props an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return whether property resource type is exists in property map or not.
	 */
	private boolean checkForResourceLocalAt(Map<String, List<String>> props) {
	
		List<String> resourceLocalAts = props.get(Constants.PROP_NAME_RESOURCE_LOCAL_AT);
		
		if ( resourceLocalAts == null ) {
			logger.warning("Property Map doesn't contain property '"+Constants.PROP_NAME_RESOURCE_LOCAL_AT+"'.");
			return false;
		}
		
		if ( resourceLocalAts.size() != 1 ) {
			logger.warning("'"+Constants.PROP_NAME_RESOURCE_LOCAL_AT+"' must have only one value.");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check whether property resource type exists in property map.
	 * 
	 * @param props an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return whether property resource type is exists in property map or not.
	 */
	private boolean checkForResourceGlobalAt(Map<String, List<String>> props) {
			
			List<String> resourceGlobalAts = props.get(Constants.PROP_NAME_GLOBAL_AT);
			
			if ( resourceGlobalAts == null ) {
				logger.warning("Property Map doesn't contain property '"+Constants.PROP_NAME_GLOBAL_AT+"'.");
				return false;
			}
			
			if ( resourceGlobalAts.size() != 1 ) {
				logger.warning("'"+Constants.PROP_NAME_GLOBAL_AT+"' must have only one value.");
				return false;
			}
			
			return true;
		}
}
