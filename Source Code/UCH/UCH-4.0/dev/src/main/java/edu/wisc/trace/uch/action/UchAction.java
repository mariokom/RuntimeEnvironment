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
package edu.wisc.trace.uch.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.wisc.trace.uch.Constants;
import edu.wisc.trace.uch.UCH;
import edu.wisc.trace.uch.util.LoggerUtil;

/**
 * Forward/Receive the HTTP Request/Response between
 * {@link edu.wisc.trace.uch.servlet.UchServlet} and
 * {@link edu.wisc.trace.uch.UCH}.
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class UchAction {

	private Logger logger = LoggerUtil.getSdkLogger();

	/**
	 * Default Constructor.
	 */
	public UchAction() {

	}

	/**
	 * It calls Post Request method of UCH.
	 * 
	 * @param request
	 *            an Object of HttpServletRequest
	 * @param response
	 *            an Object of HttpServletResponse
	 * @param servletContext
	 *            an Object of ServletContext
	 */
	public void execute(HttpServletRequest request,
			HttpServletResponse response, ServletContext servletContext) {

		try {

			Object uch = servletContext.getAttribute(Constants.CONSTANT_UCH);
			if (!(uch instanceof UCH)) {
				logger.warning("Unable to get the Object of UCH from Servlet COntext.");
				return;
			}

			// ((UCH)uch).postRequest(request, response); //Parikshit Thakur :
			// 20111205. Changes to specify request protocol.
			((UCH) uch).postRequest("http", request, response);

		} catch (Exception e) {
				logger.warning("ba"+ e);
				logger.warning("ba"+ e);logger.warning("ba"+ e);logger.warning("ba"+ e);logger.warning("ba"+ e);
		}

	}
}
