package com.l2jserver.datapack.custom.enginemods.util;

import com.l2jserver.datapack.handlers.effecthandlers.instant.SummonCubic;
import com.l2jserver.gameserver.model.effects.AbstractEffect;
import com.l2jserver.gameserver.model.skills.EffectScope;
import com.l2jserver.gameserver.model.skills.Skill;

public class UtilBuffs
{	
	/**
	 * Get the summon cubic effect class
	 * @param skill : the skill to check
	 * @return the summon cubic skill class
	 */
	public static SummonCubic getSummonCubicEffect(Skill skill)
	{
		AbstractEffect abstractEffect = skill.getEffects(EffectScope.GENERAL).get(0);
		
		System.out.println(abstractEffect.getName());
		
		if (abstractEffect instanceof SummonCubic)
		{
			return (SummonCubic) abstractEffect;
		}
		return null;
	}
}
