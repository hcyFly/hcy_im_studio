package com.stay4it.im.db;

import com.stay4it.im.IMApplication;

/**
 * @author Stay
 * @version create time：Sep 5, 2014 11:37:46 AM
 */
public class DBController {
	private static DBController instance;
	private OrmDBHelper mDBhelper;

	private DBController() {
		mDBhelper = new OrmDBHelper(IMApplication.gContext);
		mDBhelper.getWritableDatabase();
	}

	public static DBController getInstance() {
		if (instance == null) {
			instance = new DBController();
		}
		return instance;
	}
	
	public OrmDBHelper getDB(){
		return mDBhelper;
	}

}
