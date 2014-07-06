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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Walkshed_Transit_Centralized implements Processlet {

	public void process(ProcessletInputs in, ProcessletOutputs out,
			ProcessletExecutionInfo info) throws ProcessletException {
		String fromPalce = ((LiteralInput) in.getParameter("StartPoint"))
				.getValue();
		String walkTime = ((LiteralInput) in.getParameter("WalkingPeriod"))
				.getValue();
		String walkSpeed = ((LiteralInput) in.getParameter("WalkingSpeed"))
				.getValue();
		String output = ((LiteralInput) in.getParameter("WalkshedOutput"))
				.getValue();

		URL url;
		String line;
		StringBuilder sb = new StringBuilder();
		HttpURLConnection connection;

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date date = new Date();
		String time = dateFormat.format(date);

		try {
			url = new URL(
					"http://gisciencegroup.ucalgary.ca:8080/opentripplanner-api-webapp/ws/iso?layers=traveltime&styles=mask&batch=true&fromPlace="
							+ fromPalce
							+ "&toPlace=51.09098935,-113.95179705&time="
							+ time
							+ "&mode=WALK&maxWalkDistance=10000&walkTime="
							+ walkTime
							+ "&walkSpeed="
							+ walkSpeed
							+ "&output="
							+ output);

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

		LiteralOutput result = (LiteralOutput) out.getParameter("WalkshedResult");
		result.setValue("" + sb.toString());
	}

	public void destroy() {
	}

	public void init() {
	}
}