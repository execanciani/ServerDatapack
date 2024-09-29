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
package com.l2jserver.datapack.custom.enginemods.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.l2jserver.commons.database.ConnectionFactory;
import com.l2jserver.datapack.custom.enginemods.holders.ValuesHolder;
import com.l2jserver.datapack.custom.enginemods.mods.AbstractMods;


/**
 * Class responsible for keeping the information stored in the DB of all mods
 * @author fissban
 */
public class ModsData
{
	private static final Logger LOG = Logger.getLogger(ModsData.class.getName());
	
	private static final String UPDATE_DB = "UPDATE engine SET val=? WHERE event=? AND modName=? AND charId=?";
	private static final String INSERT_DB = "INSERT INTO engine (val,event,modName,charId) VALUES (?,?,?,?)";
	private static final String SELECT_DB = "SELECT charId,val,event,modName FROM engine";
	private static final String DELETE_DB_1 = "DELETE FROM engine WHERE modName=? AND event=? AND charId=?";
	private static final String DELETE_DB_2 = "DELETE FROM engine WHERE modName=?";
	private static final String DELETE_DB_3 = "DELETE FROM engine WHERE charId=?";

	
	// Map with all mods values.
	private static final Map<Integer, List<ValuesHolder>> _playersValuesDb = new ConcurrentHashMap<>();
	
	public ModsData()
	{
		// TODO Auto-generated constructor stub
	}
	
	public static void remove(AbstractMods mod)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(DELETE_DB_2))
		{
			statement.setString(1, mod.getClass().getSimpleName());
			statement.execute();
			
			removeAllPlayersModsValues(mod);
		}
		catch (Exception e)
		{
			LOG.warning("Can't delete: mod:" + mod.getClass().getSimpleName() + " " + e);
			e.printStackTrace();
		}
	}
	
	public static void remove(int objectId)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(DELETE_DB_3))
		{
			statement.setInt(1, objectId);
			statement.execute();
			
			removePlayerByObjectIdModsValues(objectId);
		}
		catch (Exception e)
		{
			LOG.warning("Can't delete: objId:" + objectId + " " + e);
			e.printStackTrace();
		}
	}
	
	public static void remove(int objectId, String event, AbstractMods mod)
	{
		try (Connection con = ConnectionFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(DELETE_DB_1))
		{
			statement.setString(1, mod.getClass().getSimpleName());
			statement.setString(2, event);
			statement.setInt(3, objectId);
			statement.execute();
			
			_playersValuesDb.get(objectId).remove(getPlayerValueHolder(objectId, event, mod));
		}
		catch (Exception e)
		{
			LOG.warning("Can't delete event:" + event + " mod:" + mod.getClass().getSimpleName() + " player objectId :" + objectId + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Remove the value holder mod
	 * @param mod : the mod to remove
	 */
	private static void removeAllPlayersModsValues(AbstractMods mod)
	{		
		for (Entry<Integer, List<ValuesHolder>> pvh : _playersValuesDb.entrySet())
		{
			for (ValuesHolder vh : pvh.getValue())
			{
				if (vh.getMod().equals(mod.getClass().getSimpleName()))
				{
					_playersValuesDb.get(pvh.getKey()).remove(vh);
				}
			}
		}
	}
	
	/**
	 * Remove the value holder of the player
	 * @param objId : the object id to remove
	 */
	private static void removePlayerByObjectIdModsValues(int objectId)
	{		
		for (int pvh : _playersValuesDb.keySet())
		{
			if (pvh == objectId)
			{
				_playersValuesDb.remove(pvh);
			}
		}
	}
	
	/**
	 * Get the value holder of the player
	 * @param objectId : the objId of the player
	 * @param event : the event name
	 * @param mod : the mod name
	 * @return the value holder
	 */
	private static ValuesHolder getPlayerValueHolder(int objectId, String event, AbstractMods mod)
	{
		if (_playersValuesDb.containsKey(objectId))
		{
			for (ValuesHolder vh : _playersValuesDb.get(objectId))
			{
				// we seek only the values of the mod in question
				if (vh.getMod().equals(mod.getClass().getSimpleName()))
				{
					// We look for the event in question
					if (vh.getEvent().equals(event))
					{
						return vh;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Obtenemos el valor de un player en determinado evento
	 * @param objectId
	 * @param event
	 * @param mod
	 * @return
	 */
	public static String get(int objectId, String event, AbstractMods mod)
	{
		ValuesHolder vh = getPlayerValueHolder(objectId, event, mod);
		if (vh != null)
		{
			return vh.getValue();
		}
		
		return null;
	}
	
	public static void set(int objectId, String event, String value, AbstractMods mod)
	{
		String modName = mod.getClass().getSimpleName();
		boolean updateInfo = false;
		// memory check.
		if (_playersValuesDb.containsKey(objectId))
		{
			for (ValuesHolder vh : _playersValuesDb.get(objectId))
			{
				if (vh.getEvent().equals(event) && vh.getMod().equals(modName))
				{
					// It requires updating the DB
					updateInfo = true;
					// update the value in memory.
					vh.setValue(value);
				}
			}
		}
		else
		{
			// It initializes the list of values of this character
			_playersValuesDb.put(objectId, new ArrayList<>());
		}
		
		if (!updateInfo)
		{
			// information saved in memory
			_playersValuesDb.get(objectId).add(new ValuesHolder(modName, event, value));
		}
		
		// update or insert values.
		try (Connection con = ConnectionFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(updateInfo ? UPDATE_DB : INSERT_DB))
		{
			statement.setString(1, value);
			statement.setString(2, event);
			statement.setString(3, modName);
			statement.setInt(4, objectId);
			statement.execute();
		}
		catch (Exception e)
		{
			LOG.warning("Can't " + (updateInfo ? "update " : "insert ") + event + " to DB " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void load()
	{
		// prevenimos datos duplicados en cado de recargar este metodo
		_playersValuesDb.clear();
		
		try (Connection con = ConnectionFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(SELECT_DB);
			ResultSet rset = statement.executeQuery())
		{
			while (rset.next())
			{
				int objId = rset.getInt("charId");
				String value = rset.getString("val");
				String event = rset.getString("event");
				String mod = rset.getString("modName");
				
				if (!_playersValuesDb.containsKey(objId))
				{
					_playersValuesDb.put(objId, new ArrayList<>());
				}
				
				_playersValuesDb.get(objId).add(new ValuesHolder(mod, event, value));
			}
		}
		catch (Exception e)
		{
			LOG.warning("Can't load values from DB" + e.getMessage());
			e.printStackTrace();
		}
		
		LOG.info(ModsData.class.getSimpleName() + " load " + _playersValuesDb.size() + " values from players.");
	}
}
