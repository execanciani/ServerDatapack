package com.l2jserver.datapack.custom.enginemods.mods.zone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import com.l2jserver.commons.util.Rnd;
import com.l2jserver.datapack.custom.enginemods.enums.TeamType;
import com.l2jserver.datapack.custom.enginemods.holders.TeamHolder;
import com.l2jserver.datapack.custom.enginemods.holders.RewardHolder;
import com.l2jserver.datapack.custom.enginemods.mods.AbstractMods;
import com.l2jserver.datapack.custom.enginemods.util.Util;
import com.l2jserver.datapack.custom.enginemods.util.builders.html.Html;
import com.l2jserver.datapack.custom.enginemods.util.builders.html.HtmlBuilder;
import com.l2jserver.datapack.custom.enginemods.util.builders.html.HtmlBuilder.HtmlType;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.instancemanager.InstanceManager;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.ListenerRegisterType;
import com.l2jserver.gameserver.model.events.annotations.RegisterEvent;
import com.l2jserver.gameserver.model.events.annotations.RegisterType;
import com.l2jserver.gameserver.model.events.impl.character.OnCreatureAttack;
import com.l2jserver.gameserver.model.events.impl.character.OnCreatureKill;
import com.l2jserver.gameserver.model.events.impl.character.OnCreatureSkillUse;
import com.l2jserver.gameserver.model.events.impl.character.OnCreatureZoneExit;
import com.l2jserver.gameserver.model.events.impl.character.npc.OnNpcFirstTalk;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerNpcBypass;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerPKFLag;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerPvPFlag;
import com.l2jserver.gameserver.model.events.returns.TerminateReturn;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;

public class ZoneTvT extends AbstractMods
{
	private static final int GK_ID =  900108;
	
	//private Instance INSTANCE;
	private int _idInstancia = InstanceManager.getInstance().createDynamicInstance(null);;
	
	private Map<TeamType, TeamHolder> EQUIPOS = new HashMap<>();
	
	private final Map<String, RewardHolder> REWARDS = new HashMap<>();
	{
		REWARDS.put("KILLER", new RewardHolder(57, 100000));
		REWARDS.put("VICTIM", new RewardHolder(57, 10000));
		REWARDS.put("WINNER", new RewardHolder(57, 10000000));
		REWARDS.put("LOSER", new RewardHolder(57, 10000000));
	}
	
	private final List<Location> GK_LOCS = new ArrayList<>();
	{
		GK_LOCS.add(new Location(114719, -114795, -11207));
		GK_LOCS.add(new Location(113055, -114799, -10987));
		GK_LOCS.add(new Location(116370, -114800, -10988));
		GK_LOCS.add(new Location(114721, -117066, -11080));
		GK_LOCS.add(new Location(114741, -113152, -10988));
	}
	
	public ZoneTvT()
	{
		LOG.info("[ZoneTvT]: Mod cargado");
		
		//INSTANCE = InstanceManager.getInstance().getInstance(instanceid);
		
		iniciarEquipos();
		
		//spawneamos los gk
		for (Location gkloc : GK_LOCS)
		{
			addSpawn(GK_ID, gkloc.getX(), gkloc.getY(), gkloc.getZ(), 0, false, 0, false, _idInstancia);
		}
	}

	private void iniciarEquipos()
	{
		TeamType tipo = null;
		TeamHolder equipo = null;
		

		tipo = TeamType.AZUL;
		equipo = new TeamHolder();
		equipo.colocarColor(tipo);
		EQUIPOS.put(tipo, equipo);
		
		tipo = TeamType.ROJO;
		equipo = new TeamHolder();
		equipo.colocarColor(tipo);
		EQUIPOS.put(tipo, equipo);
	}

	@Override
	public void onModState()
	{
		
	}
	
