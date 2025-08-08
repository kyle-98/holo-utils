package com.poggers.config;

import com.poggers.utils.NotifyPlayer;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;
import me.shedaniel.autoconfig.annotation.ConfigEntry.ColorPicker;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.TransitiveObject;

@Config(name = "holo-utils")
public class ModConfig implements ConfigData {
    @Category("inventorySearchSettings")
    @TransitiveObject
    public InventorySearchSettings iSSettings = new InventorySearchSettings();

    public static class InventorySearchSettings{
        @ColorPicker
        private String HIGHLIGHT_COLOR = "#D98845";

        private boolean STAY_ENABLED = true;

        public String getHighlightColor() {
            return HIGHLIGHT_COLOR;
        }

        public void setHighlightColor(String hc) {
            this.HIGHLIGHT_COLOR = hc;
        }

        public boolean getEnabledState() {
            return STAY_ENABLED;
        }

        public void setEnabledState(boolean es) {
            this.STAY_ENABLED = es;
        }
    }

    public enum FogRemoval {
        DISABLED,
        EVERYWHERE,
        NETHER_ONLY;
    }

    @Category("visualSettings")
    @TransitiveObject
    public VisualSettings visualSettings = new VisualSettings();    
    public static class VisualSettings {
        @ConfigEntry.Gui.EnumHandler(option= ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
        private FogRemoval removeFogEverywhere = FogRemoval.DISABLED;

        private boolean fullbrightEnabled = false;

        public FogRemoval getAllFogState() {
            return this.removeFogEverywhere;
        }

        public void setAllFogState(FogRemoval state) {
            this.removeFogEverywhere = state;
        }

        public boolean getFullbrightState() {
            return this.fullbrightEnabled;
        }

        public void setFullbrightState(boolean state) {
            this.fullbrightEnabled = state;
            AutoConfig.getConfigHolder(ModConfig.class).save();
        }
    }

    public void cycleFogOptions(){
        switch(this.visualSettings.getAllFogState()) {
            case DISABLED:
                NotifyPlayer.displayMessage("Disabling Fog Everywhere", true);
                this.visualSettings.setAllFogState(FogRemoval.EVERYWHERE);
                break;
            case EVERYWHERE:
                NotifyPlayer.displayMessage("Disabling Fog in Nether Only", true);
                this.visualSettings.setAllFogState(FogRemoval.NETHER_ONLY);
                break;
            case NETHER_ONLY:
                NotifyPlayer.displayMessage("Enabling Fog", true);
                this.visualSettings.setAllFogState(FogRemoval.DISABLED);
                break;
        }
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }
}
