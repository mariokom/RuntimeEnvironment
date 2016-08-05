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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide a Store to save local resources.
 * Also provide methods to add and find resources from the store.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $ Revision: 1.0 $
 */
class ResourceStore {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private static final String PROP_VALUE_STAR = "*";
	private static final String PROP_NAME_NAME = "name";
	private static final String PROP_NAME_MODIFIED = "modified";
	
	private Map<String, Map<String, List<String>>> resNamePropsMap = new TreeMap<String, Map<String,List<String>>>();
	private Map<String, List<Map<String, List<String>>>> resTypePropsMap = new TreeMap<String, List<Map<String,List<String>>>>();
	
	/**
	 * Default Constructor.
	 */
	ResourceStore() {
		
	}
	
	
	/**
	 * Clear all properties map list.
	 */
	void clear() {
		logger.info("Going to clear resource store.");
		resNamePropsMap = new TreeMap<String, Map<String,List<String>>>();
		resTypePropsMap = new TreeMap<String, List<Map<String,List<String>>>>();	
	}
	
	
	/**
	 * Get Resources for specified resource properties list.
	 * 
	 * @param resPropsList an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 * 
	 * @return an Object of List&lt;List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;&gt;
	 */
	List<List<Map<String, List<String>>>> getResources(List<Map<String, List<String>>> resPropsList) {
		
		if ( resPropsList == null ) {
			logger.warning("Resource Properties List is null.");
			return null;
		}
		
		List<List<Map<String, List<String>>>> returnList = new ArrayList<List<Map<String,List<String>>>>();
		
		for ( Map<String, List<String>> resProps : resPropsList ) {
			
			if ( resProps == null )
				continue;
			
			returnList.add( getResources(resProps) );
		}
		
		return returnList;
	}
	
	
	/**
	 * Get Resources for specified resource properties.
	 *
	 * 
	 * @param reqProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 */
	List<Map<String, List<String>>> getResources(Map<String, List<String>> reqProps) {
		
		if ( reqProps == null ) {
			logger.warning("Resource Properties is null.");
			return null;
		}
		
		removeGetContent(reqProps);
		modifyPropertyName(reqProps);
		int start = getStart(reqProps);
		int count = getCount(reqProps);
		
		// Find Resources from Resource Name vs. Resource Properties Map.
		List<String> resNames = reqProps.get(Constants.PROPERTY_RES_NAME);
		
		if ( (resNames != null) && (resNames.size() != 0) ) {
			
			if ( resNames.size() != 1) { // It is not possible that a Resource has more than one name.
				return null;
			}
			
			return returnResources(start, count, findResourcesByResName( CommonUtilities.getFirstItem(resNames), reqProps) );
		}
		
		// Find Resources from Resource Type vs. Resource Properties Map.
		List<String> resTypes = reqProps.get(Constants.PROPERTY_RES_TYPE);
		
		if ( (resTypes != null) && (resTypes.size() != 0) ) {
			
			return returnResources(start, count, findResourcesByResType(CommonUtilities.getFirstItem(resTypes), reqProps) );
		}
		
		return  returnResources(start, count, findResources(reqProps) );
	}
	
	/**
	 * Get props from start to count and return it.
	 * start is 1 based int value.
	 * count is also 1 based int value.
	 * 
	 * @param start an int value 
	 * @param count in int value
	 * @param resPropsList an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 * 
	 * @return an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 */
	private List<Map<String, List<String>>> returnResources(int start, int count, List<Map<String, List<String>>> resPropsList) {
			
		if ( (start <= 0) || (count <= 0) )
			return resPropsList;
		
		if ( resPropsList == null )
			return null;
		
		int size = resPropsList.size();
		
		if ( size < start )
			return null;
		
		List<Map<String, List<String>>> returnList = new ArrayList<Map<String,List<String>>>();
		
		for ( int i = start-1 ; ( ( i < (start+count-1) ) && (i < size) ) ; i++ ) 
			returnList.add( resPropsList.get(i) );
		
		return returnList;
	}

