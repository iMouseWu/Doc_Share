package controllers;

import java.util.List;

import models.Users;

public class Login extends BaseCore{
	/* 登录验证 */
	public static void loginCheck(String username, String password) {
		// List<Users> list = Users.find(
		// "select u from Users u where u.username=:" + username
		// + " and u.password=:" + password).fetch();
		List<Users> list = Users.find("username= ? And password= ?", username,
				password).fetch();
		if (list.size() == 0) {
			response.contentType = "application/json";
			response.setHeader("Content-Type", "application/json;charset=UTF-8");
			renderText("");
		} else {
			session.put("user", username);
			// String institute = list.get(0).institute;
			ViewResource.viewInsMostDown();
		}
	}

}
