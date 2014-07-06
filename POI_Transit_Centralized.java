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

public class POI_Transit_Centralized implements Processlet {

	public void process(ProcessletInputs in, ProcessletOutputs out,
			ProcessletExecutionInfo info) throws ProcessletException {
		String walkshed = ((LiteralInput) in.getParameter("Walkshed"))
				.getValue();

		URL url;
		String line;
		StringBuilder sb = new StringBuilder();
		HttpURLConnection connection;
		String url_string;

		try {
			url_string = "http://127.0.0.1:9365/poi?walkshed=" + walkshed;
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

		LiteralOutput result = (LiteralOutput) out.getParameter("POIResult");
		result.setValue("" + sb.toString());
	}

	public void destroy() {
	}

	public void init() {
	}
}