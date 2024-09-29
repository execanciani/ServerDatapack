package com.l2jserver.datapack.custom.engine.holders;

import com.l2jserver.gameserver.model.items.L2Item;

public class ItemHolder
{
	private L2Item _item;
	private int _id;
	private int _count;
	
	public ItemHolder()
	{
		 
	}
	
	public ItemHolder(int id, int count)
	{
		 _id = id;
		 _count = count;
	}
	
	public ItemHolder(L2Item item, int count)
	{
		 _item = item;
		 _id = item.getId();
		 _count = count;
	}

	public int getId()
	{
		return _id;
	}

	public void setId(int _id)
	{
		this._id = _id;
	}

	public int getCount()
	{
		return _count;
	}

	public void setCount(int _count)
	{
		this._count = _count;
	}

	public L2Item getItem()
	{
		return _item;
	}

	public void setItem(L2Item _item)
	{
		this._item = _item;
	}
}
