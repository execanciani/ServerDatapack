package com.l2jserver.datapack.custom.engine.holders;

import java.util.Map;

import com.l2jserver.datapack.custom.engine.enums.ItemType;

public class ItemShopHolder
{

	private Map<String, Integer> _multisell_id;
	private String _name;
	private ItemType _type;

	public ItemShopHolder()
	{

	}

	public ItemShopHolder(ItemType itemType, Map<String, Integer> multisell_id, String name)
	{
		_type = itemType;
		_multisell_id = multisell_id;
		_name = name;
	}

	public Map<String, Integer> multisell_ids()
	{
		return _multisell_id;
	}

	public String name()
	{
		return _name;
	}

	public ItemType type()
	{
		return _type;
	}

	public void set_multisell_ids(Map<String, Integer> _ids)
	{
		this._multisell_id = _ids;
	}

	public void set_name(String _name)
	{
		this._name = _name;
	}

	public void set_type(ItemType _type)
	{
		this._type = _type;
	}
}
