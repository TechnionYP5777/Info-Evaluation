package infoeval.test.servicesTest;

import infoeval.main.mysql.SqlRunner;
import infoeval.main.mysql.TableEntry;
import infoeval.main.services.InfoevalServiceImp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author osherh
 * @since 22/6/17
 */

public class InfoevalServicesImpTest {

	@Mock
	private SqlRunner sqlRunner;

	@InjectMocks
	private InfoevalServiceImp infoevalServiesImp;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(infoevalServiesImp).build();
	}

	@Test
	public void getBornInPlaceYearTest() throws Exception {
		ArrayList<TableEntry> people = new ArrayList<TableEntry>();
		SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
		java.sql.Date sqlBirthDate = new java.sql.Date(df.parse("1870-12-07").getTime());
		java.sql.Date sqlDeathDate = new java.sql.Date(df.parse("1940-10-14").getTime());
		TableEntry te1 = new TableEntry("url1", "name1", "birthPlace1", "deathPlace1", sqlBirthDate, sqlDeathDate,
				"occupation1", "spouseName1", "spouseOccupation1", "photoLink1", "overview1", "birthCity1",
				"deathCity1");
		TableEntry te2 = new TableEntry("url2", "name2", "birthPlace2", "deathPlace2", sqlBirthDate, sqlDeathDate,
				"occupation2", "spouseName2", "spouseOccupation2", "photoLink2", "overview2", "birthCity2",
				"deathCity2");
		people.add(te1);
		people.add(te2);
		when(sqlRunner.getBornInPlaceBeforeYear("Casablanca", "1954")).thenReturn(people);

		mockMvc.perform(get("/Queries/Query2").param("place", "Casablanca").param("year", "1954"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$[0].birthPlace", is("birthPlace1")))
				.andExpect(jsonPath("$[0].occupation", is("occupation1")))
				.andExpect(jsonPath("$[1].photoLink", is("photoLink2"))).andExpect(jsonPath("$[1].name", is("name2")));
		verify(sqlRunner, times(1)).getBornInPlaceBeforeYear("Casablanca", "1954");
		verifyNoMoreInteractions(sqlRunner);
	}

	@Test
	public void differentDeathPlaceTest() throws Exception {
		ArrayList<TableEntry> people = new ArrayList<TableEntry>();
		SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
		java.sql.Date sqlBirthDate = new java.sql.Date(df.parse("1870-12-07").getTime());
		java.sql.Date sqlDeathDate = new java.sql.Date(df.parse("1940-10-14").getTime());
		TableEntry te1 = new TableEntry("url1", "name1", "birthPlace1", "deathPlace1", sqlBirthDate, sqlDeathDate,
				"occupation1", "spouseName1", "spouseOccupation1", "photoLink1", "overview1", "birthCity1",
				"deathCity1");
		TableEntry te2 = new TableEntry("url2", "name2", "birthPlace2", "deathPlace2", sqlBirthDate, sqlDeathDate,
				"occupation2", "spouseName2", "spouseOccupation2", "photoLink2", "overview2", "birthCity2",
				"deathCity2");
		people.add(te1);
		people.add(te2);
		when(sqlRunner.getDifferentDeathPlace()).thenReturn(people);

		mockMvc.perform(get("/Queries/Query1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$[0].birthPlace", is("birthPlace1")))
				.andExpect(jsonPath("$[0].photoLink", is("photoLink1")))
				.andExpect(jsonPath("$[1].occupation", is("occupation2")))
				.andExpect(jsonPath("$[1].name", is("name2")));
		verify(sqlRunner, times(1)).getDifferentDeathPlace();
		verifyNoMoreInteractions(sqlRunner);
	}
}
