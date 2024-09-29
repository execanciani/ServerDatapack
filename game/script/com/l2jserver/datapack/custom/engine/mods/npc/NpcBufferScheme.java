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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.l2jserver.datapack.custom.enginemods.data.SchemeBuffData;
import com.l2jserver.datapack.custom.enginemods.enums.BuffType;
import com.l2jserver.datapack.custom.enginemods.holders.BuffHolder;
import com.l2jserver.datapack.custom.enginemods.mods.AbstractMods;
import com.l2jserver.datapack.custom.enginemods.util.Util;
import com.l2jserver.datapack.custom.enginemods.util.UtilBuffs;
import com.l2jserver.datapack.custom.enginemods.util.UtilInventory;
import com.l2jserver.datapack.custom.enginemods.util.UtilNpc;
import com.l2jserver.datapack.custom.enginemods.util.builders.html.Html;
import com.l2jserver.datapack.custom.enginemods.util.builders.html.HtmlBuilder;
import com.l2jserver.datapack.custom.enginemods.util.builders.html.HtmlBuilder.HtmlType;
import com.l2jserver.datapack.handlers.effecthandlers.instant.SummonCubic;
import com.l2jserver.datapack.custom.enginemods.util.builders.html.L2UI;
import com.l2jserver.datapack.custom.enginemods.util.builders.html.L2UI_CH3;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.L2Summon;
import com.l2jserver.gameserver.model.actor.instance.L2CubicInstance;
import com.l2jserver.gameserver.model.actor.instance.L2NpcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PetInstance;
import com.l2jserver.gameserver.model.actor.status.PcStatus;
import com.l2jserver.gameserver.model.actor.status.SummonStatus;
import com.l2jserver.gameserver.model.events.EventType;
import com.l2jserver.gameserver.model.events.ListenerRegisterType;
import com.l2jserver.gameserver.model.events.annotations.Id;
import com.l2jserver.gameserver.model.events.annotations.RegisterEvent;
import com.l2jserver.gameserver.model.events.annotations.RegisterType;
import com.l2jserver.gameserver.model.events.impl.character.npc.OnNpcFirstTalk;
import com.l2jserver.gameserver.model.events.impl.character.player.OnPlayerNpcBypass;
import com.l2jserver.gameserver.model.skills.AbnormalType;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.network.SystemMessageId;


/**
 * Adaptacion del buffer de RIn4
 * @author fissban
 */
public class NpcBufferScheme extends AbstractMods
{
	private static final int NPC_ID =  900106;  //ConfigData.BUFFER_SCHEME_NPC_ID;
	
	private static final String TITLE_NAME = "Buffer"; //ConfigData.BUFFER_SCHEME_NPC_TITLE_NAME;
	
	private static final boolean FREE_BUFFS =  true; //ConfigData.BUFFER_SCHEME_FREE_BUFFS;
	
	// precio de usar cada buff, sea del tipo q sea.
	private static final int BUFF_PRICE = 0; //ConfigData.BUFFER_SCHEME_BUFF_PRICE;
	
	// precio de los buffs pre-establecidos
	private static final int BUFF_SET_PRICE = 0; //ConfigData.BUFFER_SCHEME_BUFF_SET_PRICE;
	// item a cobrar
	private static final int CONSUMABLE_ID = 57; //ConfigData.BUFFER_SCHEME_CONSUMABLE_ID;
	
	private static final boolean TIME_OUT = false;
	// tiempo entre cada accion...crear scheme,bufearse,etc en segundos
	private static final int TIME_OUT_TIME = 1;
	// minimo nievel para usar el buffer
	private static final int MIN_LEVEL = 85; //ConfigData.BUFFER_SCHEME_MIN_LEVEL;
	private static final int BUFF_REMOVE_PRICE = 0; //ConfigData.BUFFER_SCHEME_BUFF_REMOVE_PRICE;
	private static final int SCHEME_BUFF_PRICE = 0; // ConfigData.BUFFER_SCHEMES_BUFF_PRICE;
	private static final int SCHEMES_PER_PLAYER = 3; //ConfigData.BUFFER_SCHEMES_PER_PLAYER;
	
	// maximo de buffs q se pueden agregar de cada tipo por scheme
	private static final int MAX_SCHEME_BUFFS = 46; //ConfigData.BUFFER_SCHEME_MAX_SCHEME_BUFFS;
	private static final int MAX_SCHEME_DANCES = 46; //ConfigData.BUFFER_SCHEME_MAX_SCHEME_DANCES;
	
	public NpcBufferScheme()
	{
		//registerMod(ConfigData.ENABLE_NpcBufferScheme);
		LOG.info("[NpcBufferScheme]: Mod Loaded");
	}
	
	@Override
	public void onModState()
	{
		// TODO Auto-generated method stub
	}
	
