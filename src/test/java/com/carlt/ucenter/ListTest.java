package com.carlt.ucenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListTest {

	public static void main(String[] args) {

		List<Integer> p = new ArrayList<>(Arrays.asList(2, 6, 7, 137, 3, 8, 9, 10, 4, 12, 17, 135, 19, 18, 151, 11, 15,
				16, 152, 153, 13, 20, 21, 22, 23, 14, 24, 25, 129));
		List<Integer> c = new ArrayList<>(Arrays.asList(58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73,
				74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99,
				100, 101, 102, 103, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121,
				123, 124, 127, 128));

		boolean suc = p.addAll(c);

		System.out.println(suc);
		
		System.out.println(p);
	}

}
