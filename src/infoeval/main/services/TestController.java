package infoeval.main.services;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author geniashand
 * @Since 12-05-2017
 * 
 *        This class was created for tutorial use and trial and error. 
 *        Will be deleted once we figure out how the ionic + REST work
 *
 */

import infoeval.main.mysql.TableEntry;

public class TestController {

	@Test
	public void test1() throws Exception{
		ArrayList<TableEntry> lst = (new InfoevalServiceImp()).getBornInPlaceYear("Casablanca","1954");
		
	}
	
    }

