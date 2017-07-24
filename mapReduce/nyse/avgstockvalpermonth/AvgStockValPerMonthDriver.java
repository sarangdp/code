#Calculate average stock value per month 
##Map output key - TextPair.java
##Map output value - LongPair.java
## Partitioner - SecondKeyTextPairPartitioner
package nyse.avgstoclvalpermonth;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import nyse.keyValues.LongPair;
import nyse.keyValues.TextPair;
import nyse.partitioners.SecondKeyTextPairPartitioner;

public class AvgStockValPerMonthDriver extends Configured implements Tool {

	@Override
	public int run(String[] arg0) throws Exception {
		// TODO Auto-generated method stub
		Job job = Job.getInstance(getConf());
		
		job.setJarByClass(getClass());
		job.setInputFormatClass(TextInputFormat.class);
		
		job.setMapperClass(AvgStockValPerMonthMapper.class);
		job.setMapOutputKeyClass(TextPair.class);
		job.setMapOutputValueClass(LongPair.class);
		
		job.setPartitionerClass(SecondKeyTextPairPartitioner.class);
		job.setCombinerClass(AvgStockValPerMonthCombiner.class);
		job.setReducerClass(AvgStockValPerMonthReducer.class);		
		
		job.setOutputKeyClass(TextPair.class);
		job.setOutputValueClass(LongPair.class);
		
		job.setNumReduceTasks(4);
		
		FileInputFormat.setInputPaths(job, new Path(arg0[0]));
		FileOutputFormat.setOutputPath(job, new Path(arg0[1]));
		
		return job.waitForCompletion(true)? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		System.exit(ToolRunner.run(new AvgStockValPerMonthDriver(), args));
	}

}
