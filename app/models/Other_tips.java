package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;
@Entity
@Table(name = "db_other_tips")
public class Other_tips extends Model{
	public String tip_content;
	public String tip_date;
	public String to_name;
	public String to_no;
	public int tip_status;/*1表示没有读，0表示已经读了*/
}
