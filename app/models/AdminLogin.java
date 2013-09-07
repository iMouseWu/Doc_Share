package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;
@Entity
@Table(name = "db_admin")
public class AdminLogin extends Model{
public String username;
public String password;
}
