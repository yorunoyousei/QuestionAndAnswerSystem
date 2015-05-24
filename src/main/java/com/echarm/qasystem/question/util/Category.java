package com.echarm.qasystem.question.util;

public enum Category {
	Category_1,
	Category_2;
	
	public static Category isCategoryExist(String category) {
		for (Category c : Category.values()) {
			if (c.name().equalsIgnoreCase(category)) {
				return c;
			}
		}
		return null;
	}

}
