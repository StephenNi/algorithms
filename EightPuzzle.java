
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;


class Node {
	public char[] board;
	public int space;
	public Node parent;
	public int dir;
	public int g;
	public int h;
	
	int[][] goal_state = {{0,0}, {0,1}, {0,2},
	        {1,0}, {1,1}, {1,2}, {2,0}, {2,1}, {2,2}};
	
	public Node(char[] board, int space) {
		this.board = board;
		this.space = space;
		parent = null;
		g = Integer.MAX_VALUE;
		h = heuristic2();
	}
	
	public boolean equals(Node n) {
		for (int i = 0; i < board.length; i++)
			if (board[i] != n.board[i])
				return false;
		return true;
	}
	
	/*
	private int heuristic() {
		int count = 0;
		for (int i = 0; i < board.length; i++)
			if (board[i] != '1' + i)
				count++;
		return count;
	}
	*/
	
	private int heuristic2() {
		int hv = 0;
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++) {
				int k = i*3 + j;
				if (board[k] != '9') {
					 hv += Math.abs(i - goal_state[board[k]-'1'][0]) +
		                        Math.abs(j - goal_state[board[k]-'1'][1]);
				}
			}
		return hv;
	}
}

class NodeComparator implements Comparator<Node>
{

	@Override
	public int compare(Node n1, Node n2) {
		return (n1.g + n1.h) - (n2.g + n2.h);
	}
	
}

public class EightPuzzle {

	private static final int[] facs = {1, 1, 2, 6, 24, 120, 720, 5040, 40320};
	private static final int NumStates = 362880;
	private static final int Size = 9;
	private static final int[] xDirs = {-1, 1, 0, 0};
	private static final int[] yDirs = {0, 0, -1, 1};
	private static final char[] dirs = {'u', 'd', 'l', 'r'};
	private static final char[] revDirs = {'d', 'u', 'r', 'l'};
	
	private boolean found;
	private Map<Integer, Node> nodeMap;

	private Node end;
	
	public EightPuzzle() {
		char[] board = new char[Size];
		for (int i = 0; i < board.length; i++)
			board[i] = (char) ('1' + i);
		end = new Node(board, 8);
		
		found = false;
		nodeMap = new HashMap<Integer, Node>();		
		nodeMap.put(order(end), end);
	}
	
	public int order(Node nd) {
		char[] s = nd.board;
		
		int order = 0;
		int n = s.length;
		
		for (int i = 0; i < n - 1; i++) {
			int count = 0;
			
			for (int j = i + 1; j < n; j++)
				if (s[j] < s[i])
					count++;
			order += count * facs[n - i - 1];
		}
		
		return order;
	}
	
	private boolean inside(int x, int y) {
		if (x < 0 || y < 0 || x > 2 || y > 2)
			return false;
		return true;
	}
	
	// BFS
	public Node BFS(Node start) {
		boolean[] visited = new boolean[NumStates];
		Queue<Node> queue = new LinkedList<Node>();
		queue.offer(start);
		visited[order(start)] = true;
		
		while (!queue.isEmpty()) {
			Node n = queue.poll();
			
			if (n.equals(end)) {
				return n;
			}
			
			for (int i = 0; i < xDirs.length; i++) {
				int spaceX = n.space / 3;
				int spaceY = n.space % 3;
				
				spaceX += xDirs[i];
				spaceY += yDirs[i];
				
				if (inside(spaceX, spaceY)) {
					int newSpace = spaceX * 3 + spaceY;
					char[] newBoard = Arrays.copyOf(n.board, Size);
					char temp = newBoard[newSpace];
					newBoard[newSpace] = newBoard[n.space];
					newBoard[n.space] = temp;
					Node ext = new Node(newBoard, newSpace);

					int order = order(ext);
					if (!visited[order]) {
						visited[order] = true;
						ext.parent = n;
						ext.dir = i;
						queue.offer(ext);
					}
				}
			}
		}
		return null;
	}
	
	public void printPath(Node end) {
		if (end.parent != null) {
			printPath(end.parent);
			System.out.print(dirs[end.dir]);
		}
	}
	
	public void printRevPath(Node end) {
		if (end.parent != null) {
			System.out.print(revDirs[end.dir]);
			printRevPath(end.parent);
		}
	}
	
	public void printPath(Node mid, boolean fromStart, Node mirror, int dir) {
		if (fromStart) {
			printPath(mid);
			System.out.print(revDirs[dir]);
			printRevPath(mirror);
		} else {
			printPath(mirror);
			System.out.print(dirs[dir]);
			printRevPath(mid);
		}
	}
	
	// Double BFS
	public void DBFS(Node start) {
		int[] visited = new int[NumStates];
		
		Queue<Node> sq = new LinkedList<Node>();
		sq.offer(start);
		visited[order(start)] = -1;
		
		Queue<Node> tq = new LinkedList<Node>();
		tq.offer(end);
		visited[order(end)] = 1;
		
		while (!sq.isEmpty() && !tq.isEmpty()) {
			if (sq.size() < tq.size()) 
				expand(sq, visited, -1);
			else
				expand(tq, visited, 1);
			if (found)
				return;
		}
		
		while (!sq.isEmpty()) {
			expand(sq, visited, -1);
			if (found)
				return;
		}
		
		while (!tq.isEmpty()) {
			expand(tq, visited, 1);
			if (found)
				return;
		}
	}
	
