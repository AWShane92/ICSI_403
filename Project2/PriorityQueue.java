package Project2;

import java.io.*;
import javax.json.*;

public class PriorityQueue {
	
	public Node top;
	public Node bottom;
	public static String en = "enqueue";
	public static String de = "dequeue";
	
	public PriorityQueue(){
		//empty constructor
	}
	public Node getTop(){
		return this.top; //returns the first node in the queue. 
	}
	/* This method takes in a newNode and adds the node to the queue in 
	 * priority order. It does this by keeping track of the order
	 * with each insert into the queue. 
	 */
	public void enqueue(Node newNode){
		
		//If the queue is empty. 
		if(isEmpty()){
			top = bottom = newNode;
		}
		//If there's only one node.
		else if(top == bottom){	
			if(newNode.pri < top.pri){
				newNode.next = top;
				top = newNode;
			}
			else{
				bottom.next = newNode;
				bottom = newNode;
				bottom.next = null;
			}
		}
		//If the newNode's priority is less than the first node's
		else if(newNode.pri < top.pri){
			newNode.next = top;
			top = newNode;
		}
		//If the newNode's priority is less than the last nodes's 
		else if(newNode.pri > bottom.pri){
			bottom.next = newNode;
			bottom = newNode;
			bottom.next = null;
		}
		//Adds the node to the queue in priority order. 
		else{
			
			Node temp = top;
			
			while(temp != null){
				/* If the newNode's priority is greater than temp's but less than temp's next	
				 * Insert the newNode in between. 		
				 */
				if((temp.next.pri > newNode.pri)&&(newNode.pri > temp.pri)){
					newNode.next = temp.next;
					temp.next = newNode;
					return;
				}
				/* If the newNode's priority is equal to temp's but is not the temp
				 */
				if((temp.pri == newNode.pri)&&(temp != newNode)){
					newNode.next = temp;
					temp.next = newNode;
					return;
				}
				temp = temp.next;
			}
		}
	}
	//This method removes the node from the front of the queue
	public void dequeue(){
		if(isEmpty()){
			return;
		}
		else{
			top = top.next;		
			if(top == null){
				bottom = null;
			}
		}
	}
	//A test method used to keep track of contents of the queue
	public void printQueue(){
		
		if(isEmpty()){
			System.out.println("The list is Empty");
			return;
		}
		Node temp = top;
		while(temp!=null){
			System.out.println("Job name: "+temp.name+", "+" Priority: " + temp.pri);
			temp = temp.next;
		}
	}
	//To test if the queue is empty
	public boolean isEmpty(){
		return ((top == null)&&(bottom == null));
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
	/* This method will test each parsing of the 
	 * JsonObject it will take in an Array for this
	 * Assignment's purposes. */
	public static boolean testJsonParse(JsonObject jsonObj){
		
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
			System.out.println("Mal json");
			System.exit(0);
		}	
		return goodJson;
	}
	//This main method is used for test purposes. 
	public static void main(String[]args) throws IOException{
	
		File json = new File("/Users/akeemshane/Desktop/inList.json");		
		BufferedReader br = new BufferedReader(new FileReader(json)); 
		String jsonString = "";
		String malJson = "Malformed Json";
		
		if(br != null){
			jsonString = br.readLine();
		}
		
		StringReader strReader = new StringReader(jsonString);
		JsonReader reader = Json.createReader(strReader);
		JsonArray inArray = testObject(reader);
		
		if(inArray == null){
			System.out.println(malJson);
			System.exit(0);
		}
			
		PriorityQueue pq = new PriorityQueue();

		for(int i = 0; i < inArray.size();i++){
			
			if(testJsonParse(inArray.getJsonObject(i))){
				if(inArray.getJsonObject(i).getString("cmd").compareTo("enqueue") == 0){
					pq.enqueue(new Node(inArray.getJsonObject(i).getString("cmd"),inArray.getJsonObject(i).getString("name"),inArray.getJsonObject(i).getInt("pri")));
				}
				if(inArray.getJsonObject(i).getString("cmd").compareTo("dequeue")==0){
					pq.dequeue();
				}
			}
			else{
				System.out.println(malJson);
				System.exit(0);
			}
		}
		pq.printQueue();
	}
}
