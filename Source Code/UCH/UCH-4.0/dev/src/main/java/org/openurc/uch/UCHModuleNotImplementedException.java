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

package org.openurc.uch;

/**
 * This class allows developers to distinguish UCHModuleNotImplemented Exception from Java 
 * exceptions.
 *  
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
 */
public class UCHModuleNotImplementedException extends Exception {

	 /**
     *  Generated serialization ID.
     */
    private static final long serialVersionUID = 3256444702852002346L;

    /**
     * Creates a new <code>UCHModuleNotImplementedException</code> instance.
     *
     */
    public UCHModuleNotImplementedException() {
        super();
    }

    /**
     * Creates a new <code>UCHModuleNotImplementedException</code> instance with a specific message.
     *
     * @param message The message to be sent with the exception.
     */
    public UCHModuleNotImplementedException(String message) {
        super(message);
    }

    /**
     * Creates a new <code>UCHModuleNotImplementedException</code> instance with a specific message 
     * and a <code>Throwable</code> cause.
     *
     * @param message The message to be sent with the exception.
     * @param cause The <code>Throwable</code> cause.
     */
    public UCHModuleNotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new <code>UCHModuleNotImplementedException</code> instance with a <code>Throwable</code> cause.
     *
     * @param cause The <code>Throwable</code> cause.
     */
    public UCHModuleNotImplementedException(Throwable cause) {
        super(cause);
    }
    
    

}
