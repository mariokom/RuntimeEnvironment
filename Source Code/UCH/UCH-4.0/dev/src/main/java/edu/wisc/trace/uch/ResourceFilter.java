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
import java.util.Map;
import java.util.Set;

/**
 * Provide method to filter specific kind of resources.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class ResourceFilter {

	/**
	 * Filter specific kind of resources from specified list.
	 * If property Map contains the property 'http://openurc.org/ns/res#type' and its value 'http://openurc.org/TR/uch/#userprofile' then remove that map from list.
	 * 
	 * @param retResPropsList an object of List&lt;List&lt;Map&lt;String, List&lt;String&gt;&gt;&gt;&gt;
	 */
	void filter(List<List<Map<String, List<String>>>> retResPropsList) {
		
		if ( retResPropsList == null ) 
			return;
		
		for ( List<Map<String, List<String>>> resPropsList : retResPropsList ) {
			
			if ( resPropsList == null )
				continue;
			
			List<Map<String, List<String>>> resPropsToRemove = new ArrayList<Map<String,List<String>>>();
		
			for ( Map<String, List<String>> resProps : resPropsList ) {
				
				if ( resProps == null )
					continue;
				
				if ( containsUserProfile(resProps) ) 
					resPropsToRemove.add(resProps);
			}
			
			if ( resPropsToRemove.size() > 0 ) {
				
				for ( Map<String, List<String>> remResProps : resPropsToRemove ) 
					resPropsList.remove(remResProps);		
			}
			
		}
	}
	
	/**
	 * Check whether map contain the property 'http://openurc.org/ns/res#type' and its value 'http://openurc.org/TR/uch/#userprofile'.
	 * 
	 * @param map an object of Map&lt;String, List&lt;String&gt;&gt;
	 * 
	 * @return a boolean value specifies whether map contain the property 'http://openurc.org/ns/res#type' and its value 'http://openurc.org/TR/uch/#userprofile'.
	 */
	boolean containsUserProfile(Map<String, List<String>> map) {
		
		if ( map == null )
			return false;
		
		List<String> resTypes = map.get(Constants.PROPERTY_RES_TYPE);
		
		if ( resTypes == null )
			return false;
		
		if ( resTypes.contains(Constants.PROPERTY_RES_TYPE_VALUE_USER_PROFILE) )
			return true;
		else
			return false;
	}
}
