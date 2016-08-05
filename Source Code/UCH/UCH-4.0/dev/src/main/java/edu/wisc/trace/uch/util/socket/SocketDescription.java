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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.openurc.uch.ITAListener;
import org.openurc.uch.TAException;
import org.openurc.uch.UCHException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.Constants;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Parses the UI Socket Description file and Store all the Elements and 
 * makes Socket Document information accessible.
 *  
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class SocketDescription {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private ITAListener taListener;
	
	private final String xmlns = "http://openurc.org/ns/uisocketdesc-2";	
	
	private String id;
	private String name;
	private boolean hidden;
	private String about;
/// Now 'socketDescriptionLocalAt' not needed
	///private String socketDescriptionLocalAt; /// socketDescriptionLocalAt is deprecated.
	private String retrieveFrom; /// Added for 'retrieveFrom' element
	
	private List<IUISocketElement> elementList;
	
	// 'includesRes' attribute is now removed from socket elements.
	//private List<String> includeResElement;
	
	private List<SimpleType> simpleTypeList;
	private URI sdUri;
	private TargetDescription td;
	
	private String creator;
	private String contributor;
	private String publisher;
	private String rights;
	private String conformsTo;
	private String modified;
	
	@SuppressWarnings("unchecked")
	private Map<String, Class> types;
	
	/**
	 * Constructor.
	 * Parse the &lt;socket&gt; Node of Target Description file(.td).
	 * 
	 * @param td an Object of TargetDescription
	 * @param element an Object of Element
	 * @throws TAException
	 */
	public SocketDescription(TargetDescription td, Element element, ITAListener taListener) throws TAException {
		
		logger.info("Constructor of SocketDescription.");
		this.td = td;
		this.taListener = taListener;
		
		initializeTypes();
		
		parseSocketElement(element);
	}
	
	/**
	 * Initialize Types.
	 */
	@SuppressWarnings("unchecked")
	private void initializeTypes() {
		
		types = new HashMap<String, Class>();
		
		types.put("uis:basicCommand", UISocketBasicCommand.class);
		types.put("uis:timedCommand", UISocketTimedCommand.class);
		types.put("didl-lite:root.type", UISocketXmlVariable.class);
	}
	
	/**
	 * Parse the &lt;socket&gt; Node of Target Description file(.td).
	 * And fetches the correct uri for Socket Description file(.uis).
	 * 
	 * @param element an Object of Element
	 * @throws TAException
	 */
	private void parseSocketElement(Element element) throws TAException {
		
		id = element.getAttribute("id");	
		if (id == null || id.trim().equals("")) {
			logger.warning("'id' attributes is not available.");
            throw new TAException("'id' attribute is not exists in socket Element.");
		}
		
		name = element.getAttribute("name");
		if (name == null || name.trim().equals("")) {
			logger.warning("'about' attributes is not available.");
			throw new TAException("'name' attribute is not exists in socket Element.");
		}
		
		String hiddenStr = element.getAttribute("hidden"); 
		hidden = hiddenStr.equals("true") ? true : false;
		
		NodeList nodeLists = element.getChildNodes();
		List<String> retrieveFromURIList = new ArrayList<String>();
	
		for (int i = 0; i < nodeLists.getLength(); i++) {
			
			Node node = nodeLists.item(i);
			
// 2012:09:05 - <socketDescriptionLocalAt> and <resName> are no more used in the <socket> element.	

			// 2012:09:05 start of now socket have retrieveFrom element to specify a URI as content.
			if( node.getNodeName().equals("retrieveFrom")){
				
				if( !(node instanceof Element) ) {
					logger.warning("Socket Element is not in proper Format - retrieveFrom element missing.");
					throw new TAException("Socket Element is not in proper Format - retrieveFrom element missing.");
				}
				
				if( ((Element)node).getFirstChild() != null){
					
					String temp = ((Element)node).getFirstChild().getNodeValue().trim();
				
					if(temp.length() > 0){
						temp = temp.replace(" ","%20");
						retrieveFromURIList.add(temp);
					}
				}
			}
				
		}
			
		if(retrieveFromURIList.size() > 0){
		
			for(String retrievePath : retrieveFromURIList){ // for each retrieveFrom URIs
				
				if(isItTrueSDUri(retrievePath)){ 
					return;
				}
			}
			
			logger.severe(" 'retrieveFrom' uris in TD, at '"+td.getTdUri()+"' are not sufficient to find socket file ..!!! ");
		}
			
		// asks resource manager for checking in local resources or fetch it from UCH configured resource server.
		
		/*try {
					
			String sdURIString = getDocumentFromResourceManager(this.name);
					
			if(sdURIString != null){
						
				this.retrieveFrom = sdURIString;
				sdUri = new URI(sdURIString);
			}
					
		} catch (URISyntaxException e) {
		
			e.printStackTrace();
		}*/

		if(getDocumentFromResourceManager(this.name)==null)
			logger.severe(" Resource name  '"+this.name+"' is not sufficient to find socket file ..!!! ");
		
		return;
	}
	
	
	/**
	 * Return 'id' attribute of &lt;socket&gt; Element of Target Description file.
	 * 
	 * @return a String value of id
	 */	
	public String getSocketId() {
		
		return id;
	}
	
	/**
	 * Return a String value of URI that points to the location of Socket Description File(.uis).
	 * 
	 * @return a String value of URI that points to the location of Socket Description File(.uis).
	 */
	public String getSocketDescriptionUri() {
		
		///return socketDescriptionLocalAt;
		return retrieveFrom;
	}
	
	
	
	/**
	 * Generate Socket Description file URI from 'retrieveFrom'.
	 * checking for relative path, absolute file path, direct resource URI.
	 * 
	 * @param socketRetrieveURI a String value of retrieveFrom
	 * @throws TAException
	 */
	private boolean isItTrueSDUri(String socketRetrieveURI) throws TAException {
		
		
		if ( socketRetrieveURI == null ){ 
			return false;
		}
		
		URI tdUri = td.getTdUri();
		String tdUriString = tdUri.toString();
		
		if ( (socketRetrieveURI.indexOf("http://") != -1) || (socketRetrieveURI.indexOf("https://") != -1) ) {
			
			if(socketRetrieveURI.indexOf("?") == -1){
				
				//GetDocument for Socket Description using resource URI.
				if(getDocument(tdUriString, socketRetrieveURI)){
					return true;
				}
			}else{
				
				// if URI contains '?', it means this request is made using resource name.
				String socketName = socketRetrieveURI.substring(socketRetrieveURI.indexOf("name")+5).trim();
				
				if(getDocumentFromResourceManager(socketName)!=null)
					return true;
					
				logger.severe(" Socket name in the retrieveFrom URI, '"+socketName+"' is not sufficient to find UIS file ..!!! ");
			}
						
		}else if( socketRetrieveURI.indexOf("file://") != -1 ) { 
		
			//checking for existence of file at specified URI for Socket Description.		
				if(isSocketDescExistsAtThisURI(socketRetrieveURI))
					return true;
			
		}else{
			
			//checking for existence of file at relative path.
			String sdUriString = tdUriString.substring(0,
					tdUriString.lastIndexOf("/") + 1)
					+ (socketRetrieveURI.indexOf("/") == 0 ? socketRetrieveURI
							.substring(1) : socketRetrieveURI);

			if(isSocketDescExistsAtThisURI(sdUriString))
				return true;

		}

		return false;
	}
	
	
	/**
	 * Check availability of the Socket Description in the local resource manager or 
	 * on the UCH configured resource server. 
	 * 
	 * @param socketRetrieveURI String representation of the socket path.
	 * 
	 * @return Socket Description resource URI.
	 */
	
	private String getDocumentFromResourceManager(String socketRetrieveURI){

		String socketPathLocal = null;

		// If above conditions
		Map<String, List<String>> propMap = new HashMap<String, List<String>>();
		
		
		propMap.put(Constants.RESOURCE_QUERY_PROP_NAME_START, CommonUtilities.convertToList("1") );
		propMap.put(Constants.RESOURCE_QUERY_PROP_NAME_COUNT, CommonUtilities.convertToList("1") );
		

		propMap.put(Constants.PROPERTY_RES_NAME,
				CommonUtilities.convertToList(socketRetrieveURI));
		
		propMap.put(Constants.PROPERTY_MIME_TYPE, CommonUtilities.convertToList(Constants.PROPERTY_MIME_TYPE_VALUE_UIS));
		

		List<Map<String, List<String>>> resProps = new ArrayList<Map<String, List<String>>>();
		resProps.add(propMap);

		List<List<Map<String, List<String>>>> returnProps = taListener
				.getResources(null, resProps);

		if ((returnProps != null) && (returnProps.size() != 0)) {

			List<Map<String, List<String>>> propList = returnProps.get(0);

			if (propList != null) {

				if (propList.size() != 0) {

					Map<String, List<String>> returnPropMap = propList.get(0);

					if (returnPropMap != null) {

						socketPathLocal = CommonUtilities
								.getFirstItem(returnPropMap
										.get(Constants.PROP_NAME_RESOURCE_LOCAL_AT));

						if (socketPathLocal != null) {

							try {
								retrieveFrom = socketPathLocal;
								sdUri = new URI(socketPathLocal);
							} catch (URISyntaxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		return socketPathLocal;
	}
	
	
	/**
	 * Asking TAListener for SocketDescription resource file at specified path.
	 * If exist save it at location where TargetDescription placed.
	 * 
	 * @param tdUriString String representation of the TDUri
	 * @param socketRetrieveURI String representation of the socket path.
	 * 
	 * @return boolean value for completeness of the operation.
	 */
	boolean getDocument(String tdUriString, String socketRetrieveURI){
		
		String socketData=null,sdUriString=null;
		
		try {
			socketData = taListener.getDocument(socketRetrieveURI, null, null);
			
			if(socketData != null){

				sdUriString = tdUriString.substring(0,
						tdUriString.lastIndexOf("/") + 1)
						+ socketRetrieveURI.substring(socketRetrieveURI
								.lastIndexOf("/") + 1);

				File f = new File(new URI(sdUriString));

				PrintWriter out = new PrintWriter(f);

				out.println(socketData);

				out.close();
				
				this.retrieveFrom = sdUriString;
				sdUri = new URI( sdUriString );
				
				return true;
			}
			
		} catch (UCHException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	/**
	 * Checking of existence of SocketDescription resource file at specified path.
	 * 
	 * @param sdURI String representation of the socket path.
	 * 
	 * @return boolean value for successfulness of the operation.
	 */
	
	boolean isSocketDescExistsAtThisURI(String sdURI){
		
		sdURI = sdURI.replace(" ","%20");
		
		try {
			
			if(new java.io.File(new URI(sdURI)).exists()){
				sdUri = new URI( sdURI );
				this.retrieveFrom = sdURI;
				return true;
			}
		} catch (URISyntaxException e) {
			logger.warning("URISyntaxException");
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	/**
	 * Get List of IUISocketElements by parsing Socket Description file(.uis) if it is not parsed.
	 * Else get the clone copy of parsed IUISocketElement List.
	 * 
	 * @return List&lt;IUISocketElement&gt; of IUISocketElements
	 */
	public List<IUISocketElement> getSocketElements() {
		
		if ( elementList == null ) {
			elementList = new ArrayList<IUISocketElement>();
			
			// 'includesRes' attribute is now removed from socket elements.
			//includeResElement = new ArrayList<String>();
			simpleTypeList = new ArrayList<SimpleType>();
			parse();
		}		
		
		if( elementList != null ) 
			return clonedElements();
		else 
			return null;
	}
	
	/**
	 * Get the String value of the &lt;creator&gt; Element of Socket Description File(.uis)
	 * 
	 * @return a String value of the &lt;creator&gt; Element of Socket Description File(.uis)
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * Get the String value of the &lt;contributor&gt; Element of Socket Description File(.uis)
	 * 
	 * @return a String value of the &lt;contributor&gt; Element of Socket Description File(.uis)
	 */
	public String getContributor() {
		return contributor;
	}

	/**
	 * Get the String value of the &lt;publisher&gt; Element of Socket Description File(.uis)
	 * 
	 * @return a String value of the &lt;publisher&gt; Element of Socket Description File(.uis)
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * Get the String value of the &lt;rights&gt; Element of Socket Description File(.uis)
	 * 
	 * @return a String value of the &lt;rights&gt; Element of Socket Description File(.uis)
	 */
	public String getRights() {
		return rights;
	}

	/**
	 * Get the String value of the &lt;conformsTo&gt; Element of Socket Description File(.uis)
	 * 
	 * @return a String value of the &lt;conformsTo&gt; Element of Socket Description File(.uis)
	 */
	public String getConformsTo() {
		return conformsTo;
	}

	/**
	 * Get the String value of the &lt;modified&gt; Element of Socket Description File(.uis)
	 * 
	 * @return a String value of the &lt;modified&gt; Element of Socket Description File(.uis)
	 */
	public String getModified() {
		return modified;
	}

	/**
	 * Get the boolean value of the attribute 'hidden' of the Element &lt;uiSocket&gt; of Socket Description File(.uis)
	 * 
	 * @return a boolean value of the attribute 'hidden' of the Element &lt;uiSocket&gt; of Socket Description File(.uis)
	 */
	public boolean getHidden() {
		return hidden;
	}

	/**
	 * Return cloned copy of IUISocketElements.
	 * 
	 * @return List&lt;IUISocketElement&gt; of IUISocketElements
	 */
	
	private List<IUISocketElement> clonedElements() {
		
		List<IUISocketElement> clonedElementList = new ArrayList<IUISocketElement>();
		
		for ( IUISocketElement socketElemet : elementList ) 
			clonedElementList.add( (IUISocketElement)socketElemet.clone() );
		
		return clonedElementList;
	}
	
	/**
	 * Get a 'name' attribute of &lt;socket&gt; Element of Target Description file.
	 * 
	 * @return a String value of Socket Name
	 */
	
	public String getSocketName() {
		return name;
	}
	
	/**
	 * Get Friendly Name for this Socket('id' attribute of &lt;socket&gt; Element of Target Description file).
	 * 
	 * @return a String value of friendly name
	 */
	public String getFriendlyName() {
		logger.info("Socket Friendly Name = "+id);
		return id;
	}
	
	
	/**
	 * Parse the Socket Description file(.uis).
	 * 
	 */	
	private void parse() {
	
		Document sdDoc = null;
		
		try {
			
			sdDoc = CommonUtilities.parseDocument(sdUri);
			
		} catch (NullPointerException e) {
			logger.warning("NullPointerException : Parsing Socket Description File '"+sdUri+"'.");
			e.printStackTrace();
		} catch (SAXException e) {
			logger.warning("SAXException : Parsing Socket Description File '"+sdUri+"'.");
			e.printStackTrace();
		} catch (IOException e) {
			logger.warning("IOException : Parsing Socket Description File '"+sdUri+"'.");
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			logger.warning("ParserConfigurationException : Parsing Socket Description File '"+sdUri+"'.");
			e.printStackTrace();
		}
		
        if (sdDoc == null) {
        	logger.warning("File is not in proper form of XML");
            return;
        }
        
        Element root = sdDoc.getDocumentElement();
        
        String tagName = root.getLocalName();
		String ns = root.getNamespaceURI();
		if (!tagName.equals("uiSocket") && ns.equals(xmlns)) {
			logger.warning("uiSocket missing as root element. Not parsing UISocketDocument.");
			return;
		}
		
		about = root.getAttribute("about");
		if (about == null || about.trim().equals("")) {
			logger.warning("uiSocket missing as about. Not parsing UISocketDocument.");
			return;
		}
		
		
		//try{
		parseDepth(root, null, true );
		//} catch(Exception e){
		//	e.printStackTrace();
		//}
	}
	
	/**
	 * Parse the Elements of Socket Description file(.uis) in Depth. Its a method which is called
	 * recursively. Parses the generic details of all elements :Set, Command, Variable, Notification, Constants
	 * 
	 * @param root an Object of Element
	 * @param socketSet an Object of UISocketSet
	 * @param flag a boolean value of flag
	 */
	
	@SuppressWarnings("unchecked")
	private void parseDepth(Element root, UISocketSet socketSet, boolean flag ) {
		
		
		NodeList nodeList = root.getChildNodes();
		String nodeName;
		Element element;
		String ns;
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			
			Node node = nodeList.item(i);
			
			
			if (!(node instanceof Element)) {
				continue;
			}
			
			element = (Element) node;
			
			nodeName = node.getLocalName();
			
			ns = element.getNamespaceURI();
			
			//logger.info("NodeName = " + nodeName + " NameSpace = " + ns);
			AbstractUISocketElement socketElement = null;
			
			if ( nodeName.equals("set") && ns.equals(xmlns) ) {
				
				socketElement = new UISocketSet();
				//parse generic details of this element
				this.parseSocketElement(element, socketElement);
				
				if(socketSet != null)
					socketSet.addIUISocketElement(socketElement);
				
				
				parseDepth(element, (UISocketSet)socketElement, false);
				
			} else if ( nodeName.equals("variable") && ns.equals(xmlns) ) {
				
				String type = element.getAttribute("type");
				
				if( type == null ) {
					socketElement = new UISocketVariable();
				} else {
					
					Class cls = types.get(type);
					
					if ( cls == null ) {
						socketElement = new UISocketVariable();
					} else {
						
						try{
							socketElement = (AbstractUISocketElement)cls.newInstance();
						}catch(Exception e){
							logger.info("Unable to create Instance.");
						}
					}
					
				}
				
				
				this.parseSocketElement(element, socketElement);
				if(socketSet != null){
					socketSet.addIUISocketElement(socketElement);
				}
				
			} else if ( nodeName.equals("command") && ns.equals(xmlns) ) {
				
				String type = element.getAttribute("type");
				
				if( type == null ) {
					socketElement = new UISocketVoidCommand();
				} else {
					
					Class cls = types.get(type);
					
					if ( cls == null ) {
						socketElement = new UISocketVoidCommand();
					} else {
						
						try{
							socketElement = (AbstractUISocketElement)cls.newInstance();
						}catch(Exception e){
							logger.info("Unable to create Instance.");
						}
					}
				}
				
				this.parseSocketElement(element, socketElement);
				
				if(socketSet != null)
					socketSet.addIUISocketElement(socketElement);
				
			} else if ( nodeName.equals("notify") && ns.equals(xmlns) ) {
									
				socketElement = new UISocketNotification();
				
				this.parseSocketElement(element, socketElement);
				
				
				//Parikshit Thakur : 20111010. Changes to parse newly added nodes for variable and command in notify node(for custom type of notification).
				NodeList nodeListForNotify = element.getChildNodes();
				String nodeNameForNotify;
				Element elementForNotify;
				String nsForNotify;
				
								
				for (int j = 0; j < nodeListForNotify.getLength(); j++) {
					Node nodeForNotify = nodeListForNotify.item(j);
					if (!(nodeForNotify instanceof Element)) {
						continue;
					}
					
					elementForNotify = (Element) nodeForNotify;
					nodeNameForNotify = nodeForNotify.getLocalName();
					nsForNotify = elementForNotify.getNamespaceURI();
					AbstractUISocketElement socketElementForNotify = null;
									
					if ( nodeNameForNotify.equals("variable") && nsForNotify.equals(xmlns) ) {
						
						String type = elementForNotify.getAttribute("type");
						
						if( type == null ) {
							socketElementForNotify = new UISocketVariable();
						} else {
							
							Class cls = types.get(type);
							
							if ( cls == null ) {
								socketElementForNotify = new UISocketVariable();
							} else {
								
								try{
									socketElementForNotify = (AbstractUISocketElement)cls.newInstance();
								}catch(Exception e){
									logger.info("Unable to create Instance.");
								}
							}
							
						}
						
						this.parseSocketElement(elementForNotify, socketElementForNotify);
						if( socketElementForNotify != null ) {
							socketElementForNotify.setSocket(this);
							
							
						((UISocketNotification) socketElement).addIUISocketElement(socketElementForNotify);
							//elementList.add(socketElementForNotify);
						}
						}
						
					else if ( nodeNameForNotify.equals("command") && nsForNotify.equals(xmlns) ) {
						
						String type = elementForNotify.getAttribute("type");
						
						if( type == null ) {
							socketElementForNotify = new UISocketVoidCommand();
						} else {
							
							Class cls = types.get(type);
							
							if ( cls == null ) {
								socketElementForNotify = new UISocketVoidCommand();
							} else {
								
								try{
									socketElementForNotify = (AbstractUISocketElement)cls.newInstance();
								}catch(Exception e){
									logger.info("Unable to create Instance.");
								}
							}
						}
						
						this.parseSocketElement(elementForNotify, socketElementForNotify);
						if( socketElementForNotify != null ) {
							socketElementForNotify.setSocket(this);
							
							
						((UISocketNotification) socketElement).addIUISocketElement(socketElementForNotify);
							
								//elementList.add(socketElementForNotify);
						}
					}
					
					
				}
			
				// Change ends.
				
				
				if(socketSet != null)
					socketSet.addIUISocketElement(socketElement);
				
			} else if ( nodeName.equals("conformsTo") && (node.getChildNodes().getLength() == 1) ) {
            	
            	conformsTo = node.getFirstChild().getNodeValue();
            	
            	if ( (conformsTo != null) && !conformsTo.equals(Constants.PROPERTY_CONFORMS_TO_VALUE_UIS) ) {
            		logger.severe("Document conforms to '" + conformsTo + "' is not equal to '" + Constants.PROPERTY_CONFORMS_TO_VALUE_UIS+"'.");
            		return;
            	}
            	
            } else if ( nodeName.equals("creator") && (node.getChildNodes().getLength() == 1) ) {
            	creator = node.getFirstChild().getNodeValue();
            } else if ( nodeName.equals("contributor") && (node.getChildNodes().getLength() == 1) ) {
            	contributor = node.getFirstChild().getNodeValue();
            } else if ( nodeName.equals("publisher") && (node.getChildNodes().getLength() == 1) ) {
            	publisher = node.getFirstChild().getNodeValue();
            } else if ( nodeName.equals("rights") && (node.getChildNodes().getLength() == 1) ) {
            	rights = node.getFirstChild().getNodeValue();
            } else if ( nodeName.equals("modified") && (node.getChildNodes().getLength() == 1) ) {
            	modified = node.getFirstChild().getNodeValue();
            } else if ( nodeName.equals("schema") ) {
            	
            	parseSchema(node);
            }
			
			
			if( socketElement != null ) {
				socketElement.setSocket(this);
				
				if ( flag )
					elementList.add(socketElement);
			}
			
		}
		//logger.info("conformsTo="+conformsTo+" creator="+creator+" contributor="+contributor+" publisher="+publisher+" rights="+rights+" modified="+modified);
	}
		
	/**
	 * Parse the generic details of the DOM Element 'rootElement' and set relative variables of AbstractUISocketElement 'socketElement'
	 * like 'dim', 'includeRes' etc.
	 * 
	 * @param rootElement an Object of DOM Element
	 * @param socketElement an Object of AbstractUISocketElement
	 */
	private void parseSocketElement(Element rootElement, AbstractUISocketElement socketElement) {
		
		socketElement.setElementId( rootElement.getAttribute("id") );

		if ( rootElement.hasAttribute("dim") ) {	
			socketElement.setDimensional(true);
			socketElement.setDimType( rootElement.getAttribute("dim") );
		} else {
			socketElement.setDimensional(false);
		}
		
		if ( rootElement.hasAttribute("type") ) {
			socketElement.setType( rootElement.getAttribute("type") );
		}
// 2012-09-18 : process final attribute.
		if ( rootElement.hasAttribute("final") ) {
			socketElement.setFinal( Boolean.parseBoolean(rootElement.getAttribute("final").trim()) );
		}
		
		if ( socketElement instanceof UISocketNotification ) {
			logger.info("-------------State=inactive------------------");
			((UISocketNotification)socketElement).setState("inactive");
			// Changes to add category attribute to notification. Parikshit Thakur : 20110727
			if ( rootElement.hasAttribute("category") ){ 
				((UISocketNotification)socketElement).setCategory(rootElement.getAttribute("category"));
			}
			
			if ( rootElement.hasAttribute("type") ){
				//socketElement.setType(rootElement.getAttribute("type"));
			}
			
			
		} else if ( socketElement instanceof UISocketCommand ) {
			
			if ( socketElement instanceof UISocketBasicCommand ) {
				((UISocketBasicCommand)socketElement).setState("initial");
			} else if ( socketElement instanceof UISocketTimedCommand ) {		
				((UISocketTimedCommand)socketElement).setState("initial");
				((UISocketTimedCommand)socketElement).setTtc(0);
			}
			
			NodeList nodeList = rootElement.getChildNodes();
			
			for (int i = 0; i < nodeList.getLength(); i++) {
				
				Node node = nodeList.item(i);
				
				
				
				if ( !node.getNodeName().equals("param") ) {
					continue;
				}

				Element e = (Element) node;

				String id = e.getAttribute("id");
				boolean isIdRef = false;

				if (id == null | id.equals("")) {
					id = e.getAttribute("idref");
					isIdRef = true;
				}

				UISocketCommandParam commandParam = new UISocketCommandParam();

				commandParam.setId(id);
				commandParam.setIdRef(isIdRef);
				commandParam.setType(e.getAttribute("type"));
				commandParam.setDir(e.getAttribute("dir"));

				((UISocketCommand) socketElement).addCommandParam(commandParam);
				
				
			}
		}
		
	}
	
	
	private void parseSchema(Node schemaNode) {
	
		
		if ( schemaNode == null ) 
			return;
		
		NodeList schemaChilds = schemaNode.getChildNodes();
		
		if ( schemaChilds == null )
			return;
		
		for ( int i=0 ; i<schemaChilds.getLength() ; i++ ) {
			
			Node schemaChild = schemaChilds.item(i);
			
			if ( (schemaChild == null) || (schemaChild.getNodeType() != Node.ELEMENT_NODE) ) {
				logger.warning("Child Node is not Element Node.");
				return;
			}
			
			String nodeName = schemaChild.getNodeName();
			
			if ( nodeName.equals("simpleType") && (schemaChild instanceof Element) ) {
				
				Element simpleTypeElement = (Element)schemaChild;
				
				String name = simpleTypeElement.getAttribute("name");
				String id = simpleTypeElement.getAttribute("id");
				String includesRes = simpleTypeElement.getAttribute("includesRes");
				String restrictionBase = null;
				String minInclusive = null;
				String maxInclusive = null;
				List<String> enumeration = new ArrayList<String>();
				
				NodeList simpleTypeChilds = simpleTypeElement.getChildNodes();
				
				Node restriction = null;
				for ( int j=0 ; j<simpleTypeChilds.getLength() ; j++ ) {
					
					Node simpleTypeChild = simpleTypeChilds.item(j);
					
					if ( (simpleTypeChild != null) 
					  && (simpleTypeChild.getNodeType() == Node.ELEMENT_NODE) 
					  && simpleTypeChild.getNodeName().equals("restriction") ) {
						restriction = simpleTypeChild;
						break;
					}
				}
				
				if ( (restriction != null) && (restriction instanceof Element) ) {
					
					restrictionBase = ((Element)restriction).getAttribute("base");
					
					NodeList restrictionChilds = restriction.getChildNodes();
					
					for ( int j=0 ; j<restrictionChilds.getLength() ; j++ ) {
						
						Node restrictionChild = restrictionChilds.item(j);
						
						if ( (restrictionChild == null) || !(restrictionChild instanceof Element) )
							continue;
						
						String restrictionChildNodeName = restrictionChild.getNodeName();
						
						if ( restrictionChildNodeName.equals("enumeration") ) {
							
							enumeration.add( ((Element)restrictionChild).getAttribute("value") );
							
						} else if ( restrictionChildNodeName.equals("minInclusive") ) {
							
							minInclusive = ((Element)restrictionChild).getAttribute("value");
							
						} else if ( restrictionChildNodeName.equals("maxInclusive") ) {
							
							maxInclusive = ((Element)restrictionChild).getAttribute("value");
						}
					
					}
				}
					
				
				SimpleType simpleType = new SimpleType(id, name, includesRes, restrictionBase,
						minInclusive, maxInclusive, enumeration);
				
				simpleTypeList.add(simpleType);
			} 
		}
		
	}
	
	/**
	 * Check whether IUISocketElement specified by 'elementId' exists in this Socket Description or not.
	 * 
	 * @param elementId a String value of elementId
	 * @return whether IUISocketElement specified by 'elementId' exists or not.
	 */
	boolean isSocketNameExists(String elementId) {
		
		logger.info("In Socket Description");
		if(elementList == null ) {
			elementList = new ArrayList<IUISocketElement>();
			simpleTypeList = new ArrayList<SimpleType>();
			
			// 'includesRes' attribute is now removed from socket elements.
			//includeResElement = new ArrayList<String>();
			parse();
		}
			
		
		if( elementList == null ) 
			return false;
		
		for( IUISocketElement element : elementList ){
			
			if ( element.equals(elementId) )
				return true;
			
			else if ( element instanceof UISocketSet ){
				
				if( isSocketNameExists(elementId, (UISocketSet) element) )
					return true;
		
			}
			
		}
		return false;
	}
	
	/**
	 * Check whether IUISocketElement specified by 'elementId' exists or not in the UISocketSet specified by 'socketSet'.
	 * 
	 * @param elementId a String value of elementId
	 * @param socketSet an Object of UISocketSet
	 */

	protected boolean isSocketNameExists(String elementId, UISocketSet socketSet) {
		
		Map<String, IUISocketElement> elementMap = socketSet.getElementMap();
			
		for( IUISocketElement element : elementMap.values() ) {
			
			if ( elementId.equals( element.getElementId() ) )
				return true;
			
			logger.info("Element :"+ element);
			
			if ( element instanceof UISocketSet ) {
			
				if( isSocketNameExists(elementId, (UISocketSet) element) )
					return true;
				
			}
		}
		return false;
	}

	/**
	 * Return the boolean value of hidden.
	 * 
	 * @return a boolean value of hidden.
	 */
	boolean isHidden() {
		return hidden;
	}
	
	
	
	
	
	/**
	 * Get Socket Description File From Resource Server.
	 * 
	 * @param uriString a String value of File Path URI
	 * 
	 * @return a String value of Code File Path URI
	 */
	private String getFileFromResourceServer(String uriString){
		
		if ( (uriString == null) || (taListener == null) )
			return null;
		
		int startIndex = -1;
		
		//if ( (startIndex = uriString.indexOf('?')) == -1 )
			//return null;
		
		Map<String, List<String>> propMap = parseQueryString(uriString.substring(startIndex+1) );
		
		propMap.put(Constants.RESOURCE_QUERY_PROP_NAME_START, CommonUtilities.convertToList("1") );
		propMap.put(Constants.RESOURCE_QUERY_PROP_NAME_COUNT, CommonUtilities.convertToList("1") );
		
		List<Map<String, List<String>>> resProps = new ArrayList<Map<String,List<String>>>();
		resProps.add(propMap);
		
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
	 * Prepare a Map from a String like prop1=val1&prop2=val2&prop1&val11...
	 * 
	 * @param queryString a String value
	 * 
	 * @return an Object of Map&lt;String, List&lt;String&gt;&gt;
	 */
	private Map<String, List<String>> parseQueryString(String queryString) {
		
		if ( queryString == null )
			return null;
		
		if ( queryString.indexOf("=") == -1 ) 
			return null;
		
		Map<String, List<String>> returnMap = new HashMap<String, List<String>>();
		
		while ( queryString.indexOf("&") != -1 ) {
			
			String key = queryString.substring(0, queryString.indexOf("=")).trim();
			String value = queryString.substring(queryString.indexOf("=")+1, queryString.indexOf("&")).trim();	
			
			if ( returnMap.containsKey(key) ) {
				
				List<String> valueList = returnMap.get(key);
				valueList.add(value);
				
			} else {
				
				List<String> valueList = new ArrayList<String>();
				valueList.add(value);
				returnMap.put(key, valueList);
			}
			
			
			queryString = queryString.substring(queryString.indexOf("&")+1);
		}
		
		String key = queryString.substring(0, queryString.indexOf("=")).trim();
		String value = queryString.substring(queryString.indexOf("=")+1).trim();
		
		if ( returnMap.containsKey(key) ) {
			
			List<String> valueList = returnMap.get(key);
			valueList.add(value);
			
		} else {
			
			List<String> valueList = new ArrayList<String>();
			valueList.add(value);
			returnMap.put(key, valueList);
		}
		
		return returnMap;
	}
}
