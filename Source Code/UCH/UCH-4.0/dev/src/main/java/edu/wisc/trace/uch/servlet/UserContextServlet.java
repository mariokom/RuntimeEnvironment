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
package edu.wisc.trace.uch.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.wisc.trace.uch.UCH;
import edu.wisc.trace.uch.action.CloseUserContextAction;
import edu.wisc.trace.uch.action.OpenUserContextAction;
import edu.wisc.trace.uch.util.LoggerUtil;

/** 
 * UserContextServlet accepts request for OpenUserContext and CloseUserContext.
 * Also open and close session using cookies. 
 *  
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class UserContextServlet extends HttpServlet {

	private static final String CONTEXT_PATH_OPEN_USER_CONTEXT = "openUserContext";
	private static final String CONTEXT_PATH_CLOSE_USER_CONTEXT = "closeUserContext";
	private Logger logger = LoggerUtil.getSdkLogger();
	
	/** 
	 * Accepts HTTP GET request for OpenUserContext and CloseUserContext.
	 * Also open and close session using cookies. 
	 * 
	 * @param request an Object of HttpServletRequest
	 * @param response an Object of HttpServletResponse
	 * 
	 * @throws ServletException an object of ServletException
	 * @throws IOException an object of IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext context = this.getServletContext();
		String path = context.getContextPath();
		
		processRequest(request, response);
	}

	/** 
	 * Accepts HTTP POST request for OpenUserContext and CloseUserContext.
	 * Also open and close session using cookies. 
	 * 
	 * @param request an Object of HttpServletRequest
	 * @param response an Object of HttpServletResponse
	 * 
	 * @throws ServletException an object of ServletException
	 * @throws IOException an object of IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		processRequest(request, response);
	}

	/**
	 * Process HTTP GET/POST request.
	 * Accepts request for OpenUserContext and CloseUserContext.
	 * Also open and close session using cookies. 
	 * 
	 * @param request an Object of HttpServletRequest
	 * @param response an Object of HttpServletResponse
	 * 
	 * @throws ServletException an object of ServletException
	 * @throws IOException an object of IOException
	 */
	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		String requestURL = getRequestURL(request);
		
		if ( requestURL == null ) {
			logger.info("Request URL is null.");
			return;
		}
		
		if ( requestURL.indexOf(CONTEXT_PATH_OPEN_USER_CONTEXT) != -1 ) {
			
			Object uch = getServletContext().getAttribute("uch");
			
			if ( uch instanceof UCH )
				new OpenUserContextAction().execute(request, response, (UCH)uch );
			
		} else if ( requestURL.indexOf(CONTEXT_PATH_CLOSE_USER_CONTEXT) != -1 ) {
			
			Object uch = getServletContext().getAttribute("uch");
			
			if ( uch instanceof UCH )
				new CloseUserContextAction().execute(request, response, (UCH)uch );
		}
	}
	
	
	private String getRequestURL(HttpServletRequest request) {
		
		if ( request == null )
			return null;
		
		return request.getRequestURL().toString();
	}
}
