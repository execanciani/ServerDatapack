package com.l2jserver.datapack.custom.enginemods.mods.zone;

import java.util.Collection;
import java.util.List;

import com.l2jserver.commons.util.Rnd;
import com.l2jserver.datapack.custom.enginemods.mods.AbstractMods;
import com.l2jserver.gameserver.instancemanager.ZoneManager;
import com.l2jserver.gameserver.model.zone.L2ZoneForm;
import com.l2jserver.gameserver.model.zone.type.L2WaterZone;

public class ZoneFarm extends AbstractMods
{
	private final L2ZoneForm _zone;
	private Collection<L2WaterZone> water_zones;
	
	public ZoneFarm()
	{
		LOG.info("[ZoneFarm]: Mod cargado");
		
		_zone = ZoneManager.getInstance().getZoneById(40101).getZone();
		water_zones = ZoneManager.getInstance().getAllZones(L2WaterZone.class);
		
		//spawneamos
		for (int i = 1; i<50; i++)
		{
			final int[] randomPoint = _zone.getRandomPoint();
			if (isInsideZone(randomPoint[0], randomPoint[1]))
			{
				continue;
			}
			
			int z = Rnd.get(-30000, 30000);
			addSpawn(18804, randomPoint[0], randomPoint[1], z, 0, false, 0, false, 0);
			System.out.println("Mob spawned in x: " + randomPoint[0] + " y: " + randomPoint[1] + " z: " +z );
		}
	}
	
	private boolean isInsideZone(int x, int y)
	{
		for (L2WaterZone zone : water_zones)
		{
			if (zone.isInsideZone(x, y))
				return true;
		}
		return false;
	}
	
	@Override
	public void onModState()
	{
		
	}

	
	public static void main(String[] args)
	{
		new ZoneFarm();
	}
}
