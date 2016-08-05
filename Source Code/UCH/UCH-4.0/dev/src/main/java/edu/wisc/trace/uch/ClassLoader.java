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
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide methods to load java class or library (which are not in classpath) dynamically.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $ Revision: 1.0 $
 */
//Parikshit Thakur : 20110829. Now ClassLoader is a subclass of URLClassLoader. Commented out old code.

class ClassLoader extends URLClassLoader {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	/*private static String FILE_EXTENSION_JAR = ".jar";
	
	private List<URL> urlList = new ArrayList<URL>();
	
	*/ 
		
	
	public ClassLoader(URL[] urls, java.lang.ClassLoader parent) {
		super(urls, parent);
		
	}
	
	
	
	/**
	 * Load all classes/jar of lib directory.
	 *  
	 * @param libDirURI a String value of Library Directory URI 
	 */
	
	/*ClassLoader(String libDirURI) {
		
		if ( libDirURI == null ) {
			logger.severe("Lib Directory URL is null.");
			return;
		}
		
		File libDir = null;
		
		try {
			libDir = new File( new URI(libDirURI) );
		} catch (URISyntaxException e) {
			logger.severe("URISyntaxException : Unable to get Object of File for path '"+libDirURI+"'.");
			return;
		}
		
		addJarFiles(libDirURI, libDir);
	}*/
	
	
	/**
	 * Add jar files from specified directory.
	 * 
	 * @param libDirUri a String value of Lib Directory URI
	 * @param libDir an Object of File specifies Lib Directory
	 */
	/*private void addJarFiles(String libDirUri, File libDir) {
		
		if ( (libDirUri == null) || (libDir == null) || !libDir.isDirectory() )
			return;
		
		File[] jarFiles = libDir.listFiles( new ExtensionFilter(FILE_EXTENSION_JAR) );
		
		if ( (jarFiles == null) || (jarFiles.length == 0) )
			return;
		
		for ( File jarFile : jarFiles ) {
			
			if ( jarFile == null )
				continue;
			
			addJar( (libDirUri + "/" + jarFile.getName()).replaceAll(" ", "%20") );
		}
	}*/
	
	
	/**
	 * Add specified JAR File to ClassLoader.
	 * 
	 * @param jarFileUri a String value of Jar File URI
	 * @return whether the Jar file is added successfully or not.
	 */
	boolean addJar(String jarFileUri) {
		
		URL url = null;
		System.out.println("addJAR");
		try {
			
			url = new URI(jarFileUri.replace(" ", "%20")).toURL();
			addURL(url);
			
			//URL[] urls = {url};
				
					
			/*// Parikshit Thakur : 20110819. Changes to load classes using SystemClassLoader.
			if(url != null && url.toString().indexOf("/WEB-INF/") == -1){
				URLClassLoader sysloader = (URLClassLoader) java.lang.ClassLoader.getSystemClassLoader();
		        Class<?> sysclass = URLClassLoader.class;

		        try {
		            Method method = sysclass.getDeclaredMethod("addURL", new Class[] {URL.class});
		            method.setAccessible(true);
		            method.invoke(sysloader, new Object[] {url});
		        } catch (Throwable t) {
System.out.println("xxx:-3");
		            t.printStackTrace();
		            //throw new IOException("Error, could not add URL to system classloader");
		            logger.info("Error, could not add URL to system classloader");
		        }
			}
			// Changes end.
*/			
		} catch (MalformedURLException e) {
			System.out.println("xxx:-2");
			logger.info("MalformedURLException : Loading jar file '"+jarFileUri+"'.");
			//e.printStackTrace();
		} catch (URISyntaxException e) {
			logger.info("URISyntaxException : Loading jar file '"+jarFileUri+"'.");
			//e.printStackTrace();
		}
		
		if ( url == null ) {
			logger.warning("Unable to add jar file to UrlClassLoader.");
			return false;
		}
		
	//	synchronized (urlList) {
	//		urlList.add(url);
	//	}
	System.out.println("successful");	
		return true;
	}
	
	
	/**
	 * Load the specified class.
	 * 
	 * @param className a String value of Class.
	 * 
	 * @return an Object of class.
	 */
	
	/*Class loadClass(String className){
		
			
		if ( className == null ) {
			logger.warning("Class Name is null.");
			return null;
		}
		
		int size = urlList.size();
		
		if ( size == 0 ) {
			logger.warning("No URL is added.");
			return null;
		}
		
				
		URL[] urls = new URL[size];
		
		for ( int i=0 ; i<size ; i++ ) {
			
			URL url = urlList.get(i);
			
			if ( url == null )
				continue;
			
			urls[i] = url;
			
		}
		
		//Parikshit Thakur : 20110819. Commented. We are going to use systemClassLoader from now onwards to load classes. For that changed code above in addJar method
		//URLClassLoader classLoader = new URLClassLoader(urls, ClassLoader.class.getClassLoader());
		
		
		try {
			return classLoader.loadClass(className);
			//   return java.lang.ClassLoader.getSystemClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			logger.warning("ClassNotFoundException : Unable to get class for '"+className+"'.");
			e.printStackTrace();
			return null;
		}
	}*/
	
	
	/**
	 * Implements FilenameFilter.
	 * Provide implementation of the method accept of FilenameFileter interface.
	 * 
	 * @author Parikshit Thakur & Team, Trace R&D Center
	 * @version $ Revision: 1.0 $
	 */
	/*private class ExtensionFilter implements FilenameFilter {
		
		private String extension;
		 
		*//**
		 * Constructor.
		 * Provide reference of the local variable extension.
		 *  
		 * @param extension a String value of file extension
		 *//*
		public ExtensionFilter( String extension ) {
			
			this.extension = extension;             
		}
		 
		*//**
		 * Check whether specified filename ends with given extension.
		 * 
		 * @param dir an Object of File that specifies a directory
		 * @param name a String value of filename
		 * 
		 * @return a boolean value specifies whether specified file name ends with given extension.
		 *//*
		public boolean accept(File dir, String name) {
			
			return (name.endsWith(extension));
		}
	}
	*/
	/*
	//Just for testing
	public static void main(String[] args) {
		
		String libDir = "file:///D:/software/eclipse-reporting-galileo-win32/webapps/UCH/WEB-INF/lib/";
		
		ClassLoader classLoader = new ClassLoader(libDir);
		
		classLoader.addJar("file:///D:/software/eclipse-reporting-galileo-win32/webapps/UCH/resources/upnptdm/upnptdm.jar");
		
		Class cls = classLoader.loadClass("edu.wisc.trace.uch.tdm.upnp.UpnpTDM");
		
		if ( cls == null ) {
			logger.info("Unable to load class 'edu.wisc.trace.uch.tdm.upnp.UpnpTDM'.");
		} else {
			logger.info("Class '"+cls.getName()+"' is loaded successfully.");
		}
		
		cls = classLoader.loadClass("edu.wisc.trace.uch.tdm.upnp.UpnpTDM");
		
		if ( cls == null ) {
			logger.info("Unable to load class 'edu.wisc.trace.uch.tdm.upnp.UpnpTDM'.");
		} else {
			logger.info("Class '"+cls.getName()+"' is loaded successfully.");
		}
	}
	*/
}
