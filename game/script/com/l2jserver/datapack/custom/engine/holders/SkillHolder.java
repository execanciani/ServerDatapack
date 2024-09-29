package com.l2jserver.datapack.custom.engine.holders;

import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.skills.Skill;

public class SkillHolder
{
	private int _id;
	private int _level;
	private boolean _isReadyForUse;
	private long _seconds;
	
	public SkillHolder(int id, int level)
	{
		_id = id;
		_level = level;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public int getLevel()
	{
		return _level;
	}
	
	public void setReadyForUse(boolean isReadyForUse)
	{
		_isReadyForUse = isReadyForUse;
	}
	
	public boolean isReadyForUse() 
	{
		return _isReadyForUse;
	}
	
	public void setSeconds(long time)
	{
		//30    
		_seconds = System.currentTimeMillis() + time;
	}
	
	public long getSeconds()
	{
		return _seconds;
	}
	
	public Skill getL2Skill()
	{
		return SkillData.getInstance().getSkill(_id, _level);
	}
}
