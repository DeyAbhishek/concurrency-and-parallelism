// Please see 1MergeSort.java for complete sample code //



// A Sorter represents a task that can be run in a thread.
// It performs a merge sort on a given array.
// The idea is that the overall parallel merge sort method can create
// several Sorters, each for a given range of the array, and ask them to sort
// different portions of the array in parallel.
// Then it will merge the pieces in a single thread.

public class Sorter implements Runnable {
	private int[] a;
	private int threadCount;
	
	public Sorter(int[] a, int threadCount) {
		this.a = a;
		this.threadCount = threadCount;
	}
	
	public void run() {
		MergeSort.parallelMergeSort(a, threadCount);
	}
}


class MergeSort{	
	public static void parallelMergeSort(int[] a) {
		 int cores = Runtime.getRuntime().availableProcessors();
		//  int cores = 8;
		parallelMergeSort(a, cores);
	}
	
	public static void parallelMergeSort(int[] a, int threadCount) {
		if (threadCount <= 1) {
			mergeSort(a);
		} else if (a.length >= 2) {
			// split array in half
			int[] left  = Arrays.copyOfRange(a, 0, a.length / 2);
			int[] right = Arrays.copyOfRange(a, a.length / 2, a.length);
			
			// sort the halves
			// mergeSort(left);
			// mergeSort(right);
			Thread lThread = new Thread(new Sorter(left,  threadCount / 2));
			Thread rThread = new Thread(new Sorter(right, threadCount / 2));
			lThread.start();
			rThread.start();
			
			try {
				lThread.join();
				rThread.join();
			} catch (InterruptedException ie) {}
			
			// merge them back together
			merge(left, right, a);
		}
	}
	
	// Arranges the elements of the given array into sorted order
	// using the "merge sort" algorithm, which splits the array in half,
	// recursively sorts the halves, then merges the sorted halves.
	// It is O(N log N) for all inputs.
	public static void mergeSort(int[] a) {
		if (a.length >= 2) {
			// split array in half
			int[] left  = Arrays.copyOfRange(a, 0, a.length / 2);
			int[] right = Arrays.copyOfRange(a, a.length / 2, a.length);
			
			// sort the halves
			mergeSort(left);
			mergeSort(right);
			
			// merge them back together
			merge(left, right, a);
		}
	}
	
	// Combines the contents of sorted left/right arrays into output array a.
	// Assumes that left.length + right.length == a.length.
	public static void merge(int[] left, int[] right, int[] a) {
		int i1 = 0;
		int i2 = 0;
		for (int i = 0; i < a.length; i++) {
			if (i2 >= right.length || (i1 < left.length && left[i1] < right[i2])) {
				a[i] = left[i1];
				i1++;
			} else {
				a[i] = right[i2];
				i2++;
			}
		}
	}
}
