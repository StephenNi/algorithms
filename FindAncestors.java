import java.util.*;

class Node {
	int key;
	Node left;
	Node right;
	boolean visited;

	public Node(int key) {
		this.key = key;
		this.left = null;
		this.right = null;
		this.visited = false;
	}
}

public class FindAncestors {
	
	public boolean printAncestors(Node root, int key) {
		if (root != null) {
			boolean leftFound = printAncestors(root.left, key);
			boolean rightFound = printAncestors(root.right, key);
			
			if (leftFound || rightFound) {
				System.out.print(root.key + " ");
				return true;
			}
			return (root.key == key);
		} else {
			return false;
		}
	}

	public void printAncestorsWithStack(Node root, int key) {
		if (root == null)
			return;
		Stack<Node> stack = new Stack<Node>();
		stack.push(root);
			
		while (!stack.isEmpty()) {
			Node node = stack.peek();
			if (node.left != null && !node.left.visited)
				stack.push(node.left);
			else if (node.right != null && !node.right.visited)
				stack.push(node.right);
			else {
				node.visited = true;
				stack.pop();
				if (node.key == key) {
					while (!stack.isEmpty()) {
						Node ans = stack.pop();
						System.out.print(ans.key + " ");
					}
					break;
				}
			}
		}
	}

	public static void main(String[] args) {
		FindAncestors ins = new FindAncestors();
		System.out.println("Input key      List of Ancestors");
		System.out.println("-------------------------------");
		for (int i = 1; i <= 10; i++) {
			Node root = new Node(1);
			root.left = new Node(2);
			root.right = new Node(3);
			root.left.left = new Node(4);
			root.left.right = new Node(5);
			root.right.left = new Node(6);
			root.right.right = new Node(7);
			root.left.left.left = new Node(8);
			root.left.right.right = new Node(9);
			root.right.right.left = new Node(10);
			System.out.print(i + "           ");
			ins.printAncestors(root, i);
			System.out.println();
		}
	}
}
