
public class FindMaximumProduct {

	public int maxProduct(int[] arr) {
		// ignore overflow now
		int max, posMax, negMax;
		max = posMax = negMax = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] > 0) {
				posMax = Math.max(arr[i], posMax*arr[i]);
				negMax = Math.min(arr[i]*negMax, 0);
			} else if (arr[i] < 0) {
				int temp = negMax;
				negMax = Math.min(arr[i], arr[i]*posMax);
				posMax = Math.max(arr[i]*temp, 0);
			} else {
				posMax = negMax = 0;
			}
			if (posMax > max)
				max = posMax;
		}
		return max;
	}

	public static void main(String[] args) {
		FindMaximumProduct ins = new FindMaximumProduct();
		int[] arr = {12, 2, -3, -5, -6, -2};
		System.out.println(ins.maxProduct(arr));
	}

}
