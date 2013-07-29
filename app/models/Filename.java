package models;


import javax.persistence.Entity;

import play.db.jpa.Model;
@Entity
public class Filename extends Model {
	public String hashName;
	public String realName;

}
