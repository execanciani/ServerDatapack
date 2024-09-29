package com.l2jserver.datapack.custom.engine.data;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.l2jserver.datapack.custom.engine.holders.NpcHolder;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.L2Npc;

public class NpcData {

	private static final Logger LOG = Logger.getLogger(PlayerData.class.getName());	
	private static final Map<Integer, NpcHolder> _npcs = new ConcurrentHashMap<>();

	
	public NpcData() 
	{
		//
	}
	
	public static void load()
	{
		// prevenimos datos duplicados en cado de recargar este metodo
		_npcs.clear();
		
		
		for (L2Object n : L2World.getInstance().getVisibleObjects())
		{
			if (n instanceof L2Npc)
			{
				_npcs.put(n.getObjectId(), new NpcHolder(n.getObjectId(),n.getName()));
			}
		}
		LOG.info(NpcData.class.getSimpleName() + " load " + _npcs.size() + " npcs");
	}
	
	public static synchronized NpcHolder get(L2Npc npc)
	{
		return _npcs.get(npc.getObjectId());
	}
	
	public static synchronized NpcHolder get(int objectId)
	{
		return _npcs.get(objectId);
	}
	
	public static synchronized void add(int objectId, String name)
	{
		_npcs.put(objectId, new NpcHolder(objectId, name));
	}
	
	public static synchronized Collection<NpcHolder> getAllNpcs()
	{
		return _npcs.values();
	}
}
