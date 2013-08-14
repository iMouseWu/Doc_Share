package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "db_tips")
public class Tips extends Model{
	public String tip_from_name;
	public String tip_content;
	public String tip_date;
	public int tip_status;/*1表示没有读，0表示已经读了*/
	public String tip_type;/*tip的类型 1.分享 2.问答*/
	public long tip_from_id;/*通过此ID反项可以知道此条信息是有什么事件产生的*/
}
