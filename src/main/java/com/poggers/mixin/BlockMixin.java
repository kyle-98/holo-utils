package com.poggers.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.poggers.config.ModConfig;
import com.poggers.utils.XrayUtils;

import me.shedaniel.autoconfig.AutoConfig;

@Mixin(value = Block.class)
public class BlockMixin {
        if (XrayUtils.getXrayState()) {
            if (config.xraySettings.getXrayBlocks().contains(state.getBlock())) {
                ci.setReturnValue(true);
            } else {
                ci.setReturnValue(false);
            }
        }
    }

    private BlockMixin() {
    }
}
