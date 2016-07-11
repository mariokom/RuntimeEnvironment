package test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import cy.ac.ucy.cs.seit.uchCommunication.session.responses.GetDependenciesResponse;
import cy.ac.ucy.cs.seit.uchCommunication.session.responses.GetResourcesResponse;
import cy.ac.ucy.cs.seit.uchCommunication.session.responses.GetResourcesWithPropResponse;
import cy.ac.ucy.cs.seit.uchCommunication.session.responses.GetUpdatesResponse;
import cy.ac.ucy.cs.seit.uchCommunication.session.responses.GetValuesResponse;
import cy.ac.ucy.cs.seit.uchCommunication.session.responses.SetValuesResponse;

public class SessionResponsesTest {

	public static void main(String[] args) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {
		
		System.out.println("==========================================================================================");
		System.out.println("Testing the objects which store the result of the URC-HTTP protocol responses.");
		System.out.println("The responses were taken from the URC-HTTP protocol documentation.");
		System.out.println("==========================================================================================\n");
		
		
		SessionResponsesTest.testGetValuesResponse();
		
		SessionResponsesTest.testGetUpdatesResponse();
		
		SessionResponsesTest.testGetDependenciesResponse();
		
		SessionResponsesTest.testSetValuesResponse();
		
		SessionResponsesTest.testGetResoursesResponse();
		
		SessionResponsesTest.testGetResoursesWithPropResponse();
		
		System.out.println("==========================================================================================");
		System.out.println("End of test.");
		System.out.println("==========================================================================================\n");
	}
	
	
	public static void testGetValuesResponse() throws UnsupportedEncodingException, SAXException, IOException, ParserConfigurationException {
		String uchResponse = "<values>" +
				  "<elt ref=\"path\" hasDynRes=\"hasDynResValue\" socketEltType=\"socketEltTypeValue\" notifyCat=\"notifyCatValue\">" +
				    "<value>value  </value>" +
				    "<resource />" +
				    "<resource> resource </resource>" +
				    "<resource at=\"resourceUri\" />" +
				    "<dependencies>" +
				      "<relevant> relevantValue </relevant>" +
				      "<write> writeValue </write>" +
				      "<insert> insertValue </insert>" +
				    "</dependencies>" +
				  "</elt>" +
				  "<index ref=\"elementPath\">" +
				    "<indexValue>indexValue " +
				     " <resource />" +
				      "<resource>resource </resource>" +
				    "  <resource at=\"resourceUri\" />" +
				  "  </indexValue>" +
				 " </index>" +
				"</values>";

		GetValuesResponse getValuesResponse = new GetValuesResponse(uchResponse);
		System.out.println(getValuesResponse);
		
		System.out.println();
	}
	
	
	public static void testGetUpdatesResponse() throws UnsupportedEncodingException, SAXException, IOException, ParserConfigurationException {
		String uchResponse = "<updates>" +
				  "<add ref=\"addPath\" hasDynRes=\"hasDynResValue\" socketEltType=\"socketEltTypeValue\" notifyCat=\"notifyCatValue\">" +
				    "<value> value</value>" +
				    "<dependencies>" +
				      "<relevant> relevantValue </relevant>" +
				      "<write> writeValue </write>" +
				      "<insert> insertValue </insert>" +
				    "</dependencies>" +
				    "<resource />" +
				    "<resource> resource </resource>" +
				    "<resource at=\"resourceUri \" />" +
				  "</add>" +
				  "<remove ref=\"removePath\" hasDynRes=\"hasDynResValue\" socketEltType=\"socketEltTypeValue\" notifyCat=\"notifyCatValue\" />" +
				  "<update ref=\"path\" hasDynRes=\"hasDynResValue\" socketEltType=\"socketEltTypeValue\" notifyCat=\"notifyCatValue\" >" +
				    "<value> value</value>" +
				    "<dependencies>" +
				      "<relevant> relevantValue </relevant>" +
				      "<write> writeValue </write>" +
				      "<insert> insertValue </insert>" +
				    "</dependencies>" +
				    "<resource />" +
				    "<resource> resource </resource>" +
				    "<resource at=\"resourceUri \" />" +
				  "</update>" +
				  "<forward type=\"forwardType \" targetName=\"targetName\" targetId=\"targetId\" socketName=\"socketName\" authorizationCode=\"authorizationCode\" />" +
				  "<abortSession> message </abortSession>" +
				"</updates>";

		GetUpdatesResponse getUpdatesResponse = new GetUpdatesResponse(uchResponse);
		System.out.println(getUpdatesResponse);
		
		System.out.println();
		
		getUpdatesResponse = new GetUpdatesResponse("<updates />");
		System.out.println(getUpdatesResponse);
		
		System.out.println();
	}
	
	
	public static void testGetDependenciesResponse() throws UnsupportedEncodingException, SAXException, IOException, ParserConfigurationException {
		String uchResponse = "<dependencies>" +
							"<dependency ref=\"path\" dependency=\"dependencyType\">dependencyValue</dependency>" +
							"</dependencies>";

		GetDependenciesResponse getDependenciesResponse = new GetDependenciesResponse(uchResponse);
		System.out.println(getDependenciesResponse);
		
		System.out.println();
	}
	
	
	public static void testSetValuesResponse() throws UnsupportedEncodingException, SAXException, IOException, ParserConfigurationException {
		String uchResponse = "<updates>" +
				  "<add ref=\"addPath\" hasDynRes=\"hasDynResValue\" socketEltType=\"socketEltTypeValue\" notifyCat=\"notifyCatValue\">" +
				    "<value> value</value>" +
				    "<dependencies>" +
				      "<relevant> relevantValue </relevant>" +
				      "<write> writeValue </write>" +
				      "<insert> insertValue </insert>" +
				    "</dependencies>" +
				    "<resource />" +
				    "<resource> resource </resource>" +
				    "<resource at=\"resourceUri \" />" +
				  "</add>" +
				  "<remove ref=\"removePath\" hasDynRes=\"hasDynResValue\" socketEltType=\"socketEltTypeValue\" notifyCat=\"notifyCatValue\" />" +
				  "<update ref=\"path\" hasDynRes=\"hasDynResValue\" socketEltType=\"socketEltTypeValue\" notifyCat=\"notifyCatValue\" >" +
				    "<value> value</value>" +
				    "<dependencies>" +
				      "<relevant> relevantValue </relevant>" +
				      "<write> writeValue </write>" +
				      "<insert> insertValue </insert>" +
				    "</dependencies>" +
				    "<resource />" +
				    "<resource> resource </resource>" +
				    "<resource at=\"resourceUri \" />" +
				  "</update>" +
				"</updates>";

		SetValuesResponse setValuesResponse = new SetValuesResponse(uchResponse);
		System.out.println(setValuesResponse);
		
		System.out.println();
	}
	
	
	public static void testGetResoursesResponse() throws UnsupportedEncodingException, SAXException, IOException, ParserConfigurationException {
		String uchResponse = "<resourceResponse>" +
						  "<resource />" +
						  "<resource> content </resource>" +
						  "<resource at=\"resourceUri\" />" +
						"</resourceResponse>";

		GetResourcesResponse getResourcesResponse = new GetResourcesResponse(uchResponse);
		System.out.println(getResourcesResponse);
		
		System.out.println();
	}
	
	
	public static void testGetResoursesWithPropResponse() throws UnsupportedEncodingException, SAXException, IOException, ParserConfigurationException {
		String uchResponse = "<resourceResponse>" +
							  "<resourceSet start=\"start\" count=\"count\" total=\"total\">" +
							    "<resource " +
							      "eltRef=\"eltRef\" " +
							      "valRef=\"value\" " +
							      "opRef=\"opUri\" " +
							      "role=\"roleUri\" " +
							      "type=\"type\" " +
							      "format=\"mimetype\" " +
							      "forLang=\"langcode\" " +
							      "forTargetInstance=\"targetId\" " +
							      "creator=\"creator\" " +
							      "publisher=\"publisher\" " +
							      "date=\"date\" " +
							      "audience=\"audience\" >" +
							      "content" +
							    "</resource>" +
							    "<resource " +
							      "eltRef=\"eltRef\" " +
							      "valRef=\"value\" " + 
							      "opRef=\"opUri\" " +
							      "role=\"roleUri\" " +
							      "type=\"type\" " +
							      "format=\"mimetype\" " +
							      "forLang=\"langcode \" " +
							      "forTargetInstance=\"targetId\" " +
							      "creator=\"creator\" " +
							      "publisher=\"publisher\" " +
							      "date=\"date\" " +
							      "audience=\"audience\" " +
							      "at=\"resourceUri\" />" +
							  "</resourceSet>" +
							"</resourceResponse>";

		GetResourcesWithPropResponse getResourcesWithPropResponse = new GetResourcesWithPropResponse(uchResponse);
		System.out.println(getResourcesWithPropResponse);
		
		System.out.println();
	}
	
}
