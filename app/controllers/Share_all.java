package controllers;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.crypto.Data;

import org.apache.commons.mail.*;
import org.h2.engine.User;

import models.Share_Tips;
import models.Users;
	public class Share_all extends BaseCore{
		public static void share_resource(String[] linkname,String hashName,String realName,String share_content) throws EmailException, UnsupportedEncodingException{
			String content = "这是我上传的文件 ：<a href='/ViewResource/viewDownloadsDetails?hashName="+hashName+"'>"+ 
					realName +"</a><br>备注:"+share_content;
			Date date = new Date();
			/*构建邮件发送端,目测一个Email对象只能发送一次*/
			Users hostuser = (Users)Users.find("username = ?", session.get("user")).fetch().get(0);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(String single_linkname : linkname){
				Share_Tips share_Tips = new Share_Tips();
				share_Tips.tip_content = content;
				share_Tips.tip_from_name = session.get("user");
				share_Tips.tip_status = 1;
				share_Tips.tip_to_name = single_linkname;
				share_Tips.tip_date = formatter.format(date);
				dao.AddResources.addShare_Tips(share_Tips);
				Users linkuser = (Users)Users.find("username = ?",single_linkname).fetch().get(0);
				/*发送email*/
				HtmlEmail email = new HtmlEmail();
				email.setHostName("smtp.qq.com");
				email.setSmtpPort(25);
				email.setAuthenticator(new DefaultAuthenticator(hostuser.mailaddress, hostuser.mailpassword));
				email.setFrom(hostuser.mailaddress);
				email.setHtmlMsg(new String(content.getBytes("utf-8"),"iso-8859-1"));
				email.addTo(linkuser.mailaddress);
				email.send();
			}
			
		}
	}

