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

package edu.wisc.trace.uch.util.socket;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.openurc.uch.ITAListener;
import org.openurc.uch.TAException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.Constants;
import edu.wisc.trace.uch.util.LoggerUtil;
/**
 * Parses the Target Description file and 
 * provides access to the components contained within. 
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class TargetDescription {
	
	private Logger logger = LoggerUtil.getSdkLogger();
	
	private ITAListener taListener;
	
	private String name;
	private String id;
	private String conformsTo;
	private String created;
	private String creator;
	private String modified;
	boolean hidden;
	
	private Map<String,Locator> locators = new HashMap<String, Locator>();
	private List<SocketDescription> sockets = new ArrayList<SocketDescription>();
	
	// 2012-09-14 : No need to parse 'grpSheet'.
	//List<Map<String, List<String>>> grpSheetPropsList = new ArrayList<Map<String, List<String>>>();
	
	private String targetDescriptionAt;
	
	private URI tdUri;
	
	/**
	 * Constructor.
	 * Parse the Target Description File(.td) and Store its Elements and attributes of that Element.
	 * 
	 * @param tdUri URI value of tdUri
	 * @throws TAException
	 */
	public TargetDescription(URI tdUri) throws TAException {
		
		this.tdUri = tdUri;
		logger.info(tdUri.toString());
		
		parse();
		
		//targetDescriptionAt = tdUri.toString().substring(tdUri.toString().lastIndexOf("/"));
		targetDescriptionAt = tdUri.toString();
		
		// Just for testing
		/*
		for (SocketDescription socket : sockets) {
			showSockets( socket.getSocketElements() );
		}
		*/
	}
	
	/**
	 * Constructor.
	 * Download Target Description File(.td) from Resource Server using  taProps, parse it and Store its Elements and attributes of that Element.
	 * 
	 * @param tdProps an Object of Map&lt;String, String&gt; of Target Description File Properties
	 * @param taListener an Object of ITAListener
	 * 
	 * @throws TAException an Object of TAException
	 */
	public TargetDescription(Map<String, String> tdProps, ITAListener taListener) throws TAException {
		
		if ( taListener == null ) {
			logger.warning("TA Listener is null.");
			throw new TAException();
		}
			
		if( (tdProps == null) || (tdProps.size() == 0) ) {
			logger.warning("Target Description Properties is null.");
			throw new TAException();
		}
		
		this.taListener = taListener;
		
		String tdResUri = getFileFromResourceServer(tdProps, taListener);
		
		if ( tdResUri == null ) {
			logger.warning("Unable to get Target Description File from Resource Server.");
			throw new TAException();
		}
		
		try {
			tdUri = new URI( tdResUri.toString() );
		} catch (URISyntaxException e) {
			logger.severe("URISyntaxException, check URI : '"+tdResUri+"'");
			e.printStackTrace();
			throw new TAException();			
		}
		
		parse();
		
		targetDescriptionAt = tdUri.toString();
	}
	
	
	/**
	 * Get the sockets of this Target.
	 * 
	 * @return a List&lt;SocketDescription&gt; of Sockets
	 */
	public List<SocketDescription> getSockets() {
		return sockets;
	}
	
	/**
	 * Get String value of TargetName('about' attribute of &lt;target&gt; Element).
	 * 
	 * @return a String value of Target Name
	 */
	public String getTargetName() {
		return name;
	}
	
	/**
	 * Get the String value of the &lt;conformsTo&gt; Element of Target Description File(.td)
	 * 
	 * @return a String value of the &lt;conformsTo&gt; Element of Target Description File(.td)
	 */
	public String getConformsTo() {
		return conformsTo;
	}

	/**
	 * Get the String value of the &lt;created&gt; Element of Target Description File(.td)
	 * 
	 * @return a String value of the &lt;created&gt; Element of Target Description File(.td)
	 */
	public String getCreated() {
		return created;
	}

	/**
	 * Get the String value of the &lt;creator&gt; Element of Target Description File(.td)
	 * 
	 * @return a String value of the &lt;creator&gt; Element of Target Description File(.td)
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * Get the String value of the &lt;hidden&gt; Element of Target Description File(.td)
	 * 
	 * @return a String value of the &lt;hidden&gt; Element of Target Description File(.td)
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * Get the String value of the &lt;modified&gt; Element of Target Description File(.td)
	 * 
	 * @return a String value of the &lt;modified&gt; Element of Target Description File(.td)
	 */
	public String getModified() {
		return modified;
	}
	
	/**
	 * Get Friendly Name of the Socket specified by 'socketName'.
	 * 
	 * @param socketName a String value of socketName
	 * @return a String value of Socket Friendly Name
	 */
	public String getSocketFriendlyName(String socketName) {
		
		for(SocketDescription socketDescription : sockets) {
			
			if( socketDescription.getSocketName().equals(socketName))
				return socketDescription.getFriendlyName();
		}
		
		return null;
	}
	
	/**
	 * Get Socket Name for the SocketId specified 'socketId'.
	 * 
	 * @param socketId a String value of socketId
	 * @return a String value of Socket Name
	 */
	public String getSocketName(String socketId) {
		
		for(SocketDescription socketDescription : sockets) {
			
			if( socketDescription.getSocketId().equals(socketId))
				return socketDescription.getSocketName();
		}
		
		return null;
	}
	
	/**
	 * Return List of Socket Names available in this Target Description.
	 * 
	 * @return List&lt;String&gt; value of Socket Names
	 */
	
	public List<String> getSocketNames() {
		
		List<String> socketNames = new ArrayList<String>();

		for(SocketDescription socketDescription : sockets) 
			socketNames.add( socketDescription.getSocketName() );
		
		if ( socketNames.size()==0 )
			return null;
		else 
			return socketNames;
		
	}
	
	/**
	 * Return List&lt;IUISocketElement&gt; of IUISocketElement of the Socket specified by 'socketName'.
	 * 
	 * @param socketName a String value of socketName
	 * @return List&lt;IUISocketElement&gt; of IUISocketElements
	 */
	public List<IUISocketElement> getSocketElements(String socketName) {
		
		for(SocketDescription socket : sockets) {
			
			if( socketName.equals(socket.getSocketName()) )
				return socket.getSocketElements();
		}
		return null;
	}
	

	/**
	 * a URI that points to the location of Target Description File(.td).
	 * 
	 * @return URI that points to the location of Target Description File(.td).
	 */
	public URI getTdUri() {
		return tdUri;
	}
	
	/**
	 * Return a String value of URI that points to the location of Target Description File(.td).
	 * 
	 * @return a String value of URI that points to the location of Target Description File(.td).
	 */
	public String getTargetDescriptionUri() {
		
		return targetDescriptionAt;
	}
	
	/**
	 * Get a String value of URI that points to the location of Socket Description File(.uis) of the Socket specified by 'socketName'.
	 * 
	 * @param socketName a String value of socketName
	 * @return a String value of URI that points to the location of Socket Description File(.uis).
	 */
	public String getSocketDescriptionUri(String socketName) {
		
		for( SocketDescription socketDescription : sockets) {
			
			if( socketDescription.getSocketName().equals(socketName) ) {
				
				return socketDescription.getSocketDescriptionUri();
			}
		}
		
		return null;
	}
	
	/**
	 * Check whether IUISocketElement specified by 'eltId' is Available in the Socket specified by 'socketName' or not.
	 * 
	 * @param socketName a String value of Socket Name
	 * @param eltId a String value of eltId
	 * @return whether Element is Available or not
	 */
	public boolean isElementAvailable(String socketName,  String eltId){
		logger.info("In Target Description");
		for(SocketDescription socketDescription : sockets) {
			
			if( socketDescription.getSocketName().equals(socketName))
				return socketDescription.isSocketNameExists(eltId);
		}
		
		return false;
	}
	
	/**
	 * Get a String value of SocketId of the Socket specified 'socketName'.
	 * 
	 * @param socketName a String value of socketName
	 * @return a String value of Socket Id
	 */
	public String getSocketId(String socketName){
		
		for( SocketDescription socketDescription : sockets) {
			
			if( socketDescription.getSocketName().equals(socketName) ) {
				
				return socketDescription.getSocketId();
			}
		}
		
		return null;
	}
	
	/**
	 * Parse the Target Description file(.td).
	 * 
	 */
	private void parse() throws TAException{
	
		Document tdDoc = null;
		try {
			tdDoc = CommonUtilities.parseDocument(tdUri);
		} catch (NullPointerException e) {
			logger.warning("NullPointerException : Parsing Target Description File '"+tdUri+"'.");
			throw new TAException("NullPointerException");
		} catch (SAXException e) {
			logger.warning("SAXException : Parsing Target Description File '"+tdUri+"'.");
			throw new TAException("SAXException");
		} catch (IOException e) {
			String message = "IOException : An error occured when parsing Target Description File '"+ tdUri;
					if (!new File( tdUri).exists() ){
						message += " The file does not exist";
					}
			logger.warning(message);
			
			throw new TAException("IOException");
		} catch (ParserConfigurationException e) {
			logger.warning("ParserConfigurationException : Parsing Target Description File '"+tdUri+"'.");
			throw new TAException("ParserConfigurationException");
		}
		
        if (tdDoc == null) {
        	logger.warning("File is not in proper form of XML");
            throw new TAException();
        }
        
        Element root = tdDoc.getDocumentElement();

        if( !root.getLocalName().equals("target") )
        	throw new TAException();
           
        //Check id attribute.
        id = root.getAttribute("id");
        if (id == null || id.trim().equals(""))  {
        	logger.warning("'id' attributes is not available.");
        	throw new TAException("'id' attribute is not exists in target Element.");
        }
        //Check name attribute.
        name = root.getAttribute("about");
        if (name == null || name.trim().equals("")) {
        	logger.warning("'about' attributes is not available.");
        	throw new TAException("'about' attribute is not exists in target Element.");
        }
        //Check hidden attribute if any.
        String temp = root.getAttribute("hidden");
        hidden = temp.equals("true") ? true : false;

        NodeList nodeLists = root.getChildNodes();
// Start : Getting 'resSvc' from TD.
      /*
        Node resSvc = root.getElementsByTagName("resSvc").item(0);
        
        if(resSvc.getLocalName().equals("resSvc") && resSvc.hasChildNodes() && (resSvc instanceof Element)){
			String resSer = ((Element)resSvc).getAttribute("about");
			if(resSer != null){
				need to call talistener method to give resSvc
				//resourceManager.setTargetResSvcMap(targetName, resSer);
			}
		}*/
// End : Getting 'resSvc' from TD.        
        for (int i = 0; i < nodeLists.getLength(); i++) {  
            Node node = nodeLists.item(i);
            if (node instanceof Element) {
            	
                Element element = (Element) node;
                String tagName = element.getLocalName();
                
                logger.info("Node Name = "+tagName);
                
                if ( tagName.equals("conformsTo") && (node.getChildNodes().getLength() == 1) ) {
                	
                	conformsTo = node.getFirstChild().getNodeValue();
                	
                	if ( (conformsTo != null) && !conformsTo.equals(Constants.PROPERTY_CONFORMS_TO_VALUE_TD) ) {
                		logger.severe("Document conforms to '" + conformsTo + "' is not equal to '" + Constants.PROPERTY_CONFORMS_TO_VALUE_TD+"'.");
                		return;
                	}
                	
                } else if ( tagName.equals("created") && (node.getChildNodes().getLength() == 1) ) {
                	created = node.getFirstChild().getNodeValue();
                } else if ( tagName.equals("modified") && (node.getChildNodes().getLength() == 1) ) {
                	modified = node.getFirstChild().getNodeValue();
                } else if ( tagName.equals("creator") && (node.getChildNodes().getLength() == 1) ) {
                	creator = node.getFirstChild().getNodeValue();
                } else if ( tagName.equals("socket") ) {
                	SocketDescription socket = new SocketDescription(this, element, taListener);
                    sockets.add(socket);
                } else if( tagName.equals("locator")){
                	Locator l = new Locator(element);
                	String id = l.getId();
                	if(locators.containsKey(id)){
                		logger.warning("Locator with the id = "+id+" is already exists . .!!!!");
                	}
                	locators.put(id, l);
                }
                // 2012-09-14 : No need to parse 'grpSheet'.
                // else if ( tagName.equals("grpSheet") ) { // Parikshit Thakur : 20111024. Added to parse grpSheet element in TD.
                	
                	// No need to parse 'grpSheet' element here.
                	//parseGrpSheetElements((Element) node);
                	
                	 
             //   }
                
            }
        }
        //logger.info("conformsTo="+conformsTo+" created="+created+" modified="+modified+" creator="+creator);
        logger.info("TD parsing completed successfully.");
	}
	

	// 2012-09-14 : No need to parse 'grpSheet' element.
	
