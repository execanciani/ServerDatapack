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
package com.l2jserver.datapack.custom.engine.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author fissban
 */
public class UtilPlayer
{
	private static final String CREATE_ACCOUNTS = "INSERT INTO accounts (login, password, lastactive, access_level) values (?, ?, ?, ?)";
	private static final String UPDATE_CHARACTER = "UPDATE characters SET char_name=?,level=?,maxHp=?,curHp=?,maxCp=?,curCp=?,maxMp=?,curMp=?,face=?,hairStyle=?,hairColor=?,sex=?,heading=?,x=?,y=?,z=?,exp=?,expBeforeDeath=?,sp=?,karma=?,pvpkills=?,pkkills=?,clanid=?,race=?,classid=?,base_class=?,deletetime=?,cancraft=?,title=?,rec_have=?,rec_left=?,accesslevel=?,online=?,onlinetime=?,clan_privs=?,wantspeace=?,isin7sdungeon=?,punish_level=?,punish_timer=?,power_grade=?,nobless=?,hero=?,lvl_joined_academy=?,apprentice=?,sponsor=?,varka_ketra_ally=?,clan_join_expiry_time=?,clan_create_expiry_time=?,death_penalty_level=? WHERE obj_id=?";
	
	private static final ReentrantLock _locker = new ReentrantLock();
	
	public static L2PcInstance createPlayer(L2PcInstance admin, String playerName, String accountName, PlayerTemplate template, int lvl, Sex sex, byte hairStyle, byte face, List<Integer> items, int offSet)
	{
		
		_locker.lock();
		int objectId = IdFactory.getInstance().getNextId();
		
		Player newChar = null;
		// Salvamos los valores en la DB
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(CREATE_ACCOUNTS))
		{
			ps.setString(1, accountName);
			
			MessageDigest md = MessageDigest.getInstance("SHA");
			byte[] raw = "123456Abc!".getBytes(StandardCharsets.UTF_8);
			String hashBase64 = Base64.getEncoder().encodeToString(md.digest(raw));
			
			ps.setString(2, hashBase64);
			ps.setLong(3, System.currentTimeMillis());
			ps.setInt(4, 0);
			ps.execute();
			
			// hairColor -> random
			byte hairColor = 0;
			
			newChar = Player.create(objectId, template, accountName, playerName, hairStyle, hairColor, face, sex);
			
			World.getInstance().addObject(newChar);
			newChar.setXYZInvisible(admin.getX() + Rnd.get(-offSet, offSet), admin.getY() + Rnd.get(-offSet, offSet), admin.getZ());
			//newChar.setHeading(Rnd.get(65536));
			// random level
			final long oldExp = newChar.getStatus().getExp();
			final long newExp = newChar.getStatus().getExpForLevel(lvl);
			newChar.getStatus().addExp(newExp - oldExp);
			// lo curamos por las dudas
			newChar.getStatus().setHpMp(newChar.getStatus().getMaxHp(), newChar.getStatus().getMaxMp());
			// items a equipar o no :P
			for (int itemId : items)
			{
				if (itemId == 0)
				{
					continue;
				}
				
				ItemInstance item = newChar.getInventory().addItem("Init", itemId, 1, newChar, null);
				
				if (item.isEquipable())
				{
					if (newChar.getActiveWeaponItem() == null || !(item.getItem().getType2() != Item.TYPE2_WEAPON))
					{
						newChar.getInventory().equipItemAndRecord(item);
					}
				}
			}
			
			newChar.giveSkills(); //newChar.giveAvailableSkills();
			newChar.deleteMe();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			_locker.unlock();
		}
		
		return newChar;
	}
	
	public static Player spawnPlayer(int objectId)
	{
		_locker.lock();
		
		Player player = null;
		try
		{
			GameClient client = new GameClient(null);
			
			player = Player.restore(objectId);
			player.setOnlineStatus(true, true);
			player.setClient(client);
			//client.setActiveChar(player);
			client.setState(GameClientState.IN_GAME);
			client.setAccountName(player.getAccountName());
			client.setDetached(true);
			
			client.setSessionId(new SessionKey(0, 0, 0, 0));
			LoginServerThread.getInstance().addClient(player.getAccountName(), client);
			World.getInstance().addPlayer(player);
			
			player.spawnMe(player.getX(), player.getY(), player.getZ());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			_locker.unlock();
		}
		
		return player;
	}
	
	public static void storeCharBase(Player player)
	{
		try (Connection con = ConnectionPool.getConnection())
		{
			// Get the exp, level, and sp of base class to store in base table
			// int currentClassIndex = player.getClassIndex();
			long exp = player.getStatus().getExp();
			int level = player.getStatus().getLevel();
			int sp = player.getStatus().getSp();
			
			PreparedStatement statement = con.prepareStatement(UPDATE_CHARACTER);
			
			statement.setString(1,player.getName());
			statement.setInt(2, level);
			statement.setInt(3, player.getStatus().getMaxHp());
			statement.setDouble(4, player.getStatus().getHp());
			statement.setInt(5, player.getStatus().getMaxCp());
			statement.setDouble(6, player.getStatus().getCp());
			statement.setInt(7, player.getStatus().getMaxMp());
			statement.setDouble(8, player.getStatus().getMp());
			statement.setInt(9, player.getAppearance().getFace());
			statement.setInt(10, player.getAppearance().getHairStyle());
			statement.setInt(11, player.getAppearance().getHairColor());
			statement.setInt(12, player.getAppearance().getSex().ordinal());
			statement.setInt(13, player.getHeading());
			statement.setInt(14, player.getX());
			statement.setInt(15, player.getY());
			statement.setInt(16, player.getZ());
			statement.setLong(17, exp);
			statement.setLong(18, player.getExpBeforeDeath());
			statement.setInt(19, sp);
			statement.setInt(20, player.getKarma());
			statement.setInt(21, player.getPvpKills());
			statement.setInt(22, player.getPkKills());
			statement.setInt(23, player.getRecomHave());
			statement.setInt(24, player.getRecomLeft());
			statement.setInt(25, player.getClanId());
			statement.setInt(26, player.getRace().ordinal());
			statement.setInt(27, player.getClassId().getId());
			statement.setLong(28, player.getDeleteTimer());
			statement.setString(29, player.getTitle());
			statement.setInt(30, player.getAccessLevel().getLevel());
			statement.setInt(31, player.isOnlineInt());
			statement.setInt(32, player.isIn7sDungeon() ? 1 : 0);
			statement.setInt(33, player.getClanPrivileges());
			statement.setInt(34, player.wantsPeace() ? 1 : 0);
			statement.setInt(34, player.getBaseClass());
			statement.setLong(35, 0);
			statement.setInt(36, player.getPunishment().getType().ordinal());
			statement.setLong(37, player.getPunishment().getTimer());
			statement.setInt(38, player.isNoble() ? 1 : 0);
			statement.setLong(39, player.getPowerGrade());
			statement.setInt(40, player.getPledgeType());
			statement.setLong(41, player.getRecomHave()); //(41, player.getLastRecomUpdate());
			statement.setInt(42, player.getLvlJoinedAcademy());
			statement.setLong(43, player.getApprentice());
			statement.setLong(44, player.getSponsor());
			statement.setInt(45, player.getAllianceWithVarkaKetra());
			statement.setLong(46, player.getClanJoinExpiryTime());
			statement.setLong(47, player.getClanCreateExpiryTime());
			statement.setString(48, player.getName());
			statement.setLong(49, player.getDeathPenaltyBuffLevel());
			statement.setInt(50, player.getObjectId());
			
			statement.execute();
			statement.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
