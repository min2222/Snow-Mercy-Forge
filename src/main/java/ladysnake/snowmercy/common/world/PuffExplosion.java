package ladysnake.snowmercy.common.world;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class PuffExplosion extends Explosion {
    private static final ExplosionDamageCalculator field_25818 = new ExplosionDamageCalculator();
    private final PuffExplosion.BlockInteraction destructionType;
    private final Random random;
    private final Level world;
    private final double x;
    private final double y;
    private final double z;
    private final Entity entity;
    private final float power;
    private final float knockbackPower;
    private final DamageSource damageSource;
    private final ExplosionDamageCalculator behavior;
    private final ObjectArrayList<BlockPos> affectedBlocks;
    private final Map<Player, Vec3> affectedPlayers;
    private final boolean spawnPuff;

    public PuffExplosion(Level world, Entity entity, DamageSource damageSource, ExplosionDamageCalculator explosionBehavior, double x, double y, double z, float power, float knockbackPower, BlockInteraction destructionType, boolean spawnPuff) {
        super(world, entity, damageSource, explosionBehavior, x, y, z, power, false, destructionType);
        this.random = new Random();
        this.affectedBlocks = new ObjectArrayList<>();
        this.affectedPlayers = Maps.newHashMap();
        this.world = world;
        this.entity = entity;
        this.power = power;
        this.knockbackPower = knockbackPower;
        this.x = x;
        this.y = y;
        this.z = z;
        this.destructionType = destructionType;
        this.damageSource = damageSource == null ? entity.damageSources().explosion(this) : damageSource;
        this.behavior = explosionBehavior == null ? this.chooseBehavior(entity) : explosionBehavior;
        this.spawnPuff = spawnPuff;
    }

    public static float getExposure(Vec3 source, Entity entity) {
        AABB box = entity.getBoundingBox();
        double d = 1.0D / ((box.maxX - box.minX) * 2.0D + 1.0D);
        double e = 1.0D / ((box.maxY - box.minY) * 2.0D + 1.0D);
        double f = 1.0D / ((box.maxZ - box.minZ) * 2.0D + 1.0D);
        double g = (1.0D - Math.floor(1.0D / d) * d) / 2.0D;
        double h = (1.0D - Math.floor(1.0D / f) * f) / 2.0D;
        if (d >= 0.0D && e >= 0.0D && f >= 0.0D) {
            int i = 0;
            int j = 0;

            for (float k = 0.0F; k <= 1.0F; k = (float) ((double) k + d)) {
                for (float l = 0.0F; l <= 1.0F; l = (float) ((double) l + e)) {
                    for (float m = 0.0F; m <= 1.0F; m = (float) ((double) m + f)) {
                        double n = Mth.lerp(k, box.minX, box.maxX);
                        double o = Mth.lerp(l, box.minY, box.maxY);
                        double p = Mth.lerp(m, box.minZ, box.maxZ);
                        Vec3 vec3d = new Vec3(n + g, o, p + h);
                        if (entity.level.clip(new ClipContext(vec3d, source, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() == HitResult.Type.MISS) {
                            ++i;
                        }

                        ++j;
                    }
                }
            }

            return (float) i / (float) j;
        } else {
            return 0.0F;
        }
    }

    private static void method_24023(ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList, ItemStack itemStack, BlockPos blockPos) {
        int i = objectArrayList.size();

        for (int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = objectArrayList.get(j);
            ItemStack itemStack2 = pair.getFirst();
            if (ItemEntity.areMergable(itemStack2, itemStack)) {
                ItemStack itemStack3 = ItemEntity.merge(itemStack2, itemStack, 16);
                objectArrayList.set(j, Pair.of(itemStack3, pair.getSecond()));
                if (itemStack.isEmpty()) {
                    return;
                }
            }
        }

        objectArrayList.add(Pair.of(itemStack, blockPos));
    }

    private ExplosionDamageCalculator chooseBehavior(Entity entity) {
        return entity == null ? field_25818 : new EntityBasedExplosionDamageCalculator(entity);
    }

    public void collectBlocksAndDamageEntities() {
        Set<BlockPos> set = Sets.newHashSet();

        int k;
        int l;
        for (int j = 0; j < 16; ++j) {
            for (k = 0; k < 16; ++k) {
                for (l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d = (float) j / 15.0F * 2.0F - 1.0F;
                        double e = (float) k / 15.0F * 2.0F - 1.0F;
                        double f = (float) l / 15.0F * 2.0F - 1.0F;
                        double g = Math.sqrt(d * d + e * e + f * f);
                        d /= g;
                        e /= g;
                        f /= g;
                        float h = this.power * (0.7F + this.world.random.nextFloat() * 0.6F);
                        double m = this.x;
                        double n = this.y;
                        double o = this.z;

                        for (; h > 0.0F; h -= 0.22500001F) {
                            BlockPos blockPos = BlockPos.containing(m, n, o);
                            BlockState blockState = this.world.getBlockState(blockPos);
                            FluidState fluidState = this.world.getFluidState(blockPos);
                            Optional<Float> optional = this.behavior.getBlockExplosionResistance(this, this.world, blockPos, blockState, fluidState);
                            if (optional.isPresent()) {
                                h -= (optional.get() + 0.3F) * 0.3F;
                            }

                            if (h > 0.0F && this.behavior.shouldBlockExplode(this, this.world, blockPos, blockState, h)) {
                                set.add(blockPos);

                                // spawn flying snow on all blocks that will be destroyed
                                if (spawnPuff && random.nextInt(25) == 0) {
                                    FallingBlockEntity flyingSnow;
                                    if (random.nextBoolean()) {
                                        flyingSnow = FallingBlockEntity.fall(world, blockPos, Blocks.SNOW_BLOCK.defaultBlockState());
                                    } else {
                                        flyingSnow = FallingBlockEntity.fall(world, blockPos, Blocks.POWDER_SNOW.defaultBlockState());
                                    }
                                    flyingSnow.time = 1;
                                    flyingSnow.dropItem = false;
                                    //fix for forge;
                                    //world.addFreshEntity(flyingSnow);
                                }
                            }

                            m += d * 0.30000001192092896D;
                            n += e * 0.30000001192092896D;
                            o += f * 0.30000001192092896D;
                        }
                    }
                }
            }
        }

        this.affectedBlocks.addAll(set);
        float q = this.power * 2.0F;
        k = Mth.floor(this.x - (double) q - 1.0D);
        l = Mth.floor(this.x + (double) q + 1.0D);
        int t = Mth.floor(this.y - (double) q - 1.0D);
        int u = Mth.floor(this.y + (double) q + 1.0D);
        int v = Mth.floor(this.z - (double) q - 1.0D);
        int w = Mth.floor(this.z + (double) q + 1.0D);
        List<Entity> list = this.world.getEntities(this.entity, new AABB(k, t, v, l, u, w));
        Vec3 vec3d = new Vec3(this.x, this.y, this.z);

        for (Entity entity : list) {
            if (!entity.ignoreExplosion()) {
                double y = Mth.sqrt((float) entity.distanceToSqr(vec3d)) / q;
                if (y <= 1.0D) {
                    double z = entity.getX() - this.x;
                    double aa = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.y;
                    double ab = entity.getZ() - this.z;
                    double ac = Mth.sqrt((float) (z * z + aa * aa + ab * ab));
                    if (ac != 0.0D) {
                        z /= ac;
                        aa /= ac;
                        ab /= ac;
                        double ad = getExposure(vec3d, entity);
                        double ae = ((1.0D - y) * ad) * this.knockbackPower;
                        //entity.damage(this.getDamageSource(), (float)((int)((ae * ae + ae) / 2.0D * 7.0D * (double)q + 1.0D)));
                        double af = ae;
                        if (entity instanceof LivingEntity) {
                            af = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity) entity, ae);
                        }

                        entity.setDeltaMovement(entity.getDeltaMovement().add(z * af, aa * af, ab * af));
                        if (entity instanceof Player) {
                        	Player playerEntity = (Player) entity;
                            if (!playerEntity.isSpectator() && (!playerEntity.isCreative() || !playerEntity.getAbilities().flying)) {
                                this.affectedPlayers.put(playerEntity, new Vec3(z * ae, aa * ae, ab * ae));
                            }
                        }
                    }
                }
            }
        }
    }

    public void affectWorld(boolean bl) {
        if (this.world.isClientSide) {
            this.world.playLocalSound(this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F, false);
        }

        boolean bl2 = this.destructionType != PuffExplosion.BlockInteraction.KEEP;
        if (bl) {
            if (this.power >= 2.0F && bl2) {
                this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            } else {
                this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            }
        }

        if (bl2) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList = new ObjectArrayList<>();
            Util.shuffle(this.affectedBlocks, this.world.random);

            for (BlockPos blockPos : this.affectedBlocks) {
                BlockState blockState = this.world.getBlockState(blockPos);
                Block block = blockState.getBlock();
                if (!blockState.isAir()) {
                    BlockPos blockPos2 = blockPos.immutable();
                    this.world.getProfiler().push("explosion_blocks");
                    if (blockState.canDropFromExplosion(this.world, blockPos, this) && this.world instanceof ServerLevel) {
                        BlockEntity blockEntity = this.world.getBlockEntity(blockPos);
                        LootParams.Builder builder = (new LootParams.Builder((ServerLevel) this.world)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockPos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity).withOptionalParameter(LootContextParams.THIS_ENTITY, this.entity);
                        if (this.destructionType == BlockInteraction.DESTROY) {
                            builder.withParameter(LootContextParams.EXPLOSION_RADIUS, this.power);
                        }

                        blockState.getDrops(builder).forEach((itemStack) -> method_24023(objectArrayList, itemStack, blockPos2));
                    }

                    this.world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                    block.wasExploded(this.world, blockPos, this);
                    this.world.getProfiler().pop();
                }
            }

            for (Pair<ItemStack, BlockPos> itemStackBlockPosPair : objectArrayList) {
                Block.popResource(this.world, itemStackBlockPosPair.getSecond(), itemStackBlockPosPair.getFirst());
            }
        }

        for (BlockPos blockPos3 : this.affectedBlocks) {
            if (this.world.getBlockState(blockPos3).isAir() && this.world.getBlockState(blockPos3.below()).isSolidRender(this.world, blockPos3.below())) {
                this.world.setBlockAndUpdate(blockPos3, Blocks.SNOW.defaultBlockState());
            }
        }
    }

    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    public Map<Player, Vec3> getAffectedPlayers() {
        return this.affectedPlayers;
    }


    public LivingEntity getCausingEntity() {
        if (this.entity == null) {
            return null;
        } else if (this.entity instanceof PrimedTnt) {
            return ((PrimedTnt) this.entity).getOwner();
        } else if (this.entity instanceof LivingEntity) {
            return (LivingEntity) this.entity;
        } else {
            if (this.entity instanceof Projectile) {
                Entity entity = ((Projectile) this.entity).getOwner();
                if (entity instanceof LivingEntity) {
                    return (LivingEntity) entity;
                }
            }

            return null;
        }
    }

    public void clearAffectedBlocks() {
        this.affectedBlocks.clear();
    }

    public List<BlockPos> getAffectedBlocks() {
        return this.affectedBlocks;
    }
}
