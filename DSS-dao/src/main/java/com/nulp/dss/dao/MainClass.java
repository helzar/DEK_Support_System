package com.nulp.dss.dao;

import com.nulp.dss.model.User;

public class MainClass {

	public static void main(String[] args) {
		User user = new UserDao().getById(1);
		System.out.println(user);
	}

}
