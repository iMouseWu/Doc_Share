package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "db_seekhelp")
public class Seek_Help extends Model {
	public String seek_user;
	public String seek_content;
	public String seek_title;
	public String seek_date;
	public int seek_status;/*0代表取消发布，1带表发布*/
//	@OneToMany(fetch=FetchType.LAZY,mappedBy="seek_Help", cascade=CascadeType.ALL)
//	public List<Re_Seek_Help> list;
}
