package com.domtar.confluence;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class Confluence 
{
	public  String createSpace(String url, String userName, String apiToken, String sKey,String sName) 
	{
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
		    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String line = "";
		    return "success";
	    } 
		catch (Exception e) 
		{
	    	StringWriter sw = new StringWriter();
	    	PrintWriter pw = new PrintWriter(sw);
	    	e.printStackTrace(pw);
	    	String sStackTrace = sw.toString(); // stack trace as a string
	    	
	    	Boolean found = Arrays.asList(sStackTrace.split(" ")).contains("java.io.IOException:");
	    	if(found){
	    		return "already exists";
	       	}
	    	else {
	    		System.out.println("github connector can't able to contact tool server");
	    		return "err";
	    	}
	    }
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
