package com.l2jserver.datapack.custom.engine.holders;

import java.util.ArrayList;
import java.util.List;

import com.l2jserver.datapack.custom.engine.data.PlayerData;
import com.l2jserver.datapack.custom.engine.enums.TeamType;
import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class TeamHolder
{
	private List<L2PcInstance> _jugadores = new ArrayList<>();
	private int _puntos = 0;
	private TeamType _color = TeamType.NINGUNO;
	private String _condition = "";
	
	public void sumarPuntoPorKill()
	{
		if (_puntos < 25)
			_puntos += 1;
	}
	
	public int tomarPuntos()
	{
		return _puntos;
	}
	
	public void colocarColor(TeamType color)
	{
		_color = color;
	}
	
	public TeamType tomarColor()
	{
		return _color;
	}
	
	public void prepararJugador(L2PcInstance player, boolean saveColorName)
	{
		eliminarJugador(player);
		
		_jugadores.add(player);
		
		if (saveColorName)
			getPlayerHolder(player).saveColorName(player.getAppearance().getNameColor());
		
		player.getAppearance().setVisibleName(player.getClassId().name().toUpperCase());
		player.getAppearance().setNameColor(_color.tomarRojo(), _color.tomarVerde(), _color.tomarAzul());
		
		//nobles
		SkillData.getInstance().getSkill(1323, 1).applyEffects(player, player);
	}
	
	public void cleanPlayer (L2PcInstance player)
	{
		eliminarJugador(player);
		
		player.getAppearance().setVisibleName(player.getName());
		player.getAppearance().setNameColor(getPlayerHolder(player).getSaveColorName());
	}
	
	public boolean isTeamParticipant(L2PcInstance jugador)
	{
		if (_jugadores.contains(jugador))
		{
			return true;
		}
		return false;
	}
	
	public List<L2PcInstance> getParticipants()
	{
		return _jugadores;
	}
	
	public void eliminarJugador(L2PcInstance jugador)
	{
		if (_jugadores.contains(jugador))
		{
			_jugadores.remove(jugador);
		}
	}
	
	public int tomarCantidadDeJugadores()
	{
		return _jugadores.size();
	}
	
	public void setCondition(String condition)
	{
		_condition = condition;
	}
	
	public String getCondition()
	{
		return _condition;
	}
	
	public PlayerHolder getPlayerHolder(L2PcInstance player)
	{
		return PlayerData.get(player);
	}
}
