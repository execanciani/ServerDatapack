/*
// * L2J_EngineMods
// * Engine developed by Fissban.
// *
// * This software is not free and you do not have permission
// * to distribute without the permission of its owner.
// *
// * This software is distributed only under the rule
// * of www.devsadmins.com.
// * 
// * Contact us with any questions by the media
// * provided by our web or email marco.faccio@gmail.com
// */
package com.l2jserver.datapack.custom.enginemods.mods;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import com.l2jserver.datapack.ai.npc.AbstractNpcAI;
import com.l2jserver.datapack.custom.enginemods.data.ModsData;
import com.l2jserver.datapack.custom.enginemods.enums.EngineStateType;
import com.l2jserver.datapack.custom.enginemods.enums.WeekDayType;
import com.l2jserver.datapack.custom.enginemods.holders.ModTimerHolder;
import com.l2jserver.datapack.custom.enginemods.util.builders.html.HtmlBuilder;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.ShowBoard;
import com.l2jserver.gameserver.util.Broadcast;


///**
// * @author fissban
// */
public abstract class AbstractMods extends AbstractNpcAI
{
	public static final Logger LOG = Logger.getLogger(AbstractMods.class.getName());
	
	// Event state
	protected EngineStateType _state = EngineStateType.END;
	// timers
	private final Map<Integer, List<ModTimerHolder>> _eventTimers = new ConcurrentHashMap<>();
	
	protected static List<Future<?>> _sheduledStateMod = new ArrayList<>();
	
	/**
	 * Constructor
	 */
	public AbstractMods()
	{
		super(AbstractMods.class.getSimpleName(), "custom/enginemods");
	}
	
	/**
	 * Cancelamos los threads de inicio y fin de eventos por si no son destruidos por VM
	 */
	public void cancelScheduledState()
	{
		for (Future<?> run : _sheduledStateMod)
		{
			run.cancel(true);
		}
	}
	
	/**
	 * Estado del evento<br>
	 * <li>EngineStateType.START</li><br>
	 * <li>EngineStateType.END</li>
	 * @return
	 */
	public EngineStateType getState()
	{
		return _state;
	}
	
//	/**
//	 * Here the specific actions that will have an event during actions will be defined:<br>
//	 * <li>EngineStateType.START</li><br>
//	 * <li>EngineStateType.END</li>
//	 */
	public abstract void onModState();
	
	// XXX DB DATA ----------------------------------------------------------------------------------------------- //
	
	/**
	 * Obtenemos el valor de un player en determinado evento
	 * @param objectId
	 * @param event
	 * @return
	 */
	public String getValueDB(int objectId, String event)
	{
		return ModsData.get(objectId, event, this);
	}
	
	/**
	 * Obtenemos el valor de un player en determinado evento
	 * @param player
	 * @param event
	 * @return
	 */
	public String getValueDB(L2PcInstance player, String event)
	{
		return ModsData.get(player.getObjectId(), event, this);
	}
	
	/**
	 * Se almacena informacion sobre un evento y su valor para un determinado player<br>
	 * @param player
	 * @param event
	 * @param value
	 */
	public void setValueDB(L2PcInstance player, String event, String value)
	{
		ModsData.set(player.getObjectId(), event, value, this);
	}
	
	/**
	 * Se almacena informacion sobre un evento y su valor para un determinado player
	 * @param objectId
	 * @param event
	 * @param value
	 */
	public void setValueDB(int objectId, String event, String value)
	{
		ModsData.set(objectId, event, value, this);
	}
	
	/**
	 * Removemos todos los eventos y valores de un un mod.
	 */
	public void clearValueDB()
	{
		ModsData.remove(this);
	}
	
	/**
	 * Removemos todos los eventos y valores de un player.
	 */
	public void clearPlayerValueDB(int objectId)
	{
		ModsData.remove(objectId);
	}
	
	/**
	 * Se remueve un valor determinado de un personaje.
	 * @param objectId
	 * @param event
	 */
	public void removeValueDB(int objectId, String event)
	{
		ModsData.remove(objectId, event, this);
	}
	
	// XXX TIMERS ------------------------------------------------------------------------------------------------ //
	
