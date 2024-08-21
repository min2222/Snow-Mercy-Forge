package ladysnake.snowmercy.common.entity;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import ladysnake.snowmercy.common.SnowMercy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public enum WeaponizedGolemType {
    SAWMAN("sawman"), SNUGGLES("mister_snuggles"), ROCKETS("aftermarket_snowman"), MORTARS("ice_mortar"), CHILL_SNUGGLES("mister_chill_snuggles"), BOOMBOX("iceboombox");

    public static final WeaponizedGolemType DEFAULT = SNUGGLES;

    public static final EntityDataSerializer<WeaponizedGolemType> TRACKED_DATA_HANDLER = new EntityDataSerializer<>() {
        public void write(FriendlyByteBuf packetByteBuf, WeaponizedGolemType type) {
            packetByteBuf.writeEnum(type);
        }

        public WeaponizedGolemType read(FriendlyByteBuf packetByteBuf) {
            return packetByteBuf.readEnum(WeaponizedGolemType.class);
        }

        public WeaponizedGolemType copy(WeaponizedGolemType type) {
            return type;
        }
    };

    private static final Map<ResourceLocation, WeaponizedGolemType> types = Arrays.stream(values()).collect(Collectors.toMap(WeaponizedGolemType::getId, Function.identity()));
    private final ResourceLocation id;
    private final ResourceLocation textureLocation;

    WeaponizedGolemType(String id) {
        this.id = SnowMercy.id(id);
        this.textureLocation = SnowMercy.id("textures/entity/" + id + ".png");
    }

    public static WeaponizedGolemType byId(ResourceLocation id) {
        return types.getOrDefault(id, DEFAULT);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public ResourceLocation getTexture() {
        return textureLocation;
    }

    public EntityType<? extends WeaponizedSnowGolemEntity> getEntityType() {
        @SuppressWarnings("unchecked") EntityType<? extends WeaponizedSnowGolemEntity> t = (EntityType<? extends WeaponizedSnowGolemEntity>) ForgeRegistries.ENTITY_TYPES.getValue(this.id);
        return t;
    }

    @Override
    public String toString() {
        return this.id.toString();
    }
}