	private void expand(Queue<Node> queue, int[] visited, int queueType) {
		Node n = queue.poll();
		
		for (int i = 0; i < xDirs.length; i++) {
			int spaceX = n.space / 3;
			int spaceY = n.space % 3;
			
			spaceX += xDirs[i];
			spaceY += yDirs[i];
			
			if (inside(spaceX, spaceY)) {
				int newSpace = spaceX * 3 + spaceY;
				char[] newBoard = Arrays.copyOf(n.board, Size);
				char temp = newBoard[newSpace];
				newBoard[newSpace] = newBoard[n.space];
				newBoard[n.space] = temp;
				Node ext = new Node(newBoard, newSpace);
				
				int order = order(ext);
				
				if (visited[order] == queueType)
					continue;
				
				if (visited[order] == 0) {
					visited[order] = queueType;
					ext.parent = n;
					ext.dir = i;
					queue.offer(ext);
					nodeMap.put(order(ext), ext);
				} else {
					// found the result
					found = true;
					printPath(nodeMap.get(order(ext)), (visited[order] == -1), n, i);
				}
			}
		}
	}
	
	// A*
	public Node aStar(Node start) {
		boolean[] closed = new boolean[NumStates];
		boolean[] open = new boolean[NumStates];
		Node[] nodes = new Node[NumStates];
		
		Queue<Node> queue = new PriorityQueue<Node>(10, new NodeComparator());
		queue.offer(start);
		start.g = 0;
		open[order(start)] = true;
		nodes[order(start)] = start;
		
		while (!queue.isEmpty()) {
			Node n = queue.poll();
			
			if (n.equals(end)) {
				return n;
			}
			
			for (int i = 0; i < xDirs.length; i++) {
				int spaceX = n.space / 3;
				int spaceY = n.space % 3;
				
				spaceX += xDirs[i];
				spaceY += yDirs[i];
				
				if (inside(spaceX, spaceY)) {
					int newSpace = spaceX * 3 + spaceY;
					char[] newBoard = Arrays.copyOf(n.board, Size);
					char temp = newBoard[newSpace];
					newBoard[newSpace] = newBoard[n.space];
					newBoard[n.space] = temp;
					Node ext = new Node(newBoard, newSpace);
					
					int order = order(ext);
					if (open[order]) {
						ext = nodes[order];
						if (n.g + 1 < ext.g) {
							ext.g = n.g + 1;
							ext.parent = n;
							ext.dir = i;
						}
					} else if (!closed[order]) {
						open[order] = true;
						ext.parent = n;
						ext.dir = i;
						ext.g = n.g + 1;
						queue.offer(ext);
						nodes[order] = ext;
					}	
				}
			}
			closed[order(n)] = true;
			open[order(n)] = false;
		}
		return null;
	}
		
	// IDA*
	public List<Integer> idAstar(Node start) {
		int costLimit = start.h;
		List<Integer> path = new ArrayList<Integer>();
		
		while (!found && costLimit < Integer.MAX_VALUE) {
			costLimit = dfs(start, 0, costLimit, path, 10);
		}
		return path;
	}
	
	private int dfs(Node node, int startCost, int costLimit, List<Integer> path, int prevMove) {
		int minCost = startCost + node.h;
		if (minCost > costLimit)
			return minCost;
		
		if (node.equals(end)) {
			found = true;
			return costLimit;
		}
		
		int nextCostLimit = Integer.MAX_VALUE;
		for (int i = 0; i < xDirs.length; i++) {
			if (i + prevMove == 1 || i + prevMove == 5)
				continue;
			int spaceX = node.space / 3;
			int spaceY = node.space % 3;
			
			spaceX += xDirs[i];
			spaceY += yDirs[i];
			
			if (inside(spaceX, spaceY)) {
				int newSpace = spaceX * 3 + spaceY;
				char[] newBoard = Arrays.copyOf(node.board, Size);
				char temp = newBoard[newSpace];
				newBoard[newSpace] = newBoard[node.space];
				newBoard[node.space] = temp;
				Node ext = new Node(newBoard, newSpace);
				
				path.add(i);
				int newCostLimit = dfs(ext, startCost + 1, costLimit, path, i);
				
				if (found)
					return newCostLimit;
				nextCostLimit = Math.min(nextCostLimit, newCostLimit);
				path.remove(path.size() - 1);
			}
		}
		return nextCostLimit;
	}
	
	public void printPath(List<Integer> path) {
		for (int dir : path) {
			System.out.print(dirs[dir]);
		}
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		EightPuzzle ins = new EightPuzzle();
		
		if (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			line = line.trim();
			String[] splits = line.split("\\s+");
			char[] board = new char[Size];
			int space = 0;
			
			for (int i = 0; i < Size; i++)
				if (splits[i].equals("x")) {
					board[i] = '9';
					space = i;
				}
				else
					board[i] = splits[i].charAt(0);
			
			Node start = new Node(board, space);
			ins.nodeMap.put(ins.order(start), start);
			
			/*
			Node end = ins.BFS(start);
			if (end == null)
				System.out.println("unsolvable");
			else
				ins.printPath(end);
			*/
			/*
			ins.DBFS(start);
			if (!ins.found)
				System.out.println("unsolvable");
			*/
			/*
			Node end = ins.aStar(start);
			if (end == null)
				System.out.println("unsolvable");
			else
				ins.printPath(end);
			*/
			List<Integer> path = ins.idAstar(start);
			if (ins.found) {
				ins.printPath(path);
			} else {
				System.out.println("unsolvable");
			}
		}
	}

}