	/**
	 *  Change the property name 'name' to 'http://openurc.org/ns/res#name' if exists.
	 *  Change the property name 'modified' to 'http://purl.org/dc/terms/modified' if exists.
	 *  
	 * @param reqProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 */
	private void modifyPropertyName(Map<String, List<String>> reqProps) {
		
		if ( reqProps == null )
			return;
		
		List<String> values = reqProps.remove(PROP_NAME_NAME);
		
		if ( values != null )
			reqProps.put(Constants.PROPERTY_RES_NAME, values);
		
		values = reqProps.remove(PROP_NAME_MODIFIED);
		
		if ( values != null )
			reqProps.put(Constants.PROPERTY_DC_TERMS_MODIFIED, values);
	}
	
	/**
	 *  Remove the property 'getContent' if it is exists.
	 *  
	 * @param reqProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 */
	private void removeGetContent(Map<String, List<String>> reqProps) {
		
		if ( reqProps == null )
			return;
		
		 reqProps.remove(Constants.RESOURCE_QUERY_PROP_NAME_GET_CONTENT);
	}
	
	
	/**
	 * Get the int value of property 'start' and if exists then remove it.
	 * If property doesn't exist then return -1.
	 * 
	 * @param reqProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an int value
	 */
	private int getStart(Map<String, List<String>> reqProps) {
		
		if ( reqProps == null )
			return -1;
		
		String startStr = CommonUtilities.getFirstItem( reqProps.remove(Constants.RESOURCE_QUERY_PROP_NAME_START) );
		
		if ( startStr == null )
			return -1;
		
		try {
			return Integer.parseInt(startStr);
		} catch(NumberFormatException e) {
			return -1;
		}
	}
	
