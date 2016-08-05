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
package edu.wisc.trace.uch.contextmanager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.openurc.uch.IProfile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide methods to parse User Profile File.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version Revision: 1.0
 */
class UserProfileParser {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private static final String NODE_NAME_USER = "user";
	private static final String NODE_NAME_PASSWORD = "password";
	private static final String NODE_NAME_USER_PROFILE = "userProfile";
	private static final String NODE_NAME_PROP = "prop";
	private static final String NODE_NAME_NAME = "name";
	private static final String NODE_NAME_VALUE = "value";
	
	private static final String CONSTANT_USER_NAME = "userName";
	private static final String CONSTANT_USER_PASSWORD = "userPassword";
	
	
	/**
	 * Default Constructor
	 */
	UserProfileParser() {
		
	}
	
	UserProfile parseData(String xmlData, String userName, String password) {
		
		if ( xmlData == null ) {
			logger.warning("XML Data is null.");
			return null;
		}
		
		if ( userName == null ) {
			logger.warning("UserName is null.");
			return null;
		}
		
		if ( password == null ) {
			logger.warning("Password is null.");
			return null;
		}

		Document dom = null;
		
		try {
			
			dom = CommonUtilities.parseXml(xmlData);
			
		} catch (NullPointerException e) {
			logger.warning("NullPointerException : Unable to parse row XML String");
		} catch (SAXException e) {
			logger.warning("SAXException : Unable to parse row XML String");
		} catch (IOException e) {
			logger.warning("IOException : Unable to parse row XML String");
		} catch (ParserConfigurationException e) {
			logger.warning("ParserConfigurationException : Unable to parse row XML String");
		}
		
		if ( dom == null ) {
			logger.warning("Unable to get Document object.");
			return null;
		}
		
		if ( dom == null ) {
			logger.warning("Unable to parse row XML String.");
			return null;
		}

		Element docRoot = dom.getDocumentElement();
		
		if ( docRoot == null ) {
			logger.warning("Unable to parse row XML String.");
			return null;
		}
		
		String nodeName = docRoot.getNodeName();
		
		if ( (nodeName == null) || !nodeName.equals(NODE_NAME_USER) ) {
			logger.warning("Root Node of the row XML String is not '"+NODE_NAME_USER+"'.");
			return null;
		}
		
		if ( !docRoot.hasChildNodes() ) {
			logger.warning("Root Node of the row XML String has no child node.");
			return null;
		}
		
		System.out.println("Going to parse user node.");
		return parseUserNode(docRoot, userName, password);
	}
	
	/**
	 * Parse specified user profile file and return an object of UserProfile.
	 * 
	 * @param userProfileFileURI a String value of User Profile File URI 
	 * 
	 * @return an object of UserProfile
	 */
	UserProfile parseFile(String userProfileFileURI) {
		
		if ( userProfileFileURI == null ) {
			logger.warning("User Profile File URI is null.");
			return null;
		}
		
		Document dom = getDocument(userProfileFileURI);
		
		if ( dom == null ) {
			logger.warning("User Profile File '"+userProfileFileURI+"' is not in proper XML format.");
			return null;
		}

		Element docRoot = dom.getDocumentElement();
		
		if ( docRoot == null ) {
			logger.warning("Unable to get Document Root from the User Profile File '"+userProfileFileURI+"'.");
			return null;
		}
		
		String nodeName = docRoot.getNodeName();
		
		if ( (nodeName == null) || !nodeName.equals(NODE_NAME_USER) ) {
			logger.warning("Root Node of the User Profile File '"+userProfileFileURI+"' is not '"+NODE_NAME_USER+"'.");
			return null;
		}
		
		if ( !docRoot.hasChildNodes() ) {
			logger.warning("Root Node of the User Profile File '"+userProfileFileURI+"' has no child node.");
			return null;
		}
		
		return parseUserNode(docRoot, null, null);
	}
	
	/**
	 * Parse the user node and return an Object of UserProfile.
	 * 
	 * @param userNode an object of Node
	 * 
	 * @return an Object of UserProfile
	 */
	