	@RegisterEvent(EventType.ON_CREATURE_ZONE_EXIT)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void salidaDeZona(OnCreatureZoneExit evento)
	{
		L2PcInstance jugador = evento.getCreature() instanceof L2PcInstance ? (L2PcInstance)evento.getCreature() : null;
		
		if (jugador == null)
			return;
		
		if (jugador.getInstanceId() != _idInstancia)
			return;
		
		eliminarJugador(jugador);
	}
	
	private void eliminarJugador(L2PcInstance player)
	{
		for (TeamHolder team : EQUIPOS.values())
		{
			if (team.isTeamParticipant(player))
			{
				team.cleanPlayer(player);
			}
		}
	}

	
	@RegisterEvent(EventType.ON_PLAYER_NPC_BYPASS)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void NpcBypass(OnPlayerNpcBypass evento)
	{
		if (!Util.checkBypass(evento.getCommand(), "ZoneTvT"))
			return;
		
		L2PcInstance player = evento.getActiveChar();
		if (player == null)
			return;
		
		StringTokenizer st = new StringTokenizer(evento.getCommand(), " ");
		
		st.nextToken();
		st.nextToken();
		
		switch (st.nextToken())
		{
			case "teleport":
				
				String locName = st.nextToken();//getTokens(st);
				
				if (locName.equalsIgnoreCase("tozonetvt"))
				{
					teleportPlayer(player,false,true);
					break;
				}
				
				if (locName.equalsIgnoreCase("tospot"))
				{
					teleportToSpot(player);
					break;
				}
				
				if (locName.equalsIgnoreCase("tocity"))
				{
					eliminarJugador(player);
					//giran
					player.teleToLocation(82698, 148638, -3473, 0, 0);
					break;
				}
		}
	}
	
	private void teleportToSpot(L2PcInstance player)
	{
		Location loc = GK_LOCS.get(Rnd.get(0,GK_LOCS.size() - 1));
		player.teleToLocation(loc, _idInstancia, 100);
	}

	private void teleportPlayer(L2PcInstance player, boolean revive, boolean saveColorName)
	{
		if (revive)
		{
			player.doRevive();
			player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
			player.setCurrentCp(player.getMaxCp());
		}
		
		removePlayerTeam(player);
		addPlayerToTeam(player, saveColorName);
		
		Location loc = GK_LOCS.get(Rnd.get(0,GK_LOCS.size() - 1));
		player.teleToLocation(loc, _idInstancia, 100);
	}
	
	private void removePlayerTeam(L2PcInstance participant)
	{
		for (TeamHolder tholder : EQUIPOS.values())
		{
			if (tholder.isTeamParticipant(participant))
				tholder.eliminarJugador(participant);
		}
	}

	@RegisterEvent(EventType.ON_PLAYER_PVP_FLAG)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public TerminateReturn OnPlayerPvPFlag(OnPlayerPvPFlag event)
	{
		L2PcInstance activeChar = event.getActiveChar();
		
		if (activeChar == null)
		{
			return new TerminateReturn(true, true, true);
		}
		
		if (activeChar.getInstanceId() == _idInstancia)
			return new TerminateReturn(true, true, true);
		
		return null;
	}
	

	@RegisterEvent(EventType.ON_PLAYER_PK_FLAG)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public TerminateReturn OnPlayerPKFLag(OnPlayerPKFLag event)
	{
		L2PcInstance activeChar = event.getActiveChar();
		
		if (activeChar == null)
		{
			return new TerminateReturn(true, true, true);
		}
		
		if (activeChar.getInstanceId() == _idInstancia)
		{
			return new TerminateReturn(true, true, true);
		}
		
		return null;
	}
	
