package com.stay4it.im.widget.row;

import java.util.ArrayList;

/**
 * @author Stay
 * @version create time：Oct 10, 2014 4:34:23 PM
 */
public class GroupDescriptor {
	public String title;
	public ArrayList<BaseRowDescriptor> descriptors;

	public GroupDescriptor(String title, ArrayList<BaseRowDescriptor> descriptors) {
		this.title = title;
		this.descriptors = descriptors;
	}

	public GroupDescriptor(ArrayList<BaseRowDescriptor> descriptors) {
		this.descriptors = descriptors;
	}

}
