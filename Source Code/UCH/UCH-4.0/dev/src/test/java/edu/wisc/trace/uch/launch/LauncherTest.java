package edu.wisc.trace.uch.launch;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.junit.Test;
public class LauncherTest {
	/*
	public LauncherTest() throws InterruptedException {
		Thread uchThread = new UCHThread();
		uchThread.start();

		Thread.sleep(3000);
}
	
@Test
public void testLauncher() throws LifecycleException, ServletException, IOException, URISyntaxException, InterruptedException{

	StatusLine statusLine = null;
	for (int i = 0;i <= 30;i++){
	try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
		HttpResponse response = Request.Get( new URI("http://localhost:8080/UCH/")).execute().returnResponse();
statusLine =     response.getStatusLine();
    System.out.println(statusLine.getStatusCode());
    
    if (statusLine.getStatusCode() == 302){
    	break;
    }
	}
assertEquals(302, statusLine.getStatusCode()    );    
		
	System.exit(0);
}*/
}
