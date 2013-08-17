package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;
@Entity
@Table(name = "db_rescomment")
public class Rescomment extends Model{
public String resource_hashName;
public String  comment;
public String comment_date;
public String comment_user;
}