	private UserProfile parseUserNode(Node userNode, String userName, String password) {
		
		IProfile profile = null;
		
		NodeList userChildList = userNode.getChildNodes();
		
		if ( userChildList == null )
			return null;
		
		for ( int j=0 ; j<userChildList.getLength() ; j++ ) {
			
			Node userChild = userChildList.item(j);
			
			if ( (userChild == null) || (userChild.getNodeType() != Node.ELEMENT_NODE) || !userChild.hasChildNodes() ) 
				continue;
			
			String userChildNodeName = userChild.getNodeName();
			
			if ( (userChildNodeName == null) || !userChildNodeName.equals(NODE_NAME_USER_PROFILE) )
				continue;
			
			profile = parseProfile(userChild);
			
			if ( profile != null ) {
				// Get the properties 'userName' and 'userPassword' from profile object and if exists then remove them from profile object.
				
				if ( userName == null ) {
					
					userName = profile.getValue(CONSTANT_USER_NAME, null);
					
					if ( userName != null )
						profile.removeValue(CONSTANT_USER_NAME, null);
				}
				
				if ( password == null ) {
					
					password = profile.getValue(CONSTANT_USER_PASSWORD, null);
					
					if ( password != null ) 
						profile.removeValue(CONSTANT_USER_PASSWORD, null);	
				}
			}
			
		}
		
		if ( (userName == null) || (password == null) || (profile == null) ) 
			return null;
		
		return new UserProfile(userName, password, profile);
	}
	
	
	/**
	 * Parse the specified profile node and return an object of IProfile.
	 * 
	 * @param profileNode an object of Node
	 * 
	 * @return an object of IProfile
	 */
	private IProfile parseProfile(Node profileNode) {
		
		if ( (profileNode == null) || (profileNode.getNodeType() != Node.ELEMENT_NODE) || !profileNode.hasChildNodes() ) 
			return null;
		
		NodeList nodeList = profileNode.getChildNodes();
		
		if ( nodeList == null )
			return null;
		
		IProfile profile = new Profile();
		
		for ( int i=0 ; i<nodeList.getLength() ; i++ ) {
			
			Node childNode = nodeList.item(i);
			
			if ( (childNode == null) || (childNode.getNodeType() != Node.ELEMENT_NODE) || !childNode.hasChildNodes() ) 
				continue;
			
			String childNodeName = childNode.getNodeName();
			
			if ( (childNodeName == null) || !childNodeName.equals(NODE_NAME_PROP) )
				continue;
			
			NodeList propChildList = childNode.getChildNodes();
			
			if ( propChildList == null ) 
				continue;
			
			String name = null;
			String value = null;
			String password = null;
			
			for ( int j=0 ; j<propChildList.getLength() ; j++ ) {
				
				Node propChild = propChildList.item(j);
				
				if ( (propChild == null) || (propChild.getNodeType() != Node.ELEMENT_NODE) || !propChild.hasChildNodes() ) 
					continue;
				
				String propChildNodeName = propChild.getNodeName();
				
				if ( propChildNodeName == null )
					continue;
				
				if ( propChildNodeName.equals(NODE_NAME_NAME) ) {
					
					Node nameNode = propChild.getFirstChild();
					
					if ( (nameNode != null) && (nameNode.getNodeType() == Node.TEXT_NODE) )
						name = nameNode.getNodeValue();
					
				} else if ( propChildNodeName.equals(NODE_NAME_VALUE) ) {
					
					Node valueNode = propChild.getFirstChild();
					
					if ( (valueNode != null) && (valueNode.getNodeType() == Node.TEXT_NODE) )
						value = valueNode.getNodeValue();
					
				} else if ( propChildNodeName.equals(NODE_NAME_PASSWORD) ) {
					
					Node passwordNode = propChild.getFirstChild();
					
					if ( (passwordNode != null) && (passwordNode.getNodeType() == Node.TEXT_NODE) )
						password = passwordNode.getNodeValue();
					
				}
			}
			
			if ( (name != null) || (value != null) ) {
				
				profile.setValue(name, value, password);
			}
		}
		
		return profile;
	}
	
	
	/**
	 * Create Document Object for specified XML file and return it.
	 * 
	 * @param xmlFileUri a String value of XML file URI
	 * 
	 * @return an Object of Document
	 */
	private Document getDocument(String xmlFileUri) {
		
		if ( xmlFileUri == null )
			return null;
		
		try {
			
			URI uri = new URI( (xmlFileUri).replace(" ", "%20") );
			
			return CommonUtilities.parseDocument(uri);
			
		} catch (URISyntaxException e) {
			logger.warning("URISyntaxException : Parsing XML file '"+xmlFileUri+"'.");
		} catch (FileNotFoundException e) {
			logger.warning("FileNotFoundException : Parsing XML file '"+xmlFileUri+"'.");
		} catch (ParserConfigurationException e) {
			logger.warning("ParserConfigurationException : Parsing XML file '"+xmlFileUri+"'.");
		} catch (IOException e) {
			logger.warning("IOException : Parsing XML file '"+xmlFileUri+"'.");
		} catch (SAXException e) {
			logger.warning("SAXException : Parsing XML file '"+xmlFileUri+"'.");
		}
		
		return null;
	}
}
