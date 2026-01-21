package com.eni.entropy;

import com.eni.entropy.event.DecayHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NaturesRevengeMod implements ModInitializer {
    public static final String MOD_ID = "entropy";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Nature's Revenge Initializing...");
        
        // Register Chunk Load Event
        ServerChunkEvents.CHUNK_LOAD.register(DecayHandler::onChunkLoad);
    }
}
