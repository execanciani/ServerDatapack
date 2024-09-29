package main.holders;

import main.data.SkillData;
import net.sf.l2j.gameserver.skills.L2Skill;

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
	
	public L2Skill getL2Skill()
	{
		return SkillData.getL2Skill(_id, _level);
	}
}
