package processor;

import model.customer;
import model.report;
import model.sales;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import scala.Tuple2;
import scala.Tuple3;
import scala.Tuple4;
import scala.Tuple5;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by rvohra on 12/10/2018.
 */
public class SalesProcessor implements SalesProcessorInterface, Serializable {

    private static final long serialVersionUID = 40L;

    @Override
    public void process(JavaSparkContext sc, JavaRDD<String> customerrdd,
                        JavaRDD<String> salesrdd, String outputDir) {

        // parse the lines and create customer object
        JavaRDD<customer> customerRows = customerrdd.map(line -> customer.parseRecord(line));

        // parse the lines and create sales object
        JavaRDD<sales> salesRows = salesrdd.map(line -> sales.parseRecord(line));

        //Creates a pair RDD using customerId as Key and value as customer
        JavaPairRDD<String, customer> cRows = customerRows
                .mapToPair(c -> new Tuple2<>(c.getCustomerId(), c));

        //Creates a pair RDD using customerId as Key and value as sales
        JavaPairRDD<String, sales> sRows = salesRows
                .mapToPair(s -> new Tuple2<>(s.getCustomerId(), s));

        JavaRDD<report> joinData = join(sc, cRows, sRows);

        joinData.collect();
        processJoinedData(joinData, outputDir);
    }


    protected void processJoinedData(JavaRDD<report> joinData, String outputDir) {
        // cache the results as we are reusing this rdd multiple times.
        joinData.cache();

        // based on State
        // mapToPair function will map JavaRDD to JavaPairRDD
        JavaRDD<report> aggState
                = joinData.mapToPair(e -> new Tuple2<>(e.getState(), e.getAmount()))
                .reduceByKey( (x,y) -> x.add(y))
                .map(e2 -> new report(e2._1, e2._2));

        rddToFile(aggState, outputDir + "/state");

        // based on state and year
        JavaRDD<report> aggYear
                = joinData.mapToPair(e -> new Tuple2<>(new Tuple2<>(e.getState(), e.getYear()), e.getAmount()))
                .reduceByKey( (x,y) -> x.add(y))
                .map(e2 -> new report(e2._1._1, e2._1._2, e2._2));

        rddToFile(aggYear, outputDir + "/year");

        //based on state, year, month
        JavaRDD<report> aggMonth
                = joinData.mapToPair(e -> new Tuple2<>(new Tuple3<>(e.getState(), e.getYear(), e.getMonth()), e.getAmount()))
                .reduceByKey( (x,y) -> x.add(y))
                .map(e2 -> new report(e2._1._1(), e2._1._2(), e2._1._3(), e2._2));

        rddToFile(aggMonth, outputDir + "/month");

        //based on state, year, month, day
        JavaRDD<report> aggDay
                = joinData.mapToPair(e -> new Tuple2<>(new Tuple4<>(e.getState(), e.getYear(), e.getMonth(), e.getDay()), e.getAmount()))
                .reduceByKey( (x,y) -> x.add(y))
                .map(e2 -> new report(e2._1._1(), e2._1._2(), e2._1._3(), e2._1._4(), e2._2));

        rddToFile(aggDay, outputDir + "/day");

        //based on state, year, month, day, Hour
        JavaRDD<report> aggHour
                = joinData.mapToPair(e -> new Tuple2<>(new Tuple5<>(e.getState(), e.getYear(), e.getMonth(), e.getDay(), e.getHour()), e.getAmount()))
                .reduceByKey( (x,y) -> x.add(y))
                .map(e2 -> new report(e2._1._1(), e2._1._2(), e2._1._3(), e2._1._4(), e2._1._5(), e2._2));

        rddToFile(aggHour, outputDir + "/hour");

        ArrayList<String> states = new ArrayList(Arrays.asList("CA","AL"));

        JavaRDD<report> sortedR=aggDay.union(aggHour).union(aggMonth).union(aggDay).union(aggYear).sortBy(new Function<report, String>() {
            public String call(report value) throws Exception {
                return value.getState();
            }
        }, true, 1);

        rddToFile(sortedR, outputDir + "/result");

        System.out.println("RESULT IS IN " + outputDir + "/result");

    }

    /**
     * Join the input data
     * @param cRows
     * @param sRows
     * @return
     */
    protected JavaRDD<report> join(JavaSparkContext sc, JavaPairRDD<String, customer> cRows, JavaPairRDD<String, sales> sRows) {
        //Tuple2<String, Tuple2<Customer,Sales>>
        JavaRDD<report> joinData = cRows.join(sRows).map(e -> new report(e._2._1, e._2._2));
        return joinData;
    }

    public void rddToFile(JavaRDD<report> rdd, String filePath) {
        rdd.saveAsTextFile(filePath);
    }

}


