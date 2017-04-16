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
import com.stay4it.im.widget.row.NormalRowDescriptor;
import com.stay4it.im.widget.row.OnRowClickListener;
import com.stay4it.im.widget.row.ProfileRowDescriptor;
import com.stay4it.im.widget.row.RowActionEnum;

import java.util.ArrayList;

/** 
 * @author Stay  
 * @version create time：Apr 12, 2015 11:09:17 AM 
 */
public class ProfileFragment extends BaseFragment implements OnRowClickListener {
	private ContainerView mProfileContainerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile, null);
		mProfileContainerView = (ContainerView)view.findViewById(R.id.mProfileContainerView);
		initializeView();
		return view;
	}
	
	private void initializeView() {
		ArrayList<GroupDescriptor> groupDescriptors = new ArrayList<GroupDescriptor>();

		ArrayList<BaseRowDescriptor> descriptors0 = new ArrayList<BaseRowDescriptor>();
		descriptors0.add(new ProfileRowDescriptor("", "Stay", "微信号:StayAlways", RowActionEnum.WECHAT_ID));
		GroupDescriptor descriptor0 = new GroupDescriptor(descriptors0);

		groupDescriptors.add(descriptor0);

		ArrayList<BaseRowDescriptor> descriptors1 = new ArrayList<BaseRowDescriptor>();
		descriptors1.add(new NormalRowDescriptor(R.drawable.more_my_album, "相册", RowActionEnum.MY_POSTS));
		descriptors1.add(new NormalRowDescriptor(R.drawable.more_my_favorite, "收藏", RowActionEnum.FAVORITE_MSG));
		GroupDescriptor descriptor1 = new GroupDescriptor(descriptors1);

		groupDescriptors.add(descriptor1);

		ArrayList<BaseRowDescriptor> descriptors2 = new ArrayList<BaseRowDescriptor>();
		descriptors2.add(new NormalRowDescriptor(R.drawable.more_my_bank_card, "钱包", RowActionEnum.MY_BANK_CARD));
		GroupDescriptor descriptor2 = new GroupDescriptor(descriptors2);

		groupDescriptors.add(descriptor2);

		ArrayList<BaseRowDescriptor> descriptors3 = new ArrayList<BaseRowDescriptor>();
		descriptors3.add(new NormalRowDescriptor(R.drawable.more_setting, "设置", RowActionEnum.SETTINGS));
		GroupDescriptor descriptor3 = new GroupDescriptor(descriptors3);

		groupDescriptors.add(descriptor3);

		mProfileContainerView.initializeData(groupDescriptors, this);
		mProfileContainerView.notifyDataChanged();
	}

	@Override
	public void onRowClick(RowActionEnum action) {
		Toast.makeText(getActivity(), "row click on:" + action.name(), 0).show();
		// startActivity(new Intent÷)
	}
}
