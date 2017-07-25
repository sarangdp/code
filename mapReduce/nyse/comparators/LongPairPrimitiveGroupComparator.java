package nyse.comparators;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import nyse.keyValues.LongPairPrimitive;

public class LongPairPrimitiveGroupComparator extends WritableComparator {
	public LongPairPrimitiveGroupComparator() {
		// TODO Auto-generated constructor stub
		super(LongPairPrimitive.class, true);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		// TODO Auto-generated method stub
		
		LongPairPrimitive first = (LongPairPrimitive) a;
		LongPairPrimitive second = (LongPairPrimitive) b;
		return LongPairPrimitive.compare(first.getFirst(), second.getFirst());
	}
}
