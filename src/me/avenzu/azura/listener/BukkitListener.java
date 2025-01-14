package me.avenzu.azura.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

import me.avenzu.azura.Azura;
import me.avenzu.azura.packet.PacketHandler;
import me.avenzu.azura.stats.PlayerStats;
import me.avenzu.azura.util.entity.NMSUtil;
import me.avenzu.azura.util.location.VelocityUtils;

public class BukkitListener implements Listener {

    public BukkitListener() {
        Azura.INST.getServer().getPluginManager().registerEvents(this, Azura.INST);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        NMSUtil.getNmsPlayer(player).playerConnection.networkManager.channel.pipeline()
                .addBefore("packet_handler", "azura_packet_handler", new PacketHandler(Azura.INST.getStats(player)));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location from = event.getFrom(), to = event.getTo();

        Player player = event.getPlayer();

        if (from.distance(to) == 0) {
            return;
        }

        Azura.INST.getStats(player).getCheckManager().getChecks().forEach(check -> check.onMove(from, to, event));
    }

    @EventHandler
    public void onVelocity(PlayerVelocityEvent event) {
        Vector velocity = event.getVelocity();

        double x = velocity.getX();
        double y = velocity.getY();
        double z = velocity.getZ();

        double horizontal = Math.sqrt(x * x + z * z);
        double vertical = Math.abs(y);

        PlayerStats playerStats = Azura.INST.getStats(event.getPlayer());

        playerStats.setHorizontalVelocity(horizontal);

        playerStats.getVelocityDeque().addLast(new VelocityUtils(horizontal, vertical));
    }
}
