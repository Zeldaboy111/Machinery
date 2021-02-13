package ma.zeldaboy111.wand.spells;

import ma.zeldaboy111.Main;
import ma.zeldaboy111.wand.WandCmd;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import java.util.HashMap;
import java.util.UUID;

public class WolfeRaid implements ISpell {
    private static final HashMap<UUID, Long> onCooldown = new HashMap<>();
    private static final long COOLDOWN = 14000;
    private static final long DESPAWN_AFTER = 10;

    private static final int[] relativeX = new int[] { 3, 2, 0, -2, -3, -2, 0, 2 };
    private static final int[] relativeZ = new int[] { 0, 2, 3, 2, 0, -2, -3, -2 };

    @Override
    public String getName() {
        return "§2Wolfe Raid";
    }

    @Override
    public void cast(Player p) {
        int cooldown = getCooldownFromPlayer(p);
        if(cooldown >= 0) { p.sendMessage(WandCmd.prefix + "You are on cooldown. §8(§e" + cooldown + " seconds§8)"); }
        else summonWolves(p);
    }
    private Integer getCooldownFromPlayer(Player p) {
        long startedAt;
        if(onCooldown.isEmpty() || (startedAt = onCooldown.get(p.getUniqueId())) == -1) return -1;
        if(System.currentTimeMillis() - startedAt < COOLDOWN) return Math.round((COOLDOWN - (System.currentTimeMillis() - startedAt))/1000);
        onCooldown.remove(p.getUniqueId());
        return -1;
    }
    private void summonWolves(Player p) {
        Location center = p.getLocation().getBlock().getLocation().add(0.5, 0.0, 0.5);
        Wolf[] wolves = new Wolf[relativeX.length];
        for(int i = 0; i < relativeX.length; i++) {
            if (relativeZ.length < i) break;
            wolves[i] = summonWolfe(p, center.clone().add(relativeX[i], 0, relativeZ[i]));
        }
        for(Player lp : Bukkit.getServer().getOnlinePlayers()) {
            playSpawnSound(lp, center);
        }
        startDespawnTimer(p, wolves);
        startCooldown(p);
    }
    private void playSpawnSound(Player p, Location loc) {
        p.playSound(loc, Sound.BLOCK_ANVIL_LAND, 12, 1);
        for(int delay = 0; delay < 1000; delay++);
        p.playSound(loc, Sound.ENTITY_WOLF_HOWL, 11, 1);
    }
    private Wolf summonWolfe(Player p, Location loc) {
        Wolf wolf = (Wolf)p.getWorld().spawnEntity(loc, EntityType.WOLF);
        wolf.setTamed(true);
        wolf.setOwner(p);
        wolf.setCustomNameVisible(true);
        wolf.setCustomName("§2Forest Wolf");
        wolf.setMaxHealth(40.0);
        wolf.setHealth(40.0);
        wolf.setCollarColor(DyeColor.GREEN);
        wolf.setAngry(true);
        wolf.setRotation(p.getLocation().getYaw(), p.getLocation().getPitch());
        return wolf;
    }

    private void startDespawnTimer(Player p, Wolf[] wolves) {
        Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Main.plugin, new Runnable() {
            public void run() {
                for(Wolf wolf : wolves) {
                    if(wolf != null && !wolf.isDead()) wolf.remove();
                }
            }
        }, DESPAWN_AFTER*20);
    }

    private void startCooldown(Player p) {
        onCooldown.put(p.getUniqueId(), System.currentTimeMillis());


    }
}
