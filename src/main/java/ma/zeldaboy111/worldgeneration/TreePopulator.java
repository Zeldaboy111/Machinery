package ma.zeldaboy111.worldgeneration;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class TreePopulator extends BlockPopulator {
    @Override
    public void populate(World world, Random random, Chunk chunk) {
        if(!random.nextBoolean()) return;
        int treeAmount = random.nextInt(8);
        for(int i = 1; i < treeAmount; i++) {
            generateNewTree(world, random, chunk);
        }
    }
    private void generateNewTree(World world, Random random, Chunk chunk) {
        int x = random.nextInt(15);
        int z = random.nextInt(15);
        int y = getGroundLevel(x, z, world, chunk);
        world.generateTree(chunk.getBlock(x, y, z).getLocation(), TreeType.TREE);
    }
    private int getGroundLevel(int x, int z, World world, Chunk chunk) {
        int y;
        for(y = world.getMaxHeight()-1; chunk.getBlock(z, y, z).getType() == Material.AIR; y--);
        return y;
    }

    //https://bukkit.gamepedia.com/Developing_a_World_Generator_Plugin
}
