package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.crypto.Data;

import models.Share_Tips;
	public class Share_all extends BaseCore{
		public static void share_resource(String[] linkname,String hashName,String realName,String share_content){
			String content = "这是我上传的文件 ：<a href='/ViewResource/viewDownloadsDetails?hashName="+hashName+"'>"+ 
					realName +"</a><br>备注:"+share_content;
			Date date = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(String single_linkname : linkname){
				Share_Tips share_Tips = new Share_Tips();
				share_Tips.tip_content = content;
				share_Tips.tip_from_name = session.get("user");
				share_Tips.tip_status = 1;
				share_Tips.tip_to_name = single_linkname;
				share_Tips.tip_date = formatter.format(date);
				dao.AddResources.addShare_Tips(share_Tips);
			}
		}
	}

