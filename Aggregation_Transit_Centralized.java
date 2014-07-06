package org.deegree.wps;

import org.deegree.services.wps.Processlet;
import org.deegree.services.wps.ProcessletException;
import org.deegree.services.wps.ProcessletExecutionInfo;
import org.deegree.services.wps.ProcessletInputs;
import org.deegree.services.wps.ProcessletOutputs;
import org.deegree.services.wps.input.LiteralInput;
import org.deegree.services.wps.output.LiteralOutput;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

public class Aggregation_Transit_Centralized implements Processlet {

	public void process(ProcessletInputs in, ProcessletOutputs out,
			ProcessletExecutionInfo info) throws ProcessletException {
		String poi = ((LiteralInput) in.getParameter("POI")).getValue();
		String crime = ((LiteralInput) in.getParameter("Crime")).getValue();
		String transit = ((LiteralInput) in.getParameter("Transit")).getValue();
		String start_point = ((LiteralInput) in.getParameter("StartPoint"))
				.getValue();
		String walkshed_collection = ((LiteralInput) in
				.getParameter("WalkshedCollection")).getValue();
		String walkshed_union = ((LiteralInput) in
				.getParameter("WalkshedUnion")).getValue();
		String distance_decay_function = ((LiteralInput) in
				.getParameter("DistanceDecayFunction")).getValue();
		String walking_time_period = ((LiteralInput) in
				.getParameter("WalkingTimePeriod")).getValue();

		URL url;
		String line;
		HttpURLConnection connection;
		StringBuilder sb = new StringBuilder();
		String url_string;
		String url_parameters;
		String USER_AGENT = "Mozilla/5.0";
		DataOutputStream wr;

		try {

			url_string = "http://127.0.0.1:9364/aggregation";
			url_parameters = "start_point=" + start_point
					+ "&walkshed_collection=" + walkshed_collection
					+ "&walkshed_union=" + walkshed_union + "&poi=" + poi
					+ "&crime=" + crime + "&transit=" + transit
					+ "&walking_time_period=" + walking_time_period
					+ "&distance_decay_function=" + distance_decay_function;

			url = new URL(url_string);

			connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("POST");
			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			connection.setDoOutput(true);
			wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(url_parameters);
			wr.flush();
			wr.close();

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			rd.close();

			connection.disconnect();

		} catch (Exception e) {
			System.out.println("Errors...");
		}

		LiteralOutput result = (LiteralOutput) out
				.getParameter("AggregationResult");
		result.setValue("" + sb.toString());
	}

	public void destroy() {
	}

	public void init() {
	}
}