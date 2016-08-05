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

package edu.wisc.trace.uch.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Enumeration;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
/**
 * Provide Method to unzip zip file.
 *  
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class ZipUtilities {

	private static Logger logger = LoggerUtil.getSdkLogger();
	
	/**
	 * Default Constructor.
	 */
	public ZipUtilities() {

	}

	/**
	 * Extract zip file to specified directory.
	 * 
	 * @param zipFileUri an URI representing Zip File Path
	 * @param outDirUri an URI representing Output Directory Path
	 * @return whether file extract successfully or not. 
	 */
	public static boolean unzip(URI zipFileUri, URI outDirUri) {

		if (zipFileUri == null) {
			logger.warning("Zip File URI is null.");
			return false;
		}

		if (outDirUri == null) {
			logger.warning("Output Directory URI is null.");
			return false;
		}
		
		return unzip( new File(zipFileUri) ,  new File(outDirUri) );
	}
	
	/**
	 * Unzip zip file to specified directory.
	 * 
	 * @param zipFilePath a String value of Zip File Path
	 * @param outDirPath a String value of Output Directory Path
	 * @return whether file unzipped successfully or not. 
	 */
	public static boolean unzip(String zipFilePath, String outDirPath) {
    	
    	//System.out.println("Going to unzip file : "+zipFilePath);
    	
    	if ( zipFilePath == null ) {
    		logger.warning("Zip File Path is null.");
    		return false;
    	}
    	
    	if ( outDirPath == null ) {
    		logger.warning("Output Directory Path is null.");
    		return false;
    	}
    	
    	return unzip( new File(zipFilePath) , new File(outDirPath) );
    }
	
	/**
	 * Extract zip file to specified directory.
	 * 
	 * @param file an Object of File representing Zip File
	 * @param outDir an Object of File representing Output Directory
	 * 
	 * @return whether file extract successfully or not. 
	 */
    @SuppressWarnings("unchecked")
	public static boolean unzip(File file, File outDir) {
		
		if ( file == null ) {
			logger.warning("File is null.");
			return false;
		}
		
		if ( outDir == null ) {
			logger.warning("Output directory is null.");
			return false;
		}
		if ( !outDir.exists() ) {

			if ( !outDir.mkdirs() ) { // Create output Directory if not exists. // Parikshit Thakur. Resolved issue in unzipping zipped resource.
				logger.warning("Unable to create new directory '"+ outDir.getAbsolutePath() + "'.");
				return false;
			}
		}

		try {
			
			ZipFile zipFile = new ZipFile(file);
			
			if ( zipFile == null ) {
				return false;
			}

			Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipFile.entries();

			while ( entries.hasMoreElements() ) {
				
				ZipEntry entry = (ZipEntry) entries.nextElement();
				
				if ( entry.isDirectory() ) {

					File newDir = new File(outDir, entry.getName());

					if ( newDir.exists() ) {

						// Delete Existing File/Directory

						if ( newDir.isDirectory() ) {

							if ( !deleteDir(newDir) ) {
								logger.warning("Unable to delete existing Directory '"+ newDir.getAbsolutePath() + "'.");
								return false;
							}

						} else {

							if ( !deleteFile(newDir) ) {
								logger.warning("Unable to delete existing File '"+ newDir.getAbsolutePath() + "'.");
								return false;
							}
						}

					}

					if ( !newDir.mkdirs() ) { // Parikshit Thakur. Resolved issue in unzipping zipped resource. 
						logger.warning("Unable to create new directory '"+ newDir.getAbsolutePath() + "'.");
						return false;
					}

					continue;
				}
				
				String parentEntry = getParentPath( entry.getName() );
				// create if parent directory does not exists. 
				if ( parentEntry != null ) {
					
					File parentDir = new File(outDir, parentEntry);
					
					if ( !parentDir.exists() ) {
						
						if ( !parentDir.mkdirs() ) {
							
							logger.warning("Unable to create directory '"+parentDir.getAbsolutePath()+"'.");
							return false;
						}
					}
						
				}
				
				File newFile = new File(outDir, entry.getName());

				if ( newFile.exists() ) {

					if ( !deleteFile(newFile) ) {
						logger.warning("Unable to delete existing file '"+ newFile.getAbsolutePath() + "'.");
						return false;
					}
				}
				
				if ( !newFile.createNewFile() ) {

					logger.warning("Unable to create new file '" + newFile.getAbsolutePath() + "'.");
					return false;
				}
				
				copyInputStream(zipFile.getInputStream(entry),
						new BufferedOutputStream(new FileOutputStream(newFile)));
				
			}

		} catch (IOException ioe) {

			logger.warning("IOException: Unable to unzip file '" + file.getAbsolutePath() + "'.");
			return false;
			
		} catch (Exception e) {

			logger.warning("Exception: Unable to unzip file '" + file.getAbsolutePath() + "'.");
			return false;
		}

		return true;
	}

	/**
	 * Get path of parent directory of specifies path.
	 * 
	 * @param path a String value that specifies file or directory path
	 * 
	 * @return a String value that specifies parent directory path of specified file or directory path
	 */
	private static String getParentPath(String path) {
		
		if ( path == null ) {
			logger.warning("Path is null.");
			return null;
		}
		
		int lastSlashIndex = path.lastIndexOf('/');
		int lastBackSlashIndex = path.lastIndexOf('\\');
		
		if ( lastSlashIndex == -1 && lastBackSlashIndex == -1 ) {
			logger.warning("'"+path+"' don't have parent dir path.");
			return null;
		}
		
		if ( lastSlashIndex > lastBackSlashIndex )
			return path.substring(0, lastSlashIndex);
		else
			return path.substring(0, lastBackSlashIndex);
	}
	
    /**
     * Copy the data from inputStream to outputStream.
     * 
     * @param in an Object of InputStream
     * @param out an Object of OutputStream
     * 
     * @throws IOException
     * @see java.io.InputStream
     * @see java.io.OutputStream
     */
    private static final void copyInputStream(InputStream in, OutputStream out)
    							throws IOException {
    	
		  byte[] buffer = new byte[1024];
		  int len;
		
		  while((len = in.read(buffer)) >= 0)
		    out.write(buffer, 0, len);
		
		  in.close();
		  out.close();
	}
    
    /**
     * Delete the specified directory.
     * 
     * @param dir an Object of File
     * @return whether dir deleted successfully 
     */
    private static boolean deleteDir(File dir) {
    	
    	if ( dir == null )
    		return true;
    	
    	if ( !dir.exists() )
    		return true;
    	
    	 File files[] = dir.listFiles();
    	 
    	 for ( File file : files ) {
    		 
    		 if ( file.isDirectory() ) {
    			 
    			 if( !deleteDir(file) )
    				 return false;
    			 
    		 } else {
    			 
    			 if( !deleteFile(file) )
    				 return false;
    		 }
    	 }
    	
    	return dir.delete();
    }
    
    /**
     * Delete the specified file.
     * 
     * @param file an Object of File
     * @return whether file deleted successfully 
     */
    private static boolean deleteFile(File file) {
    	
    	return file.delete();
    }
    
    public static void main(String[] args) {
		
    	unzip("D:/temp/test.xml", "D:/temp");
	}
    
}
