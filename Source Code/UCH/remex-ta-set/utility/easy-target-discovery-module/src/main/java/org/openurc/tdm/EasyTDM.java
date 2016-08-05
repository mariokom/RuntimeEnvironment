package org.openurc.tdm;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openurc.uch.IProfile;
import org.openurc.uch.ITDM;
import org.openurc.uch.ITDMListener;
import org.openurc.uch.TDMFatalException;
import org.openurc.uch.TDMNotImplementedException;

import edu.wisc.trace.uch.util.LoggerUtil;

public class EasyTDM extends Thread implements ITDM {
	public Logger logger = LoggerUtil.getSdkLogger();
	public Map<String, String> uchProps;
	protected Map<String, String> tdmProps;
	protected ITDMListener tdmListener;
	private String tdmName = "simple-tdm";
	
	@Override
	public void contextsClosed(List<Map<String, IProfile>> arg0) throws TDMFatalException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextsOpened(List<Map<String, IProfile>> arg0) throws TDMFatalException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextsUpdated(List<Map<String, IProfile>> arg0) throws TDMFatalException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, String> getTDMProps() {
		return this.tdmProps;

		}

	@Override
	public List<Map<String, IProfile>> getTargetContexts(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(ITDMListener tdmListener, Map<String, String> tdmProps, Map<String, String> uchProps,
			List<Map<String, IProfile>> arg3) throws TDMFatalException {
		
		if ( (tdmListener == null) || (tdmProps == null) || (uchProps == null) ) {
			throw new TDMFatalException();
		}
		
		this.tdmListener = tdmListener;
		this.tdmProps = tdmProps;
		this.uchProps = uchProps;
		
		logger.info(tdmName + " was successfuly initialized");
		
	}

	@Override
	public void startDiscovery() throws TDMFatalException {
		logger.info("Start discovery process in " + tdmName);
		this.start();
		
	}

	@Override
	public void stopDiscovery() throws TDMFatalException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void targetRequest(String arg0, Object arg1, Object arg2, Map<String, IProfile> arg3)
			throws TDMFatalException, TDMNotImplementedException {
		// TODO Auto-generated method stub
		
	}

	public void finalize() {
		
		
	}
	
@Override
public void run() {

	TargetList targetList = EasyTDMConfigurator.readTargetList("./resources/time-service/time-service-tdm/TargetList.xml");
	logger.info("Found " + targetList.getSize() + " targets.");
	for(Target target : targetList.getTarget()){
		
		
		Map<String, Object> targetProps = target.getTargetPropsMap(); 
		Map<String, String> taProps = target.getTaPropsMap();
		
		targetProps = convertTargetProps(targetProps);
		logger.info(targetProps + "");
		logger.info(taProps + "");
		tdmListener.targetDiscovered((ITDM)this, targetProps, taProps, null);
	}
		
	
	
}

protected static Map<String, Object> convertTargetProps(Map<String, Object> targetProps) {
	for (String key: targetProps.keySet()){
		Object value = targetProps.get(key);
		value = value.toString().replace("${uch.resources}", "file:///" + System.getProperty("user.dir") + "/resources" );
		System.out.println(value);
		value = value.toString().replace("\\", "/");
		targetProps.put(key, value);
	}
	return targetProps;
}	
}
