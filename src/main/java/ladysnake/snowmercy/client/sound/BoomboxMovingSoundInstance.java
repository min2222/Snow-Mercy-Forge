package ladysnake.snowmercy.client.sound;

import ladysnake.snowmercy.common.entity.IceboomboxEntity;
import ladysnake.snowmercy.common.init.SnowMercySoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(value = Dist.CLIENT)
public class BoomboxMovingSoundInstance extends AbstractTickableSoundInstance {
    private final IceboomboxEntity iceboombox;
    private float distance = 0.0f;

    public BoomboxMovingSoundInstance(IceboomboxEntity iceboombox) {
        super(SnowMercySoundEvents.JINGLE_BELLS.get(), SoundSource.RECORDS, SoundInstance.createUnseededRandom());

        this.iceboombox = iceboombox;
        this.looping = true;
        this.delay = 0;
        this.volume = 1.5f;
        this.x = (float) iceboombox.getX();
        this.y = (float) iceboombox.getY();
        this.z = (float) iceboombox.getZ();
    }

    @Override
    public boolean canPlaySound() {
        return !this.iceboombox.isSilent();
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        if (this.iceboombox.isRemoved()) {
            this.stop();
            return;
        }
        this.x = (float) this.iceboombox.getX();
        this.y = (float) this.iceboombox.getY();
        this.z = (float) this.iceboombox.getZ();
        this.distance = Mth.clamp(this.distance + 0.0025f, 0.0f, 1.0f);
    }
}

