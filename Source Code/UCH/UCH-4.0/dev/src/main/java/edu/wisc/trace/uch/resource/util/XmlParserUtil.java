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
package edu.wisc.trace.uch.resource.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide methods to parse XML nodes.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version Revision: 1.0
 */
public class XmlParserUtil {

	private static Logger logger = LoggerUtil.getSdkLogger();
	
	private static final String NODE_NAME_PROP = "prop";
	private static final String NODE_NAME_NAME = "name";
	private static final String NODE_NAME_VALUE = "value";
	
	
	/**
	 * Traverse child nodes of specified node and return a map that contains property names and its value.
	 * If same property is repeated then add value to property value list.
	 * 
	 * @param node an Object of Node.
	 * 
	 * @return an Object of Map&lt;String, List&lt;String&gt;&gt;
	 */
	public static Map<String, List<String>> getProps(Node node) {
		
		 if ( node == null ) {
			 logger.warning("Node is null.");
			 return null;
		 }
		 
		 if ( node.getNodeType() != Node.ELEMENT_NODE ) {
			 logger.warning("Node is not Element Node.");
			 return null;
		 }
		 
		 if ( !node.hasChildNodes() ) {
			 logger.warning("Node does not have child nodes.");
			 return null;
		 }
		 
		 NodeList nodeList = node.getChildNodes();
		 
		 if ( (nodeList == null) || (nodeList.getLength() == 0) ) {
			 logger.warning("Node does not have child nodes.");
			 return null;
		 }
		 
		 Map<String, List<String>> propMap = new HashMap<String, List<String>>();
		 
		 for ( int i=0 ; i<nodeList.getLength() ; i++ ) {
			 
			 Node propNode = nodeList.item(i);
			 
			 if ( (propNode == null) || (propNode.getNodeType() != Node.ELEMENT_NODE) || !propNode.hasChildNodes() ) 
				 continue;
			 
			 String propNodeName = propNode.getNodeName();
			 
			 if ( (propNodeName == null) || !propNodeName.equals(NODE_NAME_PROP) )
				 continue;
			 
			 NodeList propChildren = propNode.getChildNodes();
			 
			 if ( (propChildren == null) || (propChildren.getLength() == 0) )
				 continue;
			 
			 String name = null;
			 String value = null;
			 
			 for ( int j=0 ; j<propChildren.getLength() ; j++ ) {
				
				Node propChild = propChildren.item(j);
				
				if ( (propChild == null) || (propChild.getNodeType() != Node.ELEMENT_NODE) || !propChild.hasChildNodes() )
					continue;
				
				String propChildNodeName = propChild.getNodeName();
				
				if ( propChildNodeName == null )
					continue;
				
				if ( propChildNodeName.equals(NODE_NAME_NAME) ) {
					
					Node nameNode = propChild.getFirstChild();
					
					if ( (nameNode == null) || (nameNode.getNodeType() != Node.TEXT_NODE) )
						continue;
					
					name = nameNode.getNodeValue();
					
				} else if ( propChildNodeName.equals(NODE_NAME_VALUE) ) {
					
					Node valueNode = propChild.getFirstChild();
					
					if ( (valueNode == null) || (valueNode.getNodeType() != Node.TEXT_NODE) )
						continue;
					
					value = valueNode.getNodeValue();
				}
			}
			 
			 addProp(name, value, propMap);
		 }
		 
		return propMap;
	}
	
	
	/**
	 * Add specified name and value to Map.
	 * If map contains entry of name then it add the value to corresponding list of values.
	 * Else create a list of values and add value to it.
	 * 
	 * @param name a String value of Name
	 * @param value a String value
	 * @param map an Object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return a boolean value specifies whether name and value are added successfully
	 */
	private static boolean addProp(String name, String value, Map<String, List<String>> map) {
		
		if ( (name == null) || (value == null) || (map == null) )
			return false;
		
		name = name.trim();
		value = value.trim();
		
		if ( map.containsKey(name) ) {
			
			List<String> values = map.get(name);
			
			if ( values == null ) {
				
				values = new ArrayList<String>();
				values.add(value);
				map.put(name, values);
				
			} else {
				
				values.add(value);
			}
			
		} else {
			
			List<String> values = new ArrayList<String>();
			values.add(value);
			map.put(name, values);
		}
		
		return true;
	}
}
