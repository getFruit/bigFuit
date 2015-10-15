package com.get.fruit.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.get.fruit.R;
import com.get.fruit.activity.BaseFragment;

public class GardenFragment extends BaseFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_garden, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		//initView();
	}

	/* (non-Javadoc)
	 * @see com.get.fruit.activity.BaseFragment#lazyLoad()
	 */
	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub
		
	}
	
}
