package infoeval.main.mysql;

import java.sql.Date;

/**
 * 
 * @author Netanel
 * @Since 07-05-2017
 * 
 * This class represents an entry in the mySql table.
 * It is used in order to pass the data from the DB to the GUI
 *
 */

public class TableEntry{
	private String url;
	private String name;
	private String birthPlace;
	private String deathPlace;
	private Date birthDate;
	private Date deathDate;
	//C'tors
	public TableEntry(){
		this.deathPlace=this.birthPlace = this.name = this.url = "";
		this.deathDate=null;
		this.birthDate=null;
		
	}
	public TableEntry(String url, String name, String birthPlace, String deathPlace, Date birthDate, Date deathDate){
		setUrl(url);
		setName(name);
		setBirthPlace(birthPlace);
		setDeathPlace(deathPlace);
		setBirthDate(birthDate);
		setDeathDate(deathDate);
		
	}
	//Setters
	public void setUrl(String url){
		this.url=url;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setBirthPlace(String birthPlace){
		this.birthPlace=birthPlace;
	}
	public void setDeathPlace(String deathPlace){
		this.deathPlace=deathPlace;
	}
	public void setBirthDate(Date birthDate){
		this.birthDate=birthDate;
	}
	public void setDeathDate(Date deathDate){
		this.deathDate=deathDate;
	}
	//Getters
	public String getUrl(){
		return this.url;
	}
	public String getName(){
		return this.name;
	}
	public String getBirthPlace(){
		return this.birthPlace;
	}
	public String getDeathPlace(){
		return this.deathPlace;
	}
	public Date getBirthDate(){
		return this.birthDate;
	}
	public Date getDeathDate(){
		return this.deathDate;
	}
	
}