# servlettester-jetty

[![Build Status](https://travis-ci.org/tokuhirom/servlettester-jetty.svg)](https://travis-ci.org/tokuhirom/servlettester-jetty)

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

## LICENSE

    The MIT License (MIT)
    Copyright © 2014 Tokuhiro Matsuno, http://64p.org/ <tokuhirom@gmail.com>

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the “Software”), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
