package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;
@Entity
@Table(name = "db_institute")
public class Institute extends Model{
	public String institute;
}
