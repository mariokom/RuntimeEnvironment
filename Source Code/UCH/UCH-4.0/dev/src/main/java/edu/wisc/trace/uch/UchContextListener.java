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

package edu.wisc.trace.uch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Does the initialization when the UCH Context is initialized in WebServer and
 * performs necessary tasks when Context is destroyed.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */

public class UchContextListener implements ServletContextListener {

	private Logger logger = LoggerUtil.getSdkLogger();
	private UCH uch;
	private static final String INIT_PARAMETER_DEBUG_LEVEL = "debugLevel";

	/**
	 * Clean up when Context is destroyed.
	 * 
	 * @param servletContextEvent
	 *            an Object of ServletContextEvent
	 * 
	 * @see javax.servlet.ServletContext
	 */
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
uch.stopUCH();
	}

	/**
	 * Provide the initialization when the Context is initialized. Set
	 * appropriate debug level. Instantiate an object UCH and set it as an
	 * attributes of ServletContext.
	 * 
	 * @param servletContextEvent
	 *            an Object of ServletContextEvent
	 * 
	 * @see javax.servlet.ServletContext
	 */
	public void contextInitialized(ServletContextEvent servletContextEvent) {

		ServletContext servletContext = servletContextEvent.getServletContext();
		String loggerLevel = servletContext.getInitParameter(INIT_PARAMETER_DEBUG_LEVEL);
createLogger(loggerLevel);		
		try {
			uch = UCH.getInstance(servletContext);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		servletContext.setAttribute(Constants.CONSTANT_UCH, uch);
	}
	
	
private static void createLogger(String loggerLevel){


	
	
	
	Logger logger = LoggerUtil.getSdkLogger();
System.out.println("LL: " + loggerLevel );
	if (loggerLevel != null) {

		if (loggerLevel.trim().equals("0")) {
			logger.setLevel(Level.OFF);
		} else if (loggerLevel.trim().equals("1")) {
			logger.setLevel(Level.SEVERE);
		} else if (loggerLevel.trim().equals("2")) {
			logger.setLevel(Level.WARNING);
		} else if (loggerLevel.trim().equals("3")) {
			logger.setLevel(Level.INFO);
		}

	} else {

		logger.setLevel(Level.OFF);
	}
	
}
}
