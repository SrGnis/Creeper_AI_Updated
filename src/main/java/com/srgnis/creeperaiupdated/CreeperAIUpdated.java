package com.srgnis.creeperaiupdated;

import java.lang.reflect.Field;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.srgnis.creeperaiupdated.ai.UpdatedCreeperSwellGoal;
import com.srgnis.creeperaiupdated.ai.UpdatedNearestAttackableTargetGoal;
import com.srgnis.creeperaiupdated.config.Config;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.SpecialSpawn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(CreeperAIUpdated.MOD_ID)
@SuppressWarnings("unchecked")
public class CreeperAIUpdated
{
	public static CreeperAIUpdated instance;
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "creeperaiupdated";
 
	public CreeperAIUpdated() 
	{	 
		instance = this;
		
		Config.register();

		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onEntitySpawn(SpecialSpawn event)
	{
		if(event.getWorld().isRemote()) 
		{
			return;
		}
		
		Entity entity = event.getEntity();
		if (entity instanceof CreeperEntity ) { // is a creeper
			
			CreeperEntity centity = ((CreeperEntity) entity); // cast to CreeperEntity

			try 
			{
				if(Math.random() < Config.COMMON.powered_prob.get())
				{
					Field f_POWERED = CreeperEntity.class.getDeclaredField("field_184714_b"); // getting the field POWERED (when debugging change the name to "POWERED")
					f_POWERED.setAccessible(true); // set the field POWERED accessible from here
					DataParameter<Boolean> POWERED = (DataParameter<Boolean>)f_POWERED.get(centity); // getting the value of the POWERED field
					centity.getDataManager().set(POWERED, true); // setting POWERED to true
				}
				
				Field f_goals = GoalSelector.class.getDeclaredField("field_220892_d"); // getting the field goals (when debugging change the name to "goals")
				f_goals.setAccessible(true);
				
				Set<PrioritizedGoal> targets = (Set<PrioritizedGoal>)f_goals.get(centity.targetSelector);
				Set<PrioritizedGoal> goals = (Set<PrioritizedGoal>)f_goals.get(centity.goalSelector);
				
				centity.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(Config.COMMON.follow_range.get()); // increase the follow range and awareness
				
				centity.targetSelector.removeGoal( ((PrioritizedGoal)targets.toArray()[0]).getGoal() );// removing player target
				centity.targetSelector.addGoal(1, new UpdatedNearestAttackableTargetGoal<>(centity, PlayerEntity.class, false)); // adding the goal of targeting players using xray
				
				if(Config.COMMON.can_break_walls.get())
				{
					centity.goalSelector.removeGoal( ((PrioritizedGoal)goals.toArray()[1]).getGoal() ); // removing swell goal (second goal added -> second goal in the array)
					centity.goalSelector.addGoal(2, new UpdatedCreeperSwellGoal(centity)); // adding the new SwellGoal
				}
				
			} catch (Exception e) {
				LOGGER.debug(e.getMessage());
			}
		}
	}
}