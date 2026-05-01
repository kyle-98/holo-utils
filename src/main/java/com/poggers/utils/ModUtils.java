package com.poggers.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;

public class ModUtils {
    public static void toggleGamma(boolean enabled) {
        SimpleOption<Double> gamma = MinecraftClient.getInstance().options.getGamma();
        gamma.setValue(enabled ? 15.0 : 1.0);
    }
}
