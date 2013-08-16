package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;
@Entity
@Table(name = "db_linkman")
public class LinkMan extends Model{
	public String host_name;
	public String friend_name;
	public String firend_group;
}
