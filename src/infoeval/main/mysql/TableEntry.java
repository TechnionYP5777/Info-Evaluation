package infoeval.main.mysql;

import java.sql.Date;

/**
 * 
 * @author Netanel
 * @Since 07-05-2017
 *
 */

public class TableEntry{
	private String url;
	private String name;
	private String birthPlace;
	private String deathPlace;
	private Date birthDate;
	private Date deathDate;
	
	public TableEntry(){
		this.deathPlace=this.birthPlace = this.name = this.url = "";
		this.deathDate=null;
		this.birthDate=null;
		
	}
	
}