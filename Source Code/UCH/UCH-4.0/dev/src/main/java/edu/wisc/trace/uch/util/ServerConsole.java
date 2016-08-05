package edu.wisc.trace.uch.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.catalina.startup.Tomcat;

/**
@author  Lukas Smirek
* @created 13.08.2015

Copyright (C) 2015 Hochschule der Medien (HDM) / Stuttgart Media University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/


public class ServerConsole extends Thread {
	
	private Tomcat server;
	
	   public ServerConsole(Tomcat tomcat) {
		   this.server = tomcat;
	}
	public void run(){
		   
		   
		   						BufferedReader	reader = new BufferedReader(new InputStreamReader(System.in));
				boolean stop = false;
						while (!stop){
				String line = "";
				try {
					line = reader.readLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (line == null){		
					continue;
				}
				if (line.equals("stop")){
							
								
									System.out.println("Stoppe Tomcat");
									try {
										server.stop();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							stop = true;
							return;
						}
						System.out.println("Invalid command");
						}
			
		}
	}
