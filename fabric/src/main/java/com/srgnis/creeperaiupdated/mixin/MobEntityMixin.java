package com.srgnis.creeperaiupdated.mixin;

import com.srgnis.creeperaiupdated.goals.BreachCreeperIgniteGoal;
import com.srgnis.creeperaiupdated.goals.XrayFollowTargetGoal;
import com.srgnis.creeperaiupdated.CreeperAIUpdated;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PounceAtTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD"), method = "initialize")
    private void onInitialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityTag, CallbackInfoReturnable info){
        if(getType() == EntityType.CREEPER){
            Object object = this;
            CreeperEntity centity = ((CreeperEntity)object);
            if(getRandom().nextDouble() < CreeperAIUpdated.config.poweredChance  && spawnReason == SpawnReason.NATURAL){

                NbtCompound tag = new NbtCompound();
                centity.writeCustomDataToNbt(tag);
                tag.putBoolean("powered", true);
                centity.readCustomDataFromNbt(tag);
            }

            centity.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).setBaseValue(CreeperAIUpdated.config.followingRadius);

            GoalSelector targets = ((MobEntityAccessor) centity).getTargetSelector();
            Set<PrioritizedGoal> tGoals = ((GoalSelectorAccessor) targets).getGoals();

            targets.remove( ((PrioritizedGoal)tGoals.toArray()[0]).getGoal() );// removing player target
            targets.add(1, new XrayFollowTargetGoal(centity, PlayerEntity.class, false));

            GoalSelector goals = ((MobEntityAccessor) centity).getGoalSelector();
            Set<PrioritizedGoal> gGoals = ((GoalSelectorAccessor) goals).getGoals();

            if(CreeperAIUpdated.config.canBreach && (centity.getPos().getY() < CreeperAIUpdated.config.maxY && centity.getPos().getY() > CreeperAIUpdated.config.minY)) {

                goals.remove(((PrioritizedGoal) gGoals.toArray()[1]).getGoal()); // removing ignite goal (second goal added -> second goal in the array)
                goals.add(2, new BreachCreeperIgniteGoal(centity)); // adding the new Ignite Goal

            }

            if(CreeperAIUpdated.config.canLeap) {
                goals.add(3, new PounceAtTargetGoal(centity, 0.4F));
            }
        }
    }
}
