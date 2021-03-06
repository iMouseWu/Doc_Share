package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.httpclient.HttpException;

import play.mvc.Before;
import tools.Assist;
import tools.GetClass;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import models.Filename;
import models.Institute;
import models.Instituteinfo;
import models.LinkMan;
import models.Linkgroup;
import models.Other_tips;
import models.Rescomment;
import models.Seek_Help;
import models.Ask_Tips;
import models.Share_Tips;
import models.Users;

public class ViewResource extends BaseCore {
	@Before(only={"viewInsMostDown","viewLinkname"})
	static void checksession() {
		if (session.get("user") == null) {
			String tipinfo = "请登录";
			Application.index(tipinfo);
		}
	}
	/* 显示相应资源下载页面 */
	public static void viewDownloadsDetails(String hashName) {
		List<Filename> list = Filename.find("hashName = ?", hashName).fetch();
		Filename filename = list.get(0);
		String downLoadRoute = "?institute=" + filename.institute
				+ "&hashName=" + filename.hashName + "&subject="
				+ filename.subject;
		List<Rescomment> rescomments = Rescomment.find("resource_hashName = ?", hashName).fetch();
		render(filename, downLoadRoute,rescomments);
	}
	/* 显示相应学院的学科模块 */
	public static void viewSubject(String institute) {
		List<Instituteinfo> list = Instituteinfo
				.find("institute= ?", institute).fetch();
		response.contentType = "application/json";
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		response.setHeader("Cache-Control","no-cache");
		Gson gson = new Gson();
		String listToJson = gson.toJson(list);
		renderText(listToJson);
	}
	
	/* 获取相应的学院以及相应的学科下载目录，并返回json格式 (没有完成当目录为空的时候所执行的操作) */
	public static void viewDownloadsList(String fileroute, String subject,int page) {
		int pageCount;
		List filenameCountList = new ArrayList();
		List<List> reJsonList =new ArrayList<List>();
		List<Filename>  refilelist = Filename.find("institute = ? And subject = ?",fileroute,subject).from((page-1)*10).fetch(10);
		int userFilenameCount =(int)Filename.count("institute = ? And subject = ?",fileroute,subject);
		if(userFilenameCount % 10 == 0){
			pageCount = userFilenameCount / 10 ;
		}else{
			pageCount = userFilenameCount / 10 + 1 ;
		}
		filenameCountList.add(pageCount);
		reJsonList.add(refilelist);
		reJsonList.add(filenameCountList);
		response.contentType = "application/json";
		Gson gson = new Gson();
		String listToJson = gson.toJson(reJsonList);
		System.out.println(listToJson);
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
	public static void viewInsMostDown(String username,String password) throws HttpException, IOException {
		List re_list = new ArrayList();
		Gson gson = new Gson();
		TreeSet<String> treeset = GetClass.getUserClass(username,password);
		List<String> list = new ArrayList(Arrays.asList(treeset.toArray()));
//		List<String> list = new ArrayList<String>();
//		list.add("会计学");
		List<Filename> alllistname = new ArrayList<Filename>();
		for (String  allClass : list) {
			List<Filename> listname = Filename.find("bysubjectLike",
					"%" + allClass + "%").fetch();
			for (Filename a : listname) {
				alllistname.add(a);
			}
		}
		Assist assit = new Assist();
		Collections.sort(alllistname, assit);
		String listToJson = "";
		if (alllistname.size() > 6) {
			alllistname = alllistname.subList(0, 6);
		}  
		/*获取用户的信息，获取后天提醒的消息数*/
		/*回答的消息数目*/
		List<Seek_Help> se_list = Seek_Help.find("seek_no = ?", session.get("user")).fetch();
		int size = 0 ;
		for(Seek_Help seek_Help : se_list){
		List<Ask_Tips> tips_list = Ask_Tips.find("tip_from_id = ? And tip_status = ?",seek_Help.id,1).fetch();
		size += tips_list.size();
		}
		/*分享的消息数目*/
		List<Share_Tips> share_list = Share_Tips.find("tip_to_name = ? And tip_status = ?", session.get("user"),1).fetch();
		size += share_list.size();
		/*获取其他消息数目*/
		List<Other_tips> other_tips = Other_tips.find("to_no = ? And tip_status = ?", session.get("user"),1).fetch();
		size += other_tips.size();
		/*获取好友列表*/
		List<Map> linkman_group_list = new ArrayList<Map>();
		List<Linkgroup> group_list = Linkgroup.find("host_name= ?",session.get("user")).fetch();
		/*i传到前端用来唯一标识分组的div*/
		int i = 0;
		for(Linkgroup linkgroup : group_list){
			List<LinkMan> linkman_list = LinkMan.find("linkgroup_id = ?",linkgroup.id).fetch();
			Map map = new HashMap();
			map.put("group_id", i);
			map.put("group",linkgroup.firend_group);
			map.put("linkman", linkman_list);
			i++;
			linkman_group_list.add(map);
		}
		re_list.add(alllistname);
		re_list.add(size);
		re_list.add(linkman_group_list);
		listToJson = gson.toJson(re_list);
		response.contentType = "application/json";
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		renderText(listToJson);
	}
	/*返回学校学院目录*/
	public static void viewInstitute(){
		List instituteinfos = Institute.find("select p.institute from Institute p order BY p.institute").fetch();
		Gson gson = new Gson();
		String listToJson = gson.toJson(instituteinfos);
		response.contentType = "application/json";
		//response.setHeader("Content-Type", "application/json;charset=UTF-8");
		response.setHeader("Cache-Control","no-cache");
		renderText(listToJson);
	}
	/*获取当前登陆者的联系人*/
	public static void viewLinkname(){
		List<Linkgroup> group_list = Linkgroup.find("host_name= ?",session.get("user")).fetch();
		List<Map> linkman_group_list = new ArrayList<Map>();
		/*i传到前端用来唯一标识分组的div*/
		int i = 0;
		for(Linkgroup linkgroup : group_list){
			List<LinkMan> linkman_list = LinkMan.find("linkgroup_id = ?",linkgroup.id).fetch();
			Map map = new HashMap();
			map.put("group_id", i);
			map.put("group",linkgroup.firend_group);
			map.put("linkman", linkman_list);
			i++;
			linkman_group_list.add(map);
		}
		Gson gson = new Gson();
		String listToJson = gson.toJson(linkman_group_list);
		response.contentType = "application/json";
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		renderText(listToJson);
	}
}
