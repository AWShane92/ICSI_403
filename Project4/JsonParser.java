package Project4;

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
	      out.println("{ 'message' : 'Bad JSON!'}");
	  }

	  // Our main worker method
	  // Parses messages e.g. {"inList" : [5, 32, 3, 12]}   
	  private void doService(HttpServletRequest request,HttpServletResponse response)
	            throws ServletException, IOException{
	      // Get received JSON data from HTTP request
	      BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
	      String jsonStr = "";
	      boolean error = false;
	      if(br != null){
	          jsonStr = br.readLine();
	      }
	      
	      // Create JsonReader object
	      StringReader strReader = new StringReader(jsonStr);
	      JsonReader reader = Json.createReader(strReader);
	      //Creates inArray object. 
	      JsonArray inArray = testObject(reader);
	      
	      /*** Main Code for Priority Queue. ***/
	      
	      if(inArray == null){
	    	error = true;
			}  
	      else if(inArray.size() < 3){
	    	  error = true;
	      }
	      else{
				//Instance of polygon object
	    	  	Polygon.pc = 0;
				Polygon polygon  = null;
				try{
					polygon = new Polygon(inArray);
				}
				catch(Exception e){
					error = true;
				}
				
				if(polygon.verticies != null){
					
					//Computing largest Possible number of points. 
					int lx, ly;
					lx = ly = 0;			
					for(int i = 0; i < Polygon.size; i++){
						if(polygon.sides[i].rightEndpoint().x > lx){
							lx = (int)polygon.sides[i].rightEndpoint().x;
							}
						if(polygon.sides[i].higherPoint().y > ly){
							ly = (int)polygon.sides[i].higherPoint().y;
							}
						}
					Polygon.points = new Point[(lx * ly)];
				
					for(int i = 0; i < 20; i++){
						Polygon.sweeper.p1.x = Polygon.sweeper.p2.x = i;
						for(int j = 0; j < Polygon.size; j++){
							if(Polygon.Segements_Intersect(Polygon.sweeper,polygon.sides[j])){
								if(polygon.sides[j].leftEndpoint().x == i){
								Polygon.insert(polygon.sides[j]);
							}
							if(polygon.sides[j].rightEndpoint().x == i){
								Polygon.remove(polygon.sides[j]);
							}
							}
						if(Polygon.BSTsize == 2){
							Polygon.CountPoints(i);
							}
						else if (Polygon.BSTsize > 2){
							error = true;
							break;
							}
						}				
					}					
				}
				else{
					error = true;
				}
			}
	      
	      if(error){
	    	  doGet(request, response);
	      }else{   
	      // Set response content type to be JSON
	      response.setContentType("application/json");
	      // Send back the response JSON message
	      PrintWriter out = response.getWriter();
	      out.println("{ \"count\" : " + Polygon.pc + "}"); 
	      
	      
	      }
	  }
	    
	  // Standard Servlet method
	  public void destroy()
	  {
	      // Do any required tear-down here, likely nothing.
	  }
	  
		/* This method will test the object from the JsonReader 
		 * For this assignment it returns an array. */
		public static JsonArray testObject (JsonReader reader) {
			JsonObject jObj;
			JsonArray inArray = null;
			try{
				jObj = reader.readObject();
				inArray = jObj.getJsonArray("inList");
				return inArray;
			}
			catch (Exception e){
				//Exception left blank output message in JsonParser. 
			}
			return inArray;
		}

}
