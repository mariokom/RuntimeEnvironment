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
 
/** 
 * Defines Constant values.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center 
 * @version $Revision: 798 $ 
 */ 
public final class Constants {
 
	// Used in Local files
	static final String CONSTANT_UCH_CONFIG_FILE_NAME = "uch.config";
	static final String CONTEXT_PATH_DIR_NAME_WEB_INF = "WEB-INF/";
	
	public static final String CONSTANT_UCH = "uch";
	
	public static final String CONSTANT_SESSION_ID_PREFIX_SESSION = "session";
	public static final String OPEN_SESSION_RESPONSE_PROP_NAME_RESULT = "result";
	public static final String OPEN_SESSION_RESPONSE_PROP_NAME_SESSION_ID = "sessionId";
	public static final String OPEN_SESSION_RESPONSE_PROP_NAME_RESULT_VALUE_ACCEPT = "A";
	public static final String OPEN_SESSION_RESPONSE_PROP_NAME_RESULT_VALUE_REJECT = "R";
	public static final String OPEN_SESSION_RESPONSE_PROP_NAME_RESULT_VALUE_FAILED = "F";
	
	static final String CONSTANT_VALUE_CONTENT_AT = "contentAt";
	
	 // web.xml context parameter names
	static final String CONTEXT_INIT_PARAM_NAME_IP_ADDRESS = "ipAddress";
	static final String CONTEXT_INIT_PARAM_NAME_PORT_NO = "portNo";
	static final String CONTEXT_INIT_PARAM_NAME_RESSERVER_USER_NAME = "resserver.username";
	static final String CONTEXT_INIT_PARAM_NAME_RESSERVER_PASSWORD = "resserver.password";
	static final String CONTEXT_INIT_PARAM_NAME_RESSERVER_APP_PATH = "resserver.appPath";
	 
	static final String REQUEST_PARAM_NAME_RESSERVER_USERNAME = "username";
	static final String REQUEST_PARAM_NAME_RESSERVER_PASSWORD = "password";
	
	static final String CLASS_PATH_VALUE_CLASS_LOADER = "edu.wisc.trace.uch.ClassLoader";
	static final String RESSERVER_USER_STATUS_GUEST = "guest";
	
	 // UCH Property Names
	 public static final String UCH_PROP_DOC_ROOT = "docRoot";
	 public static final String UCH_PROP_UCH_INSTANCE_ID = "uchInstanceId";
	 
	 
	 // Properties
	 public static final String PROPERTY_RES_NAME = "http://openurc.org/ns/res#name";
	 public static final String PROPERTY_RES_TYPE = "http://openurc.org/ns/res#type";
	 public static final String PROPERTY_RES_SUB_TYPE = "http://openurc.org/ns/res#subtype";
	 public static final String PROPERTY_RES_ROLE = "http://openurc.org/ns/res#role";
	 public static final String PROPERTY_RES_FOR_LANG = "http://openurc.org/ns/res#forLang";
	 public static final String PROPERTY_RES_MANUFACTURER = "http://openurc.org/ns/res#manufacturer";
	 public static final String PROPERTY_RES_TARGET_MODEL_NAME = "http://openurc.org/ns/res#targetModelName";
	 public static final String PROPERTY_RES_DEVICE_MODEL_NUMBER = "http://openurc.org/ns/res#deviceModelNumber";
	 public static final String PROPERTY_RES_DYNAMIC_LIB_CLASS = "http://openurc.org/ns/res#dynamicLibClass";
	 public static final String PROPERTY_RES_UCH_INSTANCE_ID = "http://openurc.org/ns/res#uchInstanceId";
	 public static final String PROPERTY_RES_TARGET_FRIENDLY_NAME = "http://openurc.org/ns/res#targetFriendlyName";
	 public static final String PROPERTY_RES_INSTANCE_ID = "http://openurc.org/ns/res#instanceId";
	 
	 /*
	  * 2010-12-30 : Paresh Kansara
	  * Change Reason :	To identify basic resource icons  
	  */
	 public static final String PROPERTY_RES_ICON_RESOURCE = "http://openurc.org/ns/res#iconResource";
	 public static final String PROPERTY_RES_ICON_INTERNAL = "http://openurc.org/ns/res#iconInternal";
	 
	 
	 //public static final String PROPERTY_RES_PROTOCOL_INFO_CAP_PROFILE = "http://openurc.org/ns/res/protocolInfo#capProfile";
	 public static final String PROPERTY_RES_UIPM_URC_HTTP_GET_COMPATIBLE_UIS_INTERVAL = "http://res.openurc.org/uipm/urc-http#getCompatibleUIsInteval";
	 
