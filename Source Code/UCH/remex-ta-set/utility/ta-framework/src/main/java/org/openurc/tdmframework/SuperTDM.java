/*
Copyright 2006-2013  Board of Regents of the University of Wisconsin System (Univ. of Wisconsin-Madison, Trace R&D Center).  
Copyright 2015 Hochschule der Medien (HDM) / Stuttgart Media University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 

"Parts of this program were based on software developed by the Trace Center, University of Wisconsin-Madison under funding from NIDRR / US Dept of Education."

*/

package org.openurc.tdmframework;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openurc.uch.IProfile;
import org.openurc.uch.ITDM;
import org.openurc.uch.ITDMListener;
import org.openurc.uch.TDMFatalException;
import org.openurc.uch.TDMNotImplementedException;

import edu.wisc.trace.uch.util.LoggerUtil;
/**
 * 
 * @author LukasSmirek
 *
 */
public abstract class SuperTDM implements ITDM {
	public Logger logger = LoggerUtil.getSdkLogger();
	public Map<String, String> uchProps;
	protected Map<String, String> tdmProps;
	protected ITDMListener tdmListener;
	public void contextsClosed(List<Map<String, IProfile>> arg0) throws TDMFatalException {
			 
	}
	public void contextsOpened(List<Map<String, IProfile>> arg0) throws TDMFatalException {
		// TODO Auto-generated method stub
		
	}
	public void contextsUpdated(List<Map<String, IProfile>> arg0) throws TDMFatalException {
		// TODO Auto-generated method stub
		logger.info("lusm;");
	}
	public Map<String, String> getTDMProps() {
		return this.tdmProps;
	}
	public void finalize() {
		
	
	}
	public List<Map<String, IProfile>> getTargetContexts(String arg0) {
		// TODO Auto-generated method stub
		
		return null;
	}
	public void init(ITDMListener tdmListener, Map<String, String> tdmProps, Map<String, String> uchProps, List<Map<String, IProfile>> contexts)
			throws TDMFatalException {
				
				
				if ( (tdmListener == null) || (tdmProps == null) || (uchProps == null) ) {
					throw new TDMFatalException();
				}
				
				this.tdmListener = tdmListener;
				this.tdmProps = tdmProps;
				this.uchProps = uchProps;
				
				logger.info("Woehlke-TDM is initialiced");
			}	public void targetRequest(String arg0, Object arg1, Object arg2,
					
			Map<String, IProfile> arg3) throws TDMFatalException, TDMNotImplementedException {
				// TODO Auto-generated method stub
				
			}
/* this class is callled by the UCH to start the discovery process. When using the extending the superTDM class  a discovery thread should be instanciated and started
 *  (non-Javadoc)
 * @see org.openurc.uch.ITDM#startDiscovery()
 */
			public abstract void startDiscovery() throws TDMFatalException;
}
