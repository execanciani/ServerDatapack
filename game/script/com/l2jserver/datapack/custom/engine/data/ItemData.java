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
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.l2jserver.datapack.custom.engine.enums.ItemType;
import com.l2jserver.datapack.custom.engine.holders.ItemHolder;

/**
 * @author fissban
 */
public class ItemData
{
	private static final List<ItemHolder> _generalItems = new ArrayList<>();

	
	public static void load()
	{
		// prevenimos datos duplicados en cado de recargar este metodo
		_generalItems.clear();

		
		loadItems();
	}
	
	private static void loadItems()
	{
		try
		{
			File f = new File("./data/engine/items/generalItems.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			Document doc = factory.newDocumentBuilder().parse(f);
			
			Node n = doc.getFirstChild();
			for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
			{
				if (d.getNodeName().equalsIgnoreCase("item"))
				{
					NamedNodeMap attrs = d.getAttributes();
					
					ItemType type = ItemType.valueOf(attrs.getNamedItem("type").getNodeValue());
					int id = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
					String name = attrs.getNamedItem("name").getNodeValue();
					
					//_generalItems.add(new ItemHolder(type, id, name));
					_generalItems.add(new ItemHolder(id, 1));
				}
			}
			
			System.out.println("ItemData: Load " + _generalItems.size() + " buffs.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static List<ItemHolder> getAllGeneralItems()
	{
		return _generalItems;
	}
}
