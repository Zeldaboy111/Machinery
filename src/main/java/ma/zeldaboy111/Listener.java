package ma.zeldaboy111;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class Listener implements org.bukkit.event.Listener {

    @EventHandler
    public void playerInteractArmorStand(PlayerInteractEntityEvent e) {
        if(e.getRightClicked().getType() == EntityType.ARMOR_STAND) {
            e.setCancelled(true);
        }
    }

}
