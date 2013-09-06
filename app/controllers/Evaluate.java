package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import play.mvc.Before;
import sun.text.resources.FormatData;
import models.Filename;
import models.Rescomment;

public class Evaluate extends BaseCore{
	@Before
	static void checksession() {
		if (session.get("user") == null) {
			String tipinfo = "请登录";
			Application.index(tipinfo);
		}
	}
	/* 评分系统 */
	public static void addScore(float score, String hashName,String res_comment) {
		List<Filename> list = Filename.find("hashName = ?", hashName).fetch();
		Filename filename = list.get(0);
		filename.avescore = (filename.avescore * filename.numberval + score)
				/ (filename.numberval + 1);
		filename.numberval += 1;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date date = new Date();
		Rescomment rescomment = new Rescomment();
		rescomment.comment_user =session.get("user");
		rescomment.comment = res_comment;
		rescomment.comment_date = formatter.format(date);
		rescomment.resource_hashName =  filename.hashName;
		rescomment.save();
		filename.save();
	}

}
