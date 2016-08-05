package edu.wisc.trace.uch.launch;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.xml.bind.JAXBException;

import org.apache.catalina.LifecycleException;

public class UCHThread extends Thread {
public void run(){
	try {
		Launcher.main(null);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

}
