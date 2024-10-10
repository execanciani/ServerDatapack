package com.l2jserver.datapack.custom.engine.mods.creature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.l2jserver.datapack.custom.engine.data.NpcData;
import com.l2jserver.datapack.custom.engine.holders.NpcHolder;
import com.l2jserver.datapack.custom.engine.mods.AbstractMods;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2GrandBossInstance;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2RaidBossInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;
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
	public void NpcS(NpcSpawn eventNpc)
	{
		L2Npc monster = eventNpc.npc();
		if (monster == null)
			return;	
		if (monster instanceof L2MonsterInstance)
		{
			if (monster.getLevel() < 35)
			{
				Location loc = monster.getLocation();
				int level = monster.getLevel();
				monster.getSpawn().stopRespawn();
				monster.deleteMe();
				
				for (NpcHolder npcH : NpcData.getAllNpcs())
				{
					if (npcH.getNpc() instanceof L2MonsterInstance)
					{
						if (npcH.getLevel() < 35)
							continue;
						if (npcH.getLevel() == level + 34)
						{
							addSpawn(npcH.getId(), loc);
						}	
					}
				}
			}
		}
	}
	
	private List<L2Npc> getNpcList(Collection<NpcHolder> lista, int monsterLevel)
	{
		List<L2Npc> nhl = new ArrayList<>();
		for (NpcHolder nh: lista)
		{
			L2Npc npc = nh.getNpc(nh.getObjectId());
			if (npc == null)
				continue;
			if  (npc instanceof L2RaidBossInstance || npc instanceof L2GrandBossInstance)
				continue;
			if (npc.isRaid() || npc.isRaidMinion() || npc.isMinion())
				continue;
			if (npc instanceof L2MonsterInstance)
			{
				if (npc.getLevel() < 35)
				{
					continue;
				}
				if (npc.getLevel() == monsterLevel + 34)
				{
					nhl.add(npc);
				}
			}
		}
		System.out.println(nhl);
		return nhl;
	}
	

	public static void main(String[] args) {
		new DesPawnCreature();
	}
	
}
