/*
 * L2J_EngineMods
 * Engine developed by Fissban.
 *
 * This software is not free and you do not have permission
 * to distribute without the permission of its owner.
 *
 * This software is distributed only under the rule
 * of www.devsadmins.com.
 * 
 * Contact us with any questions by the media
 * provided by our web or email marco.faccio@gmail.com
 */
package com.l2jserver.datapack.custom.enginemods.holders;

import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import com.l2jserver.datapack.custom.enginemods.mods.AbstractMods;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;


public class ModTimerHolder
{
	protected static final Logger LOG = Logger.getLogger(ModTimerHolder.class.getName());
	
	protected final Integer _mod;
	protected final String _name;
	protected final L2Npc _npc;
	protected final L2PcInstance _player;
	protected final boolean _isRepeating;
	
	private ScheduledFuture<?> _schedular;
	
	public ModTimerHolder(AbstractMods mod, String name, L2Npc npc, L2PcInstance player, long time, boolean repeating)
	{
		_mod = mod.hashCode();
		_name = name;
		_npc = npc;
		_player = player;
		_isRepeating = repeating;
		
		if (repeating)
		{
			_schedular = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new ScheduleTimerTask(), time, time);
		}
		else
		{
			_schedular = ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleTimerTask(), time);
		}
	}
	
	protected final class ScheduleTimerTask implements Runnable
	{
		@Override
		public void run()
		{
			if (!_isRepeating)
			{
				cancel();
			}
			
			//if (!EngineModsManager.getMod(_mod).isStarting())
			//{
			//	return;
			//}
			
			//EngineModsManager.getMod(_mod).onTimer(_name, _npc, _player);
		}
	}
	
	public final void cancel()
	{
		if (_schedular != null)
		{
			_schedular.cancel(false);
		}
		
		//EngineModsManager.getMod(_mod).removeTimer(this);
	}
	
	/**
	 * public method to compare if this timer matches with the key attributes passed.
	 * @param mod : Mod instance to which the timer is attached
	 * @param name : Name of the timer
	 * @param npc : Npc instance attached to the desired timer (null if no npc attached)
	 * @param player : Player instance attached to the desired timer (null if no player attached)
	 * @return boolean
	 */
	public final boolean equals(AbstractMods mod, String name, L2Npc npc, L2PcInstance player)
	{
		if (mod == null || mod.hashCode() != _mod)
		{
			return false;
		}
		
		if (name == null || !name.equals(_name))
		{
			return false;
		}
		
		return npc == _npc && player == _player;
	}
	
	public String getName()
	{
		return _name;
	}
}