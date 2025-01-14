package me.avenzu.azura.check.checks.combat.reach;

import java.lang.reflect.Field;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import me.avenzu.azura.check.Check;
import me.avenzu.azura.stats.PlayerStats;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

public class ReachA extends Check {
	
    public ReachA(PlayerStats player) {
        super(player);
    }

    @Override
    public void onPacketReceiving(Class<? extends Packet> type, Packet packet) {
    	if (type == PacketPlayInUseEntity.class) {
    		PacketPlayInUseEntity useEntity = (PacketPlayInUseEntity) packet;
    		
    		if (useEntity.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
    			int entityID = getEntityIDFromPacket(useEntity);
    			
    			if (entityID != -1) {
    				Entity attackedEntity = player.getBukkitPlayer().getWorld().getEntities().stream()
    						.filter(entity -> entity.getEntityId() == entityID)
    						.findFirst()
    						.orElse(null);
    				
    				if (attackedEntity != null) {
    					Location attackerLocation = player.getBukkitPlayer().getLocation();
    					Location attackedLocation = attackedEntity.getLocation();
    					
    					double distance = attackerLocation.distance(attackedLocation);
    					
    					double maxReach = 3.05;
    					
    					if(distance > maxReach) {
    						onViolation(String.format("Reach (A) [%.2f]", distance), 60000L, 0);
    					}
    				}
    			}
    		}
    	}
    }

	private int getEntityIDFromPacket(PacketPlayInUseEntity packet) {
    	try {
    		Field entityIDField = PacketPlayInUseEntity.class.getDeclaredField("a");
    		entityIDField.setAccessible(true);
    		return entityIDField.getInt(packet);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return -1;
    	}
    }
}
