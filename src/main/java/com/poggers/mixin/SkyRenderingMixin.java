package com.poggers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.SkyRendering;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexFormat;

@Mixin(SkyRendering.class)
public abstract class SkyRenderingMixin implements SkyRenderingAccessor{
    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gl/VertexBuffer;createAndUpload(Lnet/minecraft/client/render/VertexFormat$DrawMode;Lnet/minecraft/client/render/VertexFormat;Ljava/util/function/Consumer;)Lnet/minecraft/client/gl/VertexBuffer;",
            ordinal = 1
        )
    )
    private VertexBuffer redirectSkyBufferCreateAndUpload(VertexFormat.DrawMode mode, VertexFormat format, java.util.function.Consumer<VertexConsumer> consumer) {
        SkyRenderingAccessor self = (SkyRenderingAccessor)(Object)this;
        return VertexBuffer.createAndUpload(mode, format, (vertexConsumer) -> {
            self.invokeTessellateSky(vertexConsumer, -1000.0F);
        });
    }

    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gl/VertexBuffer;createAndUpload(Lnet/minecraft/client/render/VertexFormat$DrawMode;Lnet/minecraft/client/render/VertexFormat;Ljava/util/function/Consumer;)Lnet/minecraft/client/gl/VertexBuffer;",
            ordinal = 2
        )
    )
    private VertexBuffer redirectDarkSkyBufferCreateAndUpload(VertexFormat.DrawMode mode, VertexFormat format, java.util.function.Consumer<VertexConsumer> consumer) {
        SkyRenderingAccessor self = (SkyRenderingAccessor)(Object)this;
        return VertexBuffer.createAndUpload(mode, format, (vertexConsumer) -> {
            self.invokeTessellateSky(vertexConsumer, -1000.0F);
        });
    }

}