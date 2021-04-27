package com.github.kedaryu.assistdebugstick.command;

import com.github.kedaryu.assistdebugstick.SettingManager;
import com.github.kedaryu.utilities.chat.DecorativeCode;
import com.github.kedaryu.utilities.command.CommandAnnotation;
import com.github.kedaryu.utilities.command.CommandExe;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class SettingCommand {

    @CommandAnnotation(args = {"visualization", CommandExe.BOOLEAN})
    public void settingVisualization(CommandSender sender, Location loc, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(DecorativeCode.convert("&cThe sender must be a player."));
            return;
        }
        SettingManager.getInstance().setVisualization((Player) sender, getBoolean(args[1]));
    }

    private boolean getBoolean(String booleanString) {
        return booleanString.equalsIgnoreCase("true");
    }
}
