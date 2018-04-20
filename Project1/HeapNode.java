package Project1;

import javax.json.*;

public class HeapNode {

	//variables for heap
	public static HeapNode[] heap;
	public int key;
	
	public static int maxSize;
	public static int currentSize = 0;
	
	//Constructors
	public HeapNode(){
		//Empty to use for call. 
	}
	public HeapNode(int value){		
		this.key = value;	
	}
	public static int getSize(){
		return maxSize;
	}
	
	public HeapNode[] createHeap(JsonArray inArray){	
		currentSize = inArray.size();
		maxSize = currentSize + 1;
		heap = new HeapNode[maxSize];
		
		for( int i = 0; i < currentSize; i++){			
			heap[(i + 1)] = new HeapNode(inArray.getInt(i));			
		}		
		//buildMaxHeap(heap);
		return heap;
	}
	//
	public static void buildMaxHeap(HeapNode[] hna){		
		int i = (maxSize - 1)/2;	
			for( ;i > 0; i-- ){
				maxHeapify(heap,i);
			}
	}
	//This method uses the trickle down technique to compare which values
	//Are larger in the heap.
	public static void maxHeapify(HeapNode[] hna, int i){
		
		int left = 2 * i;//Left child
		int right = (2 * i) + 1;//Right child
		int largest = i;//Parent Tree
		
		if((left < maxSize)&&(hna[left].key > hna[largest].key)){
			//Left child is larger than the parent.
			largest = left;	
		}
		if((right < maxSize)&&(hna[right].key > hna[largest].key)){
			//Right child is now the largest value.
			largest = right;			
		}
		//If the parent isn't the largest value then swap the parent with the largest child
		//Then move down the heap and compare again. 
		if(largest != i){
			int temp = hna[i].key;
			hna[i].key = hna[largest].key;
			hna[largest].key = temp;
			maxHeapify(hna,largest);
		}		
	}
	
	//Main sorting algorithm.
	public static void heapSort(HeapNode[] hna){
		int temp;//to hold the key values.
		buildMaxHeap(hna);//rebuilds heap upon each call.
		int i = currentSize;
		//Swaps the root for the last element in the heap 
		//Then decreases the heap size by one. 
		for(; i > 1; i--){
			temp = hna[1].key;
			hna[1].key = hna[i].key;
			hna[i].key = temp;
			--maxSize;
			maxHeapify(hna,1);
		}	
		//Resets the heap size.
		maxSize = currentSize + 1;
	}
	//Used for testing. 
	public void printHeap(HeapNode[] hna){
		System.out.print("HeapArray:");
		for(int i = 1; i < maxSize; i++){
			System.out.print(" "+hna[i].key);
		}		
		System.out.println(".");
	}
}
