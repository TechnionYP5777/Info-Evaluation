package infoeval.main.services;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import infoeval.main.mysql.TableEntry;
@RestController
public class TestController {
    @RequestMapping("/test")
    public List<TableEntry> test() {
    	List<TableEntry> $ = new ArrayList<>();
		Calendar cal1 = Calendar.getInstance();
		cal1.set(Calendar.YEAR, 1912);
		cal1.set(Calendar.MONTH, Calendar.JUNE);
		cal1.set(Calendar.DAY_OF_MONTH, 23);
		Date dateRep1 = (Date) cal1.getTime();
		cal1 = Calendar.getInstance();
		cal1.set(Calendar.YEAR, 1954);
		cal1.set(Calendar.MONTH, Calendar.JUNE);
		cal1.set(Calendar.DAY_OF_MONTH, 7);
		Date dateRep2 = (Date) cal1.getTime();
		TableEntry entry = new TableEntry(null, "Alan Turing", "Maida Vale", "Wilmslow", dateRep1, dateRep2);
		$.add(entry);

		cal1.set(Calendar.YEAR, 1886);
		cal1.set(Calendar.MONTH, Calendar.OCTOBER);
		cal1.set(Calendar.DAY_OF_MONTH, 16);
		dateRep1 = (Date) cal1.getTime();
		cal1 = Calendar.getInstance();
		cal1.set(Calendar.YEAR, 1973);
		cal1.set(Calendar.MONTH, Calendar.DECEMBER);
		cal1.set(Calendar.DAY_OF_MONTH, 1);
		dateRep2 = (Date) cal1.getTime();

		entry = new TableEntry(null, "David Ben-Gurion", "plonsk", "Ramat Gan", dateRep1, dateRep2);
		$.add(entry);
		return $;
    }
}
