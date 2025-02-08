package com.poggers.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.poggers.config.ModConfig;
import com.poggers.config.ModConfig.EspSettings.Friend;
import com.poggers.utils.ColorUtils;

import me.shedaniel.autoconfig.AutoConfig;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin
{
	//	changes which color the entitiy should be highlighted in.
	// @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getTeamColorValue()I"))
	// private int forceHighlightColor(Entity entity)
	// {
    //     if(entity instanceof OtherClientPlayerEntity || entity instanceof ClientPlayerEntity){
    //         ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    //         Friend eF = config.espSettings.getEspFriendByName(entity.getName().getLiteralString());
    //         if (config.espSettings.shouldRenderEspFriend(eF.name)){
    //             return ColorUtils.parseHexColor(eF.color);
    //         } else {
    //             return 0xFFFFFF;
    //         }
    //     }
	// 	return 0xFFFFFF;
	// }
}