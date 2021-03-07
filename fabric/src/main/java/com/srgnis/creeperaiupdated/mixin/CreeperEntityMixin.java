package com.srgnis.creeperaiupdated.mixin;

import com.srgnis.creeperaiupdated.CreeperAIUpdated;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreeperEntity.class)
public abstract class CreeperEntityMixin extends HostileEntity {

    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "Lnet/minecraft/entity/mob/CreeperEntity;explode()V", at = @At( value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/explosion/Explosion$DestructionType;)Lnet/minecraft/world/explosion/Explosion;" ))
    private Explosion redirectCreateExplosion(World world, Entity entity, double x, double y, double z, float power, Explosion.DestructionType destructionType){
        if(CreeperAIUpdated.config.fireExplosions) {
            return world.createExplosion(entity, x, y, z, power, true, destructionType);
        } else{
            return world.createExplosion(entity, x, y, z, power, false, destructionType);
        }
    }
}
