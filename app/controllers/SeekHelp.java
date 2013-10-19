package controllers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import play.cache.Cache;
import play.libs.Codec;
import play.libs.Images;
import play.libs.Images.Captcha;
import play.mvc.Before;
import models.*;
public class SeekHelp extends BaseCore {
	@Before
	static void checksession() {
		if (session.get("user") == null) {
			String tipinfo = "请登录";
			Application.index(tipinfo);
		}
	}
	/*返回寻求帮助信息的条目*/
	public static void seek_home(int page,int seek_id,int imgstatue){
		int allcount = (int)Seek_Help.count();
		int allpage;
		if(allcount % 9 == 0){
			allpage = allcount / 9;
		}else{
			allpage = (allcount/9) + 1;
		}
		List<Seek_Help> list = Seek_Help.find("seek_status = ? order by id desc" , 1).from((page-1)*9).fetch(9);
		/*获取用户的信息，获取后天提醒的消息数*/
		/*回答的消息数目*/
		List<Seek_Help> se_list = Seek_Help.find("seek_user = ?", session.get("user")).fetch();
		int size = 0 ;
		for(Seek_Help seek_Help : se_list){
		List<Ask_Tips> tips_list = Ask_Tips.find("tip_from_id = ? And tip_status = ?",seek_Help.id,1).fetch();
		size += tips_list.size();
		}
		/*分享的消息数目*/
		List<Share_Tips> share_list = Share_Tips.find("tip_to_name = ? And tip_status = ?", session.get("user"),1).fetch();
		size += share_list.size();
		/*返回一个随机数，用来存储在服务器缓存中，对应相应的验证码的Id*/
		String randomID = Codec.UUID();
		render(list,allpage,page,seek_id,size,randomID,imgstatue);
	}
	/*增加寻求帮助信息*/
	public static void addhelp(String comment_help,String seek_title,String validateid,String code){
		System.out.println(Cache.get(validateid));
		if(!code.toUpperCase().equals(((String)Cache.get(validateid)).toUpperCase())){
			seek_home(1,0,1);
		}else{
		 Seek_Help seek_Help = new Seek_Help();
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		 Date date = new Date();
		seek_Help.seek_content = comment_help;
		seek_Help.seek_date = formatter.format(date);
		seek_Help.seek_status = 1;
		String user = ((Users)Users.find("username = ?", session.get("user")).fetch().get(0)).nickname;
		seek_Help.seek_user = user;
		seek_Help.seek_title = seek_title;
		dao.AddResources.addSeek_Help(seek_Help);
		seek_home(1,0,0);
		}
	}
	/*返回验证码*/
	public static void backValidate(String id){
		Captcha captcha = Images.captcha(140,40);
	    String code = captcha.getText("#ABCDEF");
	    captcha.addNoise();
        captcha.setBackground("#FFFFFF");
	    Cache.set(id, code, "10mn");
	    renderBinary(captcha);
	}
}
