package com.poggers.mixin;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.SkyRendering;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.BufferAllocator;

@Mixin(SkyRendering.class)
public abstract class SkyRenderingMixin implements SkyRenderingAccessor{
    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/GpuDevice;createBuffer(Ljava/util/function/Supplier;ILjava/nio/ByteBuffer;)Lcom/mojang/blaze3d/buffers/GpuBuffer;",
            ordinal = 0
        )
    )
    private GpuBuffer redirectTopSkyBuffer(GpuDevice device, Supplier<String> labelGetter, int usage, ByteBuffer data) {
        SkyRenderingAccessor self = (SkyRenderingAccessor) (Object) this;
        try (BufferAllocator bufferAllocator = new BufferAllocator(10 * VertexFormats.POSITION.getVertexSize())) {
            BufferBuilder bufferBuilder = new BufferBuilder(bufferAllocator, VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION);
            self.invokeCreateSky(bufferBuilder, -1000.0F);
            try (BuiltBuffer builtBuffer = bufferBuilder.end()) {
                return RenderSystem.getDevice()
                    .createBuffer(() -> "Top sky vertex buffer", 32, builtBuffer.getBuffer());
            }
        }
    }

    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/GpuDevice;createBuffer(Ljava/util/function/Supplier;ILjava/nio/ByteBuffer;)Lcom/mojang/blaze3d/buffers/GpuBuffer;",
            ordinal = 1
        )
    )
    private GpuBuffer redirectBottomSkyBuffer(GpuDevice device, Supplier<String> labelGetter, int usage, ByteBuffer data) {
        SkyRenderingAccessor self = (SkyRenderingAccessor) (Object) this;
        try (BufferAllocator bufferAllocator = new BufferAllocator(10 * VertexFormats.POSITION.getVertexSize())) {
            BufferBuilder bufferBuilder = new BufferBuilder(bufferAllocator, VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION);
            self.invokeCreateSky(bufferBuilder, -1000.0F);
            try (BuiltBuffer builtBuffer = bufferBuilder.end()) {
                return RenderSystem.getDevice()
                    .createBuffer(() -> "Bottom sky vertex buffer", 32, builtBuffer.getBuffer());
            }
        }
    }
}