	/**
	 * Add a timer to the mod, if it doesn't exist already. If the timer is repeatable, it will auto-fire automatically, at a fixed rate, until explicitly canceled.
	 * @param name name of the timer (also passed back as "event" in onTimer)
	 * @param time time in ms for when to fire the timer
	 * @param npc npc associated with this timer (can be null)
	 * @param player player associated with this timer (can be null)
	 * @param repeating indicates if the timer is repeatable or one-time.
	 */
	public void startTimer(String name, long time, L2Npc npc, L2PcInstance player, boolean repeating)
	{
		// Get mod timers for this timer type.
		List<ModTimerHolder> timers = _eventTimers.get(name.hashCode());
		if (timers == null)
		{
			// None timer exists, create new list.
			timers = new CopyOnWriteArrayList<>();
			// Add new timer to the list.
			timers.add(new ModTimerHolder(this, name, npc, player, time, repeating));
			// Add timer list to the map.
			_eventTimers.put(name.hashCode(), timers);
		}
		else
		{
			// Check, if specific timer already exists.
			for (ModTimerHolder timer : timers)
			{
				// If so, return.
				if (timer != null && timer.equals(this, name, npc, player))
				{
					return;
				}
			}
			// Add new timer to the list.
			timers.add(new ModTimerHolder(this, name, npc, player, time, repeating));
		}
	}
	
	public ModTimerHolder getTimer(String name)
	{
		return getTimer(name, null, null);
	}
	
	public ModTimerHolder getTimer(String name, L2PcInstance player)
	{
		return getTimer(name, null, player);
	}
	
	public ModTimerHolder getTimer(String name, L2Npc npc, L2PcInstance player)
	{
		// Get mod timers for this timer type.
		List<ModTimerHolder> timers = _eventTimers.get(name.hashCode());
		
		// Timer list does not exists or is empty, return.
		if (timers == null || timers.isEmpty())
		{
			return null;
		}
		
		// Check, if specific timer exists.
		for (ModTimerHolder timer : timers)
		{
			// If so, return him.
			if (timer != null && timer.equals(this, name, npc, player))
			{
				return timer;
			}
		}
		
		return null;
	}
	
	public void cancelTimer(String name, L2Npc npc, L2PcInstance player)
	{
		// If specified timer exists, cancel him.
		ModTimerHolder timer = getTimer(name, npc, player);
		if (timer != null)
		{
			timer.cancel();
		}
	}
	
	public void cancelTimers(String name)
	{
		// Get mod timers for this timer type.
		List<ModTimerHolder> timers = _eventTimers.get(name.hashCode());
		
		// Timer list does not exists or is empty, return.
		if (timers == null || timers.isEmpty())
		{
			return;
		}
		
		// Cancel all mod timers.
		for (ModTimerHolder timer : timers)
		{
			if (timer != null)
			{
				timer.cancel();
			}
		}
	}
	
	/**
	 * Removes modTimer from timer list, when it terminates.
	 * @param timer : modTimer, which is being terminated.
	 */
	public void removeTimer(ModTimerHolder timer)
	{
		// Timer does not exist, return.
		if (timer == null)
		{
			return;
		}
		
		// Get mod timers for this timer type.
		List<ModTimerHolder> timers = _eventTimers.get(timer.getName().hashCode());
		
		// Timer list does not exists or is empty, return.
		if (timers == null || timers.isEmpty())
		{
			return;
		}
		
		// Remove timer from the list.
		timers.remove(timer);
	}
	
	// XXX MISC -------------------------------------------------------------------------------------------------- //
	
	/**
	 * Spawn Npc
	 * @param npcId
	 * @param x
	 * @param y
	 * @param z
	 * @param headin
	 * @param randomOffset
	 * @param despawnDelay
	 * @return instance of the newly spawned npc with summon animation.
	 */
	public L2Npc addSpawn(int npcId, Location loc, boolean randomOffset, long despawnDelay)
	{
		return addSpawn(npcId, loc.getX(), loc.getY(), loc.getZ(), 0, randomOffset, despawnDelay);
	}
	
//	/**
//	 * @param npcId
//	 * @param x
//	 * @param y
//	 * @param z
//	 * @param heading
//	 * @param randomOffset
//	 * @param despawnDelay
//	 * @return instance of the newly spawned npc with summon animation.
//	 */
//	public L2Npc addSpawn(int npcId, int x, int y, int z, int heading, boolean randomOffset, long despawnDelay)
//	{
//		L2Npc npc = null;
//		try
//		{
//			L2NpcTemplate template = NpcData.getInstance().getTemplate(npcId);
//			if (template != null)
//			{
//				if (x == 0 && y == 0)
//				{
//					LOG.log(Level.SEVERE, "Failed to adjust bad locks for mod spawn!  Spawn aborted!");
//					return null;
//				}
//				
//				if (randomOffset)
//				{
//					x += Rnd.get(-100, 100);
//					y += Rnd.get(-100, 100);
//				}
//				
//				final L2Spawn spawn = new L2Spawn(template);
//				spawn.setLocation(new Location(x, y, z + 20, heading));
//				//spawn.setRespawnState(false);
//				
//				npc = spawn.doSpawn(true);// isSummonSpawn
//				if (despawnDelay > 0)
//				{
//					npc.scheduleDespawn(despawnDelay);
//				}
//			}
//		}
//		catch (Exception e1)
//		{
//			LOG.warning("Could not spawn Npc " + npcId);
//		}
//		
//		return npc;
//	}
	
