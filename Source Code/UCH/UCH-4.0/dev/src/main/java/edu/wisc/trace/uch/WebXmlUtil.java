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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide method to change resource server UserName and Password of Resource Server User.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class WebXmlUtil {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private static final String WEB_XML_NODE_NAME_PARAM_NAME = "param-name";
	private static final String WEB_XML_NODE_NAME_PARAM_VALUE = "param-value";
	
	private static final int BUFFER_SIZE = 1024;
	
	/**
	 * Default Constructor.
	 */
	WebXmlUtil() {
		
	}
	
	/**
	 * Change the value of web XML init parameters userName and password.
	 * 
	 * @param userName a String value of UserName
	 * @param password a String value of Password
	 * @param webXmlUrlString a String value of web.xml file Path URI
	 * 
	 * @return a boolean value specifies whether userName and password changed successfully or not
	 */
	boolean changeWebXmlUserPassword(String userName, String password, String webXmlUrlString) {
		
		if ( userName == null ) {
			logger.warning("UserName is null.");
			return false;
		}
		
		if ( password == null ) {
			logger.warning("Password is null.");
			return false;
		}
		
		if ( webXmlUrlString == null ) {
			logger.warning("web.xml file path URL is null.");
			return false;
		}
		
		File webXmlFile = null;
		
		try {
			webXmlFile = new File( new URI(webXmlUrlString) );
		} catch (URISyntaxException e) {
			logger.warning("URISyntaxException : '"+webXmlUrlString+"' is not valid URI.");
			return false;
		}
		
		StringBuilder sb = readFileData(webXmlFile);
		
		if ( sb == null ) {
			logger.warning("Unable to get file data '"+webXmlUrlString+"'.");
			return false;
		}
		
		if ( !changeUserName(sb, userName) ) {
			logger.warning("Unable to get user name from the file '"+webXmlUrlString+"'.");
			return false;
		}
		
		if ( !changePassword(sb, password) ) {
			logger.warning("Unable to get password from the file '"+webXmlUrlString+"'.");
			return false;
		}
		
		writeFileData( webXmlFile, sb.toString() );
		
		return true;
	}
	
	
	/**
	 * Replace the value of password from the specified StringBuilder.
	 * 
	 * @param sb an Object of StringBuilder
	 * @param newPassword a String value of new Password
	 * 
	 * @return a boolean value specifies whether password value changed successfully or not.
	 */
	private boolean changePassword(StringBuilder sb, String newPassword) {
		
		if ( sb == null )
			return false;
		
		int nameIndex = sb.indexOf("<" + WEB_XML_NODE_NAME_PARAM_NAME + ">" + 
				Constants.CONTEXT_INIT_PARAM_NAME_RESSERVER_PASSWORD + 
		   "</" + WEB_XML_NODE_NAME_PARAM_NAME + ">");

		if ( nameIndex == -1 )
			return false;
		
		int valueIndex = sb.indexOf("<" + WEB_XML_NODE_NAME_PARAM_VALUE + ">", nameIndex);
		
		if ( valueIndex == -1 )
			return false;
		
		int valueEndIndex = sb.indexOf("</" + WEB_XML_NODE_NAME_PARAM_VALUE + ">", valueIndex);
		
		if ( valueEndIndex == -1 )
			return false;
		
		int valueStartIndex = valueIndex + ("<" + WEB_XML_NODE_NAME_PARAM_VALUE + ">").length();
		
		String oldValue = sb.substring(valueStartIndex, valueEndIndex);
		
		logger.info("Old Password : "+oldValue);
		
		sb.replace(valueStartIndex, valueEndIndex, newPassword);
		
		return true;
	}
	
	/**
	 * Replace the value of userName from the specified StringBuilder.
	 * 
	 * @param sb an Object of StringBuilder
	 * @param newUserName a String value of new UserName
	 * 
	 * @return a boolean value specifies whether username value changed successfully or not.
	 */
	private boolean changeUserName(StringBuilder sb, String newUserName) {
		
		if ( sb == null )
			return false;
		
		int nameIndex = sb.indexOf("<" + WEB_XML_NODE_NAME_PARAM_NAME + ">" + 
										Constants.CONTEXT_INIT_PARAM_NAME_RESSERVER_USER_NAME + 
								   "</" + WEB_XML_NODE_NAME_PARAM_NAME + ">");
		
		if ( nameIndex == -1 )
			return false;
		
		int valueIndex = sb.indexOf("<" + WEB_XML_NODE_NAME_PARAM_VALUE + ">", nameIndex);
		
		if ( valueIndex == -1 )
			return false;
		
		int valueEndIndex = sb.indexOf("</" + WEB_XML_NODE_NAME_PARAM_VALUE + ">", valueIndex);
		
		if ( valueEndIndex == -1 )
			return false;
		
		int valueStartIndex = valueIndex + ("<" + WEB_XML_NODE_NAME_PARAM_VALUE + ">").length();
		
		String oldValue = sb.substring(valueStartIndex, valueEndIndex);
		
		logger.info("Old User Name : "+oldValue);
		
		sb.replace(valueStartIndex, valueEndIndex, newUserName);
		
		return true;
	}
	
	/**
	 * Get the specified file data.
	 * 
	 * @param filePathURI a String value of File Path URI
	 * 
	 * @return an Object of StringBuilder
	 */
	private StringBuilder readFileData(File file) {
		
		if ( file == null ) {
			logger.warning("File is null.");
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
			
			FileReader fr = new FileReader( file );
			
			char[] charBuffer = new char[BUFFER_SIZE];
			
			int readLength;
			
			while( ( readLength = fr.read(charBuffer) ) != -1 ) {
				
				if ( readLength == BUFFER_SIZE ) {
					
					sb.append(charBuffer);
					
				} else {
					
					int index = 0;
					
					while ( index < readLength ) {
						
						sb.append( (char)charBuffer[index] );
						index++;
						
					}
				}
				
				charBuffer = new char[1024];
				readLength = -1;
			}
			
			fr.close();
			
			return sb;
			
		} catch (FileNotFoundException e) {
			logger.warning("FileNotFoundException : '"+file.getAbsolutePath()+"' does not found.");
			return null;
		} catch (IOException e) {
			logger.warning("IOException : Reading file '"+file.getAbsolutePath()+"'.");
			return null;
		}
	
	}
	
	/**
	 * Write the specified data to the specified file.
	 * 
	 * @param file an Object of File
	 * @param data a String value of file data
	 * 
	 * @return a boolean value specifies whether data is written to the file successfully or not
	 */
	private boolean writeFileData(File file, String data) {
		
		if ( file == null ) {
			logger.warning("File is null");
			return false;
		}
		
		if ( data == null ) {
			logger.warning("File Data is null.");
			return false;
		}
		
		if ( file.exists() ) {
			
			if( !file.delete() ) {
				logger.warning("Unable to remove the existing file '"+file.getAbsolutePath()+"'.");
				return false;
			}
		}
		
		try {
			
			if ( !file.createNewFile() ) {
				logger.warning("Unable to create new file '"+file.getAbsolutePath()+"'.");
				return false;
			}
			
			BufferedOutputStream out = new BufferedOutputStream( new FileOutputStream(file) );
			
			out.write( data.getBytes() );
			
			out.flush();
			
			out.close();
			
		} catch (IOException e) {
			logger.warning("IOException : Try to write file '"+file.getAbsolutePath()+"'.");
			//e.printStackTrace();
			return false;
		}
		
		logger.info("File '"+file.getAbsolutePath()+"' is written successfully.");
		return true;
	}

	/*
	//Just for testing
	public static void main(String[] args) {
		
		
		// User validation test
		//ResUserUtil util = new ResUserUtil("http://res.dotui.com/resserver");
		
		//if ( util.isUserValid("LiveCD2009", "uchworks") ) 
		//	logger.info("UserName and Password is valid.");
		//else
		//	logger.info("UserName and Password is invalid.");
		
		
		String path = "file:///C:/Documents and Settings/plenar/Desktop/UCH/WEB-INF/web.xml".replaceAll(" ", "%20");
		//WebXmlUtil util = new WebXmlUtil("http://res.dotui.com/resserver");
		WebXmlUtil util = new WebXmlUtil();
		util.changeWebXmlUserPassword("Sandip", "Patel", path);
		
	}
	*/
}
