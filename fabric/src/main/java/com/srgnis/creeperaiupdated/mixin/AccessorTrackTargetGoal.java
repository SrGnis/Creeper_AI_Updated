package com.srgnis.creeperaiupdated.mixin;

import net.minecraft.entity.ai.goal.TrackTargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TrackTargetGoal.class)
public interface AccessorTrackTargetGoal {

    @Accessor("checkVisibility")
    public void setCheckVisibility(boolean bol);
}