	/**
	 * @return Event is starting
	 */
	public boolean isStarting()
	{
		if (_state == EngineStateType.START)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * @param player
	 * @param npc
	 * @param content -> setHtml
	 */
	public static final void sendHtml(L2PcInstance player, L2Npc npc, HtmlBuilder content)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(npc == null ? 0 : npc.getObjectId());
		html.setHtml(content.toString());
		player.sendPacket(html);
	}
	
	/**
	 * Se envia a un personaje un html
	 * @param player
	 * @param npc
	 * @param content -> setHtml
	 */
	public static final void sendHtml(L2PcInstance player, L2Npc npc, String content)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(npc == null ? 0 : npc.getObjectId());
		html.setHtml(content.toString());
		player.sendPacket(html);
	}
	
	/**
	 * Se envia a un personaje un html
	 * @param player
	 * @param npc
	 * @param htmlFile -> setFile
	 */
	public static final void sendHtmlFile(L2PcInstance player, L2Npc npc, String htmlFile)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(npc == null ? 0 : npc.getObjectId());
		//html.setFile(htmlFile.toString());
		player.sendPacket(html);
	}
	
	/**
	 * Se envia a un personaje un Community
	 * @param player
	 * @param html
	 */
	public static void sendCommunity(L2PcInstance player, String html)
	{
		if (html == null || player == null)
		{
			return;
		}
		
		if (html.length() < 4090)
		{
			player.sendPacket(new ShowBoard(html, "101"));
			//player.sendPacket(ShowBoard.STATIC_SHOWBOARD_102);
			//player.sendPacket(ShowBoard.STATIC_SHOWBOARD_103);
		}
		else if (html.length() < 8180)
		{
			player.sendPacket(new ShowBoard(html.substring(0, 4090), "101"));
			player.sendPacket(new ShowBoard(html.substring(4090, html.length()), "102"));
			//player.sendPacket(ShowBoard.STATIC_SHOWBOARD_103);
		}
		else if (html.length() < 12270)
		{
			player.sendPacket(new ShowBoard(html.substring(0, 4090), "101"));
			player.sendPacket(new ShowBoard(html.substring(4090, 8180), "102"));
			player.sendPacket(new ShowBoard(html.substring(8180, html.length()), "103"));
		}
		else
		{
			System.out.println("community html muy largo-> " + (html.length() - 12270));
		}
	}
	
	// REGISTER START & END - EVENT -------------------------------------------------------------------------- //
	
	/**
	 * Register mod<br>
	 */
	public void registerMod(boolean config)
	{
		//EngineModsManager.registerMod(this);
		if (config)
		{
			startMod();
		}
	}
	
	/**
	 * The mod is registered to run only on certain days of the week.<br>
	 * one thread start and one thread end will be created for each day us to add to the list.
	 * @param day <br>
	 *            <li>SUNDAY</li>
	 *            <li>MONDAY</li>
	 *            <li>TUESDAY</li>
	 *            <li>WEDNESDAY</li>
	 *            <li>THURSDAY</li>
	 *            <li>FRIDAY</li>
	 *            <li>SATURDAY</li>
	 */
	public void registerMod(boolean config, List<WeekDayType> day)
	{
		//EngineModsManager.registerMod(this);
		if (config)
		{
			for (WeekDayType d : day)
			{
				registerMod(d);
			}
		}
	}
	
	/**
	 * The mod is registered to run only on certain days of the week.<br>
	 * one thread start and one thread end will be created for each day us to add to the list.
	 * @param day <br>
	 *            <li>SUNDAY</li>
	 *            <li>MONDAY</li>
	 *            <li>TUESDAY</li>
	 *            <li>WEDNESDAY</li>
	 *            <li>THURSDAY</li>
	 *            <li>FRIDAY</li>
	 *            <li>SATURDAY</li>
	 */
	private void registerMod(WeekDayType day)
	{
		int weekToStartEvent = 1;
		
		while (weekToStartEvent >= 0)
		{
			// variable que decide la cantidad de dias para iniciar el evento
			int eventTime = -1;
			// controla la cant de dias faltantes para iniciar el evento
			int missingDayToStart = 0;
			// simple auxiliar para saber que dia de la semana es.
			Calendar time = new GregorianCalendar();
			// obtenemos el valor del dia de la semana
			int i = time.get(Calendar.DAY_OF_WEEK);
			// buscamos cuantos dias faltan para llegar a ese dia
			while (eventTime < 0)
			{
				if (WeekDayType.values()[i - 1] == day)
				{
					eventTime = missingDayToStart;
				}
				else
				{
					i++;
					missingDayToStart++;
					if (i > WeekDayType.values().length)
					{
						i = 1;
					}
				}
			}
			
			eventTime += weekToStartEvent * 7;
			// one thread where we indicate when to start the event and the actions to take will be created.
			time.add(Calendar.DAY_OF_YEAR, eventTime);
			long timeStart = time.getTimeInMillis() - System.currentTimeMillis();
			
			_sheduledStateMod.add(ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleStart(), timeStart < 0 ? 0 : timeStart));
			// one thread where we indicate when to end the event and the actions to take will be created.
			time.add(Calendar.DAY_OF_YEAR, eventTime + 1);
			_sheduledStateMod.add(ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleEnd(), time.getTimeInMillis() - System.currentTimeMillis()));
			
			weekToStartEvent--;
		}
	}
	
	/**
	 * @param start : date en DD-MM-AAAA
	 * @param end : date en DD-MM-AAAA
	 */
	public void registerMod(String start, String end)
	{
		//EngineModsManager.registerMod(this);
		// the day you get the month and year of start and end of the event.
		try
		{
			StringTokenizer parse = null;
			// Date start -> parse
			parse = new StringTokenizer(start, "-");
			int diaStart = Integer.parseInt(parse.nextToken());
			int mesStart = Integer.parseInt(parse.nextToken());
			int anioStart = Integer.parseInt(parse.nextToken());
			// Date end -> parse
			parse = new StringTokenizer(end, "-");
			int diaEnd = Integer.parseInt(parse.nextToken());
			int mesEnd = Integer.parseInt(parse.nextToken());
			int anioEnd = Integer.parseInt(parse.nextToken());
			
			// Create calendar
			Calendar timeStart = new GregorianCalendar();
			timeStart.set(anioStart, mesStart, diaStart, 0, 0, 0);
			
			Calendar timeEnd = new GregorianCalendar();
			timeEnd.set(anioEnd, mesEnd, diaEnd, 0, 0, 0);
			
			long hoy = System.currentTimeMillis();
			
			LOG.warning("Event " + getClass().getSimpleName() + ": Start! -> " + timeStart.getTime().toString());
			LOG.warning("Event " + getClass().getSimpleName() + ": End! -> " + timeEnd.getTime().toString());
			
			// If the end date of the event is less than today's event should not run.
			if (timeEnd.getTimeInMillis() < hoy)
			{
				return;
			}
			
			// If the end date of the event is less than the start must be corrected.
			if (timeStart.getTimeInMillis() >= timeEnd.getTimeInMillis())
			{
				LOG.warning("Event " + getClass().getSimpleName() + ": The start date of the event can not be greater than or equal to the end of the event");
				return;
			}
			
			// one thread where we indicate when to start the event and the actions to take is created.
			long time = 0;
			if (timeStart.getTimeInMillis() - hoy > 0)
			{
				time = timeStart.getTimeInMillis() - hoy;
			}
			_sheduledStateMod.add(ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleStart(), time));
			// one thread where we indicate when to end the event and the actions to take is created.
			_sheduledStateMod.add(ThreadPoolManager.getInstance().scheduleGeneral(new ScheduleEnd(), timeEnd.getTimeInMillis() - hoy));
		}
		catch (Exception e)
		{
			LOG.warning("Event " + getClass().getSimpleName() + ": The date of the event register is invalid");
			return;
		}
	}
	
	public void endMod()
	{
		// It fits the event status
		_state = EngineStateType.END;
		// Announce to all players
		Broadcast.toAllOnlinePlayers("Event " + getClass().getSimpleName() + ": End!", true);
		LOG.info("Event " + getClass().getSimpleName() + ": End!");
		
		onModState();
	}
	
	private void startMod()
	{
		// It fits the event status
		_state = EngineStateType.START;
		// Announce to all players
		Broadcast.toAllOnlinePlayers("Event " + getClass().getSimpleName() + ": Start!", true);
		LOG.info("Event " + getClass().getSimpleName() + ": Start!");
		
		onModState();
	}
	
	// XXX TASK's ---------------------------------------------------------------------------------------------------
	
	/**
	 * Start Event
	 */
	protected class ScheduleStart implements Runnable
	{
		@Override
		public void run()
		{
			startMod();
		}
	}
	
	/**
	 * End Event
	 */
	protected class ScheduleEnd implements Runnable
	{
		@Override
		public void run()
		{
			endMod();
		}
	}
	
