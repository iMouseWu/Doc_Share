package controllers;

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
	public static void search_sure_res(String sure_name,String sure_subject,String sure_institute){
		List<Filename> list = Filename.find("realName like ? And subject= ? And institute= ?",
				"%" + sure_name + "%",sure_subject,sure_institute).fetch();
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

}
