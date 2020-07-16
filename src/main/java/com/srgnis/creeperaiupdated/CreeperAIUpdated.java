package com.srgnis.creeperaiupdated;

import java.lang.reflect.Field;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.srgnis.creeperaiupdated.ai.UpdatedCreeperSwellGoal;
import com.srgnis.creeperaiupdated.ai.UpdatedNearestAttackableTargetGoal;
import com.srgnis.creeperaiupdated.config.Config;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.SpecialSpawn;
import net.minecraftforge.event.world.ExplosionEvent;
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
					Field f_POWERED = CreeperEntity.class.getDeclaredField("field_184714_b"); // getting the field POWERED/field_184714_b (when debugging change the name to "POWERED")
					f_POWERED.setAccessible(true); // set the field POWERED accessible from here
					DataParameter<Boolean> POWERED = (DataParameter<Boolean>)f_POWERED.get(centity); // getting the value of the POWERED field
					centity.getDataManager().set(POWERED, true); // setting POWERED to true
				}
				
				Field f_goals = GoalSelector.class.getDeclaredField("field_220892_d"); // getting the field goals/field_220892_d (when debugging change the name to "goals")
				f_goals.setAccessible(true);
				
				Set<PrioritizedGoal> targets = (Set<PrioritizedGoal>)f_goals.get(centity.targetSelector);
				Set<PrioritizedGoal> goals = (Set<PrioritizedGoal>)f_goals.get(centity.goalSelector);
				
				//field_233819_b_ => Follow_Range
				centity.getAttribute(Attributes.field_233819_b_).setBaseValue(Config.COMMON.follow_range.get()); // increase the follow range and awareness
				
				centity.targetSelector.removeGoal( ((PrioritizedGoal)targets.toArray()[0]).getGoal() );// removing player target
				centity.targetSelector.addGoal(1, new UpdatedNearestAttackableTargetGoal<>(centity, PlayerEntity.class, false)); // adding the goal of targeting players using xray
				
				if(Config.COMMON.can_break_walls.get() && Config.COMMON.maxLayer.get() >= centity.prevPosY && Config.COMMON.minLayer.get() <= centity.prevPosY)
				{
					centity.goalSelector.removeGoal( ((PrioritizedGoal)goals.toArray()[1]).getGoal() ); // removing swell goal (second goal added -> second goal in the array)
					centity.goalSelector.addGoal(2, new UpdatedCreeperSwellGoal(centity)); // adding the new SwellGoal
				}
				
				if(Config.COMMON.can_leap.get()) 
				{
					centity.goalSelector.addGoal(3, new LeapAtTargetGoal(centity, 0.4F)); 
				}
				
			} catch (Exception e) {
				LOGGER.debug(e.getMessage());
			}
		}
	}
	
	@SubscribeEvent
	public void onExplosion(ExplosionEvent.Start event) // on a creeper explosion we allow it to cause fire
	{
		
		if(Config.COMMON.fire_explosions.get() && event.getExplosion().getExplosivePlacedBy() instanceof CreeperEntity) 
		{
			try {
			
				Field f_causesFire = Explosion.class.getDeclaredField("field_77286_a"); // getting the field causesFire/field_220892_d (when debugging change the name to "goals")
				f_causesFire.setAccessible(true);
				f_causesFire.set(event.getExplosion(), true);
				
			} catch (Exception e) {
				LOGGER.debug(e.getMessage());
			}
		}
	}
}