//	// XXX LISTENERS --------------------------------------------------------------------------------------------- //
//	
//	public boolean onCommunityBoard(L2PcInstance player, String command)
//	{
//		return false;
//	}
//	
//	public void onShutDown()
//	{
//		//
//	}
//	
//	/**
//	 * @param player
//	 * @return
//	 */
//	public boolean onExitWorld(L2PcInstance player)
//	{
//		return false;
//	}
//	
//	/**
//	 * @param killer
//	 * @param npc
//	 * @return
//	 */
//	public void onNpcExpSp(L2PcInstance killer, Attackable npc, NpcExpInstance instance)
//	{
//		//
//	}
//	
//	/**
//	 * @param killer
//	 * @param npc
//	 * @return
//	 */
//	public void onNpcDrop(L2PcInstance killer, Attackable npc, NpcDropsInstance instance)
//	{
//		//
//	}
//	
//	public void onEnterZone(Creature player, ZoneForm zone)
//	{
//		//
//	}
//	
//	public void onExitZone(Creature player, ZoneForm zone)
//	{
//		//
//	}
//	
//	public void onCreateCharacter(L2PcInstance player)
//	{
//		//
//	}
//	
//	public boolean onChat(L2PcInstance player, String chat)
//	{
//		return false;
//	}
//	
//	public boolean onAdminCommand(L2PcInstance player, String chat)
//	{
//		return false;
//	}
//	
//	public boolean onVoicedCommand(L2PcInstance player, String chat)
//	{
//		return false;
//	}
//	
//	public boolean onInteract(L2PcInstance player, Creature character)
//	{
//		return false;
//	}
//	
//	public void onEvent(L2PcInstance player, Creature npc, String command)
//	{
//		//
//	}
//	
	public void onTimer(String timerName, L2Npc npc, L2PcInstance player)
	{
		//
	}
