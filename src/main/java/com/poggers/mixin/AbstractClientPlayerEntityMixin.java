package com.poggers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.poggers.config.ModConfig;
import com.poggers.utils.CapeManager;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {
    @Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
    public void getSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {

        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity cpe = client.player;

        if(player.getUuid().equals(cpe.getUuid())){
            CapeManager.reloadCapeTexture(config.capesSettings.getCapePath());
            var current = cir.getReturnValue();
            if(CapeManager.getCapeTexture() == null){
                cir.setReturnValue(new SkinTextures(
                        current.texture(),     
                        current.textureUrl(), 
                        current.capeTexture(),             
                        current.elytraTexture(),
                        current.model(),
                        current.secure()
                ));
            } else {
                cir.setReturnValue(new SkinTextures(
                        current.texture(),     
                        current.textureUrl(), 
                        CapeManager.getCapeTexture(),             
                        current.elytraTexture(),
                        current.model(),
                        current.secure()
                ));
            }
        }

    }
}
