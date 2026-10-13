package com.kasper.performanceboost.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KasperPerformanceBoostClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("kasper-optimizer");
    private int ticks = 0;
    private static final int CLEANUP_INTERVAL_TICKS = 3600; // 3 minuty

    @Override
    public void onInitializeClient() {
        LOGGER.info("[Kasper Performance Boost] Mod zostal pomyslnie zaladowany!");

        System.gc();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            ticks++;
            if (ticks >= CLEANUP_INTERVAL_TICKS) {
                ticks = 0;

                long memoryBefore = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
                System.gc();
                long memoryAfter = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
                long freed = memoryBefore - memoryAfter;

                Text message = Text.literal("[Kasper Optimizer] ")
                        .formatted(Formatting.GOLD, Formatting.BOLD)
                        .append(Text.literal("Wyczyszczono pamięć RAM! Zwolniono: " + (freed > 0 ? freed : 0) + " MB.")
                                .formatted(Formatting.GREEN));

                client.player.sendMessage(message, false);
                LOGGER.info("[Kasper Performance Boost] Wyczyszczono RAM. Zwolniono: " + freed + " MB.");
            }
        });
    }
}
