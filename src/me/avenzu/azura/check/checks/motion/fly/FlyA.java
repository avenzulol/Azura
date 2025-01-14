package me.avenzu.azura.check.checks.motion.fly;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import me.avenzu.azura.util.entity.NMSUtil;
import net.minecraft.server.v1_8_R3.EntityPlayer;

public class FlyA extends Check {

    private int airTicks;

    public FlyA(PlayerStats player) {
        super(player);
    }

    @Override
    public void onMove(Location from, Location to, PlayerMoveEvent event) {
        if (player.getBukkitPlayer().isFlying() || from.getY() > to.getY()) {
            airTicks = 0;
            return;
        }

        double deltaY = to.getY() - from.getY();

        EntityPlayer nmsPlayer = NMSUtil.getNmsPlayer(player.getBukkitPlayer());

        if (++airTicks == 1 && !nmsPlayer.world.c(nmsPlayer.getBoundingBox().grow(0.1, 0.42 * 0.99, 0.1)) && deltaY < 0.42 * 0.99) {
            onViolation("Fly (A2)", 60000L, 0);
        }

        if (!nmsPlayer.world.c(nmsPlayer.getBoundingBox().grow(1.0, 1.0, 1.0))) {
            if (to.getY() - from.getY() > 0.165) {
                onViolation("Fly (A)", 60000L, 0);
            }
        }
    }
}
