package test.guiFrames;

import static org.junit.Assert.*;
import org.junit.Test;

import main.guiFrames.PersonalInfo;

/**
 * This function test the tyoe PersonalInfo
 * 
 * @author ward-mattar
 */
public class PersonalInfoTest {

	@Test
	public void testNull() {
		try {
			new PersonalInfo(null, null);
			assert false;
		} catch (NullPointerException e) {

		}

	}

	@Test
	public void testEquals() {
		assertEquals(new PersonalInfo("Place of Birth", "Boston, Massachusetts"),
				new PersonalInfo("Place of Birth", "Boston, Massachusetts"));
		assertNotEquals(new PersonalInfo("Place of Birth", "Boston, Massachusetts"),
				new PersonalInfo("Full name:", "Ward Mattar"));
		try {
			assertEquals(new PersonalInfo("Place of Birth", "Boston, Massachusetts"), "Boston, Massachusetts");
			assert false;
		} catch (Exception e) {
		}

	}

	@Test
	public void testGetAttribute() {
		assertEquals((new PersonalInfo("Place of Birth", "Boston, Massachusetts")).getAttribute(), "Place of Birth");
	}

	@Test
	public void testGetInfo() {
		assertEquals((new PersonalInfo("Place of Birth", "Boston, Massachusetts")).getInfo(), "Boston, Massachusetts");
	}
}
