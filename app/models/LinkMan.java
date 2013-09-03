package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;
@Entity
@Table(name = "db_linkman")
public class LinkMan extends Model{
	public String friend_name;/*firend_name就是user表里面的nickname*/
	@ManyToOne
	public Linkgroup linkgroup;
}	
	