	/**
	 * Get the int value of property 'count' and if exists then remove it.
	 * If property doesn't exist then return -1.
	 * 
	 * @param reqProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an int value
	 */
	private int getCount(Map<String, List<String>> reqProps) {
		
		if ( reqProps == null )
			return -1;
		
		String startStr = CommonUtilities.getFirstItem( reqProps.remove(Constants.RESOURCE_QUERY_PROP_NAME_COUNT) );
		
		if ( startStr == null )
			return -1;
		
		try {
			return Integer.parseInt(startStr);
		} catch(NumberFormatException e) {
			return -1;
		}
	}
	
	
	/**
	 * Find Resources using the the request property resource name. It makes searching of resource easy. 
	 * 
	 * @param resName a String value of Resource Name
	 * @param reqProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 *  
	 * @return an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 */
	private List<Map<String, List<String>>> findResourcesByResName(String resName, Map<String, List<String>> reqProps) {
		
		if ( (resName == null) || (reqProps == null) ) 
			return null;
		
		synchronized(resNamePropsMap) {
			
			Map<String, List<String>> resProps = resNamePropsMap.get(resName);
			
			if ( resProps == null ) 
				return null;
			
			/*if ( !doesFound(reqProps, resProps) )
				return null;
			
			return createList(resProps);*/
			
			if ( !doesFound(reqProps, resProps,false) )
				return null;
			
			return createList(resProps);
		}	
	}
	
	
	/**
	 * Find Resources using the the request property resource type. It makes searching of resource easy. 
	 *  
	 * @param resType a String value of Resource Type
	 * @param reqProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt; 
	 */
	private List<Map<String, List<String>>> findResourcesByResType(String resType, Map<String, List<String>> reqProps) {
		
		if ( (resType == null) || (reqProps == null) ) 
			return null;
		
		List<Map<String, List<String>>> returnList = new ArrayList<Map<String,List<String>>>();
		
		synchronized(resTypePropsMap) {
			
			List<Map<String, List<String>>> resPropsList = resTypePropsMap.get(resType);
			
			if ( resPropsList == null )
				return null;
			
			for ( Map<String, List<String>> resProps : resPropsList ) {
				
				if ( resProps == null ) 
					continue;
				
				if ( !doesFound(reqProps, resProps,false) )
					continue;
				
				returnList.add(resProps);
			}
		}
//old-start
		/*if ( returnList.size() == 0 )
			return null;
		else 
			return returnList;*/
//old-end
		
//Yuvaraj-start
		if ( returnList.size() == 0 ) {
			synchronized(resTypePropsMap) {
				
				List<Map<String, List<String>>> resPropsList = resTypePropsMap.get(resType);
				
				if ( resPropsList == null )
					return null;
				
				for ( Map<String, List<String>> resProps : resPropsList ) {
					
					if ( resProps == null ) 
						continue;
					
					if ( !doesFound(reqProps, resProps,true) )
						continue;
					
					returnList.add(resProps);
				}
				
			}
		}
		
		
		if ( returnList.size() == 0 )
			return null;
		else 
			return returnList;
//Yuvaraj-end		
		
		
	}
	
	
	/**
	 * Find resources from available resources list using implemented matching algorithm.
	 * 
	 * @param reqProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt; 
	 */
	private List<Map<String, List<String>>> findResources(Map<String, List<String>> reqProps) {
		
		if (reqProps == null) 
			return null;
		
		List<Map<String, List<String>>> returnList = new ArrayList<Map<String,List<String>>>();
		
		synchronized(resNamePropsMap) {
			
			for ( Map<String, List<String>> resProps : resNamePropsMap.values() ) {
				
				if ( resProps == null )
					continue;
				
				if ( !doesFound(reqProps, resProps,false) )
					continue;
				
				returnList.add(resProps);
			}
		}
		
		/*if ( returnList.size() == 0 )
			return null;
		else 
			return returnList;*/
		if (returnList.size() == 0) {
			synchronized (resNamePropsMap) {

				for (Map<String, List<String>> resProps : resNamePropsMap
						.values()) {

					if (resProps == null)
						continue;

					if (!doesFound(reqProps, resProps, true))
						continue;

					returnList.add(resProps);
				}
			}
		}
		if ( returnList.size() == 0 )
			return null;
		else 
			return returnList;
	}
	
	
	/**
	 * Check whether requested properties found from resource properties.
	 * 
	 * @param reqProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * @param resProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return a boolean value specifies whether requested properties found from resource properties
	 */
	/*private boolean doesFound(Map<String, List<String>> reqProps, Map<String, List<String>> resProps) {
		
		if ( (reqProps == null) || (resProps == null) )
			return false;
		
		for ( Entry<String, List<String>> entry : reqProps.entrySet() ) {
			
			if ( entry == null )
				continue;
			
			String key = entry.getKey();
			
			if ( key == null )
				continue;
			
			List<String> valueList = entry.getValue();
			
			if ( valueList == null )
				continue;
			
			if ( !resProps.containsKey(key) )
				return false;
			
			if ( !doesFound( valueList, resProps.get(key) ) )
				return false;	
		}
		
		return true;
	}*/
	
private boolean doesFound(Map<String, List<String>> reqProps, Map<String, List<String>> resProps, boolean needOredResult) {
		
		if ( (reqProps == null) || (resProps == null) )
			return false;
		
		for ( Entry<String, List<String>> entry : reqProps.entrySet() ) {
			
			if ( entry == null )
				continue;
			
			String key = entry.getKey();
			
			if ( key == null )
				continue;
			
			List<String> valueList = entry.getValue();
			
			if ( valueList == null )
				continue;
			
			if ( !resProps.containsKey(key) )
				return false;
			
			if ( !doesFound( valueList, resProps.get(key),needOredResult ) )
				return false;	
		}
		
		return true;
	}
	/**
	 * Check whether requested values found from resource values.
	 * If resource properties contains value '*' then return true.
	 * 
	 * @param reqList an Object of List&lt;String&gt;
	 * @param resList an Object of List&lt;String&gt;
	 * 
	 * @return a boolean value specifies whether requested values found from resource values
	 */
	private boolean doesFound(List<String> reqList, List<String> resList, boolean needOredResult) {
		
		if ( (reqList == null) || (resList == null) )
			return false;
		
		if ( resList.contains(PROP_VALUE_STAR) ) // Any property is accepted.
			return true;
		
		// Yuvaraj:2013-08-21
		  
		/*for ( String value : reqList) {
			if ( !resList.contains(value) )
				return false;
		}
		return true;*/
		
		
		//If this property occurs multiple times in the resource query request, the values should be ORed.
		if (needOredResult) {
			for (String value : reqList) {

				if (resList.contains(value))
					return true;
			}

			return false;
			
		} else {
			
			for (String value : reqList) {
				
				if (!resList.contains(value))
					return false;
			}
			return true;
		}
	}
	
	
	/**
	 * Add specified Resource Properties to local maps.
	 * Make entry of Resource Properties in Resource Name and Resource Type Map.
	 * 
	 * @param resProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return a boolean value specifies whether Resource Properties added successfully or not
	 */
	boolean addResource(Map<String, List<String>> resProps) {
		
		if ( resProps == null ) {
			logger.warning("Resource Properties is null.");
			return false;
		}
		
		String resName = CommonUtilities.getFirstItem( resProps.get(Constants.PROPERTY_RES_NAME) );
		
		if ( resName == null ) {
			logger.warning("Property Map doesn't contain property '"+Constants.PROPERTY_RES_NAME+"'.");
			return false;
		}
		
		List<String> resTypes = resProps.get(Constants.PROPERTY_RES_TYPE);
		
		if ( (resTypes == null) || (resTypes.size() == 0) ) {
			logger.warning("Property Map doesn't contain property '"+Constants.PROPERTY_RES_TYPE+"'.");
			return false;
		}
			
		putResNameAmdResProps(resName, resProps);
		
		for ( String resType : resTypes ) {
			
			if ( resType == null )
				continue;
			
			putResTypeAndResProps(resType, resProps);
		}
		logger.info(resProps + " successfuly added");
		return true;
	}
	
	
	/**
	 *  Add specified Resource Name and Resource Properties in Resource Name vs. Resource Properties Map.
	 *  If entry of Resource Name exists then replace Resource Properties.
	 *  Else put an entry of Resource Name and Resource Properties.
	 *  
	 * @param resName a String value of Resource Name
	 * @param resProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return a boolean value specifies whether Resource Name and Resource Properties are added successfully
	 */
	private boolean putResNameAmdResProps(String resName, Map<String, List<String>> resProps) {
		
		if ( (resName == null) || (resProps == null) )
			return false;
		
		synchronized(resNamePropsMap) {
			
			resNamePropsMap.put(resName, resProps);
		}
		
		return true;
	}
	
	
	/**
	 * Add specified Resource Type and Resource Properties in Resource Type vs. Resource Properties Map.
	 * If map contains entry of Resource Type then it add the Resource Properties Map to corresponding list of values.
	 * Else create a list of Resource Properties Map and add Resource Properties Map to it.
	 * 
	 * @param resType a String value of Resource Type
	 * @param resProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 *
	 * @return a boolean value specifies whether Resource Type and Resource Properties are added successfully
	 */
	private boolean putResTypeAndResProps(String resType, Map<String, List<String>> resProps) {
		
		if ( (resType == null) || (resProps == null) )
			return false;
		
		synchronized(resTypePropsMap) {
			
			if ( resTypePropsMap.containsKey(resType) ) {
				
				List<Map<String, List<String>>> values = resTypePropsMap.get(resType);
				
				if ( values == null ) {
					
					values = new ArrayList<Map<String, List<String>>>();
					values.add(resProps);
					resTypePropsMap.put(resType, values);
					
				} else {
					
					values.add(resProps);
				}
				
			} else {
				
				List<Map<String, List<String>>> values = new ArrayList<Map<String, List<String>>>();
				values.add(resProps);
				resTypePropsMap.put(resType, values);
			}
		}
		
		return true;
	}
	
	
	/**
	 * Create a list and add specified map to it and return it.
	 * 
	 * @param map an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt; 
	 */
	private List<Map<String, List<String>>> createList(Map<String, List<String>> map) {
		
		List<Map<String, List<String>>> list = new ArrayList<Map<String,List<String>>>();
		list.add(map);
		
		return list;
	}
}
