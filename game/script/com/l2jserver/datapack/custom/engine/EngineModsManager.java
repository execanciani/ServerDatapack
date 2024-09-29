package com.l2jserver.datapack.custom.enginemods;

import com.l2jserver.datapack.custom.enginemods.bypass.EngineBypassHandler;
import com.l2jserver.datapack.custom.enginemods.data.ModsData;
import com.l2jserver.datapack.custom.enginemods.data.PlayerData;
import com.l2jserver.datapack.custom.enginemods.data.SchemeBuffData;
import com.l2jserver.datapack.custom.enginemods.mods.AbstractMods;
import com.l2jserver.gameserver.handler.BypassHandler;

public class EngineModsManager extends AbstractMods
{
	
	public EngineModsManager()
	{	
		LOG.info("[EngineModsManager]: Mod Loaded");
		
		load();
	}
	
	public static void load()
	{
		BypassHandler.getInstance().registerHandler(EngineBypassHandler.getInstance());
		
		//ConfigData.load();
		PlayerData.load();
		ModsData.load();
		SchemeBuffData.load();
	}

	@Override
	public void onModState() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		new EngineModsManager();
	}
}
