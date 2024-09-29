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

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.l2jserver.datapack.custom.engine.holders.SkillHolder;
import com.l2jserver.gameserver.engines.skills.DocumentSkill;
import com.l2jserver.gameserver.model.skills.Skill;

/**
 * @author fissban
 */
public class SkillData
{
	private static final Logger LOG = Logger.getLogger(SkillData.class.getName());
	
	private static final Map<String, String> SKILLS = new HashMap<>();
	
	private static final Map<Integer, SkillHolder> _skills = new ConcurrentHashMap<>();
	
	public static void load()
	{
		// prevenimos datos duplicados en cado de recargar este metodo
		SKILLS.clear();
		_skills.clear();
		
		try
		{
			final File dir = new File("./data/xml/skills");
			
			for (File file : dir.listFiles())
			{
				DocumentSkill doc = new DocumentSkill(file);
				doc.parse();
				
				for (Skill skill : doc.getSkills())
				{
					SkillHolder sh = new SkillHolder(skill.getId(),skill.getLevel());
					_skills.put(sh.getId(), sh);
				}			
			}
			
			/** TODO skillEngine
			File f = new File("./data/xml/modsSkill.xml");
			//Document doc = IXmlReader.getInstance().loadDocument(f);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			Document doc = factory.newDocumentBuilder().parse(f);
			
			Node n = doc.getFirstChild();
			for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
			{
				if (d.getNodeName().equalsIgnoreCase("skill"))
				{
					NamedNodeMap attrs = d.getAttributes();
					
					String id = attrs.getNamedItem("id").getNodeValue();
					LOG.info("id: " +id);
					String level = attrs.getNamedItem("level").getNodeValue();
					String description = attrs.getNamedItem("description").getNodeValue();
					SKILLS.put(id + " " + level, description);
					
					LOG.info("skills: " +id + " " + level);
					
					if (id.contains("?"))
						id = id.replace("?", "");
					
					LOG.info("skills: nId " +id);
					
					SkillHolder sh = new SkillHolder(Integer.parseInt(id),Integer.parseInt(level));
					_skills.put(sh.getId(), sh);

				}
			}
			*/
			
			LOG.info(SkillData.class.getSimpleName() + " load " + _skills.size() + " skills data.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static synchronized SkillHolder get(int id)
	{
		return _skills.get(id);
	}

	public static synchronized Collection<SkillHolder> getAllSkills()
	{
		return _skills.values();
	}
	
	public static String getDescription(int id, int lvl)
	{
		return SKILLS.get(id + " " + lvl);
	}
	
	public static String getSkillIcon(int id)
	{
		String formato;
		if (id == 4)
		{
			formato = "0004";
		}
		else if (id > 9 && id < 100)
		{
			formato = "00" + id;
		}
		else if (id > 99 && id < 1000)
		{
			formato = "0" + id;
		}
		else if (id == 1517)
		{
			formato = "1536";
		}
		else if (id == 1518)
		{
			formato = "1537";
		}
		else if (id == 1547)
		{
			formato = "0065";
		}
		else if (id == 2076)
		{
			formato = "0195";
		}
		else if (id > 4550 && id < 4555)
		{
			formato = "5739";
		}
		else if (id > 4698 && id < 4701)
		{
			formato = "1331";
		}
		else if (id > 4701 && id < 4704)
		{
			formato = "1332";
		}
		else if (id == 6049)
		{
			formato = "0094";
		}
		else
		{
			formato = String.valueOf(id);
		}
		return "Icon.skill" + formato;
	}
	
	public static Skill getL2Skill(int id, int level)
	{
		return SkillData.getL2Skill(id, level);
	}
}
