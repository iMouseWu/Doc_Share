package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "db_user")
public class Users extends Model {
	public String username;
	public String password;
	public String institute;
	public String mailaddress;
	public String mailpassword;
}
