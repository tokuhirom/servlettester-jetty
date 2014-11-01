# servlettester-jetty

## SYNOPSIS

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

or

    JettyServletTester.runServlet(MyServlet.class, (uri) -> {
          // your test code
        }
    );

or

    JettyServletTester.runServlet(new MyServlet(), (uri) -> {
          // your test code
        }
    );

## DESCRIPTION

ServletTester is a utility class for writing test cases for HTTP servlets.
It based on Java8's lambda.

## Run jetty process by yourself

If you want to use low level API, you can call this library with following form:

		try (JettyServletRunner jettyServletRunner = new JettyServletRunner(servletHolder)) {
			// do something with `jettyServletRunner.getBaseURI()`
		}
