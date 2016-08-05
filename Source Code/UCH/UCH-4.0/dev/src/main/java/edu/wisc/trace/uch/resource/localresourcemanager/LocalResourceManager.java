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

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.wisc.trace.uch.resource.ResourceManager;
import edu.wisc.trace.uch.resource.util.ResourceUtil;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Parse all .rprop(UCH Config File) files from specific location.
 * Validate these files and save their contents as resources.
 * Also provide methods to find resources from available local resources.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $ Revision: 1.0 $
 */
public class LocalResourceManager {

	private Logger logger = LoggerUtil.getSdkLogger();
	// Parikshit Thakur : 20110825. Changed extension of .ucf file to .rprop
	//private static final String FILE_NAME_EXTENSION_DOT_UCF = ".ucf"; 
	
	private static final String FILE_NAME_EXTENSION_DOT_RPROP = ".rprop";
	
	private static final String CACHE_DIR_NAME = "cache";
	
	private String resourceDirUri;
	
	private ResourceStore resourceStore;
	
	private PropertyValidator propertyValidator;
	
	private PropertyModifier propertyModifier;
	
	private ResourceManager resourceManager;
	
	/**
	 * Constructor.
	 * Parse all .rprop(UCH Config File) files from sub directories of specified Directory.
	 * Validate these files and save their contents as resources.
	 * 
	 * @param resourceDirUri a String value of Resource Directory URI
	 */
	public LocalResourceManager(ResourceManager resourceManager, String resourceDirUri) {
		this.resourceDirUri = getProperResDirURI(resourceDirUri);
		this.resourceManager = resourceManager;
		
		resourceStore = new ResourceStore();
		propertyValidator = new PropertyValidator();
		
		if ( this.resourceDirUri == null ) {
			logger.severe("Unable to get Resource Directory URI");
			return;
		}
		
		propertyModifier = new PropertyModifier(this, resourceDirUri + "/" + CACHE_DIR_NAME);
		
		loadResources();
	}
	
	/**
	 * Call the same method of Resource Manager.
	 * 
	 * @param uri a String value of URI
	 * 
	 * @return return value from Resource Manager
	 */
	boolean isUriContainsResServerAppPath(String uri) {
		
		if ( resourceManager != null )
			return resourceManager.isUriContainsResServerAppPath(uri);
		else
			return false;
	}
	
	
	/**
	 * Call the same method of Resource Manager.
	 * 
	 * @param resourceProps an Object of Map&lt;String, String&gt;
	 * @return an Object of Map&lt;String, Object&gt;
	 */
	Map<String, List<String>> getResServerResource(Map<String, List<String>> resourceProps) {
		
		if ( resourceManager != null )
			return resourceManager.getResServerResource(resourceProps);
		else
			return null;
	}
	
	/**
	 * Get Resources for specified resource properties.
	 * 
	 * @param reqProps an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return an Object of List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;
	 */
	public List<Map<String, List<String>>> getResources(Map<String, List<String>> reqProps) {
		
		//logger.info("Going to get resource for :::::: "+reqProps);
		List<Map<String, List<String>>> resPropsList =  resourceStore.getResources( ResourceUtil.cloneMap(reqProps) );
		//logger.info("Got Resources ::::: "+resPropsList);

		return resPropsList;
	}
	
	/**
	 * Clear all local resource properties and load all resource again.
	 */
	public void reload() {
		
		resourceStore.clear();
		loadResources();
	}
	
