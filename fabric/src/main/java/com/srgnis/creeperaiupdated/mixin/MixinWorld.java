package com.srgnis.creeperaiupdated.mixin;

import com.srgnis.creeperaiupdated.CreeperAIUpdated;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(World.class)
public abstract class MixinWorld {

    @ModifyArgs(method = "createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/explosion/Explosion$DestructionType;)Lnet/minecraft/world/explosion/Explosion;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;)Lnet/minecraft/world/explosion/Explosion;"))
    private void causesFire(Args args){
        Entity ent = args.get(0);
        if(ent != null && ent.getType() == EntityType.CREEPER){
            args.set(7, CreeperAIUpdated.config.fireExplosions);
        }
    }
}
