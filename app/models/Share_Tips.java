package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;
@Entity
@Table(name = "db_share_tips")
public class Share_Tips extends Model{
	public String tip_from_name;
	public String tip_content;
	public String tip_date;
	public int tip_status;/*1表示没有读，0表示已经读了*/
	public String tip_to_name;

}
