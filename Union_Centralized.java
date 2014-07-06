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

public class Union_Centralized implements Processlet {

	public void process(ProcessletInputs in, ProcessletOutputs out,
			ProcessletExecutionInfo info) throws ProcessletException {
		String walkshed_collection = ((LiteralInput) in
				.getParameter("WalkshedCollection")).getValue();

		URL url;
		String line;
		StringBuilder sb = new StringBuilder();
		HttpURLConnection connection;
		String url_string;

		String url_parameters;
		String USER_AGENT = "Mozilla/5.0";
		DataOutputStream wr;

		try {
			url_string = "http://127.0.0.1:9368/union";
			url_parameters = "walkshed_collection=" + walkshed_collection;

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

		LiteralOutput result = (LiteralOutput) out.getParameter("UnionResult");
		result.setValue("" + sb.toString());
	}

	public void destroy() {
	}

	public void init() {
	}
}