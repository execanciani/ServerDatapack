package com.l2jserver.datapack.custom.enginemods.mods.npc;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.apache.commons.collections4.map.HashedMap;

import com.l2jserver.datapack.custom.enginemods.enums.ItemType;
import com.l2jserver.datapack.custom.enginemods.holders.ItemShopHolder;
import com.l2jserver.datapack.custom.enginemods.mods.AbstractMods;
import com.l2jserver.datapack.custom.enginemods.util.Util;
import com.l2jserver.datapack.custom.enginemods.util.builders.html.Html;
import com.l2jserver.datapack.custom.enginemods.util.builders.html.HtmlBuilder;
import com.l2jserver.datapack.custom.enginemods.util.builders.html.L2UI;
import com.l2jserver.datapack.custom.enginemods.util.builders.html.L2UI_CH3;
import com.l2jserver.datapack.custom.enginemods.util.builders.html.HtmlBuilder.HtmlType;
import com.l2jserver.gameserver.data.xml.impl.MultisellData;
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

public class NpcGmShop extends AbstractMods
{	
	private static final int NPC_ID = 900107;
	
	private static final String TITLE_NAME = "GM Shop";
	
	private static final int MIN_LEVEL = 85; //ConfigData.BUFFER_SCHEME_MIN_LEVEL;
	
	private static final Map<ItemType, ItemShopHolder> ITEMS_INFO = new LinkedHashMap<>();
	{
		ItemShopHolder ih = null;
		Map<String, Integer> m_ids = null;
		
		ih = new ItemShopHolder();
		ih.set_type(ItemType.WEAPON);
		m_ids = new LinkedHashMap<>();
		m_ids.put("INFINITY",900105);
		m_ids.put("S84",900106);
		m_ids.put("S80",900107);
		m_ids.put("S",900108);
		m_ids.put("A",900109);
		m_ids.put("B",900110);
		m_ids.put("C",900111);	
		ih.set_multisell_ids(m_ids);
		ITEMS_INFO.put(ih.type(), ih);
		
		ih = new ItemShopHolder();
		ih.set_type(ItemType.ARMOR);
		m_ids = new LinkedHashMap<>();
		m_ids.put("S84",900112);
		m_ids.put("S80",900113);
		m_ids.put("S",900114);
		m_ids.put("A",900115);
		m_ids.put("B",900116);
		m_ids.put("C",900117);	
		ih.set_multisell_ids(m_ids);
		ITEMS_INFO.put(ih.type(), ih);
		
		ih = new ItemShopHolder();
		ih.set_type(ItemType.JEWELS);
		m_ids = new LinkedHashMap<>();
		m_ids.put("S84",900118);
		ih.set_multisell_ids(m_ids);
		ITEMS_INFO.put(ih.type(), ih);
		
		ih = new ItemShopHolder();
		ih.set_type(ItemType.MISC);
		m_ids = new LinkedHashMap<>();
		m_ids.put("OTHERS",900131);
		m_ids.put("MASK",900130);
		ih.set_multisell_ids(m_ids);
		ITEMS_INFO.put(ih.type(), ih);
	};
	
	//bypass
	//<a action="bypass -h npc_%objectId%_multisell 202">Purchase quest items and rare spellbooks</a>
	
	public NpcGmShop()
	{
		LOG.info("[NpcGmShop]: Mod Loaded");
	}
	
	@Override
	public void onModState()
	{
		
	}

	@RegisterEvent(EventType.ON_NPC_FIRST_TALK)
	@RegisterType(ListenerRegisterType.NPC)
	@Id(NPC_ID)
	public void onFirstTalk(OnNpcFirstTalk event)
	{	
		L2PcInstance player = event.getActiveChar();
		L2Npc npc = event.getNpc();
		
		Util.print(NpcGmShop.class, " ", "onFirstTalk", " ", player.getName(), " ", npc.getId());
		
		rebuildMainHtml(player, (L2NpcInstance)npc);
	}
	