	@RegisterEvent(EventType.ON_PLAYER_NPC_BYPASS)
	@RegisterType(ListenerRegisterType.GLOBAL_PLAYERS)
	public void onPlayerNpcBypass(OnPlayerNpcBypass event)
	{		
		LOG.info(NpcBufferScheme.class.getSimpleName() + " -- command: " + event.getCommand());
		
		if (!Util.checkBypass(event.getCommand(), "NpcBufferScheme"))
			return;
		
		L2PcInstance player = event.getActiveChar();
		L2NpcInstance npc = (L2NpcInstance) event.getNpc();
		
		if (player == null || npc == null)
			return;
		
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
		String eventParam7 = st.hasMoreTokens() ? st.nextToken() : "";
		System.out.println(eventParam7);
		String eventParam8 = st.hasMoreTokens() ? st.nextToken() : "";
		System.out.println(eventParam8);
		
		switch (eventParam2)
		{
			case "reloadscript":
			{
				if (eventParam1.equals("0"))
				{
					rebuildMainHtml(player, npc);
					return;
				}
			}
			case "redirect_main":
				rebuildMainHtml(player, npc);
				return;
			case "redirect_view_buff":
				buildHtml(player, npc, BuffType.BUFF, eventParam3.equals("") ? 1 : Integer.parseInt(eventParam3));
				return;
			case "redirect_view_resist":
				buildHtml(player, npc, BuffType.RESIST, eventParam3.equals("") ? 1 : Integer.parseInt(eventParam3));
				return;
			case "redirect_view_song":
				buildHtml(player, npc, BuffType.SONG, eventParam3.equals("") ? 1 : Integer.parseInt(eventParam3));
				return;
			case "redirect_view_dance":
				buildHtml(player, npc, BuffType.DANCE, eventParam3.equals("") ? 1 : Integer.parseInt(eventParam3));
				return;
			case "redirect_view_chant":
				buildHtml(player, npc, BuffType.CHANT, eventParam3.equals("") ? 1 : Integer.parseInt(eventParam3));
				return;
			case "redirect_view_other":
				buildHtml(player, npc, BuffType.OTHER, eventParam3.equals("") ? 1 : Integer.parseInt(eventParam3));
				return;
			case "redirect_view_special":
				buildHtml(player, npc, BuffType.SPECIAL, eventParam3.equals("") ? 1 : Integer.parseInt(eventParam3));
				return;
			case "redirect_view_cubic":
				buildHtml(player, npc, BuffType.CUBIC, eventParam3.equals("") ? 1 : Integer.parseInt(eventParam3));
				return;
			//case "buffpet":
			//{
				//if (checkTimeOut(player))
				//{
					//setValueDB(player.getObjectId(), "Pet-On-Off", eventParam1);
					//if (TIME_OUT)
					//{
						//addTimeout(player, GaugeColor.GREEN, TIME_OUT_TIME / 2, 600);
					//}
				//}
				//rebuildMainHtml(player, (L2NpcInstance)event.getNpc());
				//return;
			//}
			case "create":
			{
				// anti sql inject
				String name = eventParam3.replaceAll("[ !" + "\"" + "#$%&'()*+,/:;<=>?@" + "\\[" + "\\\\" + "\\]" + "\\^" + "`{|}~]", ""); // JOJO
				
				if (name.length() == 0 || name.equals("no_name"))
				{
					player.sendPacket(SystemMessageId.INCORRECT_NAME_TRY_AGAIN);
					showText(player, npc, "Info", "Please, enter the scheme name!", true, "Return", "main");
					return;
				}
				
				// XXX INSERT INTO npcbuffer_scheme_list (player_id,scheme_name) VALUES (?,?)
				// obtenemos el listado de schemes
				String allSchemes = getValueDB(player.getObjectId(), "schemeName");
				
				// if first scheme create...init var
				if (allSchemes == null)
				{
					allSchemes = "";
				}
				else
				{
					// check if scheme name exist
					for (String s : allSchemes.split(","))
					{
						if (s != null && s.equals(name))
						{
							player.sendPacket(SystemMessageId.INCORRECT_NAME_TRY_AGAIN);
							showText(player, npc, "Info", "The name you are trying to use is already in use!", true, "Return", "main");
							return;
						}
					}
				}
				
				allSchemes += name + ",";
				
				// salvamos el nuevo listado
				setValueDB(player.getObjectId(), "schemeName", allSchemes);
				
				rebuildMainHtml(player, npc);
				return;
			}
			case "delete":
			{
				String schemeName = eventParam3;
				// removemos la lista de buffs
				removeValueDB(player.getObjectId(), schemeName);
				// removemos el nombre del scheme del listado
				String schemes = getValueDB(player.getObjectId(), "schemeName");
				
				// prevent bypass
				if (schemes != null)
				{
					String[] cantidad_schemes = schemes.split(","); 
					
					if (cantidad_schemes.length == 1)
					{
						removeValueDB(player.getObjectId(), "schemeName");
					}
					else if (cantidad_schemes.length > 1)
					{
						if (schemes.contains(schemeName + ","))
						{
							schemes = schemes.replace(schemeName + ",", "");
						}
						else
						{
							// TODO prevenimos para los q ya tienen mas de un scheme con el viejo sistema
							schemes = schemes.replace(schemeName, "");
						}
						
						// salvamos el nuevo listado de los nombres de los schemes
						setValueDB(player.getObjectId(), "schemeName", schemes);
					}
				}
				
				rebuildMainHtml(player, npc);
				return;
			}
			case "delete_c":
			{
				// TODO podriamos crear un metodo q devuelva el html para evitar
				// tenerlo todo en una misma linea no?
				sendHtml(player, npc, "<html><title>" + TITLE_NAME + "</title><body><center>" + Html.headHtml("BUFFER") + "<br>Do you really want to delete '" + eventParam3 + "' scheme?<br><br>" + "<button value=\"Yes\" action=\"bypass -h Engine NpcBufferScheme delete " + eventParam3 + "\" width=75 height=21 back=" + L2UI_CH3.Btn1_normalOn + " fore=" + L2UI_CH3.Btn1_normal + ">" + "<button value=\"No\" action=\"bypass -h Engine NpcBufferScheme delete_1\" width=75 height=21 back=" + L2UI_CH3.Btn1_normalOn + " fore=" + L2UI_CH3.Btn1_normal + ">" + "<br><font color=303030>" + TITLE_NAME + "</font></center></body></html>");
				return;
			}
			case "create_1":
			{
				createScheme(player, npc);
				return;
			}
			case "edit_1":
			{
				editScheme(player, npc);
				return;
			}
			case "delete_1":
			{
				deleteScheme(player, npc);
				return;
			}
			case "manage_scheme_add":
			{
				viewAllSchemeBuffs(player, npc, eventParam3, eventParam4, "add");
				return;
			}
			case "manage_scheme_remove":
			{
				viewAllSchemeBuffs(player, npc, eventParam3, eventParam4, "remove");
				return;
			}
			case "manage_scheme_select":
			{
				getOptionList(player, npc, eventParam3);
				return;
			}
			case "remove_buff":
			{
				String[] split = eventParam3.split("_");
				String schemeNameRemove = split[0];
				String id = split[1];
				String level = split[2];
				// "DELETE FROM npcbuffer_scheme_contents WHERE scheme_id=? AND skill_id=? AND skill_level=? LIMIT 1"
				
				// obtenemos el scheme actual
				String listBuff = getValueDB(player.getObjectId(), schemeNameRemove);
				// agregamos el nuevo buff
				listBuff = listBuff.replaceFirst(id + "," + level + ";", "");
				// lo salvamos en la memoria y la db
				setValueDB(player.getObjectId(), schemeNameRemove, listBuff);
				
				//int temp = Integer.parseInt(eventParam4) - 1;
				
				//if (temp <= 0)
				//{
				//	getOptionList(player, npc, schemeNameRemove);
				//}
				//else
				//{
					viewAllSchemeBuffs(player, npc, schemeNameRemove, eventParam4, "remove");
				//}
				
				return;
			}
			case "add_buff":
			{
				String[] split = eventParam3.split("_");
				String schemeNameAdd = split[0];
				String id = split[1];
				String level = split[2];
				
				// "INSERT INTO npcbuffer_scheme_contents (scheme_id,skill_id,skill_level,buff_class) VALUES (?,?,?,?)"
				
				// obtenemos el scheme actual
				String listBuff = getValueDB(player.getObjectId(), schemeNameAdd);
				// agregamos el nuevo buff
				if (listBuff == null)
				{
					listBuff = id + "," + level + ";";
				}
				else
				{
					listBuff = listBuff.concat(id + "," + level + ";");
				}
				
				// lo salvamos en la memoria y la db
				setValueDB(player.getObjectId(), schemeNameAdd, listBuff);
				
				int temp = Integer.parseInt(eventParam4) + 1;
				
				if (temp >= MAX_SCHEME_BUFFS + MAX_SCHEME_DANCES)
				{
					getOptionList(player, npc, schemeNameAdd);
				}
				else
				{
					viewAllSchemeBuffs(player, npc, schemeNameAdd, eventParam4, "add");
				}
				return;
			}
			case "heal":
			{
				if (checkTimeOut(player))
				{
					if (UtilInventory.getItemsCount(player, CONSUMABLE_ID) < BUFF_PRICE)
					{
						showText(player, npc, "Sorry", "You don't have the enough items:<br>You need: <font color=LEVEL>" + BUFF_PRICE + " " + getItemNameHtml(CONSUMABLE_ID) + "!", false, "0", "0");
						return;					
					}
					//final boolean getPetbuff = isPetBuff(player);
					//if (getPetbuff)
					//{
						//if (UtilNpc.isPet(player.getSummon()))
						//{
						//	heal(player, getPetbuff);
						//}
						//else
						//{
						//	showText(player, "Info", "You can't use the Pet's options.<br>Summon your pet first!", false, "Return", "main");
						//	return;
						//}
					//}
					//else
					//{
						heal(player, false);//getPetbuff);
					//}
					UtilInventory.takeItems(player, CONSUMABLE_ID, BUFF_PRICE);
					if (TIME_OUT)
					{
						//addTimeout(player, GaugeColor.BLUE, TIME_OUT_TIME / 2, 600);
					}
				}
				rebuildMainHtml(player, (L2NpcInstance)event.getNpc());
				return;
			}
			case "removeBuffs":
			{
				if (checkTimeOut(player))
				{
					if (UtilInventory.getItemsCount(player, CONSUMABLE_ID) < BUFF_REMOVE_PRICE)
					{
						showText(player, npc, "Sorry", "You don't have the enough items:<br>You need: <font color=LEVEL>" + BUFF_REMOVE_PRICE + " " + getItemNameHtml(CONSUMABLE_ID) + "!", false, "0", "0");
						return;
					}
					//final boolean getPetbuff = isPetBuff(player);
					//if (getPetbuff)
					//{
						//if (player.getPet() != null)
						//if (UtilNpc.isPet(player.getSummon()))
						//{
							//player.getPet().stopAllEffects();
						//	player.getSummon().stopAllEffects();
						//}
						//else
						//{
						//	showText(player, "Info", "You can't use the Pet's options.<br>Summon your pet first!", false, "Return", "main");
						//	return;
						//}
					//}
					//else
					//{
						player.stopAllEffects();
						if (player.getCubics() != null)
						{
							for (L2CubicInstance cubic : player.getCubics().values())
							//for (Cubic cubic : player.getCubics().values())
							{
								cubic.stopAction();
								player.getCubics().remove(cubic.getId());
								//player.delCubic(cubic.getId());
							}
						}
					//}
					UtilInventory.takeItems(player, CONSUMABLE_ID, BUFF_REMOVE_PRICE);
					if (TIME_OUT)
					{
						//addTimeout(player, GaugeColor.RED, TIME_OUT_TIME / 2, 600);
					}
				}
				
				rebuildMainHtml(player, (L2NpcInstance)event.getNpc());
				return;
			}
			case "cast":
			{
				if (checkTimeOut(player))
				{
					List<BuffHolder> buffs = new ArrayList<>();
					
					String schemeName = eventParam3;
					String buffList = getValueDB(player.getObjectId(), schemeName);
						
					if (buffList != null && !buffList.isEmpty())
					{
						for (String buff : buffList.split(";"))
						{
							int id = Integer.parseInt(buff.split(",")[0]);
							int level = Integer.parseInt(buff.split(",")[1]);
							
							if (isEnabled(id, level))
							{
								buffs.add(new BuffHolder(id, level));
							}
						}
					}
					
					if (buffs.isEmpty())
					{
						viewAllSchemeBuffs(player, npc, eventParam3, "1", "add");
						return;
					}
					if (!FREE_BUFFS)
					{
						if (UtilInventory.getItemsCount(player, CONSUMABLE_ID) < SCHEME_BUFF_PRICE)
						{
							showText(player, npc, "Sorry", "You don't have the enough items:<br>You need: <font color=LEVEL>" + SCHEME_BUFF_PRICE + " " + getItemNameHtml(CONSUMABLE_ID) + "!", false, "0", "0");
							return;
						}
					}
					
					//final boolean getPetbuff = isPetBuff(player);
					
					for (BuffHolder bh : buffs)
					{
						//if (!getPetbuff)
						//{
							SkillData.getInstance().getSkill(bh.getId(), bh.getLevel()).applyEffects(player, player);
						//}
//						else
//						{
//							if (UtilNpc.isPet(player.getSummon()))
//							//if (player.getPet() != null)
//							{
//								SkillData.getInstance().getSkill(bh.getId(), bh.getLevel()).applyEffects(player.getSummon(), player.getSummon());
//							}
//							else
//							{
//								showText(player, "Info", "You can't use the Pet's options.<br>Summon your pet first!", false, "Return", "main");
//								return;
//							}
//						}
					}
					UtilInventory.takeItems(player, CONSUMABLE_ID, SCHEME_BUFF_PRICE);
					if (TIME_OUT)
					{
						//addTimeout(player, GaugeColor.CYAN, TIME_OUT_TIME, 600);
					}
				}
				rebuildMainHtml(player, npc);
				return;
			}
			case "giveBuffs":
			{
				final int cost = BUFF_PRICE;
				
				int id = Integer.parseInt(eventParam3);
				int level = Integer.parseInt(eventParam4);
				if (!isEnabled(id, level))
				{
					// posible bypass
					System.out.println("posible bypass en scheme buff -> " + player.getName());
					return;
				}
				
				if (checkTimeOut(player))
				{
					if (!FREE_BUFFS)
					{
						if (UtilInventory.getItemsCount(player, CONSUMABLE_ID) < cost)
						{
							showText(player, npc, "Sorry", "You don't have the enough items:<br>You need: <font color=LEVEL>" + cost + " " + getItemNameHtml(CONSUMABLE_ID) + "!", false, "0", "0");
							return;
						}
					}
					Skill skill = SkillData.getInstance().getSkill(id, level);
					if (skill.getAbnormalType() == AbnormalType.SUMMON_CONDITION)
					{
						if (UtilInventory.getItemsCount(player, skill.getItemConsumeId()) < skill.getItemConsumeCount())
						{
							showText(player, npc, "Sorry", "You don't have the enough items:<br>You need: <font color=LEVEL>" + skill.getItemConsumeCount() + " " + getItemNameHtml(skill.getItemConsumeId()) + "!", false, "0", "0");
							return;
						}
					}
					//final boolean getPetbuff = isPetBuff(player);
					//if (!getPetbuff)
					//{
						if (eventParam5.equals("CUBIC"))
						{
							if (!player.getCubics().isEmpty())
							{
								for (L2CubicInstance cubic : player.getCubics().values())
								{
									cubic.stopAction();
									player.getCubics().remove(cubic.getId());
								}
							}
							
							SummonCubic sc = UtilBuffs.getSummonCubicEffect(skill);
							
							if (sc != null)
							{
								int cubicPower = sc.getPower();
								int cubicDuration = sc.getDuration();
								int cubicDelay = sc.getDelay();
								int cubicSkillChance = sc.getSkillChance();
								int cubicMaxCount = sc.getMaxCount();
								
								player.addCubic(Integer.parseInt(eventParam3), Integer.parseInt(eventParam4), cubicPower, cubicDelay, cubicSkillChance, cubicMaxCount, cubicDuration, false);
								player.broadcastUserInfo();
							}
						}
					//}
					//else
					//{
						//if (eventParam3.equals("CUBIC"))
						//{
						//	if (!player.getCubics().isEmpty())
						//	{
						//		for (L2CubicInstance cubic : player.getCubics().values())
						//		{
						//			cubic.stopAction();
						//			player.getCubics().remove(cubic.getId());
									//player.delCubic(cubic.getId());
						//		}
						//	}
						//}
						//else
						//{
							//if (UtilNpc.isPet(player.getSummon()))
							//{
							//	showText(player, "Info", "You can't use the Pet's options.<br>Summon your pet first!", false, "Return", "main");
							//	return;
							//}
						//}
					//}
					skill.applyEffects(player, player);
					UtilInventory.takeItems(player, CONSUMABLE_ID, cost);
					if (TIME_OUT)
					{
						//addTimeout(player, GaugeColor.CYAN, TIME_OUT_TIME / 10, 600);
					}
				}
				buildHtml(player, npc, BuffType.valueOf(eventParam5), eventParam6.equals("") ? 1 : Integer.parseInt(eventParam6));
				return;
			}
			case "buff_description":
			{
				//info buff
				if (eventParam6.equals("infobuffs"))
				{
					buildHtml(player, npc, Integer.parseInt(eventParam3), Integer.parseInt(eventParam4), Integer.parseInt(eventParam5), eventParam6, "", "");
				}
				else if (eventParam5.equals("infobuffs"))
				{
					buildHtml(player, npc, BuffType.valueOf(eventParam3), Integer.parseInt(eventParam4));
				}
				
				//scheme buff info
				else if (eventParam8.equals("infoschemebuffs"))
				{
					buildHtml(player, npc, Integer.parseInt(eventParam3), Integer.parseInt(eventParam4), Integer.parseInt(eventParam5), eventParam8, eventParam6, eventParam7);
				}
				else if (eventParam6.equals("infoschemebuffs"))
				{
					viewAllSchemeBuffs(player, npc, eventParam3, eventParam4, eventParam5);
				}
				return;
			}
			case "castBuffSet":
			{
				if (checkTimeOut(player))
				{
					if (!FREE_BUFFS)
					{
						if (UtilInventory.getItemsCount(player, CONSUMABLE_ID) < BUFF_SET_PRICE)
						{
							showText(player, npc, "Sorry", "You don't have the enough items:<br>You need: <font color=LEVEL>" + BUFF_SET_PRICE + " " + getItemNameHtml(CONSUMABLE_ID) + "!", false, "0", "0");
							return;
						}
					}
					
					//final boolean getPetbuff = isPetBuff(player);
					//if (!getPetbuff)
					//{
						for (BuffHolder bh : player.isMageClass() ? SchemeBuffData.getAllMageBuffs() : SchemeBuffData.getAllWarriorBuffs())
						{
							SkillData.getInstance().getSkill(bh.getId(), bh.getLevel()).applyEffects(player, player);
						}
					//}
					//else
					//{
						//if (UtilNpc.isPet(player.getSummon()))
						//{
							// a los pets le daremos los mismos buff que a los guerreros
						//	for (BuffHolder bh : SchemeBuffData.getAllWarriorBuffs())
						//	{
						//		SkillData.getInstance().getSkill(bh.getId(), bh.getLevel()).applyEffects(player.getSummon(), player.getSummon());
						//	}
						//}
						//else
						//{
						//	showText(player, "Info", "You can't use the Pet's options.<br>Summon your pet first!", false, "Return", "main");
						//	return;
						//}
					//}
					UtilInventory.takeItems(player, CONSUMABLE_ID, BUFF_SET_PRICE);
					if (TIME_OUT)
					{
						//addTimeout(player, GaugeColor.CYAN, TIME_OUT_TIME, 600);
					}
				}
				rebuildMainHtml(player, (L2NpcInstance)event.getNpc());
				return;
			}
			
		}
		rebuildMainHtml(player, (L2NpcInstance)event.getNpc());
	}
	
