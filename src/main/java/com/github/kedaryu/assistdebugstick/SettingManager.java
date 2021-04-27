package com.github.kedaryu.assistdebugstick;

import com.github.kedaryu.assistdebugstick.constant.PersistentDataConstant;
import com.github.kedaryu.utilities.persistentdata.PersistentData;
import org.bukkit.entity.Player;

public final class SettingManager {

    public void setVisualization(Player player, boolean flag) {
        PersistentData.setStringDataContainerKey(AssistDebugStick.getPlugin(), player, PersistentDataConstant.VisualizationKey, flag? "true" : "false");
    }

    public boolean isVisualization(Player player) {
        var persistentData = PersistentData.getStringDataContainerKey(AssistDebugStick.getPlugin(), player, PersistentDataConstant.VisualizationKey).orElse(null);
        if (persistentData == null) return false;

        return persistentData.equalsIgnoreCase("true");
    }


    //<editor-fold desc="Singleton">
    public static SettingManager getInstance() {
        return SettingManagerInstanceHolder.INSTANCE;
    }

    public static class SettingManagerInstanceHolder {
        /** 唯一のインスタンス */
        private static final SettingManager INSTANCE = new SettingManager();
    }
    //</editor-fold>
}
