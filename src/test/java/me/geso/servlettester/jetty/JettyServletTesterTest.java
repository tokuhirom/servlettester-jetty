package me.geso.servlettester.jetty;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class JettyServletTesterTest {

	@Test
	public void test() throws IOException, Exception {
		JettyServletTester.runServlet((req, resp) -> {
			resp.getWriter().print("Hey");
		}, (uri) -> {
			try (CloseableHttpClient client = HttpClientBuilder.create()
					.build()) {
				HttpGet request = new HttpGet(uri);
				try (CloseableHttpResponse resp = client.execute(request)) {
					String body = EntityUtils.toString(resp.getEntity(),
							StandardCharsets.UTF_8);
					assertEquals("Hey", body);
				}
			}
		});
	}

}
