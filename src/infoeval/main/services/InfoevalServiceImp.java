package infoeval.main.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author geniashand
 * @Since 06-05-2017
 * 
 *        This class is the functions of the web services
 *
 */

@EnableAutoConfiguration
@RestController
public class InfoevalServiceImp implements InfoevalService {
	@Override
	@RequestMapping("Queries/Query2")
	public List<String> getBornInPlaceYear() {
		List<String> $ = new ArrayList<String>();
		$.add("Yevgenia Shandalov");
		$.add("Moshiko Elisof");
		$.add("Netanel Felcher");
		$.add("Osher Hajaj");
		return $;

	}

	@Override
	public List<TableEntry> differentDeathPlace() {
		List<TableEntry> $ = new ArrayList<>();
		Calendar cal1 = Calendar.getInstance();
		cal1.set(Calendar.YEAR, 1912);
		cal1.set(Calendar.MONTH, Calendar.JUNE);
		cal1.set(Calendar.DAY_OF_MONTH, 23);
		Date dateRep1 = cal1.getTime();
		cal1 = Calendar.getInstance();
		cal1.set(Calendar.YEAR, 1954);
		cal1.set(Calendar.MONTH, Calendar.JUNE);
		cal1.set(Calendar.DAY_OF_MONTH, 7);
		Date dateRep2 = cal1.getTime();
		TableEntry entry = new TableEntry(null, "Alan Turing", "Maida Vale", "Wilmslow", dateRep1, dateRep2);
		$.add(entry);

		cal1.set(Calendar.YEAR, 1886);
		cal1.set(Calendar.MONTH, Calendar.OCTOBER);
		cal1.set(Calendar.DAY_OF_MONTH, 16);
		dateRep1 = cal1.getTime();
		cal1 = Calendar.getInstance();
		cal1.set(Calendar.YEAR, 1973);
		cal1.set(Calendar.MONTH, Calendar.DECEMBAR);
		cal1.set(Calendar.DAY_OF_MONTH, 1);
		dateRep2 = cal1.getTime();

		entry = new TableEntry(null, "David Ben-Gurion", "plonsk", "Ramat Gan", dateRep1, dateRep2);
		$.add(entry);
		// TODO Auto-generated method stub
		return $;
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(InfoevalServiceImp.class, args);
	}

}
