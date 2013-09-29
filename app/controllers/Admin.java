package controllers;

import java.io.File;
import java.util.List;

import play.Play;
import play.db.jpa.JPABase;
import play.libs.Files;
import play.mvc.Before;
import models.AdminLogin;
import models.Filename;
import models.Institute;
import models.Instituteinfo;
import models.LinkMan;
import models.Seek_Help;
import models.Users;

public class Admin extends BaseCore {
	@Before(unless={"admin","adminlogin"})
	 static void checkAuthentification() {
	  if(session.get("adminusername") == null || session.get("adminpassword") == null) 
		  admin();
	    }
	
	public static void admin(){
		if(session.get("adminusername") == null || session.get("adminpassword") == null)
		render();
		else 
	    render("Admin/adminlogin.html");
	}
	 
	/*admin登录验证*/
	public static void adminlogin(String username,String password){
		List<AdminLogin> list = AdminLogin.find("username = ? And password = ?", username,password).fetch();
		if(list.size() == 0){
			admin();
		}
		else{
			session.put("adminusername", username);
			session.put("adminpassword", password);
			render();
		}
	}
	/*欢迎界面*/
	public static void welcome(){
		render();
	}
/************************************************资源的增删改除************************************************/
	public static void admin_resource(int page){
		if(page == 0){
			page = 1;
		}
		int allpage;
		int count = (int)Filename.count();
		if(count % 10 == 0){
			allpage = count / 10;
		}else{
			allpage = count / 10 + 1;
		}
		List<Filename> filelist = Filename.all().from((page - 1) * 10).fetch(10);
		render(filelist,page,allpage);
	}
	
	/*跳转到资源修改界面*/
	public static void vieweditResource(int page,long id){
		List<Institute> list_institute = Institute.findAll();
		List<Instituteinfo> list_suject = Instituteinfo.findAll();
		Filename filename = Filename.findById(id);
		render(list_institute,list_suject,filename,page);
	}
	
	/*修改资源*/
	public static void editResource(String realname,String institute,String subject,long id,int page){
		Filename filename = Filename.findById(id);
		String oldpath = "/public/resourse/" + filename.institute + "/" + filename.subject + "/"
				+ filename.hashName;
		String newpath = "/public/resourse/" + institute + "/" + subject + "/"
				+ filename.hashName;
		filename.realName = realname;
		filename.institute = institute;
		filename.subject = subject;
		/*修改数据库信息*/
		dao.AddResources.addFile(filename);
		/*修改文件夹信息*/
		File oldFile = Play.getFile(oldpath);
		Files.copy(oldFile, Play.getFile(newpath));
		oldFile.delete();
		admin_resource(page);
	}
	
	 /*删除资源*/
    public static void deleteResource(int page,long id){
    	Filename filename = Filename.findById(id);
    	String path = "/public/resourse/" + filename.institute + "/" + filename.subject + "/"
				+ filename.hashName;
    	/*删除数据库信息*/
    	filename.delete();
    	/*删除文件夹文件*/
    	Play.getFile(path).delete();
    	admin_resource(page);
    }
    /*资源搜索*/
    public static void searchResource(int page,String serach_name){
    	int allpage;
		if(page == 0){
			page = 1;
		}
		List<Filename> filelist_number = Filename.find("realName = ?", serach_name).fetch();
		int count = filelist_number.size();
		List<Filename> filelist = Filename.find("realName = ?", serach_name).from((page - 1) * 10).fetch(10);
		
		if(count % 10 == 0){
			allpage = count / 10;
		}else{
			allpage = count / 10 + 1;
		}
		renderTemplate("Admin/admin_resource.html",allpage,filelist,page);
    }
/*******************************************求助的增删改除******************************************/
	public static void admin_help(int page){
		if(page == 0){
			page = 1;
		}
		int allpage;
		int count = (int)Seek_Help.count();
		if(count % 10 == 0){
			allpage = count / 10;
		}else{
			allpage = count / 10 + 1;
		}
		List<Seek_Help> filelist = Seek_Help.all().from((page - 1) * 10).fetch(10);
		render(filelist,page,allpage);
	}
	
	/*跳转帮助修改界面*/
	public static void vieweditHelp(int page,long id){
		Seek_Help seek_Help = Seek_Help.findById(id);
		render(seek_Help,page);
	}
	
	/*修改帮助*/
    public static void editHelp(long id,int page,String seek_user,String seek_content){
		Seek_Help seek_Help = Seek_Help.findById(id);
		seek_Help.seek_user = seek_user;
		seek_Help.seek_content = seek_content;
		seek_Help.save();
		admin_help(page);
	}
    