	@RegisterEvent(EventType.ON_PLAYER_NPC_BYPASS)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void onPlayerNpcBypass(OnPlayerNpcBypass event)
	{			
		if (!Util.checkBypass(event.getCommand(), "NpcGmShop"))
			return;
		
		L2PcInstance player = event.getActiveChar();
		L2NpcInstance npc = (L2NpcInstance) event.getNpc();
		
		if (player == null || npc == null)
			return;
		
		//bypass multisell
		//<a action="bypass -h npc_%objectId%_multisell 002">Exchange Dimension Diamonds.</a><br>
		//<a action="bypass -h npc_%objectId%_Quest NoblesseTeleport">Noblesse Exclusive Teleport</a><br>
		
		StringTokenizer st = new StringTokenizer(event.getCommand(), " ");
		String bypass = st.hasMoreTokens() ? st.nextToken() : "redirect_main";
		System.out.println(bypass);
		String eventParam1 = st.hasMoreTokens() ? st.nextToken() : "";
		System.out.println(eventParam1);
		String eventParam2 = st.hasMoreTokens() ? st.nextToken() : "";
		System.out.println(eventParam2);
		String eventParam3 = st.hasMoreTokens() ? st.nextToken() : "";
		System.out.println(eventParam3);
		String eventParam4 = st.hasMoreTokens() ? st.nextToken() : "";
		System.out.println(eventParam4);
		String eventParam5 = st.hasMoreTokens() ? st.nextToken() : "";
		System.out.println(eventParam5);
		String eventParam6 = st.hasMoreTokens() ? st.nextToken() : "";
		System.out.println(eventParam6);
		
		switch (eventParam2)
		{
			case "redirect_main":
			{
				rebuildMainHtml(player, npc);
				return;
			}
			case "redirect_view_weapons":
			{
				buildHtml(player, npc, ItemType.WEAPON);
				return;
			}
			case "redirect_view_armors":
			{
				buildHtml(player, npc, ItemType.ARMOR);
				//buildHtml(player, npc, BuffType.RESIST, eventParam3.equals("") ? 1 : Integer.parseInt(eventParam3));
				return;
			}
			case "redirect_view_jewels":
			{
				buildHtml(player, npc, ItemType.JEWELS);
				//buildHtml(player, npc, BuffType.BUFF, eventParam3.equals("") ? 1 : Integer.parseInt(eventParam3));
				return;
			}
			case "redirect_view_others":
			{
				//buildHtml(player, npc, BuffType.RESIST, eventParam3.equals("") ? 1 : Integer.parseInt(eventParam3));
				return;
			}
			case "redirect_view_misc":
			{
				buildHtml(player, npc, ItemType.MISC);
				//buildHtml(player, npc, BuffType.RESIST, eventParam3.equals("") ? 1 : Integer.parseInt(eventParam3));
				return;
			}
			case "redirect_view_exchange":
			{
				//buildHtml(player, npc, BuffType.RESIST, eventParam3.equals("") ? 1 : Integer.parseInt(eventParam3));
				return;
			}
			case "view_items":
			{
				//buildItemHtml(player, npc, ItemType.valueOf(eventParam3), eventParam4);
				MultisellData.getInstance().separateAndSend(Integer.parseInt(eventParam3), player, npc, false);
				return;
			}
		}
	}
		
