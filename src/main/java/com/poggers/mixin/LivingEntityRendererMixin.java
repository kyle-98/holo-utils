package com.poggers.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.poggers.config.ModConfig;
import com.poggers.config.ModConfig.EspSettings.Friend;
import me.shedaniel.autoconfig.AutoConfig;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin
{
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;hasOutline(Lnet/minecraft/entity/Entity;)Z"))
	public boolean forceHighlight(MinecraftClient client, Entity entity) {
		if(entity instanceof ClientPlayerEntity || entity instanceof OtherClientPlayerEntity) {
			ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
			Friend eF = config.espSettings.getEspFriendByName(entity.getName().getLiteralString());
			if(eF != null && eF.enabled) {
				return true;
			} else {
				return false;
			}
        } else {
            return client.hasOutline(entity);
        }
	}
}