package ladysnake.snowmercy.common.init;

import java.util.ArrayList;

public class SnowMercyWaves {
    public static final ArrayList<ArrayList<WaveSpawnEntry>> WAVES = new ArrayList<>();

    public static void init() {
        WAVES.clear();
        for (int i = 1; i <= 10; i++) {
            WAVES.add(new ArrayList<>());
        }

        // 50 mobs
        WAVES.get(0).add(new WaveSpawnEntry(SnowMercyEntities.SAWMAN.get(), 40));
        WAVES.get(0).add(new WaveSpawnEntry(SnowMercyEntities.TUNDRABID.get(), 10));
        WAVES.get(0).add(new WaveSpawnEntry(SnowMercyEntities.ICEBALL.get(), 5));

        // 75 mobs
        WAVES.get(1).add(new WaveSpawnEntry(SnowMercyEntities.SAWMAN.get(), 40));
        WAVES.get(1).add(new WaveSpawnEntry(SnowMercyEntities.TUNDRABID.get(), 15));
        WAVES.get(1).add(new WaveSpawnEntry(SnowMercyEntities.ROCKETS.get(), 5));
        WAVES.get(1).add(new WaveSpawnEntry(SnowMercyEntities.MORTARS.get(), 5));

        // 91 mobs
        WAVES.get(2).add(new WaveSpawnEntry(SnowMercyEntities.SAWMAN.get(), 40));
        WAVES.get(2).add(new WaveSpawnEntry(SnowMercyEntities.TUNDRABID.get(), 20));
        WAVES.get(2).add(new WaveSpawnEntry(SnowMercyEntities.ROCKETS.get(), 10));
        WAVES.get(2).add(new WaveSpawnEntry(SnowMercyEntities.SNUGGLES.get(), 10));
        WAVES.get(2).add(new WaveSpawnEntry(SnowMercyEntities.BOOMBOX.get(), 1));
        WAVES.get(2).add(new WaveSpawnEntry(SnowMercyEntities.ICEBALL.get(), 10));

        // 113 mobs
        WAVES.get(3).add(new WaveSpawnEntry(SnowMercyEntities.SAWMAN.get(), 50));
        WAVES.get(3).add(new WaveSpawnEntry(SnowMercyEntities.ROCKETS.get(), 25));
        WAVES.get(3).add(new WaveSpawnEntry(SnowMercyEntities.MORTARS.get(), 25));
        WAVES.get(3).add(new WaveSpawnEntry(SnowMercyEntities.SNUGGLES.get(), 10));
        WAVES.get(3).add(new WaveSpawnEntry(SnowMercyEntities.BOOMBOX.get(), 2));

        // 1 mob, headmaster introduction
        WAVES.get(4).add(new WaveSpawnEntry(SnowMercyEntities.HEADMASTER.get(), 1));

        // 148 mobs
        WAVES.get(5).add(new WaveSpawnEntry(SnowMercyEntities.SAWMAN.get(), 50));
        WAVES.get(5).add(new WaveSpawnEntry(SnowMercyEntities.ROCKETS.get(), 30));
        WAVES.get(5).add(new WaveSpawnEntry(SnowMercyEntities.MORTARS.get(), 30));
        WAVES.get(5).add(new WaveSpawnEntry(SnowMercyEntities.SNUGGLES.get(), 15));
        WAVES.get(5).add(new WaveSpawnEntry(SnowMercyEntities.CHILL_SNUGGLES.get(), 5));
        WAVES.get(5).add(new WaveSpawnEntry(SnowMercyEntities.ICEBALL.get(), 15));

        // 200 mobs
        WAVES.get(6).add(new WaveSpawnEntry(SnowMercyEntities.SAWMAN.get(), 50));
        WAVES.get(6).add(new WaveSpawnEntry(SnowMercyEntities.TUNDRABID.get(), 10));
        WAVES.get(6).add(new WaveSpawnEntry(SnowMercyEntities.ROCKETS.get(), 30));
        WAVES.get(6).add(new WaveSpawnEntry(SnowMercyEntities.MORTARS.get(), 45));
        WAVES.get(6).add(new WaveSpawnEntry(SnowMercyEntities.SNUGGLES.get(), 20));
        WAVES.get(6).add(new WaveSpawnEntry(SnowMercyEntities.CHILL_SNUGGLES.get(), 5));
        WAVES.get(6).add(new WaveSpawnEntry(SnowMercyEntities.ICEBALL.get(), 20));
        WAVES.get(6).add(new WaveSpawnEntry(SnowMercyEntities.POLAR_BEARER.get(), 15));
        WAVES.get(6).add(new WaveSpawnEntry(SnowMercyEntities.HEADMASTER.get(), 2));

        // 150 mobs
        WAVES.get(7).add(new WaveSpawnEntry(SnowMercyEntities.CHILL_SNUGGLES.get(), 50));
        WAVES.get(7).add(new WaveSpawnEntry(SnowMercyEntities.ICEBALL.get(), 100));

        // 205 mobs
        WAVES.get(8).add(new WaveSpawnEntry(SnowMercyEntities.SAWMAN.get(), 150));
        WAVES.get(8).add(new WaveSpawnEntry(SnowMercyEntities.HEADMASTER.get(), 5));
        WAVES.get(8).add(new WaveSpawnEntry(SnowMercyEntities.SNUGGLES.get(), 50));

        // grand finale, 390 mobs
        WAVES.get(9).add(new WaveSpawnEntry(SnowMercyEntities.SAWMAN.get(), 100));
        WAVES.get(9).add(new WaveSpawnEntry(SnowMercyEntities.TUNDRABID.get(), 30));
        WAVES.get(9).add(new WaveSpawnEntry(SnowMercyEntities.ROCKETS.get(), 50));
        WAVES.get(9).add(new WaveSpawnEntry(SnowMercyEntities.MORTARS.get(), 80));
        WAVES.get(9).add(new WaveSpawnEntry(SnowMercyEntities.SNUGGLES.get(), 20));
        WAVES.get(9).add(new WaveSpawnEntry(SnowMercyEntities.CHILL_SNUGGLES.get(), 20));
        WAVES.get(9).add(new WaveSpawnEntry(SnowMercyEntities.ICEBALL.get(), 50));
        WAVES.get(9).add(new WaveSpawnEntry(SnowMercyEntities.POLAR_BEARER.get(), 30));
        WAVES.get(9).add(new WaveSpawnEntry(SnowMercyEntities.HEADMASTER.get(), 10));

    }
}
