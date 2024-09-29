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
package com.l2jserver.datapack.custom.engine.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.datapack.custom.engine.holders.PlayerHolder;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Clase encargada de llevar informacion sobre los personajes.
 * @author fissban
 */
public class PlayerData
{
	private static final Logger LOG = Logger.getLogger(PlayerData.class.getName());
	// SQL
	private static final String SELECT_CHARACTERS = "SELECT charId,char_name,account_name FROM characters";
	
	private static final Map<Integer, PlayerHolder> _players = new ConcurrentHashMap<>();
	
	public PlayerData()
	{
		// TODO Auto-generated constructor stub
	}
	
	public static void load()
	{
		// prevenimos datos duplicados en cado de recargar este metodo
		_players.clear();
		
		try (Connection con = ConnectionFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(SELECT_CHARACTERS);
			ResultSet rset = statement.executeQuery())
		{
			// Go though the recordset of this SQL query
			while (rset.next())
			{
				PlayerHolder ph = new PlayerHolder(rset.getInt("charId"), rset.getString("char_name"), rset.getString("account_name"));
				_players.put(ph.getObjectId(), ph);
			}
		}
		catch (Exception e)
		{
			LOG.warning("Could not restore character values: " + e.getMessage());
			e.printStackTrace();
		}
		
		LOG.info(PlayerData.class.getSimpleName() + " load " + _players.size() + " players from DB");
	}
	
	public static synchronized PlayerHolder get(L2PcInstance player)
	{
		return _players.get(player.getObjectId());
	}
	
	public static synchronized PlayerHolder get(int objectId)
	{
		return _players.get(objectId);
	}
	
	public static synchronized void add(int objectId, String name, String accountName)
	{
		_players.put(objectId, new PlayerHolder(objectId, name, accountName));
	}
	
	public static synchronized Collection<PlayerHolder> getAllPlayers()
	{
		return _players.values();
	}
}
