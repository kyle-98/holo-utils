package com.poggers.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;

public class ModUtils {
    public static void toggleGamma(boolean enabled) {
        OptionInstance<Double> gamma = Minecraft.getInstance().options.gamma();
        gamma.set(enabled ? 15.0 : 1.0);
    }
}
