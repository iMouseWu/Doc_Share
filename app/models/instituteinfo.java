package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "db_instituteinfo")
public class instituteinfo extends Model {
	public String institute;
	public String subject;

}
