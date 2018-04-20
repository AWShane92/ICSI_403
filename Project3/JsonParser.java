package Project3;

//Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;

//Import required java libraries
public class JsonParser extends HttpServlet{

	  // Standard servlet method
	  public void init() throws ServletException{
	      // Do any required initialization here - likely none
	  }

	  // Standard servlet method - we will handle a POST operation
	  public void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException{
	      doService(request, response);
	  }

	  // Standard servlet method - we will not respond to GET
	  public void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException{
	      // Set response content type and return an error message
	      response.setContentType("application/json");
	      PrintWriter out = response.getWriter();
	      out.println("{ 'message' : 'Bad Json!'}");
	  }


	  // Our main worker method
	  // Parses messages e.g. {"inList" : [5, 32, 3, 12]}
	  private void doService(HttpServletRequest request, HttpServletResponse response)
			  throws ServletException, IOException{
	      // Get received JSON data from HTTP request
	      BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
	      String jsonStr = "";
	      boolean badJson = false;
	      
	      if(br != null){
	          jsonStr = br.readLine();
	      }

	      // Create JsonReader object
	      StringReader strReader = new StringReader(jsonStr);
	      JsonReader reader = Json.createReader(strReader);

	      // From the object get the array named "inList"
	      JsonArray inArray = testObject(reader);//test input from reader. 
	      HashTable ht = null; //Sets the Hash table to null;
	      
	      /*** Main code for Hashing algorithm ***/
	      
	      if(inArray == null){ //If the inArray is null then do nothing send message.
	    	  badJson = true;
	      }
	      else{
	    	  
	    	ht = new HashTable(inArray);//Hash table is created. 
	  		
	  		for(int i = 0; i < inArray.size(); i++){
	  			try{
	  				//If insert returns an error for any reason break. 
	  				ht.insert(new Node(inArray.getString(i), HashTable.HashFunction(inArray.getString(i))));
	  			}
	  			catch(Exception e){
	  				//Send error message and set the Hash table to null;
	  				badJson = true;
	  				ht = null;
	  				break;
	  			}
	  		}	  
	      }

	      //Sends the Hashtable to outputArray if and only if the Hash table was created.
	      JsonArrayBuilder outArrayBuilder = Json.createArrayBuilder();
	     
	     if(ht != null){
	    	 for(int i = (ht.tableSize - 1); i >= 0; i--){
	    		 if((ht.table[i] != null)&&(ht.table[i].next != null)){
	    			 JsonArrayBuilder elements = Json.createArrayBuilder();
	    			 for(Node temp = ht.table[i]; temp != null; temp = temp.next){
		    			  	elements.add(temp.name);
		    			   }  
	    			 outArrayBuilder.add(elements.build());
	    		 }
	    	 }
	     }
	     
	     if(badJson){
	    	 doGet(request,response);
	     }
	     else{
	      // Set response content type to be JSON
	      response.setContentType("application/json");
	      // Send back the response JSON message
	      PrintWriter out = response.getWriter();
	      out.println("{ \"outList\" : " + outArrayBuilder.build().toString() + "}");
	     }
	  }

	  // Standard Servlet method
	  public void destroy(){
	      // Do any required tear-down here, likely nothing.
	  }
	  
	  /* This method will test the object from the JsonReader 
	  * For this assignment it returns an array. */
	  public JsonArray testObject (JsonReader reader){
		JsonObject jObj;
		JsonArray inArray = null;
		try{
		jObj = reader.readObject();
		inArray = jObj.getJsonArray("inList");
		return inArray;
		}catch(NullPointerException e){
			
		}
		return inArray;
	}
}
