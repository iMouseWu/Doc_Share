package controllers;

import groovy.ui.SystemOutputInterceptor;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import play.mvc.Before;

import com.google.gson.Gson;

import models.Filename;
import models.LinkMan;
import models.Linkgroup;
import models.Re_Seek_Help;
import models.Seek_Help;
import models.Ask_Tips;
import models.Share_Tips;
import models.Users;

public class Personal extends BaseCore {
	@Before
	static void checksession() {
		if (session.get("user") == null) {
			String tipinfo = "请登录";
			Application.index(tipinfo);
		}
	}
	public static void welcome(){
		render();
	}
	public static void view_personalinfo(String iframe_info) {
		render(iframe_info);
	}
	public static void view_message() {
		List<Map> re_list = new ArrayList<Map>();
		List<Seek_Help> seek_list = Seek_Help.find("seek_user = ?",
				session.get("user")).fetch();
		for (Seek_Help seek_Help : seek_list) {
			List<Ask_Tips> tips_list = Ask_Tips.find(
					"tip_from_id = ? And tip_status = ?", seek_Help.id, 1)
					.fetch();
			for (Ask_Tips tips : tips_list) {
				Map list_map = new HashedMap();
				String message = tips.tip_from_name + "回答了"
						+ seek_Help.seek_content + "问题：" + tips.tip_content;
				list_map.put("message", message);
				list_map.put("tips_id", tips.id);
				re_list.add(list_map);
			}
		}
		List<Share_Tips> share_list = Share_Tips.find(
				"tip_to_name = ?And tip_status = ?", session.get("user"), 1)
				.fetch();
		List<Map> share_message_list = new ArrayList<Map>();
		Map share_mapMap = new HashMap();
		for (Share_Tips share_Tips : share_list) {
			String message = share_Tips.tip_from_name + "向你分享了："
					+ share_Tips.tip_content;
			share_mapMap.put("message", message);
			share_mapMap.put("tips_id", share_Tips.id);
			share_message_list.add(share_mapMap);
		}

		render(re_list, share_message_list);
	}

	// List<Tips> tips_list =
	// Tips.find("tip_to_name = ? And tip_status = ?",session.get("user"),1).fetch();
	// List<Long> list = new ArrayList<Long>();
	// for(Tips tips : tips_list){
	// // Re_Seek_Help re_Seek_Help = Re_Seek_Help.find("",
	// params)tips.tip_from_id
	// }
	// /*消息的显示涉及到表连接线不考虑*/
	// /*向前端需要传递“xx,xx,xx回复了你的xx问题”,seek_help的id值*/
	// render(tips_list);
	// }
	/*
	 * seek_id:消息对应的id值 page:对应显示的页数
	 */
	public static void view_old_message( int page) {
		// /*1.需要把该seek_id对应的tips都要把状态改为已读 *
		// *2.需要把特定的该seek_id对应的回复的div显示出现，其它的隐藏
		// *3.需要知道该seek_id对应的再第几页，并且定位到该页，然后将滚轮也定位到这里！
		// */
		// List<Map> re_list = new ArrayList<Map>();
		// List<Seek_Help> seek_list =
		// Seek_Help.find("seek_user = ?",session.get("user")).from(1).fetch(3);
		// for(Seek_Help seek_Help : seek_list){
		// Map map = new HashedMap();
		// System.out.println(seek_Help.id);
		// List<Re_Seek_Help> re_Seek_list = Re_Seek_Help.find("seek_id = ?",
		// seek_Help.id).fetch();
		// map.put("re_Seek_list", re_Seek_list);
		// map.put("seek_Help", seek_Help);
		// re_list.add(map);
		// }
		// render(re_list,seek_id);
		List<Map> re_list = new ArrayList<Map>();
		List<Map> share_list = new ArrayList<Map>();
		List<Seek_Help> seek_list = Seek_Help.find("seek_user = ?",
				session.get("user")).fetch();
		for (Seek_Help seek_Help : seek_list) {
			List<Ask_Tips> tips_list = Ask_Tips.find(
					"tip_from_id = ? And tip_status = ?", seek_Help.id, 0)
					.fetch();
			for (Ask_Tips ask_tips : tips_list) {
				Map askMap = new HashedMap();
				askMap.put("ask_tips", ask_tips);
				String messageString = ask_tips.tip_from_name + "回答了"
						+ seek_Help.seek_content + "问题：" + ask_tips.tip_content;
				askMap.put("message", messageString);
				re_list.add(askMap);
			}
		}
		List<Share_Tips> share_tips_list = Share_Tips.find("tip_to_name",
				session.get("user")).fetch();
		for (Share_Tips share_Tips : share_tips_list) {
			Map shareMap = new HashedMap();
			shareMap.put("share_Tips", share_Tips);
			String message = share_Tips.tip_content;
			shareMap.put("message", message);
			share_list.add(shareMap);
		}
		render(re_list, share_list,page);
	}

