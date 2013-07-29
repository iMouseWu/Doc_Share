package controllers;

import play.*;
import play.libs.Files;
import play.mvc.*;

import java.io.File;
import java.util.*;

import org.hibernate.mapping.Array;

import com.google.gson.Gson;

import models.*;

public class Application extends Controller {

	public static void index() {
		render();
	}
	/*上传功能*/
	public static void uploads(File upfile,String institute) {
        //UUID uuid = UUID.randomUUID();
		/*获取文件哈希码*/
		int hash = upfile.hashCode();
	    /*获取文件名字*/
		String filename = upfile.getName();
		/*获取文件扩展名*/
		String fileext = filename.substring(filename .lastIndexOf("."));
		/*构成hash路径名*/
	    String path = "/public/resourse/" + institute + "/" + hash + fileext;
		Files.copy(upfile, Play.getFile(path));
		/*将hash名字和真实名字映射关系储存到数据库*/
		dao.AddFilename.addFile(hash + fileext, filename);
	}
	/*获取相应的下载目录，并返回json格式*/
    public static void downloads(String fileroute){
    	List<Filename> refileList = new ArrayList();
//    	File file=new File(".");
//    	System.out.println(file.getAbsolutePath());
    	File listfile = new File("Doc_Share/public/resourse/" + fileroute);
    	String list[] = listfile.list();
    	for(String name:list){
    		List<Filename> filelist = Filename.find("hashName", name).fetch();
    		Filename filename = filelist.get(0);
    		refileList.add(filename);
    	}
    	response.contentType="application/json";
        Gson gson = new Gson();        
        String listToJson = gson.toJson(refileList);
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        renderText(listToJson);
    }

}