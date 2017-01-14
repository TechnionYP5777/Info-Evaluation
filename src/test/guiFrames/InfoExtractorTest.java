package test.guiFrames;

import main.guiFrames.InfoExtractor;
import main.guiFrames.PersonalInfo;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

/**
 * This class tests the functions of InfoExtractor
 * @author ward-mattar 
 */
public class InfoExtractorTest {
	@Test
	public void testStringWrapp() {
		assertNull(InfoExtractor.wrapString(null));
		assert "".equals(InfoExtractor.wrapString(""));
		assertEquals("a short string", InfoExtractor.wrapString("a short string"));
		assertEquals("Lil Wayne Tyga Rihanna Bow Wow\nThe Game Nicki Minaj Kevin McCall",
				InfoExtractor.wrapString("Lil Wayne Tyga Rihanna Bow Wow The Game Nicki Minaj Kevin McCall"));
		assertEquals(
				"In 2009, Brown received significant\nmedia attention after pleading\nguilty to felony assault of his\nthen girlfriend, singer Rihanna",
				InfoExtractor.wrapString(
						"In 2009, Brown received significant media attention after pleading guilty to felony assault of his then girlfriend, singer Rihanna"));
	}

	@Test
	public void testGetWikiURL() {
		InfoExtractor extractor = new InfoExtractor();
		assertNull(extractor.getWikiURL(null));
		assertEquals("https://en.wikipedia.org/wiki/Chris_Brown", extractor.getWikiURL("Chris Brown"));
		assertEquals("https://en.wikipedia.org/wiki/Hope_Solo", extractor.getWikiURL("Hope Solo"));
	}

	@Test
	public void testGetInfo() {
		List<PersonalInfo> act = new LinkedList<>();
		PersonalInfo exp = new PersonalInfo("Full name: ", "Hope Amelia Solo");
		act = new InfoExtractor().getCelebInfo("Hope Solo");
		assert act.contains(exp);
	}

	@Test
	public void testCelebsInfoMap() {
		InfoExtractor extractor = new InfoExtractor();
		extractor.getCelebInfo("Chris Brown");
		extractor.getCelebInfo("Chris Brown");
		assertEquals(1, extractor.celebsInfo.size());
		extractor.getCelebInfo("Hope Solo");
		assertEquals(2, extractor.celebsInfo.size());
	}
}
