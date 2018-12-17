import processor.SalesProcessor;
import processor.SalesProcessorSmallCustSet;

/**
 * Created by rvohra on 12/14/18.
 */
public class StrategyFactory {
    /**
     * Created by rvohra on 12/13/18.
     */     
    public enum STRATEGY {
            SMALL_CUST_SET("small_cust_set"),
            DEFAULT("default");

            private String str;
            private STRATEGY(String inputType) {
                str=inputType;
            }

            public static STRATEGY getExecutionType (String inputType) {
                if (inputType == null)
                    return DEFAULT;

                String STRATEGY = inputType.toLowerCase();
                if (STRATEGY.equals(SMALL_CUST_SET.str)) {
                    return SMALL_CUST_SET;
                } else {
                    return DEFAULT;
                }
            }
        }


        public static SalesProcessor getProcessor(STRATEGY type) {

            switch (type) {
                case SMALL_CUST_SET:
                    return new SalesProcessorSmallCustSet();

                default:
                    return new SalesProcessor();
            }
        }

}
