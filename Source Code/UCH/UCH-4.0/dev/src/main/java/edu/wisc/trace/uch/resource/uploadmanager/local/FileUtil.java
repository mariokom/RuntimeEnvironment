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
package edu.wisc.trace.uch.resource.uploadmanager.local;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Provide methods to create and copy files and directory.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class FileUtil {

	private static Logger logger = LoggerUtil.getSdkLogger();
	
	private static final String CONSTANT_NAME_RESOURCE = "resource";
	
	private static final int MAX_RESOURCES = 1000;
	
	/**
	 * Create a unique directory in specified directory.
	 * 
	 * @param parentDir an object of File specifies parent directory
	 * 
	 * @return a String value specifies directory URI
	 */
	static String createDirForResource(String parentDirURI) {
		
		File parentDir = null;
		
		try {
			parentDir = new File( new URI(parentDirURI) );
		} catch (URISyntaxException e) {
			logger.warning("URISyntaxException : Invalid File URI '"+parentDirURI+"'.");
			return null;
		}
		
		if ( parentDir == null ) {
			logger.warning("Parent Directory is null.");
			return null;
		}
		
		if ( !parentDir.exists() || !parentDir.isDirectory() ) {
			logger.warning("'"+parentDir.getAbsolutePath()+"' is not exists or not a directory.");
			return null;
		}
		
		int count = 0;
		String resourceDirName = CONSTANT_NAME_RESOURCE + "_" + (++count);
		
		try {
			
			File resDir = new File( new URI(parentDirURI + "/" + resourceDirName) );
			
			while ( resDir.exists() ) {
				
				resourceDirName = CONSTANT_NAME_RESOURCE + "_" + (++count);
				resDir = new File( new URI(parentDirURI + "/" + resourceDirName) );
				
				if ( count > MAX_RESOURCES ) {
					
					return null;
				}
			}

			if ( resDir.mkdir() ) {
				return parentDirURI + "/" + resourceDirName;
			} else {
				return null;
			}
			
		} catch(URISyntaxException e) {
			logger.warning("URISyntaxException : Invalid File URI '"+parentDirURI + "/" + resourceDirName+"'.");
			return null;
		}
		
	}
	
	/**
	 * Copy specified file.
	 * 
	 * @param src an object of file specifies source file
	 * @param dest an object of file specifies destination file
	 * 
	 * @return a boolean value specifies whether file copied successfully or not.
	 */
	static boolean copyFile( File src, File dest ) {
		
		if ( src == null ) {
			logger.warning("Source File is null.");
			return false;
		}
		
		if ( dest == null ) {
			logger.warning("Destination File is null.");
			return false;
		}
		
		if ( !src.exists() || !src.isFile() ) {
			logger.warning("'"+src.getAbsolutePath()+"' is not exists or not a file.");
			return false;
		}
		
		try {
			
			FileReader fr = new FileReader(src);
			FileWriter fw = new FileWriter(dest);
			
			int ch = -1;
			
			while ( ( ch = fr.read() ) != -1 )
				fw.write(ch);
		
			fw.flush();
			fw.close();
			fr.close();
			
			logger.info("Copy file from '"+src.getAbsolutePath()+"' to '"+dest.getAbsolutePath()+"' successfully.");
			
			return true;
			
		} catch (FileNotFoundException e) {
			logger.warning("FileNotFoundException : Unable to copy file '"+src.getAbsolutePath()+"' to file '"+dest.getAbsolutePath()+"'.");
			return false;
		} catch (IOException e) {
			logger.warning("IOException : Unable to copy file '"+src.getAbsolutePath()+"' to file '"+dest.getAbsolutePath()+"'.");
			return false;
		}
		
	}
	
	/*
	// Just for testing
	public static void main(String[] args) {
		
		//logger.info(createDirForResource("file:///D:/documents/test" ));
		//copyFile(new File("D:/documents/change log.txt"), new File("D:/documents/change log new.txt") );
	}
	*/
}
