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

package edu.wisc.trace.uch.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * CommonUtilities class is a Utility class which provides following functionality.
 * <ul>
 * <li>Store codebase as a public static member</li>
 * <li>parseXml(String rawXML), It parse the XML string and return the Document Object.</li>
 * <li>encodeString(String), It basically replaces all occurrence of special 
 * character to HTML entities. e.g., \"&\" to \"&amp;amp;\" </li>
 * <li>percentEncoding(String in), It performs percentage encoding on input argument 'in' and returns it.
 * e.g., "%", "%25" </li>
 * <li>percentDecoding(String in), It performs percentage decoding on input argument 'in' and returns it.
 * e.g., "%25", "%" </li>
 * </ul>
 *  
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class CommonUtilities {
	
	private static Logger logger = LoggerUtil.getSdkLogger();
	
	/**
	 * Parse a XML String and return a Document Object.
	 * 
	 * @param rawXML a String of XML.
	 * @return an Object of Document
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws NullPointerException
	 */
	public static Document parseXml(String rawXML) throws SAXException,
    		IOException, ParserConfigurationException, NullPointerException {

		if ( rawXML == null ) {
			logger.warning("Raw XML is null.");
			return null;
		}
		
		try {
			Document doc = null;
	
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			doc = db.parse( new InputSource(new StringReader(rawXML)) );
			return doc;
			
		}catch(Exception e){
			logger.warning("Exception : Parsing raw XML.");
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Parse an XML file located at the URI specified and returns the
     * DOM Document resulting from the parse.
     *  
	 * @param uri an URI value points to the location of the XML document to be parsed.
	 * @return a DOM Document Object created from parsing the XML file.
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws NullPointerException
	 */
	public static Document parseDocument(URI uri) throws SAXException,
    		IOException, ParserConfigurationException, NullPointerException {
        
		if ( uri == null ) {
			logger.warning("Documet URI is null.");
			return null;
		}
		
        //DocumentBuilderFactory  produces DOM object trees from XML documents.
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);  
        
        DocumentBuilder db =  dbf.newDocumentBuilder();
		db.setErrorHandler(null);
		return db.parse( uri.toString() );
    }
    
	/**
	 * Parse specified XML file and returns the
     * DOM Document resulting from the parse.
     *  
	 * @param file an Object of File contains the XML document to be parsed.
	 * @return a DOM Document Object created from parsing the XML file.
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws NullPointerException
	 */
	public static Document parseDocument(File file) throws SAXException,
    		IOException, ParserConfigurationException, NullPointerException {
        
		if ( file == null ) {
			logger.warning("File is null.");
			return null;
		}
		
        //DocumentBuilderFactory  produces DOM object trees from XML documents.
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);  
        
        DocumentBuilder db =  dbf.newDocumentBuilder();
		db.setErrorHandler(null);
		return db.parse(file);
    }
	
	/**
	 * Check whether the indexValue is in proper format or not.
	 * 
	 * @param indices a String value of indices
	 * @return whether the indexValue is in proper format or not
	 */
    public static boolean isIndexValueValid(String indices) {
    	
    	if ( indices == null )
    		return false;
    	
    	indices = indices.trim();
    	
    	if ( !indices.startsWith("[") ) 
    		return false;
    	
    	String indexValue = indices.substring(indices.indexOf('[')+1);
    	
    	int nextPos = -1;
		
		while ( ( nextPos = indexValue.indexOf(']') ) != -1 ) {
			
			String dimValue = indexValue.substring(0, nextPos);
			
			if ( dimValue.startsWith(" ") || dimValue.endsWith(" ") ) { // dimValue does not start or end with space. 
				return false;
			}
			
			if ( indexValue.substring(0, nextPos).indexOf('[') != -1 ) { // indexValue is something like [inde[x]
				return false;
			}
			
			if ( (nextPos+1) == indexValue.length() ) // '[' is at last index and dimension value is in proper format.
				return true;
			
			if ( indexValue.charAt(nextPos+1) != '[' ) { // char next to ']' is not '['.
				return false;
			}
			
			indexValue = indexValue.substring(nextPos+2);
		}
		
		if ( indexValue.length() > 0 ) { // there is '[' and/or some text after last ']'. 
			return false;
		}
		
		return true; // dimension value is in proper format.
    }
    
    
	/**
	 * Perform percentage encoding on input argument 'in' and returns it.
     * e.g., "%", "%25"
     * 
	 * @param in a String value
	 * @return percentage encoded String
	 */
	public static String percentEncoding(String in) {

		if ( in == null )
			return null;
		
		in = in.replace("%", "%25");
		in = in.replace("!", "%21");
		in = in.replace("*", "%2A");
		in = in.replace("'", "%27");
		in = in.replace("(", "%28");
		in = in.replace(")", "%29");
		in = in.replace(";", "%3B");
		in = in.replace(":", "%3A");
		in = in.replace("@", "%40");
		in = in.replace("&", "%26");
		in = in.replace("=", "%3D");
		in = in.replace("+", "%2B");
		in = in.replace("$", "%24");
		in = in.replace(",", "%2C");
		in = in.replace("/", "%2F");
		in = in.replace("?", "%3F");
		in = in.replace("#", "%23");
		in = in.replace("[", "%5B");
		in = in.replace("]", "%5D");
		in = in.replace(" ", "%20");

		return in;
	}
	
	/**
	 * Perform percentage decoding on input argument 'in' and returns it.
     * e.g., "%25", "%"
     * 
	 * @param in input String
	 * @return percentage decoded String
	 */
	public static String percentDecoding(String in) {

		if ( in == null )
			return null;
		
		in = in.replace("%25", "%");
		in = in.replace("%21", "!");
		in = in.replace("%2A", "*");
		in = in.replace("%27", "'");
		in = in.replace("%28", "(");
		in = in.replace("%29", ")");
		in = in.replace("%3B", ";");
		in = in.replace("%3A", ":");
		in = in.replace("%40", "@");
		in = in.replace("%26", "&");
		in = in.replace("%3D", "=");
		in = in.replace("%2B", "+");
		in = in.replace("%24", "$");
		in = in.replace("%2C", ",");
		in = in.replace("%2F", "/");
		in = in.replace("%3F", "?");
		in = in.replace("%23", "#");
		in = in.replace("%5B", "[");
		in = in.replace("%5D", "]");
		in = in.replace("%20", " ");

		return in;
	}

	/**
	 * It replaces the following characters of input argument 'in'.
	 * 
	 * <ul>
	 *      <li> & = &amp; </li>
	 *      <li> < = &lt; </li>
	 *      <li> > = &gt; </li>
	 *      <li> ' = &apos; </li>
	 *      <li> " = &quot; </li>
	 * </ul>
	 * 
	 * @param in a String value 
	 * @return encoded String
	 */
	public static String encodeString(String in) {

		if ( in == null )
			return null;
		
		in = in.replace("&", "&amp;");
		in = in.replace("<", "&lt;");
		in = in.replace(">", "&gt;");
		in = in.replace("'", "&apos;");
		in = in.replace("\"", "&quot;");
		
		//in = in.charAt(0)+in.replaceAll("^\\p{ASCII}", "");
		/*
		for (int i=0;i<in.length();i++) {
			
			if ((int)in.charAt(i)>=128) {
				String str = Character.toString(in.charAt(i));
				in = in.replace(str, "");
			}			
		}
		*/
		return in;
	}
	
	/**
	 * Perform XML Decoding.
	 * 
	 * @param in a String to be decoded
	 * 
	 * @return  decoded String 
	 */
	public static String decodeString(String in) {

		if ( in == null )
			return null;
		
		in = in.replace( "&lt;", "<");
		in = in.replace("&gt;",">");
		in = in.replace("&apos;", "'");
		in = in.replace("&quot;", "\"");
		in = in.replace("&amp;", "&");
		
		
		return in;
	}
	
	/**
	 * Copy specified file to specific location.
	 * 
	 * @param sourceFileUri an Object of URI representing source File
	 * @param destinationFilePath a String of destination File Path.
	 * 
	 * @return whether file copied successfully.
	 */
	public static boolean copyFile(URI sourceFileUri, String destinationFilePath) {
		
		File sourceFile = new File(sourceFileUri);
		File destinationFile = new File(destinationFilePath);
		
		if ( !sourceFile.exists() || !sourceFile.isFile() ) {
			logger.warning("'"+sourceFile.getAbsolutePath()+"' does not exists or it is not a file.");
			return false;
		}
		
		if( destinationFile.exists() ) {
			
			if( !destinationFile.delete() ) {
				logger.warning("Unable to delete existed file'"+destinationFile.getAbsolutePath()+"'");
				return false;
			}
		} 
		
		logger.info("Destination File Name : "+destinationFilePath);
		try {
			
			InputStream in = new FileInputStream(sourceFile);
			OutputStream out = new FileOutputStream(destinationFile);
			
			byte[] buf = new byte[1024];
			  
		    int len;
		 
		     while( (len = in.read(buf)) > 0 )
		    	 out.write(buf, 0, len);

		     in.close();
		     out.close();
		 
		     logger.info("File copied successfully from '"+sourceFile.getAbsolutePath()+"' to '"+destinationFile.getAbsolutePath()+"'.");
		     return true;
		} catch (FileNotFoundException e) {
			logger.warning("FileNotFoundException");
			//e.printStackTrace();
		} catch (IOException e) {
			logger.warning("IOException");
			e.printStackTrace();
		}
		
		logger.info("Unable to copy file.");
		return false;
	}
	
	
	/**
	 * Delete specified file or directory.
	 * 
	 * @param uriString a String value of File URI
	 * 
	 * @return whether specified file or directory deleted successfully or not
	 */
	public static boolean deleteFileOrDir(String uriString) {
		
		if ( uriString == null )
			return false;
		
		 URI uri = null ;
		try {
			uri = new URI(uriString);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		if ( uri == null )
			return false;
		
		File f = new File(uri);
		
		if ( !f.exists() ) {
			logger.info("File '"+f.getAbsolutePath()+"' does not exist.");
			return true;
		}
		if ( f.isFile() )
			return deleteFile(f);
		else
			return deleteDir(f);
		
	}
	/*
	public static void main(String[] args) {
		
		File f = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 6.0\\webapps\\UCH\\cache\\2009_02_20T09_59_42Z\\webclient");
		System.out.println(deleteDir(f));
	}
	*/
	/**
     * Delete the specified directory.
     * 
     * @param dir an Object of File
     * @return whether dir deleted successfully 
     */
    private static boolean deleteDir(File dir) {
    	
    	if ( dir == null )
    		return true;
    	
    	if ( !dir.exists() )
    		return true;
    	
    	 File files[] = dir.listFiles();
    	 
    	 for ( File file : files ) {
    		 
    		 if ( file.isDirectory() ) {

    			 if( !deleteDir(file) ) {
    				// System.out.println("Unable to delete Dir : "+file.getAbsolutePath());
    				 return false;
    			 }
    		 } else {
    			 
    			 if( !deleteFile(file) ) {
    				// System.out.println("Unable to delete File : "+file.getAbsolutePath());
    				 return false;
    			 }
    		 }
    	 }
    	
    	return dir.delete();
    }
    
    /**
     * Delete the specified file.
     * 
     * @param file an Object of File
     * @return whether file deleted successfully 
     */
    private static boolean deleteFile(File file) {
    	
    	if ( !file.exists() )
    		return true;
    	
    	return file.delete();
    }
    
    /**
	 * Replace all 'ch1' to 'ch2' from the String 'str' and return it.
	 * 
	 * @param str a String value
	 * @param ch1 a char value
	 * @param ch2 a char value
	 * 
	 * @return a String value
	 */
    public static String replaceAll(String str, char ch1, char ch2) {
		
		if ( str == null )
			return null;
		
		int index;
		while ( ( index = str.indexOf(ch1) ) != -1 ) 
			str = str.substring(0, index) + ch2 + str.substring(index+1);
		
		return str;
	}

	
	/**
	 * Prepare a Map from a String like prop1=val1&prop2=val2&prop1&val11...
	 * 
	 * @param str a String value
	 * 
	 * @return an Object of Map&lt;String, String&gt;
	 */
	public static Map<String, String> prepareKeyValueMap(String str) {
		
		if ( str == null )
			return null;
		
		int equalIndex = str.indexOf('=');
		
		if ( equalIndex == -1 ) 
			return null;
		
		Map<String, String> returnMap = new HashMap<String, String>();
		
		int andIndex = -1;
		while ( ( andIndex = str.indexOf('&') ) != -1 ) {
			
			String key = decodeURL( str.substring(0, equalIndex).trim() );
			String value = decodeURL( str.substring(equalIndex+1, andIndex).trim() );		
			returnMap.put(key, value);
			
			str = str.substring(andIndex+1);
			equalIndex = str.indexOf('=');
		}
		
		if ( equalIndex != -1 ) {
			String key = decodeURL( str.substring(0, equalIndex).trim() );
			String value = decodeURL( str.substring(equalIndex+1).trim() );
			returnMap.put(key, value);
		}
		return returnMap;
	}


	/**
	 * Prepare a Map from a String like prop1=val1&prop2=val2&prop1&val11...
	 * 
	 * @param str a String value
	 * 
	 * @return an Object of Map&lt;String, List&lt;String&gt;&gt;
	 */
	public static Map<String, List<String>> prepareKeyValueListMap(String str) {
		
		if ( str == null )
			return null;
		
		int equalIndex = str.indexOf('=');
		
		if ( equalIndex == -1 ) 
			return null;
		
		Map<String, List<String>> returnMap = new HashMap<String, List<String>>();
		
		int andIndex = -1;
		while ( ( andIndex = str.indexOf('&') ) != -1 ) {
			
			if ( equalIndex > andIndex ) {
				str = str.substring(andIndex+1);
				equalIndex = str.indexOf('=');
				continue;
			}
			
			String key = decodeURL( str.substring(0, equalIndex).trim() );
			String value = decodeURL( str.substring(equalIndex+1, andIndex).trim() );	
			
			if ( returnMap.containsKey(key) ) {
				
				List<String> valueList = returnMap.get(key);
				valueList.add(value);
				
			} else {
				
				List<String> valueList = new ArrayList<String>();
				valueList.add(value);
				returnMap.put(key, valueList);
			}
			
			
			str = str.substring(andIndex+1);
			equalIndex = str.indexOf('=');
		}
		
		if ( equalIndex != -1 ) {
			
			String key = decodeURL( str.substring(0, equalIndex).trim() );
			String value = decodeURL( str.substring(equalIndex+1).trim() );
			
			if ( returnMap.containsKey(key) ) {
				
				List<String> valueList = returnMap.get(key);
				valueList.add(value);
				
			} else {
				
				List<String> valueList = new ArrayList<String>();
				valueList.add(value);
				returnMap.put(key, valueList);
			}
		}
		
		return returnMap;
	}
	
	
	/**
	 * Get Value specified Property.
	 * 
	 * @param propName a String value of Property Name
	 * @param propMap an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return a String value of Property Value
	 */
	public static String getPropValueFromPropMap(String propName, Map<String, List<String>> propMap) {
		
		if ( (propName == null) || (propMap == null) )
			return null;
		
		return CommonUtilities.getFirstItem( propMap.get(propName) );
	}
    
    /**
	 * Get the String value Stored on the 0 index of specified List.
	 * If list is null or empty then return null.
	 *  
	 * @param valueList an Object of List&lt;String&gt;
	 * 
	 * @return a String value;
	 */
	public static String getFirstItem(List<String> valueList) {
		
		if ( valueList == null )
			return null;
		
		try {
			return valueList.get(0);
		} catch( Exception e ) {
			return null;
		}
	}
	
	/**
	 * Get the String value Stored on the 0 index of specified List.
	 * If list is null or empty then return null.
	 *  
	 * @param valueList an Object of List&lt;Object&gt;
	 * @return a String value
	 */
	public static String getFirstObject(List<Object> valueList) {
		
		try {
			return valueList.get(0).toString();
		} catch( Exception e ) {
			return null;
		}
	}
	
	/**
	 * Create an ArrayList and add the str value to its 0 index and return it.
	 * 
	 * @param str a String value
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	public static List<String> convertToList(String str) {
		
		if ( str == null )
			return null;
		
		List<String> strList = new ArrayList<String>();
		
		strList.add(str);
		
		return strList;	
	}
	
	/**
	 * Get the first value from the specified Map.
	 * 
	 * @param valueMap an Object of Map&lt;String, String&gt;
	 * 
	 * @return a String value.
	 */
	public static String convertToString(Map<String, String> valueMap ) {
		
		if ( (valueMap == null) || (valueMap.size() == 0) )
			return null;
		
		for ( String value : valueMap.values() )
			return value;
		
		return null;
	}
	
	/**
	 * Get the value from the first map of specified List.
	 * 
	 * @param valueList an Object of List&lt;Map&lt;String, String&gt;&gt;
	 * 
	 * @return a String value.
	 */
	//Parikshit : added new method
	public static String getStringValueFromList(List<Map<String, String>> valueList){
		
		if ( (valueList == null) || (valueList.size() == 0) )
			return null;
		
		Map<String, String> attrMap = valueList.get(0);
		String value = attrMap.get("value");
		return value;
		
	}
	
	/**
	 * Decode the specified String as par UCH Space.
	 * 
	 * @param in a String value
	 * 
	 * @return a decoded String
	 */
	public static String decodeDimAsUCHSpace(String in ) {
		
		if ( in == null )
			return null;
		
		in = in.replace( "&#x20;", " ");
		
		return in;
	}
	
	/**
	 * Encode the specified String as par UCH Space.
	 * 
	 * @param in a String value
	 * 
	 * @return an encoded String
	 */
	public static String encodeDimAsUCHSpace(String in ) {
		
		if ( in == null )
			return null;
		
		in = in.replace(" ", "&#x20;");
		
		return in;
	}
	
	/**
	 * Encode the specified URL.
	 * 
	 * @param url a String value of URL
	 * 
	 * @return a String value of encoded URL
	 */
	public static String encodeURL(String url) {
		
		if ( url == null )
			return null;
		
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.warning("UnsupportedEncodingException : 'UTF-8' is not supporting encoding scheme.");
			return null;
		}
	}
	
	/**
	 * Decode the specified URL.
	 * 
	 * @param url a String value of URL
	 * 
	 * @return a String value of decoded URL
	 */
	public static String decodeURL(String url) {
		
		if ( url == null )
			return null;
		
		try {	
			return URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.warning("UnsupportedEncodingException : 'UTF-8' is not supporting encoding scheme.");
			return null;
		}
	}
	
	/**
	 * Delete sub-directories from specified directory.
	 * 
	 * @param dirPathUri a String value of path URL
	 * 
	 * @return a boolean value specifies whether sub-directories from specified directory is deleted or not
	 */
	public static boolean deleteSubDirectories(String dirPathUri) {
	    	
	    	if ( dirPathUri == null )
	    		return false;
	    	
	    	dirPathUri = dirPathUri.replaceAll(" ", "%20");
	    	
	    	URI uri = null;
	    	try {
	    		uri = new URI(dirPathUri);
			} catch (URISyntaxException e) {
				
			}
	    	
			if ( uri == null )
				return false;
			
			File dir = new File(uri);
			
			if ( !dir.exists() )
				return false;
			
			if ( dir.isFile() )
				 return false;
			
			File[] subFiles = dir.listFiles();
	    	
			if ( subFiles == null )
				return false;
			
			boolean returnValue = true;
			
			for( File file : subFiles ) {
				
				if ( file == null )
					continue;
				
				if ( file.isFile() ) {
					
					if ( !deleteFile(file) )
						returnValue = false;
					
				} else {
					
					if ( !deleteDir(file) )
						returnValue = false;
				}
			}
			
			return returnValue;
	    }
	  
	
	/**
	 * Utility method to check backward compatibility of 'rdf' attribute.
	 * 
	 * @param ele Element of rSheet
	 * @param attr attribute of an element
	 * @return boolean value for existence of an attribute.
	 */
	public static boolean checkRDFAttribute(Element ele, String attr){
		  
		  if(ele.hasAttribute("rdf:"+attr) || ele.hasAttribute(attr)){
			  
			  return true;
		  }
		  return false;
	  }
	
	/**
	 * Utility method to check backward compatibility of 'rdf' attribute.
	 * 
	 * @param ele Element of rSheet
	 * @param attr attribute of an element
	 * @return boolean value for existence of an attribute.
	 */
	public static String getRDFAttribute(Element ele, String attr){
		  
		  if(ele.hasAttribute("rdf:"+attr)){
			  
			  return ele.getAttribute("rdf:"+attr);
		  }else if(ele.hasAttribute(attr)){
			  
			  return ele.getAttribute(attr);
		  }
		  
		  return null;
	  } 
}
