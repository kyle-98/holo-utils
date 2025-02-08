package com.poggers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.poggers.config.ModConfig;
import com.poggers.config.ModConfig.EspSettings.Friend;
import com.poggers.utils.ColorUtils;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void modifyPlayerList(DrawContext context, int screenWidth, Scoreboard scoreboard, ScoreboardObjective objective, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.getNetworkHandler() == null) return;

        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        for (PlayerListEntry entry : client.getNetworkHandler().getPlayerList()) {
            String playerName = entry.getProfile().getName();
            for(Friend friend : config.espSettings.getEspFriends()){
                if(playerName.equals(friend.name) && friend.enabled) {
                    Text coloredDisplayName = Text.literal(playerName).styled(style -> style.withColor(ColorUtils.parseHexColor(friend.color)));
                    entry.setDisplayName(coloredDisplayName);
                }
            }
        }
    }
}
