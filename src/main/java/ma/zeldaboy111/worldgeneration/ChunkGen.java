package ma.zeldaboy111.worldgeneration;


import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChunkGen extends ChunkGenerator {
    private int currentHeight = 50;

    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
        ChunkData chunk = createChunkData(world);
        generator.setScale(0.005D);
        generateHeightAndBlocks(chunk, chunkX, chunkZ, generator);
        return chunk;
    }
    private void generateHeightAndBlocks(ChunkData chunk, int chunkX, int chunkZ, SimplexOctaveGenerator generator) {
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                currentHeight = (int) ((generator.noise(chunkX * 16 + x, chunkZ*16+z, 0.05D, 0.05D) + 1) * 15D + 50D);
                chunk.setBlock(x, currentHeight, z, Material.GRASS_BLOCK);
                chunk.setBlock(x, currentHeight-1, z, Material.DIRT);
                for(int y = currentHeight-2; y > 0; y--) chunk.setBlock(x, y, z, Material.STONE);
                chunk.setBlock(x, 0, z, Material.BEDROCK);
            }
        }
    }
    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList((BlockPopulator)new TreePopulator());
    }

}
