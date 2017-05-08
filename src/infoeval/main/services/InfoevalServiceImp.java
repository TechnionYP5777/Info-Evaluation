package infoeval.main.services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
@Path("Queries")
public class InfoevalServiceImp implements InfoevalService {

	@GET
	@Path("Query2")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public List<String> getBornIn(String place, int year) {
		List<String> retval=new ArrayList<String>();
		retval.add("Yevgenia Shandalov");
		retval.add("Moshiko Elisof");
		retval.add("Netanel Felcher");
		retval.add("Osher Hajaj");
		return retval;
	}

	@Override
	public List<String> differentDeathPlace() {
		// TODO Auto-generated method stub
		return null;
	}

}
