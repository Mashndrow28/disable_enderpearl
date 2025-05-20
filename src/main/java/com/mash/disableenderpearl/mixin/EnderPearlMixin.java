package com.mash.disableenderpearl.mixin;

import com.mash.disableenderpearl.EnderPearlConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.entity.Entity.RemovalReason;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderPearlEntity.class)
public class EnderPearlMixin {
    // 💥 Inject into collisions with entities
    @Inject(method = "onEntityHit", at = @At("HEAD"), cancellable = true)
    private void onEntityHit(EntityHitResult hitResult, CallbackInfo info) {
        handleTeleportCancel(info);
    }

    // 💥 Inject into collisions with blocks
    @Inject(method = "onCollision", at = @At("HEAD"), cancellable = true)
    private void onCollision(HitResult hitResult, CallbackInfo info) {
        handleTeleportCancel(info);
    }

    // ✅ Shared logic for both types of collision
    @Unique
    private void handleTeleportCancel(CallbackInfo info) {
        Entity owner = ((EnderPearlEntity)(Object)this).getOwner();
        if (owner instanceof PlayerEntity player) {
            RegistryKey<World> playerDim = player.getWorld().getRegistryKey();
            Identifier dimId = playerDim.getValue();

            if (EnderPearlConfig.blockedDimensions.contains(dimId)) {
                info.cancel();
                ((EnderPearlEntity)(Object)this).setRemoved(RemovalReason.DISCARDED);
            }
        }
    }
}