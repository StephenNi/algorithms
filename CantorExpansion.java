
public class CantorExpansion {
	
	private static final int[] facs = {1, 1, 2, 6, 24, 120, 720, 5040, 40320};
	
	public long order(String s) {
		int order = 0;
		int n = s.length();
		
		for (int i = 0; i < n - 1; i++) {
			int count = 0;
			
			for (int j = i + 1; j < n; j++)
				if (s.charAt(j) < s.charAt(i))
					count++;
			order += count * facs[n - i - 1];
		}
		
		return order;
	}
	
	public String revOrder(long order, int dim) {
		int[] res = new int[dim];
		boolean[] used = new boolean[dim];
		
		for (int i = 0; i < dim; i++) {
			long div = order / facs[dim - i - 1];
			order -= div * facs[dim - i - 1];
			
			int j, count;
			for (j = 0, count = 0; j < dim && count < div + 1; j++) {
				if (!used[j]) 
					count++;
			}
			used[j - 1] = true;
			res[i] = j;
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < res.length; i++)
			sb.append(res[i]);
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		CantorExpansion ins = new CantorExpansion();
		System.out.println(ins.revOrder(15, 5));
	}

}
