public class FindMinimum {
	public int find(int[] arr) {
		return findInternal(arr, 0, arr.length - 1);
	}

	private int findInternal(int[] arr, int l, int r) {
		int m = l + ((r-l)>>1);
		if (arr[m] > arr[r]) {
			return findInternal(arr, m+1, r);
		} else if (arr[l] > arr[m]) {
			return findInternal(arr, l+1, m);
		} else {
			return arr[l];
		}
	}

	public static void main(String[] args) {
		FindMinimum ins = new FindMinimum();
		int[] arr = {5,6,7,1,2,3,4};
		System.out.println(ins.find(arr));
	}
}
