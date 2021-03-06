package infoeval.main.mysql;

import java.sql.Date;

/**
 * 
 * @author Netanel
 * @Since 07-05-2017
 * 
 *        This class represents an entry in the mySql table. It is used in order
 *        to pass the data from the DB to the GUI
 *
 */

public class TableEntry {
	private String url;
	private String name;
	private String birthPlace;
	private String deathPlace;
	private String birthExpandedPlace;
	private String deathExpandedPlace;
	private Date birthDate;
	private Date deathDate;
	private String occupation;
	private String SpouseName;
	private String spouseOccupation;
	private String photoLink;
	private String overview;

	// C'tors
	public TableEntry() {
		this.deathPlace = this.birthPlace = this.name = this.url = this.occupation = this.SpouseName = this.spouseOccupation = this.photoLink = this.overview = this.birthExpandedPlace = this.deathExpandedPlace = "";
		this.deathDate = null;
		this.birthDate = null;
	}

	public TableEntry(TableEntry te) {
		this(te.getUrl(), te.getName(), te.getBirthPlace(), te.getDeathPlace(), te.getBirthDate(), te.getDeathDate(),
				te.getOccupation(), te.getSpouseName(), te.getSpouseOccupation(), te.getPhotoLink(), te.getOverview(),
				te.getBirthExpandedPlace(), te.getDeathExpandedPlace());
	}

	public TableEntry(String url, String name, String birthPlace, String deathPlace, Date birthDate, Date deathDate,
			String occupation, String spouseName, String spouseOccupation, String link, String overview,
			String birthExpandedPlace, String deathExpandedPlace) {
		setUrl(url);
		setName(name);
		setBirthPlace(birthPlace);
		setDeathPlace(deathPlace);
		setBirthDate(birthDate);
		setDeathDate(deathDate);
		setOccupation(occupation);
		setSpouseName(spouseName);
		setSpouseOccupation(spouseOccupation);
		setPhotoLink(link);
		setOverview(overview);
		setBirthExpandedPlace(birthExpandedPlace);
		setDeathExpandedPlace(deathExpandedPlace);
	}

	// Setters
	public void setBirthExpandedPlace(String birthExpandedPlace) {
		this.birthExpandedPlace = birthExpandedPlace;
	}

	public void setDeathExpandedPlace(String deathExpandedPlace) {
		this.deathExpandedPlace = deathExpandedPlace;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public void setDeathPlace(String deathPlace) {
		this.deathPlace = deathPlace;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public void setSpouseName(String name) {
		this.SpouseName = name;
	}

	public void setSpouseOccupation(String occupation) {
		this.spouseOccupation = occupation;
	}

	public void setPhotoLink(String link) {
		this.photoLink = link;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}

	// Getters
	public String getBirthExpandedPlace() {
		return this.birthExpandedPlace;
	}

	public String getDeathExpandedPlace() {
		return this.deathExpandedPlace;
	}

	public String getUrl() {
		return this.url;
	}

	public String getName() {
		return this.name;
	}

	public String getBirthPlace() {
		return this.birthPlace;
	}

	public String getDeathPlace() {
		return this.deathPlace;
	}

	public Date getBirthDate() {
		return this.birthDate;
	}

	public Date getDeathDate() {
		return this.deathDate;
	}

	public String getOccupation() {
		return this.occupation;
	}

	public String getSpouseName() {
		return this.SpouseName;
	}

	public String getSpouseOccupation() {
		return this.spouseOccupation;
	}

	public String getPhotoLink() {
		return this.photoLink;
	}

	public String getOverview() {
		return this.overview;
	}
}