package lusm.whoelke.electricityoutlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import edu.wisc.trace.uch.util.LoggerUtil;

public class ElectricityOutletHandler {

	public Logger logger = LoggerUtil.getSdkLogger();
	private String host_name;
private int port = 80;

public WoelkeWebSteckdose update() {
WoelkeWebSteckdose web_steckdose = null;
	String request_string = "http://" + host_name + ":" + port + "/cgi-bin/status";
	
	try {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		System.out.println(request_string);
		HttpGet httpGet = new HttpGet(request_string);
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		    System.out.println(response1.getStatusLine());

		    
		    		    HttpEntity entity1 = response1.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
		    		    web_steckdose = parseStatusResponse(entity1.getContent());
System.out.println("geparst");						
		    		    
//		    EntityUtils.consume(entity1);

					response1.close();				
		     System.out.println("fertig");
		} catch (ClientProtocolException e) {
						e.printStackTrace();
		} catch (IOException e) {
						e.printStackTrace();
		} finally {


		}
		

	return web_steckdose;
}

	private WoelkeWebSteckdose parseStatusResponse(InputStream content) {
				WoelkeWebSteckdose woelke = new WoelkeWebSteckdose(); 
		BufferedReader in = new BufferedReader(new InputStreamReader(content	));
		try {
		String line = "";
			while (!(line = in.readLine() ).equals("</html>") )  {
				System.out.println(line);
								if (line.equals("<div>") ){
StringTokenizer	data  = new StringTokenizer(in.readLine(), ";");
while (data.hasMoreTokens()){
	try {
	String data_splitter = data.nextToken();
	System.out.println(data_splitter);
		StringTokenizer splitter = new StringTokenizer(data_splitter,":");
	String p1 = splitter.nextToken();
	String p2 = splitter.nextToken();
		if ( p1.equals("S1") || p1.equals("S2") ||  p1.equals("S3")   ){
		if (p1.equals("S1")) woelke.s1 = p2.equals("AN");
		else if (p1.equals("S2")) woelke.s2 = p2.equals("AN");
		else if (p1.equals("S3")){ woelke.s3 = p2.equals("AN"); } 
			
		 else if (p1.equals("T")){
			woelke.temp = Double.parseDouble(p2);
		}
		
		}
		} catch (NoSuchElementException e){
		}
		
	}// splitting while 
	} // if <div>
					}
									} catch (IOException e) { 
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
System.out.println("w " + woelke);
	return woelke;
}

	public double getTemperature() {
		return update().temp;
	}

	public boolean getSocket1() {
		WoelkeWebSteckdose whoelke = update();	
		return whoelke.s1;
	}

	public boolean getSocket2() {
		return update().s2;
	}

	public boolean getSocket3() {
		return update().s3;
	}

	public void setSocket1(boolean setting) {
		System.out.println("set1");
		sendSetRequest(1,setting);
	}
	
	public void setSocket2(boolean setting) {
		sendSetRequest(2,setting);
		
	}

	public void setSocket3(boolean setting) {
				sendSetRequest(3,setting);
	}

	private void sendSetRequest(int socket_number, boolean setting){
	String request_string = createSetRequestString(socket_number,setting);
	handleRequest(request_string);
	}
	
		private void sendgetRequest(int socket_number, boolean setting){
			String request_string = createGetRequestString(socket_number,setting);
			handleRequest(request_string);
		}
			
		
		public InputStream handleRequest(String request_string){ 
		try {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		System.out.println(request_string);
		HttpGet httpGet = new HttpGet(request_string);
		CloseableHttpResponse response1 = httpclient.execute(httpGet);
		    System.out.println(response1.getStatusLine());

		    
		    		    HttpEntity entity1 = response1.getEntity();
		    // do something useful with the response body
		    // and ensure it is fully consumed
		    		    readRequest(entity1.getContent());
		    EntityUtils.consume(entity1);
//		    response1.close();
		    return entity1.getContent();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {


		}
		return null;
				}

	private String createGetRequestString(int socket_number, boolean setting) {
		// TODO Auto-generated method stub
		return null;
	}
	

	private String createSetRequestString(int i, boolean setting) {
				int j;
		if (setting) {
		  j = 1;
		} else {
			j = 0;
		} 
		return "http://" + host_name + ":" + port + "/cgi-bin/schalten?steckdose_nr=" + i + "&steckdose_soll="+ j;
		
	}

	public void test() {
		// TODO Auto-generated method stub
		handleRequest("http://www.google.de");
	}
	void readRequest(InputStream inputStream){
		BufferedReader test_in = new BufferedReader(new InputStreamReader(inputStream));
		try {
			System.out.println( test_in.readLine() );
			System.out.println( test_in.readLine() );
			System.out.println( test_in.readLine() );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setHost(String host) {
this.host_name = host;
logger.info("Hostname was set to: " + this.host_name);		
	}

	public void setPort(int  port) {
		this.port = port;
		
	}

	public String getHost() {
		return this.host_name;
	}
	
		public int getPort(){
				return this.port;
	}

		public void testProgram() {
			System.out.print("testing...");
			for (int i = 0; i < 3; i++){
			System.out.print(" ," + (i+1 ));
				try {				
				this.sendSetRequest(1, true);
				this.sendSetRequest(2, false);
				this.sendSetRequest(3, false);

	Thread.				sleep(1000);

this.sendSetRequest(1, false);
this.sendSetRequest(2, true);
this.sendSetRequest(3, false);
Thread.				sleep(1000);

this.sendSetRequest(1, false);
this.sendSetRequest(2, false);
this.sendSetRequest(3, true);
Thread.				sleep(1000);

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println(" ...done!");

			
		}
}