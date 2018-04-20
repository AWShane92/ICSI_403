package Project1;

//Import required java libraries
import java.io.*;
import java.util.concurrent.TimeUnit;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;

public class SortingIntegers extends HttpServlet {
	  // Standard servlet method 
	  public void init() throws ServletException{
	      // Do any required initialization here - likely none
	  }
	  // Standard servlet method - we will handle a POST operation
	  public void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	      doService(request, response); 
	  }
	  // Standard servlet method - we will not respond to GET
	  public void doGet(HttpServletRequest request, HttpServletResponse response) 
			  throws ServletException, IOException {
	      // Set response content type and return an error message
	      response.setContentType("application/json");
	      PrintWriter out = response.getWriter();
	      out.println("{ 'message' : 'Use POST!'}");
	  }
	  // Our main worker method
	  // Parses messages e.g. {"inList" : [5, 32, 3, 12]}
	  // Returns the list reversed.   
	  private void doService(HttpServletRequest request,HttpServletResponse response)
	            throws ServletException, IOException{
	      // Get received JSON data from HTTP request
	      BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
	      String jsonStr = "";
	      if(br != null){
	          jsonStr = br.readLine();
	      }
	      
	      // Create JsonReader object
	      StringReader strReader = new StringReader(jsonStr);
	      JsonReader reader = Json.createReader(strReader);

	      // Get the singular JSON object (name:value pair) in this message.    
	      JsonObject obj = reader.readObject();
	      // From the object get the array named "inList"
	      JsonArray inArray = obj.getJsonArray("inList");
	      
	      	      
	      //Sends the JSON input array into a heap then sorts using heapSort.
	      HeapNode[] hna = new HeapNode().createHeap(inArray);
	      long start = System.nanoTime();//Begin time.
	      HeapNode.heapSort(hna);//heapsort is called      
	      long end = System.nanoTime();//End time.
	      long timeInMilliseconds = TimeUnit.MILLISECONDS.convert((end - start),TimeUnit.NANOSECONDS);

	      //Sends the sorted heap to JSON output array. 
	      JsonArrayBuilder outArrayBuilder = Json.createArrayBuilder();
	      for (int i = 0 ; i < inArray.size(); i++ ) {
	          outArrayBuilder.add(hna[(i + 1)].key); 
	      }
	      
	      // Set response content type to be JSON
	      response.setContentType("application/json");
	      // Send back the response JSON message
	      PrintWriter out = response.getWriter();
	      out.println("{ \"outList\" : " + outArrayBuilder.build().toString()+","+" \"algorithm\" : \"heapsort\", "+
	      "\"timeMS\" : " + timeInMilliseconds +" }"); 
	   	     
	  }	    
	  // Standard Servlet method
	  public void destroy(){
	      // Do any required tear-down here, likely nothing.
	  }

}
