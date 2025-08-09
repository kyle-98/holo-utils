package com.poggers;

import com.poggers.utils.NotifyPlayer;
import com.poggers.config.ModConfig;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class HoloUtils implements ClientModInitializer, ModMenuApi {
	public static TextFieldWidget searchBox;
	private static ConfigHolder<ModConfig> configHolder;
	public ModConfig config;

	public static boolean isEspEnabled = false;

	//keybindings
	public static KeyBinding CYCLE_FOG_KEYBIND;
	public static KeyBinding FULLBRIGHT_KEYBIND;
	public static KeyBinding XRAY_KEYBIND;
	public static KeyBinding ESP_KEYBIND;

	public static ModConfig getConfig() {
		return configHolder.getConfig();
	}

	public static void saveConfig(){
		configHolder.save();
	}

	@Override
	public void onInitializeClient() {

		CYCLE_FOG_KEYBIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.holo-utils.cycleFog",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_U,
			"category.holo-utils"
		));

		FULLBRIGHT_KEYBIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.holo-utils.fullBright",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_N,
			"category.holo-utils"
		));

		configHolder = AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
		config = getConfig();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while(CYCLE_FOG_KEYBIND.wasPressed()) {
				config.cycleFogOptions(); 
			}

			while(FULLBRIGHT_KEYBIND.wasPressed()){ 
				SimpleOption<Double> gamma = MinecraftClient.getInstance().options.getGamma();
				if(!config.visualSettings.getFullbrightState()) {
					gamma.setValue(15.0);
					NotifyPlayer.displayMessage("Fullbright ON", true);
				}
				else {
					gamma.setValue(1.0);
					NotifyPlayer.displayMessage("Fullbright OFF", true);
				}
				config.visualSettings.setFullbrightState(!config.visualSettings.getFullbrightState());
			}
        });
	}
}