	@RegisterEvent(EventType.ON_CREATURE_SKILL_USE)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public TerminateReturn OnCreatureSkillUse(OnCreatureSkillUse event)
	{
		L2PcInstance caster = event.getCaster() instanceof L2PcInstance ? (L2PcInstance)event.getCaster() : null;
		L2PcInstance target = event.getTarget() instanceof L2PcInstance ? (L2PcInstance)event.getTarget() : null;
		
		if (caster == null || target == null)
			return null;
		
		if (caster.getInstanceId() != _idInstancia || target.getInstanceId() != _idInstancia)
		{
			return null;
		}
		
		TeamHolder casterTeam = getTeam(caster);
		TeamHolder targetTeam = getTeam(target);

		if (casterTeam == null || targetTeam == null)
			return null;
		
		if (casterTeam.isTeamParticipant(target) && caster != target)
			return new TerminateReturn(true, true, true);
		
		return null;
	}
	
	@RegisterEvent(EventType.ON_CREATURE_ATTACK)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public TerminateReturn onPlayerAttack(OnCreatureAttack event)
	{
		L2PcInstance attacker = event.getAttacker() instanceof L2PcInstance ? (L2PcInstance)event.getAttacker() : null;
		L2PcInstance target = event.getTarget() instanceof L2PcInstance ? (L2PcInstance)event.getTarget() : null;
		
		if (attacker == null || target == null)
		{
			return null;
		}
		
		if (attacker.getInstanceId() != _idInstancia || target.getInstanceId() != _idInstancia)
		{
			return null;
		}
		
		TeamHolder attackerTeam = getTeam(attacker);
		TeamHolder targetTeam = getTeam(target);
		
		if (attackerTeam == null || targetTeam == null)
			return null;
		
		if (attackerTeam.isTeamParticipant(target))
			return new TerminateReturn(true, true, true);
		
		return null;
	}
	
