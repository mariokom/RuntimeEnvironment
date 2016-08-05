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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.wisc.trace.uch.util.CommonUtilities;

/**
 * Parse and Store childNodes and attributes of &lt;Context&gt; Element of &lt;AResDesc&gt; Element of ResourceSheet and provide methods to retrieve its properties.
 * Also provide methods to retrieve and match its Properties.
 *  
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */

class ResourceContext {

	
	private String eltRef;
	private List<String> valRefList;
	private List<String> opRefList;
	private List<String> forTargetInstanceList;
	private String role;
	private List<String> forLangList = new ArrayList<String>();

	
	/**
	 * Parse &lt;Context&gt; Element of &lt;AResDesc&gt; Element of ResourceSheet.
	 * 
	 * @param element an Object of Element
	 * @param forLang a String value of forLang
	 */
	ResourceContext(Element element) {

		//this.forLang = forLang; //Parikshit Thakur: 20111103. Removed inheritence from rSheet attributes.
		parseElement(element);
		
	}

	/**
	 * Parses the 'Context' element of Resource Sheet.
	 * 
	 * @param element an Object of Element
	 */
	private void parseElement(Element element) {
		
		NodeList nodeList = element.getChildNodes();
		
		for(int i=0; i<nodeList.getLength() ; i++) {
			
			Node node = nodeList.item(i);
			
			if( node.getNodeName().equals("eltRef") && (node instanceof Element) ) {
				
				//eltRef= ((Element)node).getAttribute("rdf:resource");
				eltRef= CommonUtilities.getRDFAttribute((Element)node, "resource");
				
			} else if ( node.getNodeName().equals("valRef") && (node instanceof Element) && (node.getChildNodes().getLength() == 1) ) {
				
				String valRef = ((Element)node).getFirstChild().getNodeValue();
				if ( valRef != null ) {
					
					if ( valRefList == null )
						valRefList = new ArrayList<String>();
					
					valRefList.add(valRef);
				}
				
			} else if ( node.getNodeName().equals("forTargetInstance") && (node instanceof Element) && (node.getChildNodes().getLength() == 1) ) {
				
				String forTargetInstance = ((Element)node).getFirstChild().getNodeValue();
				if ( forTargetInstance != null ) {
					
					if ( forTargetInstanceList == null )
						forTargetInstanceList = new ArrayList<String>();
					
					forTargetInstanceList.add(forTargetInstance);
				}
				
				
			} else if ( node.getNodeName().equals("opRef") && (node instanceof Element) ) {
				
				//String opRef = ((Element)node).getAttribute("rdf:resource");
				String opRef = CommonUtilities.getRDFAttribute((Element)node,"resource");
				
				if ( opRef != null ) {
					
					if( opRefList == null )
						opRefList = new ArrayList<String>();
					
					opRefList.add(opRef);
				}
					
			} else if ( node.getNodeName().equals("role") && (node instanceof Element) ) {
				
				//role = ((Element)node).getAttribute("rdf:resource");
				role = CommonUtilities.getRDFAttribute((Element)node, "resource");
				
				
			} else if ( node.getNodeName().equals("forLang") && node.hasChildNodes() && (node.getChildNodes().getLength() == 1)  ) {
				String forLang = node.getFirstChild().getNodeValue();
			
				if(forLang != null)
					this.forLangList.add(forLang) ;
			}
			
		}
		
	}
	
	/**
	 * Compare the properties of &lt;context&gt; Element. 
	 * If matched return true else return false.
	 *  
	 * @param eltRef a String value of eltRef
	 * @param valRef a String value of valRef
	 * @param opRef a String value of opRef
	 * @param role a String value of role
	 * @param forLang a String value of forLang
	 * @return boolean
	 */
	boolean propMatched(String eltRef, String valRef, String opRef, String role, String forLang, String forTargetInstance) {
		
		if ( eltRef != null ) {
			if ( this.eltRef == null || !eltRef.equals(this.eltRef) )
				return false;
		}
		
		if ( role != null ) {
			if ( this.role == null || !role.equals(this.role) )
				return false;
		}
		
		if ( forLang != null ) {
			// Parikshit Thakur : 20111103. Modified code to support multiple forLang value.
			boolean matched = false;
			if(this.forLangList != null && this.forLangList.size() > 0){
				for(int i=0; i<this.forLangList.size(); i++){
					if(this.forLangList.get(i).equals(forLang))
						matched = true;
				}
				if(!matched)
					return false;
			}
			//if ( this.forLang == null || !forLang.equals(this.forLang) )
			//	return false;
		}
		
		if ( valRef != null ) {
			if ( valRefList == null || !valRefList.contains(valRef) )
				return false;
		}else{// Yuvaraj:2013-04-20-Bug removed:If the label for custom type is not found, label for enumeration values was used.   
			
			if(valRefList != null)
				return false;
		}
		
		if ( opRef != null ) {
			if ( opRefList == null || !opRefList.contains(opRef) )
				return false;
		}
		
		if ( forTargetInstance != null ) {
			if ( forTargetInstanceList == null || !forTargetInstanceList.contains(forTargetInstance) )
				return false;
		}
		
		return true;
	}
	
	
	/**
	 * Return String value of Element Reference(eltRef).
	 * 
	 * @return a String value of Element Reference(eltRef)
	 */
	String getEltRef() {
		return eltRef;
	}

	/**
	 * Return String value of role.
	 * 
	 * @return a String value of role
	 */
	String getRole() {
		return role;
	}

	/**
	 * Return List&lt;String&gt; of Value Reference(valRef).
	 * 
	 * @return a List&lt;String&gt; of Value Reference(valRef)
	 */
	List<String> getValRefList() {
		return valRefList;
	}
	
	/**
	 * Return List&lt;String&gt; of Operation Reference(opRef).
	 * 
	 * @return a List&lt;String&gt; of Operation Reference(opRef)
	 */
	List<String> getOpRefList() {
		return opRefList;
	}
	
	/**
	 * Return String value of forLang.
	 * 
	 * @return a String value of forLang
	 */
	List<String> getForLang() {
		return forLangList;
	}

	
}
