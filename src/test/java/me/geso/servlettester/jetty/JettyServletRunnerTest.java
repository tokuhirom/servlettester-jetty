package me.geso.servlettester.jetty;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.Test;

public class JettyServletRunnerTest {

	public static class MyServletClass extends HttpServlet {
		private static final long serialVersionUID = 1L;

		@Override
		protected void service(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			resp.getWriter().print("Hey: " + req.getContextPath() + ", " + req.getPathInfo());
		}
	}

	@Test
	public void test() throws Exception {
		try (JettyServletRunner runner = new JettyServletRunner(
			MyServletClass.class)) {
			URI baseURI = runner.getBaseURI();
			try (CloseableHttpClient client = HttpClientBuilder.create()
				.build()) {
				HttpGet request = new HttpGet(baseURI);
				try (CloseableHttpResponse resp = client.execute(request)) {
					String body = EntityUtils.toString(resp.getEntity(),
						StandardCharsets.UTF_8);
					assertEquals("Hey: , /", body);
				}
			}
		}
	}

	@Test
	public void testWithServlet() throws Exception {
		try (JettyServletRunner runner = new JettyServletRunner(
				new MyServletClass())) {
			URI baseURI = runner.getBaseURI();
			try (CloseableHttpClient client = HttpClientBuilder.create()
					.build()) {
				HttpGet request = new HttpGet(baseURI);
				try (CloseableHttpResponse resp = client.execute(request)) {
					String body = EntityUtils.toString(resp.getEntity(),
							StandardCharsets.UTF_8);
					assertEquals("Hey: , /", body);
				}
			}
		}
	}

	@Test
	public void testWithServletHolder() throws Exception {
		try (JettyServletRunner runner = new JettyServletRunner(
				new ServletHolder(new MyServletClass()))) {
			URI baseURI = runner.getBaseURI();
			try (CloseableHttpClient client = HttpClientBuilder.create()
					.build()) {
				HttpGet request = new HttpGet(baseURI);
				try (CloseableHttpResponse resp = client.execute(request)) {
					String body = EntityUtils.toString(resp.getEntity(),
							StandardCharsets.UTF_8);
					assertEquals("Hey: , /", body);
				}
			}
		}
	}

	@Test
	public void testWithContextPath() throws Exception {
		try (JettyServletRunner runner = new JettyServletRunner(
			MyServletClass.class, "/xxx/")) {
			URI baseURI = runner.getBaseURI();
			try (CloseableHttpClient client = HttpClientBuilder.create()
				.build()) {
				final URIBuilder builder = new URIBuilder(baseURI);
				final URI uri = builder.setPath("/xxx/").build();
				final HttpGet request = new HttpGet(uri);
				try (CloseableHttpResponse resp = client.execute(request)) {
					String body = EntityUtils.toString(resp.getEntity(),
						StandardCharsets.UTF_8);
					assertEquals("Hey: /xxx, /", body);
				}
			}
			try (CloseableHttpClient client = HttpClientBuilder.create()
					.build()) {
				final URIBuilder builder = new URIBuilder(baseURI);
				final URI uri = builder.setPath("/xxx/yyy/zzz").build();
				final HttpGet request = new HttpGet(uri);
				try (CloseableHttpResponse resp = client.execute(request)) {
					String body = EntityUtils.toString(resp.getEntity(),
							StandardCharsets.UTF_8);
					assertEquals("Hey: /xxx, /yyy/zzz", body);
				}
			}
		}
	}

}
