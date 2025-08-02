package com.poggers.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

public class CapeManager {
    private static Identifier currentCapeID = null;

    public static Identifier getCapeTexture() {
        return currentCapeID;
    }

    public static void reloadCapeTexture(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            currentCapeID = null;
            return;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            currentCapeID = null;
            return;
        }

        try {
            NativeImage image = NativeImage.read(Files.newInputStream(file.toPath()));
            NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
            currentCapeID = Identifier.of("holo-utils", "dynamic_cape");

            MinecraftClient.getInstance().execute(() -> {
                MinecraftClient.getInstance().getTextureManager().registerTexture(currentCapeID, texture);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
