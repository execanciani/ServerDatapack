/*
 * L2J_EngineMods
 * Engine developed by Fissban.
 *
 * This software is not free and you do not have permission
 * to distribute without the permission of its owner.
 *
 * This software is distributed only under the rule
 * of www.devsadmins.com.
 * 
 * Contact us with any questions by the media
 * provided by our web or email marco.faccio@gmail.com
 */
package com.l2jserver.datapack.custom.engine.holders;

import com.l2jserver.datapack.custom.engine.enums.BuffType;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.skills.Skill;

/**
 * Clase usada en NpcBufferScheme
 * @author fissban
 */
public class BuffHolder
{
	private int _id;
	private int _level;
	private BuffType _type = BuffType.NONE;
	private String _description;
	
	public BuffHolder(int id, int level)
	{
		_id = id;
		_level = level;
	}
	
	public BuffHolder(BuffType type, int id, int level, String description)
	{
		_type = type;
		_id = id;
		_level = level;
		_description = description;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public int getLevel()
	{
		return _level;
	}
	
	public void type(BuffType type)
	{
		_type = type;
	}
	
	public BuffType getType()
	{
		return _type;
	}
	
	public void description(String description)
	{
		_description = description;
	}
	
	public String getDescription()
	{
		return _description;
	}
	
	/**
	 * @return the L2Skill associated to the id/value.
	 */
	public final Skill getSkill()
	{
		return SkillData.getInstance().getSkill(_id,_level);
	}
}
