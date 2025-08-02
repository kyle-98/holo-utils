package com.poggers.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.poggers.config.ModConfig;
import com.poggers.config.ModConfig.EspSettings.Friend;
import com.poggers.utils.ColorUtils;

import me.shedaniel.autoconfig.AutoConfig;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin
{
    @Inject(method = "renderEntity", at = @At("HEAD"))
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if(entity instanceof OtherClientPlayerEntity || entity instanceof ClientPlayerEntity){
            ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
            Friend eF = config.espSettings.getEspFriendByName(entity.getName().getLiteralString());
            if(eF == null){
                return;
            }
            if (config.espSettings.shouldRenderEspFriend(eF.name)){
                OutlineVertexConsumerProvider outlineVertexConsumers = (OutlineVertexConsumerProvider) vertexConsumers;
                int argbInt = ColorUtils.parseHexColor(eF.color);
                int red = (argbInt >> 16) & 0xFF;
                int green = (argbInt >> 8) & 0xFF;
                int blue = argbInt & 0xFF;
                outlineVertexConsumers.setColor(red, green, blue, 0xFF);
            } 
        }
    }
}