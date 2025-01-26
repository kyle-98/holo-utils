package com.poggers.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.poggers.config.ModConfig;
import com.poggers.config.ModConfig.EspSettings.Friend;

import me.shedaniel.autoconfig.AutoConfig;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
	// This tells the game that this is glowing, which is needed for entity culling compat
	@Inject(method = "hasOutline", at = @At(value = "HEAD"), cancellable = true)
	public void overrideHasOutline(Entity entity, CallbackInfoReturnable<Boolean> cir)
	{
		if(entity instanceof ClientPlayerEntity || entity instanceof OtherClientPlayerEntity) {
			ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
			Friend eF = config.espSettings.getEspFriendByName(entity.getName().getLiteralString());
			if(eF != null && eF.enabled){
				cir.setReturnValue(true);;
			} else{
				cir.setReturnValue(false);
			}
		}
	}
}