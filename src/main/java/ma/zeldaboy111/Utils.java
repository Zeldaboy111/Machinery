package ma.zeldaboy111;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Utils {

    public static ItemStack createItem(Material material, String name, String[] lore) {
        if(material == null) return new ItemStack(Material.AIR);
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        if(name != null && !name.trim().equals("")) meta.setDisplayName(name);
        setLore(meta, lore);

        stack.setItemMeta(meta);
        return stack;
    }
    private static void setLore(ItemMeta meta, String[] lore) {
        if (lore == null || lore.length < 1) return;
        ArrayList<String> loreArray = new ArrayList<>();
        for(String s : lore) loreArray.add(s);
        meta.setLore(loreArray);
    }

}
