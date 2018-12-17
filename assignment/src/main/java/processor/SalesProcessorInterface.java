package processor;

import org.apache.spark.api.java.JavaRDD;

import org.apache.spark.api.java.JavaSparkContext;

/**
 * Created by rvohra on 12/14/18.
 */
public interface SalesProcessorInterface {
    public void process(JavaSparkContext sc, JavaRDD<String> customers,
                        JavaRDD<String> sales, String outputDir);

}
