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


package edu.wisc.trace.uch.resource.resourcesheetmanager;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.wisc.trace.uch.util.LoggerUtil;


/**
 * Store Resource Sheet Properties like localAt, conformsTo, type, forDomain, forLang and role.
 * Also provide Methods to get/set them and get matched Resource Sheet.
 *  
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
class ResourceSheetProperties {

	private Logger logger = LoggerUtil.getSdkLogger();
	
	private String resSheetUri;
	
	private List<String> conformsToList;
	
	private List<String> typeList;
	
	private List<String> forDomainList;
	
	private List<String> forLangList;

	private List<String> roleList;
	
	/**
	 * Get the String value of Resource Sheet URI.
	 *  
	 * @return a String value of resSheetUri
	 */
	public String getResSheetUri() {
		return resSheetUri;
	}

	/**
	 * Set the String value of Resource Sheet URI.
	 * 
	 * @param resSheetUri a String value of resSheetUri
	 */
	public void setResSheetUri(String resSheetUri) {
		this.resSheetUri = resSheetUri;
	}

	/**
	 * Get the List of conformsTo.
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	public List<String> getConformsToList() {
		return conformsToList;
	}

	/**
	 * Set the List of conformsTo.
	 * 
	 * @param conformsToList an Object of List&lt;String&gt;
	 */
	public void setConformsToList(List<String> conformsToList) {
		this.conformsToList = conformsToList;
	}

	/**
	 * Get the List of type.
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	public List<String> getTypeList() {
		return typeList;
	}

	/**
	 * Set the List of type.
	 * 
	 * @param typeList an Object of List&lt;String&gt;
	 */
	public void setTypeList(List<String> typeList) {
		this.typeList = typeList;
	}

	/**
	 * Get the List of forDomain.
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	public List<String> getAResDescForDomainList() {
		return forDomainList;
	}

	/**
	 * Set the List of forDomain.
	 * 
	 * @param forDomainList an Object of List&lt;String&gt;
	 */
	public void setAResDescForDomainList(List<String> forDomainList) {
		
		this.forDomainList = forDomainList;
	}

	/**
	 * Get the List of forLang.
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	public List<String> getForLangList() {
		return forLangList;
	}

	/**
	 * Set the List of forLang.
	 * 
	 * @param forLangList an Object of List&lt;String&gt;
	 */
	public void setForLangList(List<String> forLangList) {
		this.forLangList = forLangList;
	}

	/**
	 * Get the List of role.
	 * 
	 * @return an Object of List&lt;String&gt;
	 */
	public List<String> getRoleList() {
		return roleList;
	}

	/**
	 * Set the List of role.
	 * 
	 * @param roleList an Object of List&lt;String&gt;
	 */
	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}
	
	
	/**
	 * Return whether Resource Sheet Properties are matched with specified resSheetProps or not.
	 * 
	 * @param resSheetProps an Object of Map&lt;String, String&gt;
	 * 
	 * @return whether Resource Sheet Properties are matched or not.
	 */
	public boolean matched(Map<String, String> resSheetProps) {
		
		//logger.info("resSheetUri="+resSheetUri);
		//logger.info("conformsToList="+conformsToList);
		//logger.info("typeList="+typeList);
		//logger.info("forDomainList="+forDomainList);
		//logger.info("forLangList="+forLangList);
		//logger.info("roleList="+roleList);
		//logger.info("resSheetProps="+resSheetProps);
		
		if ( resSheetProps == null )
			return false;
		
		String forDomain = resSheetProps.get("forDomain");
		
		
		if ( (forDomain == null) || (forDomainList == null) 
				|| !forDomainList.contains(forDomain) ) {
			logger.warning("'forDomain' not matched.");
			return false;
		}
		
		String type = resSheetProps.get("type");
		
		if ( (type == null) || (typeList == null) || !typeList.contains(type) ) {
			logger.warning("'type' not matched.");
		//	logger.info(forDomainList+" "+ forDomain);
		//	logger.info(typeList+" "+ type);
			return false;
		}
		
		String forLang = resSheetProps.get("forLang");
		
		if ( (forLang == null) || (forLangList == null) || !forLangList.contains(forLang) ) {
			logger.warning("'forLang' not matched.");
			return false;
		}
		
		String role = resSheetProps.get("role");
		
		if ( (role == null) || (roleList == null) || (roleList.size() == 0) ) {
			logger.info("Resource Sheet Properties Matched.");
			return true;
		} 
		
		if ( roleList.contains(role) ) {
			logger.info("Resource Sheet Properties Matched.");
			return true;
		}
		
		logger.warning("'role' not matched.");
		return false;
	}
}
