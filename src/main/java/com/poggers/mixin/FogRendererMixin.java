package com.poggers.mixin;

import com.poggers.config.holoutils.ModConfig;
import com.poggers.config.holoutils.ModConfig.FogRemoval;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Inject(
        method = "setupFog",
        at = @At("RETURN"),
        cancellable = true
    )
    private void onSetupFog(
            Camera camera,
            int renderDistanceInChunks,
            DeltaTracker deltaTracker,
            float darkenWorldAmount,
            ClientLevel level,
            CallbackInfoReturnable<FogData> cir
    ) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        boolean removeFog =
                config.visualSettings.getAllFogState() == FogRemoval.EVERYWHERE
                || (config.visualSettings.getAllFogState() == FogRemoval.NETHER_ONLY
                    && level.dimension() == Level.NETHER);

        if (!removeFog) return;

        FogData fog = cir.getReturnValue();

        if (fog == null) return;

        fog.renderDistanceStart = Float.MAX_VALUE;
        fog.renderDistanceEnd = Float.MAX_VALUE;

        fog.environmentalStart = Float.MAX_VALUE;
        fog.environmentalEnd = Float.MAX_VALUE;

        cir.setReturnValue(fog);
    }
}