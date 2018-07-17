package quickSortThreadImplementation;

import java.util.concurrent.Callable;

public class PartitionThreadJob implements Callable<Integer>{
	int arr[];
	int low;
	int high;
	
	public PartitionThreadJob(int a[], int l, int h){
		arr=a;
		low=l;
		high=h;
	}
	public Integer call(){
		return partition();
	}
	public Integer partition()
	{	
		 /* This function takes last element as pivot,
	    places the pivot element at its correct
	    position in sorted array, and places all
	    smaller (smaller than pivot) to left of
	    pivot and all greater elements to right
	    of pivot */
		
		int pivot = arr[high]; 
		int i = (low-1); // index of smaller element
		for (int j=low; j<high; j++)
		{
			// If current element is smaller than or
			// equal to pivot
			if (arr[j] <= pivot)
			{
				i++;

				// swap arr[i] and arr[j]
				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
			}
		}

		// swap arr[i+1] and arr[high] (or pivot)
		int temp = arr[i+1];
		arr[i+1] = arr[high];
		arr[high] = temp;
		return i+1;
	}
}
