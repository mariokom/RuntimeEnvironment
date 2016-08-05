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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Parse and Store &lt;AResDesc&gt; element of ResourceSheet.
 * Also store value of Inherited Properties from Resource Sheet.
 * Provide methods to retrieve and match its properties.
 *  
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */


public class AResourceDescription {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private String relativePathURI;
	
	
	private List<String> forLangList = new ArrayList<String>();
	private List<String> typeList = new ArrayList<String>(); 
	private String format;
	private String about;
	private String content;
	private String contentAt;
	
	private List<ResourceContext> contextList;
	private List<String> creatorList;
	private List<String> publisherList;
	private List<String> contributorList;
	private List<String> dateList;
	private List<String> rightsList;
	private List<String> audienceList;
	private List<String> hasVersionList;
	private List<String> isVersionOfList;
	private List<String> isReplacedByList;
	private List<String> replacesList;
	
	/**
	 * Parse the &lt;AResDesc&gt; Node of Resource Sheet and Stores attributes and childNodes Values. 
	 * 
	 * @param element an Object of Element 
	 * @param resPropMap a Map&lt;String, String&gt; of Resource Properties inherited outside of &lt;AResDesc&gt; Node of Resource Sheet.
	 * @param relativePathURI a URI String used prepare value for contentAt 
	 */
	AResourceDescription(Element element, Map<String, List<String>> resPropMap, String relativePathURI) {
		//Parikshit Thakur:20111103. Changed argument from 'Map<String, String> resPropMap' to 'Map<String, List<String>> resPropMap'
		this.relativePathURI = relativePathURI;
		
		for ( String key : resPropMap.keySet() ) {
			
			List<String> valueList = resPropMap.get(key);
			
			//Parikshit Thakur:20111103. removed inheritance of forLang and type from rsheet attributes.
			/*if ( key.equals("forLang") ) {	
				forLang = valueList.get(0);
			} */
			/*else if ( key.equals("typeList") ) {
				
				for(int i=0; i<valueList.size(); i++ ){
					typeList.add(valueList.get(i));
				}
				
			}*/
			if ( key.equals("format") ) {	
				format = valueList.get(0);
			} else if ( key.equals("audience") ) {
				
				if ( !hasAudience(valueList.get(0)) ) {
					
					if ( audienceList == null )
						audienceList = new ArrayList<String>();
					
					audienceList.add(valueList.get(0));
				}
			} else if ( key.equals("creator") ) {
				
				if ( !hasCreator(valueList.get(0)) ) {
					
					if ( creatorList == null )
						creatorList = new ArrayList<String>();
					
					creatorList.add(valueList.get(0));
				}
			} else if ( key.equals("publisher") ) {
				
				if ( !hasPublisher(valueList.get(0)) ) {
					
					if ( publisherList == null )
						publisherList = new ArrayList<String>();
					
					publisherList.add(valueList.get(0));
				}
			} else if ( key.equals("contributor") ) {
				
				if ( !hasContributor(valueList.get(0)) ) {
					
					if ( contributorList == null )
						contributorList = new ArrayList<String>();
					
					contributorList.add(valueList.get(0));
				}
			} else if ( key.equals("date") ) {
				
				if ( !hasDate(valueList.get(0)) ) {
					
					if ( dateList == null )
						dateList = new ArrayList<String>();
					
					dateList.add(valueList.get(0));
				}
			} else if ( key.equals("rights") ) {
				
				if ( !hasRights(valueList.get(0)) ) {
					
					if ( rightsList == null )
						rightsList = new ArrayList<String>();
					
					rightsList.add(valueList.get(0));
				}
			}
				
		}
		
		parseElement(element);
	}
	
	
	/**
	 * Return List of Element Reference(eltRef).
	 * 
	 * @return  a List&lt;String&gt; of 'eltRef'.
	 */
	List<String> getEltRefList() {
		
		List<String> eltRefList = new ArrayList<String>();
		
		for( ResourceContext resourceContext : contextList ) 
			eltRefList.add( resourceContext.getEltRef() );
		
		
		return eltRefList;
	}
	
	/**
	 * Return value of 'about' Attribute of Resource Sheet.
	 * 
	 * @return a String representing value of 'about'
	 */
	String getAbout() {
		return about;
	}

	/**
	 * Return value of 'content' Element of Resource Sheet.
	 * 
	 * @return a String representing value of 'content'
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Return value of 'contentAt' Attribute of Resource Sheet.
	 * 
	 * @return a String representing value of 'contentAt'
	 */
	public String getContentAt() {
		return contentAt;
	}
	
