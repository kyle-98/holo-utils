package com.poggers.mixin;

import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.serialization.Codec;

@Mixin(SimpleOption.class)
public class OverrideIllegalMixin<T> {
    @Shadow
    T value;

    @Shadow
    @Final
    Text text;

    @Inject(method = "getCodec", at = @At("HEAD"), cancellable = true)
    private void returnFakeCodec(CallbackInfoReturnable<Codec<Double>> info) {
        if (text.getString().equals(I18n.translate("options.gamma"))) {
            info.setReturnValue(Codec.DOUBLE);
        }
    }

    @Inject(method = "setValue", at = @At("HEAD"), cancellable = true)
    private void setOverrideValue(T value, CallbackInfo info){
        this.value = value;
        info.cancel();
    }

}