package com.srgnis.creeperaiupdated.mixin;

import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ActiveTargetGoal.class)
public interface ActiveTargetGoalAccessor {

    @Accessor("targetPredicate")
    public TargetPredicate getTargetPredicate();
}
