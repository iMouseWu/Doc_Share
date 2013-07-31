package controllers;

import play.*;
import play.libs.Files;
import play.mvc.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

import org.eclipse.jdt.core.dom.ThisExpression;
import org.hamcrest.core.Is;
import org.hibernate.mapping.Array;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.And;

import models.*;

public class Application extends Controller {

	public static void index() {
		render();
	}

	/* 上传功能 */
	public static void uploads(File upfile, String institute, String subject) {
		/* 获取文件哈希码 */
		int hash = upfile.hashCode();
		/* 获取文件名字 */
		String filename = upfile.getName();
		/* 获取文件扩展名 */
		String fileext = filename.substring(filename.lastIndexOf("."));
		/* 构成hash路径名 */
		String path = "/public/resourse/" + institute + "/" + subject + "/"
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
		filedatename.institute = institute;
		filedatename.subject = subject;
		filedatename.downcount = 0;
		filedatename.uploadname = user;
		filedatename.uploaddate = datenow.toString();
		dao.AddFilename.addFile(filedatename);
	}

	/* 获取相应的下载目录，并返回json格式 (没有完成当目录为空的时候所执行的操作) */
	public static void downloads(String fileroute, String subject) {
		List<Filename> refileList = new ArrayList();
		// File file=new File(".");
		// System.out.println(file.getAbsolutePath());
		File listfile = new File("./public/resourse/" + fileroute + "/"
				+ subject);
		String[] list = listfile.list();
		for (String name : list) {
			List<Filename> filelist = Filename.find("hashName", name).fetch();
			Filename filename = filelist.get(0);
			refileList.add(filename);
		}
		response.contentType = "application/json";
		Gson gson = new Gson();
		String listToJson = gson.toJson(refileList);
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		renderText(listToJson);
	}

	/* 统计下载次并且实现下载链接的重定向 */
	public static void addCount(String institute, String hashName,
			String subject) throws UnsupportedEncodingException {
		List<Filename> list = Filename.find("hashName", hashName).fetch();
		Filename filename = list.get(0);
		filename.downcount = filename.downcount + 1;
		filename.save();
		String msg = URLEncoder.encode(institute, "UTF-8");
		String sub = URLEncoder.encode(subject, "UTF-8");
		redirect("/public/resourse/" + msg + "/" + sub + "/" + hashName);
	}

	/* 显示最热的前10资源 */
	public static void viewMostDown() {
		List<Filename> list = Filename.find("order by downcount asc").fetch(10);
		response.contentType = "application/json";
		Gson gson = new Gson();
		String listToJson = gson.toJson(list);
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		renderText(listToJson);
	}

	/* 显示该学生所在学院热门的前10资源 */
	public static void viewInsMostDown(String institute) {
		List<Filename> list = Filename.find(
				"institute = ? order by downcount asc", institute).fetch(10);
		response.contentType = "application/json";
		Gson gson = new Gson();
		String listToJson = gson.toJson(list);
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		renderText(listToJson);

	}

	/* 登录验证 */
	public static void loginCheck(String username, String password) {
		// List<Users> list = Users.find(
		// "select u from Users u where u.username=:" + username
		// + " and u.password=:" + password).fetch();
		List<Users> list = Users.find("username= ? And password=?", username,
				password).fetch();
		if (list.size() == 0) {
			response.contentType = "application/json";
			response.setHeader("Content-Type", "application/json;charset=UTF-8");
			renderText("");
		} else {
			String institute = list.get(0).institute;
			session.put("user", list.get(0).username);
			viewInsMostDown(institute);
		}
	}

	/* 搜索资源(缺少资源没有处理过程) */
	public static void searchRes(String searchname) {
		List<Filename> list = Filename.find("realName like ?",
				"%" + searchname + "%").fetch();
		response.contentType = "application/json";
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		Gson gson = new Gson();
		if (list.size() == 0) {
			renderText("");
		} else {
			String listToJson = gson.toJson(list);
			renderText(listToJson);
		}

	}

	/* 返回相应学院的学科模块 */
	public static void searchSub(String institute) {
		List<instituteinfo> list = instituteinfo
				.find("institute= ?", institute).fetch();
		response.contentType = "application/json";
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		Gson gson = new Gson();
		String listToJson = gson.toJson(list);
		renderText(listToJson);
	}

	/* 显示相应资源下载页面 */
	public static void viewDownloads(String hashName) {
		List<Filename> list = Filename.find("hashName = ?", hashName).fetch();
		Filename filename = list.get(0);
		String downLoadRoute = "?institute="
				+ filename.institute + "&hashName=" + filename.hashName
				+ "&subject=" + filename.subject;
		render(filename,downLoadRoute);

	}
}