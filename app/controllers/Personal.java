package controllers;

import groovy.ui.SystemOutputInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import models.Filename;
import models.Re_Seek_Help;
import models.Seek_Help;
import models.Ask_Tips;

public class Personal extends BaseCore{
	public static void view_personalinfo(){
		String iframe_info = "./view_message";
		render(iframe_info);
	}
	public static void view_message(){
		List<Map> re_list = new ArrayList<Map>();
		List<Seek_Help> seek_list = Seek_Help.find("seek_user = ?", session.get("user")).fetch();
		for(Seek_Help seek_Help : seek_list){
			List<Ask_Tips> tips_list = Ask_Tips.find("tip_from_id = ? And tip_status = ?", seek_Help.id,1).fetch();
			for(Ask_Tips tips : tips_list){
					Map list_map = new HashedMap();
				String message = tips.tip_from_name + "回答了" + seek_Help.seek_content + "问题：" +
						tips.tip_content;
				list_map.put("message", message);
				list_map.put("tips_id",tips.id);
				re_list.add(list_map);
			}
		}
		render(re_list);
	}
//		List<Tips> tips_list = Tips.find("tip_to_name = ? And tip_status = ?",session.get("user"),1).fetch();
//		List<Long> list = new ArrayList<Long>();
//		for(Tips tips : tips_list){
//		//	Re_Seek_Help re_Seek_Help  = Re_Seek_Help.find("", params)tips.tip_from_id
//		}
//		/*消息的显示涉及到表连接线不考虑*/
//		/*向前端需要传递“xx,xx,xx回复了你的xx问题”,seek_help的id值*/
//		render(tips_list);
//	}
	/*
	 * seek_id:消息对应的id值
	 * page:对应显示的页数
	 * */
	public static void view_old_message(long seek_id,int page){
//		/*1.需要把该seek_id对应的tips都要把状态改为已读		 * 
//		 *2.需要把特定的该seek_id对应的回复的div显示出现，其它的隐藏
//		 *3.需要知道该seek_id对应的再第几页，并且定位到该页，然后将滚轮也定位到这里！
//		 */
//		List<Map> re_list = new ArrayList<Map>();
//		List<Seek_Help> seek_list = Seek_Help.find("seek_user = ?",session.get("user")).from(1).fetch(3);
//		for(Seek_Help seek_Help : seek_list){
//			Map map = new HashedMap();
//			System.out.println(seek_Help.id);
//			List<Re_Seek_Help> re_Seek_list = Re_Seek_Help.find("seek_id = ?", seek_Help.id).fetch();
//			map.put("re_Seek_list", re_Seek_list);
//			map.put("seek_Help", seek_Help);
//			re_list.add(map);
//		}
//		render(re_list,seek_id);
		List<String> re_list = new ArrayList<String>();
		List<Seek_Help> seek_list = Seek_Help.find("seek_user = ?", session.get("user")).fetch();
		for(Seek_Help seek_Help : seek_list){
			List<Ask_Tips> tips_list = Ask_Tips.find("tip_from_id = ? And tip_status = ?", seek_Help.id,0).fetch();
			for(Ask_Tips tips : tips_list){
				String messageString = tips.tip_from_name + "回答了" + seek_Help.seek_content + "问题：" +
						tips.tip_content;
				re_list.add(messageString);
			}
		}
		render(re_list);
	}
	public static void Remove_Tips(long id){
		Ask_Tips tips = (Ask_Tips)Ask_Tips.find("id = ?", id).fetch().get(0);
		tips.tip_status = 0;
		tips.save();
		view_message();
	}
	public static void view_myresources(){
		List<Filename> file_list = Filename.find("uploadname = ?", session.get("user")).fetch();
		render(file_list);
	}
}
