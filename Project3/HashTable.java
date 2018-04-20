package Project3;

import java.io.*;
import javax.json.*;


public class HashTable {
	
	public static int tableSize;
	public static Node[] table;
	
	public HashTable(){
		//Empty constructor
	}
	//Main Constructor for class, Initializes the hashtable
	public HashTable(JsonArray inArray){
		tableSize = firstPrime(inArray);
		table = new Node[tableSize];
	}
	
	public static void main(String[]args)throws IOException{
		
		File json = new File("/Users/akeemshane/Desktop/inList.json");		
		BufferedReader br = new BufferedReader(new FileReader(json)); 
		String jsonString = "";
		boolean badJson = false;
		String malJson = "Malformed Json";
		
		if(br != null){
			jsonString = br.readLine();
		}
		
		StringReader strReader = new StringReader(jsonString);
		JsonReader reader = Json.createReader(strReader);
		JsonArray inArray = testObject(reader);
		HashTable ht = null;
		
		if(inArray == null){
			badJson = true;
		}
		else{
		
		ht = new HashTable(inArray);
		
		for(int i = 0; i < inArray.size(); i++){
			try{
			ht.insert(new Node(inArray.getString(i), HashFunction(inArray.getString(i))));
			}
			catch(Exception e){	
				badJson = true;
				ht = null;
				break;
				}
			}
			//ht.printTable();
		}
		//Sends the Hashtable to output.
	      JsonArrayBuilder outArrayBuilder = Json.createArrayBuilder();
	     
	     if(ht != null){
	    	 for(int i = (ht.tableSize -1); i >= 0 ; i--){
	    		 if((ht.table[i] != null)&&(ht.table[i].next != null)){
	    			 JsonArrayBuilder elements = Json.createArrayBuilder();   			 
	    			 for(Node temp = table[i]; temp != null; temp = temp.next){
	    			  	elements.add(temp.name);
	    			   } 
	    			 outArrayBuilder.add(elements.build());
	    		 }
	    	 }
	     }
	     
	     if(badJson){
	    	System.out.println(malJson);
	     }
	     else{
	    System.out.println("{ \"outList\" : " + outArrayBuilder.build().toString() + "}");
	     }
	     
	}
	/* Inserts each node into the Hashtable. Collisions are Handled
	 * using a linked list.
	 */
	public void insert(Node element){
		//If this position in the array is empty simply insert the Node there
		if(table[element.key] == null){
			table[element.key] = element;
		}
		/* If it is a collision however there ASCII values don't match 
		 * call HashFunction2 to get a new key. 
		 */
		else if(!checkString(table[element.key].name,element.name)){ //Values don't match. 
			element.key = HashFunction2(element.name);
			//If that position in the Hashtable is empty simply insert.
			if(table[element.key] == null){
				table[element.key] = element;
			}
			/* If the position is occupied and the 
			 * ASCII values do match simply reinsert into the table.
			 */
			else if(checkString(table[element.key].name,element.name)){
				this.insert(element);
			}
			else{
				/* Checks for the next available location in the Hashtable
				 * If no next location is found the Node isn't added. 
				 * Meaning there were NO similarities between it and another 
				 * element in the table. 
				 */
				for(int i = 0; (i + element.key) < tableSize; i++){
					if(table[i + element.key] == null){
						table[i + element.key] = element;
						element.key = element.key + i;
						return;
					}
				}				
			}
		}
		else{
			/* When a collision has happened and the nodes have the 
			 * same ASCII value. 
			 */
			Node temp = table[element.key];
			while(temp != null){
				//Return when element is added.
				if(temp.next == null){
					temp.next = element;
					return;
					}
				temp = temp.next;
				}
			}		
	}
	//This method checks the ASCII values of both strings. 
	public boolean checkString(String name1, String name2){
		
		//Convert both strings to char[]
		char[] cname1 = name1.toLowerCase().toCharArray();
		char[] cname2 = name2.toLowerCase().toCharArray();
		int key1,key2;
		key1 = key2 = 0;
		//Compute key1 and key2. 
		for(int i = 0; i < name1.length(); i++){
			key1 += cname1[i];					
		}
		for(int i = 0; i < name2.length(); i++){
			key2 += cname2[i];					
		}
		//Return their truth value of their comparison.
		return (key1 == key2);
	}
	
	/* Primary Hash function. Takes the sum of the ASCII 
	 * values of the input string and returns a key value 
	 * using the division method. 
	 */
	public static int HashFunction(String name){
	
		char[] cname = name.toLowerCase().toCharArray();
		int key = 0;
		
		for(int i = 0; i < name.length(); i++){
			key += cname[i];
		}
		
		return key = key % tableSize;
	}
	
	/* Secondary Hash function. This function is only called when
	 * Nodes are hashed to the same key from the previous function
	 * but their ASCII sums are different. This method uses the multiplication method. 
	 */
	public static int HashFunction2(String name){
		
		char[] cname = name.toLowerCase().toCharArray();
		int key = 0;
		double A = (Math.sqrt(5) -1)/2;
		
		for(int i = 0; i < name.length(); i++){
			key += cname[i];
		}
		
		return key = (int)(tableSize * ((key * A) % 1));
	}
	
	//Gets first prime number that is smaller than the inArray's size. 
	public int firstPrime(JsonArray inArray){
		
		//First assumes that the array size is a prime number. 
		int myPrime = inArray.size();
		
		for(;myPrime > 1; myPrime--){
			if(checkPrimeNum(myPrime)){
				return myPrime;
			}
		}
		
		return myPrime;
	}
	//Checks to see if the input number is prime
	public boolean checkPrimeNum(int myPrime){
		
		for(int j = 2; (j*j) <= myPrime; j++){
			if(myPrime % j == 0){
				return false;
			}
		}	
		return true;
	}
	//Prints the hash table. this function isn't used in my primary program. 
	public void printTable(){
		
		for(int i = (tableSize - 1); i >= 0; i--){
			if((table[i] != null)&&(table[i].next != null)){
				Node temp = table[i];
				while(temp != null){
					System.out.print(temp.name + " " + temp.key+ ",");
					temp = temp.next;			
				}
				System.out.println();
			}		
		}
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
