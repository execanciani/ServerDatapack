package com.l2jserver.datapack.custom.enginemods.util;

import com.l2jserver.gameserver.model.actor.L2Summon;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PetInstance;

public class UtilNpc
{
	public static boolean isPet(L2Summon pet)
	{
		if (pet == null)
			return false;
		return pet instanceof L2PetInstance;
	}
}
