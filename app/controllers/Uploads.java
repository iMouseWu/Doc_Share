package controllers;

import java.io.File;
import java.util.Date;

import models.Filename;
import play.Play;
import play.libs.Files;
import play.mvc.Controller;

public class Uploads extends BaseCore{
	
	/* 上传功能 */
	public static void uploads(File upfile, String institute_sel, String subject_sel) {
		/* 获取文件哈希码 */
		int hash = upfile.hashCode();
		/* 获取文件名字 */
		String filename = upfile.getName();
		/* 获取文件扩展名 */
		String fileext = filename.substring(filename.lastIndexOf("."));
		/* 构成hash路径名 */
		String path = "/public/resourse/" + institute_sel + "/" + subject_sel + "/"
				+ hash + fileext;
		Files.copy(upfile, Play.getFile(path));
		/* 获取当前日期 */
		Date datenow = new Date();
		/* 获取上传者的姓名 */
		String user = session.get("user");
		/* 将此资源储存到数据库 */
		Filename filedatename = new Filename();
		filedatename.hashName = hash + fileext;
		filedatename.realName = filename;
		filedatename.institute = institute_sel;
		filedatename.subject = subject_sel;
		filedatename.downcount = 0;
		filedatename.uploadname = user;
		filedatename.uploaddate = datenow.toString();
		dao.AddFilename.addFile(filedatename);
	}

}
