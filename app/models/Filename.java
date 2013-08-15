package models;
import javax.persistence.Entity;
import javax.persistence.Table;
import play.db.jpa.Model;
@Entity
@Table(name = "db_file")
public class Filename extends Model {
	public String hashName;
	public String realName;
	public String institute;
	public int downcount;
	public String uploadname;
	public String uploaddate;
	public String subject;
	public float avescore;
	public int numberval;
	public String intro;

}
