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
import models.LinkMan;
import models.Seek_Help;
import models.Ask_Tips;
import models.Share_Tips;

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
		//response.setHeader("Content-Type", "application/json;charset=UTF-8");
		response.setHeader("Cache-Control","no-cache");
		Gson gson = new Gson();
		String listToJson = gson.toJson(list);
		renderText(listToJson);
	}
	
	/* 获取相应的学院以及相应的学科下载目录，并返回json格式 (没有完成当目录为空的时候所执行的操作) */
	public static void viewDownloadsList(String fileroute, String subject,int page) {
		List filenameCountList = new ArrayList();
		List<List> reJsonList =new ArrayList<List>();
		List<Filename>  refilelist = Filename.find("institute = ? And subject = ?",fileroute,subject).from((page-1)*3).fetch(3);
		long userFilenameCount =Filename.count("institute = ? And subject = ?",fileroute,subject);
		int pageCount = (int)(userFilenameCount / 3 + 1) ;
		filenameCountList.add(pageCount);
		reJsonList.add(refilelist);
		reJsonList.add(filenameCountList);
		response.contentType = "application/json";
		Gson gson = new Gson();
		String listToJson = gson.toJson(reJsonList);
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
	/* 根据登陆者的当前学期的课表显示推荐资源，并且获取该用户的信息（e.g.还有几条消息没有读取） */
	public static void viewInsMostDown() {
		List re_list = new ArrayList();
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
			alllistname = alllistname.subList(0, 9);
		}  
		/*获取用户的信息，获取后天提醒的消息数*/
		/*回答的消息数目*/
		List<Seek_Help> se_list = Seek_Help.find("seek_user = ?", session.get("user")).fetch();
		int size = 0 ;
		for(Seek_Help seek_Help : se_list){
		List<Ask_Tips> tips_list = Ask_Tips.find("tip_from_id = ? And tip_status = ?",seek_Help.id,1).fetch();
		size += tips_list.size();
		}
		/*分享的消息数目*/
		List<Share_Tips> share_list = Share_Tips.find("tip_to_name = ?", session.get("user")).fetch();
		size += share_list.size();
		/*获取好友列表*/
		List<LinkMan> link_list = LinkMan.find("host_name = ?", session.get("user")).fetch();
		List<String> linkman_list = new ArrayList<String>();
		for(LinkMan linkMan : link_list){
			linkman_list.add(linkMan.friend_name);
		}
		re_list.add(alllistname);
		re_list.add(size);
		re_list.add(linkman_list);
		listToJson = gson.toJson(re_list);
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
		//response.setHeader("Content-Type", "application/json;charset=UTF-8");
		response.setHeader("Cache-Control","no-cache");
		renderText(listToJson);
	}
	/*获取当前登陆者的联系人*/
	public static void viewLinkname(){
		List<LinkMan> list = LinkMan.find("host_name = ?", session.get("user")).fetch();
		Gson gson = new Gson();
		String listToJson = gson.toJson(list);
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		renderText(listToJson);
	}
}
