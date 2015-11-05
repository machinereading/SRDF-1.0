package main.java.edu.kaist.cs.srdf.rest;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

public class Main {

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://143.248.135.216/").port(47361).build();
	}

	public static final URI BASE_URI = getBaseURI();

	protected static HttpServer startServer() throws IOException {
		System.out.println("Starting grizzly...");
		ResourceConfig rc = new PackagesResourceConfig(
				"main.java.edu.kaist.cs.srdf.rest");
		return GrizzlyServerFactory.createHttpServer(BASE_URI, rc);
	}

	public static void main(String[] args) throws IOException {
		HttpServer httpServer = startServer();
		
		SRDFService ss = new SRDFService();
		ss.preloadSRDF();
		
		System.out
				.println(String
						.format("Jersey app started with WADL available at "
								+ "%sapplication.wadl\nTry out %ssrdfservice\nHit enter to stop it...",
								BASE_URI, BASE_URI));
		System.in.read();
		httpServer.stop();
	}
}
