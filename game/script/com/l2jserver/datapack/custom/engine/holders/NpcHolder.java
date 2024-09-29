package main.holders;

import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Npc;

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
	
	public Npc getNpc(Npc npc)
	{
		return	(Npc) World.getInstance().getObject(npc.getObjectId());
	}
	
	public Npc getNpc(int objId)
	{
		return (Npc) World.getInstance().getObject(objId);
	}
}
