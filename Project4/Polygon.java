package Project4;

import java.io.*;
import java.lang.Math;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Polygon {
	
	public Point[] verticies;
	public Edge[] sides;
	public static BSTnode root;
	public static int BSTsize;
	public static Point[] points;
	public static int pc = 0;
	public static int size;
	public static Edge sweeper = new Edge("sweeper",new Point(0,0),new Point(0,19));
	
	public Polygon(JsonArray inArray){
		size = inArray.size();
		this.verticies = new Point[size];
		this.sides = new Edge[size];
		this.buildPolygon(inArray);
	}
	public void buildPolygon(JsonArray inArray){
		
		for(int i = 0; i < size; i++){
			if(testJsonParse(inArray.getJsonObject(i))){
			this.verticies[i] = new Point(inArray.getJsonObject(i).getInt("x"),inArray.getJsonObject(i).getInt("y"));
			}
			else{
				size = 0;
				this.verticies = null;
				this.sides = null;
				return;
			}
		}
		for(int j = 0; j < size; j++){
			if((j + 1) == size){
				this.sides[j] = new Edge(j,this.verticies[j],this.verticies[0]);
			}else{
				this.sides[j] = new Edge(j,this.verticies[j],this.verticies[(j + 1)]);
			}
		}
	}
	
	public static int crossProduct(Point p1, Point p2, Point p3){
		int determinant = (int)((p1.x - p3.x)*(p2.y - p3.y) - (p2.x - p3.x)*(p1.y - p3.y));	
		return determinant;
	}
	
	public static boolean Segements_Intersect(Edge sweeper, Edge e){
		int d1, d2, d3, d4;
		
		d1 = crossProduct(e.p1,e.p2,sweeper.p1);
		d2 = crossProduct(e.p1,e.p2,sweeper.p2);
		d3 = crossProduct(sweeper.p1,sweeper.p2,e.p1);
		d4 = crossProduct(sweeper.p1,sweeper.p2,e.p2);
		if((((d1 > 0)&&(d2 < 0))||((d1 < 0) && (d2 > 0)))&&(((d3 > 0)&&(d4 < 0))||((d3 < 0) && (d4 > 0))) ){
			return true;		
		}
		else if((d1 == 0)&&(On_Segment(e.p1,e.p2,sweeper.p1))){
			return true;
		}
		else if((d2 == 0)&&(On_Segment(e.p1,e.p2,sweeper.p2))){
			return true;
		}
		else if((d3 == 0)&&(On_Segment(sweeper.p1,sweeper.p2,e.p1))){
			return true;
		}
		else if((d4 == 0)&&(On_Segment(sweeper.p1,sweeper.p2,e.p2))){
			return true;	
		}
		
		return false;
	}
	
	public static boolean On_Segment(Point p1, Point p2, Point p3){
			
		if(((Math.min(p1.x, p2.x) <= p3.x) &&(p3.x <= Math.max(p1.x, p2.x)))&&((Math.min(p1.y, p2.y) <= p3.y) &&(p3.y <= Math.max(p1.y, p2.y)))){
			return true;
		}
		return false;
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
			inArray = null;
			//Exception left blank output message in JsonParser. 
		}
		return inArray;
	}
	public boolean testJsonParse(JsonObject jo){
		
		boolean ne = true;
		String fvx, fvy;
		float xflo, yflo;
		
		try{
			
			if((jo.getInt("x") < 0)||(jo.getInt("x") > 19)){	
				ne = false;
			}
			if((jo.getInt("y") < 0)||(jo.getInt("y") > 19)){	
				ne = false;
			}
			if((jo.getInt("x") >= 0)&&(jo.getInt("x") <= 19 )){
				fvx = jo.get("x").toString();
				xflo = Float.parseFloat(fvx);
				if(xflo != jo.getInt("x")){
					ne = false;
				}
				
			}
			if((jo.getInt("y") >= 0)&&(jo.getInt("y") <= 19 )){
				fvy = jo.get("y").toString();
				yflo = Float.parseFloat(fvy);
				if(yflo != jo.getInt("y")){
					ne = false;
				}				
			}				
		}
		catch(Exception e){
			ne = false;
		}		
		return ne;	
	}
	public static void insert(Edge e)
	     {
	        if(root!=null){
	            insert(root, e);
	        }
	        else{
	            root = new BSTnode(e);
	            BSTsize += 1;
	        }
	     }
	public static void insert(BSTnode node, Edge e)
	     {
	        if (e.leftEndpoint().y <= node.segment.leftEndpoint().y) {
	            if (node.below != null) {
	                   insert(node.below, e);
	            } else {
	                node.below = new BSTnode(e);
	                node.below.prev = node;
	                BSTsize += 1;
	            }
	        } else if (e.leftEndpoint().y > node.segment.leftEndpoint().y) {
	            if (node.above != null) {
	                insert(node.above, e);
	            } else {
	                node.above = new BSTnode(e);
	                node.above.prev = node;
	                BSTsize += 1;
	            }
	        }
	     }
	public static BSTnode search(Edge e){
		//If the tree is empty or if the data is empty then return null	
		if(isEmpty() || e == null){ 
			return null;
		}
		else{
			BSTnode temp = root;
			while(temp != null){
				//Returns temp if data is found
				if(temp.segment == e){
					return temp;
				}
				//Compares temp to data to determine which direction  to traverse
				else if(temp.segment.leftEndpoint().y > e.leftEndpoint().y){
					temp = temp.below;
				}
				else{
					temp = temp.above;
				}
			}
			return temp;
		}
	}
	//***This delete method uses find Maximum***
	public static void remove(Edge e){
		
		BSTnode delete = search(e);
		if(delete == null){
			System.out.println("This node is not in the tree");
			return;
		}
		else
		{
			if(delete.below == null && delete.above == null)
			{
				if(delete == root){
					root = null;
					BSTsize--;
				}
				else if(delete.prev.below == delete)
				{
					delete.prev.below = null;
					BSTsize--;
				}
				else
				{
					delete.prev.above = null;
					BSTsize--;
				}
			}
			else if(delete.below != null && delete.above == null)
			{
				if(delete == root){
					root = delete.below;	
					BSTsize--;
				}
				else if(delete.prev.below == delete)
				{
					delete.prev.below = delete.below;
					BSTsize--;
				}
				else
				{
					delete.prev.above = delete.below;
					BSTsize--;
				}
			}
			else if(delete.below == null && delete.above != null)
			{
				if(delete == root){
					root = delete.above;				
					BSTsize--;
				}
				else if(delete.prev.below == delete)
				{
					delete.prev.below = delete.above;
					BSTsize--;
				}
				else
				{
					delete.prev.above = delete.above;
					BSTsize--;
				}
			}
			else
			{
				//If delete is the root node
				if(delete==root){
					
					BSTnode max = findMaximum(delete.below);
					
						max.above = delete.above;
						root = delete.below;	
						BSTsize--;
				}
				else{
				/*If delete has two children uses findMaximum to find the maximum
				 * value of delete's left sub tree, then sets delete's right subtree
				 * to the right of the max value of the left subtree, then sets
				 * deletes parents to deletes left
				 */
				BSTnode max = findMaximum(delete.below);
				
				if(delete.prev.below == delete){
					
					max.above= delete.above;
					delete.prev.below = delete.below;
					BSTsize--;
				}
				else{
					max.above = delete.above;
					delete.prev.above = delete.below;
					BSTsize--;
					}	
				}
			}	
		}			
	}
	public static boolean isEmpty(){
		return root == null;
	}
	//Uses recursion to find the maximum value of a subtree
	public static BSTnode findMaximum(BSTnode node)	{
		if(node.above == null){
			return node;
		}else{
			return findMaximum(node.above);
		}
	}

	public static void CountPoints(int x){
		
		Point top, bottom;
		if(isEmpty()){
			return;
		}
		else if(BSTsize == 2){
			if(root.below != null){//The line is below the root
				top = findPoint(root.segment, x);
				bottom = findPoint(root.below.segment,x);
				
				for(int i = (int)bottom.y + 1; i < (int)top.y; i++){
					if(NewPoint(x,i)){
						points[pc] = new Point(x,i);
						pc++;	
					}				
				}				
			}
			else if(root.above != null){
				top = findPoint(root.above.segment, x);
				bottom = findPoint(root.segment,x);
				
				for(int i = (int)bottom.y + 1; i < (int)top.y; i++ ){
					if(NewPoint(x,i)){
						points[pc] = new Point(x,i);
						pc++;	
					}
				}
			}
		}
		else {
			return;
		}
			
	}
	public static Point findPoint(Edge e,int x){ 
		
		Point p;
		int y = (int)e.leftEndpoint().y;
		
		for(int i = (int)e.leftEndpoint().x; i <= x; i++){
			y += (int)e.slope;
		}	
		
		p = new Point(x, y);
		
		return p;
	}
	public static boolean NewPoint(int x, int y){
		for(int i = 0; i < pc; i++){
			if(((int)points[i].x == x )&&((int)points[i].y == y)){
				return false;
			}
		}
		return true;
	}
	public static int getpc(){
		return pc;
	}
	public static void main(String[]args) throws IOException{
		
		File json = new File("/Users/akeemshane/Desktop/inList.json");		
		BufferedReader br = new BufferedReader(new FileReader(json)); 
		String jsonString = "";
		boolean error = false;
		if(br != null){
			jsonString = br.readLine();
		}
		
		StringReader strReader = new StringReader(jsonString);
		JsonReader reader = Json.createReader(strReader);
		JsonArray inArray = testObject(reader);
		
		if(inArray == null){
			error = true;
		}
		else{
			
			Polygon polygon = null;
			try{
			//Instance of polygon object
				polygon = new Polygon(inArray);
			}
			catch(Exception e){
				//polygon = null;
				error = true;
			}
			
			
			if(polygon.verticies != null){
			//Computing largest Possible number of points. 
			int lx, ly;
			lx = ly = 0;			
			for(int i = 0; i < size; i++){
				if(polygon.sides[i].rightEndpoint().x > lx){
					lx = (int)polygon.sides[i].rightEndpoint().x;
				}
				if(polygon.sides[i].higherPoint().y > ly){
					ly = (int)polygon.sides[i].higherPoint().y;
				}
			}
			points = new Point[(lx * ly)];
			
			for(int i = 0; i < 20; i++){
				sweeper.p1.x = sweeper.p2.x = i;
				for(int j = 0; j < size; j++){
					if(Segements_Intersect(sweeper,polygon.sides[j])){
						if(polygon.sides[j].leftEndpoint().x == i){
							//System.out.println(polygon.sides[j].name);
							insert(polygon.sides[j]);
						}
						if(polygon.sides[j].rightEndpoint().x == i){
							//System.out.println(polygon.sides[j].name);
							remove(polygon.sides[j]);
						}
					}
					if(BSTsize > 1){
						CountPoints(i);
					}	
					else if (Polygon.BSTsize > 2){
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
			System.out.println("Bad JSON!");
		}else{
			System.out.println("Count: " + (pc - 1));
		}
		br.close();
	}
}
