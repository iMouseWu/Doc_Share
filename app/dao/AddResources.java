package dao;

import models.Filename;
import models.Re_Seek_Help;
import models.Seek_Help;
import models.Ask_Tips;
import models.Share_Tips;

public class AddResources {
	public static void addFile(Filename filedatename){
		filedatename.save();
	}
	public static void addSeek_Help(Seek_Help seek_Help){
		seek_Help.save();
	}
	public static void addRe_Seek(Re_Seek_Help re_Seek_Help){
		re_Seek_Help.save();
	}
	public static void addAsk_Tips(Ask_Tips tips){
		tips.save();
	}
	public static void addShare_Tips(Share_Tips share_Tips){
		share_Tips.save();
	}

}
