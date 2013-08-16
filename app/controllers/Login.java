package controllers;

import java.util.List;

import models.Users;

public class Login extends BaseCore{
	/* 登录验证 */
	public static void loginCheck(String username, String password) {
//		 List<String> list1 = Users.find("select distinct u.firend_group from LinkMan u where u.host_name='mysql1'").fetch();
//		 System.out.println(list1.size());
//		 for(String a : list1)
//		 System.out.println(a);
		List<Users> list = Users.find("username= ? And password= ?", username,
				password).fetch();
		if (list.size() == 0) {
			response.contentType = "application/json";
			response.setHeader("Content-Type", "application/json;charset=UTF-8");
			renderText("");
		} else {
			session.put("user", username);
			session.put("password",password);
			// String institute = list.get(0).institute;
			ViewResource.viewInsMostDown();
		}
	}

}
