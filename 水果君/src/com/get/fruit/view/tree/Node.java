package com.get.fruit.view.tree;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

import android.R.integer;
import android.media.Image;

public class Node
{
	public Node()
	{
	}

	public Node(int id, int pId, String name)
	{
		this.id = id;
		this.pId = pId;
		this.name = name;
	}
	public Node(int id, int pId, String name,int image ,BmobFile file)
	{
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.image=image;
		this.file=file;
	}



	private int id;
	/**
	 * 跟节点的pid=0
	 */
	private int pId = 0;
	private String name;
	private int image;
	private BmobFile file;
	/**
	 * 树的层级
	 */
	private int level;
	/**
	 * 是否是展�?
	 */
	private boolean isExpand = false;
	private int icon;

	private Node parent;
	private List<Node> children = new ArrayList<Node>();

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getpId()
	{
		return pId;
	}

	public void setpId(int pId)
	{
		this.pId = pId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getIcon()
	{
		return icon;
	}

	public void setIcon(int icon)
	{
		this.icon = icon;
	}

	public Node getParent()
	{
		return parent;
	}

	public void setParent(Node parent)
	{
		this.parent = parent;
	}

	public List<Node> getChildren()
	{
		return children;
	}

	public void setChildren(List<Node> children)
	{
		this.children = children;
	}

	
	
	public BmobFile getFile() {
		return file;
	}

	public void setFile(BmobFile file) {
		this.file = file;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	/**
	 * 属否是根节点
	 * 
	 * @return
	 */
	public boolean isRoot()
	{
		return parent == null;
	}

	/**
	 * 判断当前父节点的收缩状�??
	 * 
	 * @return
	 */
	public boolean isParentExpand()
	{
		if (parent == null)
			return false;
		return parent.isExpand();
	}

	/**
	 * 是否是叶子节�?
	 * 
	 * @return
	 */
	public boolean isLeaf()
	{
		return children.size() == 0;
	}

	/**
	 * 得到当前节点的层�?
	 * @return
	 */
	public int getLevel()
	{
		return parent == null ? 0 : parent.getLevel() + 1;
	}
	
	public boolean isExpand()
	{
		return isExpand;
	}

	public void setExpand(boolean isExpand)
	{
		this.isExpand = isExpand;
		
		if(!isExpand)
		{
			for(Node node :children)
			{
				node.setExpand(false);
			}
		}
	}
	
	

	public void setLevel(int level)
	{
		this.level = level;
	}

	@Override
	public String toString()
	{
		return "Node [id=" + id + ", pId=" + pId + ", name=" + name
				+ ", level=" + level + ", isExpand=" + isExpand + ", icon="
				+ icon + "]\n";
	}

}
