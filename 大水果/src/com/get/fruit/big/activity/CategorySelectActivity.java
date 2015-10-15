package com.get.fruit.big.activity;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.get.fruit.big.R;
import com.get.fruit.big.bean.Category.CategoryName;
import com.get.fruit.big.bean.Category.Function;
import com.get.fruit.big.bean.Category.Taste;
import com.get.fruit.big.bean.Fruit.Color;
import com.get.fruit.big.bean.Fruit.Origin;
import com.get.fruit.big.bean.Fruit.Season;
import com.get.fruit.view.big.tree.SimpleTreeListViewAdapter;
import com.get.fruit.view.big.tree.TreeListViewAdapter.OnTreeNodeClickListener;
import com.get.fruit.view.big.tree.TreeNodeId;
import com.get.fruit.view.big.tree.TreeNodeLabel;
import com.get.fruit.view.big.tree.TreeNodePid;

public class CategorySelectActivity extends BaseActivity {

	private ListView mTree;
	private SimpleTreeListViewAdapter<CategoryNode> mAdapter;
	private List<CategoryNode> mDatas;
	String categoryBy;
	public enum CategoryBy{种类,季节,产地,颜色,功能,口味}
	Intent intent;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		intent=getIntent();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category_select);
		categoryBy=getIntent().getStringExtra("categoryBy");
		mTree = (ListView) findViewById(R.id.listView);

		initDatas();
			try {
				mAdapter = new SimpleTreeListViewAdapter<CategoryNode>(mTree, this,mDatas, 0);
				mTree.setAdapter(mAdapter);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

		initEvent();
	}

	private void initEvent()
	{
		mAdapter.setOnTreeNodeClickListener(new OnTreeNodeClickListener()
		{

			@Override
			public void onClick(com.get.fruit.view.big.tree.Node node,
					int position) {
				if (node.isLeaf())
				{
					
					intent.putExtra("which", node.getParent().getId());
					intent.putExtra("value", node.getName());
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
	}
	
	
	private void initDatas()
	{
		int i=1;
		CategoryNode node;
		mDatas = new ArrayList<CategoryNode>();
		for(CategoryBy categoryBy:CategoryBy.values()){
			node=new CategoryNode(i, 0, categoryBy.toString());
			mDatas.add(node);
			i++;
		}
		for(CategoryName item:CategoryName.values()){
			node=new CategoryNode(i, 1, item.toString());
			mDatas.add(node);
			i++;
		}
		for(Season item:Season.values()){
			node=new CategoryNode(i, 2, item.toString());
			mDatas.add(node);
			i++;
		}
		for(Origin item:Origin.values()){
			node=new CategoryNode(i, 3, item.toString());
			mDatas.add(node);
			i++;
		}
		for(Color item:Color.values()){
			node=new CategoryNode(i, 4, item.toString());
			mDatas.add(node);
			i++;
		}
		for(Taste item:Taste.values()){
			node=new CategoryNode(i, 5, item.toString());
			mDatas.add(node);
			i++;
		}
		for(Function item:Function.values()){
			node=new CategoryNode(i, 6, item.toString());
			mDatas.add(node);
			i++;
		}
		
	}
	
	
	public class CategoryNode{
		@TreeNodeId
		private int _id;
		@TreeNodePid
		private int parentId;
		@TreeNodeLabel
		private String name;
		
		public CategoryNode(int _id, int parentId, String name)
		{
			this._id = _id;
			this.parentId = parentId;
			this.name = name;
		}

		public int get_id()
		{
			return _id;
		}

		public void set_id(int _id)
		{
			this._id = _id;
		}

		public int getParentId()
		{
			return parentId;
		}

		public void setParentId(int parentId)
		{
			this.parentId = parentId;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}
	}
}
