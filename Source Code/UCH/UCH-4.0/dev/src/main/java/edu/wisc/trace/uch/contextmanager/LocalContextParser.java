package edu.wisc.trace.uch.contextmanager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
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
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $ Revision: 1.0 $
 */
class LocalContextParser {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private static final String ATTRIBUTE_NAME_ID = "id";
	
	private static final String NODE_NAME_CONTEXTS = "contexts";
	private static final String NODE_NAME_CONTEXT = "context";
	private static final String NODE_NAME_USER = "user";
	private static final String NODE_NAME_USER_NAME = "userName";
	private static final String NODE_NAME_PASSWORD = "password";
	private static final String NODE_NAME_USER_PROFILE = "userProfile";
	private static final String NODE_NAME_PROP = "prop";
	private static final String NODE_NAME_NAME = "name";
	private static final String NODE_NAME_VALUE = "value";
	private static final String NODE_NAME_CONTROLLER_PROFILE = "controllerProfile";
	
	List<Context> parse(String xmlFileURI) {
		
		if ( xmlFileURI == null ) {
			logger.warning("XML File URI is null.");
			return null;
		}
		
		Document dom = getDocument(xmlFileURI);
		
		if ( dom == null ) {
			logger.warning("Context File '"+xmlFileURI+"' is not in proper XML format.");
			return null;
		}

		Element docRoot = dom.getDocumentElement();
		
		if ( docRoot == null ) {
			logger.warning("Unable to get Document Root from the Context File '"+xmlFileURI+"'.");
			return null;
		}
		
		String nodeName = docRoot.getNodeName();
		
		if ( (nodeName == null) || !nodeName.equals(NODE_NAME_CONTEXTS) ) {
			logger.warning("Root Node of the Context File '"+xmlFileURI+"' is not '"+NODE_NAME_CONTEXTS+"'.");
			return null;
		}
		
		if ( !docRoot.hasChildNodes() ) {
			logger.warning("Root Node of the Context File '"+xmlFileURI+"' has no child node.");
			return null;
		}
		
		NodeList nodeList = docRoot.getChildNodes();
		
		if ( nodeList == null ) {
			logger.warning("Unable to get child nodes of the Root Node '"+NODE_NAME_CONTEXTS+"' of the Context File '"+xmlFileURI+"'.");
			return null;
		}
		
		List<Context> contexts = new ArrayList<Context>();
		
		for ( int i=0 ; i<nodeList.getLength() ; i++ ) {
			
			Node childNode = nodeList.item(i);
			
			if ( (childNode == null) || (childNode.getNodeType() != Node.ELEMENT_NODE) || !childNode.hasChildNodes() )
				continue;
			
			String childNodeName = childNode.getNodeName();
			
			if ( (childNodeName == null) || !childNodeName.equals(NODE_NAME_CONTEXT) )
				continue;
			
			Context context = parseContextNode(childNode);
			
			if ( context == null )
				continue;
			
			contexts.add(context);
		}
		
		return contexts;
	}
	
	private Context parseContextNode(Node contextNode) {
		
		if ( (contextNode == null) || (contextNode.getNodeType() != Node.ELEMENT_NODE) || !contextNode.hasChildNodes() )
			return null;
		
		NodeList nodeList = contextNode.getChildNodes();
		
		if ( nodeList == null )
			return null;
		
		String id = null;
		String userName = null;
		String password = null;
		IProfile userProfile = null;
		List<IProfile> controllerProfiles = new ArrayList<IProfile>();
		
		for ( int i=0 ; i<nodeList.getLength() ; i++ ) {
			
			Node childNode = nodeList.item(i);
			
			
			if ( (childNode == null) || (childNode.getNodeType() != Node.ELEMENT_NODE) || !childNode.hasChildNodes() ) 
				continue;
			
			String childNodeName = childNode.getNodeName();
			
			if ( childNodeName == null )
				continue;
			
			if ( childNodeName.equals(NODE_NAME_USER) ) {
				
				id = ((Element)childNode).getAttribute(ATTRIBUTE_NAME_ID);
				
				NodeList userChildList = childNode.getChildNodes();
				
				if ( userChildList == null )
					continue;
				
				for ( int j=0 ; j<userChildList.getLength() ; j++ ) {
					
					Node userChild = userChildList.item(j);
					
					if ( (userChild == null) || (userChild.getNodeType() != Node.ELEMENT_NODE) || !userChild.hasChildNodes() ) 
						continue;
					
					String userChildNodeName = userChild.getNodeName();
					
					if ( userChildNodeName == null )
						continue;
					
					if ( userChildNodeName.equals(NODE_NAME_USER_NAME) ) {
						
						Node userNameNode = userChild.getFirstChild();
						
						if ( (userNameNode != null) && (userNameNode.getNodeType() == Node.TEXT_NODE) )
							userName = userNameNode.getNodeValue();
						
					} else if ( userChildNodeName.equals(NODE_NAME_PASSWORD) ) {
						
						Node passwordNode = userChild.getFirstChild();
						
						if ( (passwordNode != null) && (passwordNode.getNodeType() == Node.TEXT_NODE) )
							password = passwordNode.getNodeValue();
						
					} else if ( userChildNodeName.equals(NODE_NAME_USER_PROFILE) ) {
						
						userProfile = parseProfile(userChild);
					}
					
				}
				
			} else if ( childNodeName.equals(NODE_NAME_CONTROLLER_PROFILE) ) {
				
				IProfile controllerProfile = parseProfile(childNode);
				
				if ( controllerProfile != null )
					controllerProfiles.add(controllerProfile);
			}
		}
		
		if ( (id != null) || (userName != null) || (password != null) || 
				(userProfile != null) || (controllerProfiles.size() != 0) ) {
			
			Context context = new Context(id, userName, password);
			context.setUserProfile(userProfile);
			context.setControllerProfiles(controllerProfiles);
			
			return context;
		}
		
		return null;
	}
	
	
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
	
	/*
	// Just for testing
	public static void main(String[] args) {
		
		LocalContextParser parser = new LocalContextParser();
		
		List<Context> contexts = parser.parse("file:///D:/documents/contexts.xml");
		
		if ( contexts == null ) {
			logger.info("Contexts is null.");
			return;
		}
		
		for(Context context : contexts) {
			
			if ( context == null ) {
				logger.info("Context is null.");
				continue;
			}
			
			logger.info("new Context-----------------------------------------"+context.getId());
			logger.info("User Name = "+context.getUserName());
			logger.info("Password = "+context.getPassword());
			logger.info("User Profile ::::::::\n"+context.getUserProfile()+"\n");
			
			List<IProfile> profiles = context.getControllerProfiles();
			
			if ( profiles == null ) {
				logger.info("Controller Profiles is null.");
			} else {
				
				for( IProfile profile : profiles ) {
					logger.info("Controller Profile ::::::::\n"+profile+"\n");
				}
			}
			logger.info("-------------------------------------------------------");
		}
	}
	*/
}
