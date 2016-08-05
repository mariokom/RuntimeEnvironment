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

package edu.wisc.trace.uch.resource.uploadmanager.local;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide method to create and write data to file.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class RPropFileUtil {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private static final String NODE_NAME_PROPS = "props";
	private static final String NODE_NAME_PROP = "prop";
	private static final String NODE_NAME_NAME = "name";
	private static final String NODE_NAME_VALUE = "value";
	
	
	/**
	 * Create specified rprop(UCH Context File)(.rprop) file and Save specified properties to it.
	 * 
	 * @param rpropFilePathURI a String specifies rprop file path URI string
	 * @param props  an object of Map&lt;String, List&lt;String&gt;&gt; specifies rprop file properties
	 * @param resourceFileRelativePath a String value specifies resource file relative path
	 * 
	 * @return a boolean value specifies whether data saved to file successfully
	 */
	boolean saveRPropFile(String rpropFilePathURI, Map<String, List<String>> props, String resourceFileRelativePath) {
		
		if ( rpropFilePathURI == null ) {
			logger.warning("rprop file path URI is null.");
			return false;
		}
		
		if ( props == null ) {
			logger.warning("rprop file props is null.");
			return false;
		}
		
		if ( resourceFileRelativePath == null ) {
			logger.warning("Resource File relative Path is null.");
			return false;	
		}
		
		File rpropFile = createFile(rpropFilePathURI);
			
		return saveRPropFile(rpropFile, props, resourceFileRelativePath);
	}
	
	
	/**
	 * Save specified properties to specified rprop file.
	 * 
	 * @param rpropFile an object of rprop file.
	 * @param props an object of Map&lt;String, List&lt;String&gt;&gt; specifies rprop file properties
	 * @param resourceFileRelativePath a String value specifies resource file relative path
	 * 
	 * @return a boolean value specifies whether data saved to file successfully
	 */
	private boolean saveRPropFile(File rpropFile, Map<String, List<String>> props, String resourceFileRelativePath) {
	
		if ( rpropFile == null ) {
			logger.warning("rprop file is null.");
			return false;
		}
		
		if ( props == null ) {
			logger.warning("rprop file props is null.");
			return false;
		}
		
		if ( resourceFileRelativePath == null ) {
			logger.warning("Resource File relative Path is null.");
			return false;	
		}
		
		String rpropFileData = preparePropsXml(props, resourceFileRelativePath);
		
		if ( rpropFileData == null ) {
			logger.warning("Unable to cteate rprop File XML data.");
			return false;
		}
		
		try {
			
			FileWriter fw = new FileWriter(rpropFile);
			fw.write(rpropFileData);
			fw.flush();
			fw.close();
			
			logger.info("Data is written to file '"+rpropFile.getAbsolutePath()+"' succesfully.");
			return true;
			
		} catch (IOException e) {
			logger.warning("IOException : Unable to write data to file '"+rpropFile.getAbsolutePath()+"'.");
			return false;
		}
	}
	
	
	/**
	 * Prepare rprop(UCH Context File) XML File data from specified properties.
	 * 
	 * @param props an object of Map&lt;String, List&lt;String&gt;&gt; specifies rprop file properties
	 * @param resourceUri a String value of resource URI
	 * 
	 * @return a String contains XML data
	 */
	private String preparePropsXml(Map<String, List<String>> props, String resourceUri) {
		
		if ( (props == null) || (props.size() == 0) ) {
			logger.warning("Resource Props is null.");
			return null;
		}
		
		if ( resourceUri == null ) {
			logger.warning("Resource URI is null.");
		}
		
		StringBuilder sb = new StringBuilder();
		
		sb.append( XML_HEADER + "\n" );
		sb.append( "<" + NODE_NAME_PROPS + ">\n" );
		
		for ( Entry<String, List<String>> entry : props.entrySet() ) {
			
			if ( entry == null )
				continue;
			
			String propStr = preparePropNodeXmlString( entry.getKey(), entry.getValue() );
			
			if ( propStr == null )
				continue;
			
			sb.append(  propStr );
		}
		
		sb.append(  preparePropNodeXmlString(Constants.PROPERTY_RESOURCE_URI, CommonUtilities.convertToList(resourceUri) ) );
		
		sb.append( "</" + NODE_NAME_PROPS + ">" );
		
		return sb.toString();
	}
	
	
	/**
	 * Prepare 'prop' node XML String using specified property name and property values, and return it. 
	 * 
	 * @param propName a String value of Property Name
	 * @param propValues an object of List&lt;String&gt; of values
	 * 
	 * @return a String value
	 */
	private String preparePropNodeXmlString(String propName, List<String> propValues) {
		
		if ( propName == null )
			return null;
		
		if ( (propValues == null) || (propValues.size() == 0) )
			return null;
		
		StringBuilder sb = new StringBuilder();
		
		for ( String value : propValues ) {
			
			if ( value == null )
				continue;
			
			sb.append( "<" + NODE_NAME_PROP + ">\n" );
				sb.append( "<" + NODE_NAME_NAME + ">" + propName + "</" + NODE_NAME_NAME + ">\n" );
				sb.append( "<" + NODE_NAME_VALUE + ">" + value + "</" + NODE_NAME_VALUE + ">\n" );
			sb.append( "</" + NODE_NAME_PROP + ">\n" );
		}
		
		return sb.toString();
	}
	
	
	/**
	 * Create specified file. If same file or directory exists then delete it and create a new file and return it.
	 * 
	 * @param filePathURI a String value of File Path URI
	 * 
	 * @return an object of File
	 */
	private File createFile(String filePathURI) {
		
		if ( filePathURI == null ) {
			logger.warning("File path URI is null.");
			return null;
		}
		
		if ( !CommonUtilities.deleteFileOrDir(filePathURI) ) {
			logger.warning("Unable to delete file '"+filePathURI+"' if it is exists.");
			return null;
		}
		
		try {
			
			File file = new File( new URI(filePathURI) );
			
			if ( !file.createNewFile() ) {
				logger.warning("Unable to create file '"+filePathURI+"'.");
				return null;
			}
			
			return file;
			
		} catch (URISyntaxException e) {
			logger.warning("URISyntaxException : '"+filePathURI+"' is invalid file path.");
			return null;
		} catch (IOException e) {
			logger.warning("IOException : Unable to create file '"+filePathURI+"'.");
			return null;
		}
		
	}
}
