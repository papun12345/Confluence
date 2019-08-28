package com.domtar.confluence;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
	public String generateSpaceKey(String url,String userName, String apiToken,String sName)
	{
		String sKey="";
		String div[]=sName.split(" ");
		System.out.println(div.length);
		if(div.length==1)
		{
			if(div[0].length()==4)
			{
				sKey=div[0].substring(0, 4);
			}
			else if(div[0].length()>4)
			{
				sKey=div[0].substring(0, 3);
			}
		}
		else if(div.length>1)
		{
		for (String var : div) {
			System.out.println(var);
			sKey=sKey+var.charAt(0);
		}
		}
		System.out.println("sKey-->"+sKey.toUpperCase());
		if(checkKeyWithConfluence(url,userName,apiToken,sKey))
		{
			return sKey.toUpperCase();
		}
		else
		{
			sKey=sKey.toUpperCase()+LocalDateTime.now().getHour()+LocalDateTime.now().getMinute();
			System.out.println(sKey);
			return sKey;		
		}
			
	}
	public boolean checkKeyWithConfluence(String url, String userName, String apiToken, String sKey) 
	{
		url=url+"/"+sKey;
		int responseCode;
		String keyResponseMessage = null;
		boolean rtrnResponse = false;
		try 
		{
		 	URL obj = new URL(url);
		    HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		    conn.setRequestProperty("Content-Type", "application/json");
		    conn.setDoOutput(true);
		    conn.setRequestMethod("GET");
		    String userpass = userName + ":" + apiToken;			   
		    String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
		    conn.setRequestProperty ("Authorization", basicAuth); 
		    System.out.println(conn.getResponseCode());
		    System.out.println(conn.getResponseMessage());
		    responseCode=conn.getResponseCode();
		   //keyResponseMessage=conn.getResponseMessage();
		    switch(responseCode)
		    {
			    case 200:
			    	keyResponseMessage="The spaceKey is exists";
			    	rtrnResponse=false;
		    		break;
		    	case 400:
		    		keyResponseMessage="The spaceKey doesnot  exists";
		    		rtrnResponse=true;
		    		break;
		    	case 404:
		    		keyResponseMessage="The spaceKey doesnot  exists";
		    		rtrnResponse=true;
		    		break;
		    	case 401:
		    		keyResponseMessage="The authentication credentials are incorrect";
		    		rtrnResponse=false;
		    		break;
		    	case 403:
		    		keyResponseMessage="User does not have permission to create a space";
		    		rtrnResponse=false;
		    		break;
			    
		    }
	    } 
		catch (Exception e) 
		{
			System.out.println(e);
	    }
		
	return rtrnResponse;
		
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
			    case 200:
			    	responseMessage="The space is created";
		    		break;
		    	case 400:
		    		responseMessage="The space already exists";
		    		break;
		    	case 401:
		    		responseMessage="The authentication credentials are incorrect";
		    		break;
		    	case 403:
		    		responseMessage="User does not have permission to create a space";
		    		break;
			    
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
		
		String sName="New Fesst";
		String sKey=new Confluence().generateSpaceKey(url, userName, apiToken, sName);
		System.out.println(sKey);
		new Confluence().createSpace(url, userName, apiToken, sKey, sName);
		
	}

}
