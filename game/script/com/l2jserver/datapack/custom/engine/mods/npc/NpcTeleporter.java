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
package com.l2jserver.datapack.custom.enginemods.mods.npc;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.l2jserver.datapack.custom.enginemods.mods.AbstractMods;
import com.l2jserver.datapack.custom.enginemods.util.Util;
import com.l2jserver.datapack.custom.enginemods.util.builders.html.Html;
import com.l2jserver.datapack.custom.enginemods.util.builders.html.HtmlBuilder;
import com.l2jserver.datapack.custom.enginemods.util.builders.html.HtmlBuilder.HtmlType;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2NpcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.ListenerRegisterType;
import com.l2jserver.gameserver.model.events.annotations.Id;
import com.l2jserver.gameserver.model.events.annotations.RegisterEvent;
import com.l2jserver.gameserver.model.events.annotations.RegisterType;
import com.l2jserver.gameserver.model.events.impl.character.npc.OnNpcFirstTalk;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerNpcBypass;


/**
 * @author fissban
 */
public class NpcTeleporter extends AbstractMods
{
	//private static final int NPC = ConfigData.TELEPORTER_NPC_ID;
	private static final int NPC = 900105;
	
	private static final Map<String, Map<String, Location>> TELEPORTS = new HashMap<>();
	{
		TELEPORTS.put("Town Center", TOWN_CENTER_TELEPORTS);
		TELEPORTS.put("Village Center", VILLAGE_CENTER_TELEPORTS);
	}
	
	private static final Map<String, Location> TOWN_CENTER_TELEPORTS = new HashMap<>();
	{
		// 30 missing
		//TELEPORTS.put("40", new Location(121980, -118800, -2574));
		TOWN_CENTER_TELEPORTS.put("Giran", new Location(82698, 148638, -3473));
		TOWN_CENTER_TELEPORTS.put("Aden", new Location(147450, 27064, -2208));
		TOWN_CENTER_TELEPORTS.put("Dion", new Location(18748, 145437, -3132));
		TOWN_CENTER_TELEPORTS.put("Gludio", new Location(-14225, 123540, -3121));
		// 50 missing
		//TELEPORTS.put("60-TOP", new Location(174528, 52683, -4369));
		//TELEPORTS.put("60-UNDER", new Location(170327, 53985, -4583));
		//TELEPORTS.put("70", new Location(188191, -74959, -2738));
		// ......missing
	}
	
	private static final Map<String, Location> VILLAGE_CENTER_TELEPORTS = new HashMap<>();
	{
		VILLAGE_CENTER_TELEPORTS.put("Human", new Location(-82687, 243157, -3734));
		VILLAGE_CENTER_TELEPORTS.put("Orc", new Location(-44133, -113911, -244));
		VILLAGE_CENTER_TELEPORTS.put("Elven", new Location(45873, 49288, -3064));
	}
	
	public NpcTeleporter()
	{
		LOG.info("[NpcTeleporter]: Mod Loaded");
		
		//registerMod(ConfigData.ENABLE_NpcTeleporter);
		//spawnGuards();	
	}
	
	@Override
	public void onModState()
	{
		// TODO Auto-generated method stub
	}
	
	/**
	 * Se spawnean guardias en las zonas de teleports
	 */
	/*private void spawnGuards()
	{
		ThreadPool.schedule(() ->
		{
			for (Location loc : TELEPORTS.values())
			{
				addSpawn(60010, loc, true, 0);
				addSpawn(60010, loc, true, 0);
			}
			
		}, 20 * 1000); // 20 seg es hardcode
	}*/
	
