package dao;

import models.Filename;

public class AddFilename {
	public static void addFile(String hashName,String realName){
		Filename filename = new Filename();
		filename.hashName = hashName;
		filename.realName = realName;
		filename.save();
	}

}
