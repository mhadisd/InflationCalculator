import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class InflationAPI {
    public static void APIcall() {
        //create Arraylist to store CPI data taken from the API call
        ArrayList<CPIData> CPI_Data = new ArrayList<CPIData>();

        //This sets up the formatting for the two calendar variables used in the URL API Call
        //The first one should be the current date when the program is run, and then that string will
        //be converted into a Date instance, then a calendar instance, where a year will be subtracted
        //before being converted back into the previous year's date

        LocalDate dateToday = LocalDate.now();
        LocalDate LastMonth = dateToday.minusMonths(1);
        LocalDate LastDateOfLastMonth = LastMonth.withDayOfMonth(
                LastMonth.getMonth().length(LastMonth.isLeapYear()));
        LocalDate LastDateOfLastMonthLastYear = LastDateOfLastMonth.minusYears(1);
        Scanner fileScan;
        try {
            //concatenate URL string for API call based on date the program is accessed
            String URLString = "https://data.nasdaq.com/api/v3/datasets/RATEINF/CPI_USA.csv??start_date="
                    + LastDateOfLastMonthLastYear + "&end_date=" + LastDateOfLastMonth;
            URL url = new URL(URLString);
            fileScan = new Scanner(url.openStream());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            LocalDate TwoMonthsAgo = LastMonth.minusMonths(1);
            LocalDate TwoMonthsAgoLastDay = TwoMonthsAgo.withDayOfMonth(
                    TwoMonthsAgo.getMonth().length(TwoMonthsAgo.isLeapYear()));
            String URLString = "https://data.nasdaq.com/api/v3/datasets/RATEINF/CPI_USA.csv??start_date="
                    + LastDateOfLastMonthLastYear + "&end_date=" + TwoMonthsAgoLastDay;
            try {
                URL url = new URL(URLString);
                fileScan = new Scanner(url.openStream());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
//        while (fileScan.hasNext()){
//            if (fileScan.hasNextDouble()){
////                CPI_Data.add(new CPIData(fileScan.hasNext(), fileScan.hasNextDouble()));
//            }
//        }
    }
    static class CPIData{
        private Date date;
        private double CPI;

        CPIData(Date date, double CPI){
            this.date = date;
            this.CPI = CPI;
        }
        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public double getCPI() {
            return CPI;
        }

        public void setCPI(double CPI) {
            this.CPI = CPI;
        }
    }
}