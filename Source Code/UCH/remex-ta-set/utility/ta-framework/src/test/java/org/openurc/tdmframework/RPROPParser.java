/*
Copyright 2015 Hochschule der Medien (HDM) / Stuttgart Media University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 

*/


package org.openurc.tdmframework;



import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
/**
 * 
 * @author Lukas Smirek
 *
 */
public class RPROPParser {
	private XMLStreamReader parser;
private Map<String, String> props;


	public RPROPParser(XMLStreamReader parser) {
		this.parser = parser;
		props = new HashMap<String, String>();
			}

	public void parseProps(){
		StringBuilder spacer = new StringBuilder();

		try {
			String elementName = ""; // hier
			String attr = "";
			boolean open = true;
			
			while ( parser.hasNext() )
			{		
			

int event = parser.getEventType(); 

switch ( event )
{

case XMLStreamConstants.START_DOCUMENT:
System.out.println( "START_DOCUMENT: " + parser.getVersion() );
break;
case XMLStreamConstants.END_DOCUMENT:
 System.out.println( "END_DOCUMENT: " );
				  parser.close();
break;

				 case XMLStreamConstants.NAMESPACE:
break;

case XMLStreamConstants.START_ELEMENT:
	elementName = parser.getLocalName();
break;

case XMLStreamConstants.CHARACTERS:
if ( !parser.isWhiteSpace() && 	elementName.equals("value") ){
String value = parser.getText();


props.put(attr, value);
} 
if ( !parser.isWhiteSpace() && 	elementName.equals("name") ){
	attr = parser.getText();
}

 break;

case XMLStreamConstants.END_ELEMENT:
break;
				 default:
				  break;
			  }
				 try {
					
					 parser.next();
				} catch (XMLStreamException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

}
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}

	public Map getprops() {
		return props;
		
	}

	
	
}
