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

public class JettyServletTesterTest {

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

	@Test
	public void testWithServletHolder() throws Exception {
		JettyServletTester.runServlet(new ServletHolder(MyServletClass.class), (uri) -> {
			try (CloseableHttpClient client = HttpClientBuilder.create()
					.build()) {
				HttpGet request = new HttpGet(uri);
				try (CloseableHttpResponse resp = client.execute(request)) {
					String body = EntityUtils.toString(resp.getEntity(),
							StandardCharsets.UTF_8);
					assertEquals("Hey: , /", body);
				}
			}
		});
	}

	@Test
	public void testWithServletClass() throws Exception {
		JettyServletTester.runServlet(MyServletClass.class, (uri) -> {
			try (CloseableHttpClient client = HttpClientBuilder.create()
					.build()) {
				HttpGet request = new HttpGet(uri);
				try (CloseableHttpResponse resp = client.execute(request)) {
					String body = EntityUtils.toString(resp.getEntity(),
							StandardCharsets.UTF_8);
					assertEquals("Hey: , /", body);
				}
			}
		});
	}

	@Test
	public void testWithServletClassWithPath() throws Exception {
		JettyServletTester.runServlet(MyServletClass.class, "/xxx", (baseUri) -> {
			try (CloseableHttpClient client = HttpClientBuilder.create()
					.build()) {
				final URI uri = new URIBuilder(baseUri).setPath("/xxx/yyy").build();
				HttpGet request = new HttpGet(uri);
				try (CloseableHttpResponse resp = client.execute(request)) {
					String body = EntityUtils.toString(resp.getEntity(),
							StandardCharsets.UTF_8);
					assertEquals("Hey: /xxx, /yyy", body);
				}
			}
		});
	}

	@Test
	public void testWithServletObject() throws Exception {
		JettyServletTester.runServlet(new MyServletClass(), (uri) -> {
			try (CloseableHttpClient client = HttpClientBuilder.create()
					.build()) {
				HttpGet request = new HttpGet(uri);
				try (CloseableHttpResponse resp = client.execute(request)) {
					String body = EntityUtils.toString(resp.getEntity(),
							StandardCharsets.UTF_8);
					assertEquals("Hey: , /", body);
				}
			}
		});
	}

	@Test
	public void testWithServletObjectWithPath() throws Exception {
		JettyServletTester.runServlet(new MyServletClass(), "/xxx", (baseUri) -> {
			try (CloseableHttpClient client = HttpClientBuilder.create()
					.build()) {
				final URI uri = new URIBuilder(baseUri).setPath("/xxx/yyy").build();
				HttpGet request = new HttpGet(uri);
				try (CloseableHttpResponse resp = client.execute(request)) {
					String body = EntityUtils.toString(resp.getEntity(),
							StandardCharsets.UTF_8);
					assertEquals("Hey: /xxx, /yyy", body);
				}
			}
		});
	}

	@Test
	public void testWithContextPath() throws Exception {
		JettyServletTester.runServlet((req, resp) -> {
			resp.getWriter().print("Hey:" + req.getContextPath() + ", " + req.getServletPath() + ", " + req.getPathInfo());
		}, "/xxx/", (baseUri) -> {
			try (CloseableHttpClient client = HttpClientBuilder.create()
					.build()) {
				final URI uri = new URIBuilder(baseUri).setPath("/xxx/yyy").build();
				HttpGet request = new HttpGet(uri);
				try (CloseableHttpResponse resp = client.execute(request)) {
					String body = EntityUtils.toString(resp.getEntity(),
							StandardCharsets.UTF_8);
					assertEquals("Hey:/xxx, , /yyy", body);
				}
			}
		});
	}

}
