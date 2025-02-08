package com.poggers.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.poggers.config.ModConfig;
import com.poggers.utils.CapeManager;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {
    private static final UUID MY_UUID = UUID.fromString("3d5fde6a-a17b-4264-9770-c9fccbf997da");
    
    @Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
    public void getSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {

        //Identifier localCape = Identifier.of("holo-utils", "texture/my_cape.png");
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        if(player.getUuid().equals(MY_UUID)){
            CapeManager.reloadCapeTexture(config.capesSettings.getCapePath());
            var current = cir.getReturnValue();
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
