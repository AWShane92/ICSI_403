package Project2;

public class Node {
	
	public String cmd;
	public String name;
	public int pri;
	public Node next;
	
	//Main Constructor
	public Node(String command, String name, int priority){
		this.cmd = command;
		this.name = name;
		this.pri = priority;
		this.next = null;
	}
}
