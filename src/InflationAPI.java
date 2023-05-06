import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class InflationAPI {

    public static ArrayList<DataEntry> APIcall() throws IOException {
        // Get the current date and calculate the date range for the API call
        LocalDate dateToday = LocalDate.now();
        LocalDate LastMonth = dateToday.minusMonths(1);
        LocalDate LastDateOfLastMonth = LastMonth.withDayOfMonth(LastMonth.getMonth().length(LastMonth.isLeapYear()));
        LocalDate LastDateOfLastMonthLastYear = LastDateOfLastMonth.minusYears(1);
        
        
        try {
            // Create the URL string for the API call based on the date range
            String URLString = "https://data.nasdaq.com/api/v3/datasets/RATEINF/CPI_USA.csv?start_date="
                    + LastDateOfLastMonthLastYear + "&end_date=" + LastDateOfLastMonth +
                    "&api_key=HCNh65jaC8fvZjFHvnnr";
            ArrayList<DataEntry> data = ReadData(URLString) ;
            return data;
        } catch (MalformedURLException e) {
            // If there is an error creating the URL, throw a runtime exception
            throw new RuntimeException(e);
        } catch (IOException e) {
            // If there is an error reading the CSV file, retry the API call with an earlier end date
            LocalDate TwoMonthsAgo = LastMonth.minusMonths(1);
            LocalDate TwoMonthsAgoLastDay = TwoMonthsAgo.withDayOfMonth(
                    TwoMonthsAgo.getMonth().length(TwoMonthsAgo.isLeapYear()));
            String URLString = "https://data.nasdaq.com/api/v3/datasets/RATEINF/CPI_USA.csv??start_date="
                    + LastDateOfLastMonthLastYear + "&end_date=" + TwoMonthsAgoLastDay;
            ArrayList<DataEntry> data = ReadData(URLString) ;
            return data;
        }
    }

    private static ArrayList<DataEntry> ReadData(String urlString) throws IOException {
        // Set up variables for parsing the CSV file
        String csvUrl = urlString;
        String line;
        String csvSplitBy = ",";
        ArrayList<DataEntry> data = new ArrayList<>();

        // Open a buffered reader for the URL stream
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(csvUrl).openStream()));

        // Skip the header row
        br.readLine();

        // Parse the CSV file line by line
        while ((line = br.readLine()) != null) {
            String[] values = line.split(csvSplitBy);

            // Parse the date
            String date = values[0];

            // Parse the value as a double
            double value = Double.parseDouble(values[1]);

            // Add the data entry to the list
            data.add(new DataEntry(date, value));
        }
		return data;

    }
        
    public static double InflationCalc(ArrayList<DataEntry> data) {
    	// Calculate the inflation rate as the percentage change between the first and last data points
    	return (data.get(0).getValue() - data.get(data.size()-1).getValue())/(data.get(data.size()-1).getValue());
    }    

    // DataEntry class to represent a single data point from the API call
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

