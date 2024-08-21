package ladysnake.snowmercy.common.structure;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import ladysnake.snowmercy.common.SnowMercy;
import ladysnake.snowmercy.common.init.SnowMercyFeatures;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class IcepostFeature extends Structure
{
	public static final Codec<IcepostFeature> CODEC = simpleCodec(IcepostFeature::new);
	   
	public IcepostFeature(StructureSettings p_226558_)
	{	
		super(p_226558_);
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext ctx) 
	{
        ChunkPos chunkPos = ctx.chunkPos();
        int x = chunkPos.x * 16;
        int z = chunkPos.z * 16;
        if (!(/*ctx.isBiomeValid(Heightmap.Types.WORLD_SURFACE) && */ctx.heightAccessor().getMinBuildHeight() == -80 && ctx.heightAccessor().getHeight() == 304)) {
            return Optional.empty();
        }
        
        BlockPos.MutableBlockPos centerPos = new BlockPos.MutableBlockPos(x, 0, z);
        
        return Optional.of(new GenerationStub(centerPos, (structurePieces) -> 
        {
            WorldgenRandom chunkRandom = ctx.random();
            
            StructureTemplatePool structuretemplatepool = ctx.registryAccess().registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(new ResourceLocation(SnowMercy.MODID, "icepost"));
            StructurePoolElement spawnedStructure = structuretemplatepool.getRandomTemplate(chunkRandom);

            if (spawnedStructure != EmptyPoolElement.INSTANCE) {
                Rotation rotation = Util.getRandom(Rotation.values(), chunkRandom);
                BlockPos startPos = chunkPos.getWorldPosition();
                StructureTemplateManager structureManager = ctx.structureTemplateManager();
                PoolElementStructurePiece piece = new PoolElementStructurePiece(
                        structureManager,
                        spawnedStructure,
                        startPos,
                        spawnedStructure.getGroundLevelDelta(),
                        rotation,
                        spawnedStructure.getBoundingBox(structureManager, startPos, rotation)
                );
                BoundingBox boundingBox = piece.getBoundingBox();
                OptionalInt floorY = getFloorHeight(chunkRandom, ctx.chunkGenerator(), boundingBox, ctx.heightAccessor(), ctx.randomState());

                if (floorY.isEmpty()) return;

                int lowering = boundingBox.minY() + 2 + piece.getGroundLevelDelta();
                piece.move(0, floorY.getAsInt() - lowering, 0);
                structurePieces.addPiece(piece);

                // Since by default, the start piece of a structure spawns with its corner at centerPos
                // and will randomly rotate around that corner, we will center the piece on centerPos instead.
                // This is so that our structure's start piece is now centered on the water check done in shouldStartAt.
                // Whatever the offset done to center the start piece, that offset is applied to all other pieces
                // so the entire structure is shifted properly to the new spot.
                Vec3i structureCenter = structurePieces.build().pieces().get(0).getBoundingBox().getCenter();
                int xOffset = centerPos.getX() - structureCenter.getX();
                int zOffset = centerPos.getZ() - structureCenter.getZ();
                for (StructurePiece structurePiece : structurePieces.build().pieces()) {
                    structurePiece.move(xOffset, 0, zOffset);
                }
            }
        }));
	}
	
    static OptionalInt getFloorHeight(WorldgenRandom random, ChunkGenerator chunkGenerator, BoundingBox box, LevelHeightAccessor world, RandomState state) {
        int maxY = Mth.randomBetweenInclusive(random, 60, 100);
        List<BlockPos> corners = ImmutableList.of(new BlockPos(box.minX(), 0, box.minZ()), new BlockPos(box.maxX(), 0, box.minZ()), new BlockPos(box.minX(), 0, box.maxZ()), new BlockPos(box.maxX(), 0, box.maxZ()));
        List<NoiseColumn> cornerColumns = corners.stream().map((blockPos) -> 
        {
            return chunkGenerator.getBaseColumn(blockPos.getX(), blockPos.getZ(), world, state);
        }).collect(Collectors.toList());
        
        Heightmap.Types heightmapType = Heightmap.Types.WORLD_SURFACE_WG;

        int y;
        for (y = maxY; y > 15; --y) {
            int validCorners = 0;

            for (NoiseColumn cornerColumn : cornerColumns) {
                BlockState blockState = cornerColumn.getBlock(y);
                if (heightmapType.isOpaque().test(blockState)) {
                    ++validCorners;
                }
            }

            if (validCorners >= 3) {
                validCorners = 0;

                for (NoiseColumn cornerColumn : cornerColumns) {
                    BlockState blockState = cornerColumn.getBlock(y + box.getYSpan() - 1);
                    if (blockState.isAir()) {
                        ++validCorners;
                        if (validCorners == 2) {
                            return OptionalInt.of(y + 1);
                        }
                    }
                }
            }
        }

        return OptionalInt.empty();
    }

	@Override
	public StructureType<?> type() 
	{
		return SnowMercyFeatures.ICEPOST_STRUCTURETYPE.get();
	}
}
