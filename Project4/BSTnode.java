package Project4;

public class BSTnode {
	
	public Edge segment;
	public BSTnode above;
	public BSTnode below;
	public BSTnode prev;
	
	public BSTnode(Edge e){
		this.segment = e;
		this.prev = null;
		this.above = null;
		this.below = null;
	}
	
}
