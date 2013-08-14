package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;
@Entity
@Table(name = "db_reseekhelp")
public class Re_Seek_Help extends Model{
	public String re_seek_user;
	public String re_seek_content;
	public String re_seek_date;
	@ManyToOne 
	public Seek_Help seek_Help;
}
