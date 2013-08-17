package controllers;

import play.*;
import play.libs.Files;
import play.mvc.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

import org.eclipse.jdt.core.dom.ThisExpression;
import org.hamcrest.core.Is;
import org.hibernate.mapping.Array;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xpath.internal.operations.And;

import models.*;

public class Application extends BaseCore {

	public static void index(String user) {
		String username = "";
		String password = "";
		username = session.get("user");
		password = session.get("password");
		render(user,username,password);
	}
}