	@RegisterEvent(EventType.ON_NPC_FIRST_TALK)
	@RegisterType(ListenerRegisterType.NPC)
	@Id(NPC_ID)
	public void onNpcFirstTalk(OnNpcFirstTalk event)
	{		
		L2PcInstance player = event.getActiveChar();
		L2Npc npc = event.getNpc();
		
		if (player.isGM())
		{
			rebuildMainHtml(player, (L2NpcInstance)npc);
			return;
		}
		
		if (checkTimeOut(player))
		{
			if (player.getLevel()
					< MIN_LEVEL)
			{
				showText(player, npc, "Info", "Your level is too low!<br>You have to be at least level <font color=LEVEL>" + MIN_LEVEL + "</font>,<br>to use my services!", false, "Return", "main");
				return;
			}
			else if (player.isInCombat())
			{
				showText(player, npc, "Info", "You can't buff while you are attacking!<br>Stop your fight and try again!", false, "Return", "main");
				return;
			}
			// return showText(st, "Sorry", "You have to wait a while!<br>if you wish to use my services!", false, "Return", "main");
		}
		rebuildMainHtml(player, (L2NpcInstance)npc);
	}
	
	private static String getSkillIconHtml(int id, int level)
	{
		String iconNumber = SkillData.getInstance().getSkill(id, level).getIcon();
		return "<button action=\"bypass -h Engine NpcBufferScheme description " + id + " " + level + " x\" width=32 height=32 back=\"Icon.skill" + iconNumber + "\" fore=\"Icon.skill" + iconNumber + "\">";
	}
	
