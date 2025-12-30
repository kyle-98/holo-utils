package com.poggers.mixin;

import java.util.List;

import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.poggers.config.holoutils.ModConfig;
import com.poggers.config.holoutils.ModConfig.FogRemoval;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {

    @Shadow
    @Final
    private static List<FogModifier> FOG_MODIFIERS;

    @Inject(
        method = "applyFog(Lnet/minecraft/client/render/Camera;ILnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;", 
        at = @At(value = "FIELD", 
            target = "Lnet/minecraft/client/render/fog/FogData;renderDistanceEnd:F", 
            ordinal = 0, 
            shift = At.Shift.AFTER
        ), 
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void onApplyFog(
        Camera camera, 
        int renderDistance, 
        RenderTickCounter renderTickCounter, 
        float viewDistance, 
        ClientWorld world, 
        CallbackInfoReturnable<Vector4f> cir, 
        float g, 
        Vector4f vector4f, 
        float h, 
        CameraSubmersionType cameraSubmersionType, 
        Entity entity, 
        FogData fogData
    ) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        boolean removeFog = config.visualSettings.getAllFogState() == FogRemoval.EVERYWHERE
                || (config.visualSettings.getAllFogState() == FogRemoval.NETHER_ONLY
                    && world != null && world.getRegistryKey() == World.NETHER);

        if (removeFog) {
            for (int i = 0; i < FOG_MODIFIERS.size(); ++i) {
                if (FOG_MODIFIERS.get(i).shouldApply(cameraSubmersionType, entity)) {
                    fogData.environmentalStart = Float.MAX_VALUE;
                    fogData.environmentalEnd = Float.MAX_VALUE;
                    fogData.renderDistanceStart = Float.MAX_VALUE;
                    fogData.renderDistanceEnd = Float.MAX_VALUE;
                    fogData.skyEnd = viewDistance;
                    fogData.cloudEnd = MinecraftClient.getInstance().options.getCloudRenderDistance().getValue() * 16f;
                }
            }
        }
    }

    @ModifyConstant(
        method = "applyFog(Lnet/minecraft/client/render/Camera;ILnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;", 
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