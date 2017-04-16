package com.stay4it.im.widget.chat.emo;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;

/** 
 * @author Stay  
 * @version create time：Oct 16, 2014 9:03:26 PM 
 */
public class EmoGroupManager {
	private static EmoGroupManager mInstance;
	public ArrayList<EmoPage> mEmoPages = null;
	public ArrayList<EmoGroup> mEmoGroups = null;
	public int totalPage = 0;
	
	private EmoGroupManager(){
		initializeData();
	}
	
	public static EmoGroupManager getInstance(){
		if (mInstance == null) {
			mInstance = new EmoGroupManager();
		}
		return mInstance;
	}
	
	public void initializeData(){
//		TODO get params from server
		mEmoGroups = new ArrayList<EmoGroup>();
		mEmoGroups.add(EmoGroup.getDefault());
		mEmoGroups.add(EmoGroup.getCustom());
		mEmoGroups.add(EmoGroup.getCustom());
		mEmoPages = new ArrayList<EmoPage>();
		EmoGroup group = null;
		EmoPage page = null;
		for (int i = 0; i < mEmoGroups.size(); i++) {
			group = mEmoGroups.get(i);
			group.startIndex = totalPage;
			totalPage += group.page;
			for (int j = 0; j < group.page; j++) {
				page = new EmoPage();
				page.totalIndex = mEmoPages.size();
				page.innerIndex = j;
				page.groupIndex = i;
				page.groupCount = group.page;
				this.mEmoPages.add(page);
			}
		}
	}

	public int getTotalPage() {
		return mEmoPages.size();
	}

	/**
	 * @param groupIndex index of the selected group view
	 * @return
	 */
	public EmoGroup getEmoGroup(int groupIndex) {
		return mEmoGroups.get(groupIndex);
	}
	
	/**
	 * @param pageIndex  index of the selected ViewPager's page
	 * @return
	 */
	public EmoPage getEmoPage(int pageIndex){
		return mEmoPages.get(pageIndex);
	}

	public int getTotalGroupCount() {
		return mEmoGroups.size();
	}
	
}
