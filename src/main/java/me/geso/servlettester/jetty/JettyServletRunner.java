package me.geso.servlettester.jetty;

import java.net.URI;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import lombok.NonNull;

public class JettyServletRunner implements AutoCloseable {
	private final URI baseURI;
	private final Server server;

	public JettyServletRunner(Class<? extends Servlet> servletClass) throws Exception {
		this(servletClass, "/");
	}

	public JettyServletRunner(Class<? extends Servlet> servletClass, String contextPath) throws Exception {
		this(new ServletHolder(servletClass), contextPath);
	}

	public JettyServletRunner(Servlet servlet) throws Exception {
		this(servlet, "/");
	}

	public JettyServletRunner(Servlet servlet, String contextPath) throws Exception {
		this(new ServletHolder(servlet), contextPath);
	}

	public JettyServletRunner(@NonNull ServletHolder servletHolder) throws Exception {
		this(servletHolder, "/");
	}

	public JettyServletRunner(@NonNull ServletHolder servletHolder, @NonNull String contextPath) throws Exception {
		this.server = new Server(0);
		ServletContextHandler context = new ServletContextHandler(
			server,
			contextPath,
			ServletContextHandler.SESSIONS
				);
		context.addServlet(servletHolder, "/*");
		server.setStopAtShutdown(true);
		server.start();
		ServerConnector connector = (ServerConnector)server
			.getConnectors()[0];
		int port = connector.getLocalPort();
		this.baseURI = new URI("http://127.0.0.1:" + port);
	}

	@Override
	public void close() throws Exception {
		if (this.server != null) {
			this.server.stop();
		}
	}

	public URI getBaseURI() {
		return this.baseURI;
	}
}