	 public static final String PROPERTY_DEVICE_TYPE = "http://openurc.org/ns/res#deviceType";
	 public static final String PROPERTY_PLATFORM = "http://openurc.org/ns/res#platform";
	 public static final String PROPERTY_DEVICE_PLATFORM = "http://openurc.org/ns/res#devicePlatform";
	 public static final String PROPERTY_FOR_TARGET_NAME = "http://openurc.org/ns/res#forTargetName";
	 public static final String PROPERTY_FOR_SOCKET_NAME = "http://openurc.org/ns/res#forSocketName";
	 public static final String PROPERTY_MIME_TYPE = "http://openurc.org/ns/res#mimeType";
	 public static final String PROPERTY_A_RRES_DESC_FOR_DOMAIN = "http://openurc.org/ns/res#aResDescForDomain";
	 public static final String PROPERTY_PROTOCOL_SHORT_NAME = "http://openurc.org/ns/res#protocolShortName";
	 public static final String PROPERTY_WRAPS_PROTOCOL_SHORT_NAME = "http://openurc.org/ns/res#wrapsProtocolShortName";
	 public static final String PROPERTY_INDEX_FILE = "http://openurc.org/ns/res#indexFile";
	 public static final String PROPERTY_RESOURCE_URI = "http://openurc.org/ns/res#resourceUri";
	 public static final String PROPERTY_WIDTH = "http://openurc.org/ns/res#width";
	 public static final String PROPERTY_HEIGHT = "http://openurc.org/ns/res#height";
	 public static final String PROPERTY_DEPTH = "http://openurc.org/ns/res#depth";
	 
	 public static final String PROPERTY_CONFORMS_TO = "http://purl.org/dc/terms/conformsTo";
	 public static final String PROPERTY_DC_TERMS_MODIFIED = "http://purl.org/dc/terms/modified";
	 public static final String PROPERTY_DC_ELEMENTS_TYPE = "http://purl.org/dc/elements/1.1/type";
	 public static final String PROPERTY_DC_ELEMENTS_TITLE = "http://purl.org/dc/elements/1.1/title";
	 public static final String PROPERTY_DC_ELEMENTS_CREATOR = "http://purl.org/dc/elements/1.1/creator";
	 public static final String PROPERTY_DC_ELEMENTS_PUBLISHER = "http://purl.org/dc/elements/1.1/publisher";
	 public static final String PROPERTY_DC_ELEMENTS_DESCRIPTION = "http://purl.org/dc/elements/1.1/description";
	 
	 public static final String LOCAL_RESOURC_NAME_PREFIX = "http://res.openurc.org/local/resource#";
	 
	 public static final String PROPERTY_VALUE_STAR = "*";
	 
	//value of PROPERTY_RES_INSTANCE_ID
	 public static final String PROPERTY_RES_INSTANCE_ID_VALUE_ALL = "all";
	 
	 //value of PROPERTY_PLATFORM
	 public static final String PROPERTY_PLATFORM_VALUE_JAVA = "java";
	 
	 
	 //value of PROPERTY_RES_TYPE
	 public static final String PROPERTY_RES_TYPE_VALUE_TDM = "http://openurc.org/restypes#tdm";
	 public static final String PROPERTY_RES_TYPE_VALUE_TA = "http://openurc.org/restypes#ta";
	 public static final String PROPERTY_RES_TYPE_VALUE_UIPM = "http://openurc.org/restypes#uipm";
	 
	 public static final String PROPERTY_RES_TYPE_VALUE_TD = "http://openurc.org/restypes#targetdesc";
	 public static final String PROPERTY_RES_TYPE_VALUE_UIS = "http://openurc.org/restypes#uisocketdesc";
	 public static final String PROPERTY_RES_TYPE_VALUE_RESSHEET = "http://openurc.org/restypes#ressheet";
	 
	 public static final String PROPERTY_RES_TYPE_VALUE_TARGET = "http://openurc.org/TR/uch/#target";
	 
	 public static final String PROPERTY_RES_TYPE_VALUE_UIPM_CLIENT = "http://openurc.org/restypes#uipm-client";
	 
	 //public static final String PROPERTY_RES_TYPE_VALUE_URCHTTP_UIPM_CLIENT = "http://openurc.org/restypes#urchttp-uipm-client";
	 
	 public static final String PROPERTY_RES_TYPE_VALUE_CONFIG_FILE = "http://openurc.org/restypes#configfile";
	 
	 public static final String PROPERTY_RES_TYPE_VALUE_ATOMIC = "http://openurc.org/restypes#atomic";
	 
	 public static final String PROPERTY_RES_TYPE_VALUE_USER_PROFILE = "http://openurc.org/TR/uch/#userprofile";
	 
	 //value of PROPERTY_RES_SUB_TYPE
	 public static final String PROPERTY_RES_SUB_TYPE_VALUE_TARGETDESC_TEMPLATE = "http://openurc.org/restypes#targetdesc-template";
	 
	 // 2012-09-14 : new value for 'conformsTo' element is added below for TD, UIS, RSheet. 
	 public static final String PROPERTY_CONFORMS_TO_VALUE_TD = "http://openurc.org/ns/targetdesc-2/isoiec24752-4-2013";
	 											//"http://openurc.org/iso24752-4/2007";
	 
