package com.srgnis.creeperaiupdated.mixin;

import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(FollowTargetGoal.class)
public interface FollowTargetGoalAccessor {

    @Accessor("targetPredicate")
    public TargetPredicate getTargetPredicate();
}
