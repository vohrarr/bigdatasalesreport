import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import processor.SalesProcessor;

/**
 * Created by rvohra on 12/10/18.
 */
public class AppMain {
    public JavaSparkContext init(String[] args) {
        if (args.length < 2 ) {
            System.err.println(
                    "Usage: BigDataSalesAnalyzer master outputDirHDFS customer sales strategy");
            System.exit(1);
        }

        // Create a Java Spark Context
        SparkConf conf = new SparkConf().setMaster("local").setAppName("SalesReport");
        //SparkConf conf = new SparkConf().setAppName("TEST");
        JavaSparkContext sc = new JavaSparkContext(conf);
        return sc;
    }

    public void process(String[] args) {

        JavaSparkContext sc = init(args);

        if (sc == null) {
            System.err.println("Something wrong in initialization.");
            return;
        }

        JavaRDD<String> customer = sc.textFile(args[0]);
        JavaRDD<String> sales = sc.textFile(args[1]);

        StrategyFactory.STRATEGY strategy = StrategyFactory.STRATEGY.getExecutionType(args[3]);
        SalesProcessor processor = StrategyFactory.getProcessor(strategy);
        processor.process(sc, customer, sales, args[2]);

        //Instead of If, use startegy pattern to chnge the processor.

        /*if (args[3].equals("SMALL_CUST_SET")) {
            //SalesService ss = new SalesService();
            //ss.process(sc, customer, sales, args[2]);
            System.out.println("The application is for small customer");

        }
        else {
            SalesService ss = new SalesService();
            ss.process(sc, customer, sales, args[2]);
        }*/

    }


    // input datasets and output directory will be input to the app.
    public static void main(String[] args) {
        AppMain app = new AppMain();
        app.process(args);
        //String[] s = new String[]{ "/home/rvohra/data/customer.txt", "/home/rvohra/data/sales.txt", "/home/rvohra/data/output", ""};
        //app.process(s);

    }
}
