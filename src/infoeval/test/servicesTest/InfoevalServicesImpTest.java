package infoeval.test.servicesTest;

import infoeval.main.mysql.SqlRunner;
import infoeval.main.mysql.TableEntry;
import infoeval.main.services.Application;
import infoeval.main.services.InfoevalServiceImp;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.mockito.Mockito.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author osherh
 * @since 22/6/17
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class InfoevalServicesImpTest {

	@InjectMocks
	private InfoevalServiceImp infoevalServicesImp;

	@Mock
	private SqlRunner runner;

	private MockMvc mockMvc;

	// private HttpMessageConverter mappingJackson2HttpMessageConverter;

	// private MediaType contentType = new
	// MediaType(MediaType.APPLICATION_JSON.getType(),
	// MediaType.APPLICATION_JSON.getSubtype(),
	// Charset.forName("utf8"));
	/*
	 * @Autowired private WebApplicationContext webApplicationContext;
	 * 
	 * @Autowired void setConverters(HttpMessageConverter<?>[] converters) {
	 * this.mappingJackson2HttpMessageConverter =
	 * Arrays.asList(converters).stream() .filter(hmc -> hmc instanceof
	 * MappingJackson2HttpMessageConverter) .findAny() .orElse(null); }
	 */
	// @Before
	// public void setup() throws Exception {
	// this.mockMvc = webAppContextSetup(webApplicationContext).build();

	// }

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(infoevalServicesImp).build();
	}

	@Ignore
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
		when(runner.getBornInPlaceBeforeYear("Casablanca", "1954")).thenReturn(people);

		/*
		 * mockMvc.perform(get("/Queries/Query2")
		 * .accept(MediaType.APPLICATION_JSON)) .andExpect(status().isOk())
		 * .andExpect(content().contentType(MediaType.
		 * APPLICATION_JSON_UTF8_VALUE)) .andExpect(jsonPath("$", hasSize(2)))
		 * .andExpect(jsonPath("$[0].birthPlace", is("birthPlace1")))
		 * .andExpect(jsonPath("$[0].occupation", is("occupation1")))
		 * .andExpect(jsonPath("$[1].photoLink", is("photoLink2"))) //TODO:
		 * FIX!!! .andExpect(jsonPath("$[1].name", is("name2")));
		 * verify(infoevalServicesImp,
		 * times(1)).getBornInPlaceYear("Casablanca","1954");
		 * verifyNoMoreInteractions(infoevalServicesImp);
		 */

		MvcResult result = mockMvc.perform(get("/Queries/Query2").accept(MediaType.APPLICATION_JSON)).andReturn();

		String content = result.getResponse().getContentAsString();

		System.out.println(content);
	}
}
