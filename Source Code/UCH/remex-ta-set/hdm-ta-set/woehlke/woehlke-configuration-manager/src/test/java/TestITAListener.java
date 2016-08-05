import java.util.List;
import java.util.Map;

import org.openurc.uch.IProfile;
import org.openurc.uch.ITA;
import org.openurc.uch.ITAListener;
import org.openurc.uch.IUCHStore;
import org.openurc.uch.UCHException;
import org.openurc.uch.UCHNotImplementedException;


public class TestITAListener implements ITAListener {

	@Override
	public void abortSession(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addUriServiceContexts(ITA arg0, String arg1,
			List<Map<String, IProfile>> arg2) throws UCHException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDocument(String arg0, String arg1,
			Map<String, IProfile> arg2) throws UCHException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIpAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IUCHStore getLocalUCHStore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<List<Map<String, List<String>>>> getResources(String arg0,
			List<Map<String, List<String>>> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, IProfile>> getTargetContexts(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getUCHProps() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isImplemented(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeTA(ITA arg0) throws UCHNotImplementedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUriServiceContexts(ITA arg0, String arg1,
			List<Map<String, IProfile>> arg2) throws UCHException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionForwardRequest(String arg0, Map<String, String> arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean setValidation(ITA arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String startUriService(ITA arg0, String arg1, int arg2,
			boolean arg3, String arg4, boolean arg5,
			List<Map<String, IProfile>> arg6) throws UCHException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stopUriService(ITA arg0, String arg1) throws UCHException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDynRes(List<String> arg0, List<String> arg1,
			List<Map<String, Object>> arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateValues(List<String> arg0, List<String> arg1,
			List<String> arg2, List<String> arg3, List<Map<String, String>> arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Map<String, String>> uploadResources(
			List<Map<String, List<String>>> arg0, List<String> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
