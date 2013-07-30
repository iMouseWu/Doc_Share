package dao;

import models.Filename;

public class AddFilename {
	public static void addFile(Filename filedatename){
		filedatename.save();
	}

}
