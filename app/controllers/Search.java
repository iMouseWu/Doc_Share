package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Filename;

import com.google.gson.Gson;

public class Search extends BaseCore {
	/* 搜索资源(缺少资源没有处理过程) */
	public static void searchRes(String searchname) {
		List<Filename> list = Filename.find("realName like ?",
				"%" + searchname + "%").fetch();
		response.contentType = "application/json";
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		Gson gson = new Gson();
		if (list.size() == 0) {
			renderText("");
		} else {
			String listToJson = gson.toJson(list);
			renderText(listToJson);
		}

	}
	/*根据学院和学科搜索资源*/
	public static void search_sure_res(int sure_page,String sure_name,String sure_subject,String sure_institute){
		List filenameCountList = new ArrayList();
		List<List> reJsonList =new ArrayList<List>();
		List<Filename> list = Filename.find("realName like ? And subject= ? And institute= ?",
				"%" + sure_name + "%",sure_subject,sure_institute).from((sure_page-1)*3).fetch(3);
		long userFilenameCount =Filename.count("realName like ? And subject= ? And institute= ?",
				"%" + sure_name + "%",sure_subject,sure_institute);
		int pageCount = (int)(userFilenameCount / 3 + 1) ;
		filenameCountList.add(pageCount);
		reJsonList.add(list);
		reJsonList.add(filenameCountList);
		response.contentType = "application/json";
		response.setHeader("Content-Type", "application/json;charset=UTF-8");
		Gson gson = new Gson();
		if (list.size() == 0) {
			renderText("1");
		} else {
			String listToJson = gson.toJson(reJsonList);
			renderText(listToJson);
		}
	}

}
