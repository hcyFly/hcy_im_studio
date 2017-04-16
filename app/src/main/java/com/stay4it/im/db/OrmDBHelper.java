package com.stay4it.im.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.stay4it.im.entities.Conversation;
import com.stay4it.im.entities.Message;

/** 
 * @author Stay  
 * @version create time：Sep 5, 2014 11:36:04 AM 
 */
public class OrmDBHelper extends OrmLiteSqliteOpenHelper{
	
	public static final String DB_NAME = "stay4it";
	public static final int DB_VERSION = 1;

	public OrmDBHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion) {
		super(context, DB_NAME, factory, DB_VERSION);
	}

	public OrmDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Message.class);
			TableUtils.createTable(connectionSource, Conversation.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int arg2, int arg3) {
	}

}
