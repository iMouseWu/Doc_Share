package controllers;

import java.util.List;

import models.Filename;

public class Evaluate extends BaseCore{
	/* 评分系统 */
	public static void addScore(float score, String hashName) {
		List<Filename> list = Filename.find("hashName = ?", hashName).fetch();
		Filename filename = list.get(0);
		filename.avescore = (filename.avescore * filename.numberval + score)
				/ (filename.numberval + 1);
		filename.numberval += 1;
		filename.save();
	}

}
