package controllers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.*;
public class SeekHelp extends BaseCore {
	
	public static void seek_home(int page,int seek_id){
		long allcount = Seek_Help.count();
		int allpage = (int)(allcount/3);
//		List<SeekAndRe_view> sar_list = new ArrayList<SeekAndRe_view>();
		List<Seek_Help> list = Seek_Help.find("seek_statue = ? order by id desc" , 1).from((page-1)*3).fetch(3);
//		for(Seek_Help seek_Help : list){
//			SeekAndRe_view seekAndRe_view =new SeekAndRe_view();
//			List<Re_Seek_Help> re_list = Re_Seek_Help.find("Seek_Help_id = ?",seek_Help.id).fetch();
//			seekAndRe_view.seek_Help = seek_Help;
//			seekAndRe_view.list = re_list;
//			sar_list.add(seekAndRe_view);
//		}
		render(list,allpage,page,seek_id);
	}
	public static void addhelp(String comment_help){
		 Seek_Help seek_Help = new Seek_Help();
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		 Date date = new Date();
		seek_Help.seek_content = comment_help;
		seek_Help.seek_date = formatter.format(date);
		seek_Help.seek_status = 1;
		seek_Help.seek_user = session.get("user");
		dao.AddResources.addSeek_Help(seek_Help);
		seek_home(1,0);
	}
}
