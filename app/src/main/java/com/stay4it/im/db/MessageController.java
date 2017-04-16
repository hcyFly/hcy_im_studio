package com.stay4it.im.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.stay4it.im.entities.Message;
import com.stay4it.im.untils.Constants;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Stay
 * @version create time：Oct 23, 2014 10:45:30 AM
 */
public class MessageController {
	public static Dao<Message, String> getDao() throws SQLException {
		return DBController.getInstance().getDB().getDao(Message.class);
	}

	public static void addOrUpdate(Message message) {
		try {
			getDao().createOrUpdate(message);
			ConversationController.syncMessage(message);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void delete(Message message) {
		try {
			getDao().delete(message);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Message> queryAllByTimeAsc(String id1, String id2, int state, long timestamp) {
		ArrayList<Message> messages = new ArrayList<Message>();
		try {
			QueryBuilder<Message, String> queryBuilder = getDao().queryBuilder();
			queryBuilder.orderBy(Message.TIMESTAMP, false);
			Where<Message, String> where = queryBuilder.where();
			where.in(Message.SENDERID, id1, id2);
			where.and();
			where.in(Message.RECEIVERID, id1, id2);
			if (state == Constants.LOADMORE) {
				where.and();
				where.lt(Message.TIMESTAMP, timestamp);
			}
			queryBuilder.limit(Constants.PAGECOUNT);
			// (senderid=id1 and receiverid=id2) or (senderid=id2 and
			// receiverid=id1)
			// "where senderid in(id1,id2) and receiverid in(id1,id2) order by timestamp desc";
			List<Message> tmp =  getDao().query(queryBuilder.prepare());
			if (tmp == null || tmp.size() == 0) {
				return messages;
			}
			for (int i = tmp.size() - 1; i >= 0; i--) {
				messages.add(tmp.get(i));
			}
			return messages;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messages;
	}

	public static Message queryById(String id) {
		try {
			return getDao().queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void addOrUpdate(ArrayList<Message> t) {
		try {
			if (t != null && t.size() > 0) {
				for (Message message : t) {
					getDao().createOrUpdate(message);
				}
//				if (queryById(t.get(0).get_id()) == null) {
//					deleteOldData(t.get(0));
//				}
//				ConversationController.syncMessage(t.get(t.size() - 1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void deleteOldData(Message message) {
		// TODO delete old data
		try {
			DeleteBuilder<Message, String> builder = getDao().deleteBuilder();
			Where<Message, String> where = builder.where();
			where.in(Message.SENDERID, message.getSenderId(), message.getReceiverId());
			where.and();
			where.in(Message.RECEIVERID, message.getSenderId(), message.getReceiverId());
			where.and();
			where.lt(Message.TIMESTAMP, message.getTimestamp());
			getDao().delete(builder.prepare());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param targetId
	 * @param selfId
	 * @return query latest read message timestamp
	 */
	public static long queryEndTimestamp(String targetId, String selfId) {
		try {
			QueryBuilder<Message, String> queryBuilder = getDao().queryBuilder();
			queryBuilder.orderBy(Message.TIMESTAMP, false);
			Where<Message, String> where = queryBuilder.where();
			where.eq(Message.RECEIVERID, selfId);
			where.and();
			where.eq(Message.SENDERID, targetId);
			where.and();
			where.eq(Message.ISREAD, true);
			queryBuilder.limit(1l);
			// (senderid=id1 and receiverid=id2) or (senderid=id2 and
			// receiverid=id1)
			// "where senderid in(id1,id2) and receiverid in(id1,id2) order by timestamp desc";
			List<Message> messages2 = getDao().query(queryBuilder.prepare());
			if (messages2 != null && messages2.size() > 0) {
				return messages2.get(0).getTimestamp();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void markAsRead(String targetId, String selfId) {
		try {
			getDao().updateRaw("UPDATE message SET " + Message.ISREAD + "=? WHERE "+Message.SENDERID+"=? and "+Message.RECEIVERID+"=?","1",targetId,selfId);
			ConversationController.markAsRead(targetId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
