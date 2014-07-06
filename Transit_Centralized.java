package org.deegree.wps;

import org.apache.commons.httpclient.util.URIUtil;
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
import java.io.InputStreamReader;

public class Transit_Centralized implements Processlet {

	public void process(ProcessletInputs in, ProcessletOutputs out,
			ProcessletExecutionInfo info) throws ProcessletException {
		String start_time = ((LiteralInput) in.getParameter("StartTime"))
				.getValue();
		String walkshed = ((LiteralInput) in.getParameter("Walkshed"))
				.getValue();
		String walking_time_period = ((LiteralInput) in
				.getParameter("WalkingTime")).getValue();
		String walking_speed = ((LiteralInput) in.getParameter("WalkingSpeed"))
				.getValue();
		String bus_waiting_time = ((LiteralInput) in
				.getParameter("BusWaitingTime")).getValue();
		String bus_ride_time = ((LiteralInput) in.getParameter("BusRideTime"))
				.getValue();

		URL url;
		String line;
		StringBuilder sb = new StringBuilder();
		HttpURLConnection connection;
		String url_string;

		try {
			url_string = "http://127.0.0.1:9367/transit?start_time="
					+ start_time + "&walkshed=" + walkshed
					+ "&walking_time_period=" + walking_time_period
					+ "&walking_speed=" + walking_speed + "&bus_waiting_time="
					+ bus_waiting_time + "&bus_ride_time=" + bus_ride_time;

			url_string = URIUtil.encodeQuery(url_string);
			url = new URL(url_string);

			connection = (HttpURLConnection) url.openConnection();

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

		LiteralOutput result = (LiteralOutput) out.getParameter("TransitResult");
		result.setValue("" + sb.toString());
	}

	public void destroy() {
	}

	public void init() {
	}
}