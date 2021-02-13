package ma.zeldaboy111.wand;

import ma.zeldaboy111.wand.wandlist.DruidWand;
import ma.zeldaboy111.wand.wandlist.IWand;
import org.bukkit.inventory.ItemStack;

public enum WandList {

    DRUID(new DruidWand()),

    ;

    private IWand wand;
    WandList(IWand wand) {
        this.wand = wand;
    }
    public IWand getWand() { return wand; }
    public ItemStack getWandItem() { return wand.getWandItem(); }
    public String getName() { return wand == null ? "" : wand.getName(); }

}
