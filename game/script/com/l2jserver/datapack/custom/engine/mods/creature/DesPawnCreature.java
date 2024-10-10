package com.l2jserver.datapack.custom.engine.mods.creature;

import com.l2jserver.commons.util.Rnd;
import com.l2jserver.datapack.custom.engine.data.NpcData;
import com.l2jserver.datapack.custom.engine.holders.NpcHolder;
import com.l2jserver.datapack.custom.engine.mods.AbstractMods;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.ListenerRegisterType;
import com.l2jserver.gameserver.model.events.annotations.RegisterEvent;
import com.l2jserver.gameserver.model.events.annotations.RegisterType;
import com.l2jserver.gameserver.model.events.impl.character.npc.NpcSpawn;

public class DesPawnCreature extends AbstractMods {

	public DesPawnCreature() {

		LOG.info("[DesPawnCreature]: Mod Cargado");
	}
	
	@Override
	public void onModState() {
		
		
	}
	
	@RegisterEvent(EventType.NPC_SPAWN)
	@RegisterType(ListenerRegisterType.GLOBAL_MONSTERS)
	public void OnNpcSpawn(NpcSpawn npcSpawn)
	{
		L2Npc monster = npcSpawn.npc();
		if (monster == null)
			return;
		
		if (monster.isAttackable())
		{
			if (monster.getSpawn() == null)
			{
				return;
			}
			
			if (monster.getLevel() < 35)
			{
				while (true)
				{
					for (NpcHolder npch : NpcData.getAllNpcs())
					{
						L2Npc npc = npch.getNpc(npch.getObjectId());
						if (npc.getLevel() == monster.getLevel())
						{
							if (Rnd.get(2) == 1)
							{
								Location loc = monster.getLocation();
								npc.spawnMe(loc.getX(), loc.getY(), loc.getZ());
								
								monster.deleteMe();
								break;
							}
						}
					}
					break;
				}
			}
			
		}
	}
	
	
	public static void main(String[] args) {
		new DesPawnCreature();
	}
	
}
