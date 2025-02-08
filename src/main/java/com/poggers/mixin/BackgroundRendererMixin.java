package com.poggers.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FogShape;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.World;

import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.poggers.config.ModConfig;
import com.poggers.config.ModConfig.FogRemoval;

import me.shedaniel.autoconfig.AutoConfig;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    private static float fogStart = 1000.0F;
    private static float fogEnd = 1000.0F;


    @Inject(method = "applyFog", at = @At(value = "HEAD"), cancellable = true)
    private static void applyCustomFog(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta, CallbackInfoReturnable<Fog> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        FogShape fogShape = FogShape.SPHERE;

        if(config.visualSettings.getAllFogState() == FogRemoval.EVERYWHERE) {
            cir.setReturnValue(new Fog(fogStart, fogEnd, fogShape, color.x, color.y, color.z, color.w));
        }
        else if(config.visualSettings.getAllFogState() == FogRemoval.NETHER_ONLY && world != null && world.getRegistryKey() == World.NETHER){
            cir.setReturnValue(new Fog(fogStart, fogEnd, fogShape, color.x, color.y, color.z, color.w));
        }
    }
}
