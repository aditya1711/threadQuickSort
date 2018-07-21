package quickSortThreadImplementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MainThreadRunnable implements Callable<Integer[]> {

	
	static ExecutorService pool = Executors.newCachedThreadPool();
	//static ExecutorService pool = Executors.newFixedThreadPool(50);
	
	Integer arr[];
	int low;
	int high;
	
	public MainThreadRunnable(Integer a[], int l, int h){
		arr=a;
		low=l;
		high=h;
	}
	
	public Integer[] call(){
		return sort();
	}
	/* The main function that implements QuickSort()
   arr[] --> Array to be sorted,
   low  --> Starting index,
   high  --> Ending index */
	public Integer[] sort()
	{	//System.out.println("sort() entered with arr: " + Arrays.toString(arr));
		//Integer result[] = new Integer[arr.length];
		
		/*if(high-low <100000){
			Arrays.sort(arr);
			return arr;
		}*/
		
		if (low <= high)
		{
			/*FutureTask partitionThreads = new FutureTask(new PartitionThreadJob(arr,low,high));
			pool.execute(partitionThreads);
			while (true) {
				if (partitionThreads.isDone()) {
					
				} 
			}*/
			
			/* pi is partitioning index, arr[pi] is 
	           now at right place */
			int pi = partition();
			
			
			/*Integer[] a = new Integer[pi];
			Integer[] b = new Integer[high-pi];
			System.arraycopy(arr, 0, a, 0, pi);
			System.arraycopy(arr, pi+1, b, 0, high-pi);*/
			
			FutureTask sortThread1 = new FutureTask(new MainThreadRunnable(arr,low,pi-1));
			FutureTask sortThread2 = new FutureTask(new MainThreadRunnable(arr,pi+1,high));
	
			pool.execute(sortThread1);
			pool.execute(sortThread2);
			
			/*while (true) {
				if (sortThread1.isDone() && sortThread2.isDone()) {
					//System.out.println("Sorting threads for " + Arrays.toString(arr) + " done");
					try {
						a = (Integer[]) sortThread1.get();
						b = (Integer[]) sortThread2.get();
						
						//copy first half
						System.arraycopy(a,0,result,0,a.length);
						//copy pivot element
						System.arraycopy(arr, pi, result, a.length, 1);
						//copy second half
						System.arraycopy(b,0,result,a.length+1,b.length);
						
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					break;
				}
			}*/
		}
		return arr;
	}
	
	public Integer partition()
	{	
		 /* This function takes last element as pivot,
	    places the pivot element at its correct
	    position in sorted array, and places all
	    smaller (smaller than pivot) to left of
	    pivot and all greater elements to right
	    of pivot */
		
		synchronized (arr) {
			int pivot = arr[high];
			int i = (low - 1); // index of smaller element
			for (int j = low; j < high; j++) {
				// If current element is smaller than or
				// equal to pivot
				if (arr[j] <= pivot) {
					i++;

					// swap arr[i] and arr[j]
					int temp = arr[i];
					arr[i] = arr[j];
					arr[j] = temp;
				}
			}
			// swap arr[i+1] and arr[high] (or pivot)
			int temp = arr[i + 1];
			arr[i + 1] = arr[high];
			arr[high] = temp;
			return i + 1;
		}
	}

	/* A utility function to print array of size n */

	// Driver program
	public static void main(String args[])
	{	
		//Integer arr[] = {10, 7, 8, 9, 1, 5,15,3,6,9,4651,6484,64,64,84,684,654,684,684,684,684,84,654,684,84,86974,974,6879,7,984,984,6849,46,84};
		Integer[] arr = generateRandomList(15);
		int n = arr.length;
		/*Integer[] arr2 = new Integer[n];
		System.arraycopy(arr, 0, arr2, 0, n);*/
		
		System.out.println(Arrays.toString(arr));
		
		FutureTask sortThread = new FutureTask(new MainThreadRunnable(arr,0,n-1));
		long threadSortStartTime = System.currentTimeMillis();
		pool.execute(sortThread);
		long threadSortEndTime;
		while (true) {
			if (sortThread.isDone()) {
				threadSortEndTime = System.currentTimeMillis();
				System.out.println("time by thread:\t" + (threadSortEndTime- threadSortStartTime));
				try {
					//System.out.println("sorted array");
					//Integer[] result = (Integer[]) sortThread.get();
					System.out.println(Arrays.toString(arr));
					//System.out.println("length: " + result.length);
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("exception main");
					e.printStackTrace();
				}
				break;
			} 
		}
		pool.shutdown();
		
		/*long normalQuickSortStartTime = System.currentTimeMillis();
		//System.out.println("array enterd\n" + Arrays.toString(arr));
		Arrays.sort(arr2);
		long normalQuickSortEndTime = System.currentTimeMillis();
		System.out.println("Time by normal:\t" + (normalQuickSortEndTime- normalQuickSortStartTime));*/
	}
	
	
	public static Integer[] generateRandomList(int N){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i=0;i<N;i++){
			list.add((int)(Math.random()*N));
		}
		return list.toArray(new Integer[N]);
	}

}
