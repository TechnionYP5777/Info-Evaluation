package main.guiFrames;

/**
 * This class holds a personal attribute for a celeb
 * @author ward-mattar
 */
public class PersonalInfo {
	public PersonalInfo(final String name, final String value) {
		if (name == null || value == null)
			throw new NullPointerException();
		attributeName = String.valueOf(name);
		attributeValue = String.valueOf(value);

	}

	@Override
	public boolean equals(Object ¢) {
		if (¢ == null || !(¢ instanceof PersonalInfo))
			throw new NullPointerException();
		return ((PersonalInfo) ¢).attributeName.equals(attributeName)
				&& ((PersonalInfo) ¢).attributeValue.equals(attributeValue);
	}

	public String getAttribute() {
		return attributeName;
	}

	public String getInfo() {
		return attributeValue;
	}

	private String attributeName;
	private String attributeValue;

}
