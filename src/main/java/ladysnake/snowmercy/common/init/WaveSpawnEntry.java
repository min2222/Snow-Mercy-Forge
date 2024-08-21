package ladysnake.snowmercy.common.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

public class WaveSpawnEntry {
    public EntityType<? extends Mob> entityType;
    public int count;

    public WaveSpawnEntry(EntityType<? extends Mob> entityType, int count) {
        this.entityType = entityType;
        this.count = count;
    }
}
