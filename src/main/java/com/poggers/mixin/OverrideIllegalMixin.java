package com.poggers.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionInstance.class)
public class OverrideIllegalMixin<T> {
    @Shadow
    T value;

    @Shadow
    @Final
    Component caption;

    @Inject(method = "set", at = @At("HEAD"), cancellable = true)
    private void overrideSet(T newValue, CallbackInfo ci){
        Minecraft mc = Minecraft.getInstance();

        if (mc.options != null){
            Options options = mc.options;

            if((Object)this == options.gamma()) {
                this.value = newValue;
                ci.cancel();
            }
        }
    }
}