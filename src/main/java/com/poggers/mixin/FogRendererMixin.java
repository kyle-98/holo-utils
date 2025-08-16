package com.poggers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.poggers.config.ModConfig;
import com.poggers.config.ModConfig.FogRemoval;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
    @WrapOperation(
        method = "applyFog(Lnet/minecraft/client/render/Camera;IZLnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/fog/FogModifier;applyStartEndModifier(Lnet/minecraft/client/render/fog/FogData;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/world/ClientWorld;FLnet/minecraft/client/render/RenderTickCounter;)V"
        )
    )
    public void applyFogOverride( FogModifier fogModifier, FogData fogData, Entity entity, BlockPos blockPos, ClientWorld clientWorld, float viewDistance, RenderTickCounter renderTickCounter, Operation<Void> originalOperation ) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;
        viewDistance /= 2;

        boolean removeFog = config.visualSettings.getAllFogState() == FogRemoval.EVERYWHERE
                || (config.visualSettings.getAllFogState() == FogRemoval.NETHER_ONLY
                    && world != null && world.getRegistryKey() == World.NETHER);

        if (removeFog) {
            fogData.environmentalStart = Float.MAX_VALUE;
            fogData.environmentalEnd = Float.MAX_VALUE;
            fogData.skyEnd = viewDistance;
            fogData.cloudEnd = client.options.getCloudRenderDistance().getValue() * 16;
        } else {
            originalOperation.call(fogModifier, fogData, entity, blockPos, clientWorld, viewDistance, renderTickCounter);
        }
    }

    @ModifyConstant(
        method = "applyFog(Lnet/minecraft/client/render/Camera;IZLnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;", 
        constant = @Constant(intValue = 16)
    )
    private int disableDistanceFog(int originalValue) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;

        boolean removeFog = config.visualSettings.getAllFogState() == FogRemoval.EVERYWHERE
                || (config.visualSettings.getAllFogState() == FogRemoval.NETHER_ONLY
                    && world != null && world.getRegistryKey() == World.NETHER);
        if (removeFog) {
            originalValue *= 2;
        }
        return originalValue;
    }
   
}