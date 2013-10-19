package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jaxen.function.StringFunction;

import play.cache.Cache;
import play.mvc.Before;
import models.Re_Seek_Help;
import models.Seek_Help;
import models.Ask_Tips;
import models.Users;

public class ReSeek extends BaseCore {
	/*
	 * re_seek_content:回复内容 seek_id:seek内容的id号
	 */
	@Before
	static void checksession() {
		if (session.get("user") == null) {
			String tipinfo = "请登录";
			Application.index(tipinfo);
		}
	}
	public static void AddReSeek(String re_seek_content,long seek_id,int nowpage ,String code){
		if(!code.toUpperCase().equals(((String)Cache.get(""+seek_id)).toUpperCase())){
			SeekHelp.seek_home(nowpage,(int)(seek_id/1),1);
		}else{
		Ask_Tips tips = new Ask_Tips();
		/*获取该条信息的发起者，并且判断如果发起者和回复者不相同就将这条信息存到tips数据表里面*/
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		List<Seek_Help> se_list = Seek_Help.find("id = ?",seek_id).fetch();
		String user = ((Users)Users.find("username = ?", session.get("user")).fetch().get(0)).nickname;
		if(!se_list.get(0).seek_user.equals(user)){
		tips.tip_date = formatter.format(date);
		tips.tip_status = 1;
		tips.tip_from_name = user;
		tips.tip_content = re_seek_content;
		tips.tip_from_id = seek_id;
		dao.AddResources.addAsk_Tips(tips);
		}
		SeekHelp.seek_home(nowpage,(int)(seek_id/1),0);
	}
	}
}
