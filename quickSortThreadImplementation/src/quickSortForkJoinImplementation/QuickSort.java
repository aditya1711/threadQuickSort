package quickSortForkJoinImplementation;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RecursiveAction;
//import quickSortThreadImplementation.*;

import quickSortThreadImplementation.MainThreadRunnable;

public class QuickSort extends RecursiveAction{
	
	Integer[] arr;
	int low,high;
	
	public QuickSort(Integer[] arr, int l, int r){
		this.arr=arr;
		low = l;
		high=r;
	}
	
	@Override
	protected void compute() {
		// TODO Auto-generated method stub
		if(high-low<1000){
			sortDirectly(low,high);
		}
		else{
			sort();
		}
		//sort();
	}
	
	public void sortDirectly(int low,int high){
		if (low<high) {
			int pi = partition(arr, low, high);
			sortDirectly(low, pi - 1);
			sortDirectly(pi + 1, high);
		}
		
	}
	
	public void sort()
	{	//System.out.println("sort() entered with arr: " + Arrays.toString(arr));
		//Integer result[] = new Integer[arr.length];
		
		if (low < high)
		{
			int pi = partition(arr, low, high);	
			invokeAll(new QuickSort(arr,low,pi-1) , new QuickSort(arr,pi+1,high));
		}
	}
	
	public Integer partition(Integer arr[], int low, int high)
	{	
		Integer pivot = arr[high];
		Integer i = (low-1); // index of smaller element
		for (int j=low; j<high; j++)
		{
			// If current element is smaller than or
			// equal to pivot
			if (arr[j] <= pivot)
			{
				i++;

				// swap arr[i] and arr[j]
				Integer temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}

		// swap arr[i+1] and arr[high] (or pivot)
		Integer temp = arr[i+1];
		arr[i+1] = arr[high];
		arr[high] = temp;
		
		return i+1;
	}

	public static void main(String args[])
	{	
		//Integer arr[] = {10, 7, 8, 9, 1, 5,15,3,6,9,4651,6484,64,64,84,684,654,684,684,684,684,84,654,684,84,86974,974,6879,7,984,984,6849,46,84};
		Integer[] arr = generateRandomList(5000000);
		int n = arr.length;
		
		long threadSortStartTime = System.currentTimeMillis();
		ForkJoinPool fp = new ForkJoinPool();
		fp.invoke(new QuickSort(arr,0,n-1));
		long threadSortEndTime = System.currentTimeMillis();
		//System.out.println(Arrays.toString(arr));
		System.out.println("time by thread:\t" + (threadSortEndTime- threadSortStartTime));
		
		
		/*Integer[] arr2 = new Integer[n];
		System.arraycopy(arr, 0, arr2, 0, n);
		long normalQuickSortStartTime = System.currentTimeMillis();
		//new QuickSort(arr2,0,n-1).sortDirectly(0,n-1);
		long normalQuickSortEndTime = System.currentTimeMillis();
		//System.out.println(Arrays.toString(arr2));
		System.out.println("Time by normal:\t" + (normalQuickSortEndTime- normalQuickSortStartTime));
		*/
		
		/*
		Integer[] arr3 = new Integer[n];
		System.arraycopy(arr, 0, arr3, 0, n);
		//ArrayList<Integer> list = new ArrayList<Integer>(Arrays.asList(arr3));
		long inBuiltQuickSortStartTime = System.currentTimeMillis();
		//Collections.sort(list);
		Arrays.parallelSort(arr3);
		//Arrays.sort(arr3);
		long inBuiltQuickSortEndTime = System.currentTimeMillis();
		//System.out.println(list);
		System.out.println("Time by normal:\t" + (inBuiltQuickSortEndTime- inBuiltQuickSortStartTime));
		*/
	}
	
	
	public static Integer[] generateRandomList(int N){
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i=0;i<N;i++){
			list.add((int)(Math.random()*N));
		}
		return list.toArray(new Integer[N]);
	}
}
