package com.stay4it.im.untils;

import java.util.HashMap;

/**
 * @author Stay
 * @version create time：Dec 12, 2013 3:58:38 PM
 */
public class RefreshManager {
	private static RefreshManager instance;
	private HashMap<String, Long> map = null;
	private static final long TIME_GAP = 1000 * 60 * 60;

	private RefreshManager() {
		map = new HashMap<String, Long>();
	}

	public static RefreshManager getInstance() {
		if (instance == null) {
			instance = new RefreshManager();
		}
		return instance;
	}

	public boolean shouldRefresh(Class<?> key) {
		return shouldRefresh(key.getSimpleName());
	}

	public boolean shouldRefresh(String key) {
		if (!map.containsKey(key)) {
			map.put(key, System.currentTimeMillis());
			return true;
		} else {
			if (System.currentTimeMillis() - map.get(key) > TIME_GAP) {
				map.put(key, System.currentTimeMillis());
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean shouldRefresh(Class<?> key, String id) {
		if (TextUtil.isValidate(id)) {
			return shouldRefresh(key.getSimpleName() + id);
		} else {
			return shouldRefresh(key);
		}
	}

	public void clearRefresh(Class<?> key) {
		clearRefresh(key.getSimpleName());
	}

	public void clearRefresh(Class<?> key, String id) {
		if (TextUtil.isValidate(id)) {
			clearRefresh(key.getSimpleName() + id);
		} else {
			clearRefresh(key);
		}
	}

	public void clearRefresh(String key) {
		map.remove(key);
	}

	public void clear() {
		map.clear();
	}

}
