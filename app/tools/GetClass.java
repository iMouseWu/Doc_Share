package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xpath.internal.operations.And;

public class GetClass {
	public static TreeSet<String> getUserClass(String username,String password) throws HttpException, IOException{
		/*获取当前的月份*/
		String studyyear = "";
		Calendar calendar = Calendar.getInstance();
		int daynumber = calendar.get(Calendar.DAY_OF_YEAR);
		int yearnumber = calendar.get(Calendar.YEAR);
		if(daynumber<60){
			studyyear = (yearnumber - 1) + "/" + yearnumber + "(1)";
		}else if(60 <= daynumber && daynumber <= 270 ){
			studyyear = (yearnumber - 1) + "/" + yearnumber + "(2)";
		}else{
			studyyear = (yearnumber) + "/" + (yearnumber + 1) + "(1)";
		}
		/*通过API获取课表信息*/
		HttpClient httpClient=new HttpClient();
		String url = "http://210.32.200.91:8080/classtable/GetTable";
		TreeSet<String> tr = new TreeSet<String>();
		PostMethod postMethod = new PostMethod(url);
		NameValuePair[] data = { 
				new NameValuePair("sid",username),
				new NameValuePair("pwd",password),
				new NameValuePair("term",studyyear)
				};
		postMethod.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gb2312");
		postMethod.setRequestBody(data);
	    httpClient.executeMethod(postMethod);
	    /*获取返回的json字符串*/
		InputStream instr = postMethod.getResponseBodyAsStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(instr));
		String jsonString = in.readLine();
		Gson gson = new Gson();
		Map map = gson.fromJson(jsonString,new TypeToken<Map>(){}.getType());
		if(map.get("status") != null && map.get("status").equals("error")){
			return tr;
		}else{
		Map<String,Map> mapjson = gson.fromJson(jsonString,new TypeToken<Map>(){}.getType());
		/*将信息筛选出，并且利用treeset去重*/
		for(String key : mapjson.keySet()){
			Map<String,String> innermap = mapjson.get(key);
			for(String key1 : innermap.keySet()){
				String classname = innermap.get(key1);
				int index = classname.indexOf(":");
				tr.add(classname.substring(0, index));
			}
		}
		return tr;
		}
	}

}
