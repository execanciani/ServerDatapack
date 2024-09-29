package com.l2jserver.datapack.custom.engine.holders;

import com.l2jserver.gameserver.data.xml.impl.SkillTreesData;
import com.l2jserver.gameserver.engines.DocumentEngine;
import com.l2jserver.gameserver.engines.skills.DocumentSkill;
import com.l2jserver.gameserver.engines.skills.DocumentSkill.SkillInfo;
import com.l2jserver.gameserver.model.skills.Skill;

/**
 * A generic int/int container.
 */
public class IntIntHolder
{
	private int _id;
	private int _value;
	
	public IntIntHolder(int id, int value)
	{
		_id = id;
		_value = value;
	}
	
	@Override
	public String toString()
	{
		return "IntIntHolder [id=" + _id + " value=" + _value + "]";
	}
	
	public int getId()
	{
		return _id;
	}
	
	public int getValue()
	{
		return _value;
	}
	
	public void setId(int id)
	{
		_id = id;
	}
	
	public void setValue(int value)
	{
		_value = value;
	}
	
//	/**
//	 * @return The {@link L2Skill} associated to the id/value stored on this {@link IntIntHolder}.
//	 */
//	public final Skill getSkill()
//	{
//		for (Skill skill : DocumentSkill.getSkills())
//			if (_id == skill.getId())
//				return skill;
//		
//	}
}