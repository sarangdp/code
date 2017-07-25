package nyse.comparators;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import nyse.keyValues.LongPairPrimitive;

public class LongPairPrimitiveSortingComparator extends WritableComparator {
	public LongPairPrimitiveSortingComparator() {
		// TODO Auto-generated constructor stub
		super(LongPairPrimitive.class,  true);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		// TODO Auto-generated method stub
		LongPairPrimitive first = (LongPairPrimitive) a;
		LongPairPrimitive second = (LongPairPrimitive) b;
		
		int cmp = LongPairPrimitive.compare(first.getFirst(), second.getFirst());
		
		if(cmp==0)
			cmp = -LongPairPrimitive.compare(first.getSecond(), second.getSecond());
		
		return cmp;
	}
}
