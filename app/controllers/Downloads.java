package controllers;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import play.Play;
import play.mvc.Before;
import models.Filename;

public class Downloads extends BaseCore{
	/* 统计下载次并且实现下载链接的重定向 */
	@Before
	static void checksession() {
		if (session.get("user") == null) {
			String tipinfo = "请登录";
			Application.index(tipinfo);
		}
	}
	public static void addCount(String institute, String hashName,
			String subject) throws UnsupportedEncodingException {
		List<Filename> list = Filename.find("hashName", hashName).fetch();
		Filename filename = list.get(0);
		filename.downcount = filename.downcount + 1;
		filename.save();
//		String msg = URLEncoder.encode(institute, "UTF-8");
//		String sub = URLEncoder.encode(subject, "UTF-8");
		String path = "/public/resourse/" + institute + "/" + subject + "/" + hashName;
		File file = Play.getFile(path);
		renderBinary(file);
	}


}