	/**
	 * Return List of &lt;Context&gt; Child Nodes  of &lt;AResDesc&gt; Node of Resource Sheet.
	 * 
	 * @return a List&lt;ResourceContext&gt; representing &lt;Context&gt; Child Nodes  of &lt;AResDesc&gt;
	 */
	List<ResourceContext> getContextList() {
		return contextList;
	}

	/**
	 * Check whether Creator exist or not.
	 * 
	 * @param creator a String value of creator
	 * @return whether Creator exists or not.
	 */
	boolean hasCreator(String creator) {
		
		if ( creatorList == null )
			return false;
		
		for( String creatorFromList : creatorList ) {
			if( creatorFromList.equals(creator) )
				return true;
		}
		
		return false;
	}
	
	/**
	 * Check whether publisher exists or not.
	 * 
	 * @param publisher a String value of publisher
	 * @return boolean
	 */
	boolean hasPublisher(String publisher) {
		
		if ( publisherList == null )
			return false;
		
		for( String publisherFromList : publisherList ) {
			if( publisherFromList.equals(publisher) )
				return true;
		}
		
		return false;
	}
	
	/**
	 * Check whether Contributor exists or not.
	 * 
	 * @param contributor a String value of contributor
	 * @return whether Contributor exists or not
	 */
	boolean hasContributor(String contributor) {
		
		if ( contributorList == null )
			return false;
		
		for( String contributorFromList : contributorList ) {
			if( contributorFromList.equals(contributor) )
				return true;
		}
		
		return false;
	}
	
	/**
	 * Check whether Date exists or not.
	 * 
	 * @param date a String value of date
	 * @return whether Date exists or not
	 */
	boolean hasDate(String date) {
		
		if ( dateList == null )
			return false;
		
		for( String dateFromList : dateList ) {
			if( dateFromList.equals(date) )
				return true;
		}
		
		return false;
	}
	
	/**
	 * Check whether Rights exists or not.
	 * 
	 * @param rights a String value of rights
	 * @return whether Rights exists or not
	 */
	boolean hasRights(String rights) {
		
		if ( rightsList == null )
			return false;
		
		for( String rightsFromList : rightsList ) {
			if( rightsFromList.equals(rights) )
				return true;
		}
		
		return false;
	}

	/**
	 * Check whether Audience exists or not.
	 * 
	 * @param audience a String value of audience
	 * @return whether Audience exists or not
	 */
	boolean hasAudience(String audience) {
		
		if ( audienceList == null )
			return false;
		
		for( String audienceFromList : audienceList ) {
			if( audienceFromList.equals(audience) )
				return true;
		}
		
		return false;
	}
	
	/**
	 * Check whether Version exists or not.
	 * 
	 * @param hasVersion a String value of hasVersion
	 * @return whether Version exists or not
	 */
	boolean hasVersion(String hasVersion) {
		
		if ( hasVersionList == null )
			return false;
		
		for( String hasVersionFromList : hasVersionList ) {
			if( hasVersionFromList.equals(hasVersion) )
				return true;
		}
		
		return false;
	}
	
	/**
	 * Check whether IsVersionOf exists or not.
	 * 
	 * @param isVersionOf a String value of isVersionOf
	 * @return whether IsVersionOf exists or not
	 */
	boolean hasIsVersionOf(String isVersionOf) {
		
		if ( isVersionOfList == null )
			return false;
		
		for( String isVersionOfFromList : isVersionOfList ) {
			if( isVersionOfFromList.equals(isVersionOf) )
				return true;
		}
		
		return false;
	}
	
	/**
	 * Check whether IsReplacedBy exists or not.
	 * 
	 * @param isReplacedBy a String value of isReplacedBy
	 * @return whether IsReplacedBy exists or not
	 */
	boolean hasIsReplacedBy(String isReplacedBy) {
		
		if ( isReplacedByList == null )
			return false;
		
		for( String isReplacedByFromList : isReplacedByList ) {
			if( isReplacedByFromList.equals(isReplacedBy) )
				return true;
		}
		
		return false;
	}
	
	/**
	 * Check whether hasReplaces exists or not.
	 * 
	 * @param replaces a String value of replaces
	 * @return whether hasReplaces exists or not
	 */
	boolean hasReplaces(String replaces) {
		
		if ( replacesList == null )
			return false;
		
		for( String replacesFromList : replacesList ) {
			if( replacesFromList.equals(replaces) )
				return true;
		}
		
		return false;
	}

	/**
	 * Parse the &lt;AResDesc&gt; Element of Resource Sheet.
	 * 
	 * @param element an Object of Element
	 */
	
