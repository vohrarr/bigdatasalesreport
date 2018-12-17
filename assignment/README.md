
        My project is to report the customer sales with granulity of year, month, day and hour. 

        Input : Customer data set.
        Input : Sales data set.

        Output : Get <state,total_sales> for (year, month, day and hour) granularities.

        Project Structure :

        model: This folder contains the classes for input data sets i.e. customer, sales and output report i.e. report. 

        MyAppMain : is the controller of the application which reads input data sets
        and decide the processor to process those data sets.

        processor: This folder contains two types of processors .
              a) default processor.
              b) smallcustomer data set processor.

        Difference between two processor is the join. If the data set is small, broadcast the small data set rdd and do the join using iterator.


        Pseudo Code:

        1) Read two input datasets in java rdds.
        2) Create javapairRdd for both the data sets as customerId as key and
           value as customer object or sales object.

        3) Join two rdds on CustomerId.
        4) On the joinedRdd , create different Rdds using reduceByKey 

