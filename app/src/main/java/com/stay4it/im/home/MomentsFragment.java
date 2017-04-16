package com.stay4it.im.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stay4it.im.BaseFragment;
import com.stay4it.im.R;
import com.stay4it.im.widget.row.BaseRowDescriptor;
import com.stay4it.im.widget.row.ContainerView;
import com.stay4it.im.widget.row.GroupDescriptor;
import com.stay4it.im.widget.row.MomentsRowDescriptor;
import com.stay4it.im.widget.row.NormalRowDescriptor;
import com.stay4it.im.widget.row.OnRowClickListener;
import com.stay4it.im.widget.row.RowActionEnum;

import java.util.ArrayList;

/** 
 * @author Stay  
 * @version create time：Apr 12, 2015 11:09:17 AM 
 */
public class MomentsFragment extends BaseFragment implements OnRowClickListener {
	private ContainerView mMomentsContainerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_moments, null);
		mMomentsContainerView = (ContainerView)view.findViewById(R.id.mMomentsContainerView);
		initializeView();
		return view;
	}
	
	private void initializeView() {
		ArrayList<GroupDescriptor> groupDescriptors = new ArrayList<GroupDescriptor>();

		ArrayList<BaseRowDescriptor> descriptors0 = new ArrayList<BaseRowDescriptor>();
		descriptors0.add(new MomentsRowDescriptor(R.drawable.more_my_album, "朋友圈", "url", RowActionEnum.MOMENTS));
		GroupDescriptor descriptor0 = new GroupDescriptor(descriptors0);

		groupDescriptors.add(descriptor0);

		ArrayList<BaseRowDescriptor> descriptors1 = new ArrayList<BaseRowDescriptor>();
		descriptors1.add(new NormalRowDescriptor(R.drawable.more_my_album, "扫一扫", RowActionEnum.SCAN_QR));
		descriptors1.add(new NormalRowDescriptor(R.drawable.more_my_favorite, "摇一摇", RowActionEnum.SHAKE));
		GroupDescriptor descriptor1 = new GroupDescriptor(descriptors1);

		groupDescriptors.add(descriptor1);

		ArrayList<BaseRowDescriptor> descriptors2 = new ArrayList<BaseRowDescriptor>();
		descriptors2.add(new NormalRowDescriptor(R.drawable.more_emoji_store, "附近的人", RowActionEnum.NEARBY));
		descriptors2.add(new NormalRowDescriptor(R.drawable.more_my_bank_card, "漂流瓶", RowActionEnum.DRIFT_BOTTLE));
		GroupDescriptor descriptor2 = new GroupDescriptor(descriptors2);

		groupDescriptors.add(descriptor2);

		ArrayList<BaseRowDescriptor> descriptors3 = new ArrayList<BaseRowDescriptor>();
		descriptors3.add(new NormalRowDescriptor(R.drawable.more_setting, "购物", RowActionEnum.SHOPPING));
		descriptors3.add(new NormalRowDescriptor(R.drawable.more_setting, "游戏", RowActionEnum.GAMES));
		GroupDescriptor descriptor3 = new GroupDescriptor(descriptors3);

		groupDescriptors.add(descriptor3);

		mMomentsContainerView.initializeData(groupDescriptors, this);
		mMomentsContainerView.notifyDataChanged();
	}

	@Override
	public void onRowClick(RowActionEnum action) {
		Toast.makeText(getActivity(), "row click on:" + action.name(), 0).show();
		// startActivity(new Intent÷)
	}
}
