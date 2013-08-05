package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tools.Assist;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import models.AllClass;
import models.Filename;
import models.Instituteinfo;

public class ViewResource extends BaseCore {
	/* 显示相应资源下载页面 */
	public static void viewDownloadsDetails(String hashName) {
		List<Filename> list = Filename.find("hashName = ?", hashName).fetch();
		Filename filename = list.get(0);
		String downLoadRoute = "?institute=" + filename.institute
				+ "&hashName=" + filename.hashName + "&subject="
				+ filename.subject;
		render(filename, downLoadRoute);
	}
	
	/* 显示相应学院的学科模块 */
	public static void viewSubject(String institute) {
			
		List<Instituteinfo> list = Instituteinfo
				.find("institute= ?", institute).fetch();
		response.contentType = "application/json";
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		Gson gson = new Gson();
		String listToJson = gson.toJson(list);
		renderText(listToJson);
	}
	
	/* 获取相应的学院以及相应的学科下载目录，并返回json格式 (没有完成当目录为空的时候所执行的操作) */
	public static void viewDownloadsList(String fileroute, String subject) {
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
	
	/* 显示最热的前10资源 */
	public static void viewMostDown() {
		List<Filename> list = Filename.find("order by downcount asc").fetch(10);
		Gson gson = new Gson();
		String listToJson = gson.toJson(list);
		response.contentType = "application/json";
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		renderText(listToJson);
	}
	
	/* 根据登陆者的当前学期的课表显示推荐资源 */
	public static void viewInsMostDown() {
		Gson gson = new Gson();
		String info = "[{'classname':'高数'},{'classname':'会计学'}]";
		List<AllClass> list = gson.fromJson(info,
				new TypeToken<List<AllClass>>() {
				}.getType());
		List<Filename> alllistname = new ArrayList<Filename>();
		for (AllClass allClass : list) {
			List<Filename> listname = Filename.find("subject = ?",
					allClass.classname).fetch();
			for (Filename a : listname) {
				alllistname.add(a);
			}
		}
		Assist assit = new Assist();
		Collections.sort(alllistname, assit);
		String listToJson = "";
		if (alllistname.size() > 10) {
			List returnlist = alllistname.subList(0, 9);
			listToJson = gson.toJson(returnlist);
		} else {
			listToJson = gson.toJson(alllistname);
		}
		response.contentType = "application/json";
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		renderText(listToJson);

	}
	/*返回学校学院目录*/
	public static void viewInstitute(){
		List instituteinfos = Instituteinfo.find("select p.institute from Instituteinfo p " +
			    "GROUP BY p.institute").fetch();
		Gson gson = new Gson();
		String listToJson = gson.toJson(instituteinfos);
		response.contentType = "application/json";
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		renderText(listToJson);
	}


}