    /*删除帮助*/
    public static void deleteHelp(int page,long id){
    	Seek_Help seek_Help = Seek_Help.findById(id);
    	seek_Help.delete();
    	admin_help(page);
    }
    /*查找帮助*/
    public static void searchHelp(String serach_name,int page){
		int allpage;
		if(page == 0){
			page = 1;
		}
		List<Seek_Help> filelist_number = Seek_Help.find("seek_user = ?", serach_name).fetch();
		int count = filelist_number.size();
		List<Seek_Help> filelist = Seek_Help.find("seek_user = ?", serach_name).from((page - 1) * 10).fetch(10);
		if(count % 10 == 0){
			allpage = count / 10;
		}else{
			allpage = count / 10 + 1;
		}
		renderTemplate("Admin/admin_help.html",filelist,page,allpage);
    }
    
/*************************************************用户的增删改除**********************************************/
	public static void admin_user(int page){
		if(page == 0){
			page = 1;
		}
		int allpage;
		int count = (int)Users.count();
		if(count % 10 == 0){
			allpage = count / 10;
		}else{
			allpage = count / 10 + 1;
		}
		List<Users> filelist = Users.all().from((page - 1) * 10).fetch(10);
		render(filelist,page,allpage,allpage);
	}
	
	/*跳转用户信息修改界面*/
	public static void vieweditUser(int page,long id){
		Users users = Users.findById(id);
		render(users,page);
	}
	
	/*修改用户信息*/
    public static void editUser(long id,int page,String username,String password,String mailaddress,String mailpassword,String institute){
	Users users = Users.findById(id);
	users.institute = institute;
	users.username = username;
	users.password = password;
	users.mailaddress = mailaddress;
	users.mailpassword = mailpassword;
	users.save();
	admin_user(page);
    }
    
    /*删除用户*/
    /*删除用户的同时，需要把相应的好友也删除*/
    public static void deleteUser(int page,long id){
    	Users users = Users.findById(id);
    	users.delete();
    	/*删除好友*/
    	List<LinkMan> list = LinkMan.find("friend_name = ?", users.nickname).fetch();
    	for(LinkMan linkMan : list){
    		linkMan.delete();
    	}
    	admin_user(page);
    }
    /*查找用户*/
    public static void searchUser(String serach_username,int page){
		int allpage;
		if(page == 0){
			page = 1;
		}
		List<Users> filelist_number = Users.find("username = ?", serach_username).fetch();
		int count = filelist_number.size();
		List<Users> filelist = Users.find("username = ?", serach_username).from((page - 1) * 10).fetch(10);
		if(count % 10 == 0){
			allpage = count / 10;
		}else{
			allpage = count / 10 + 1;
		}
		renderTemplate("Admin/admin_user.html",filelist,page,allpage);
    }
    /**
     * @statue为状态码，0为刚进入页面，1为操作成功，2为命名重复，3为命名空值。
     * 
     * /
/*************************************************学科的增删改除*********************************************/
	public static void admin_subject(int statue,int page){
		if(page == 0){
			page = 1;
		}
		int allpage;
		int count = (int)Instituteinfo.count();
		if(count % 10 == 0){
		allpage = count / 10;
		}else{
		allpage = count / 10 + 1;
		}
		List<Instituteinfo> filelist = Instituteinfo.all().from((page - 1) * 10).fetch(10);
		List<Institute> listinstitute = Institute.findAll();
		render(filelist,page,listinstitute,allpage,statue);
	}
	
	/*跳转学科信息修改界面*/
	public static void vieweditSubject(int page,long id){
		Instituteinfo instituteinfo = Instituteinfo.findById(id);
		render(instituteinfo,page);
	}
	
