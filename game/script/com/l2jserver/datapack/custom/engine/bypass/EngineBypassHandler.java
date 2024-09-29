package com.l2jserver.datapack.custom.enginemods.bypass;

import com.l2jserver.gameserver.handler.IBypassHandler;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class EngineBypassHandler implements IBypassHandler
{
	public static final String BYPASS = "Engine";
	
	private static final String[] BYPASS_LIST = new String[] {
		BYPASS
	};
	
	public EngineBypassHandler()
	{
	}

	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character bypassOrigin) {
		if ((bypassOrigin == null) || !bypassOrigin.isNpc()) 
		{
			return false;
		}
		return true;
	}

	@Override
	public String[] getBypassList() {
		return BYPASS_LIST;
	}
	
	public static EngineBypassHandler getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public static final class SingletonHolder {
		protected static final EngineBypassHandler INSTANCE = new EngineBypassHandler();
	}
}
