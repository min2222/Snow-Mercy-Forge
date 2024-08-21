package ladysnake.snowmercy.mixin;

//TODO
/*@Mixin(BiomeAccess.class)
public abstract class BiomeAccessMixin {
    @Unique
    private @Nullable
    Level world;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(BiomeAccess.Storage storage, long seed, CallbackInfo ci) {
        this.world = storage instanceof Level ? ((Level) storage) : null;
    }

    @Inject(method = "getBiome(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome;", at = @At("RETURN"))
    private void getBiome(BlockPos pos, CallbackInfoReturnable<Biome> cir) {
        if (cir.getReturnValue() != null) {
            ((ExtendedBiome) (Object) cir.getReturnValue()).frostlegion_setWorld(this.world);
        }
    }
}*/