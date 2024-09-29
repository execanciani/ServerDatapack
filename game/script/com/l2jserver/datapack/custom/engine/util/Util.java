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

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.l2jserver.gameserver.model.L2Object;

/**
 * @author fissban
 */
public class Util
{
	public static final String SEPARATOR = "-----------------------------------------------------------";
	
	/**
	 * Check if the objects belong to a particular instance.
	 * @param type
	 * @param objects
	 * @return
	 */
	public static <A> boolean areObjectType(Class<A> type, L2Object objects)
	{
		if (objects == null) //|| objects. <= 0)
		{
			return false;
		}
		
		//for (L2Object o : objects.)
		//{
			if (!type.isAssignableFrom(objects.getClass()))
			{
				return false;
			}
		//}
		return true;
	}
	
	public static boolean isNumber(String text)
	{
		try
		{
			Integer.parseInt(text);
			return true;
		}
		catch (NumberFormatException nfe)
		{
			return false;
		}
	}
	
	public static List<Integer> parseInt(String line, String split)
	{
		List<Integer> list = new ArrayList<>();
		
		for (String s : line.split(split))
		{
			list.add(Integer.parseInt(s));
		}
		
		return list;
	}
	
	/**
	 * Metodo para extraer cualquier tipo variable de un documento en JSON sin necesidad de librerias externas
	 * @param cadena Texto que devuelva la API de TopZone o Hopzone
	 * @param variable Valor que quieres obtener
	 * @return Valor de la variable que se define
	 */
	public static String getJsonVariable(String cadena, String variable)
	{
		cadena.replaceAll("\\}", "");
		String[] cadena_separada = cadena.split("\\{");
		for (String a : cadena_separada)
		{
			if (a.contains(variable))
			{
				String[] a_separada = a.split(",");
				for (String b : a_separada)
				{
					if (b.contains(variable))
					{
						String[] b_separada = b.split(":");
						return b_separada[1].replaceAll("\"", "");
					}
				}
			}
		}
		return "";
	}
	
	/**
	 * Metodo para saber si es valido bypass
	 */
	public static boolean checkBypass(String comando, String bypass)
	{
		String ncomando = comando.split(" ")[1];
		
		if (ncomando == null)
		{
			return false;	
		}
		
		if (ncomando.equals(bypass))
		{
			return true;
		}
		
		return false;
	}
	
	public static void print(Class<?> clase, Object... objects) 
	{
		String cadena = "";
		
		for (Object obj : objects)
		{
			cadena += obj.toString();
		}
		
		System.out.println("[ "+ clase.getSimpleName()+ "]" + " " + cadena);
	}
	
	public static void createFile(String textToWrite)
	{
		 try {
		      FileWriter myWriter = new FileWriter("locs.txt");
		      myWriter.write(textToWrite);
		      myWriter.close();
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}

}