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

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.openurc.uch.IProfile;
import org.openurc.uch.ITDM;
import org.openurc.uch.ITDMListener;
import org.openurc.uch.IUCHStore;
import org.openurc.uch.TDMFatalException;
import org.openurc.uch.UCHException;
import org.openurc.uch.UCHNotImplementedException;

import edu.wisc.trace.uch.resource.util.ResourceUtil;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Implements the methods of ITDMListener.
 * Also maintain information about TDMs.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class TDMListener implements ITDMListener {

	private static final long START_DISCOVERY_SLEEP_TIME = 500;
	
	private Logger logger = LoggerUtil.getSdkLogger();	
	
	private UCH uch;
	
	// List of TDMs
	private List<ITDM> tdmList = new ArrayList<ITDM>();
	
	// Map of targetId vs. TDM
	private Map<String, ITDM> targetIdtdmMap = new TreeMap<String, ITDM>();
	
	
	/**
	 * Constructor.
	 * Provide the reference of UCH to the local variable.
	 * 
	 * @param uch an Object of UCH
	 */
	TDMListener(UCH uch) {
		
		this.uch = uch;
	}
	
	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------------ Methods invoked from ITDM Starts --------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	/**
	 * @see org.openurc.uch.ITDMListener#getDocument(String, String, Map)
	 */
	public String getDocument(String uri, String postData,
			Map<String, IProfile> context) throws UCHException {
		
		return uch.getDocument(uri, postData, context);
	}
	
	/**
	 * @see org.openurc.uch.ITDMListener#getIpAddress()
	 */
	public String getIpAddress() {
		
		return uch.getIpAddress();
	}

	/**
	 * @see org.openurc.uch.ITDMListener#getLocalUCHStore()
	 */
	public IUCHStore getLocalUCHStore() {
		
		return uch.getLocalUCHStore();
	}

	/**
	 * @see org.openurc.uch.ITDMListener#getResources(String, List)
	 */
	public List<List<Map<String, List<String>>>> getResources(String sessionId, List<Map<String, List<String>>> resProps) {
	
		return uch.getResources(sessionId, resProps);
	}

	/**
	 * @see org.openurc.uch.ITDMListener#getUCHProps()
	 */
	public Map<String, String> getUCHProps() {
		
		return uch.getUCHProps();
	}

	/**
	 * @see org.openurc.uch.ITDMListener#isImplemented(java.lang.String)
	 */
	public boolean isImplemented(String functionName) {

		Method[] methods = this.getClass().getMethods();
		
		for ( Method method : methods) {
			
			if( method.getName().equals(functionName) )
				return true;
		}
		
		return false;
	}

	
	/**
	 * @see org.openurc.uch.ITDMListener#startUriService(ITDM, String, int, boolean, String, boolean, List)
	 */
	public String startUriService(ITDM tdm, String scheme, int port, boolean portIsFlexible, String basePath,
			boolean basePathIsFlexible, List<Map<String, IProfile>> contexts) throws UCHException {
		
		return uch.startUriService(tdm, scheme, port, portIsFlexible, basePath, basePathIsFlexible, contexts);
	}

	/**
	 * @see org.openurc.uch.ITDMListener#stopUriService(ITDM, String)
	 */
	public void stopUriService(ITDM tdm, String uri) throws UCHException {
		
		uch.stopUriService(tdm, uri);
	}

	/**
	 * @see org.openurc.uch.ITDMListener#uploadResources(List, List)
	 */
	public List<Map<String, String>> uploadResources(
			List<Map<String, List<String>>> props, List<String> resourceUri) {
		
		return uch.uploadResources(props, resourceUri);
	}
	
	/**
	 * @see org.openurc.uch.ITDMListener#targetDiscovered(ITDM, Map, Map, List)
	 */
	public String targetDiscovered(ITDM tdm, Map<String, Object> targetProps, 
			Map<String, String> taProps, List<Map<String, IProfile>> contexts) {
		
		
		if ( tdm == null ) {
			logger.severe("ITDM is null.Don't discover target.");
			return null;
		}
		
		String targetId = uch.targetDiscovered(targetProps, taProps, contexts);
		
		if ( targetId == null ) {
			logger.warning("TargetId is null");
			return null;
		}
		
		synchronized(targetIdtdmMap) {
			targetIdtdmMap.put(targetId, tdm);
		}
		
		return targetId;
	}
	
	/**
	 * @see org.openurc.uch.ITDMListener#targetDiscarded(String)
	 */
	public void targetDiscarded(String targetId) {
		
		synchronized(targetIdtdmMap) {
			targetIdtdmMap.remove(targetId);
		}
		
		uch.targetDiscarded(targetId);
	}

	/**
	 * @see org.openurc.uch.ITDMListener#removeTDM(ITDM)
	 */
	public void removeTDM(ITDM tdm) throws UCHNotImplementedException{

		if ( tdm == null ) {
			logger.warning("TDM is null.");
		}
		
		synchronized (tdmList) {
			tdmList.remove(tdm);
		}
		
		uch.removeTDM(tdm);
	}
	
	/**
	 * @see org.openurc.uch.ITDMListener#addUriServiceContexts(ITDM, String, List)
	 */
	public void addUriServiceContexts(ITDM tdm, String uri, List<Map<String, IProfile>> contexts) throws UCHException {
		
		uch.addUriServiceContexts(tdm, uri, contexts);
	}

	
	/**
	 * @see org.openurc.uch.ITDMListener#getContexts()
	 */
	public List<Map<String, IProfile>> getContexts() {
		
		return uch.getContexts();
	}


	/**
	 * @see org.openurc.uch.ITDMListener#removeUriServiceContexts(ITDM, String, List)
	 */
	public void removeUriServiceContexts(ITDM tdm, String uri,
			List<Map<String, IProfile>> contexts) throws UCHException {
		
		uch.removeUriServiceContexts(tdm, uri, contexts);
	}

	/**
	 * @see org.openurc.uch.ITDMListener#targetContextsAdded(String, List)
	 */
	public void targetContextsAdded(String targetId, List<Map<String, IProfile>> contexts) {
		
		uch.targetContextsAdded(targetId, contexts);
	}

	/**
	 * @see org.openurc.uch.ITDMListener#targetContextsRemoved(String, List)
	 */
	public void targetContextsRemoved(String targetId, List<Map<String, IProfile>> contexts) {

		uch.targetContextsRemoved(targetId, contexts);
	}

	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------------- Methods invoked from ITDM Ends ---------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	
	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------------ Methods invoked from UCH Starts ---------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	/**
	 * Instantiate Local TDMs.
	 */
	void instantiateTDMs() {
		
		List<Map<String, IProfile>> contexts = uch.getContexts();
		
		List<List<Map<String, List<String>>>> tdmPropsList = uch.getResources(null, prepareTDMPropsList());
		
		if ( tdmPropsList == null || tdmPropsList.size() == 0 ) {
			logger.severe("Unable to get any local TDM.");
			return;
		}
			
		List<Map<String, List<String>>> tdmResPropsList	= tdmPropsList.get(0);
		
		if ( tdmResPropsList == null || tdmResPropsList.size() == 0 ) {
			logger.severe("Unable to get any local TDM.");
			return;
		}
		
		for ( Map<String, List<String>>  tdmResProps : tdmResPropsList )	
			instantiateTDM(tdmResProps, contexts);
		
	}
	
	/**
	 * Call the same method in all TDMs.
	 *  
	 * @param contexts an object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	void contextsOpened(List<Map<String, IProfile>> contexts) {
		
		if ( contexts == null ) {
			logger.warning("Contexts are null.");
			return;
		}
		
		for ( ITDM tdm : tdmList ) {
			
			if ( tdm == null )
				continue;
			
			try {
				tdm.contextsOpened(contexts);
			} catch (TDMFatalException e) {
				logger.warning("TDMFatalException : unable open new contexts with TDM '"+tdm.getClass().getName()+"'.");
			}
		}
	}
	
	/**
	 * Call the same method in all TDMs.
	 *  
	 * @param contexts an object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	void contextsClosed(List<Map<String, IProfile>> contexts) {
		
		if ( contexts == null ) {
			logger.warning("Contexts are null.");
			return;
		}
		
		for ( ITDM tdm : tdmList ) {
			
			if ( tdm == null )
				continue;
			
			try {
				tdm.contextsClosed(contexts);
			} catch (TDMFatalException e) {
				logger.warning("TDMFatalException : unable open new contexts with TDM '"+tdm.getClass().getName()+"'.");
			}
		}
	}
	
	/**
	 * Get contexts for specified target.
	 * 
	 * @param targetId a String value of targetId
	 * 
	 * @return an object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	List<Map<String, IProfile>> getTargetContexts(String targetId) {
		
		if ( targetId == null ) {
			logger.warning("TargetId is null.");
			return null;
		}
		
		ITDM tdm = null;
		
		synchronized (targetIdtdmMap) {
			tdm = targetIdtdmMap.get(targetId);
		}
		
		if ( tdm == null ) {
			logger.warning("Unable to get TDM for targetId '"+targetId+"'.");
			return null;
		}
		
		return tdm.getTargetContexts(targetId);
	}
	
	//------------------------------------------------------------------------------------------------------------//
	//------------------------------------- Methods invoked from UCH Ends ----------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	/**
	 * Download specified TDM from resource server and instantiate it.
	 *  
	 * @param tdmName a String value of TDM Name
	 * 
	 * @return a boolean value specifies whether TDM instantiate successfully or not
	 */
	private boolean instantiateTDM(Map<String, List<String>> tdmResProp, List<Map<String, IProfile>> contexts) {
		logger.info("Instanciation of TDMs has startd");
		if ( tdmResProp == null ) {
			logger.warning("TDM Properties is null.");
			return false;
		}
		
		String tdmDLL = CommonUtilities.getFirstItem( tdmResProp.get(Constants.PROPERTY_RES_DYNAMIC_LIB_CLASS) );
		
		if ( tdmDLL == null ) {
			logger.info("Unable to get DLL Name for '"+CommonUtilities.getFirstItem( tdmResProp.get(Constants.PROPERTY_RES_NAME) )+"'.");
			return false;
		}
		
		for (ITDM tdm : tdmList ) {
			
			if ( tdmDLL.equals( tdm.getClass().getName() ) ) 
				return true;	
		}

		String jarFilePathURI = ResourceUtil.getJarFileUri(tdmResProp);
		
		if ( jarFilePathURI == null ) {
			logger.warning("Unable to get Jar File path.");
			return true;
		}
		
		//Parikshit Thakur : 20100829. Changes to get classLoader from uch instead of uchStore.
		/*Object object = getLocalUCHStore().getValue(Constants.CLASS_PATH_VALUE_CLASS_LOADER);
		
		if ( !(object instanceof ClassLoader) ) {
			logger.warning("Unable to get object of '"+Constants.CLASS_PATH_VALUE_CLASS_LOADER+"' from Local UCH Store.");
			return false;
		}
		
		ClassLoader classLoader = (ClassLoader)object;*/
		
		ClassLoader classLoader = uch.getClassLoader();
		if(classLoader == null ){
			return false;
		}
		//Parikshit Thakur : 20100829. Changes end.
		
		// Parikshit Thakur : 20110823. Changes to load multiple jars for a single resource.
		if(jarFilePathURI.endsWith(".jar")){
			if ( !classLoader.addJar( jarFilePathURI ) ){ 
				return false;}
			else {
				logger.info("TDM successfuly loaded from:" + jarFilePathURI );
			}
		}
		else{
			try {
				File resDir = new File(new URI(jarFilePathURI));
				loadJars(resDir, jarFilePathURI, classLoader);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
logger.info("Loading of jars completed..");
				
		//	if ( !classLoader.addJar( jarFilePathURI ) ) 
		//		return false;
	
		// Change Ends.
		
		try {
			
			Class tdmClass = classLoader.loadClass(tdmDLL);
			
			ITDM tdm = (ITDM)tdmClass.newInstance();
			tdm.init(this, ResourceUtil.convertMap(tdmResProp), uch.getUCHProps(), contexts);
			logger.info(tdm + " successfuly initialized");
			tdmList.add(tdm);
			
			// Starting discovery after predefined time interval.
			new Thread( new StartDiscovery(tdm) ).start();
			logger.info("Discovery successfuly started");
			return true;
			
		} catch (InstantiationException e) {
			logger.warning("InstantiationException : Instantiating TDM '"+tdmDLL+"'.");
		} catch (IllegalAccessException e) {
			logger.warning("IllegalAccessException : Instantiating TDM '"+tdmDLL+"'.");
		} catch (TDMFatalException e) {
			logger.warning("IllegalAccessException : Instantiating TDM '"+tdmDLL+"'.");
		} catch (Exception e) {
			logger.warning("Exception : Instantiating TDM '"+tdmDLL+"'.");
		}
		
		return false;
	}
	
	
	
	/**
	 * Loads the jarFiles in classLoader.
	 * @param resDir a File object for the directory containing the jar
	 * @param jarFilePathURI string uri of the directory containing the jar
	 * @param classLoader a ClassLoader object
	 * @return true is jar loaded successfully, else false
	 */
	private boolean loadJars(File resDir, String jarFilePathURI, ClassLoader classLoader ) {
		
		File[] jarFiles = resDir.listFiles(new ExtensionFilter(".jar"));

		if ((jarFiles != null) && (jarFiles.length != 0)) {

			for (File jarFile : jarFiles) {

				if (jarFile == null)
					continue;
				
				if ( !classLoader.addJar( jarFilePathURI + jarFile.getName() ) ) 
					return false;
			}
			
			return true;
		}

		// Get directories to further searching for .jar files
		File[] dirs = resDir.listFiles(new DirectoryFilter());

		if ((dirs != null) && (dirs.length != 0)) {

			for (File dir : dirs) {

				if (dir == null)
					continue;
				loadJars(dir, jarFilePathURI + dir.getName() + "/", classLoader);
			}
		}
		return true;
	}
	
	
	/**
	 * Prepare a List of Resource Properties List for downloading local TDMs.
	 * 
	 * @return an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 */
	private List<Map<String, List<String>>> prepareTDMPropsList() {
		
		List<Map<String, List<String>>> tdmPropsList = new ArrayList<Map<String,List<String>>>();
		
		Map<String, List<String>> tdmProps = new HashMap<String, List<String>>();
		
		tdmProps.put(Constants.PROPERTY_RES_TYPE, CommonUtilities.convertToList(Constants.PROPERTY_RES_TYPE_VALUE_TDM));
		tdmProps.put(Constants.PROPERTY_RUNTIME_PLATFORM, CommonUtilities.convertToList(Constants.PROPERTY_PLATFORM_VALUE_JAVA) );
		tdmProps.put(Constants.RESOURCE_QUERY_PROP_NAME_RESOURCE_LOCATION, CommonUtilities.convertToList(Constants.RESOURCE_QUERY_PROP_NAME_RESOURCE_LOCATION_VALUE_LOCAL) );
		
		String conformsTo = uch.getConformsTo();
		
		if ( conformsTo != null )	
			tdmProps.put(Constants.PROPERTY_CONFORMS_TO, CommonUtilities.convertToList(conformsTo) );
		
		tdmPropsList.add(tdmProps);
		
		return tdmPropsList;
	}
	
	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//--------------------------------------- Utilities Methods Starts -------------------------------------------//
	//------------------------------------------------------------------------------------------------------------//

	/**
	 * Copy entries of specified list and return a new List.
	 * 
	 * @param list an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 * 
	 * @return an Object of List&lt;Map&lt;String, IProfile&gt;&gt;
	 */
	private List<Map<String, IProfile>> cloneList(List<Map<String, IProfile>> list) {
		
		if ( list == null )
			return null;
		
		List<Map<String, IProfile>> cloneList = new ArrayList<Map<String,IProfile>>();
		
		for ( Map<String, IProfile> map : list ) {
			
			if ( map == null )
				continue;
			
			cloneList.add( cloneMap(map) );
		}
		
		return cloneList;
	}
	
	
	/**
	 * Copy entries of specified map and return a new Map.
	 * 
	 * @param map an Object of Map&lt;String, IProfile&gt;
	 * 
	 * @return an Object of Map&lt;String, IProfile&gt;
	 */
	private Map<String, IProfile> cloneMap(Map<String, IProfile> map) {
		
		if ( map == null )
			return null;
		
		Map<String, IProfile> cloneMap = new HashMap<String, IProfile>();
		
		for ( Entry<String, IProfile> entry : map.entrySet() ) {
			
			String key = entry.getKey();
			IProfile value = entry.getValue();
			
			if ( (key == null) || (value == null) )
				continue;
			
			cloneMap.put(key, value);
		}
		
		return cloneMap;
	}
	

	//------------------------------------------------------------------------------------------------------------//
	//---------------------------------------- Utilities Methods Ends --------------------------------------------//
	//------------------------------------------------------------------------------------------------------------//

	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//--------------------------------------- Utilities Classes Starts -------------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
	
	
	/**
	 * Start discovery of specified TDM in separate therad.
	 * 
	 * @author Parikshit Thakur & Team, Trace R&D Center
	 * @version  $ Revision: 1.0 $
	 */
	private class StartDiscovery implements Runnable {
		
		private ITDM tdm;
		
		/**
		 * Provide the reference of ITDM to local variable.
		 * 
		 * @param tdm an Object of ITDM
		 */
		private StartDiscovery(ITDM tdm) {
			
			this.tdm = tdm;
		}
		
		/**
		 * call the startDiscovery method of specified TDM after waiting for predefined time interval.
		 */
		public void run() {
			
			if ( tdm == null )
				return;
			
			try {
				Thread.sleep(START_DISCOVERY_SLEEP_TIME);
			} catch (InterruptedException e) {
				logger.warning("InterruptedException : Sleeping for '"+START_DISCOVERY_SLEEP_TIME+"' ms.");
			}
			
			try {
				tdm.startDiscovery();
			} catch (TDMFatalException e) {
				logger.warning("TDMFatalException : Starting discovery of TDM '"+tdm.getClass().getName()+"'.");
			}
		}
	}
	
	
	
	/**
	 * Implements FilenameFilter.
	 * Provide implementation of the method accept of FilenameFileter interface.
	 * 
	 * @author Parikshit Thakur & Team, Trace R&D Center
	 * @version $ Revision: 1.0 $
	 */
	private class ExtensionFilter implements FilenameFilter {
		
		private String extension;
		 
		/**
		 * Constructor.
		 * Provide reference of the local variable extension.
		 *  
		 * @param extension a String value of file extension
		 */
		private ExtensionFilter( String extension ) {
			
			this.extension = extension;             
		}
		 
		/**
		 * Check whether specified filename ends with given extension.
		 * 
		 * @param dir an Object of File that specifies a directory
		 * @param name a String value of filename
		 * 
		 * @return a boolean value specifies whether specified file name ends with given extension.
		 */
		public boolean accept(File dir, String name) {
			
			return (name.endsWith(extension));
		}
	}

	
	/**
	 * Implement directory filter.
	 * Provide implementation of the method accept of FilenameFileter interface.
	 * 
	 * @author Parikshit Thakur & Team, Trace R&D Center
	 * @version $ Revision: 1.0 $
	 */
	private class DirectoryFilter implements FilenameFilter {

		/**
		 * Default Constructor.
		 */
		private DirectoryFilter() {
			
		}
		
		/**
		 * Check whether specified filename represents directory or not.
		 * 
		 * @param dir an Object of File that specifies a directory
		 * @param name a String value of filename
		 * 
		 * @return a boolean value specifies whether specified filename represents a directory or not
		 */
		public boolean accept(File dir, String name) {
			
			if ( dir == null || name == null ) 
				return false;
			
			File f = new File(dir, name);
			
			return ( f.exists() && f.isDirectory() );
		}
	}
	
	
	
	//------------------------------------------------------------------------------------------------------------//
	//---------------------------------------- Utilities Classes Ends --------------------------------------------//
	//------------------------------------------------------------------------------------------------------------//
}