	 public static final String PROPERTY_CONFORMS_TO_VALUE_UIS = "http://openurc.org/ns/uisocketdesc-2/isoiec24752-2-2013"; 
			 									// "http://openurc.org/iso24752-2/2007";
	 
	 public static final String PROPERTY_CONFORMS_TO_VALUE_RESSHEET = "http://openurc.org/ns/rsheet-2/isoiec24752-5-2013"; 
	 											//"http://openurc.org/ns/res/isoiec24752-5-2013"; 
			 									// "http://openurc.org/iso24752-5/2007";
	 
	 //value of PROPERTY_MIME_TYPE
	 public static final String PROPERTY_MIME_TYPE_VALUE_TD = "application/urc-targetdesc+xml";
	 public static final String PROPERTY_MIME_TYPE_VALUE_UIS = "application/urc-uisocketdesc+xml";
	 public static final String PROPERTY_MIME_TYPE_VALUE_RSHEET = "application/urc-ressheet+xml"; 
	 public static final String PROPERTY_MIME_TYPE_VALUE_JAVA_ARCHIVE = "application/java-archive";
	 public static final String PROPERTY_MIME_TYPE_VALUE_ZIP = "application/zip";
	 
	 
	 //value of PROPERTY_PROTOCOL_SHORT_NAME
	 public static final String PROPERTY_PROTOCOL_SHORT_NAME_VALUE_HTTP_HTML = "HTTP/HTML";
	
	 // Resource Server Resources properties
	 public static String RESOURCE_QUERY_PROP_NAME_START = "start";
	 public static String RESOURCE_QUERY_PROP_NAME_COUNT = "count";
	 public static String RESOURCE_QUERY_PROP_NAME_REFERENCEID = "referenceId";
	 public static String RESOURCE_QUERY_PROP_NAME_TOTAL = "total";
	 public static String RESOURCE_QUERY_PROP_NAME_INDEX = "index";
	 public static String RESOURCE_QUERY_PROP_NAME_GET_CONTENT = "getContent";
	 public static String RESOURCE_QUERY_PROP_NAME_RESOURCE_LOCATION = "resourceLocation";
	 public static String RESOURCE_QUERY_PROP_NAME_RESOURCE_LOCATION_VALUE_ALL = "all";
	 public static String RESOURCE_QUERY_PROP_NAME_RESOURCE_LOCATION_VALUE_LOCAL = "local";
	 public static String RESOURCE_QUERY_PROP_NAME_RESOURCE_LOCATION_VALUE_RES_SERVER = "resServer";
	 
	 // Resource upload properties
	 public static String RESOURCE_UPLOAD_PROP_NAME_LOCATION = "location";
	 public static String RESOURCE_UPLOAD_PROP_NAME_LOCATION_VALUE_LOCAL = "local";
	 public static String RESOURCE_UPLOAD_PROP_NAME_LOCATION_VALUE_RESOURCE_SERVER = "resourceserver";
	 
	 //TargetId default value 
	 public static final String TARGET_ID_DEFAULT_VALUE = "All";
	 
	 //Resource Sheet default values.//
	 public static final String RSHEET_TYPE_DEFAULT_VALUE = "Text";
	 public static final String RSHEET_FOR_LANG_DEFAULT_VALUE = "en";
	 public static final String PROPERTY_DC_ELEMENTS_TYPE_VALUE_TEXT = "Text";
	 public static final String PROPERTY_RES_FOR_LANG_VALUE_EN = "en";
	 
	 //Locally used Properties.//
	 public static String PROP_NAME_GLOBAL_AT = "globalAt";
	 public static String PROP_NAME_RESOURCE_LOCAL_AT = "resourceLocalAt";
	 public static String PROP_GET_CONTENT = "getContent";
	 public static String PROP_UIPM_CLIENT_NAME = "uipmClientName";
	 public static String PROP_UIPM_CLIENT_FRIENDLY_NAME = "uipmClientName@friendlyName";
	 public static String PROP_UIPM_CLIENT_DESCRIPTION = "uipmClientName@desc";
	 public static String PROP_RESOURCE_DATA = "resourceData";
	 //UI props
	 //public static final String UI_LIST_PROPERTY_NAME_CAP_PROFILE = "capProfile";
	 
	 
	 //Resource Sheet Property Names
	 public static final String RSHEET_PROPERTY_ROLE_VALUE_LABEL = "http://openurc.org/ns/res#label";
	 public static final String RSHEET_PROPERTY_ROLE_VALUE_HELP_PURPOSE = "http://openurc.org/ns/res#help-purpose";
	 
	 // Profile Types
	 public static final String PROFILE_USER = "user";
	 public static final String PROFILE_CONTROLLER = "controller";
	 
	 
	 // Other Props
	 public static final String CONSTANTS_QUERY_PARAMS = "queryParams";
	 
	 public static final String TARGET_PROPS_TD_URI = "http://openurc.org/ns/res#tdUri";
}
