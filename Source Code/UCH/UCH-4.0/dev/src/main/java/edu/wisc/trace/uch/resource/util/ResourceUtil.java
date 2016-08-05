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
package edu.wisc.trace.uch.resource.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.resource.ResourceManager;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;
import edu.wisc.trace.uch.util.ZipUtilities;

/**
 * Provide methods to use information from resource properties. 
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class ResourceUtil {

	private static Logger logger = LoggerUtil.getSdkLogger();
	
	private static final int BUFFER_SIZE = 1024;
	private static Map<String,String> userProf_vs_Res;
	
	static {
		userProf_vs_Res = new HashMap<String, String>();
		
		userProf_vs_Res.put(Constants.USER_PROFILE_PROPERTY_LANGUAGES, Constants.PROPERTY_RES_FOR_LANG);
		userProf_vs_Res.put(Constants.USER_PROFILE_PROPERTY_UIPM_FAVORITE_PUBLISHER, Constants.PROPERTY_DC_ELEMENTS_PUBLISHER);
		userProf_vs_Res.put(Constants.USER_PROFILE_PROPERTY_UIPM_FAVORITE_SUBSCRIPTION_PACKAGE, Constants.PROPERTY_RES_SUBSCRIPTION_PACAKAGE);
		userProf_vs_Res.put(Constants.USER_PROFILE_PROPERTY_UIPM_FAVORITE_NAME, Constants.PROPERTY_RES_NAME);
	}
	
	/**
	 * Get Jar file path from specified Property Map.
	 * 
	 * @param propMap an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return a String value of Jar file URI
	 */
	public static String getJarFileUri(Map<String, List<String>> propMap) {
		
		if ( propMap == null )
			return null;
		
		String mimeType = CommonUtilities.getFirstItem( propMap.get(Constants.PROPERTY_MIME_TYPE) );
		
		if ( mimeType == null )
			return null;
		
		String resourceLocalAt = CommonUtilities.getFirstItem( propMap.get(Constants.PROP_NAME_RESOURCE_LOCAL_AT) );
		
		if ( resourceLocalAt == null ) {
			logger.warning("Unable to get '"+Constants.PROP_NAME_RESOURCE_LOCAL_AT+"' for '"+propMap.get(Constants.PROPERTY_RES_NAME)+"'.");
			return null;
		}
		
		if ( mimeType.equals(Constants.PROPERTY_MIME_TYPE_VALUE_JAVA_ARCHIVE) ) {
			
			return resourceLocalAt;
			
		} else if ( mimeType.equals(Constants.PROPERTY_MIME_TYPE_VALUE_ZIP) ) {
			
			String indexFile = CommonUtilities.getFirstItem( propMap.get(Constants.PROPERTY_INDEX_FILE) );
			
			if ( indexFile == null )
				return null;
						
			if ( resourceLocalAt.indexOf(indexFile) != -1 ) {
				
				String dirPathUri = resourceLocalAt.substring(0, resourceLocalAt.indexOf(indexFile));
				
				// Parikshit Thakur : 20110823. Changes to load multiple jars for a single resource.
				// Extracting zip file, if resources are zipped.
				
// new start				
				try {
					File dir = new File( new URI( dirPathUri ) );
					String[] files = dir.list();
					
				//	for ( String fileName : files ) {
						
				//		if ( fileName == null )
				//			continue;
						String fileName = dir.getAbsolutePath();
						if(files == null && fileName.endsWith(".zip")){
							try{
								ZipUtilities.unzip(new File( fileName.substring(0, fileName.lastIndexOf("\\")) + File.separator +  fileName.substring(fileName.lastIndexOf("\\"), fileName.lastIndexOf(".zip"))), dir);
								//extractZipFile(dir +File.separator+  fileName, dir);
								//return dirPathUri;
							}
							catch(Exception e){
								e.printStackTrace();
							}
						}
				//	}
					
				} catch (URISyntaxException e) {
					logger.severe("URISyntaxException : finding Jar File Path : invalid URI String '"+dirPathUri+"'.");
				}
// new end
				
// old start				
				/*try {
					File dir = new File( new URI( dirPathUri ) );
					String[] files = dir.list();
					
					for ( String fileName : files ) {
						
						if ( fileName == null )
							continue;
						
						//if ( fileName.endsWith(".jar") ) {
						//	return dirPathUri +  fileName;
						//}
						if(fileName.endsWith(".zip")){
							try{
								ZipUtilities.unzip(new File(dir + File.separator +  fileName), dir);
								//extractZipFile(dir +File.separator+  fileName, dir);
								//return dirPathUri;
							}
							catch(Exception e){
								e.printStackTrace();
							}
						}
					}
					
				} catch (URISyntaxException e) {
					logger.severe("URISyntaxException : finding Jar File Path : invalid URI String '"+dirPathUri+"'.");
				}*/
// old end				
				
				return dirPathUri;
				// Change ends.
			}
		}
		
		return null;
	}
	
	/**
	 * Get specified Resource using specified Resource Manager.
	 * 
	 * @param resourceManager an Object of Resource Manager.
	 * @param resProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an Object of Map&lt;String, List&lt;String&gt;&gt;
	 */
	public static Map<String, List<String>> getSingleResource(ResourceManager resourceManager, Map<String, List<String>> resProps) {
		
		if ( resourceManager == null ) {
			logger.warning("Resource Manager is null.");
			return null;
		}
		
		if ( resProps == null ) {
			logger.warning("Resource Properties is null.");
			return null;
		}
		
		resProps.put(edu.wisc.trace.uch.Constants.RESOURCE_QUERY_PROP_NAME_START, CommonUtilities.convertToList("1") );
		resProps.put(edu.wisc.trace.uch.Constants.RESOURCE_QUERY_PROP_NAME_COUNT, CommonUtilities.convertToList("1") );
		
		List<Map<String, List<String>>> reqPropsList = new ArrayList<Map<String,List<String>>>();
		reqPropsList.add(resProps);
		
		List<List<Map<String, List<String>>>> retPropsList = resourceManager.getResources(null, reqPropsList);
		
		if ( (retPropsList == null) || (retPropsList.size() == 0) )
			return null;
		
		List<Map<String, List<String>>> reqResPrtPropsList = retPropsList.get(0);
		
		if ( (reqResPrtPropsList == null) || (reqResPrtPropsList.size() == 0) )		
			return null;
		
		return reqResPrtPropsList.get(0);
		
	}
	/**
	 * Convert the map of String vs. List&lt;String&gt; to the map of String vs. String.
	 * 
	 * @param map an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an Object of Map&lt;String, String&gt;
	 */
	public static final Map<String, String> convertMap(Map<String, List<String>> map) {
		
		if ( map == null ) {
			logger.warning("Map is null.");
			return null;
		}
		
		Map<String, String> returnMap = new HashMap<String, String>();
		
		for ( Entry<String, List<String>> entry : map.entrySet() ) {
			
			if ( entry == null )
				continue;
			
			returnMap.put(entry.getKey(), CommonUtilities.getFirstItem( entry.getValue() ) );
		}
		
		return returnMap;
	}
	
	
	/**
	 * Clone the specified List.
	 * 
	 * @param list an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 * 
	 * @return an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 */
	public static List<Map<String, List<String>>> cloneList(List<Map<String, List<String>>> list) {
		
		if ( list == null )
			return null;
		
		List<Map<String, List<String>>> returnList = new ArrayList<Map<String,List<String>>>();
		
		for ( Map<String, List<String>> map : list )
			returnList.add( cloneMap(map) );
		
		return returnList;
	}
	
	
	/**
	 * Convert the map of String vs. Object&gt; to the map of String vs. List&lt;String&gt;.
	 * 
	 * @param map an Object of Map&lt;String, Object&gt;
	 * 
	 * @return an Object of Map&lt;String, List&lt;String&gt;&gt;
	 */
	public static Map<String, List<String>> convertObjectMap(Map<String, Object> map) {
		
		if ( map == null )
			return null;
		
		Map<String, List<String>> returnMap = new HashMap<String, List<String>>();
		
		for ( String key : map.keySet() ) {
			
			Object value = map.get(key);
			
			if ( value == null )
				returnMap.put(key, null);
			else {
				List<String> valueList = new ArrayList<String>();
				valueList.add(value.toString());
				returnMap.put(key, valueList);
			}
		}
		
		return returnMap;
	}
	
	
	/**
	 * Clone the specified map.
	 * 
	 * @param map an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * @return an Object of Map&lt;String, List&lt;String&gt;&gt;
	 */
	public static Map<String, List<String>> cloneMap(Map<String, List<String>> map) {
		
		if ( map == null )
			return null;
		
		Map<String, List<String>> cloneMap = new HashMap<String, List<String>>();
		
		for ( Entry<String, List<String>> entry : map.entrySet() ) {
			
			if ( entry == null ) 
				continue;
			
			String key = userProf_vs_Res.get(entry.getKey()); 
				
			cloneMap.put(key==null?entry.getKey():key, cloneStringList(entry.getValue()) );
		}
		
		return cloneMap;
	}
	
	
	/**
	 * Clone the specified List.
	 * 
	 * @param list an Object of List&lt;String&gt;
	 * @return an Object of List&lt;String&gt;
	 */
	private static List<String> cloneStringList(List<String> list) {
		
		if ( list == null )
			return null;
		
		List<String> cloneList = new ArrayList<String>();
		
		for ( String value : list ) 
			cloneList.add(value);
		
		return cloneList;
	}
	
	/**
	 * Get specified file data.
	 * 
	 * @param fileURL a String value of file Path URL
	 * 
	 * @return a String value of filedata.
	 */
	public static String getFileData(String fileURL) {
		
		File file = null;
		
		try {
			file = new File( new URI(fileURL ) );
		} catch (URISyntaxException e1) {
			logger.warning("URISyntaxException : Invalid URI '"+fileURL+"'.");
			return null;
		}
		
		if ( file == null ) {
			logger.warning("File is null");
			return null;
		}
	
		if ( !file.exists() ) {
			logger.warning("'"+file.getAbsolutePath()+"' does not exist.");
			return null;
		}
			
		if ( !file.isFile() ) {
			logger.warning("'"+file.getAbsolutePath()+"' is not a File.");
			return null;
		}
	
		StringBuilder sb = new StringBuilder();
		
		try {
			
			
			FileInputStream in  = new FileInputStream( file );
			
			byte[] data = new byte[BUFFER_SIZE];
			
			int position = -1;
			while( (position = in.read(data)) != -1 ) {
				
				if ( position != BUFFER_SIZE ) {
					
					byte[] newData = new byte[position];
					
					for ( int i=0 ; i<position ; i++ ) 
						newData[i] = data[i];
					
					sb.append( new String(newData) );
					
				} else {
					
					sb.append( new String(data) );
				}
				
				data = new byte[BUFFER_SIZE];
			}
			/*
			char[] charBuffer = new char[BUFFER_SIZE];
			
			while(  fr.read(charBuffer) != -1 ) {
				
				sb.append(charBuffer);
				
				charBuffer = new char[1024];
			}
			*/
			in.close();
			
		} catch (FileNotFoundException e) {
			logger.warning("'"+file.getAbsolutePath()+"' does not found.");
			return null;
		} catch (IOException e) {
			logger.warning("IOException");
			return null;
		}
		
		return sb.toString();
		
	}
}
