package ladysnake.snowmercy.cca;

import ladysnake.snowmercy.common.init.SnowMercyWaves;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

//TODO
public class SnowMercyEventComponent /*implements SyncedCapabilityComponent*/ {
    public boolean isEventOngoing;
    private int eventWave;
    private Level world;

    public int getEventWave() {
        return this.eventWave;
    }

    public void setEventWave(int wave) {
        if (wave <= 9 && wave >= 0) {
            this.eventWave = wave;
        } else {
            SnowMercyWaves.init();
            this.eventWave = 0;
        }
    }

    public boolean isEventOngoing() {
        return this.isEventOngoing;
    }

    public void startEvent(Level world) {
        if (!this.isEventOngoing) {
            this.isEventOngoing = true;
            this.world = world;
            world.players().forEach(serverPlayerEntity -> {
                serverPlayerEntity.displayClientMessage(
                        Component.translatable("info.snowmercy.start", world.dimension().registry().getPath()).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), false);
                serverPlayerEntity.playNotifySound(SoundEvents.END_PORTAL_SPAWN, SoundSource.MASTER, 1.0f, 2.0f);
            });
        }
    }

    public void stopEvent(Level world) {
        if (this.isEventOngoing) {
            this.isEventOngoing = false;
            this.world = world;
            world.players().forEach(serverPlayerEntity -> {
                serverPlayerEntity.displayClientMessage(
                		Component.translatable("info.snowmercy.stop", world.dimension().registry().getPath()).setStyle(Style.EMPTY.withColor(ChatFormatting.AQUA)), false);
            });
        }
    }

    //@Override
    public void read(CompoundTag compoundTag) {
        this.isEventOngoing = compoundTag.getBoolean("SnowMercyOngoing");
        this.eventWave = compoundTag.getInt("SnowMercyWave");
    }

    //@Override
    public void write(CompoundTag compoundTag) {
        compoundTag.putBoolean("SnowMercyOngoing", this.isEventOngoing);
        compoundTag.putInt("SnowMercyWave", this.eventWave);
    }
}
