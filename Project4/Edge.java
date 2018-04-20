package Project4;

public class Edge {
	
	public String name;
	public Point p1;
	public Point p2;
	public double slope;
	
	public Edge(int name,Point v1, Point v2){
		this.name = Integer.toString(name);
		this.p1 = v1;
		this.p2 = v2;
		
		if(v1.x != v2.x){
			this.slope = (v2.y - v1.y)/(v2.x - v1.x);
		}
		else{
			this.slope = 0;
		}	
	}
	public Edge(String name,Point v1, Point v2){
		this.name = name;
		this.p1 = v1;
		this.p2 = v2;
		
		if(v1.x != v2.x){
			this.slope = (v2.y - v1.y)/(v2.x - v1.x);
		}
		else{
			this.slope = 0;
		}	
	}
	
	public Point leftEndpoint(){
		
		if(p1.x > p2.x){
			return this.p2;
		}
		else{
			return this.p1;
		}
	}
	
	public Point rightEndpoint(){
		
		if(p1.x > p2.x){
			return this.p1;
		}
		else{
			return this.p2;
		}
	}
	
	public Point higherPoint(){
		if(p1.y > p2.y){
			return p1;
		}
		else{
			return p2;
		}
	}
}
