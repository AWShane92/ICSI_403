package Project2;

//Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;

public class JsonParser extends HttpServlet{
	
	  // Standard servlet method 
	  public void init() throws ServletException{
	      // Do any required initialization here - likely none
	  }

	  // Standard servlet method - we will handle a POST operation
	  public void doPost(HttpServletRequest request,HttpServletResponse response)
	            throws ServletException, IOException{
	      doService(request, response); 
	  }

	  // Standard servlet method - we will not respond to GET
	  public void doGet(HttpServletRequest request,HttpServletResponse response)
	            throws ServletException, IOException{
	      // Set response content type and return an error message
	      response.setContentType("application/json");
	      PrintWriter out = response.getWriter();
	      out.println("{ 'message' : 'Use POST!'}");
	  }

	  // Our main worker method
	  // Parses messages e.g. {"inList" : [5, 32, 3, 12]}   
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
	      //Creates inArray object. 
	      JsonArray inArray = testObject(reader, response);
	      
	      /*** Main Code for Priority Queue. ***/
	      
	      if(inArray == null){
	    	doGet(request,response);
			}      
	      //Creates the PriorityQueue.
	      PriorityQueue pq = new PriorityQueue();
	      
	      /* Creates a JSON object called command, Then parses the command object based upon the
	       * "cmd" string. If the "cmd" String is equal to enqueue then a new node will be added to the 
	       * queue. If the "cmd" String is equal to dequeue then the first node will be removed from the
	       * queue. If the command neither than the program does nothing. 
	       */
	      for(int i = 0; i < inArray.size();i++){
				
				if(testJsonParse(inArray.getJsonObject(i),request,response)){
					if(inArray.getJsonObject(i).getString("cmd").compareTo("enqueue") == 0){
						pq.enqueue(new Node(inArray.getJsonObject(i).getString("cmd"),inArray.getJsonObject(i).getString("name"),inArray.getJsonObject(i).getInt("pri")));
					}
					if(inArray.getJsonObject(i).getString("cmd").compareTo("dequeue") == 0){
						pq.dequeue();
					}
				}
				else{
			    	doGet(request,response);
			    	break;
				}
			}
	      
	      
	      // Set response content type to be JSON
	      response.setContentType("application/json");
	      // Send back the response JSON message
	      PrintWriter out = response.getWriter();      
	      JsonArrayBuilder outArrayBuilder = Json.createArrayBuilder();
	      Node temp = pq.getTop();
	      
	      while(temp != null){
	    	  outArrayBuilder.add(temp.name);
	    	  temp = temp.next;
	      	}  
	      out.println("{ \"outList\" : " + outArrayBuilder.build().toString() + "}"); 

	  }
	    
	  // Standard Servlet method
	  public void destroy()
	  {
	      // Do any required tear-down here, likely nothing.
	  }
	  
	  /* This method will test the object from the JsonReader 
	  * For this assignment it returns an array. */
	  public JsonArray testObject (JsonReader reader, HttpServletResponse response){
		JsonObject jObj;
		JsonArray inArray = null;
		try{
		jObj = reader.readObject();
		inArray = jObj.getJsonArray("inList");
		return inArray;
		}catch(Exception e){
			
		}
		return inArray;
	}
		/* This method will test each parsing of the 
		 * JsonObject it will take in an Array for this
		 * Assignment's purposes. */
	  public boolean testJsonParse(JsonObject jsonObj,HttpServletRequest request,HttpServletResponse response)
			  throws ServletException, IOException {
			
		  boolean goodJson = true;
		  String priVal = "";
		  float floVal;
			
		  try{
			//If the command isn't enqueue or dequeue
			if(!((jsonObj.getString("cmd").compareTo("enqueue")==0)||(jsonObj.getString("cmd").compareTo("dequeue") == 0))){
				goodJson = false;
			}
			//If job priority is a negative number. 
			if(jsonObj.getString("cmd").compareTo("enqueue") == 0){
				if(jsonObj.getString("name")==null){
					goodJson = false;
				}
				if(jsonObj.getInt("pri") < 0){
					goodJson = false;
				}
				if(jsonObj.getInt("pri") >= 0){
					priVal = jsonObj.get("pri").toString();
					floVal = Float.parseFloat(priVal);
					if(jsonObj.getInt("pri") != floVal){
						goodJson = false;
					}
				}
			}		
		}
		catch(Exception e){
			doGet(request,response);
		}	
		return goodJson;
	}

}
