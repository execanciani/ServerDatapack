package com.l2jserver.datapack.custom.enginemods.enums;

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


/**
 * @author fissban
 */
public enum TeamType
{
	NINGUNO(0,0,0),
	AZUL(0,0,255),
	ROJO(255,0,0);

	private int[] color = new int[3];
	
	TeamType(int rojo, int verde, int azul)
	{
		color[0] = rojo;
		color[1] = verde;
		color[2] = azul;
	}
	
	public int tomarRojo()
	{
		return color[0];
	}
	
	public int tomarVerde()
	{
		return color[1];
	}
	
	public int tomarAzul()
	{
		return color[2];
	}
}
