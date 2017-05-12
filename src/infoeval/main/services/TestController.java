package infoeval.main.services;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import infoeval.main.mysql.TableEntry;
@RestController
public class TestController {
    @RequestMapping("/test")
    public List<TableEntry> test() {
    	List<TableEntry> $ = new ArrayList<>();
    	
    	//Date utilDate1 = (Date) Date.from(Instant.from(LocalDate.of(1912, 6, 23)));
    	//Date utilDate2 = (Date) Date.from(Instant.from(LocalDate.of(1954, 6, 7)));

    	java.sql.Date utilDate1 = java.sql.Date.valueOf( LocalDate.of(1912, 6, 23) );
    	java.sql.Date utilDate2 = java.sql.Date.valueOf( LocalDate.of(1954, 6, 7) );
    	
		TableEntry entry = new TableEntry(null, "Alan Turing", "Maida Vale", "Wilmslow", utilDate1, utilDate2);
		$.add(entry);

		utilDate1 = java.sql.Date.valueOf((LocalDate.of(1886, 10, 16)));
		utilDate2 = java.sql.Date.valueOf(LocalDate.of(1973, 12, 1));

		entry = new TableEntry(null, "David Ben-Gurion", "plonsk", "Ramat Gan", utilDate1, utilDate2);
		$.add(entry);
		return $;
    }
}