//	public void parseGrpSheetElements(Element element)
//	{
//		if ( element == null ) 
//			return;
//		
//		NodeList nodeList = element.getChildNodes();
//		
//		String grpSheetURI = null;
//		
//		List<String> aResDescForDomainList = new ArrayList<String>();
//		List<String> typeList = new ArrayList<String>();
//		List<String> conformsToList = new ArrayList<String>();
//		List<String> forLangList = new ArrayList<String>();
//		List<String> roleList = new ArrayList<String>();
//		
//		
//		for( int i=0 ; i < nodeList.getLength() ; i++ ) {
//			
//			Node node = nodeList.item(i);
//			
//			if ( node == null ) 
//				continue;
//			
//			String nodeName = node.getNodeName();
//			
//			if ( (nodeName == null) || (nodeName.startsWith("#") ) )
//				continue;
//			
//			logger.info("Node Name="+nodeName);
//			
//			if ( nodeName.equals("localAt") ) {
//				
//				if ( node.getChildNodes().getLength() != 1 ) 
//					continue;
//				
//				Node firstCld = node.getFirstChild();
//				
//				if ( firstCld.getNodeType() != Node.TEXT_NODE )
//					continue;
//				
//				grpSheetURI = getGroupSheetURI( firstCld.getNodeValue(), tdUri.toString() );
//			
//				
//			}
//			
//			else if( nodeName.equals("scents") && (node instanceof Element) ){
//				
//				NodeList scentNodes = node.getChildNodes();
//				
//				for( int j=0 ; j < scentNodes.getLength() ; j++ ) {
//					
//					Node scentNode = scentNodes.item(j);
//					if ( scentNode == null ) 
//						continue;
//					
//					String scentNodeName = scentNode.getNodeName();
//					
//					if(scentNodeName.equals("forDomain") && (scentNode instanceof Element)){
//						
//						if ( scentNode.getChildNodes().getLength() != 1 ) 
//							continue;
//						
//						Node firstCld = scentNode.getFirstChild();
//						
//						if ( firstCld.getNodeType() != Node.TEXT_NODE )
//							continue;
//																		
//						aResDescForDomainList.add( firstCld.getNodeValue() );
//					}
//					else if ( scentNodeName.equals("dc:type") && (scentNode instanceof Element) ) {
//						
//						if ( scentNode.getChildNodes().getLength() != 1 ) 
//							continue;
//						
//						Node firstCld = scentNode.getFirstChild();
//						
//						if ( firstCld.getNodeType() != Node.TEXT_NODE )
//							continue;
//						
//						typeList.add( firstCld.getNodeValue() );			
//						
//					}
//					else if ( scentNodeName.equals("forLang") && (scentNode instanceof Element) ) {
//						
//						if ( scentNode.getChildNodes().getLength() != 1 ) 
//							continue;
//						
//						Node firstCld = scentNode.getFirstChild();
//						
//						if ( firstCld.getNodeType() != Node.TEXT_NODE )
//							continue;
//						
//						forLangList.add( firstCld.getNodeValue() );			
//					
//					}
//				}
//			}
//			
//		}
//		
//		// Put Resource Sheet URI in Resource Sheet Properties List.
//		// Make a new entry in 'resourceSheetPropList' for this Resource Sheet.
//		Map<String, List<String>> grpSheetProps = new HashMap<String, List<String>>();
//		
//		List<String> grpSheetURIList = new ArrayList<String>();
//		grpSheetURIList.add(grpSheetURI);
//		grpSheetProps.put(Constants.PROP_NAME_RESOURCE_LOCAL_AT, grpSheetURIList);
//		
//		grpSheetProps.put(Constants.PROPERTY_A_RRES_DESC_FOR_DOMAIN, aResDescForDomainList);
//		grpSheetProps.put(Constants.PROPERTY_DC_ELEMENTS_TYPE, typeList);
//		grpSheetProps.put(Constants.PROPERTY_CONFORMS_TO, conformsToList);
//		grpSheetProps.put(Constants.PROPERTY_RES_FOR_LANG, forLangList);
//		grpSheetProps.put(Constants.PROPERTY_RES_ROLE, roleList);
//		
//		grpSheetPropsList.add(grpSheetProps);
//	}
	
	
//	private String getGroupSheetURI(String localAt, String tdUri) {
//		
//		if ( localAt == null )
//			return null;
//		if ( (localAt.indexOf("http://") == -1) && (localAt.indexOf("https://") == -1) && (localAt.indexOf("file://") == -1) ) {
//			
//			if ( tdUri.indexOf("/") != -1 ) {
//				
//				return ( tdUri.substring(0,tdUri.lastIndexOf("/")+1) + localAt );
//	
//			} else {
//				return null;
//			}
//			
//		} else {
//			
//			return localAt;
//		}
//		
//	}
	
	
	
	
	/**
	 * Get Code file From Resource Server.
	 * 
	 * @param uriString a String value of File Path URI
	 * @param taListener an Object of ITAListener
	 * 
	 * @return a String value of Code File Path URI
	 */
	private String getFileFromResourceServer(Map<String, String> tdProps, ITAListener taListener) {
		
		if ( (tdProps == null) || (taListener == null) )
			return null;
		
		List<Map<String, List<String>>> resProps = new ArrayList<Map<String,List<String>>>();
		resProps.add( modifyMap(tdProps) );
		
		List<List<Map<String, List<String>>>> returnProps = taListener.getResources(null, resProps);
		
		if ( (returnProps == null) || (returnProps.size() == 0) )
			return null;
		
		List<Map<String, List<String>>> propList = returnProps.get(0);
		
		if ( (propList == null) || (propList.size() == 0) )
			return null;
		
		Map<String, List<String>> returnPropMap = propList.get(0);
		
		if ( returnPropMap == null )
			return null;
		
		return CommonUtilities.getFirstItem( returnPropMap.get(Constants.PROP_NAME_RESOURCE_LOCAL_AT) );
	}
	
	/**
	 * Modify elements of map.
	 * @param Map of &lt;String, String&gt;
	 * @return Map&lt;String, List&lt;String&gt;&gt;
	 */
	private Map<String, List<String>> modifyMap(Map<String, String> map) {
		
		if ( map == null )
			return null;
		
		Map<String, List<String>> returnMap = new HashMap<String, List<String>>();
		
		for ( Entry<String, String> entry : map.entrySet() ) {
			
			if ( entry == null )
				continue;
			
			returnMap.put(entry.getKey(), CommonUtilities.convertToList(entry.getValue()) );
		}
			
		return returnMap;
	}
	
	///////////////////////////For testing///////////////////////////////////
	
 	@SuppressWarnings("unused")
	private void showSockets(List<IUISocketElement> elements) {
 		
 		List<String> dimElements = new ArrayList<String>();
		logger.info("------------------------ElementList Details--------------------");
		for(IUISocketElement socketElement : elements ) {		
			printSocket(socketElement, "", dimElements);		
		}
		
		logger.info("\n\n----------------------------Dimensional Elements-------------------------");
		for(String elementId : dimElements)
			System.out.println(elementId);
		
		logger.info("\n\n------------------------ElementList Command's Details--------------------");
		for(IUISocketElement socketElement : elements ) {		
			printCommandParam(socketElement);
		}
		
	}	

	private void printCommandParam(IUISocketElement socketElement) {
		
		if (socketElement instanceof UISocketCommand) {
			
			System.out.println("CMD:"+socketElement.getElementId() );
			
			HashMap<String, UISocketCommandParam> params = ((UISocketCommand)socketElement).getCommandParamMap();
			
			for ( String id : params.keySet() ) {
				
				UISocketCommandParam param = params.get(id);
				
				if ( param.isIdRef() ) {
					System.out.println("       idref="+param.getId()+"     dir="+param.getDir()+"     type="+param.getType()+"     value="+param.getValue() );
				} else {
					System.out.println("       id="+param.getId()+"     dir="+param.getDir()+"     type="+param.getType()+"     value="+param.getValue() );
				}
				
			}
			
		} else if(socketElement instanceof UISocketSet) {
			
			for( IUISocketElement element : ((UISocketSet)socketElement).getElementMap().values() ) 
				printCommandParam(element);
			
		}
	}
	
	private void printSocket(IUISocketElement socketElement, String spaces, List<String> dimElements) {
		
		if ( socketElement.isDimensional() )
			dimElements.add(socketElement.getElementId());
		
	
		if ( socketElement instanceof UISocketSet ) {
			
			UISocketSet socketSet = (UISocketSet)socketElement;
			System.out.println( spaces + "SET:" + socketElement.getElementId() + " {Dim=" + socketElement.getDimType() + "} |" );
			String addSpace = "";
			for(int x=0 ; x<("SET:" + socketElement.getElementId() + " {Dim=" + socketElement.getDimType() + "} ").length() ; x++)
				addSpace += " ";
			spaces += addSpace + "|";
			
			for( String socketId : socketSet.getElementMap().keySet() ) {
				
				IUISocketElement socElement = socketSet.getElementMap().get(socketId);
				printSocket(socElement, spaces, dimElements);
							
			}
			
		} else {
			System.out.println( spaces + getTypeOfElement(socketElement) + socketElement.getElementId() + " {Type=" + socketElement.getType() + "} {Dim=" + socketElement.getDimType() + "} |");
		}
		
	}
	
	/**
	 * Gives the type of the socket element.
	 * @param socketElement
	 * @return String representation of socket element type.
	 */
	 private String getTypeOfElement(IUISocketElement socketElement) {
		
		if ( socketElement instanceof UISocketVariable) {
			return "VAR:" ;
		} else if ( socketElement instanceof UISocketCommand) {
			return "CMD:" ;
		} else if ( socketElement instanceof UISocketNotification) {
			return "NOT:" ;
		} else if ( socketElement instanceof UISocketSet) {
			return "SET:" ;
		}
		
		return "" ;
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////	   

}
// Locator class definded to store locator information.
/**
 * Parses the Locator information from Target Description file and 
 * provides access to the components contained within. 
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class Locator{
	
	private Element locator; // the element itself.
	
	private String type;
	private String id;
	
	private List<Element> mappings = new ArrayList<Element>();
	private List<Element> extensions = new ArrayList<Element>();
	
	private Logger logger = LoggerUtil.getSdkLogger();
	
	/**
	 * @param element
	 * @throws TAException
	 */
	Locator(Element element) throws TAException{
		
		this.locator = element;
		parse();
	}
	
	/**
	 * Get 'type' attribute value.
	 * 
	 * @return String value of 'type' attribute.
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * Get 'id' attribute value.
	 * 
	 * @return String value of 'id' attribute.

	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Get List of &lt;mapping&gt; elements.
	 * 
	 * @return List&lt;Element&gt;
	 */
	public List<Element> getMappings() {
		return this.mappings;
	}

	/**
	 * Get List of &lt;extension&gt; elements.
	 * 
	 * @return List&lt;Element&gt;

	 */
	public List<Element> getExtensions() {
		return this.extensions;
	}

	/**
	 * Parses the $lt;locator&gt; from the target description file and 
	 * the contained components within.
	 * 
	 * @throws TAException if failure.
	 */
	void parse() throws TAException { // parsing locator element.
		
		String temp = locator.getAttribute("id").trim(); // place holder dummy variable.
		
		if (temp == null || temp.trim().equals("")) {
			logger.warning("'id' attributes is not available.");
            throw new TAException("'id' attribute is not exists in locator Element in TD.");
		}
		
		this.id = temp;
		
		temp = locator.getAttribute("type").trim();
		
		if (temp == null || temp.trim().equals("")) {
			
			logger.warning("'type' attributes is not available.");
			throw new TAException("'type' attribute is not exists in locator Element.");
		}
		
		if(!(temp.equalsIgnoreCase("audio") || temp.equalsIgnoreCase("visual") || temp.equalsIgnoreCase("other"))){
			
			logger.warning("'type' attributes is not having valid value..!!!");
			throw new TAException("'type' attribute is not having valid value.");
		}
		
		this.type = temp;
		
		NodeList nodes = locator.getChildNodes();
		
		for(int i=0 ; i< nodes.getLength() ; i++){
			
			Node n = nodes.item(i);
			
			logger.info("Node Name = "+n.getLocalName());
			
			if(n instanceof Element){
				
				if(n.getLocalName().trim().equals("mapping")){
					
					mappings.add((Element)n);
				}else if(n.getLocalName().trim().equals("extension")){
					
					extensions.add((Element)n);
				}
			}
		}
		
	}
}
