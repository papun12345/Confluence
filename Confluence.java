package com.domtar.confluence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Confluence 
{
	static String stringToParse;
	public static String getHomePageId(HttpURLConnection conn) throws  Exception
	{
		StringBuilder response = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    String line = "";
	    while ((line = reader.readLine()) != null) 
	        response.append(line);
	    stringToParse=response.toString();
	    //System.out.println(response);
	    JSONParser parser = new JSONParser();
	    JSONObject json = (JSONObject) parser.parse(stringToParse);
	    JSONObject homepage = (JSONObject) json.get("homepage");
	    String id= homepage.get("id").toString();
	    System.out.println("id-->"+id);
		return id;
	}
	public String deleteHomePage(String userName,String apiToken, String id)
	{
		String url="https://subha.atlassian.net/wiki/rest/api/content/"+id;
		try 
		{
			StringBuilder response = new StringBuilder();
		 	URL obj = new URL(url);
		    HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		    conn.setRequestProperty("Content-Type", "application/json");
		    conn.setDoOutput(true);
		    conn.setRequestMethod("DELETE");
		    String userpass = userName + ":" + apiToken;			   
		    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
		    conn.setRequestProperty ("Authorization", basicAuth); 
		   // String data = "{\"key\":\""+sKey+"\",\"name\":\""+sName+"\"}";
		    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		    //out.write(data);
		    out.close();
		    System.out.println(conn.getResponseCode());
		    System.out.println(conn.getResponseMessage());
		    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line = "";
		    while ((line = reader.readLine()) != null) 
		        response.append(line);
		    System.out.println(response);
		    return "success";
	    } 
		catch (Exception e) 
		{
			System.out.println(e);
			return "error";
	    }
		
	}
	public  String createSpace(String url, String userName, String apiToken, String sKey,String sName) 
	{
		int responseCode;
		String responseMessage = null;
		String id;
		try 
		{
		 	URL obj = new URL(url);
		    HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		    conn.setRequestProperty("Content-Type", "application/json");
		    conn.setDoOutput(true);
		    conn.setRequestMethod("POST");
		    String userpass = userName + ":" + apiToken;			   
		    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
		    conn.setRequestProperty ("Authorization", basicAuth); 
		    String data = "{\"key\":\""+sKey+"\",\"name\":\""+sName+"\"}";
		    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		    out.write(data);
		    out.close();
		    System.out.println(conn.getResponseCode());
		    System.out.println(conn.getResponseMessage());
		    responseCode=conn.getResponseCode();
		    responseMessage=conn.getResponseMessage();
		    switch(responseCode)
		    {
		    	//do from here
		    	
		    
		    }
		    if(conn.getResponseCode()==200)
		    {
		    	id=getHomePageId(conn);
		    	new Confluence().deleteHomePage(userName, apiToken, id);
		    	System.out.println("home page deleted");
		    }
		    return "success";
	    } 
		catch (Exception e) 
		{
			System.out.println(e);
	    }
		return responseMessage;
	}
	public static void main(String[] args) 
	{
		String url="https://subha.atlassian.net/wiki/rest/api/space";
		String userName="papunpikun@gmail.com";
		String apiToken="RB6fL0Xe780c6pDmtZIw3252";
		String sKey="SUBA";
		String sName="Subha";
		new Confluence().createSpace(url, userName, apiToken, sKey, sName);
	}

}
