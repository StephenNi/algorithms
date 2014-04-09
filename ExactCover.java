import java.util.HashMap;
import java.util.Map;


public class ExactCover {
	
	class Node {
		Node left;
		Node right;
		Node up;
		Node down;
		Node col;
		
		int size;
		char name;
		
		public Node() {
			left = right = this;
			up = down = this;
			col = this;
		}
	}
	
	private Node header;
	
	public void buildDancingLinks(int[][] matrix) {
		if (matrix.length == 0) return;
		
		header = new Node();		
		solution = new Node[matrix.length];
		Map<Integer, Node> nodeMap = new HashMap<Integer, Node>();

		char begin = 'A';
		int rows = matrix.length;
		int cols = matrix[0].length;
		
		for (int j = 0; j < cols; j++) {
			Node columnHeader = new Node();
			Node last = header.left;
			
			last.right = columnHeader;
			columnHeader.left = last;
			
			columnHeader.right = header;
			header.left = columnHeader;
			
			int count = 0;
			for (int i = 0; i < rows; i++) {
				if (matrix[i][j] == 1) {
					Node node = new Node();
					last = columnHeader.up;
					
					last.down = node;
					node.up = last;
					
					node.down = columnHeader;
					columnHeader.up = node;
					
					node.col = columnHeader;
					count++;
					nodeMap.put(i*cols + j, node);
				}
			}
			columnHeader.size = count;
			columnHeader.name = (char) (begin + j);
		}
		
		for (int i = 0; i < rows; i++) {
			Node rowHeader = null;
			for (int j = 0; j < cols; j++) {
				Node node = nodeMap.get(i*cols + j);
				if (node != null) {
					if (rowHeader == null)
						rowHeader = node;
					else {
						Node last = rowHeader.left;
						
						last.right = node;
						node.left = last;
						
						node.right = rowHeader;
						rowHeader.left = node;
					}
				}
			}
		}
	}
	
	private Node findLeastNode() {
		Node iter = header.right;
		Node res = null;
		int min = Integer.MAX_VALUE;
		while (iter != header) {
			if (iter.size < min) {
				min = iter.size;
				res = iter;
			}
			iter = iter.right;
		}
		return res;
	}
	
	private Node[] solution;
	
	public void search(int k) {
		Node columnHeader = findLeastNode();
		if (columnHeader == null) {
			// find a solution
			printSolution(k);
			return;
		}
		
		if (columnHeader.size == 0) {
			// no solution in this branch
			return;
		}
		
		cover(columnHeader);
		
		for (Node iter = columnHeader.down; iter != columnHeader; iter = iter.down) {
			solution[k] = iter;
			for (Node rowNode = iter.right; rowNode != iter; rowNode = rowNode.right)
				cover(rowNode.col);
			search(k + 1);
			for (Node rowNode = iter.left; rowNode != iter; rowNode = rowNode.left)
				unCover(rowNode.col);
		}
		
		unCover(columnHeader);
	}
	
	private void cover(Node columnHeader) {
		columnHeader.right.left = columnHeader.left;
		columnHeader.left.right = columnHeader.right;
		
		for (Node iter = columnHeader.down; iter != columnHeader; iter = iter.down)
			for (Node rowNode = iter.right; rowNode != iter; rowNode = rowNode.right) {
				rowNode.down.up = rowNode.up;
				rowNode.up.down = rowNode.down;
				rowNode.col.size -= 1;
			}
	}
	
	private void unCover(Node columnHeader) {
		for (Node iter = columnHeader.up; iter != columnHeader; iter = iter.up)
			for (Node rowNode = iter.left; rowNode != iter; rowNode = rowNode.left) {
				rowNode.col.size += 1;
				rowNode.down.up = rowNode;
				rowNode.up.down = rowNode;
			}
		columnHeader.right.left = columnHeader;
		columnHeader.left.right = columnHeader;
	}
	
	public void printSolution(int len) {
		for (int i = 0; i < len; i++) {
			Node rowNode = solution[i];
			System.out.print(rowNode.col.name + " ");
			for (Node iter = rowNode.right; iter != rowNode; iter = iter.right)
				System.out.print(iter.col.name + " ");
			System.out.println();
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		int[][] matrix = {
				{0, 0, 1, 0, 1, 1, 0},
				{1, 0, 0, 1, 0, 0, 1},
				{0, 1, 1, 0, 0, 1, 0},
				{1, 0, 0, 1, 0, 0, 0},
				{0, 1, 0, 0, 0, 0, 1},
				{0, 0, 0, 1, 1, 0, 1}
		};
		
		ExactCover ins = new ExactCover();
		ins.buildDancingLinks(matrix);
		ins.search(0);
	}

}
