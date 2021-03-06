package com.srgnis.creeperaiupdated.ai;

import java.util.EnumSet;

import com.srgnis.creeperaiupdated.config.Config;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.pathfinding.Path;

public class UpdatedCreeperSwellGoal extends Goal {
   private final CreeperEntity creeper;
   private LivingEntity attackingEntity;
   private boolean normalExpl = false;
   
   public UpdatedCreeperSwellGoal(CreeperEntity entitycreeperIn) 
   {
      this.creeper = entitycreeperIn;
      this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
   }

   /**
    * Returns whether the EntityAIBase should begin execution.
    */
   public boolean shouldExecute() {
      LivingEntity livingentity = this.creeper.getAttackTarget();
      
      if(this.creeper.getCreeperState() > 0){
          return true;
      } else if(livingentity != null) {
          normalExpl = this.creeper.getDistanceSq(livingentity) < 9.0D;
          return (normalExpl || preBreakWall(livingentity));
      }
      return false;
   }
   
   private boolean preBreakWall(LivingEntity livingEntity){
       if (breakWall(livingEntity)){
           Path p = creeper.getNavigator().getPathToEntity(livingEntity, 0);
           if( p!=null && p.getCurrentPathLength() > 6 ){
               creeper.getNavigator().setPath(p, 1.0);
               return false;
           } else{
               return true;
           }
       } else {
           return false;
       }
   }

   public boolean breakWall(LivingEntity livingentity) 
   {
	   return creeper.ticksExisted > 60 && !creeper.hasPath() && creeper.getDistance(livingentity) < Config.COMMON.breach_range.get();
   }
   
   /**
    * Execute a one shot task or start executing a continuous task
    */
   public void startExecuting() 
   {
      this.creeper.getNavigator().clearPath();
      this.attackingEntity = this.creeper.getAttackTarget();
   }

   /**
    * Reset the task's internal state. Called when this task is interrupted by another one
    */
   public void resetTask() 
   {
      this.attackingEntity = null;
   }

   /**
    * Keep ticking a continuous task that has already been started
    */
   public void tick() 
    	{
	        if (this.attackingEntity == null)
	        {
	            this.creeper.setCreeperState(-1);
	        }
	        else if (this.normalExpl && this.creeper.getDistanceSq(this.attackingEntity) > 49.0D)
	        {
	            this.creeper.setCreeperState(-1);
	        }
	        else
	        {
	            this.creeper.setCreeperState(1);
	        }
	    }
}
