package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rvohra on 12/10/2018.
 */
public class sales  implements Serializable {

    private static final long serialVersionUID = 30L;
    private long epoch;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String customerId;
    private BigDecimal amount;

    public sales (String epoch, String customerId, BigDecimal amount) {
        long ep = Long.valueOf(epoch);

        this.setEpoch(ep);
        //converting epoch to Date object
        // From date object get all the required fields.

        Date epd = new Date(Long.parseLong(epoch) * 1000L);
        SimpleDateFormat dt = new SimpleDateFormat("yyyy");
        this.year = dt.format(epd);
        dt = new SimpleDateFormat("MM");
        this.month = dt.format(epd);
        dt = new SimpleDateFormat("dd");
        this.day = dt.format(epd);
        dt = new SimpleDateFormat("HH");
        this.hour = dt.format(epd);

        this.customerId = customerId;
        this.amount=amount;
    }

    public static sales parseRecord(String s) {
        String[] salesrec = s.split("#");

        return new sales(salesrec[0], salesrec[1], BigDecimal.valueOf(Long.valueOf(salesrec[2])));
    }

/*    public static long getSerialVersionUID() {
        return serialVersionUID;
    }*/

    public long getEpoch() {
        return epoch;
    }

    public void setEpoch(long epoch) {
        this.epoch = epoch;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

