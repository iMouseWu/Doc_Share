package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jaxen.function.StringFunction;

import play.mvc.Before;
import models.Re_Seek_Help;
import models.Seek_Help;
import models.Ask_Tips;

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
	public static void AddReSeek(String re_seek_content,long seek_id,int nowpage){
		Ask_Tips tips = new Ask_Tips();
		/*把回复信息加入回复数据表中*/
//		Re_Seek_Help re_Seek_Help = new Re_Seek_Help();
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date date = new Date();
//		re_Seek_Help.re_seek_content = re_seek_content;
//		re_Seek_Help.re_seek_user = session.get("user");
//		re_Seek_Help.re_seek_date =  formatter.format(date);
//		Seek_Help seek_Help1 =new Seek_Help();
//		seek_Help1.id = seek_id;
//		re_Seek_Help.seek_Help = seek_Help1;
//		dao.AddResources.addRe_Seek(re_Seek_Help);
//		List<Seek_Help> list = Seek_Help.find("id = ?", seek_id).fetch();
//		Seek_Help seek_Help = list.get(0);
//		String seek_user = seek_Help.seek_user;
//		String seek_content = seek_Help.seek_content;
		/*获取该条信息的发起者，并且判断如果发起者和回复者不相同就将这条信息存到tips数据表里面*/
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		List<Seek_Help> se_list = Seek_Help.find("id = ?",seek_id).fetch();
		if(!se_list.get(0).seek_user.equals(session.get("user"))){
		tips.tip_date = formatter.format(date);
		tips.tip_status = 1;
		tips.tip_from_name = session.get("user");
		tips.tip_content = re_seek_content;
		tips.tip_from_id = seek_id;
		dao.AddResources.addAsk_Tips(tips);
		}
		SeekHelp.seek_home(nowpage,(int)(seek_id/1));
	}
}
