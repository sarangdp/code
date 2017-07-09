//Word Count Program

 lines = load '/user/sarangdp/wordCount.txt' using PigStorage() AS (line:chararray);
 words = FOREACH lines GENERATE FLATTEN(TOKENIZE(line)) AS word;
grouped = GROUP words BY word;
wordCount = FOREACH grouped GENERATE group, COUNT(words);
dump wordCount;

//store above code in pig_demo.pig
pig -file pig_demo.pig //from gateway command line
exec pig_demo.pig //from pig command line i.e. grunt

/********Loading data without schema***************/
departments = load '/user/sarangdp/sqoop_import/departments' using PigStorage(',');
//reading data
departmentIds = FOREACH departments generate $0;
grunt> describe departmentIds
departmentIds: {bytearray} //default bytearray

//casting the bytearrays
grunt>  departmentIds = FOREACH departments generate (int) $0;
grunt> describe departmentIds
departmentIds: {int}

/********Loading data with schema***************/
 departments = load '/user/sarangdp/sqoop_import/departments' using PigStorage(',') AS (departmentId:int, departmentName:chararray);
 
 grunt> describe departments;
departments: {departmentId: int,departmentName: chararray}
departmentIds = FOREACH departments generate departmentId;
dump departmentIds;

/**HCatalog is used to interact with Hive***/
 pig -useHCatalog
 
 orders = LOAD 'sarang.orders' using org.apache.hive.hcatalog.pig.HCatLoader(); //load data from orders table in hive
 grunt> describe orders;
  orders: {order_id: int,order_date: chararray,order_customer_id: int,order_status: chararray}
order_limit = LIMIT orders 10;



