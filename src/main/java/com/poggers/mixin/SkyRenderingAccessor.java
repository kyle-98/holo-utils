package com.poggers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.render.SkyRendering;
import net.minecraft.client.render.VertexConsumer;

@Mixin(SkyRendering.class)
public interface SkyRenderingAccessor {
    @Invoker("tessellateSky")
    void invokeTessellateSky(VertexConsumer vertexConsumer, float height);
}