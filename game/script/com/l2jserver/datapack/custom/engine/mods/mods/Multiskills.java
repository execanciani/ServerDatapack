package main.engine.mods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import main.data.ConfigData;
import main.data.SkillData;
import main.engine.AbstractMods;
import main.holders.SkillHolder;
import net.sf.l2j.commons.pool.ConnectionPool;
import net.sf.l2j.commons.pool.ThreadPool;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.xml.PlayerData;
import net.sf.l2j.gameserver.enums.ShortcutType;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.Shortcut;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.PlayerTemplate;
import net.sf.l2j.gameserver.model.holder.skillnode.GeneralSkillNode;
import net.sf.l2j.gameserver.network.serverpackets.ShortCutRegister;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Multiskills extends AbstractMods
{
	private static final String RESTORE_SKILLS_FOR_CHAR = "SELECT skill_id,skill_level FROM character_skills WHERE char_obj_id=?";
	
	private static final long _seconds = 10000;
	
	private static Map<Integer, SkillHolder> _skills = new HashMap<>();
	
	public Multiskills()
	{
		registerMod(ConfigData.ENABLE_Multiskill);
	}
	
	@Override
	public void onModState() 
	{
	
	}
	
	@Override
	public boolean onRestoreSkills(Player player)
	{
		Map<Integer, Integer> skills = new HashMap<>();
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(RESTORE_SKILLS_FOR_CHAR))
		{
			// Retrieve all skills of this Player from the database
			ps.setInt(1, player.getObjectId());
			
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					final int id = rs.getInt("skill_id");
					final int level = rs.getInt("skill_level");
					
					// fake skills for base stats
					if (id > 9000)
					{
						continue;
					}					

					final L2Skill skill = SkillTable.getInstance().getInfo(id, level);
						
					if (skill == null)
					{
						LOG.log(Level.SEVERE, "Skipped null skill Id: " + id + ", Level: " + level + " while restoring player skills for " + player.getName());
						continue;
					}
						
					if (!ConfigData.MULTISKILL_ACUMULATIVE_PASIVE_SKILLS)
					{
						if (skill.isPassive())
						{
							continue;
						}
					}
						
					if (ConfigData.DONT_ACUMULATIVE_SKILLS_ID.contains(id))
					{
						continue;
					}	
						
					//}
					
					// We save all the skills that we will teach our character.
					// This will avoid teaching a skill from lvl 1 to 15 for example
					// And directly we teach the lvl 15 =)
					skills.put(id, level);
				}
			}
		} 
		catch (Exception e)
		{
			LOG.log(Level.SEVERE, "Could not restore " + player.getName() + " skills:", e);
		}
		
		
		
		//skills = skills guardados;  
		/*for (Entry<Integer, Integer> allSkills : getAvailableSkills(skills).entrySet())
		{
			int id = map.getKey(); int level = map.getValue();
			
			final L2Skill skill = SkillTable.getInstance().getInfo(id, level);
			
			if (skill == null)
			{
				LOG.log(Level.SEVERE, "Skipped null skill Id: " + id + ", Level: " + level + " while restoring player skills for " + player.getName());
				continue;
			}
			
			Skill Holder sh = Skill Data.get (skill.get id());
			sh.setReadyForUse(true);
			
			_skills.put(sh.getId(), sh);*/
			player.addSkill(sh.getL2Skill(),true);
		}
		
		return true;
	}
	
	@Override
	public boolean onShortCutReg(int type, int id, int slot, int page, int characterType, Player player)
	{
		final ShortcutType t = ShortcutType.VALUES[type];
		
		switch (t)
		{
			case ITEM:
			case ACTION:
			case MACRO:
			case RECIPE:
				Shortcut shortcut = new Shortcut(slot, page, t, id, -1, characterType);
				player.sendPacket(new ShortCutRegister(player, shortcut));
				player.getShortcutList().addShortcut(shortcut);
				break;
			
			case SKILL:
				final int level = player.getSkillLevel(id);
				if (level > 0)
				{
					if (CheckShortCut(player))
					{
						shortcut = new Shortcut(slot, page, t, id, level, characterType);
						player.sendPacket(new ShortCutRegister(player, shortcut));
						player.getShortcutList().addShortcut(shortcut);
						
						SkillHolder sh = _skills.get(id);
						sh.setReadyForUse(false);
						sh.setSeconds(_seconds);
						
						ThreadPool.schedule(new Runnable()
						{
							@Override
							public void run()
							{
								SkillData.get(id).setReadyForUse(true);
							}
						}, _seconds);
					}
				}
				break;
		default:
			break;
		}
		return true;
	}
	
	@Override
	public boolean onMagicSkillUse(Player player, Creature finalTarget, L2Skill skill, boolean _ctrlPressed, boolean _shiftPressed) 
	{
		if (isInShortcut(player, skill))
		{
			SkillHolder sh = _skills.get(skill.getId());
			if (sh.isReadyForUse())
			{
				player.getAI().tryToCast(finalTarget, skill, _ctrlPressed, _shiftPressed, 0);
			}
			else
			{
				player.sendMessage("Skill not ready for use");
				//player.sendMessage("You have to wait " +sh.getSeconds());
			}
			
			return true;	
		}
		
		player.sendMessage("You cant use this skill");
		return true;
	}
		
	
	private Map<Integer, Integer> GetAvailableSkills(Map<Integer, Integer> skillMap)
	{
		Map<Integer, Integer> clasesSkills = new HashMap<>();
		
		for (ClassId classId : ClassId.VALUES)
		{
			if (classId == null)
				continue;
			if (classId.toString().toUpperCase().startsWith("DUMMY"))
				continue;
			
			PlayerTemplate pt = PlayerData.getInstance().getTemplate(classId);
			
			if ( pt == null || pt.getSkills() == null)
				continue;
			
			for (GeneralSkillNode g : pt.getSkills())
			{				
				final int id = g.getId();
				int level = g.getValue();
				int maxLevel = SkillTable.getInstance().getMaxLevel(id);
				
				if (skillMap.keySet().contains(id))
				{
					continue;
				}
				
				if (level < maxLevel)
				{
					continue;
				}

				clasesSkills.put(id, level);
			}
		}
		
		for (Entry<Integer, Integer> map : skillMap.entrySet())
		{
			clasesSkills.put(map.getKey(),map.getValue());
		}
		
		return clasesSkills;
	}
	

	private boolean CheckShortCut(Player player, int slot)
	{
		int shortcuts = 0;
		
		for (Shortcut s : player.getShortcutList().getShortcuts())
		{
			if (s.getType() == ShortcutType.SKILL)
			{
				shortcuts++;
			}
			if (slot == )
		}
		if (shortcuts <= 24)
			return true;
		player.sendMessage("You have already 24 skills");
		return false;
	}
	
	private boolean IsInShortcut(Player player, L2Skill skill)
	{
		for (Shortcut s : player.getShortcutList().getShortcuts())
		{
			if (s.getType() == ShortcutType.SKILL)
			{
				if (s.getId() != skill.getId())
				{
					continue;
				}
				else
				{
					return true;
				}
			}
		}
		return false;
	}
}
