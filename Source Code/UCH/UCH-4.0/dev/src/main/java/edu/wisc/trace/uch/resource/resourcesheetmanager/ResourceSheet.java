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
package edu.wisc.trace.uch.resource.resourcesheetmanager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Parse Resource Sheet File(.rsheet) and provide methods to retrieve matched Resource.  
 *
 *  
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class ResourceSheet {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private ResourceSheetManager resourceSheetManager;
	
	private URI rSheetUri ;
	
	//private String rSheetName;
	
	private String about;
	private String xmlns;
	private String modified;
	private String conformsTo;
	private String localAt;
	private List<String> forLangList = new ArrayList<String>();
	private String audience;
	private List<String> typeList = new ArrayList<String>(); 
	private String format;
	private String forDomain;
	/*
	 Removed line for 'groupingForDomain' in resource sheet because now we have a separate Grouping Sheet.
	 
		private String groupingForDomain;
	*/
	private String creator;
	private String publisher;
	private String contributor;
	private String date;
	private String rights;
	
	/**
	 * Constructor.
	 * Instantiate the local variable rSheetUri using the parameter rSheetURI. 
	 * 
	 * @param resourceSheetManager an Object of ResourceSheetManager
	 * @param rSheetURI a String value of Resource Sheet URI
	 */
	ResourceSheet( ResourceSheetManager resourceSheetManager, String rSheetURI ) {
		
		if ( (resourceSheetManager == null) || (rSheetURI == null) )
			return;
		
		this.resourceSheetManager = resourceSheetManager;
		
		try {
			rSheetUri = new URI(rSheetURI);
		} catch (URISyntaxException e) {
			logger.warning("URISyntaxException");
			//e.printStackTrace();
		} 
		
	}
	
	/**
	 * Return an Object of AResourceDescription for matching 'resPropMap' and put all AResourceDescription in 'resourceMap' if matched Resource is found. 
	 * 
	 * @param resourceMap Map&lt;String, List&lt;AResourceDescription&gt;&gt;
	 * @param resProps Map&lt;String, String&gt;
	 * 
	 * @return an Object of AResourceDescription
	 * 
	 * @see edu.wisc.trace.uch.resource.AResourceDescription
	 */
	AResourceDescription findResourceByResProps(LinkedHashMap<String, List<AResourceDescription>>resourceMap, Map<String, String> resProps) {
			
		String eltRef = resProps.get("eltRef");
		
		if ( eltRef == null ) {
			logger.warning("ResProps doesn't contain 'eltRef'.");
			return null;
		}
		
		logger.info("going to parse ResourceSheet.");
		
		List<AResourceDescription> aResDescList = new ArrayList<AResourceDescription>();
		parse(aResDescList);
		
		// For testing whether  <aResDes> Node parsed successfully.
		//print(aResDescList);
		
		boolean found = false;
		
		AResourceDescription aResDescToReturn = null;
		
		for(AResourceDescription aResDesc : aResDescList) {
			
			
			List<String> eltRefList = aResDesc.getEltRefList();
			
			for( String eltRefOfList : eltRefList) {
				
				if( eltRef.equals(eltRefOfList) ) {
					
					aResDescToReturn = aResDesc;
					found = true;
					break;
					
				}
			}
			/*
			if (found){
				logger.info(eltRef+" ---EltRef found.");
				aResDesc.print();
			}
			*/
			if ( found && aResDesc.propMatched(resProps) ) {
				break;
			} else {
				found = false;
			}
				
		}
		
		if ( !found ) {
			logger.warning("'"+eltRef+"' does not found in this ResourceSheet.");
			return null;
		}
		
		
		for(AResourceDescription aResDesc : aResDescList) 			
			putToResourceMap(resourceMap, aResDesc);
		
		return 	aResDescToReturn;
	}
	
	/**
	 * Put the AResourceDescription specified by 'aResDesc' in the Map 'resourceMap' if that AResourceDescription is not exist in that Map.
	 * 
	 * @param resourceMap Map&lt;String, List&lt;AResourceDescription&gt;&gt;
	 * @param aResDesc an Object of AResourceDescription
	 * @see edu.wisc.trace.uch.resource.AResourceDescription
	 */
	private void putToResourceMap(LinkedHashMap<String, List<AResourceDescription>>resourceMap, AResourceDescription aResDesc) {
		
		List<String> eltRefList = aResDesc.getEltRefList();
		
		for ( String eltRef : eltRefList ) {
			
			if ( resourceMap.containsKey(eltRef) ) {
				
				List<AResourceDescription> aResDesList = resourceMap.get(eltRef);
				
				boolean found = false;
				
				for( AResourceDescription aResDescOfList : aResDesList ) {
					
					if( aResDescOfList.equals(aResDesc) ) {
						found = true;
						break;
					}
				}
				
				if ( !found )
					aResDesList.add(aResDesc);
				
			} else { 
				
				List<AResourceDescription> aResDesList = new ArrayList<AResourceDescription>();
				aResDesList.add(aResDesc);
				
				resourceMap.put(eltRef, aResDesList);
			}
			
		}
		
	}
	
	/**
	 * Get XML Document Object using rSheetUri.
	 * 
	 * @return an Object of Document.
	 * 
	 * @see org.w3c.dom.Document
	 */
	private Document getDom() {
		
		if ( rSheetUri == null ) {
			logger.warning("rSheetUri is null.");
			return null;
		}
		
		Document resSheetDoc = null;
		
		try {
			resSheetDoc = CommonUtilities.parseDocument(rSheetUri);
	
		} catch (NullPointerException e) {
			logger.warning("NullPointerException : Going to parse file '"+rSheetUri+"'.");
		} catch (SAXException e) {
			e.printStackTrace();
			logger.warning("SAXException : Going to parse file '"+rSheetUri+"'.");
		} catch (IOException e) {
			logger.warning("IOException : Going to parse file '"+rSheetUri+"'.");
		} catch (ParserConfigurationException e) {
			logger.warning("ParserConfigurationException : Going to parse file '"+rSheetUri+"'.");
		}
		
		return resSheetDoc;
	}
	
	/**
	 * Parse the Resource Sheet and add all AResourceDescription to the List&lt;AResourceDescription&gt; specified by 'aResDescList'.
	 * 
	 * @param aResDescList List&lt;AResourceDescription&gt;
	 * @see edu.wisc.trace.uch.resource.AResourceDescription
	 */
	private void parse(List<AResourceDescription> aResDescList) {
		
		Document resSheetDoc = getDom();
		
		if ( resSheetDoc == null ) {
			logger.warning("Resource Sheet is not in proper Xml format.");
			return;
		}
			
		Element root = resSheetDoc.getDocumentElement();	
		
		if( !root.getLocalName().equals("ResSheet") ) {
			logger.warning("Resource Sheet's root node is not 'ResSheet'.");
			return;
		}
		
		//if( !root.hasAttribute("rdf:about") ) {
		
		if( !CommonUtilities.checkRDFAttribute(root, "about")) {
			logger.warning("Root node has not 'about' attribute.");
			return;
		}

		//about = root.getAttribute("rdf:about");
		about = CommonUtilities.getRDFAttribute(root, "about");
		
		
		/*
		if( !root.hasAttribute("xmlns") ) {
			logger.warning("Root node has not 'xmlns' attribute.");
			return;
		}
		*/
		// Added for providing relative path to contentAt //
		String rSheetURI = resourceSheetManager.convertURI( rSheetUri.toString() );
		
		String relativePathURI = null;
		
		if ( rSheetURI.indexOf("/") != -1 )
			relativePathURI = rSheetURI.substring(0, rSheetURI.lastIndexOf("/")+1 );
		else if ( rSheetURI.indexOf("\\") != -1 )
			relativePathURI = rSheetURI.substring(0, rSheetURI.lastIndexOf("\\")+1 );
		////////////////////////////////////////////////////
		
		if( root.hasAttribute("xmlns") ) 
			xmlns = root.getAttribute("xmlns");
		
		NodeList nodeList = root.getChildNodes();
		
		for(int i=0 ; i<nodeList.getLength() ; i++) {
			
			Node node = nodeList.item(i);
			String nodeName = node.getLocalName();
			
			if(nodeName == null)
				continue;
			
			if ( nodeName.equals("modified") && (node instanceof Element) && node.getChildNodes().getLength() == 1) {
				modified = node.getFirstChild().getNodeValue();
			} else if ( nodeName.equals("conformsTo") && (node instanceof Element) && node.getChildNodes().getLength() == 1) {
				conformsTo = node.getFirstChild().getNodeValue();
				if( !conformsTo.equals(Constants.PROPERTY_CONFORMS_TO_VALUE_RESSHEET) ){
					logger.severe("Document conforms to '" + conformsTo + "' is not equal to '" + Constants.PROPERTY_CONFORMS_TO_VALUE_RESSHEET+"'.");
					break;
				}
			} else if ( nodeName.equals("localAt") && (node instanceof Element) && node.getChildNodes().getLength() == 1) {
				localAt = node.getFirstChild().getNodeValue();
			} /*else if ( nodeName.equals("forLang") && (node instanceof Element) && node.getChildNodes().getLength() == 1) {
				forLang = node.getFirstChild().getNodeValue();
			}*/ else if ( nodeName.equals("audience") && (node instanceof Element) && node.getChildNodes().getLength() == 1) {
				audience = node.getFirstChild().getNodeValue();
			} /*else if ( nodeName.equals("type") && (node instanceof Element) && node.getChildNodes().getLength() == 1) {
				typeList.add(node.getFirstChild().getNodeValue());
			} else if ( nodeName.equals("format") && (node instanceof Element) && node.getChildNodes().getLength() == 1) {
				format = node.getFirstChild().getNodeValue();
			} else if ( nodeName.equals("forDomain") && (node instanceof Element) ) {
				forDomain = ((Element)node).getAttribute("rdf:resource");	rdf:resource is obsolate, if U uncomment this code please add condition for resource also.		
			} else if ( nodeName.equals("groupingForDomain") && (node instanceof Element) ) {
			
				2012-10-15 : Removed line for 'groupingForDomain' in resource sheet because now we have a separate Grouping Sheet.
							
				groupingForDomain = ((Element)node).getAttribute("rdf:resource");  rdf:resource is obsolate, if U uncomment this code please add condition for resource also.
				
			}*/ else if ( nodeName.equals("creator") && (node instanceof Element) && node.getChildNodes().getLength() == 1) {
				creator = node.getFirstChild().getNodeValue();
			} else if ( nodeName.equals("publisher") && (node instanceof Element) && node.getChildNodes().getLength() == 1) {
				publisher = node.getFirstChild().getNodeValue();
			} else if ( nodeName.equals("contributor") && (node instanceof Element) && node.getChildNodes().getLength() == 1) {
				contributor = node.getFirstChild().getNodeValue();
			} else if ( nodeName.equals("date") && (node instanceof Element) && node.getChildNodes().getLength() == 1) {
				date = node.getFirstChild().getNodeValue();
			} else if ( nodeName.equals("rights") && (node instanceof Element) && node.getChildNodes().getLength() == 1) {
				rights = node.getFirstChild().getNodeValue();
			}
			//Parikshit Thakur : 20111103. Added to parse new rSheet structure.
			else if(nodeName.equals("scents") && node.hasChildNodes()){
				
				NodeList scentsChildren = node.getChildNodes();
				
				for( int j=0 ; j<scentsChildren.getLength() ; j++ ) {
					Node chlNode = scentsChildren.item(j);
					if ( chlNode.getNodeName().equals("dc:type") && (chlNode instanceof Element)) {
						typeList.add(chlNode.getFirstChild().getNodeValue());
					}
					else if(chlNode.getNodeName().equals("forDomain") && (chlNode instanceof Element)){
						forDomain = chlNode.getFirstChild().getNodeValue();
					}
					else if(chlNode.getNodeName().equals("forLang") && (chlNode instanceof Element)){
						forLangList.add(chlNode.getFirstChild().getNodeValue());
					}
					else if(chlNode.getNodeName().equals("dc:format") && (chlNode instanceof Element)){
						format = chlNode.getFirstChild().getNodeValue();
					}
				}
				
			}
			else if ( nodeName.equals("resItems") && node.hasChildNodes() ) {
						
				Map<String, List<String>> resPropMap = getResPropMap();
				
				NodeList resItemsChilds = node.getChildNodes();
				
				for( int j=0 ; j<resItemsChilds.getLength() ; j++ ) {
					
					Node chlNode = resItemsChilds.item(j);
					
					if ( chlNode.getNodeName().equals("Grouping") ) {
						//Future Development : process for Grouping
					} else if ( chlNode.getNodeName().equals("AResDesc") && ( chlNode instanceof Element ) ) {
						
						AResourceDescription aResourceDescription = new AResourceDescription( (Element)chlNode, resPropMap, relativePathURI );
						aResDescList.add(aResourceDescription);
						
					}
				}
				
			}
			
			
		}
		
	}
	
	/**
	 * Generate and return a map of Resource Properties.
	 * 
	 * @return Map&lt;String, String&gt;
	 */
	private Map<String, List<String>> getResPropMap() { // Parikshit Thakur : 20111103. Modified code parse new resSheet structure.
		
		Map<String, List<String>> resPropMap = new HashMap<String, List<String>>();
		
		/*if ( forLang != null ) {
			resPropMap.put("forLang", CommonUtilities.convertToList(forLang));
		}*/
		
		if ( audience != null) {
			resPropMap.put("audience", CommonUtilities.convertToList(audience));
		}
		
		/*if ( typeList != null && typeList.size() > 0) {
			resPropMap.put("typeList", typeList);
		}*/
		
		if ( format != null) {
			resPropMap.put("format", CommonUtilities.convertToList(format));
		}
		
		if ( creator != null) {
			resPropMap.put("creator", CommonUtilities.convertToList(creator));
		}
		
		if ( publisher != null) {
			resPropMap.put("publisher", CommonUtilities.convertToList(publisher));
		}
		
		if ( contributor != null) {
			resPropMap.put("contributor", CommonUtilities.convertToList(contributor));
		}
		
		if ( date != null) {
			resPropMap.put("date", CommonUtilities.convertToList(date));
		}
		
		if ( rights != null) {
			resPropMap.put("rights", CommonUtilities.convertToList(rights));
		}		
		
		return resPropMap;
	}

	/**
	 * Get 'about' information
	 * @return a String
	 */
	String getAbout() {
		return about;
	}

	/**
	 * Get 'conformsTo' information
	 * @return a String
	 */
	String getConformsTo() {
		return conformsTo;
	}

	/**
	 * Get 'audience' information
	 * @return a String
	 */
	String getAudience() {
		return audience;
	}

	/**
	 * Get 'forDomain' information
	 * @return a String
	 */
	String getForDomain() {
		return forDomain;
	}

	/**
	 * Get 'groupingForDomain' information
	 * @return a String
	 */
	/*
		Removed line for 'groupingForDomain' in resource sheet because now we have a separate Grouping Sheet.
		
		String getGroupingForDomain() {
			return groupingForDomain;
		}
	 */
	
	/**
	 * Get 'creator' information
	 * @return a String
	 */
	String getCreator() {
		return creator;
	}

	/**
	 * Get 'contributor' information
	 * @return a String
	 */
	String getContributor() {
		return contributor;
	}

	/**
	 * Get 'xmlns' information
	 * 
	 * @return String value of xmlns
	 */
	String getXmlns() {
		return xmlns;
	}

	/**
	 * Get 'modified' information
	 * 
	 * @return String value of modified
	 */
	String getModified() {
		return modified;
	}

	/**
	 * Get 'localAt' information
	 * 
	 * @return String value of localAt
	 */
	String getLocalAt() {
		return localAt;
	}
	
	///////////////////////////////////////For testing////////////////////////////////////////
	
	/*
	private void recursiveParse(Element e , String space){
		
		NodeList nodeList = e.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node != null) {
				if (node instanceof Element) {
					Element element = (Element) nodeList.item(i);
					//if(element.getNamespaceURI().equalsIgnoreCase(Constants.UIS_NAMESPACE)){
					    logger.info(space + node.getLocalName()+" ATTRIBUTES : ");
					    NamedNodeMap namedNodeMap = element.getAttributes();
					    int x=1;
					    for(int j = 0 ; j < namedNodeMap.getLength() ; j++){
						    Node node1 = namedNodeMap.item(j);
						    if ( !(node1 instanceof Element) ) {
							     String str = "";
							     for(int k = 0 ; k <(space + node.getLocalName()+" ATTRIBUTES : ").length() ; k++ ){
								     str += " ";
							     }//node1.getNodeName()
							     logger.info(str + "("+ x +")" + node1.getNodeName() +"="+node1.getNodeValue());
							     x++;
						    }
					    }
					    if (element.hasChildNodes()) {
						    recursiveParse( element , space+"       ");
					    }
					//}
				}
			}
		}
		
	}
	 
	private void print( List<AResourceDescription> aResDescList) {
		
		logger.info("resource Sheet Parsing.");
		logger.info("about="+about);
		logger.info("xmlns="+xmlns);
		logger.info("publisher="+publisher);
		logger.info("type="+type);
		logger.info("forLang="+forLang);
		logger.info("forDomain="+forDomain);
		logger.info("groupingForDomain="+groupingForDomain);
		
		for(AResourceDescription aResourceDescription : aResDescList) {
			
			logger.info("          AResDes:About="+aResourceDescription.getAbout());
			logger.info("          AResDes:Content="+aResourceDescription.getContent());
			
			for( ResourceContext resourceContext : aResourceDescription.getContextList() ) {
				
				logger.info("                    eleRef="+resourceContext.getEltRef());
				logger.info("                    role="+resourceContext.getRole());
			}
		}
	}
	*/
////////////////////////////////////////////////////////////////////////////////////////////
}
