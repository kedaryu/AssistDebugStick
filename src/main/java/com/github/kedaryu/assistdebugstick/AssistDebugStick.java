package com.github.kedaryu.assistdebugstick;

import com.github.kedaryu.assistdebugstick.command.SettingCommand;
import com.github.kedaryu.assistdebugstick.event.AssistEvent;
import com.github.kedaryu.utilities.Utilities;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class AssistDebugStick extends Utilities {

    @Getter
    private static Plugin plugin = null;
    @Getter
    private static JavaPlugin javaPlugin = null;
    @Getter
    private static AssistDebugStick instance = null;


    @Override
    protected void enable() {
        // instance/plugin設定
        instance = this;
        plugin = this;
        javaPlugin = this;

        registerEvent();
        registerCommand();

        this.getCommandManager().setCommandNameList(Arrays.asList("assistdebugstick", "ads"));
    }

    @Override
    protected void disable() {
        // nop
    }

    private void registerEvent() {
        new AssistEvent(this);
    }

    private void registerCommand() {
        this.getCommandManager().registerCommand(SettingCommand.class);
    }
}
