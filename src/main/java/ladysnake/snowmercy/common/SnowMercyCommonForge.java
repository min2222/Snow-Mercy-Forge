package ladysnake.snowmercy.common;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SnowMercy.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SnowMercyCommonForge 
{
    @SubscribeEvent
    public static void serverTick(ServerTickEvent evt)
    {
    	MinecraftServer server = evt.getServer();
        if (server.getTickCount() % 20 == 0) {	
            ServerLevel overworld = server.overworld();
            List<Player> playersToTp = new ArrayList<>();

            for (ServerPlayer player : overworld.players()) {
                BlockPos playerPos = player.blockPosition();
                if (player.getTicksFrozen() > 60 && overworld.getBlockState(playerPos).getBlock() == Blocks.POWDER_SNOW
                        && overworld.getBlockState(playerPos.offset(0, 1, 0)).getBlock() == Blocks.POWDER_SNOW
                        && overworld.getBlockState(playerPos.offset(0, 2, 0)).getBlock() == Blocks.POWDER_SNOW) {
                    boolean cancel = false;

                    for (int x = -1; x <= 1 && !cancel; x += 2) {
                        for (int y = 0; y < 2 && !cancel; y++) {
                            for (int z = -1; z <= 1 && !cancel; z += 2) {
                                if (overworld.getBlockState(playerPos.offset(x, y, z)).getBlock() != Blocks.BLUE_ICE) {
                                    cancel = true;
                                }
                            }
                        }
                    }

                    if (!cancel) {
                        playersToTp.add(player);
                    }
                }
            }

            for (Player player : playersToTp) {
                if (!player.isPassenger() && !player.isVehicle() && player.canChangeDimensions()) {
                    ServerLevel winterMurderland = server.getLevel(getWinterDimension());
                    if (winterMurderland == null) {
                        return;
                    }
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10 * 20, 5));
                    if (player instanceof ServerPlayer)
                    {
                    	ServerPlayer serverP = (ServerPlayer) player;
                    	serverP.teleportTo(winterMurderland, player.getX(), 500, player.getZ(), player.getYRot(), 90F);
                    }
                    winterMurderland.addDuringTeleport(player);
                }
            }
        }
    }
    
    public static ResourceKey<Level> getWinterDimension() {
        ResourceLocation resourcelocation = new ResourceLocation(SnowMercy.MODID, "winter_murderland");
        ResourceKey<Level> registrykey = ResourceKey.create(Registries.DIMENSION, resourcelocation);
        return registrykey;
    }
}
