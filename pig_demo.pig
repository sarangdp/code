//Word Count Program

 lines = load '/user/sarangdp/wordCount.txt' using PigStorage() AS (line:chararray);
 words = FOREACH lines GENERATE FLATTEN(TOKENIZE(line)) AS word;
grouped = GROUP words BY word;
wordCount = FOREACH grouped GENERATE group, COUNT(words);
dump wordCount;

Pig- doesn't have additional daemon process. It uses MR and Yarn.

to run pig file.--> pig -f <filename> -x <execution mode tez/mr/local>

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

//equivalent to the above statement - LOAD data by applying the hive schema
//run the describe formatted hiveTable command to get the hive path and delimiter details
categories = LOAD 'hdfs://host:port/apps/hive/warehouse/sarang.db/categories' using PigStorage(',') 
               as (category_id:int, category_department_id:int,category_name:chararray);
categories_filter = FOREACH categories GENERATE category_id,category_name

/**********Grouping***********/
 orders = LOAD 'sarang.orders' using org.apache.hive.hcatalog.pig.HCatLoader();
 orders_group_all = GROUP orders ALL;
 orders_count = FOREACH orders_group_all GENERATE COUNT_STAR(orders) as cnt;
 dump orders_count;
 
 order_count_null = FILTER orders BY order_date == '' or order_status ==''; //Filtering the records
 //Group the data of one or more pig relations
 orders = LOAD 'sarang.orders' using org.apache.hive.hcatalog.pig.HCatLoader();
 orders_cnt_status = GROUP orders BY order_status;
 orders_cnt = FOREACH orders_cnt_status GENERATE group, COUNT_STAR(orders) AS cnt;
 dump orders_cnt;
 
/*********Store data from pig relation into HDFS***********//
depts = LOAD '/user/sarangdp/sqoop_import/departments';
STORE depts INTO '/user/sarangdp/departments';
/*********with different delimiter************/
depts = LOAD '/user/sarangdp/sqoop_import/departments' using PigStorage(',') AS (department_id:int, department_name:chararray);
STORE depts INTO '/user/sarangdp/departments' using PigStorage('|');

/******Binary Storage*********/
STORE depts INTO '/user/sarangdp/departments' using BinStorage('|');
departments_bin = LOAD ' /user/sarangdp/departments' using BinStorage('|'); //loading the binary data

/************JSON Storage************/
STORE depts INTO '/user/sarangdp/departments' using JsonStorage();

/******Stoaring the data in HIVe from Pig relation*****/
//schema is mandatory. Column names should be same as in hive table
deptDemo = LOAD '/user/sarangdp/sqoop_import/departments' using PigStorage(',') AS (department_id:int, department_name:chararray);
STORE deptDemo INTO 'sarang.dept_demo' using org.apache.hive.hcatalog.pig.HCatStorer();

/*****Sorting - ORDER BY****************************/
//(submit atleast 2 MR jobs for Order by)
 categories = LOAD 'sarang.categories' using org.apache.hive.hcatalog.pig.HCatLoader();
cat_order = ORDER categories BY category_name desc;
dump cat_order;

/*****Removing duplicates- DISTINCT****************************/
//distinct always apply on the relation and not on individual field
orders = LOAD '/user/sarangdp/sqoop_import/orders' using PigStorage(',');
order_status = FOREACH orders GENERATE $3 as order_status;
distStatus = DISTINCT order_status;

/*********Specify the number of reduce tasks for a Pig MapReduce job********/
//Session level
set default_parallel 4;

//command line
 orders = LOAD 'sarang.orders' using org.apache.hive.hcatalog.pig.HCatLoader();
orders = LOAD 'sarang.orders' using org.apache.hive.hcatalog.pig.HCatLoader();
groupOrders = GROUP orders BY order_status PARALLEL 2;
cntOrders = FOREACH groupOrders GENERATE group, COUNT_STAR(orders.order_id);
dump cntOrders;

/*********Inner Join - Default join is happen at reducer side********/ 
  orders = LOAD 'sqoop_import.orders' USING org.apache.hive.hcatalog.pig.HCatLoader();
  order_items = LOAD 'sqoop_import.order_items' USING org.apache.hive.hcatalog.pig.HCatLoader();
  orderJoin = JOIN orders BY order_id, order_items BY order_item_order_id;
  ordersByDate = FOREACH orderJoin GENERATE order_date,order_item_subtotal;
  orderRevByDate = GROUP ordersByDate BY order_date;
  orderRevByDateFinal = FOREACH orderRevByDate GENERATE group as orderDate, SUM(ordersByDate.order_item_subtotal) as revenue;

/*********Left outer..orders which dont have order items********/ 
  orderLeftJoin = JOIN orders BY order_id LEFT OUTER, order_items BY order_item_order_id;   
  filterOrders = FILTER orderLeftJoin BY order_items::order_item_order_id IS NULL;
  
 /*********Right outer..order items which dont have orders********/ 
  orderRightJoin = JOIN orders BY order_id RIGHT OUTER, order_items BY order_item_order_id;
  filterOrderItems = FILTER orderRightJoin BY orders::order_id IS NULL;
  
  /****Replicated Join -replicate join is a special type of join that works well if one or more relations are small enough to 
  fit into main memory. In such cases, Pig can perform a very efficient join because all of the hadoop work is done on the map side. 
  In this type of join the large relation is followed by one or more small relations. 
  The small relations must be small enough to fit into main memory; if they don't, the process fails and an error is generated.***/
  
  orderJoin = JOIN orders BY order_id, order_items BY order_item_order_id USING 'replicated';
  
  /****Execution mode***/
  //Use -x switch
  pig -f pig_demo.txt -useHCatalog -x mapreduce
  pig -f pig_demo.txt -useHCatalog -x tez
  
  //Default exec engine is mentioned in /etc/pig/conf/pig.properties
  
  //Register jar
  REGISTER /usr/hdp/2.6.5.0-292/pig/piggybank.jar
  deptName = FOREACH depts GENERATE dept_id, org.apache.pig.piggybank.evaluation.string.UPPER(dept_name) as depName;
  //Alias can be defined instead whole package/path
  DEFINE UPPER org.apache.pig.piggybank.evaluation.string.UPPER;
  deptName = FOREACH depts GENERATE dept_id, UPPER(dept_name) as depName;
   
//export data from hive table into CSV file
orders = LOAD 'sarang.orders' using org.apache.hive.hcatalog.pig.HCatLoader();
STORE orders INTO '/user/sarangdp/output/orders' using PigStorage('\t','-schema');
hadoop fs -cat /user/sarangdp/output/orders/.pig_header /user/sarangdp/output/orders/part-m-00000 | hadoop fs -put - /user/sarangdp/csvoutput/result/output.csv
 
