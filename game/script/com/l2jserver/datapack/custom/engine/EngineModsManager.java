package com.l2jserver.datapack.custom.engine;

import com.l2jserver.datapack.custom.engine.bypass.EngineBypassHandler;
import com.l2jserver.datapack.custom.engine.data.ModsData;
import com.l2jserver.datapack.custom.engine.data.PlayerData;
import com.l2jserver.datapack.custom.engine.data.SchemeBuffData;
import com.l2jserver.datapack.custom.engine.mods.AbstractMods;
import com.l2jserver.gameserver.handler.BypassHandler;

public class EngineModsManager extends AbstractMods
{
	
	public EngineModsManager()
	{	
		LOG.info("[EngineModsManager]: Mod Cargado");
		
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