	/**
	 * Parse all .rprop(UCH Config File) files from sub directories of Resource Directory, validate them and save their contents as resources.
	 */
	private void loadResources() {
		
		if ( resourceDirUri == null ) {
			logger.severe("Local Resource Directory Path URI is null.");
			return;
		}
		
		File resourceDir = null;
		
		try {
			resourceDir = new File( new URI(resourceDirUri) );
			
		} catch (URISyntaxException e) {
			logger.severe("URISyntaxException : Fetching resources from Resource Dir  failed: Invalid URI '"+resourceDirUri+"'.");
			return;
		}
		
		if ( resourceDir == null ) {
			logger.severe("Unable to get File object of Resource Directory '"+resourceDirUri+"'.");
			return;
		}
		
		if ( !resourceDir.exists() ) {
			logger.severe("'"+resourceDirUri+"' does not exists.");
			return;
		}
		
		if ( !resourceDir.isDirectory() ) {
			logger.severe("'"+resourceDirUri+"' is not a directory.");
			return;
		}
		
		File[] subDirs = resourceDir.listFiles();
		
		if ( (subDirs == null) || (subDirs.length == 0) ) {
			logger.info("'"+resourceDirUri+"' doesn't contain any resource.");
			return;
		}
		
		for ( File subDir : subDirs ) 
			loadResource(subDir, resourceDirUri);
	}
	
	
	/**
	 * Parse .rprop(UCH Config File) file from specified directory, validate it and save its contents as a resource.
	 * 
	 * @param resDir an Object of File represents Resource Directory
	 * @param parentDirURI a String value specifies parent directory path URI
	 */
	private void loadResource(File resDir, String parentDirURI) {
		if (resDir == null) {
			logger.warning("Resource Directory is null.");
			return;
		}
		
		if ( !resDir.exists() ) {
			logger.warning("'" + resDir.getAbsolutePath() + "' does not exists.");
			return;
		}
		
		if ( !resDir.isDirectory() ) {
			logger.warning("'" + resDir.getAbsolutePath() + "' is not a directory.");
			return;
		}
		
		// Get .rprop files and parse it
		File[] rpropFiles = resDir.listFiles( new ExtensionFilter(FILE_NAME_EXTENSION_DOT_RPROP) );
		
		if ( (rpropFiles != null) && (rpropFiles.length != 0) ) {
			
			for ( File rpropFile : rpropFiles ) {
				
				if ( rpropFile == null )
					continue;
				parseRPropFile(rpropFile, parentDirURI + "/" + resDir.getName() );
			}
		}
		
		// Get directories to further searching for .rprop files
		File[] dirs = resDir.listFiles( new DirectoryFilter() );
		
		if ( (dirs != null) && (dirs.length != 0) ) {
			
			for ( File dir : dirs ) {
				
				if ( dir == null )
					continue;
				//logger.info("Directory : "+dir.getAbsolutePath());
				
				loadResource(dir, parentDirURI + "/" + resDir.getName() );
			}
		}
		
	}
	
	/**
	 * Add resource derived by specifies rprop file to the local resources.
	 * 
	 * @param rpropFileURI a String value of rprop file.
	 * 
	 * @return a boolean value specifies whether local resource added successfully or not
	 */
	public boolean addResource(String rpropFileURI) {

		if ( rpropFileURI == null ) {
			logger.warning("rprop file URI is null.");
			return false;
		}
		
		String parentDirPathURI = getParentDirPathURI(rpropFileURI);
		
		File rpropFile = null;
		try {
			rpropFile = new File( new URI(rpropFileURI) );
		} catch (URISyntaxException e) {
			logger.warning("URISyntaxException : Invalid File Path URI '"+rpropFileURI+"'.");
			return false;
		}
		
		return parseRPropFile(rpropFile, parentDirPathURI);
	}
	
	
	/**
	 * Get String value of parent directory path URI
	 * 
	 * @param pathURI a String value of path URI
	 * 
	 * @return a String value of parent directory path URI
	 */
	private String getParentDirPathURI(String pathURI) {
		
		if ( pathURI == null )
			return null;
		
		int slashIndex = pathURI.lastIndexOf('/');
		int backSlashIndex = pathURI.lastIndexOf('\\');
		
		if ( slashIndex == -1 ) {
			
			if ( backSlashIndex == -1 ) 
				return null;
			else 
				return pathURI.substring(0, backSlashIndex);
			
		} else {
			
			if ( backSlashIndex == -1 )
				return pathURI.substring(0, slashIndex);
			else if ( slashIndex > backSlashIndex )
				return pathURI.substring(0, slashIndex);
			else
				return pathURI.substring(0, backSlashIndex);
		}
	}
	
	
	/**
	 * Parse the specified .rprop file.
	 * 
	 * @param rpropFile an Object of File that specifies .rprop file.
	 * @param parentDirPathURI a String value of parent directory path URI.
	 */
	private boolean parseRPropFile(File rpropFile, String parentDirPathURI) {
	
		logger.info("parsing of " + rpropFile.getAbsolutePath());
		
		if ( (rpropFile == null) || (parentDirPathURI == null) )
			return false;
		
		Map<String, List<String>> props = new RPropFileParser(rpropFile).parse();

		logger.info("Found props"+ props);
		
		if ( props == null ){
			
		logger.warning("couldn't find any valide properties in " + rpropFile.getAbsolutePath());
			return false;
		}

		props = propertyModifier.addProperties(props, parentDirPathURI );
		
		if ( !propertyValidator.validateProps(props) ) {
			logger.warning("Failed to validate Properties "+props);
			return false;
		}
		
		return resourceStore.addResource(props);
	}
	
	/**
	 * Remove the ending slash or backslash from the resource directory URI if exists.
	 * 
	 * @param resDirURI a String value of resource directory URI
	 * 
	 * @return a String value
	 */
	private String getProperResDirURI(String resDirURI) {
		
		if ( resDirURI == null )
			return null;
		
		if ( resDirURI.endsWith("/") ) 
			return resDirURI.substring(0, (resDirURI.length()-1) );
		else if ( resDirURI.endsWith("\\") )
			return resDirURI.substring(0, (resDirURI.length()-1) );
		else
			return resDirURI;
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
}
