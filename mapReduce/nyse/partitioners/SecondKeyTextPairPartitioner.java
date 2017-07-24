package nyse.partitioners;

import org.apache.hadoop.mapreduce.Partitioner;

import nyse.keyValues.LongPair;
import nyse.keyValues.TextPair;

public class SecondKeyTextPairPartitioner extends Partitioner<TextPair, LongPair> {

	@Override
	public int getPartition(TextPair key, LongPair value, int numPartitions) {
		int partitonValue = 0;
		
		partitonValue = (key.getSecond().hashCode() & Integer.MAX_VALUE) % numPartitions;
		return partitonValue ;
	}

}
