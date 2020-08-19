package ma.zeldaboy111;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class Listener implements org.bukkit.event.Listener {

    @EventHandler
    public void playerInteractArmorStand(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();
        if(e.getRightClicked().getType() == EntityType.ARMOR_STAND) {
            e.setCancelled(true);
        }
    }

}
