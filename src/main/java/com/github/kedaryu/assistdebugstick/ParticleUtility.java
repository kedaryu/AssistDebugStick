package com.github.kedaryu.assistdebugstick;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public final class ParticleUtility {
    public static void spawnLineParticle(Location pointA, Location pointB, int divisor, int colorRed, int colorGreen, int colorBlue, Player player) {
        var direction = pointB.clone().subtract(pointA.clone()).multiply(1d / divisor);

        for (int cnt = 0; cnt < divisor; cnt++) {
            spawnParticle(pointA.clone().add(direction.clone().multiply(cnt)), colorRed, colorGreen, colorBlue, player);
        }
    }

    private static void spawnParticle(Location location, int colorRed, int colorGreen, int colorBlue, Player player) {
        if (location == null || location.getWorld() == null) return;

        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(colorRed, colorGreen, colorBlue), 1);
        player.spawnParticle(
                Particle.REDSTONE,
                location,
                0,
                colorRed, colorGreen, colorBlue,
                1,
                dustOptions
        );
    }
}