	private void rebuildMainHtml(L2PcInstance player, L2NpcInstance npc)
	{
		HtmlBuilder hb = new HtmlBuilder(HtmlType.HTML_TYPE);
		hb.append("<html><body>");
		hb.append(Html.headHtml("GMSHOP"));
		hb.append("<br>");
		
		hb.append("<center>");
		
		hb.append(Html.newImage(L2UI.SquareWhite, 264, 1));
		hb.append("<table width=275 border=0 cellspacing=0 cellpadding=1 bgcolor=000000>");
		hb.append("<tr>");
		hb.append("<td align=center><font color=FFFF00>Shop:</font></td>");
		hb.append("</tr>");
		hb.append("</table>");
		hb.append(Html.newImage(L2UI.SquareWhite, 264, 1));
		
		hb.append("<br>");
		
		hb.append("<table width=80% cellspacing=0 cellpadding=1>");
		hb.append("<tr>");
		hb.append("<td height=32 align=center><button value=WEAPONS action=\"bypass -h Engine NpcGmShop redirect_view_weapons\" width=75 height=21back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		hb.append("</tr>");
		
		hb.append("<tr>");
		hb.append("<td height=32 align=center><button value=ARMORS action=\"bypass -h Engine NpcGmShop redirect_view_armors\" width=75 height=21back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		hb.append("</tr>");
		
		hb.append("<tr>");
		hb.append("<td height=32 align=center><button value=JEWELS action=\"bypass -h Engine NpcGmShop redirect_view_jewels\" width=75 height=21back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		hb.append("</tr>");
		
		//hb.append("<tr>");
		//hb.append("<td height=32 align=center><button value=OTHERS action=\"bypass -h Engine NpcGmShop redirect_view_others\" width=75 height=21back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		//hb.append("</tr>");
		
		hb.append("<tr>");
		hb.append("<td height=32 align=center><button value=MISC action=\"bypass -h Engine NpcGmShop redirect_view_misc\" width=75 height=21back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		hb.append("</tr>");
		
		//hb.append("<tr>");
		//hb.append("<td height=32 align=center><button value=SPECIAL action=\"bypass -h Engine NpcGmShop redirect_view_special\" width=75 height=21back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		//hb.append("</tr>");
		
		hb.append("</table>");
		
		hb.append("<br>");
		hb.append("<font color=303030>", TITLE_NAME, "</font>");
		hb.append("</center>");
		hb.append("</body></html>");
		
		sendHtml(player, npc, hb);
	}
	
	private static void buildHtml(L2PcInstance player, L2NpcInstance npc, ItemType itemtype)
	{
		HtmlBuilder hb = new HtmlBuilder(HtmlType.HTML_TYPE);
		hb.append("<html><body>");
		hb.append(Html.headHtml("GMSHOP"));
		hb.append("<br>");
		
		hb.append("<center>");
		
		hb.append(Html.newImage(L2UI.SquareWhite, 264, 1));
		hb.append("<table width=275 border=0 cellspacing=0 cellpadding=1 bgcolor=000000>");
		hb.append("<tr>");
		hb.append("<td align=center><font color=FFFF00>Shop:</font></td>");
		hb.append("</tr>");
		hb.append("</table>");
		hb.append(Html.newImage(L2UI.SquareWhite, 264, 1));
		
		hb.append("<br>");
		
		hb.append("<table width=80% cellspacing=0 cellpadding=1>");
		
		for (Entry<ItemType, ItemShopHolder> itemEntry : ITEMS_INFO.entrySet())
		{
			if (!itemEntry.getKey().name().toUpperCase().equalsIgnoreCase(itemtype.name().toUpperCase()))
			{
				continue;
			}
			for (Entry<String, Integer> e : itemEntry.getValue().multisell_ids().entrySet())
			{
				hb.append("<tr>");
				hb.append("<td height=32 align=center><button value=", e.getKey() , " action=\"bypass -h Engine NpcGmShop view_items ", e.getValue(), "\" width=75 height=21back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
				hb.append("</tr>");
			}
		}
		
		hb.append("</table>");
		
		hb.append("<br>");
		hb.append("<font color=303030>", TITLE_NAME, "</font>");
		hb.append("</center>");
		hb.append("</body></html>");
		
		sendHtml(player, npc, hb);
	}
	
	public static void main(String[] args) {
		new NpcGmShop();
	}
}
