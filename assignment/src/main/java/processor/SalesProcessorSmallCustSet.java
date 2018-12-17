package processor;

import model.customer;
import model.report;
import model.sales;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by rvohra on 12/14/18.
 */
public class SalesProcessorSmallCustSet extends SalesProcessor{
    @Override
    protected JavaRDD<report> join(JavaSparkContext sc, JavaPairRDD<String, customer> cRows, JavaPairRDD<String, sales> sRows) {

        Map<String, customer> cRowsMap = cRows.collectAsMap();
        sc.broadcast(cRowsMap);

        // Use mapPartitions to combine the elements of sRows with the broadcast of customer Rows
        JavaRDD<report> joinedRow = sRows.mapPartitions(new FlatMapFunction<Iterator<Tuple2<String, sales>>, report>() {
            @Override
            public Iterator<report> call(Iterator<Tuple2<String,sales>> salesPerPartition) throws Exception {
                List<report> joinedRows = new ArrayList<>();
                while (salesPerPartition.hasNext()) {
                    Tuple2<String,sales> sales = salesPerPartition.next();
                    customer cust = cRowsMap.get(sales._1);
                    joinedRows.add(new report(cust, sales._2()));
                }
                return joinedRows.iterator();
            }
        });

        return joinedRow;
    }
}
