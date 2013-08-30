package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;
@Entity
@Table(name = "db_linkgroup")
public class Linkgroup extends Model{
	public String host_name;
	public String firend_group;
}