	private boolean checkTimeOut(L2PcInstance player)
	{
		String blockUntilTime = getValueDB(player.getObjectId(), "blockUntilTime");
		if (blockUntilTime == null || (int) (System.currentTimeMillis() / 1000) > Integer.parseInt(blockUntilTime))
		{
			return true;
		}
		
		return false;
	}
	
//	private void addTimeout(L2PcInstance player, GaugeColor gaugeColor, int amount, int offset)
//	{
//		int endtime = (int) ((System.currentTimeMillis() + amount * 1000) / 1000);
//		setValueDB(player.getObjectId(), "blockUntilTime", String.valueOf(endtime));
//		player.sendPacket(new SetupGauge(gaugeColor, amount * 1000 + offset));
//	}
	
	private static String getItemNameHtml(int itemId)
	{
		return "&#" + itemId + ";";
	}
	
	private static void heal(L2PcInstance player, boolean isPet)
	{
		L2Summon target = player.getSummon();
		if (!isPet)
		{
			PcStatus pcStatus = player.getStatus();
			//PlayerStat pcStat = player.getStat();
			//pcStatus.setMaxCpHpMp();
			pcStatus.setCurrentHp(player.getMaxHp());
			pcStatus.setCurrentMp(player.getMaxMp());
			pcStatus.setCurrentCp(player.getMaxCp());
		}
		else if (target != null)
		{
			SummonStatus petStatus = target.getStatus();
			//PlayerStatus petStatus = target.getStatus();
			//SummonStat petStat = target.getStat();
			//petStatus.setMaxHpMp();;
			petStatus.setCurrentHp(target.getMaxHp());
			petStatus.setCurrentMp(target.getMaxMp());
			
			if (target instanceof L2PetInstance)
			{
				L2PetInstance pet = (L2PetInstance) target;
				//pet.setCurrentFed(pet.getPetData().getMaxMeal());
				pet.setCurrentFed(pet.getMaxFed());
				//player.sendPacket(new SetSummonRemainTime(pet.getPetData()., pet.getCurrentFed()));
			}
			else
			{
				throw new RuntimeException();
			}
		}
	}
	
