package com.poggers;

import com.poggers.config.holoutils.ModConfig;
import com.poggers.utils.ModUtils;
import com.poggers.utils.NotifyPlayer;

import com.mojang.blaze3d.platform.InputConstants;

import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.world.InteractionResult;
import net.minecraft.client.KeyMapping;

import org.lwjgl.glfw.GLFW;

public class HoloUtils implements ClientModInitializer, ModMenuApi {
	public static EditBox searchBox;
	private static ConfigHolder<ModConfig> configHolder;
	public ModConfig config;

	public static KeyMapping CYCLE_FOG_KEYBIND;
	public static KeyMapping FULLBRIGHT_KEYBIND;
	KeyMapping.Category CATEGORY = KeyMapping.Category.register(net.minecraft.resources.Identifier.fromNamespaceAndPath("holo-utils", "keybindings"));


	public static ModConfig getConfig() {
		return configHolder.getConfig();
	}

	public static void saveConfig(){
		configHolder.save();
	}

	@Override
	public void onInitializeClient() {
		
		CYCLE_FOG_KEYBIND = KeyMappingHelper.registerKeyMapping(
			new KeyMapping(
				"key.holo-utils-cycleFog",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_U,
				CATEGORY
			)
		);

		FULLBRIGHT_KEYBIND = KeyMappingHelper.registerKeyMapping(
			new KeyMapping(
				"key.holo-utils-fullBright",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_N,
				CATEGORY
			)
		);


		configHolder = AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
		config = getConfig();

		configHolder.registerSaveListener((holder, newConfig) -> {
			this.config = newConfig;
			ModUtils.toggleGamma(this.config.visualSettings.getFullbrightState());
			return InteractionResult.PASS;
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(CYCLE_FOG_KEYBIND.consumeClick()) {
				config.cycleFogOptions(); 
			}

			while(FULLBRIGHT_KEYBIND.consumeClick()){ 
				boolean newState = !config.visualSettings.getFullbrightState();
				config.visualSettings.setFullbrightState(newState);
				configHolder.save(); 
				ModUtils.toggleGamma(newState);
				NotifyPlayer.displayMessage(newState ? "Fullbright ON" : "Fullbright OFF", true);
			}
        });
	}
}