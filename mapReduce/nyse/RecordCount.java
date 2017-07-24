# Simple program to count number of lines in the file

package nyse;

import java.io.IOException;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class RecordCount extends Configured implements Tool{
	
	public static class RecordMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
		
		@Override
		protected void map(LongWritable lineOffset, Text record, Mapper<LongWritable, Text, Text, LongWritable>.Context context)
				throws IOException, InterruptedException {
			// TODO Auto-generated method stub
			context.write(new Text("Count"), new LongWritable(1));
		}
	}
	
	public static void main(String[] args) throws Exception {
		System.exit(ToolRunner.run(new RecordCount(), args));
	}

	@Override
	public int run(String[] arg0) throws Exception {
		// TODO Auto-generated method stub
		Job job = Job.getInstance(getConf());
		
		job.setJarByClass(getClass());
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		job.setMapperClass(RecordMapper.class);		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);
		
		job.setReducerClass(LongSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		
		job.setNumReduceTasks(1);
		
		FileInputFormat.setInputPaths(job, new Path(arg0[0]));
		FileOutputFormat.setOutputPath(job, new Path(arg0[1]));
		return job.waitForCompletion(true)? 0 : 1;
	}
}