	private void rebuildMainHtml(L2PcInstance player, L2NpcInstance npc)
	{
		HtmlBuilder hb = new HtmlBuilder(HtmlType.HTML_TYPE);
		hb.append("<html><body>");
		hb.append(Html.headHtml("BUFFER"));
		hb.append("<br>");
		
		hb.append("<center>");
		
		hb.append(Html.newImage(L2UI.SquareWhite, 264, 1));
		hb.append("<table width=275 border=0 cellspacing=0 cellpadding=1 bgcolor=000000>");
		hb.append("<tr>");
		hb.append("<td align=center><font color=FFFF00>Buffs:</font></td>");
		hb.append("</tr>");
		hb.append("</table>");
		hb.append(Html.newImage(L2UI.SquareWhite, 264, 1));
		
		hb.append("<br>");
		
		final String bottonA, bottonB, bottonC;
		//String pet = getValueDB(player.getObjectId(), "Pet-On-Off");
		//if (pet == null || pet.equals("1"))
		//{
		//	bottonA = "Auto Buff Pet";
		//	bottonB = "Heal My Pet";
		//	bottonC = "Remove Buffs";
		//	hb.append("<button value=\"Pet Options\" action=\"bypass -h Engine NpcBufferScheme buffpet 0\" width=75 height=21  back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		//}
		//else
		//{
			bottonA = "Auto Buff";
			bottonB = "Heal";
			bottonC = "Remove Buffs";
			//hb.append("<button value=\"Char Options\" action=\"bypass -h Engine NpcBufferScheme buffpet 1\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		//}
		
		hb.append("<table width=80% cellspacing=0 cellpadding=1>");
		hb.append("<tr>");
		hb.append("<td height=32 align=center><button value=Buffs action=\"bypass -h Engine NpcBufferScheme redirect_view_buff\" width=75 height=21back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		hb.append("<td height=32 align=center><button value=Resist action=\"bypass -h Engine NpcBufferScheme redirect_view_resist\" width=75 height=21back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		hb.append("</tr>");
		
		hb.append("<tr>");
		hb.append("<td height=32 align=center><button value=Songs action=\"bypass -h Engine NpcBufferScheme redirect_view_song\" width=75 height=21back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		hb.append("<td height=32 align=center><button value=Dances action=\"bypass -h Engine NpcBufferScheme redirect_view_dance\" width=75 height=21back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		hb.append("</tr>");
		
		hb.append("<tr>");
		hb.append("<td height=32 align=center><button value=Chants action=\"bypass -h Engine NpcBufferScheme redirect_view_chant\" width=75 height=21back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		hb.append("<td height=32 align=center><button value=Special action=\"bypass -h Engine NpcBufferScheme redirect_view_special\" width=75 height=21back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		hb.append("</tr>");
		
		hb.append("<tr>");
		hb.append("<td height=32 align=center><button value=Others action=\"bypass -h Engine NpcBufferScheme redirect_view_other\" width=75 height=21back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		hb.append("<td height=32 align=center><button value=Cubics action=\"bypass -h Engine NpcBufferScheme redirect_view_cubic\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		hb.append("</tr>");
		hb.append("</table>");
		// ---------------------------------------------------------------------------------------------
		hb.append(Html.newImage(L2UI.SquareWhite, 264, 1));
		hb.append("<table width=275 border=0 cellspacing=0 cellpadding=1 bgcolor=000000>");
		hb.append("<tr>");
		hb.append("<td align=center><font color=FFFF00>Preset:</font></td>");
		hb.append("</tr>");
		hb.append("</table>");
		hb.append(Html.newImage(L2UI.SquareWhite, 264, 1));
		hb.append("<table width=100% height=37 border=0 cellspacing=0 cellpadding=5>");
		hb.append("<tr>");
		hb.append("<td><button value=\"", bottonA, "\" action=\"bypass -h Engine NpcBufferScheme castBuffSet 0 0 0\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		hb.append("<td><button value=\"", bottonB, "\" action=\"bypass -h Engine NpcBufferScheme heal\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		hb.append("<td><button value=\"", bottonC, "\" action=\"bypass -h Engine NpcBufferScheme removeBuffs 0 0 0\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		hb.append("</tr>");
		hb.append("</table>");
		
		hb.append("");
		// generate html scheme
		hb.append(generateScheme(player));
		
		hb.append("<br>");
		hb.append("<font color=303030>" + TITLE_NAME + "</font>");
		hb.append("</center>");
		hb.append("</body></html>");
		
		sendHtml(player, npc, hb);
	}
	
	private String generateScheme(L2PcInstance player)
	{
		String schemeNames = getValueDB(player.getObjectId(), "schemeName");
		
		HtmlBuilder hb = new HtmlBuilder(HtmlType.HTML_TYPE);
		hb.append("<br1>");
		hb.append(Html.newImage(L2UI.SquareWhite, 264, 1));
		hb.append("<table width=271 border=0 cellspacing=0 cellpadding=1 bgcolor=000000>");
		hb.append("<tr>");
		hb.append("<td align=center><font color=FFFF00>Scheme:</font></td>");
		hb.append("<td align=right><font color=LEVEL></font></td>");
		hb.append("</tr>");
		hb.append("</table>");
		hb.append(Html.newImage(L2UI.SquareWhite, 264, 1));
		
		hb.append("<br1>");
		
		hb.append("<table cellspacing=0 cellpadding=5 height=28>");
		
		if (schemeNames != null)
		{
			String[] TRS =
			{
				"<tr><td>",
				"</td>",
				"<td>",
				"</td></tr>"
			};
			
			hb.append("<table>");
			int td = 0;
			for (int i = 0; i < schemeNames.split(",").length; ++i)
			{
				if (td > 2)
				{
					td = 0;
				}
				hb.append(TRS[td] + "<button value=\"", schemeNames.split(",")[i], "\" action=\"bypass -h Engine NpcBufferScheme cast ", schemeNames.split(",")[i], "\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">", TRS[td + 1]);
				td += 2;
			}
			
			hb.append("</table>");
		}
		
		if (schemeNames == null || schemeNames.split(",").length < SCHEMES_PER_PLAYER)
		{
			hb.append("<br1><table><tr><td><button value=\"Create\" action=\"bypass -h Engine NpcBufferScheme create_1\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
		}
		else
		{
			hb.append("<br1><table width=100><tr>");
		}
		
		if (schemeNames != null)
		{
			hb.append("<td><button value=\"Edit\" action=\"bypass -h Engine NpcBufferScheme edit_1\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td><td><button value=\"Delete\" action=\"bypass -h Engine NpcBufferScheme delete_1\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td></tr></table>");
		}
		else
		{
			hb.append("</tr></table>");
		}
		return hb.toString();
	}
	
	/**
	 * Chequeamos la cantidad de buffs q tiene un player en su scheme.
	 * @param player
	 * @param schemeName
	 * @return
	 */
	private int getBuffCount(L2PcInstance player, String schemeName)
	{
		String buffList = getValueDB(player.getObjectId(), schemeName);
		if (buffList != null)
		{
			return buffList.split(";").length;
		}
		
		return 0;
	}
	
	/**
	 * Chequeamos si el buff aun esta en nuestro listado de buff habilitados.
	 * @param id
	 * @param level
	 * @return
	 */
	private static boolean isEnabled(int id, int level)
	{
		for (BuffHolder bh : SchemeBuffData.getAllGeneralBuffs())
		{
			if (bh.getId() == id && bh.getLevel() == level)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Chequeamos si el player tiene un determinado buff en su scheme o no.
	 * @param player
	 * @param scheme
	 * @param id
	 * @param level
	 * @return
	 */
	private boolean isUsed(L2PcInstance player, String scheme, int id, int level)
	{
		String buffList = getValueDB(player.getObjectId(), scheme);
		
		if (buffList == null || buffList.isEmpty())
		{
			return false;
		}
		
		for (String buff : buffList.split(";"))
		{
			if (Integer.parseInt(buff.split(",")[0]) == id && Integer.parseInt(buff.split(",")[1]) == level)
			{
				return true;
			}
		}
		
		return false;
	}
	
	private static void showText(L2PcInstance player, L2Npc npc, String type, String text, boolean buttonEnabled, String buttonName, String location)
	{
		HtmlBuilder hb = new HtmlBuilder(HtmlType.HTML_TYPE);
		hb.append("<html><head><title>", TITLE_NAME, "</title></head><body>");
		hb.append(Html.headHtml("BUFFER"));
		hb.append("<center>");
		hb.append("<br>");
		hb.append("<font color=LEVEL>", type, "</font>");
		hb.append("<br>", text, "<br>");
		if (buttonEnabled)
		{
			hb.append("<button value=\"" + buttonName + "\" action=\"bypass -h Engine NpcBufferScheme redirect_", location, " 0 0\" width=75 height=21  back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		}
		hb.append("<font color=303030>", TITLE_NAME, "</font></center></body></html>");
		
		sendHtml(player, npc, hb);
	}
	
	/**
	 * Chequeamos si el player selecciono la opcion de pet o char para bufear
	 * @param player
	 * @return
	 */
	private boolean isPetBuff(L2PcInstance player)
	{
		String pettBuff = getValueDB(player.getObjectId(), "Pet-On-Off");
		return pettBuff == null || pettBuff.equals("1");
	}
	
	/**
	 * Mini menu para los scheme
	 * @return
	 */
	private static void createScheme(L2PcInstance player, L2Npc npc)
	{
		HtmlBuilder hb = new HtmlBuilder(HtmlType.HTML_TYPE);
		hb.append("<html><head><title>", TITLE_NAME, "</title></head><body>");
		hb.append(Html.headHtml("BUFFER"));
		hb.append("<center>");
		hb.append("<br><br>");
		hb.append("You MUST separate new words with a dot (.)");
		hb.append("<br><br>");
		hb.append("Scheme name: <edit var=\"name\" width=100>");
		hb.append("<br><br>");
		hb.append("<button value=\"Create Scheme\" action=\"bypass -h Engine NpcBufferScheme create $name no_name\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		hb.append("<br>");
		hb.append("<button value=\"Back\" action=\"bypass -h Engine NpcBufferScheme redirect_main\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		hb.append("<br>");
		hb.append("<font color=303030>", TITLE_NAME, "</font>");
		hb.append("</center>");
		hb.append("</body></html>");
		sendHtml(player, npc, hb);
	}
	
	private void deleteScheme(L2PcInstance player, L2Npc npc)
	{
		HtmlBuilder hb = new HtmlBuilder(HtmlType.HTML_TYPE);
		hb.append("<html><head><title>", TITLE_NAME, "</title></head><body>");
		hb.append(Html.headHtml("BUFFER"));
		hb.append("<center>");
		hb.append("<br>Available schemes:<br><br>");
		
		// XXX "SELECT * FROM npcbuffer_scheme_list WHERE player_id=?"
		String schemeNames = getValueDB(player.getObjectId(), "schemeName");
		for (String scheme : schemeNames.split(","))
		{
			hb.append("<button value=\"", scheme, "\" action=\"bypass -h Engine NpcBufferScheme delete_c ", scheme, " x\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		}
		
		hb.append("<br>");
		hb.append("<button value=\"Back\" action=\"bypass -h Engine NpcBufferScheme redirect_main\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		hb.append("<br>");
		hb.append("<font color=303030>" + TITLE_NAME + "</font>");
		hb.append("</center>");
		hb.append("</body></html>");
		
		sendHtml(player, npc, hb);
	}
	
	private void editScheme(L2PcInstance player, L2Npc npc)
	{
		HtmlBuilder hb = new HtmlBuilder(HtmlType.HTML_TYPE);
		hb.append("<html><head><title>", TITLE_NAME, "</title></head><body>");
		hb.append(Html.headHtml("BUFFER"));
		hb.append("<center>");
		hb.append("<br>Select a scheme that you would like to manage:<br><br>");
		
		// XXX"SELECT * FROM npcbuffer_scheme_list WHERE player_id=?"
		
		String schemeNames = getValueDB(player.getObjectId(), "schemeName");
		for (String scheme : schemeNames.split(","))
		{
			hb.append("<button value=\"" + scheme + "\" action=\"bypass -h Engine NpcBufferScheme manage_scheme_select " + scheme + "\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		}
		
		hb.append("<br>");
		hb.append("<button value=\"Back\" action=\"bypass -h Engine NpcBufferScheme redirect_main\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		hb.append("<br>");
		hb.append("<font color=303030>" + TITLE_NAME + "</font>");
		hb.append("</center>");
		hb.append("</body></html>");
		
		sendHtml(player, npc, hb);
	}
	
	private void getOptionList(L2PcInstance player, L2Npc npc, String scheme)
	{
		int bcount = getBuffCount(player, scheme);
		
		HtmlBuilder hb = new HtmlBuilder(HtmlType.HTML_TYPE);
		hb.append("<html><head><title>", TITLE_NAME, "</title></head><body>");
		hb.append(Html.headHtml("BUFFER"));
		hb.append("<center>");
		hb.append("<br>There are ", Html.newFontColor("LEVEL", bcount), " buffs in current scheme!<br><br>");
		
		if (bcount < MAX_SCHEME_BUFFS + MAX_SCHEME_DANCES)
		{
			hb.append("<button value=\"Add buffs\" action=\"bypass -h Engine NpcBufferScheme manage_scheme_add ", scheme, " 1\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		}
		if (bcount > 0)
		{
			hb.append("<button value=\"Remove buffs\" action=\"bypass -h Engine NpcBufferScheme manage_scheme_remove ", scheme, " 1\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		}
		hb.append("<br>");
		hb.append("<button value=\"Back\" action=\"bypass -h Engine NpcBufferScheme edit_1\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		hb.append("<button value=\"Home\" action=\"bypass -h Engine NpcBufferScheme redirect_main\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		hb.append("<br>");
		hb.append(Html.newFontColor("303030", TITLE_NAME));
		hb.append("</center>");
		hb.append("</body></html>");
		
		sendHtml(player, npc, hb);
	}
	
	private static void buildHtml(L2PcInstance player, L2NpcInstance npc, BuffType buffType, int page)
	{
		Util.print(NpcBufferScheme.class, "buildHtml");
		
		HtmlBuilder hb = new HtmlBuilder(HtmlType.HTML_TYPE);
		hb.append("<html><head><title>", TITLE_NAME, "</title></head><body>");
		hb.append("<center><br>");
		
		List<BuffHolder> buffs = new ArrayList<>();
		
		for (BuffHolder bh : SchemeBuffData.getAllGeneralBuffs())
		{
			if (bh.getSkill() == null)
			{
				System.out.println("buffId: " + bh.getId());
			}
			else if (bh.getType() == buffType)
			{
				buffs.add(bh);
			}
		}
		
		if (buffs.size() == 0)
		{
			hb.append("No buffs are available at this moment!");
		}
		else
		{
			if (FREE_BUFFS)
			{
				hb.append("All buffs are for <font color=LEVEL>free</font>!");
			}
			else
			{
				int price = BUFF_PRICE;
				
				hb.append("All special buffs cost <font color=LEVEL>" + Html.formatAdena(price) + "</font> adena!");
			}
			hb.append("<br1>");
			
			int MAX_PER_PAGE = 12;
			int searchPage = MAX_PER_PAGE * (page - 1);
			int count = 0;
			
			hb.append(Html.newImage(L2UI.SquareWhite, 264, 1));
			for (BuffHolder bh : buffs)
			{
				// min
				if (count < searchPage)
				{
					count++;
					continue;
				}
				// max
				if (count >= searchPage + MAX_PER_PAGE)
				{
					continue;
				}
				
				hb.append("<table width=264", count % 2 == 0 ? " bgcolor=000000>" : ">");
				String name = bh.getSkill().getName().replace("+", " ");
				hb.append("<tr>");
				hb.append("<td height=32 fixwidth=32>", getSkillIconHtml(bh.getId(), bh.getLevel()), "</td>");
				hb.append("<td height=32 width=117 align=center><a action=\"bypass -h Engine NpcBufferScheme giveBuffs ", bh.getId(), " ", bh.getLevel(), " ", buffType.name(), " ", page, "\">", name, "</a></td>");
				hb.append("<td height=32 width=117 align=center><a action=\"bypass -h Engine NpcBufferScheme buff_description ", bh.getId(), " ", bh.getLevel(), " ", page, " ", "infobuffs", "\">Description</a></td>");
				hb.append("</tr>");
				
				hb.append("</table>");
				hb.append(Html.newImage(L2UI.SquareWhite, 264, 1));
				count++;
			}
			
			hb.append("<center>");
			hb.append(Html.newImage(L2UI.SquareWhite, 264, 1));
			hb.append("<table bgcolor=000000>");
			hb.append("<tr>");
			
			int currentPage = 1;
			for (int i = 0; i < buffs.size(); i++)
			{
				if (i % MAX_PER_PAGE == 0)
				{
					hb.append("<td width=20 align=center><a action=\"bypass -h Engine NpcBufferScheme redirect_view_", buffType.name().toLowerCase(), " ", currentPage, "\">", currentPage, "</a></td>");
					currentPage++;
				}
			}
			
			hb.append("</tr>");
			hb.append("</table>");
			hb.append(Html.newImage(L2UI.SquareWhite, 264, 1));
			hb.append("</center>");
		}
		hb.append("<br>");
		hb.append("<button value=\"Back\" action=\"bypass -h Engine NpcBufferScheme redirect_main\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		hb.append("<br>");
		hb.append("<font color=303030>", TITLE_NAME, "</font>");
		hb.append("</center>");
		hb.append("</body></html>");
		
		sendHtml(player, npc, hb);
	}

	private static void buildHtml(L2PcInstance player, L2NpcInstance npc, int buffId, int level, int page, String option_name, String scheme_name, String action)
	{	
		HtmlBuilder hb = new HtmlBuilder(HtmlType.HTML_TYPE);
		hb.append("<html><body>");
		hb.append(Html.headHtml("BUFFER"));
		hb.append("<br>");
		hb.append("<center>");
		
		BuffHolder buff = new BuffHolder(buffId, level);
		
		for (BuffHolder bh : SchemeBuffData.getAllGeneralBuffs())
		{
			if (bh.getId() == buff.getId())
			{
				buff.description(bh.getDescription());
				buff.type(bh.getType());
			}
		}
		String name = buff.getSkill().getName().replace("+", " ");
		hb.append("<br>", name, "<br>");
		hb.append("<br>", getSkillIconHtml(buff.getId(), buff.getLevel()), "<br>");
		hb.append(buff.getDescription(), "<br>");
		
		hb.append("<br>");
		if (option_name.equals("infobuffs"))
		{
			hb.append("<button value=\"Back\" action=\"bypass -h Engine NpcBufferScheme buff_description ", buff.getType().name(), " ", page, " ", option_name, "\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		}
		else if (option_name.equals("infoschemebuffs"))
		{
			hb.append("<button value=\"Back\" action=\"bypass -h Engine NpcBufferScheme buff_description ", scheme_name, " ", page, " ", action, " ", option_name, "\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		}
		hb.append("<br>");
		hb.append("<font color=303030>", TITLE_NAME, "</font>");
		hb.append("</center>");
		hb.append("</body></html>");
		
		sendHtml(player, npc, hb);
	}
	
	private String viewAllSchemeBuffsGetBuffCount(L2PcInstance player, String scheme)
	{
		int count = 0;
		int D_S_Count = 0;
		int B_Count = 0;
		
		// obtenemos el listado de skills del scheme de un player
		String buffList = getValueDB(player.getObjectId(), scheme);
		
		if (buffList != null && !buffList.isEmpty())
		{
			// parseamos cada buff
			for (String buff : buffList.split(";"))
			{
				// parseamos el id y lvl de cada buff
				int id = Integer.parseInt(buff.split(",")[0]);
				int level = Integer.parseInt(buff.split(",")[1]);
				
				count++;
				
				for (BuffHolder bh : SchemeBuffData.getAllGeneralBuffs())
				{
					if (!isEnabled(id, level))
					{
						continue;
					}
					
					if (bh.getId() == id && bh.getLevel() == level)
					{
						if (bh.getType() == BuffType.SONG || bh.getType() == BuffType.DANCE)
						{
							D_S_Count++;
						}
						else
						{
							B_Count++;
						}
						break;
					}
				}
			}
		}
		
		return count + " " + B_Count + " " + D_S_Count;
	}
	
	/**
	 * @param player
	 * @param schemeName
	 * @param page
	 * @param action
	 */
	private void viewAllSchemeBuffs(L2PcInstance player, L2Npc npc, String schemeName, String page, String action)
	{
		HtmlBuilder hb = new HtmlBuilder(HtmlType.HTML_TYPE);
		hb.append("<html><head><title>", TITLE_NAME, "</title></head><body>");
		hb.append(Html.headHtml("BUFFER"));
		hb.append("<center>");
		
		hb.append("<br>");
		
		String[] eventSplit = viewAllSchemeBuffsGetBuffCount(player, schemeName).split(" ");
		
		int buffsTotal = Integer.parseInt(eventSplit[0]);
		int buffsCount = Integer.parseInt(eventSplit[1]);
		int daceSong = Integer.parseInt(eventSplit[2]);
		
		List<BuffHolder> buffs = new ArrayList<>();
		
		if (action.equals("add"))
		{
			hb.append("You can add <font color=LEVEL>", MAX_SCHEME_BUFFS - buffsCount, "</font> Buffs and <font color=LEVEL>", MAX_SCHEME_DANCES - daceSong, "</font> Dances more!");
			
			for (BuffHolder bh : SchemeBuffData.getAllGeneralBuffs())
			{
				if (daceSong > MAX_SCHEME_DANCES)
				{
					if (bh.getType() == BuffType.DANCE || bh.getType() == BuffType.SONG)
					{
						continue;
					}
				}
				
				if (buffsCount > MAX_SCHEME_BUFFS)
				{
					if (bh.getType() != BuffType.DANCE && bh.getType() != BuffType.SONG)
					{
						continue;
					}
				}
				
				buffs.add(bh);
			}
		}
		else if (action.equals("remove"))
		{
			hb.append("You have <font color=LEVEL>", buffsCount, "</font> Buffs and <font color=LEVEL>", daceSong, "</font> Dances");
			
			String buffList = getValueDB(player.getObjectId(), schemeName);
			if (buffList == null || buffList.isEmpty())
			{
				System.out.println("error en remove buff");
			}
			else
			{
				for (String buff : buffList.split(";"))
				{
					int id = Integer.parseInt(buff.split(",")[0]);
					int level = Integer.parseInt(buff.split(",")[1]);
					
					buffs.add(new BuffHolder(id, level));
				}
			}
		}
		else
		{
			throw new RuntimeException();
		}
		
		hb.append("<br1>", Html.newImage(L2UI.SquareWhite, 264, 1), "<table border=0 bgcolor=000000><tr>");
		final int buffsPerPage = 10;
		final String width;
		int pc = (buffs.size() - 1) / buffsPerPage + 1;
		
		// definimos el largo de las celdas con las pagina
		if (pc > 5)
		{
			width = "25";
		}
		else
		{
			width = "50";
		}
		
		for (int ii = 1; ii <= pc; ++ii)
		{
			// creamos la botonera con las paginas
			if (ii == Integer.parseInt(page))
			{
				hb.append("<td width=", width, " align=center><font color=LEVEL>", ii, "</font></td>");
			}
			else if (action.equals("add"))
			{
				hb.append("<td width=", width, ">", "<a action=\"bypass -h Engine NpcBufferScheme manage_scheme_add ", schemeName, " ", ii, " x\">", ii, "</a></td>");
			}
			else if (action.equals("remove"))
			{
				hb.append("<td width=", width, ">", "<a action=\"bypass -h Engine NpcBufferScheme manage_scheme_remove ", schemeName, " ", ii, " x\">", ii, "</a></td>");
			}
			else
			{
				throw new RuntimeException();
			}
		}
		hb.append("</tr></table>", Html.newImage(L2UI.SquareWhite, 264, 1));
		
		int limit = buffsPerPage * Integer.parseInt(page);
		int start = limit - buffsPerPage;
		int end = Math.min(limit, buffs.size());
		int k = 0;
		for (int i = start; i < end; ++i)
		{
			BuffHolder bh = buffs.get(i);
			
			String name = bh.getSkill().getName();
			int id = bh.getId();
			int level = bh.getLevel();
			
			if (action.equals("add"))
			{
				if (!isUsed(player, schemeName, id, level))
				{
					if (k % 2 != 0)
					{
						
						hb.append("<br1>", Html.newImage(L2UI.SquareGray, 264, 1), "<table border=0>");
					}
					else
					{
						hb.append("<br1>", Html.newImage(L2UI.SquareGray, 264, 1), "<table border=0 bgcolor=000000>");
					}
					
					hb.append("<tr>");
					hb.append("<td width=35>", getSkillIconHtml(id, level), "</td>");
					hb.append("<td fixwidth=170><a action=\"bypass -h Engine NpcBufferScheme buff_description ", id, " ", level, " ", page, " ", schemeName, " ", action, " ", "infoschemebuffs", "\">", name, "</a></td>");
					hb.append("<td><button value=\"Add\" action=\"bypass -h Engine NpcBufferScheme add_buff ", schemeName, "_", id, "_", level, " ", page, " ", buffsTotal, "\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
					hb.append("</tr>");
					hb.append("</table>");
					k += 1;
				}
			}
			else if (action.equals("remove"))
			{
				if (k % 2 != 0)
				{
					hb.append("<br1>", Html.newImage(L2UI.SquareGray, 264, 1), "<table border=0>");
				}
				else
				{
					hb.append("<br1>", Html.newImage(L2UI.SquareGray, 264, 1), "<table border=0 bgcolor=000000>");
				}
				hb.append("<tr>");
				hb.append("<td width=35>", getSkillIconHtml(id, level), "</td>");
				
				hb.append("<td fixwidth=170><a action=\"bypass -h Engine NpcBufferScheme buff_description ", id, " ", level, " ", page, " ", schemeName, " ", action, " ", "infoschemebuffs", "\">", name, "</a></td>");
				//hb.append("<td fixwidth=170>", name, "</td>");
				hb.append("<td><button value=\"Remove\" action=\"bypass -h Engine NpcBufferScheme remove_buff ", schemeName, "_", id, "_", level, " ", page, " ", buffsTotal, "\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, "></td>");
				hb.append("</tr>");
				hb.append("</table>");
				k += 1;
			}
		}
		hb.append("<br><br>");
		hb.append("<button value=Back action=\"bypass -h Engine NpcBufferScheme manage_scheme_select ", schemeName, "\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		hb.append("<button value=Home action=\"bypass -h Engine NpcBufferScheme redirect_main\" width=75 height=21 back=", L2UI_CH3.Btn1_normalOn, " fore=", L2UI_CH3.Btn1_normal, ">");
		hb.append("<br>");
		hb.append("<font color=303030>", TITLE_NAME, "</font>");
		hb.append("</center>");
		hb.append("</body></html>");
		
		sendHtml(player, npc, hb);
	}
	
	public static void main(String[] args) {
		new NpcBufferScheme();
	}
}
