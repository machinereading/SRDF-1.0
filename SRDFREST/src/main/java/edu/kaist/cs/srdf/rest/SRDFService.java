package main.java.edu.kaist.cs.srdf.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import main.java.edu.kaist.cs.srdf.SRDF;

import org.json.JSONException;

@Path("/srdfservice")
public class SRDFService {
		
	public static SRDF srdf;
	
	public void preloadSRDF(){
		srdf = new SRDF();
	}
	
	@POST
	@Consumes("application/x-www-form-urlencoded; charset=utf-8")
	@Produces("text/plain; charset=utf-8")
	public Response getSRDF(@FormParam("text") String input) throws JSONException {

		System.out.println(input);
		
		String result = srdf.run(input);

		return Response
				.ok(result)
				.entity(result)
				.header("Access-Control-Allow-Methods",
						"GET, POST, PUT, DELETE, OPTIONS")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept")
				.build();

	}
	
	@OPTIONS
	@Consumes("application/x-www-form-urlencoded; charset=utf-8")
	@Produces("text/plain; charset=utf-8")
	public Response getSRDFOptions(@FormParam("text") String input)
			throws JSONException {

		return Response
				.ok()
				.header("Access-Control-Allow-Methods",
						"GET, POST, PUT, DELETE, OPTIONS")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept")
				.build();

	}

	
}
