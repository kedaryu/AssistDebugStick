package com.github.kedaryu.assistdebugstick.event;

import com.github.kedaryu.assistdebugstick.AssistDebugStick;
import com.github.kedaryu.assistdebugstick.ParticleUtility;
import com.github.kedaryu.assistdebugstick.SettingManager;
import com.github.kedaryu.utilities.transform.Direction;
import lombok.Getter;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Optional;

public final class AssistEvent implements Listener {
    public AssistEvent(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private enum PropertyType {
        WEST(new Vector(-1, 0, 0)),
        EAST(new Vector(1, 0, 0)),
        NORTH(new Vector(0, 0, -1)),
        SOUTH(new Vector(0, 0, 1)),
        UP(new Vector(0, 1, 0)),
        DOWN(new Vector(0, -1, 0)),
        WATERLOGGED(new Vector(0, 0, 0));

        @Getter
        private final Vector direction;

        PropertyType(Vector direction) {
            this.direction = direction;
        }
    }


    @EventHandler
    private void clickBlock(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK)) return;
        if (event.getClickedBlock() == null) return;

        var player = event.getPlayer();
        var mainHandItem = player.getInventory().getItemInMainHand();
        if (!mainHandItem.getType().equals(Material.DEBUG_STICK)) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                viewAssistParticle(player, mainHandItem, event.getClickedBlock());
            }
        }.runTaskLater(AssistDebugStick.getPlugin(), 1);
    }

    private void viewAssistParticle(Player player, ItemStack debugStick, Block clickBlock) {
        if (!SettingManager.getInstance().isVisualization(player)) return;

        var nmsItem = CraftItemStack.asNMSCopy(debugStick);
        if (!nmsItem.hasTag()) return;

        var tag = nmsItem.getTag();
        if (tag == null) return;

        // tag取得
        String DebugPropertyKey = "DebugProperty";
        if (!tag.hasKey(DebugPropertyKey)) return;

        var debugProperty = tag.get("DebugProperty");
        if (debugProperty == null) return;

        // クリックしたブロックのNBT取得
        if (!(debugProperty instanceof NBTTagCompound)) return;
        NBTTagCompound nbtTagCompound = (NBTTagCompound) debugProperty;
        NBTBase targetNBTBase = null;
        for (String key : nbtTagCompound.getKeys()) {
            var keyUpper = key.toUpperCase();
            var materialUpper = clickBlock.getType().name().toUpperCase();

            if (!keyUpper.contains(materialUpper)) continue;
            targetNBTBase = nbtTagCompound.get(key);
            break;
        }
        if (targetNBTBase == null) return;

        // PropertyType取得
        Optional<PropertyType> propertyType = Optional.empty();
        for (PropertyType value : PropertyType.values()) {
            if (!value.name().equalsIgnoreCase(targetNBTBase.asString())) continue;
            propertyType = Optional.of(value);
            break;
        }
        if (propertyType.orElse(null) == null) return;
        spawnAssistParticle(player, propertyType.get(), clickBlock);
    }

    private void spawnAssistParticle(Player player, PropertyType propertyType, Block clickBlock) {
        var baseLocation = clickBlock.getLocation().add(0.5, 0.5, 0.5);

        switch (propertyType) {
            case UP:
                //<editor-fold desc="UP">
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(0.3, 0.5, 0.3), baseLocation.clone().add(-0.3, 0.5, 0.3),
                        5, 255, 0, 0,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(-0.3, 0.5, 0.3), baseLocation.clone().add(-0.3, 0.5, -0.3),
                        5, 255, 0, 0,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(-0.3, 0.5, -0.3), baseLocation.clone().add(0.3, 0.5, -0.3),
                        5, 255, 0, 0,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(0.3, 0.5, -0.3), baseLocation.clone().add(0.3, 0.5, 0.3),
                        5, 255, 0, 0,
                        player
                );
                //</editor-fold>
                break;

            case DOWN:
                //<editor-fold desc="DOWN">
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(0.3, -0.5, 0.3), baseLocation.clone().add(-0.3, -0.5, 0.3),
                        5, 255, 0, 0,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(-0.3, -0.5, 0.3), baseLocation.clone().add(-0.3, -0.5, -0.3),
                        5, 255, 0, 0,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(-0.3, -0.5, -0.3), baseLocation.clone().add(0.3, -0.5, -0.3),
                        5, 255, 0, 0,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(0.3, -0.5, -0.3), baseLocation.clone().add(0.3, -0.5, 0.3),
                        5, 255, 0, 0,
                        player
                );
                //</editor-fold>
                break;

            case EAST:
            case WEST:
            case NORTH:
            case SOUTH:
                //<editor-fold desc="EAST/WEST/NORTH/SOUTH">
                var pointRightUp = baseLocation.clone()
                        .add(Direction.getForward(propertyType.direction).multiply(0.5))
                        .add(Direction.getRight(propertyType.direction).multiply(0.3))
                        .add(Direction.getUp(propertyType.direction).multiply(0.5));
                var pointLeftUp = baseLocation.clone()
                        .add(Direction.getForward(propertyType.direction).multiply(0.5))
                        .add(Direction.getRight(propertyType.direction).multiply(0.3).multiply(-1))
                        .add(Direction.getUp(propertyType.direction).multiply(0.5));
                var pointRightDown = baseLocation.clone()
                        .add(Direction.getForward(propertyType.direction).multiply(0.5))
                        .add(Direction.getRight(propertyType.direction).multiply(0.3))
                        .add(Direction.getUp(propertyType.direction).multiply(0.5).multiply(-1));
                var pointLeftDown = baseLocation.clone()
                        .add(Direction.getForward(propertyType.direction).multiply(0.5))
                        .add(Direction.getRight(propertyType.direction).multiply(0.3).multiply(-1))
                        .add(Direction.getUp(propertyType.direction).multiply(0.5).multiply(-1));

                ParticleUtility.spawnLineParticle(
                        pointRightUp, pointLeftUp,
                        5, 255, 0, 0,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        pointLeftUp, pointLeftDown,
                        5, 255, 0, 0,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        pointLeftDown, pointRightDown,
                        5, 255, 0, 0,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        pointRightDown, pointRightUp,
                        5, 255, 0, 0,
                        player
                );
                //</editor-fold>
                break;

            case WATERLOGGED:
                //<editor-fold desc="WATERLOGGED">
                //<editor-fold desc="上面">
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(0.5, 0.5, 0.5), baseLocation.clone().add(-0.5, 0.5, 0.5),
                        5, 0, 0, 255,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(-0.5, 0.5, 0.5), baseLocation.clone().add(-0.5, 0.5, -0.5),
                        5, 0, 0, 255,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(-0.5, 0.5, -0.5), baseLocation.clone().add(0.5, 0.5, -0.5),
                        5, 0, 0, 255,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(0.5, 0.5, -0.5), baseLocation.clone().add(0.5, 0.5, 0.5),
                        5, 0, 0, 255,
                        player
                );
                //</editor-fold>
                //<editor-fold desc="下面">
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(0.5, -0.5, 0.5), baseLocation.clone().add(-0.5, -0.5, 0.5),
                        5, 0, 0, 255,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(-0.5, -0.5, 0.5), baseLocation.clone().add(-0.5, -0.5, -0.5),
                        5, 0, 0, 255,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(-0.5, -0.5, -0.5), baseLocation.clone().add(0.5, -0.5, -0.5),
                        5, 0, 0, 255,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(0.5, -0.5, -0.5), baseLocation.clone().add(0.5, -0.5, 0.5),
                        5, 0, 0, 255,
                        player
                );
                //</editor-fold>
                //<editor-fold desc="側面">
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(0.5, 0.5, 0.5), baseLocation.clone().add(0.5, -0.5, 0.5),
                        5, 0, 0, 255,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(-0.5, 0.5, 0.5), baseLocation.clone().add(-0.5, -0.5, 0.5),
                        5, 0, 0, 255,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(0.5, 0.5, -0.5), baseLocation.clone().add(0.5, -0.5, -0.5),
                        5, 0, 0, 255,
                        player
                );
                ParticleUtility.spawnLineParticle(
                        baseLocation.clone().add(-0.5, 0.5, -0.5), baseLocation.clone().add(-0.5, -0.5, -0.5),
                        5, 0, 0, 255,
                        player
                );
                //</editor-fold>
                //</editor-fold>
                break;
        }
    }
}
