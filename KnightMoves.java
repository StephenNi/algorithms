import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class Point {
	int x;
	int y;
	int dis;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
		this.dis = 0;
	}
	
	public boolean equals(Point p) {
		return (p.x == this.x && p.y == this.y);
	}
}

public class KnightMoves {
	private int[] xDirs = {-1, -1, 1, 1, -2, -2, 2, 2};
	private int[] yDirs = {-2, 2, -2, 2, -1, 1, -1, 1};
	private static final int DIM = 8;
	
	private boolean inside(Point p) {
		if (p.x < 0 || p.y < 0 || p.x >= 8 || p.y >= 8)
			return false;
		return true;
	}
	
	public void pathSearch(String start, String end, Point s, Point t) {
		boolean[][] visited = new boolean[DIM][DIM];
		Queue<Point> queue = new LinkedList<Point>();
		queue.offer(s);
		visited[s.x][s.y] = true;
		
		while (!queue.isEmpty()) {
			Point p = queue.poll();
			if (p.equals(t)) {
				System.out.println("To get from " + start + " to " + end + " takes " + p.dis + " knight moves.");
				return;
			}
			for (int i = 0; i < xDirs.length; i++) {
				Point ext = new Point(p.x + xDirs[i], p.y + yDirs[i]);
				if (inside(ext) && !visited[ext.x][ext.y]) {
					queue.offer(ext);
					visited[ext.x][ext.y] = true;
					ext.dis = p.dis + 1;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		KnightMoves ins = new KnightMoves();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			Point s = new Point(line.charAt(1) - '1', line.charAt(0) - 'a');
			Point t = new Point(line.charAt(4) - '1', line.charAt(3) - 'a');
			ins.pathSearch(line.substring(0, 2), line.substring(3, 5), s, t);
		}
	}

}
