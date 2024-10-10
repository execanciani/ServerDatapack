package com.l2jserver.datapack.custom.engine.holders;

import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.L2Npc;

public class NpcHolder {

	private int _objectId;
	private String _name;
	
	public NpcHolder(int objectId, String name)
	{
		_objectId = objectId;
		_name = name;
	}
	
	public int getObjectId()
	{
		return _objectId;
	}
	
	public void setObjectId(int objectId)
	{
		_objectId = objectId;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
	
	private int _team;
	
	public void setTeam(int team)
	{
		_team = team;
	}
	
	public int getTeam()
	{
		return _team;
	}
	
	public int getLevel()
	{
		return getNpc(_objectId).getLevel();
	}
	
	public int getId()
	{
		return getNpc(_objectId).getId();
	}
	
	public L2Npc getNpc()
	{
		return (L2Npc) L2World.getInstance().findObject(_objectId);
	}
	
	public L2Npc getNpc(L2Npc npc)
	{
		return	(L2Npc) L2World.getInstance().findObject(npc.getObjectId());
	}
	
	public L2Npc getNpc(int objId)
	{
		return (L2Npc) L2World.getInstance().findObject(objId);
	}
}
