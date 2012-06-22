import java.util.*;

public class mergeSort{
	static void mergeSort(ArrayList <Comparable> A){//Recursive mergeSort
		if (A.size()==2){
			if((A.get(1).compareTo(A.get(0))<0)){
				Comparable temp = A.get(1);
				A.set(1,A.get(0));
				A.set(0,temp);
			}
		}else if(A.size()>2){ // recursion, divide list into two halves
			int i = (int)(A.size()/2.0);
			ArrayList<Comparable> B=new ArrayList(0);
			ArrayList<Comparable> C = new ArrayList(0);
			for(int u=0;u<A.size();u++){
				if(u<i) B.add(A.get(u));
				else C.add(A.get(u));
			}
			mergeSort(B);
			mergeSort(C);
			A.clear();
			merge(B,C,A);
		}
	}
	static void merge (ArrayList <Comparable> A, ArrayList <Comparable> B, ArrayList<Comparable> C) {
 		int a=0;int b=0;
 		while(a<A.size()&&b<B.size()){
 			if(A.get(a).compareTo(B.get(b))<=0){
 				C.add(A.get(a));
 				a++;
 			}
 			else{
 				C.add(B.get(b));
 				b++;
 			}
 		}
 		for(int i=a;i<A.size();i++) C.add(A.get(i));
 		for(int i=b;i<B.size();i++) C.add(B.get(i));
 	}
	
	
}