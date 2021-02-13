package ma.zeldaboy111.wand.wandlist;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IWand {

    String getName();
    ItemStack getWandItem();
    void give(Player p);
    void cast(Player p);
    void rotateSpell(Player p);

}
