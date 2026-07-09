package com.poggers.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class NotifyPlayer {
    public static void displayMessage(String message, boolean aboveHotBar){
        Minecraft mc = Minecraft.getInstance();
        assert mc.player != null;
        mc.gui.hud.setOverlayMessage(Component.literal(message).withStyle(ChatFormatting.WHITE), aboveHotBar);
    }
}