	private void parseElement( Element element) {
		
		if(!CommonUtilities.checkRDFAttribute(element, "about")){
			
			logger.warning("Element has not 'about' attribute.");
			return;
		}
		
		about = CommonUtilities.getRDFAttribute(element, "about");
		
		boolean firstTime = true;
		
		NodeList nodeList = element.getChildNodes();
		
		for(int i=0 ; i<nodeList.getLength() ; i++ ) {
			
			Node node = nodeList.item(i);
						
			if ( node.getNodeName().equals("content") && (node instanceof Element) && node.getChildNodes().getLength() == 1 ) {	
				
				content = ((Element)node).getFirstChild().getNodeValue();	
				
			} if ( node.getNodeName().equals("contentAt") && (node instanceof Element) ) {
				
				String cntAt = CommonUtilities.getRDFAttribute((Element)node, "resource");
				
				if ( cntAt != null ) {
					
					if( cntAt.startsWith("/") || cntAt.startsWith("\\") )
						cntAt = cntAt.substring(1);
					
					if ( (cntAt.indexOf("http://") != -1) || (cntAt.indexOf("https://") != -1) ) { 
						
						contentAt = cntAt;
						
					} else {
						
						if ( relativePathURI == null )
							contentAt = cntAt;
						else 
							contentAt = relativePathURI + cntAt;
					}
					
				} 
				
			} else if ( node.getNodeName().equals("useFor") && (node instanceof Element) && node.hasChildNodes() ) {
				
				NodeList useForCld = node.getChildNodes();
				
				for( int j=0; j<useForCld.getLength() ; j++ ) {
					
					Node cldNode = useForCld.item(j);	
					
					if( cldNode.getNodeName().equals("Context") && (cldNode instanceof Element) && cldNode.hasChildNodes() ) {
						
						ResourceContext resourceContext = new ResourceContext((Element)cldNode);
						
						if ( contextList == null )
							contextList = new ArrayList<ResourceContext>();
						
						contextList.add(resourceContext);
					}
					
				}
				
			} 
			
			else if ( node.getNodeName().equals("dc:type") && (node instanceof Element) && node.getChildNodes().getLength() == 1 ) {
				//Parkishit Thakur : 20111103. Modified code to support multiple types in rsheet. 
				/*if(firstTime){
					typeList.clear();
					firstTime = false;
				}*/
				typeList.add(node.getFirstChild().getNodeValue());
				
			} 
			else if ( node.getNodeName().equals("dc:format") && (node instanceof Element) && node.getChildNodes().getLength() == 1 ) {
				
				format = node.getFirstChild().getNodeValue();
				
				
			} else if ( node.getNodeName().equals("creator") && (node instanceof Element) && node.getChildNodes().getLength() == 1 ) {
				
				if ( creatorList == null )
					creatorList = new ArrayList<String>();
				
				creatorList.add( node.getFirstChild().getNodeValue() ); 
				
			} else if ( node.getNodeName().equals("publisher") && (node instanceof Element) && node.getChildNodes().getLength() == 1 ) {
				
				if ( publisherList == null )
					publisherList = new ArrayList<String>();

				publisherList.add( node.getFirstChild().getNodeValue() ); 
				
			} else if ( node.getNodeName().equals("contributor") && (node instanceof Element) && node.getChildNodes().getLength() == 1 ) {
				
				if ( contributorList == null )
					contributorList = new ArrayList<String>();

				contributorList.add( node.getFirstChild().getNodeValue() ); 
				
			} else if ( node.getNodeName().equals("date") && (node instanceof Element) && node.getChildNodes().getLength() == 1 ) {
				
				if ( dateList == null )
					dateList = new ArrayList<String>();

				dateList.add( node.getFirstChild().getNodeValue() ); 
				
			} else if ( node.getNodeName().equals("rights") && (node instanceof Element) && node.getChildNodes().getLength() == 1 ) {
				
				if ( rightsList == null )
					rightsList = new ArrayList<String>();

				rightsList.add( node.getFirstChild().getNodeValue() ); 
				
			} else if ( node.getNodeName().equals("audience") && (node instanceof Element) && node.getChildNodes().getLength() == 1 ) {
				
				if ( audienceList == null )
					audienceList = new ArrayList<String>();

				audienceList.add( node.getFirstChild().getNodeValue() ); 
				
			} else if ( node.getNodeName().equals("hasVersion") && (node instanceof Element) && node.getChildNodes().getLength() == 1 ) {
				
				if ( hasVersionList == null )
					hasVersionList = new ArrayList<String>();

				hasVersionList.add( node.getFirstChild().getNodeValue() ); 
				
			} else if ( node.getNodeName().equals("isVersionOf") && (node instanceof Element) && node.getChildNodes().getLength() == 1 ) {
				
				if ( isVersionOfList == null )
					isVersionOfList = new ArrayList<String>();

				isVersionOfList.add( node.getFirstChild().getNodeValue() ); 
				
			} else if ( node.getNodeName().equals("isReplacedBy") && (node instanceof Element) && node.getChildNodes().getLength() == 1 ) {
				
				if ( isReplacedByList == null )
					isReplacedByList = new ArrayList<String>();

				isReplacedByList.add( node.getFirstChild().getNodeValue() ); 
				
			} else if ( node.getNodeName().equals("replaces") && (node instanceof Element) && node.getChildNodes().getLength() == 1 ) {
				
				if ( replacesList == null )
					replacesList = new ArrayList<String>();

				replacesList.add( node.getFirstChild().getNodeValue() ); 
				
			}		
		}
		
	}
	