	@RegisterEvent(EventType.ON_CREATURE_KILL)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public TerminateReturn onplayerkill(OnCreatureKill event)
	{		
		//PlayerHolder asesino = PlayerData.get(evento.getActiveChar());
		//PlayerHolder victima = PlayerData.get(evento.getTarget());
		
		L2PcInstance killer = event.getAttacker() instanceof L2PcInstance ? (L2PcInstance)event.getAttacker() : null;
		L2PcInstance victim = event.getTarget() instanceof L2PcInstance ? (L2PcInstance)event.getTarget() : null;
		
		if (killer == null || victim == null)
		{
			return new TerminateReturn(true, true, true);
		}
		
		if (killer.getInstanceId() != _idInstancia || victim.getInstanceId() != _idInstancia)
		{
			return null;
		}
		
		TeamHolder killerTeam = getTeam(killer);
		TeamHolder victimTeam = getTeam(victim);
		
		if (killerTeam == null || victimTeam == null)
			return new TerminateReturn(true, true, true);
		
		killerTeam.sumarPuntoPorKill();
		
		//mostrar mensaje
		if (killerTeam.tomarPuntos() == 25)
		{
			sendMsjToTeams("Team winner is " + killerTeam.tomarColor().name(), 10 * 1000);
			//condicion
			killerTeam.setCondition("WINNER");
			victimTeam.setCondition("LOSER");
		}
		else if (killerTeam.tomarPuntos() < 25)
		{
			sendMsjToTeams("BLUE: "+ EQUIPOS.get(TeamType.AZUL).tomarPuntos() + " || RED: "+ EQUIPOS.get(TeamType.ROJO).tomarPuntos(), 10 * 1000);
			//condicion
			killerTeam.setCondition("KILLER");
			victimTeam.setCondition("VICTIM");
		}
		
		//damos reward
		giveReward(killerTeam);
		giveReward(victimTeam);
		
		eliminarJugador(victim);
		sendMsjToPlayer(victim, "You are revive in 10 seconds", 5 * 1000);
		
		//spawnear la victima en 10 segundo
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
			@Override
			public void run()
			{
				if (victim.getInstanceId() != _idInstancia)
					return;
				if (!victim.isDead())
					return;
				
				teleportPlayer(victim, true, false);
			}
		}, 10 * 1000);
		
		return null;
	}
	
	private TeamHolder getTeam(L2PcInstance participant)
	{
		for (TeamHolder tholder : EQUIPOS.values())
		{
			if (tholder.isTeamParticipant(participant))
				return tholder;
		}
		return null;
	}

	@RegisterEvent(EventType.ON_NPC_FIRST_TALK)
	@RegisterType(ListenerRegisterType.GLOBAL_NPCS)
	public void onNpcFirstTalk(OnNpcFirstTalk event)
	{		
		L2PcInstance player = event.getActiveChar();
		L2Npc npc = event.getNpc();
		
		if (player == null || npc == null)
			return;
		if (npc.getId() != GK_ID)
			return;
		
		HtmlBuilder hb = new HtmlBuilder(HtmlType.HTML_TYPE);
		hb.append(Html.HTML_START);
		hb.append(Html.headHtml("TVT GK"));
		hb.append("<br>");
		
		hb.append("<center>");
		hb.append("<Quieres volver a la City?");
		
		hb.append("<br>");
		hb.append("<table width=280>");
		hb.append("<tr>");
		hb.append("<td align=center>", Html.newImage("L2UI.bbs_folder", 32, 32), "</td>");
		hb.append("<td align=center><button action=\"bypass -h Engine ZoneTvT teleport tocity", "\" value=\"", "To City", "\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		hb.append("<td align=center>", Html.newImage("L2UI.bbs_folder", 32, 32), "</td>");
		hb.append("</tr>");
		hb.append("</table>");
		
		//other spot
		hb.append("<br>");
		hb.append("<table width=280>");
		hb.append("<tr>");
		hb.append("<td align=center>", Html.newImage("L2UI.bbs_folder", 32, 32), "</td>");
		hb.append("<td align=center><button action=\"bypass -h Engine ZoneTvT teleport tospot", "\" value=\"", "Other Spot", "\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		hb.append("<td align=center>", Html.newImage("L2UI.bbs_folder", 32, 32), "</td>");
		hb.append("</tr>");
		hb.append("</table>");
		
		hb.append(Html.HTML_END);
		
		sendHtml(player, npc, hb);
	}
	
	private void giveReward(TeamHolder team)
	{
		for (L2PcInstance player : team.getParticipants())
		{
			for (String condition : REWARDS.keySet())
			{
				if (!condition.equals("") && condition.equalsIgnoreCase(team.getCondition()))
				{
					RewardHolder reward = REWARDS.get(condition);
					player.addItem("zonetvtreward", reward.getRewardId(), reward.getRewardCount(), player, true);
				}
			}
		}
	}

	private void sendMsjToTeams(String text, int time)
	{
		ExShowScreenMessage exShowScreenMessage = new ExShowScreenMessage(text, time);
		
		for (TeamHolder equipo : EQUIPOS.values())
		{
			for (L2PcInstance player : equipo.getParticipants())
			{
				if (exShowScreenMessage != null)
					player.sendPacket(exShowScreenMessage);
			}
		}
	}
	
	private void sendMsjToPlayer(L2PcInstance player, String text, int time)
	{
		player.sendPacket(new ExShowScreenMessage(text, time));
	}

	private void addPlayerToTeam(L2PcInstance jugador, boolean saveColorName)
	{
		TeamHolder equipoAzul = EQUIPOS.get(TeamType.AZUL);
		TeamHolder equipoRojo = EQUIPOS.get(TeamType.ROJO);
		
		if (equipoAzul.tomarCantidadDeJugadores() < equipoRojo.tomarCantidadDeJugadores())
		{
			equipoAzul.prepararJugador(jugador, saveColorName);
		}
		else if (equipoAzul.tomarCantidadDeJugadores() > equipoRojo.tomarCantidadDeJugadores())
		{
			equipoRojo.prepararJugador(jugador, saveColorName);
		}
		else
		{
			int random = Rnd.get(0,1);
			((TeamHolder) EQUIPOS.values().toArray()[random]).prepararJugador(jugador, saveColorName);
		}
	}

	public static void main(String[] args)
	{
		new ZoneTvT();
	}
}
