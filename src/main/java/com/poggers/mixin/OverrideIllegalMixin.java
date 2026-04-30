package com.poggers.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.serialization.Codec;

import java.util.Optional;

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

    @WrapOperation(method = "setValue", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/SimpleOption$Callbacks;validate(Ljava/lang/Object;)Ljava/util/Optional;"))
    private Optional<T> cancelValidation(SimpleOption.Callbacks instance, T t, Operation<Optional<T>> original) {
        return text.getString().equals(I18n.translate("options.gamma")) ? Optional.of(t) : original.call(instance, t);
    }

}