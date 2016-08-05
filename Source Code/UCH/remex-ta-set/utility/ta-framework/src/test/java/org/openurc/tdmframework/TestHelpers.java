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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.openurc.uch.ITDMListener;
/**
 * 
 * @author Lukas Smirek
 *
 */
public class TestHelpers {

	
	public static ITDMListener getTestTDMListener(String sourceFile){
		Map<String,String>	 props = null;

		System.out.println(new File(sourceFile).getAbsolutePath()   );
		InputStream in;
		try {
			System.out.println("Reading XML file");
			in = new FileInputStream( sourceFile );
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory.createXMLStreamReader( in );
	
		RPROPParser rpropParser = new RPROPParser(parser);
	 rpropParser.parseProps();
		props = rpropParser.getprops();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			 			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
return  new TestITDMListener(props);

	}
}
