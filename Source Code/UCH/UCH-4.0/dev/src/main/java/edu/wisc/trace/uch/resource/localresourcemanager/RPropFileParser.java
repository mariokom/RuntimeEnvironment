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
package edu.wisc.trace.uch.resource.localresourcemanager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import edu.wisc.trace.uch.resource.util.XmlParserUtil;
import edu.wisc.trace.uch.util.CommonUtilities;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide methods to parse UCH Config File(.rprop).
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $ Revision: 1.0 $
 */
public class RPropFileParser {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private static final String NODE_NAME_PROPS = "props";
	
	private File rpropFile;
	
	/**
	 * Constructor.
	 * Provide the reference of UCH Config File to local variable.
	 * 
	 * @param rpropFile an Object of File.
	 */
	RPropFileParser(File rpropFile) {
		
		this.rpropFile = rpropFile;
	}
	
	/**
	 * Parse .rprop(UCH Config File) and returns property map.
	 * 
	 * @return an Object of Map&lt;String, List&lt;String&gt;&gt;
	 */
	Map<String, List<String>> parse() {
		
		if ( rpropFile == null ) {
			logger.warning("UCH Config File is null.");
			return null;
		}
		
		if ( !rpropFile.exists() ) {
			logger.warning("UCH Config File '"+rpropFile.getAbsolutePath()+"' is not exists.");
			return null;
		}
		
		if ( !rpropFile.isFile() ) {
			logger.warning("UCH Config File '"+rpropFile.getAbsolutePath()+"' is not a file.");
			return null;
		}
		
		Document doc = null;
		
		try {
			doc = CommonUtilities.parseDocument(rpropFile);
		} catch (NullPointerException e) {
			logger.info("NullPointerException : Parsing XML file '" + rpropFile.getAbsolutePath() + "'.");
		} catch (SAXException e) {
			logger.info("SAXException : Parsing XML file '" + rpropFile.getAbsolutePath() + "'.");
		} catch (IOException e) {
			logger.info("IOException : Parsing XML file '" + rpropFile.getAbsolutePath() + "'.");
		} catch (ParserConfigurationException e) {
			logger.info("ParserConfigurationException : Parsing XML file '" + rpropFile.getAbsolutePath() + "'.");
		}
		
		if ( doc == null ) {
			logger.warning("'"+rpropFile.getAbsolutePath()+"' file is not in proper XML format.");
			return null;
		}
		
		Element root = doc.getDocumentElement();
		
		if ( root == null ) {
			logger.warning("Unable to get root node from the XML file '" + rpropFile.getAbsolutePath() + "'.");
			return null;
		}
		
		String rootNodeName = root.getNodeName();
		
		if ( (rootNodeName == null) || !rootNodeName.equals(NODE_NAME_PROPS) ) {
			logger.warning("Root Node Name of the XML file '" + rpropFile.getAbsolutePath() + "' is not '" + NODE_NAME_PROPS + "'.");
			return null;
		}
		
		return XmlParserUtil.getProps(root);
	}
}
