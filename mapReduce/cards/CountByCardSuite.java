## Use deckOfCards.txt as input file
## Program to calculate nmber of cards per suite.
## Reducer and combiner classes - LongSumReducer
package cards;

import java.io.IOException;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class CountByCardSuite extends Configured implements Tool {
	
	private static class CountByCardSuitMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
	@Override
		protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, LongWritable>.Context context)
				throws IOException, InterruptedException {
			context.write(new Text(value.toString().split("\\|")[1]), new LongWritable(1));
		}	
	}

	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Job job = Job.getInstance(getConf());
		job.setJarByClass(getClass());
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.setMapperClass(CountByCardSuitMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);
		
		job.setCombinerClass(LongSumReducer.class);
		job.setReducerClass(LongSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		
		job.setPartitionerClass(HashPartitioner.class);
		job.setNumReduceTasks(2);
		return job.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.exit(ToolRunner.run(new CountByCardSuite(), args));
	}

}
