package test.RefineTable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import main.guiFrames.RefineTable;

/**
 * Tests for main.guiFrames.RefineTable
 * 
 * @author ward
 */
public class RefineTableTest {
	/**
	 * adds the field names of even table
	 */
	private static void addEventFieldNames(RefineTable ¢) {
		¢.addField("Name");
		¢.addField("Date");
		¢.addField("Reason");
	}

	@Test
	public void testAddFieldNullSafe() {
		(new RefineTable()).addField(null);
	}

	@Test
	public void testAddField() {
		RefineTable t = new RefineTable();
		addEventFieldNames(t);
		ArrayList<String> f = new ArrayList<>();
		f.add("Name");
		f.add("Date");
		f.add("Reason");
		assertEquals(t.getFields(), f);
	}

	@Test
	public void testRemoveFieldNullSafe() {
		(new RefineTable()).removeField(null);
	}

	@Test
	public void testRemoveField() {
		RefineTable t = new RefineTable();
		addEventFieldNames(t);
		assertEquals(t.getFields().size(), 3);
		t.removeField("Name");
		t.removeField("Date");
		t.removeField("Reason");
		assertEquals(t.getFields(), new ArrayList<>());
	}

	@Test
	public void testFieldExistsNullSafe() {
		assertFalse(new RefineTable().fieldExist(null));
	}

	@Test
	public void testFieldExists() {
		RefineTable t = new RefineTable();
		addEventFieldNames(t);
		assertEquals(t.getFields().size(), 3);
		assertTrue(t.fieldExist("Name"));
		assertTrue(t.fieldExist("Reason"));
		t.removeField("Name");
		t.removeField("Reason");
		assertFalse(t.fieldExist("Name"));
		assertFalse(t.fieldExist("Reason"));

		assertTrue(t.fieldExist("Date"));
	}
}
