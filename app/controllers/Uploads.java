package controllers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

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
		String allfilename = upfile.getName();
        /*获取文件除扩展名以外的名字*/
		String filename = allfilename.substring(0, allfilename.lastIndexOf("."));
		/* 获取文件扩展名 */
		String fileext = allfilename.substring(allfilename.lastIndexOf("."));
		/* 构成hash路径名 */
		String path = "/public/resourse/" + institute_sel + "/" + subject_sel + "/"
				+ hash + fileext;
		Files.copy(upfile, Play.getFile(path));
		/* 获取当前日期 */
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 Date date = new Date();
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
		filedatename.uploaddate = formatter.format(date);
		dao.AddFilename.addFile(filedatename);
		List list = new ArrayList();
		list.add(filedatename.id);
		list.add(filedatename.realName);
		response.contentType = "application/json";
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		Gson gson = new Gson();
		String listToJson = gson.toJson(list);
		renderText(listToJson);
		
	}
	/*修改文件名*/
	public static void alterFilename(String filename,long id){
	      List<Filename> list = Filename.find("id = ?", id).fetch();
	      list.get(0).realName = filename;
	      list.get(0).save();
		
	}

}
