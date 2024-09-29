package com.l2jserver.datapack.custom.enginemods.mods.player;

import java.util.logging.Level;

import com.l2jserver.datapack.custom.enginemods.mods.AbstractMods;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.ListenerRegisterType;
import com.l2jserver.gameserver.model.events.annotations.RegisterEvent;
import com.l2jserver.gameserver.model.events.annotations.RegisterType;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerLogin;


public class PlayerEnterWorld extends AbstractMods{

	public PlayerEnterWorld() {
		//registerMod(true);
	}
	
	@Override
	public void onModState() {
		
		
	}

	/**
	 * This method will be invoked as soon a a player logs into the game.<br>
	 * This listener is registered into global players container.
	 * @param event
	 */
	@RegisterEvent(EventType.ON_PLAYER_LOGIN)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void onPlayerLogin(OnPlayerLogin event) {
		System.out.println("PlayerEnterWorld class: "+event.getActiveChar().getName());
		LOG.log(Level.INFO, getClass().getSimpleName() + ": Player: " + event.getActiveChar() + " has logged in!");
	}
	
	public static void main(String[] args) {
		new PlayerEnterWorld();
	}
}
