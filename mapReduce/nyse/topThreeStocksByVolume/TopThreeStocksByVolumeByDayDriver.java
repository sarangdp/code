package nyse.topThreeStocksByVolume;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import nyse.comparators.LongPairPrimitiveGroupComparator;
import nyse.comparators.LongPairPrimitiveSortingComparator;
import nyse.keyValues.LongPairPrimitive;
import nyse.partitioners.FirstKeyLongPartitioner;

public class TopThreeStocksByVolumeByDayDriver extends Configured implements Tool {

	@Override
	public int run(String[] arg0) throws Exception {
		Job job = Job.getInstance(getConf());
		
		job.setJarByClass(getClass());
		
		FileInputFormat.setInputPaths(job, new Path(arg0[0]));
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		job.setMapperClass(TopThreeStocksByVolumeByDayMapper.class);
		job.setMapOutputKeyClass(LongPairPrimitive.class);
		job.setMapOutputValueClass(Text.class);		
		
		job.setPartitionerClass(FirstKeyLongPartitioner.class);
		job.setGroupingComparatorClass(LongPairPrimitiveGroupComparator.class);
		job.setSortComparatorClass(LongPairPrimitiveSortingComparator.class);
		
		job.setNumReduceTasks(6);
		job.setReducerClass(TopThreeStocksByVolumeByDayReducer.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
		FileOutputFormat.setOutputPath(job, new Path(arg0[1]));
		
		return job.waitForCompletion(true) ? 0 : 1;
	}
	
	public static void main(String[] args) throws Exception {
		System.exit(ToolRunner.run(new TopThreeStocksByVolumeByDayDriver(), args));
	}

}
