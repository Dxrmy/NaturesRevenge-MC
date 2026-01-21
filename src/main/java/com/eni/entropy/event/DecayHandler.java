package com.eni.entropy.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.Random;

public class DecayHandler {
    private static final long TEN_DAYS = 24000L * 10;
    private static final long HUNDRED_DAYS = 24000L * 100;
    private static final Random random = new Random();

    public static void onChunkLoad(ServerLevel level, LevelChunk chunk) {
        // In a real Fabric implementation with Cardinal Components, 
        // we would retrieve: MyComponents.CHUNK_DATA.get(chunk).getLastVisited();
        
        // For the "Macro" simulation without full Component interface boilerplate here:
        long timeAway = TEN_DAYS + 1; // Simulation: Always decay heavily for demo
        
        if (timeAway > TEN_DAYS) {
             applyGrowth(level, chunk);
        }
        if (timeAway > HUNDRED_DAYS) {
             applyRot(level, chunk);
        }
    }

    private static void applyGrowth(ServerLevel level, LevelChunk chunk) {
        ChunkPos pos = chunk.getPos();
        
        // Scan a subset of blocks to avoid lag (random 10 locations)
        for(int i=0; i<10; i++) {
             int x = pos.getMinBlockX() + random.nextInt(16);
             int z = pos.getMinBlockZ() + random.nextInt(16);
             int y = level.getHeight(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, x, z);
             
             BlockPos target = new BlockPos(x, y-1, z);
             BlockState state = level.getBlockState(target);
             
             // Grass Spreading
             if (state.is(Blocks.DIRT_PATH) || state.is(Blocks.COARSE_DIRT)) {
                 level.setBlock(target, Blocks.GRASS_BLOCK.defaultBlockState(), 3);
             }
        }
    }

    private static void applyRot(ServerLevel level, LevelChunk chunk) {
        ChunkPos pos = chunk.getPos();
        
        for(int i=0; i<20; i++) { // More aggressive
             int x = pos.getMinBlockX() + random.nextInt(16);
             int z = pos.getMinBlockZ() + random.nextInt(16);
             int y = 60 + random.nextInt(40); // Scan potential build heights
             
             BlockPos target = new BlockPos(x, y, z);
             BlockState state = level.getBlockState(target);
             
             if (state.is(Blocks.COBBLESTONE)) {
                 level.setBlock(target, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 3);
             } else if (state.is(BlockTags.LOGS)) {
                 if (random.nextBoolean()) {
                     level.setBlock(target, Blocks.AIR.defaultBlockState(), 3); // Rot away
                 }
             }
        }
    }
}
