package dp.dataconverter.mp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class DataConverter {

	public static void main(String[] args) throws ParseException {
		String trafficData = getTrafficData(args);
		if (trafficData != null) {
			boolean connected = sendTrafficDataViaLTE(trafficData);
			if (!connected) {
				System.out.println("ERROR! Connection failed!");
				
			}
		}

	}

	/**
	 * this Method is recieving the data from the flag --data
	 * 
	 * @param args
	 * @return returns the Data recieved throuhgh the flag or null
	 * @throws ParseException
	 */
	public static String getTrafficData(String[] args) throws ParseException {
		Options options = new Options();
		Option data = Option.builder().longOpt("data").argName("DataReciever").hasArg()
				.desc("Use Option to submit data").build();
		options.addOption(data);
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);

		// has the logFile argument been passed?
		String trafficData = null;
		if (cmd.hasOption("data")) {
			// get the logFile argument passed
			trafficData = cmd.getOptionValue("data");
			// System.out.println(trafficData);
		}
		return trafficData;
	}
	
	/*
	 * this method is sending the data via LTE in JSON-Format
	 */
	public static boolean sendTrafficDataViaLTE(String trafficData) {
		URL url = null;

		try {
			url = new URL("https://61a8cda033e9df0017ea3b0c.mockapi.io/api/v1/nodes");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);
			try (OutputStream os = con.getOutputStream()) {

				os.write(trafficData.getBytes());
				os.flush();
				os.close();
			}
			try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))) {

				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				System.out.println("Data sent successfully! Response:" + response.toString());
				return true;
			}
		} catch (Exception e) {

			return false;
			// return false;
		}
	}
}
