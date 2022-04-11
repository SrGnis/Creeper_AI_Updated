package com.srgnis.creeperaiupdated;

import java.lang.reflect.Field;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.srgnis.creeperaiupdated.ai.UpdatedCreeperSwellGoal;
import com.srgnis.creeperaiupdated.ai.UpdatedNearestAttackableTargetGoal;
import com.srgnis.creeperaiupdated.config.Config;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.SpecialSpawn;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraft.world.level.Explosion;
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
		if(event.getWorld().isClientSide())
		{
			return;
		}
		
		Entity entity = event.getEntity();
		if (entity instanceof Creeper ) { // is a creeper

			Creeper centity = ((Creeper) entity); // cast to Creeper
			try 
			{

				if(Math.random() < Config.COMMON.powered_prob.get())
				{
					Field f_POWERED = Creeper.class.getDeclaredField("f_32274_"); // getting the field DATA_IS_POWERED/f_32274_ (when debugging change the name to "DATA_IS_POWERED")
					f_POWERED.setAccessible(true); // set the field POWERED accessible from here
					EntityDataAccessor<Boolean> POWERED = (EntityDataAccessor<Boolean>)f_POWERED.get(centity); // getting the value of the POWERED field
					centity.getEntityData().set(POWERED, true); // setting POWERED to true
				}
				
				Field f_goals = GoalSelector.class.getDeclaredField("f_25345_"); // getting the field availableGoals/f_25345_ (when debugging change the name to "availableGoals")
				f_goals.setAccessible(true);
				
				Set<WrappedGoal> targets = (Set<WrappedGoal>)f_goals.get(centity.targetSelector);
				Set<WrappedGoal> goals = (Set<WrappedGoal>)f_goals.get(centity.goalSelector);

				centity.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(Config.COMMON.follow_range.get()); // increase the follow range and awareness
				
				centity.targetSelector.removeGoal( ((WrappedGoal)targets.toArray()[0]).getGoal() );// removing player target
				centity.targetSelector.addGoal(1, new UpdatedNearestAttackableTargetGoal<>(centity, Player.class, false)); // adding the goal of targeting players using xray
				
				if(Config.COMMON.can_break_walls.get() && Config.COMMON.maxLayer.get() >= centity.xOld && Config.COMMON.minLayer.get() <= centity.yOld)
				{
					centity.goalSelector.removeGoal( ((WrappedGoal)goals.toArray()[1]).getGoal() ); // removing swell goal (second goal added -> second goal in the array)
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
		
		if(Config.COMMON.fire_explosions.get() && event.getExplosion().getExploder() instanceof Creeper)
		{
			try {
			
				Field f_causesFire = Explosion.class.getDeclaredField("f_46009_"); // getting the field fire/f_46009_ (when debugging change the name to "fire")
				f_causesFire.setAccessible(true);
				f_causesFire.set(event.getExplosion(), true);
				
			} catch (Exception e) {
				LOGGER.debug(e.getMessage());
			}
		}
	}
}


