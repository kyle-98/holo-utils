package com.poggers.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.serialization.Codec;

import java.util.Optional;

@Mixin(SimpleOption.class)
public class OverrideIllegalMixin<T> {
    @Unique
    private boolean isGammaOption(){
        Text text = ((SimpleOption<?>)(Object)this).text;
        return text.getContent() instanceof TranslatableTextContent translatable && "options.gamma".equals(translatable.getKey());
    }

    @Inject(method = "getCodec", at = @At("HEAD"), cancellable = true)
    private void returnFakeCodec(CallbackInfoReturnable<Codec<Double>> info) {
        if (isGammaOption()) info.setReturnValue(Codec.DOUBLE);
    }

    @WrapOperation(method = "setValue", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/SimpleOption$Callbacks;validate(Ljava/lang/Object;)Ljava/util/Optional;"))
    private Optional<T> cancelValidation(SimpleOption.Callbacks<T> instance, T t, Operation<Optional<T>> original) {
        return isGammaOption() ? Optional.of(t) : original.call(instance, t);
    }
}