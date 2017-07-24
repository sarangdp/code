##Combiner to combine values for total volume and total count
package nyse.avgstockvalpermonth;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

import nyse.keyValues.LongPair;
import nyse.keyValues.TextPair;

public class AvgStockValPerMonthCombiner extends Reducer<TextPair, LongPair, TextPair, LongPair> {

	@Override
	protected void reduce(TextPair key, Iterable<LongPair> values,
			Reducer<TextPair, LongPair, TextPair, LongPair>.Context context) throws IOException, InterruptedException {
		Long totalVolume = (long) 0;
		Long totalRecords = (long) 0;

		for (LongPair longPair : values) {
			totalVolume += longPair.getFirst().get();
			totalRecords += longPair.getSecond().get();
		}

		context.write(key, new LongPair(new LongWritable(totalVolume), new LongWritable(totalRecords)));
	}
}