	@RegisterEvent(EventType.ON_NPC_FIRST_TALK)
	@RegisterType(ListenerRegisterType.NPC)
	@Id(NPC)
	public void teleportFistTalk(OnNpcFirstTalk event)
	{	
		L2PcInstance player = event.getActiveChar();
		L2NpcInstance npc = (L2NpcInstance) event.getNpc();
		
		if (player == null)
			return;
		
		HtmlBuilder hb = new HtmlBuilder(HtmlType.HTML_TYPE);
		hb.append(Html.HTML_START);
		hb.append(Html.headHtml("TELEPORT MASTER"));
		hb.append("<br>");
		
		//hb.append("<center>");
		//hb.append("<table width=256><tr>");
		//hb.append("<td align=center><button action=\"bypass -h Engine NpcTeleporter TownAreas\" value=\"Town Areas\" width=128 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");

		hb.append("<table width=280>");
		hb.append("<tr>");
		hb.append("<td align=center>", Html.newImage("L2UI.bbs_folder", 32, 32), "</td>");
		hb.append("<td align=center><button action=\"bypass -h Engine NpcTeleporter TownAreas\" value=\"Town Areas\" width=128 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		hb.append("<td align=center>", Html.newImage("L2UI.bbs_folder", 32, 32), "</td>");
		hb.append("</tr>");
		hb.append("</table>");
		
		
		//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/WorldAreas.htm\" value=\"World Areas\" width=128 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("</tr>");
//		hb.append("<tr>");
//		hb.append("<td align=center><button action=\"bypass -h Engine ZoneTvT teleport tozonetvt", "\" value=\"", "TvT", "\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/fort.htm\" value=\"Fortress\" width=128 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("</tr>");
//		hb.append("<tr>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/sevensigns.htm\" value=\"Seven Signs\" width=128 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/instance.htm\" value=\"Instances\" width=128 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("</tr>");
//		hb.append("<tr>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/raid.htm\" value=\"Raidboss\" width=128 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/OlympiadStadiums.htm\" value=\"Olympiad Stadiums\" width=128 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("</tr>");
//		hb.append("<tr>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/clanhall/clanhall.htm\" value=\"Conquerable Halls\" width=128 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/OtherLocations.htm\" value=\"Other Locations\" width=128 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("</tr></table><br>");
//		hb.append("</center>");
		
		hb.append("<table width=280>");
		hb.append("<tr>");
		hb.append("<td align=center>", Html.newImage("L2UI.bbs_folder", 32, 32), "</td>");
		hb.append("<td align=center><button action=\"bypass -h Engine ZoneTvT teleport tozonetvt", "\" value=\"", "TvT", "\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		hb.append("<td align=center>", Html.newImage("L2UI.bbs_folder", 32, 32), "</td>");
		hb.append("</tr>");
		hb.append("</table>");
		
		
//		hb.append("Puedo mostrarte las zonas donde");	
//		hb.append("los hombres se convierten en <font color=LEVEL>dioses!</font>");
//		
//		for (String tele : TELEPORTS.keySet())
//		{
//			hb.append("<table width=280>");
//			hb.append("<tr>");
//			hb.append("<td align=center>", Html.newImage("L2UI.bbs_folder", 32, 32), "</td>");
//			hb.append("<td><button value=\"", tele, "\" action=\"bypass -h Engine NpcTeleporter teleport ", tele, "\" width=216 height=32 back=L2UI_CH3.refinegrade3_21 fore=L2UI_CH3.refinegrade3_21></td>");
//			hb.append("<td align=center>", Html.newImage("L2UI.bbs_folder", 32, 32), "</td>");
//			hb.append("</tr>");
//			hb.append("</table>");
//		}
//		
//		hb.append("<table width=280>");
//		hb.append("<tr>");
//		hb.append("<td align=center>", Html.newImage("L2UI.bbs_folder", 32, 32), "</td>");
//		hb.append("<td><button value=\"Zone TvT", "\" action=\"bypass -h Engine ZoneTeamPvP teleport zonetvt", "\" width=216 height=32 back=L2UI_CH3.refinegrade3_21 fore=L2UI_CH3.refinegrade3_21></td>");
//		hb.append("<td align=center>", Html.newImage("L2UI.bbs_folder", 32, 32), "</td>");
//		hb.append("</tr>");
//		hb.append("</table>");
		
		hb.append(Html.HTML_END);
		
		sendHtml(player, npc, hb);
	}
	
	private void showTownAreasLocs(L2PcInstance player, L2Npc npc)
	{
		HtmlBuilder hb = new HtmlBuilder(HtmlType.HTML_TYPE);
		hb.append(Html.HTML_START);
		hb.append(Html.headHtml("TELEPORT MASTER"));
		hb.append("<br>");
		
		for (Map<String, Location> tele : TELEPORTS.values())
		{
			for (String name : tele.keySet())
			{
				hb.append("<table width=280>");
				hb.append("<tr>");
				//hb.append("<td align=center>", Html.newImage("L2UI.bbs_folder", 32, 32), "</td>");
				hb.append("<td align=center><button action=\"bypass -h Engine NpcTeleporter teleport ", name, "\" value=\"", name, "\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
				//hb.append("<td align=center>", Html.newImage("L2UI.bbs_folder", 32, 32), "</td>");
				hb.append("</tr>");
				hb.append("</table>");
			}
			
			hb.append("<br>");
			hb.append("<br>");
		}
		
		//hb.append("<table width=280>");
		//hb.append("<tr>");
		//hb.append("<td align=center>", Html.newImage("L2UI.bbs_folder", 32, 32), "</td>");
		//hb.append("<td align=center><button action=\"bypass -h Engine ZoneTvT teleport tozonetvt", "\" value=\"", "TvT", "\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		//hb.append("<td align=center>", Html.newImage("L2UI.bbs_folder", 32, 32), "</td>");
		//hb.append("</tr>");
		//hb.append("</table>");
		
//		hb.append("<table width=256>");
//		hb.append("<tr>");
//		hb.append("<td align=center><button action=\"bypass -h Engine NpcTeleporter giran_town_center\" value=\"Giran\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("<td align=center><button action=\"bypass -h Engine NpcTeleporter aden_town_center\" value=\"Aden\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("</tr>");
//		hb.append("<tr>");
//		hb.append("<td align=center><button action=\"bypass -h Engine NpcTeleporter goddard_town_center\" value=\"Goddard\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("<td align=center><button action=\"bypass -h Engine NpcTeleporter rune_town_center\" value=\"Rune\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("</tr>");
//		hb.append("<tr>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/TownAreas/dion.htm\" value=\"Dion\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/TownAreas/oren.htm\" value=\"Oren\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("</tr>");
//		hb.append("<tr>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/TownAreas/gludio.htm\" value=\"Gludio\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/TownAreas/gludin.htm\" value=\"Gludin\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("</tr>");
//		hb.append("<tr>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/TownAreas/schuttgart.htm\" value=\"Schuttgart\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/TownAreas/heine.htm\" value=\"Heine\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("</tr>");
//		hb.append("<tr>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/TownAreas/hv.htm\" value=\"Hunters\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/TownAreas/floran.htm\" value=\"Floran\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("</tr>");
//		hb.append("</table>");
//		hb.append("<br>");
//		hb.append("<table width=256>");
//		hb.append("<tr>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/TownAreas/ti_starting.htm\" value=\"Human\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/TownAreas/dwarf_starting.htm\" value=\"Dwarven\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("</tr>");
//		hb.append("<tr>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/TownAreas/orc_starting.htm\" value=\"Orc\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/TownAreas/delven_starting.htm\" value=\"Dark Elven\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("</tr>");
//		hb.append("<tr>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/TownAreas/elven_starting.htm\" value=\"Elven\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("<td align=center><button action=\"bypass -h admin_html teleports/TownAreas/kamael_starting.htm\" value=\"Kamael\" width=100 height=21 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
//		hb.append("</tr>");
//		hb.append("</table>");
		
		hb.append(Html.HTML_END);
		
		sendHtml(player, npc, hb);
	}
	
	@RegisterEvent(EventType.ON_PLAYER_NPC_BYPASS)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void onEventBypass(OnPlayerNpcBypass event)
	{		
		if (!Util.checkBypass(event.getCommand(), "NpcTeleporter"))
			return;
		
		L2PcInstance player = event.getActiveChar();
		L2Npc npc = (L2Npc) event.getNpc();
		
		if (player == null)
			return;
		
		StringTokenizer st = new StringTokenizer(event.getCommand(), " ");
		
		st.nextToken();
		st.nextToken();
		
		switch (st.nextToken())
		{
			case "TownAreas":
				
				showTownAreasLocs(player,npc);
				break;
				
			case "teleport":
				
				String locName = st.nextToken();//getTokens(st);
				
				for (Map<String, Location> tele : TELEPORTS.values())
				{
					if (!tele.containsKey(locName))
					{
						// posible bypass....juaz!
						continue;
					}
					
					event.getActiveChar().teleToLocation(tele.get(locName));
					break;
				}			
		}
	}
	
	private String getTokens(StringTokenizer st)
	{
		if (st.countTokens() == 1)
		{
			return st.nextToken();
		}
		
		int length = st.countTokens();
		String s = "";
		
		for (int i=0; i<length; i++)
		{
			s += i==0 ? st.nextToken() : " " + st.nextToken();
		}
		return s;
	}
	
	public static void main(String[] args) {
		new NpcTeleporter();
	}
}