//	
//	public String onSeeNpcTitle(int objectId)
//	{
//		return null;
//	}
//	
//	public void onSpawn(Npc obj)
//	{
//		//
//	}
//	
	public void onPlayerLogin(L2PcInstance player)
	{
		//
	}
//	
//	public void onKill(Creature killer, Creature victim, boolean isPet)
//	{
//		//
//	}
//	
//	public void onDeath(Creature player)
//	{
//		//
//	}
//	
//	public void onEnchant(Creature player)
//	{
//		//
//	}
//	
//	public void onEquip(Creature player)
//	{
//		//
//	}
//	
//	public void onUnequip(Creature player)
//	{
//		//
//	}
//	
//	public boolean onRestoreSkills(Player player)
//	{
//		return false;
//	}
//	
//	/**
//	 * This method multiplies any stat of the characters, so we return "1.0" if we want to realize any increase.<br>
//	 * Example: 1.1 -> 10% more stat
//	 * @param stat
//	 * @param character
//	 * @return
//	 */
//	public double onStats(Stats stat, Creature character, double value)
//	{
//		return value;
//	}
//	
//	public boolean onShortCutReg(int type, int id, int slot, int page, int characterType, Player player)
//	{
//		return false;
//	}
//	
//	public boolean onMagicSkillUse(Player player, Creature finalTarget, L2Skill skill, boolean _ctrlPressed, boolean _shiftPressed)
//	{
//		return false;
//	}
}
