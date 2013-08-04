package tools;

import java.util.Comparator;

import models.Filename;

public class Assist implements Comparator<Filename>{

	@Override
	public int compare(Filename o1, Filename o2) {
		if(o1.avescore > o2.avescore)
			return -1;
		else if(o1.avescore == o2.avescore ){
			if(o1.id > o2.id)
				return -1;
			else
				return 1;
		}
		else
			return 1;
	}

}
