##Output key - TradeMonth-StockName
##Output value - Volume-count(hardcoded to 1)

package nyse.avgstockvalpermonth;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import nyse.keyValues.LongPair;
import nyse.keyValues.TextPair;
import nyse.parsers.NYSEParser;

public class AvgStockValPerMonthMapper extends Mapper<LongWritable, Text, TextPair, LongPair> {
	
	private static NYSEParser parser = new NYSEParser();
	private static TextPair outputMapKey = new TextPair();
	private static LongPair outputMapValue = new LongPair();
   
	@Override
    protected void map(LongWritable rowOffSet, Text record, Mapper<LongWritable, Text, TextPair, LongPair>.Context context)
    		throws IOException, InterruptedException {
    	
		parser.parseRecord(record.toString());
		
		outputMapKey.setFirst(new Text(parser.getTradeMonth()));
		outputMapKey.setSecond(new Text(parser.getStockTicker()));		
		
		outputMapValue.setFirst(new LongWritable(parser.getVolume()));
		outputMapValue.setSecond(new LongWritable(1));
		
		context.write(outputMapKey, outputMapValue);
    }
	
}
