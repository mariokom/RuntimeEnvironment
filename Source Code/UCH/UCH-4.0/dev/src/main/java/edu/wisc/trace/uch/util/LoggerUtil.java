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

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * <p>Creates a project-wide logger with handlers and formatters.</p>
 * 
 * <p>
 * <b>Created on:</b> Feb 9, 2008<br>
 * <b>Known bugs:</b> <ul><li>The line number reported is sometimes incorrect.</li></ul><br>
 * <b>Thread safe:</b> Yes<br>
 * </p> 
 * 
 * 
 * @author Parikshit Thakur & Team, Trace R&D Center
 * @version $Revision: 798 $
*/
 
public final class LoggerUtil {

    private static Logger logger;

    private static Handler handler;

    private static Formatter formatter;

    private LoggerUtil() {
        super();
    }

    /**
     * Returns the singleton instance of the SDK logger.
     * @return a <code>Logger</code> object
     */
    public static Logger getSdkLogger() {
        if (logger == null) {
            logger = Logger.getLogger("UCH");
            formatter = new SdkFormatter();
            logger.setUseParentHandlers(false);
            handler = new SdkHandler();
            handler.setFormatter(formatter);
            logger.addHandler(handler);
           // logger.setLevel(Level.INFO);
            logger.setLevel(Level.OFF);
        }
        return logger;
    }

    /**
     * Very simple handler that outputs all logged messages to stdout.
     * 
     * <p>
     * <b>Created on:</b> Feb 9, 2008<br>
     * <b>Known bugs:</b> None<br>
     * <b>Thread safe:</b> Yes<br>
     * </p>
     * 
     * @author Parikshit Thakur & Team, Trace R&D Center
     */
    public static class SdkHandler extends Handler {

        /* (non-Javadoc)
         * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
         */
        public void publish(LogRecord record) {
            if (record == null)
                return;
            String msg = getFormatter().format(record);
            System.out.println(msg + "\n");
        }

        /* (non-Javadoc)
         * @see java.util.logging.Handler#flush()
         */
        public void flush() {
            // since we are doing System.out.println, no need to worry here.
        }

        /* (non-Javadoc)
         * @see java.util.logging.Handler#close()
         */
        public void close() throws SecurityException {
            // again, nothing to close.
        }
    }

    /**
     * <p>Formats logger output to provide the originating method
     * and line number in addition to the package on one line,
     * and the logged message on the next. For example:</p>
     * 
     * <p><code>edu.wisc.trace.uch.util.TargetDiscription.parse (Line: 85)<br>INFO: TD parsing completed successfully.</code></p>
     * 
     * <p>
     * <b>Created on:</b> Feb 9, 2008<br>
     * <b>Known bugs:</b> None<br>
     * <b>Thread safe:</b> Yes<br>
     * </p>
     * 
    * @author Parikshit Thakur & Team, Trace R&D Center
     */
    public static class SdkFormatter extends Formatter {

        /**
         * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
         */
        public synchronized String format(LogRecord record) {
            StringBuffer sb = new StringBuffer();
            Throwable t = new Throwable();
            t.fillInStackTrace();
            StackTraceElement[] stes = t.getStackTrace();
            // 7th element is logger
            StackTraceElement ste = stes[6];
            if (ste.getClassName().endsWith("ErrorHandler")) {
                // 10th element is error handler-adjusted
                ste = stes[9];
            }
            sb.append(ste.getClassName());
            sb.append(".").append(ste.getMethodName());
            sb.append(" (Line: " + ste.getLineNumber());
            sb.append(")");
            sb.append("\n");
            sb.append(record.getLevel().getLocalizedName());
            sb.append(": ").append(record.getMessage());
            return sb.toString();
        }

    }

}
