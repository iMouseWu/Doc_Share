package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "db_user")
public class Users extends Model {
	public String nickname;
	public String username;
	public String mailaddress;
	public String isfirst/*如果是第一次登陆就为0，否着为1*/;
	public String statue;/*如果是1，那就是用户被禁止登陆了，如果为0就是正常的*/
}