	/**
	 * Check whether all the properties are matched and if matches returns true else returns false.
	 * 
	 * @param resProps a Map&lt;String, String&gt; of Resource Properties
	 * @return whether all the properties match or not
	 */
	public boolean propMatched(Map<String, String> resProps) {
		
		String eltRef = resProps.get("eltRef");
		String valRef = resProps.get("valRef");
		String opRef = resProps.get("opRef");
		String role = resProps.get("role");
		String forLang = resProps.get("forLang");
	    String forTargetInstance = resProps.get("forTargetInstance");
		boolean matched = false;
		
		//logger.info("Request   :eltRef="+eltRef+" type="+resProps.get("type")+" forLang="+forLang);
		//logger.info("Available :type="+type+" forLang="+this.forLang+" Content="+content+" ContentAt="+contentAt);
		//							print();
		
		for(ResourceContext context : contextList) {
			
			if ( context.propMatched(eltRef, valRef, opRef, role, forLang, forTargetInstance) ) {
				matched =true;
				break;
			}
		}
		
		if ( !matched )
			return false;	
		
		String type = resProps.get("type");
		//Parikshit Thakur : 20111103. Modified code to support multiple types in rsheet.
		/*if ( type != null && !type.equals(this.type) )
			return false;*/
		if ( type != null ){
			boolean match = false;
			for(int i=0; i< this.typeList.size(); i++){
				if(type.equals(this.typeList.get(i)))
					match = true;
			}
			if(!match)
			return false;
		}
			
		
		String format = resProps.get("format");
		if ( format != null && !format.equals(this.format) )
			return false;
		
		String creator = resProps.get("creator");
		if ( creator != null ) {
			
			matched = false;
			for(String creatorOfList : creatorList) {
				
				if ( creator.equals(creatorOfList) ) {
					matched =true;
					break;
				}
			}
		}
		
		if ( !matched )
			return false;	
		
		String publisher = resProps.get("publisher");
		if ( publisher != null ) {
			
			matched = false;
			for(String publisherOfList : publisherList) {
				
				if ( publisher.equals(publisherOfList) ) {
					matched =true;
					break;
				}
			}
		}
		
		if ( !matched )
			return false;
		
		String date = resProps.get("date");
		if ( date != null ) {
			
			matched = false;
			for(String dateOfList : dateList) {
				
				if ( date.equals(dateOfList) ) {
					matched =true;
					break;
				}
			}
		}
		
		if ( !matched )
			return false;
		
		String audience = resProps.get("audience");
		if ( audience != null ) {
			
			matched = false;
			for(String audienceOfList : audienceList) {
				
				if ( audience.equals(audienceOfList) ) {
					matched =true;
					break;
				}
			}
		}
		
		if ( !matched )
			return false;
		
		
		return true;
	}

	/**
	 * Print local variables.
	 *  Just for testing 
	 */
	void print() {
		
		logger.info("--------------AResourceDescription-----------------");
		for ( ResourceContext context : contextList) {
			
			logger.info("eltRef="+context.getEltRef());
			logger.info("opRef="+context.getOpRefList());
			logger.info("valRef="+context.getValRefList());
			logger.info("role="+context.getRole());
			logger.info("forLang="+context.getForLang());
			logger.info("");
			
		}
		
		logger.info("type="+typeList);
		logger.info("format="+format);
		logger.info("about="+about);
		logger.info("content="+content);
		logger.info("contentAt="+contentAt);
		logger.info("creator="+creatorList);
		logger.info("publisher="+publisherList);
		logger.info("contributor="+contributorList);
		logger.info("date="+dateList);
		logger.info("rights="+rightsList);
		logger.info("audience="+audienceList);
		logger.info("hasVersion="+hasVersionList);
		logger.info("isVersionOf="+isVersionOfList);
		logger.info("isReplacedBy="+isReplacedByList);
		logger.info("replaces="+replacesList);
		
	}
}
