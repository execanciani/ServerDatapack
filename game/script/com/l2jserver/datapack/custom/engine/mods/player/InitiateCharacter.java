package com.l2jserver.datapack.custom.enginemods.mods.player;

import java.util.ArrayList;
import java.util.List;

import com.l2jserver.datapack.custom.enginemods.holders.ItemHolder;
import com.l2jserver.datapack.custom.enginemods.mods.AbstractMods;
import com.l2jserver.gameserver.datatables.ItemTable;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.ListenerRegisterType;
import com.l2jserver.gameserver.model.events.annotations.RegisterEvent;
import com.l2jserver.gameserver.model.events.annotations.RegisterType;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerCreate;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerDelete;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerLogin;

public class InitiateCharacter extends AbstractMods
{	
	private final List<ItemHolder> _soulshots = new ArrayList<>();
	{
		//soulshots S
		_soulshots.add(new ItemHolder(ItemTable.getInstance().getTemplate(1467), 10000));
		//blessed spiritshots s
		_soulshots.add(new ItemHolder(ItemTable.getInstance().getTemplate(3952), 10000));
	}
	
	public InitiateCharacter()
	{
		LOG.info("[InitiateCharacter]: Mod Loaded");
	}

	@Override
	public void onModState() {
		
	}

	@RegisterEvent(EventType.ON_PLAYER_CREATE)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void playerCreate(OnPlayerCreate event)
	{	
		L2PcInstance player = event.getActiveChar();
		
		if (player == null)
			return;
		
		//inicializamos el evento
		setValueDB(player.getObjectId(), "first login", "true");
		
		//teleport to giran
		player.teleToLocation(83013, 148623, -3469);
		
		//ponemos el lvl en 85
		if (player.getLevel() < 85)
		{
			player.setLevel(85);
		}
		
		//setHasEquipment(event.getActiveChar());
		//setMaxLvl(event.getActiveChar());
	}
	
	@RegisterEvent(EventType.ON_PLAYER_LOGIN)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void onPlayerLogin(OnPlayerLogin event)
	{
		L2PcInstance player = event.getActiveChar();
		if (player == null)
			return
		//check si es el primer login
		String first_login = getValueDB(player, "first login");
		if (first_login == null)
			return;
		if (first_login.equalsIgnoreCase("true"))
		{			
			//give soulshots
			for (ItemHolder item : _soulshots)
			{				
				player.getInventory().addItem("first", item.getId(), item.getCount(), player, null);
			}
			//removemos el first login
			removeValueDB(player.getObjectId(), "first login");
		}	
	}
	
	@RegisterEvent(EventType.ON_PLAYER_DELETE)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void onPlayerDelete(OnPlayerDelete event)
	{
		int playerId = event.getObjectId();
		if (playerId <= 0)
			return;
		//remove player value obj id
	    clearPlayerValueDB(playerId);
	}
	
	public static void main(String[] args) {
		new InitiateCharacter();
	}
}
