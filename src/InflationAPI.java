import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class InflationAPI {
    public static void APIcall() throws IOException {
        //This sets up the formatting for the two calendar variables used in the URL API Call
        //The first one should be the current date when the program is run, and then that string will
        //be converted into a Date instance, then a calendar instance, where a year will be subtracted
        //before being converted back into the previous year's date

        LocalDate dateToday = LocalDate.now();
        LocalDate LastMonth = dateToday.minusMonths(1);
        LocalDate LastDateOfLastMonth = LastMonth.withDayOfMonth(
                LastMonth.getMonth().length(LastMonth.isLeapYear()));
        LocalDate LastDateOfLastMonthLastYear = LastDateOfLastMonth.minusYears(1);
        try {
            //concatenate URL string for API call based on date the program is accessed
            String URLString = "https://data.nasdaq.com/api/v3/datasets/RATEINF/CPI_USA.csv?start_date="
                    + LastDateOfLastMonthLastYear + "&end_date=" + LastDateOfLastMonth +
                    "&api_key=HCNh65jaC8fvZjFHvnnr";
            System.out.println(URLString);
            CsvReader(URLString);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            LocalDate TwoMonthsAgo = LastMonth.minusMonths(1);
            LocalDate TwoMonthsAgoLastDay = TwoMonthsAgo.withDayOfMonth(
                    TwoMonthsAgo.getMonth().length(TwoMonthsAgo.isLeapYear()));
            String URLString = "https://data.nasdaq.com/api/v3/datasets/RATEINF/CPI_USA.csv??start_date="
                    + LastDateOfLastMonthLastYear + "&end_date=" + TwoMonthsAgoLastDay;
            CsvReader(URLString);
        }
    }
    private static void CsvReader(String urlString) throws IOException {
        String csvUrl = urlString;
        String line;
        String csvSplitBy = ",";
        ArrayList<DataEntry> data = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(csvUrl).openStream()));
            // skip header row
            br.readLine();
            while ((line = br.readLine()) != null) {

                String[] values = line.split(csvSplitBy);

                // parse date
                String date = values[0];

                // parse double
                double value = Double.parseDouble(values[1]);

                // add data entry to list
                data.add(new DataEntry(date, value));
            }
        // print data entries
        for (DataEntry entry : data) {
            System.out.println(entry.getDate() + ": " + entry.getValue());
        }
    }
    static class DataEntry {

        private String date;
        private double value;

        public DataEntry(String date, double value) {
            this.date = date;
            this.value = value;
        }

        public String getDate() {
            return date;
        }

        public double getValue() {
            return value;
        }
    }
}