package test.guiFrames;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import main.guiFrames.RefineTable;

/**
 * Tests for main.guiFrames.RefineTable fields function
 *
 * @author ward
 */
public class RefineTableFieldsTest {
	/**
	 * adds the field names of even table
	 */
	private static void addEventFieldNames(final RefineTable ¢) {
		¢.addField("Name");
		¢.addField("Date");
		¢.addField("Reason");
	}

	@Test
	public void testAddFieldNullSafe() {
		new RefineTable().addField(null);
	}

	@Test
	public void testAddField() {
		final RefineTable t = new RefineTable();
		addEventFieldNames(t);
		final ArrayList<String> f = new ArrayList<>();
		f.add("Name");
		f.add("Date");
		f.add("Reason");
		assertEquals(t.getFields(), f);
	}

	@Test
	public void testRemoveFieldNullSafe() {
		new RefineTable().removeField(null);
	}

	@Test
	public void testRemoveField() {
		final RefineTable t = new RefineTable();
		addEventFieldNames(t);
		assertEquals(t.getFields().size(), 3);
		t.removeField("Name");
		t.removeField("Date");
		t.removeField("Reason");
		assertEquals(t.getFields(), new ArrayList<>());
	}

	@Test
	public void testFieldExistsNullSafe() {
		assert !new RefineTable().fieldExist(null);
	}

	@Test
	public void testFieldExists() {
		final RefineTable t = new RefineTable();
		addEventFieldNames(t);
		assertEquals(t.getFields().size(), 3);
		assert t.fieldExist("Name");
		assert t.fieldExist("Reason");
		t.removeField("Name");
		t.removeField("Reason");
		assert !t.fieldExist("Name");
		assert !t.fieldExist("Reason");

		assert t.fieldExist("Date");
	}

}