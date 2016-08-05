package edu.wisc.trace.uch.launch;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.xml.bind.JAXBException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import edu.wisc.trace.uch.servlet.UchConfigServlet;
import edu.wisc.trace.uch.servlet.UchServlet;
import edu.wisc.trace.uch.servlet.UserContextServlet;
import edu.wisc.trace.uch.util.Configuration;
import edu.wisc.trace.uch.util.ServerConsole;

/**  
 * Launcher Class instantiating and configuring the embedded Tomcat. 
 * 
 * @author  Sven Lindauer
 * @created 13.08.2015
 
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



public class Launcher{
	//Servlet Path
	public static boolean online = false;
	public static String[] uchUrlPatterns = {"/GetResources","/GetDocument","/InvokeLocator","/URC-HTTP/config","/urchttp/*","/GC100", "/GC100/config" };
	public static String[] uchDefaultServlet = {"/"};
	public static String[] uchConfigUrlPatterns = {"/GetCompatibleUIs","/config","/cache","/getusername","/information","/updateuser"};
	public static String[] userContextUrlPatterns = {"/openUserContext", "/closeUserContext" };
	private static final String CONTEXT_PATH = "/UCH";
	
	//Servlet Names
	public static final String UCH_SERVLET = "uchServlet";
	public static final String UCH_CONFIG_SERVLET = "uchConfigServlet";
	public static final String USER_CONTEXT_SERVLET = "userContextServlet";
	public static final String DEFAULT_SERVLET = "default";
	
	//private static File configFile;
	
	public static double JAVA_VERSION = getVersion ();	
	public static void main
	(String[] args) throws LifecycleException, ServletException, IOException {

		if (JAVA_VERSION < 1.7){
			System.err.println("Your Java version is  " + System.getProperty("java.version") + ". You need at least version 1.8. Please download the latest version from http://www.java.com.");
		    InputStreamReader isr = new InputStreamReader(System.in);
		    BufferedReader br = new BufferedReader(isr);
		    System.out.println("\n Please press ENTER to exit the program.");
		    
				br.readLine();
		    
			System.exit(1);
		}
		
		String currentDir = new File(".").getCanonicalPath();
		
		
		File XMLfile = new File(currentDir+File.separator +"config.xml");
		Configuration configuration = null;
		if (!XMLfile.exists()){
		configuration = Configuration.getDefaultConfiguration();
		try {
		configuration.writeToFile(XMLfile);
		} catch (JAXBException e) {
						e.printStackTrace();
		}		  

				} else {
			try {
				configuration = Configuration.readConfigFile(XMLfile);
			} catch (Exception e) {
System.out.println("an error eccured when reading " + XMLfile.getAbsolutePath() + ". Continuing with standard configuration.");
configuration = Configuration.getDefaultConfiguration();
			}
			
		}
		

					
		
		
		Tomcat tomcat = new Tomcat();
		
		int port = 8080;
		try{
		port = Integer.parseInt(configuration.getPort());
	} catch (Exception e){}		
		
		tomcat.setPort(port);

		Context context = tomcat.addContext(CONTEXT_PATH, currentDir);
		context.addParameter("debugLevel", configuration.getUCHDebuggLevel());
		((StandardContext)context).addApplicationListener("edu.wisc.trace.uch.UchContextListener"); 
		((StandardContext)context).addWelcomeFile("redirect.html");
		
		
		ServletContext servletContext = ((StandardContext)context).getServletContext();
		
		addMimeMapping((StandardContext)context);
		
		createDefaultServlet(context,configuration);
//		initUchServlet(tomcat,context,configuration);
		initServlets(tomcat);
		mapServlet(context, UCH_SERVLET, uchUrlPatterns);
		mapServlet(context, UCH_CONFIG_SERVLET, uchConfigUrlPatterns);
		mapServlet(context, USER_CONTEXT_SERVLET, userContextUrlPatterns);
		mapServlet(context, DEFAULT_SERVLET, uchDefaultServlet);
		

		ServerConsole serverConsole = new ServerConsole(tomcat);	

		try {
			tomcat.start();
			serverConsole.start();
		} catch(Exception e){
		   	tomcat.stop();
	   	}
		online = true;
		tomcat.getServer().await();
	}
	
	/**  
	 * initialize Servlets and add them to Tomcat
	 * 
	 */
	private static void initServlets (Tomcat tomcat){
		tomcat.addServlet(CONTEXT_PATH,UCH_SERVLET, new UchServlet());
		tomcat.addServlet(CONTEXT_PATH,UCH_CONFIG_SERVLET, new UchConfigServlet());
		tomcat.addServlet(CONTEXT_PATH,USER_CONTEXT_SERVLET, new UserContextServlet());

		}
	
	/**  
	 * Map Urls to appropriate Servlets
	 * 
	 */
	private static void mapServlet(Context context, String servletName, String[] urlPatterns){
		for (String urlPattern : urlPatterns) {
			
			context.addServletMapping(urlPattern, servletName);
		}
	}
	
	/**  
	 * Define DefaultServlet to enable serving static content
	 * 
	 */
	private static void createDefaultServlet(Context context,Configuration configuration){
		Wrapper defaultServlet = context.createWrapper();
		defaultServlet.setName("default");
		defaultServlet.setServletClass("org.apache.catalina.servlets.DefaultServlet");
		defaultServlet.addInitParameter("debug", "0");
		defaultServlet.addInitParameter("listings", "false");
		defaultServlet.addInitParameter("INIT_PARAMETER_DEBUG_LEVEL",configuration.getUCHDebuggLevel() );
		defaultServlet.setLoadOnStartup(1);
		context.addChild(defaultServlet);
	}
	
	/**  
	 * Add Mime-Types to used file-extensions
	 * 
	 */
	private static void addMimeMapping(Context context){
		((StandardContext)context).addMimeMapping("td", "application/urc-targetdesc+xml");
		((StandardContext)context).addMimeMapping("uis","application/urc-uisocketdesc+xml");
		((StandardContext)context).addMimeMapping("pret", "application/urc-pret+xml");
		((StandardContext)context).addMimeMapping("rsheet", "application/urc-ressheet+xml");
		((StandardContext)context).addMimeMapping("rdir", "application/urc-resdir+xml");
	}
	
	  
	
		

	static double getVersion () {
	    String version = System.getProperty("java.version");
	    int pos = version.indexOf('.');
	    pos = version.indexOf('.', pos+1);
	    return Double.parseDouble (version.substring (0, pos));
	}

public static void initUchServlet(Tomcat tomcat, Context context, Configuration configuration){
	Wrapper uchServletWrapper = context.createWrapper();
	uchServletWrapper.setName(UCH_SERVLET);
		uchServletWrapper.setServlet(new UchServlet());


	uchServletWrapper.addInitParameter("debug", "0");
	uchServletWrapper.addInitParameter("listings", "false");
	uchServletWrapper.addInitParameter("INIT_PARAMETER_DEBUG_LEVEL",configuration.getUCHDebuggLevel() );
	uchServletWrapper.setLoadOnStartup(1);
	context.addChild(uchServletWrapper);
//	tomcat.addServlet(CONTEXT_PATH,UCH_S		ERVLET, new UchServlet());	
	
}
}
