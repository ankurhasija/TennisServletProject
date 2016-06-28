package org.ankur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Servlet implementation class FetchTennisInfo
 */
@WebServlet(description = "fetch tennis info", urlPatterns = { "/TennisPlayerInformation" })
public class FetchTennisInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String category = request.getParameter("category");
		String rank = request.getParameter("rank");
		
		try
		{
			URL url = new URL("http://localhost:8080/RestWS/webapi/players?category=" + category + "&rank=" + rank);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/xml");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode() + conn.getResponseMessage());
			}
	
//			BufferedReader br = new BufferedReader(new InputStreamReader(
//				(conn.getInputStream())));
//	
//			String output;
//			System.out.println("Output from Server .... \n");
//			while ((output = br.readLine()) != null) {
//				System.out.println(output);
//			}
	
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         
			DocumentBuilder dBuilder = null;
			try 
			{
				dBuilder = dbFactory.newDocumentBuilder();
			} 
			catch (ParserConfigurationException e) 
			{
				e.printStackTrace();
			}
         
			Document doc = null;
			try 
			{
				doc = dBuilder.parse(conn.getInputStream());
			} 
			catch (SAXException e) 
			{
				e.printStackTrace();
			}
         
			doc.getDocumentElement().normalize();
         
			
			
			NodeList nList = doc.getElementsByTagName("player");
	       
	        String[] names = new String[5];
	        
	        for (int temp = 0; temp < nList.getLength(); temp++) 
	        {
	            Node nNode = nList.item(temp);
	            
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) 
	            {
	               Element eElement = (Element) nNode;
	               
	               names[temp] = eElement.getElementsByTagName("name").item(0).getTextContent();
	              
	            }
	        }
         
			System.out.println(names[0]);
			conn.disconnect();
			
						
			response.getWriter().write(names[0] + ","  + names[1] + "," + names[2] + "," + names[3] + "," + names[4]);
	
		  } catch (MalformedURLException e) {
	
			e.printStackTrace();
		  } catch (IOException e) {
	
			e.printStackTrace();
		  }
	}
		
		
	
}
