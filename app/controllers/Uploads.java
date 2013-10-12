package controllers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ThisExpression;

import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.generic.NEW;

import models.Filename;
import play.Play;
import play.libs.Files;
import play.mvc.Before;
import play.mvc.Controller;
import tools.StaticPath;

public class Uploads extends BaseCore{
	@Before
	static void checksession() {
		if (session.get("user") == null) {
			String tipinfo = "请登录";
			Application.index(tipinfo);
		}
	}
	/* 上传功能 */
	public static void uploads(File upfile, String institute_sel, String subject_sel,String intro_sel) {
		/* 获取文件哈希码 */
		int hash = upfile.hashCode();
		/* 获取文件名字 */
		String allfilename = upfile.getName();
        /*获取文件除扩展名以外的名字*/
		String filename = allfilename.substring(0, allfilename.lastIndexOf("."));
		/* 获取文件扩展名 */
		String fileext = allfilename.substring(allfilename.lastIndexOf("."));
		/*获取扩展名，不包括点*/
		String fileextexceptdoc = allfilename.substring(allfilename.lastIndexOf(".")+1);
		/* 构成hash路径名 */
//		String path = StaticPath.path + institute_sel + "/" + subject_sel + "/"
//				+ hash + fileext;
//		Files.copy(upfile, new File(path));
		
		String path = "/public/resourse/" + institute_sel + "/" + subject_sel + "/"
				+ hash + fileext;
		Files.copy(upfile, Play.getFile(path));
		
		/* 获取当前日期 */
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date date = new Date();
		/* 获取上传者的姓名 */
		String user = session.get("user");
		if(intro_sel.equals("")){
			intro_sel = "暂无简介";
		}
		/* 将此资源储存到数据库 */
		Filename filedatename = new Filename();
		filedatename.hashName = hash + fileext;
		filedatename.realName = filename;
		filedatename.institute = institute_sel;
		filedatename.subject = subject_sel;
		filedatename.downcount = 0;
		filedatename.uploadname = user;
		filedatename.uploaddate = formatter.format(date);
		filedatename.picture = fileextexceptdoc + ".png";
		filedatename.intro = intro_sel;
		dao.AddResources.addFile(filedatename);
//		List list = new ArrayList();
//		list.add(filedatename.id);
//		list.add(filedatename.realName);
//		 List<Map> list = new ArrayList<Map>();
//		 Map jsono = new HashMap();
//         jsono.put("name", upfile.getName());
//         jsono.put("size", upfile.length());
//         jsono.put("url", "uploads/getfile/hash/"+ hash + fileext);
//         jsono.put("thumbnail_url", "uploads/getthumb/hash/" + hash + fileext);
//         jsono.put("delete_url", "uploads/delfile/hash/" + hash + fileext);
//         jsono.put("delete_type", "GET");
//         list.add(jsono);
		List list = new ArrayList();
		list.add(filedatename);
		response.contentType = "application/json";
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		Gson gson = new Gson();
		String listToJson = gson.toJson(list);
		renderText(listToJson);
		
	}
	/*修改文件名*/
	public static void alterFilename(String filename,long id){
	      List<Filename> list = Filename.find("id = ?", id).fetch();
	      list.get(0).realName = filename;
	      list.get(0).save();
	}
	/*对应jquery-file-uploads插件的get方法需要处理文件上传后的删除等功能*/
	public static void editFile(){
		String editway=params.get("editway");
		String hashname=params.get("hashname");
		if(editway.equals("delfile") ){
			/*删除数据库下面的文件*/
			List<Filename> list = Filename.find("hashName = ?",hashname).fetch();
			Filename filename = list.get(0);
			String institute = filename.institute;
			String subject = filename.subject;
			String path = "/public/resourse/" + institute + "/" + subject + "/" + hashname;
			File file = Play.getFile(path);
			Files.delete(file);
			filename.delete();
		}else if(editway .equals("getthumb")){
			/*下面这个蛋疼的东西就不知道干什么了，目测是为了如果服务端是图片的话，传完以后图片可以显示在页面上
			 但是demo里面是用流来处理的，明明可以用一个路径就可以解决了，所以demo的代码就看不懂，然后后面也发现demo的
			 代码可有可无，前端页面根本不买账所以这里留着以后写吧！*/
			
		}else if(editway.equals("getfile")){
			/*这个判断就是从服务器通过流来下载文件，防止如果是图片链接的话，会直接打开文件！如果是其它文件的话
			 效率也会高点*/
		}else{
			/*这个蛋疼的返回是给form表单的时候增加一个option的一个done的回调，目测是*/
			renderText("call POST with multipart form data");
		}
	}
	

}