	/*修改学科信息*/
    public static void editSubject(long id,int page,String subject){
    	if(subject.equals("")){
    		int statue = 3;
        	admin_subject(statue,page);
    	}else{
    	Instituteinfo instituteinfo = Instituteinfo.findById(id);
    	List<Filename> list = Filename.find("subject = ?", instituteinfo.subject).fetch();
    	for(Filename filename : list){
    		filename.subject = subject;
    		dao.AddResources.addFile(filename);
    	}
    	/*学科信息修改以后，需要修改原来数据库的文件的信息，还要修改文件夹的名字*/
    	String oldpath = "/public/resourse/" + instituteinfo.institute + "/" + instituteinfo.subject;
    	String newpath = "/public/resourse/" + instituteinfo.institute + "/" + subject;
    	File oldFile = Play.getFile(oldpath);
    	File newFile = Play.getFile(newpath);
    	boolean result = oldFile.renameTo(newFile);	
    	/*需要判断是否修改成功(因为会发生重命名的现象)*/
    	if(result == false){
    		int statue = 2;
    		admin_subject(statue,page);
    	}else{
    		int statue = 1;
    		instituteinfo.subject = subject;
        	instituteinfo.save();
        	admin_subject(statue,page);
    	}
    	}
    	}
    
//    /*删除学科*/
//    public static void deleteSubject(int page,long id){
//    	Instituteinfo instituteinfo = Instituteinfo.findById(id);
//    	instituteinfo.delete();
//    	
//    	/*需要删除文件夹，还需要删除数据库里面的文件*/
//    	
//    	admin_subject(page);
//    }
    /*查找学科*/
    public static void searchSubject(int page,String serach_subject){
    	int allpage;
		if(page == 0){
			page = 1;
		}
		List<Instituteinfo> filelist_number = Instituteinfo.find("subject = ?", serach_subject).fetch();
		int count = filelist_number.size();
		List<Instituteinfo> filelist = Instituteinfo.find("subject = ?", serach_subject).from((page - 1) * 10).fetch(10);
		if(count % 10 == 0){
			allpage = count / 10;
		}else{
			allpage = count / 10 + 1;
		}
		renderTemplate("Admin/admin_subject.html",filelist,page,allpage);
    }
    /*增加学科*/
    public static void addSubject(String institute,String subject){
    	int statue;
    	if(subject.equals("")){
    		statue = 3;
    		admin_subject(statue,0);
    	}else{
    		
	    	File file = Play.getFile("/public/resourse/" + institute + "/" + subject);
	    	boolean result = file.mkdir();
    		if(result == false){
    			statue = 2;
    	    	admin_subject(statue,0);
    		}else{
    			Instituteinfo instituteinfo  = new Instituteinfo();
    	    	instituteinfo.institute = institute;
    	    	instituteinfo.subject = subject;
    	    	instituteinfo.save();
    			statue = 1;
    	    	admin_subject(statue,0);
    		}
    	}
    }
    /**
     * @statue为状态码，0为刚进入页面，1为操作成功，2为命名重复，3为命名空值。
     * 
     * /
/*********************************************学院的增删改除*****************************************/
   
	public static void admin_institute(int statue,int page){
		if(page == 0){
			page = 1;
		}
		int allpage;
		int count = (int)Institute.count();
		if(count % 10 == 0){
			allpage = count / 10;
		}else{
			allpage = count / 10 + 1;
		}
		List<Institute> filelist = Institute.all().from((page - 1) * 10).fetch(10);
		render(filelist,page,allpage,statue);
	}
	
	/*跳转学院信息修改界面*/
	public static void vieweditInstitute(int page,long id){
		Institute institute = Institute.findById(id);
		render(institute,page);
	}
	
    /*修改学院信息*/
    public static void editInstitute(long id,int page,String institute){
    	if(institute.equals("")){
    		int statue = 3;
    		admin_institute(statue,page);
    	}else{
    	Institute _institute = Institute.findById(id);
    	List<Filename> list = Filename.find("institute = ?", _institute.institute).fetch();
    	for(Filename filename : list){
    		filename.institute = institute;
    		dao.AddResources.addFile(filename);
    	}
    	/*学科信息修改以后，需要修改原来数据库的文件的信息，还要修改文件夹的名字*/
    	String oldpath = "/public/resourse/" + _institute.institute ;
    	String newpath = "/public/resourse/" + institute ;
    	File oldFile = Play.getFile(oldpath);
    	File newFile = Play.getFile(newpath);
    	boolean result = oldFile.renameTo(newFile);	
    	/*需要判断是否修改成功(因为会发生重命名的现象)*/
    	if(result == false){
    		int statue = 2;
    		admin_institute(statue,page);
    	}else{
    		int statue = 1;
    		_institute.institute = institute;
        	_institute.save();
        	admin_institute(statue,page);
    	}
    	}
    }
   
//   /*删除学院*/
//    public static void deleteInstitute(int page,long id){
//    	Institute institute = Institute.findById(id);
//    	institute.delete();
//    	int statue = 1;
//    	/*需要删除文件夹，还需要删除数据库里面的文件*/
//    	admin_institute(statue,page);
//    }
    /*查找学院*/
    public static void searchInstitute(int page,String serach_institute){
    	int allpage;
		if(page == 0){
			page = 1;
		}
		List<Institute> filelist_number = Institute.find("institute = ?", serach_institute).fetch(10);
		int count = filelist_number.size();
		List<Institute> filelist = Institute.find("institute = ?", serach_institute).from((page - 1) * 10).fetch(10);
		if(count % 10 == 0){
			allpage = count / 10;
		}else{
			allpage = count / 10 + 1;
		}
		
		renderTemplate("Admin/admin_institute.html",filelist,page,allpage);
    }
    /*增加学院*/
    public static void addInstitute(String add_institute){
    	int statue;
    	if(add_institute.equals("")){
    		statue = 3;
    		admin_institute(statue,0);
    	}else{
    	Institute institute =new Institute();
    	institute.institute = add_institute;
    	institute.save();
    	File file = Play.getFile("/public/resourse/" + add_institute);
    	boolean result = file.mkdir();
    	if(result == true){
    		statue = 1;
    		admin_institute(statue,0);
    	}else{
    		statue = 2;
    		admin_institute(statue,0);
    	}
    	
    	}
    }
    
}
