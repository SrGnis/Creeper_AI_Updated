package com.srgnis.creeperaiupdated.mixin;

import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ActiveTargetGoal.class)
public interface AccessorActiveTargetGoal {

    @Accessor("targetPredicate")
    public TargetPredicate getTargetPredicate();
}