	public static void Remove_Tips(long id) {
		Ask_Tips tips = (Ask_Tips) Ask_Tips.find("id = ?", id).fetch().get(0);
		tips.tip_status = 0;
		tips.save();
		view_message();
	}

	public static void view_myresources(int page) {
		List<Filename> file_list_number = Filename.find("uploadname = ?",
				session.get("user")).fetch();
		if(page == 0){
			page = 1;
		}
		int allpage;
		int count = file_list_number.size();
		if(count % 10 == 0){
			allpage = count / 10;
		}else{
			allpage = count / 10 + 1;
		}
		List<Filename> file_list = Filename.find("uploadname = ?",
				session.get("user")).from((page - 1) * 10).fetch(10);
		render(file_list,allpage,page);
	}

	public static void Remove_Share_Tips(long id) {
		Share_Tips tips = (Share_Tips) Share_Tips.find("id = ?", id).fetch()
				.get(0);
		tips.tip_status = 0;
		tips.save();
		view_message();
	}

	public static void share_resource(String[] linkname, String hashName,
			String realName, String share_content) throws EmailException,
			UnsupportedEncodingException {
		if(linkname != null){
		String content = "这是我上传的文件 ：<a href='ViewResource/viewDownloadsDetails?hashName="
				+ hashName + "'>" + realName + "</a><br>备注:" + share_content;
		Date date = new Date();
		/* 构建邮件发送端,目测一个Email对象只能发送一次 */
		Users hostuser = (Users) Users
				.find("username = ?", session.get("user")).fetch().get(0);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (String single_linkname : linkname) {
			Share_Tips share_Tips = new Share_Tips();
			share_Tips.tip_content = content;
			share_Tips.tip_from_name = session.get("user");
			share_Tips.tip_status = 1;
			share_Tips.tip_to_name = single_linkname;
			share_Tips.tip_date = formatter.format(date);
			dao.AddResources.addShare_Tips(share_Tips);
			Users linkuser = (Users) Users
					.find("username = ?", single_linkname).fetch().get(0);
			/* 发送email */
			HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.qq.com");
			email.setSmtpPort(25);
			email.setAuthenticator(new DefaultAuthenticator(
					hostuser.mailaddress, hostuser.mailpassword));
			email.setFrom(hostuser.mailaddress);
			email.setHtmlMsg(new String(content.getBytes("utf-8"), "iso-8859-1"));
			email.addTo(linkuser.mailaddress);
			email.send();
		}
	}
	}
	public static void view_link_group() {
		List<Linkgroup> group_list = Linkgroup.find("host_name= ?",session.get("user")).fetch();
		render(group_list);
	}
	public static void view_linkman_bygroup(long group_id) {
		if(group_id != 0){
			/*如果是初始状态那么sign就是0，用来显示所有好友,并且移动按钮灰显，否则就是1*/
			int sign = 1;
		List<LinkMan> list = LinkMan.find("linkgroup_id = ?",group_id).fetch();
		List<Linkgroup> group_list = Linkgroup
				.find("select  u from Linkgroup u where u.id != ?",group_id).fetch();
		render(list, group_list,sign);
	}else{
		List<LinkMan> list = LinkMan.findAll();
		int sign = 0;
	/*如果是全部好友的话就返回sign=0*/
		render(list,sign);
	}
	}
	public static void moveFriend(Long[] move_linkname_id, long to_group_id) {
		for(long linkname_id : move_linkname_id){
			List<LinkMan> link_list = LinkMan.find("id = ?",linkname_id).fetch();
			LinkMan linkMan = link_list.get(0);
			Linkgroup linkgroup =new Linkgroup();
			linkgroup.id = to_group_id;
			linkMan.linkgroup= linkgroup;
			dao.AddResources.addLinkMan(linkMan);
		}
		response.contentType = "application/json";
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		Gson gson = new Gson();
		String stringToJson = gson.toJson(move_linkname_id);
		renderText(stringToJson);
	}
	public static void addGroup(String newgroup){
		Linkgroup linkgroup =new Linkgroup();
		linkgroup.host_name = session.get("user");
		linkgroup.firend_group = newgroup;
		dao.AddResources.addGroup(linkgroup);
		view_link_group();
	}
	public static void view_addLinkMan(){
		/*返回好友组别*/
		List<Linkgroup> list = Linkgroup.find("host_name = ?", session.get("user")).fetch();
		   render(list);
	}
	public static void serachLinkMan(String number){
		List<Users> list = Users.find("username = ?", number).fetch();
		response.contentType = "application/json";
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		Gson gson = new Gson();
		String stringToJson = gson.toJson(list);
		renderText(stringToJson);
	}
	public static void addLinkMan(long user_id,long group_id){
		Users users = Users.findById(user_id);
		List<LinkMan> list = LinkMan.find("friend_name = ? And linkgroup.host_name = ?", users.nickname,session.get("user")).fetch();
		System.out.println(list.size());
		if(list.size() == 1){
			/*如果联系人已经存在的话就返回0*/
			renderText(0);
		}else{
		Linkgroup linkgroup =new Linkgroup();
		linkgroup.id = group_id;
		LinkMan linkMan = new LinkMan();
		linkMan.friend_name = users.nickname;
		linkMan.linkgroup = linkgroup;
		linkMan.save();
		/*成功就返回1*/
		renderText(1);
		}
	}
	public static void deleteFriend(Long[] move_linkname_id) {
		for(long linkname_id : move_linkname_id){
			LinkMan linkMan = LinkMan.findById(linkname_id);
			linkMan.delete();
		}
		response.contentType = "application/json";
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		Gson gson = new Gson();
		String stringToJson = gson.toJson(move_linkname_id);
		renderText(stringToJson);
	}
	public static void deleteGroup(long id){
		List<Linkgroup> list = Linkgroup.find("firend_group = ? And host_name = ?","friend",session.get("user")).fetch();
		Long friend_id = list.get(0).id;
		List<LinkMan> list2 = LinkMan.find("linkgroup.id = ?", id).fetch();
		for(LinkMan linkMan : list2){
			Linkgroup linkgroup = new Linkgroup();
			linkgroup.id = friend_id;
			linkMan.linkgroup = linkgroup;
			linkMan.save();
		}
		Linkgroup linkgroup = Linkgroup.findById(id);
		linkgroup.delete();
		view_link_group();
	}
	public static void delete_asktips(int page,long id){
		Ask_Tips ask_Tips = Ask_Tips.findById(id);
		ask_Tips.delete();
		view_old_message(page);
	}
	public static void delete_sharetips(int page,long id){
		Share_Tips share_Tips = Share_Tips.findById(id);
		share_Tips.delete();
		view_old_message(page);
	}
}