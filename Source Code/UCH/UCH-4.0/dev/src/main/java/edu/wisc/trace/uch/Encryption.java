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

import java.util.ArrayList;
import java.util.List;

/**
 * It provide custom encryption algorithm.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class Encryption {

	/**
	 * Encrypt the data.
	 * 
	 * @param data a String value of data
	 * 
	 * @return a String value of encrypted data
	 */
	String encrypt(String data) {
		
		if ( data == null )
			return null;
		
		return convertToHex( insertExtraInfo( convertToList(data) ) );
		
	}
	
	/*
	 Steps ::
		(1)	Convert every later into 3 digit int value and separate it with 1 to 5 digit(s) random generated number and back to 1 until data ends.
		(2)	Now take 2 digit pair and convert it into hex string. If total string length is odd then append a 0 at the end.

	Example ::

		Data = plenar technologies
		
		Step1 = 112410825101347110862009714983114417003240911629101509993104731110714011150723108373211161910372105210146115
		
		Step2 = 0B180A52330122470A5614094731530B2C110020285B101D0A0F09631F04490B0A47280B0F07170A5349150B3D5B03480A340A0E3D0F

	 */
	/**
	 * Convert the integer value string into hex value string.
	 * 
	 * 
	 * @param data a String value of data
	 * 
	 * @return a String value
	 */
	private String convertToHex(String data) {
		
		if ( data.length() % 2 != 0 )
			data += "0";
		
		int halfLength = data.length() / 2;
		
		StringBuilder sb = new StringBuilder();
		
		for ( int i=0 ; i<halfLength ; i++ ) 
			sb.append( formatString( String.valueOf( Integer.toHexString( Integer.valueOf(data.substring(i*2, i*2+2) ) ) ), 2 ) );
		
		return sb.toString().toUpperCase();
	}
	
	/**
	 * Insert extra information between values of the list and form a String from it and return it.
	 * 
	 * @param values an Object of List&lt;String&gt;
	 * @return a String value
	 */
	private String insertExtraInfo( List<String> values ) {
		
		boolean incOrder = true;
		int append = 1;
		
		int length = values.size();
		
		StringBuilder sb = new StringBuilder();
		
		for ( int i=0 ; i<length ; i++ ) {
			
			sb.append( values.get(i) );
			
			if ( i != (length-1) ) {
				
				sb.append(generateRandomNumberString(append) );
				
				if ( incOrder ) {
					
					append++;
					
					if ( append == 5 )
						incOrder = false;
					
				} else {
					
					append--;
					
					if ( append == 1 )
						incOrder = true;
				}
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Generate a random number which has maximum length as the specified length.
	 * 
	 * @param length an int value of length
	 * 
	 * @return a String value
	 */
	private String generateRandomNumberString(int length) {
	
		String randomNumber = String.valueOf( (long)( Math.random()*(10^length) ) );
	
		int numLength = randomNumber.length();
		
		String value = null;
		
		if ( numLength == length )
			value = randomNumber;
		else if ( length < numLength ) 
			value = randomNumber.substring(0, length);
		else if ( (length - numLength) == 1 )
			value = "0" + randomNumber;
		else if ( (length - numLength) == 2 )
			value = "00" + randomNumber;
		else if ( (length - numLength) == 3 )
			value = "000" + randomNumber;
		else if ( (length - numLength) == 4 )
			value = "0000" + randomNumber;
		
		
		//logger.info(randomNumber+"   "+value+"   "+numLength+"   "+length);
		
		return value;
	}
	
	/**
	 * Divide the data String in 3 letter substring and add them in a map and return it.
	 * 
	 * @param data a String value of data
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	private List<String> convertToList(String data) {
		
		if ( data == null )
			return null;
		
		List<String> values = new ArrayList<String>();
		
		int length = data.length();
		
		for ( int i=0 ; i<length ; i++ ) 
			values.add( formatString( Integer.valueOf( data.charAt(i) ).toString(), 3 ) );
		
		return values;
	}
	
	/**
	 * convert the length of value as adding 0 one or more times as prefix to the value and return it.
	 * 
	 * @param value a String value
	 * @param maxLength an int value of max length
	 * 
	 * @return a String value
	 */
	private String formatString(String value, int maxLength) {
		
		if ( value == null )
			return null;
		
		int length = value.length();
		
		String retVal = null;
		
		if ( length == maxLength )
			retVal = value;
		else if ( (maxLength - length) == 1 )
			retVal = "0" + value;
		else if ( (maxLength - length) == 2 )
			retVal = "00" + value;
		else 
			retVal = null;
		
		//logger.info(value+"   "+retVal+"   "+length+"   "+maxLength);
		return retVal;
	}
	
	
	// Just for testing
	public static void main(String[] args) {
		
		//String data = "action=adduserinfo&userName=Sandip&password=sandip&email=sandip.patel@plenartech.com&address=ahm&publisherURL=http://sandip.com";
		String data = "";
		System.out.println( new Encryption().encrypt(data) );
		
	}
}
