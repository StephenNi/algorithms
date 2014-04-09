public class UglyNumber {

	private boolean isUgly(int num) {
		while (true) {
			if (num == 1) return true;
			if (num % 5 == 0) num /= 5;
			else if (num % 3 == 0) num /= 3;
			else if (num % 2 == 0) num /= 2;
			else return false;
		}
	}

	public void printNth(int n) {
		int start = 1;
		int count = 0;
		while (true) {
			if (isUgly(start)) count++;
			if (count == n) {
				System.out.println(start);
				break;
			}
			start++;
		}
	}

	public void printNthDP(int n) {
		boolean[] dp = new boolean[10000];
		dp[1] = true;
		for (int i = 1; i < dp.length; i++) {
			if (i % 2 == 0 && dp[i/2])
				dp[i] = true;
			if (i % 3 == 0 && dp[i/3])
				dp[i] = true;
			if (i % 5 == 0 && dp[i/5])
				dp[i] = true;
		}
		int i, count;
		for (i = 1, count = 0; i < dp.length && count < n; i++) {
			if (dp[i])
				count++;
		}
		System.out.println(i-1);
	}	

	public void printNthLinear(int n) {
		int[] dp = new int[n];
		dp[0] = 1;
		int i2 = 0, i3 = 0, i5 = 0;
		
		for (int i = 1; i < n; i++) {
			int next = Math.min(dp[i2]*2, dp[i3]*3);
			next = Math.min(next, dp[i5]*5);

			if (next == dp[i2]*2)
				i2++;
			if (next == dp[i3]*3)
				i3++;
			if (next == dp[i5]*5)
				i5++;
			dp[i] = next;
		}
		System.out.println(dp[n-1]);
	}

	public static void main(String[] args) {
		UglyNumber ins = new UglyNumber();
		ins.printNthLinear(150);
	}
}
