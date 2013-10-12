package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.httpclient.HttpException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tools.StaticPath;
import models.Users;

public class Login extends BaseCore{
	/* 登录验证 */
	public static void loginCheck(String username, String password) throws HttpException, IOException {
//		 List<String> list1 = Users.find("select distinct u.firend_group from LinkMan u where u.host_name='mysql1'").fetch();
//		List<Users> list = Users.find("username= ? And password= ?", username,
//				password).fetch();
//		if (list.size() == 0) {
//			response.contentType = "application/json";
//			response.setHeader("Content-Type", "application/json;charset=UTF-8");
//			renderText("");
//		} else {
//			session.put("user", username);
//			session.put("password",password);
//			ViewResource.viewInsMostDown(username,password);
//		}
		List<Users> userlist = Users.find("username = ?", username).fetch();
		Map map = ValidateUser(username,password);
		if(map.get("state").equals("success")){
			/*判断该用户是否以前登陆过*/
			if(userlist.size() == 0){
				Users users = new Users();
				users.isfirst = "0";
				users.statue = "0";
				users.nickname = users.username = username;
				if(map.get("zjutmail") == null){
					/*用户中心已经注册，且doc_share是第一次登陆，并且邮箱没有开通（需要修改昵称和是否开通邮箱）*/
					users.mailaddress = null;
					users.save();
					session.put("user", username);
					session.put("password",password);
					Map map2 = new HashedMap();
					map2.put("info", "addtwo");
					Gson gson = new Gson();
					String listToJson = gson.toJson(map2);
					response.contentType = "application/json";
					response.setHeader("Cache-Control","no-cache");
					renderText(listToJson);
				}else{
					/*用户中心已经注册，且doc_share是第一次登陆，并且邮箱开通（需要修改昵称）*/
					users.mailaddress = (String)map.get("zjutmail");
					users.save();
					session.put("user", username);
					session.put("password",password);
					Map map2 = new HashedMap();
					map2.put("info", "addone");
					Gson gson = new Gson();
					String listToJson = gson.toJson(map2);
					response.contentType = "application/json";
					response.setHeader("Cache-Control","no-cache");
					renderText(listToJson);
				}
				
			  }else{
				/*数据库中已经有该用户名的数据了，如果是第二次登陆，那么就把isfirst改成1*/
				Users users = userlist.get(0);
				if(users.isfirst.equals("0")){
					users.isfirst = "1";
					users.save();
				}
				session.put("user", username);
				session.put("password",password);
				System.out.println(123456);
				ViewResource.viewInsMostDown(username,password);
			}
		}else{
			/*返回的是错误的信息*/
			/*密码错误*/
            if(map.get("info").equals("密码不正确，请重新输入")){
            	response.contentType = "application/json";
				response.setHeader("Content-Type", "application/json;charset=UTF-8");
				renderText("");
			}else{
				/*用户名中心没有注册*/
				Gson gson = new Gson();
				Map map2 = new HashedMap();
				map2.put("info", map.get("info"));
				String listToJson = gson.toJson(map2);
				response.contentType = "application/json";
				response.setHeader("Cache-Control","no-cache");
				renderText(listToJson);
			}
		}
	}
	/*退出登录*/
	public static void exitLogin(){
		session.clear();
		Application.index("");
	}
	public static Map ValidateUser(String username,String password){
		String validateUrl = StaticPath.USERCENTER_API_STUDENT_NUMBER.replace("{0}", username)
				.replace("{1}", password);
		String resultJson = null;
		BufferedReader reader = null;
		Map mapjson = null;
		try {
			URL url = new URL(validateUrl);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			resultJson = reader.readLine();
			reader.close();
			Gson gson = new Gson();
			mapjson = gson.fromJson(resultJson,new TypeToken<Map>(){}.getType());
//			String state = (String) mapjson.get("state");
//			if(state.equals("success")){
//				return true;
//			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();}
		return mapjson;
	}
}
