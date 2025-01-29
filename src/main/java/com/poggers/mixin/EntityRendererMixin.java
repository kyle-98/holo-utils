package com.poggers.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.poggers.config.ModConfig;

import me.shedaniel.autoconfig.AutoConfig;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin
{
    @SuppressWarnings("unused")
    private Entity currentEntity;

    @Inject(method = "renderLabelIfPresent", at = @At(value = "HEAD"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void captureEntity(Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta, CallbackInfo ci) {
        if(entity instanceof ClientPlayerEntity || entity instanceof OtherClientPlayerEntity) {
            this.currentEntity = entity;
        }
    }

    @SuppressWarnings("rawtypes")
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;hasLabel(Lnet/minecraft/entity/Entity;)Z"))
    public boolean renderNameTag(EntityRenderer renderer, Entity entity) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        if(entity instanceof ClientPlayerEntity || entity instanceof OtherClientPlayerEntity) {
            if (config.espSettings.shouldRenderEspFriend(entity.getName().getLiteralString())){
                return true;
            } else {
                if(entity instanceof ClientPlayerEntity){
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Redirect(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaky()Z"))
    private boolean redirectIsSneaky(Entity entity) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        if(config.espSettings.shouldRenderEspFriend(entity.getName().getLiteralString()))
        {
            return false;
        }
        return entity.isSneaky();
    }

}