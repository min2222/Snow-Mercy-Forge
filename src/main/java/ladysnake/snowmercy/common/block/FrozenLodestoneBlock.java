package ladysnake.snowmercy.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class FrozenLodestoneBlock extends Block {
    public FrozenLodestoneBlock(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide && !player.isPassenger() && !player.isVehicle() && player.canChangeDimensions()) {
            ServerLevel spawnWorld = world.getServer().getLevel(((ServerPlayer) player).getRespawnDimension());

            if (spawnWorld == null) {
                spawnWorld = world.getServer().overworld();
            }

            BlockPos spawn = ((ServerPlayer) player).getRespawnPosition();
            if (spawn == null) {
                spawn = spawnWorld.getSharedSpawnPos();
            }
            if (player instanceof ServerPlayer)
            {
            	ServerPlayer serverP = (ServerPlayer) player;
            	serverP.teleportTo(spawnWorld, spawn.getX(), spawn.getY(), spawn.getZ(), player.getYRot(), 90F);
            }
            spawnWorld.addDuringTeleport(player);
        }
        return super.use(state, world, pos, player, hand, hit);
    }
}
