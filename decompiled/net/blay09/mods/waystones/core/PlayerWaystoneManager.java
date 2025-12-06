/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Either
 *  net.blay09.mods.balm.api.Balm
 *  net.blay09.mods.balm.api.BalmEnvironment
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Position
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundSetExperiencePacket
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.server.level.TicketType
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.TagKey
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.Entity$RemovalReason
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ChunkPos
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.core;

import com.mojang.datafixers.util.Either;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.BalmEnvironment;
import net.blay09.mods.waystones.api.IMutableWaystone;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.IWaystoneTeleportContext;
import net.blay09.mods.waystones.api.TeleportDestination;
import net.blay09.mods.waystones.api.WaystoneActivatedEvent;
import net.blay09.mods.waystones.api.WaystoneTeleportError;
import net.blay09.mods.waystones.api.WaystoneTeleportEvent;
import net.blay09.mods.waystones.api.WaystonesAPI;
import net.blay09.mods.waystones.block.entity.WarpPlateBlockEntity;
import net.blay09.mods.waystones.config.DimensionalWarp;
import net.blay09.mods.waystones.config.InventoryButtonMode;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.core.IPlayerWaystoneData;
import net.blay09.mods.waystones.core.InMemoryPlayerWaystoneData;
import net.blay09.mods.waystones.core.PersistentPlayerWaystoneData;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.core.WaystoneEditPermissions;
import net.blay09.mods.waystones.core.WaystoneManager;
import net.blay09.mods.waystones.core.WaystoneSyncManager;
import net.blay09.mods.waystones.core.WaystoneTeleportContext;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.blay09.mods.waystones.network.message.TeleportEffectMessage;
import net.blay09.mods.waystones.tag.ModItemTags;
import net.blay09.mods.waystones.worldgen.namegen.NameGenerationMode;
import net.blay09.mods.waystones.worldgen.namegen.NameGenerator;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class PlayerWaystoneManager {
    private static final Logger logger = LogManager.getLogger();
    private static final IPlayerWaystoneData persistentPlayerWaystoneData = new PersistentPlayerWaystoneData();
    private static final IPlayerWaystoneData inMemoryPlayerWaystoneData = new InMemoryPlayerWaystoneData();

    public static boolean mayBreakWaystone(Player player, BlockGetter world, BlockPos pos) {
        if (WaystonesConfig.getActive().restrictions.restrictToCreative && !player.m_150110_().f_35937_) {
            return false;
        }
        return WaystoneManager.get(player.m_20194_()).getWaystoneAt(world, pos).map(waystone -> {
            if (!player.m_150110_().f_35937_) {
                if (waystone.wasGenerated() && WaystonesConfig.getActive().restrictions.generatedWaystonesUnbreakable) {
                    return false;
                }
                boolean isGlobal = waystone.isGlobal();
                boolean mayBreakGlobalWaystones = !WaystonesConfig.getActive().restrictions.globalWaystoneSetupRequiresCreativeMode;
                return !isGlobal || mayBreakGlobalWaystones;
            }
            return true;
        }).orElse(true);
    }

    public static boolean mayPlaceWaystone(@Nullable Player player) {
        return !WaystonesConfig.getActive().restrictions.restrictToCreative || player != null && player.m_150110_().f_35937_;
    }

    public static WaystoneEditPermissions mayEditWaystone(Player player, Level world, IWaystone waystone) {
        if (WaystonesConfig.getActive().restrictions.restrictToCreative && !player.m_150110_().f_35937_) {
            return WaystoneEditPermissions.NOT_CREATIVE;
        }
        if (WaystonesConfig.getActive().restrictions.restrictRenameToOwner && !waystone.isOwner(player)) {
            return WaystoneEditPermissions.NOT_THE_OWNER;
        }
        if (waystone.isGlobal() && !player.m_150110_().f_35937_ && WaystonesConfig.getActive().restrictions.globalWaystoneSetupRequiresCreativeMode) {
            return WaystoneEditPermissions.GET_CREATIVE;
        }
        return WaystoneEditPermissions.ALLOW;
    }

    public static boolean isWaystoneActivated(Player player, IWaystone waystone) {
        return PlayerWaystoneManager.getPlayerWaystoneData(player.m_9236_()).isWaystoneActivated(player, waystone);
    }

    public static void activateWaystone(Player player, IWaystone waystone) {
        if (!waystone.hasName() && waystone instanceof IMutableWaystone && waystone.wasGenerated()) {
            NameGenerationMode nameGenerationMode = WaystonesConfig.getActive().worldGen.nameGenerationMode;
            String name = NameGenerator.get(player.m_20194_()).getName(waystone, player.m_9236_().f_46441_, nameGenerationMode);
            ((IMutableWaystone)((Object)waystone)).setName(name);
        }
        if (!waystone.hasOwner() && waystone instanceof IMutableWaystone) {
            ((IMutableWaystone)((Object)waystone)).setOwnerUid(player.m_20148_());
        }
        if (player.m_20194_() != null) {
            WaystoneManager.get(player.m_20194_()).m_77762_();
        }
        if (!PlayerWaystoneManager.isWaystoneActivated(player, waystone) && waystone.getWaystoneType().equals((Object)WaystoneTypes.WAYSTONE)) {
            PlayerWaystoneManager.getPlayerWaystoneData(player.m_9236_()).activateWaystone(player, waystone);
            Balm.getEvents().fireEvent((Object)new WaystoneActivatedEvent(player, waystone));
        }
    }

    public static int predictExperienceLevelCost(Entity player, IWaystone waystone, WarpMode warpMode, @Nullable IWaystone fromWaystone) {
        WaystoneTeleportContext context = new WaystoneTeleportContext(player, waystone, null);
        context.getLeashedEntities().addAll(PlayerWaystoneManager.findLeashedAnimals(player));
        context.setFromWaystone(fromWaystone);
        return PlayerWaystoneManager.getExperienceLevelCost(player, waystone, warpMode, context);
    }

    public static int getExperienceLevelCost(Entity entity, IWaystone waystone, WarpMode warpMode, IWaystoneTeleportContext context) {
        double xpLevelCost;
        if (!(entity instanceof Player)) {
            return 0;
        }
        Player player = (Player)entity;
        if (context.getFromWaystone() != null && waystone.getWaystoneUid().equals(context.getFromWaystone().getWaystoneUid())) {
            return 0;
        }
        boolean enableXPCost = !player.m_150110_().f_35937_;
        int xpForLeashed = WaystonesConfig.getActive().xpCost.xpCostPerLeashed * context.getLeashedEntities().size();
        double xpCostMultiplier = warpMode.getXpCostMultiplier();
        if (waystone.isGlobal()) {
            xpCostMultiplier *= WaystonesConfig.getActive().xpCost.globalWaystoneXpCostMultiplier;
        }
        BlockPos pos = waystone.getPos();
        double dist = Math.sqrt(player.m_20275_((double)pos.m_123341_(), player.m_20186_(), (double)pos.m_123343_()));
        double minimumXpCost = WaystonesConfig.getActive().xpCost.minimumBaseXpCost;
        double maximumXpCost = WaystonesConfig.getActive().xpCost.maximumBaseXpCost;
        if (waystone.getDimension() != player.m_9236_().m_46472_()) {
            int dimensionalWarpXpCost = WaystonesConfig.getActive().xpCost.dimensionalWarpXpCost;
            xpLevelCost = Mth.m_14008_((double)dimensionalWarpXpCost, (double)minimumXpCost, (double)dimensionalWarpXpCost);
        } else if (WaystonesConfig.getActive().xpCost.blocksPerXpLevel > 0) {
            xpLevelCost = Mth.m_14008_((double)Math.floor(dist / (double)WaystonesConfig.getActive().xpCost.blocksPerXpLevel), (double)minimumXpCost, (double)maximumXpCost);
            if (WaystonesConfig.getActive().xpCost.inverseXpCost) {
                xpLevelCost = maximumXpCost - xpLevelCost;
            }
        } else {
            xpLevelCost = minimumXpCost;
        }
        return enableXPCost ? (int)Math.round((xpLevelCost + (double)xpForLeashed) * xpCostMultiplier) : 0;
    }

    @Nullable
    public static IWaystone getInventoryButtonWaystone(Player player) {
        InventoryButtonMode inventoryButtonMode = WaystonesConfig.getActive().getInventoryButtonMode();
        if (inventoryButtonMode.isReturnToNearest()) {
            return PlayerWaystoneManager.getNearestWaystone(player);
        }
        if (inventoryButtonMode.hasNamedTarget()) {
            return WaystoneManager.get(player.m_20194_()).findWaystoneByName(inventoryButtonMode.getNamedTarget()).orElse(null);
        }
        return null;
    }

    public static boolean canUseInventoryButton(Player player) {
        IWaystone waystone = PlayerWaystoneManager.getInventoryButtonWaystone(player);
        int xpLevelCost = waystone != null ? PlayerWaystoneManager.predictExperienceLevelCost((Entity)player, waystone, WarpMode.INVENTORY_BUTTON, null) : 0;
        return PlayerWaystoneManager.getInventoryButtonCooldownLeft(player) <= 0L && (xpLevelCost <= 0 || player.f_36078_ >= xpLevelCost);
    }

    public static boolean canUseWarpStone(Player player, ItemStack heldItem) {
        return PlayerWaystoneManager.getWarpStoneCooldownLeft(player) <= 0L;
    }

    public static double getCooldownMultiplier(IWaystone waystone) {
        return waystone.isGlobal() ? WaystonesConfig.getActive().cooldowns.globalWaystoneCooldownMultiplier : 1.0;
    }

    private static void informPlayer(Entity entity, String translationKey) {
        if (entity instanceof Player) {
            MutableComponent chatComponent = Component.m_237115_((String)translationKey);
            chatComponent.m_130940_(ChatFormatting.RED);
            ((Player)entity).m_5661_((Component)chatComponent, false);
        }
    }

    public static Consumer<WaystoneTeleportError> informRejectedTeleport(Entity entityToInform) {
        return error -> {
            logger.info("Rejected teleport: " + error.getClass().getSimpleName());
            if (error.getTranslationKey() != null) {
                PlayerWaystoneManager.informPlayer(entityToInform, error.getTranslationKey());
            }
        };
    }

    public static Either<List<Entity>, WaystoneTeleportError> tryTeleportToWaystone(Entity entity, IWaystone waystone, WarpMode warpMode, @Nullable IWaystone fromWaystone) {
        return WaystonesAPI.createDefaultTeleportContext(entity, waystone, warpMode, fromWaystone).flatMap(PlayerWaystoneManager::tryTeleport).ifRight(PlayerWaystoneManager.informRejectedTeleport(entity));
    }

    public static Either<List<Entity>, WaystoneTeleportError> tryTeleport(IWaystoneTeleportContext context) {
        boolean isCreativeMode;
        WarpMode warpMode;
        WaystoneTeleportEvent.Pre event = new WaystoneTeleportEvent.Pre(context);
        Balm.getEvents().fireEvent((Object)event);
        if (event.isCanceled()) {
            return Either.right((Object)new WaystoneTeleportError.CancelledByEvent());
        }
        IWaystone waystone = context.getTargetWaystone();
        Entity entity = context.getEntity();
        if (!PlayerWaystoneManager.canUseWarpMode(entity, warpMode = context.getWarpMode(), context.getWarpItem(), context.getFromWaystone())) {
            return Either.right((Object)new WaystoneTeleportError.WarpModeRejected());
        }
        if (!warpMode.getAllowTeleportPredicate().test(entity, waystone)) {
            return Either.right((Object)new WaystoneTeleportError.WarpModeRejected());
        }
        if (context.isDimensionalTeleport() && !event.getDimensionalTeleportResult().withDefault(() -> PlayerWaystoneManager.canDimensionalWarpBetween(entity, waystone))) {
            return Either.right((Object)new WaystoneTeleportError.DimensionalWarpDenied());
        }
        if (!context.getLeashedEntities().isEmpty()) {
            if (!WaystonesConfig.getActive().restrictions.transportLeashed) {
                return Either.right((Object)new WaystoneTeleportError.LeashedWarpDenied());
            }
            List<ResourceLocation> forbidden = WaystonesConfig.getActive().restrictions.leashedDenyList.stream().map(ResourceLocation::new).toList();
            if (context.getLeashedEntities().stream().anyMatch(e -> forbidden.contains(BuiltInRegistries.f_256780_.m_7981_((Object)e.m_6095_())))) {
                return Either.right((Object)new WaystoneTeleportError.SpecificLeashedWarpDenied());
            }
            if (context.isDimensionalTeleport() && !WaystonesConfig.getActive().restrictions.transportLeashedDimensional) {
                return Either.right((Object)new WaystoneTeleportError.LeashedDimensionalWarpDenied());
            }
        }
        if (entity instanceof Player && ((Player)entity).f_36078_ < context.getXpCost()) {
            return Either.right((Object)new WaystoneTeleportError.NotEnoughXp());
        }
        boolean bl = isCreativeMode = entity instanceof Player && ((Player)entity).m_150110_().f_35937_;
        if (!context.getWarpItem().m_41619_() && event.getConsumeItemResult().withDefault(() -> !isCreativeMode && context.consumesWarpItem())) {
            context.getWarpItem().m_41774_(1);
        }
        if (entity instanceof Player) {
            Player player = (Player)entity;
            PlayerWaystoneManager.applyCooldown(warpMode, player, context.getCooldown());
            PlayerWaystoneManager.applyXpCost(player, context.getXpCost());
        }
        List<Entity> teleportedEntities = PlayerWaystoneManager.doTeleport(context);
        Balm.getEvents().fireEvent((Object)new WaystoneTeleportEvent.Post(context, teleportedEntities));
        return Either.left(teleportedEntities);
    }

    private static void sendHackySyncPacketsAfterTeleport(Entity entity) {
        if (entity instanceof ServerPlayer) {
            ServerPlayer player = (ServerPlayer)entity;
            player.f_8906_.m_9829_((Packet)new ClientboundSetExperiencePacket(player.f_36080_, player.f_36079_, player.f_36078_));
        }
    }

    private static void applyXpCost(Player player, int xpLevelCost) {
        if (xpLevelCost > 0) {
            player.m_6749_(-xpLevelCost);
        }
    }

    private static void applyCooldown(WarpMode warpMode, Player player, int cooldown) {
        if (cooldown > 0) {
            Level level = player.m_9236_();
            switch (warpMode) {
                case INVENTORY_BUTTON: {
                    PlayerWaystoneManager.getPlayerWaystoneData(level).setInventoryButtonCooldownUntil(player, System.currentTimeMillis() + (long)cooldown * 1000L);
                    break;
                }
                case WARP_STONE: {
                    PlayerWaystoneManager.getPlayerWaystoneData(level).setWarpStoneCooldownUntil(player, System.currentTimeMillis() + (long)cooldown * 1000L);
                }
            }
            WaystoneSyncManager.sendWaystoneCooldowns(player);
        }
    }

    public static int getCooldownPeriod(WarpMode warpMode, IWaystone waystone) {
        return (int)((double)PlayerWaystoneManager.getCooldownPeriod(warpMode) * PlayerWaystoneManager.getCooldownMultiplier(waystone));
    }

    private static int getCooldownPeriod(WarpMode warpMode) {
        return switch (warpMode) {
            case WarpMode.INVENTORY_BUTTON -> WaystonesConfig.getActive().cooldowns.inventoryButtonCooldown;
            case WarpMode.WARP_STONE -> WaystonesConfig.getActive().cooldowns.warpStoneCooldown;
            default -> 0;
        };
    }

    private static boolean canDimensionalWarpBetween(Entity player, IWaystone waystone) {
        ResourceLocation fromDimension = player.m_9236_().m_46472_().m_135782_();
        ResourceLocation toDimension = waystone.getDimension().m_135782_();
        List<String> dimensionAllowList = WaystonesConfig.getActive().restrictions.dimensionalWarpAllowList;
        List<String> dimensionDenyList = WaystonesConfig.getActive().restrictions.dimensionalWarpDenyList;
        if (!(dimensionAllowList.isEmpty() || dimensionAllowList.contains(toDimension.toString()) && dimensionAllowList.contains(fromDimension.toString()))) {
            return false;
        }
        if (!dimensionDenyList.isEmpty() && (dimensionDenyList.contains(toDimension.toString()) || dimensionDenyList.contains(fromDimension.toString()))) {
            return false;
        }
        DimensionalWarp dimensionalWarpMode = WaystonesConfig.getActive().restrictions.dimensionalWarp;
        return dimensionalWarpMode == DimensionalWarp.ALLOW || dimensionalWarpMode == DimensionalWarp.GLOBAL_ONLY && waystone.isGlobal();
    }

    public static ItemStack findWarpItem(Entity entity, WarpMode warpMode) {
        return switch (warpMode) {
            case WarpMode.WARP_SCROLL -> PlayerWaystoneManager.findWarpItem(entity, ModItemTags.WARP_SCROLLS);
            case WarpMode.WARP_STONE -> PlayerWaystoneManager.findWarpItem(entity, ModItemTags.WARP_STONES);
            case WarpMode.RETURN_SCROLL -> PlayerWaystoneManager.findWarpItem(entity, ModItemTags.RETURN_SCROLLS);
            case WarpMode.BOUND_SCROLL -> PlayerWaystoneManager.findWarpItem(entity, ModItemTags.BOUND_SCROLLS);
            default -> ItemStack.f_41583_;
        };
    }

    private static ItemStack findWarpItem(Entity entity, TagKey<Item> warpItemTag) {
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            if (livingEntity.m_21205_().m_204117_(warpItemTag)) {
                return livingEntity.m_21205_();
            }
            if (livingEntity.m_21206_().m_204117_(warpItemTag)) {
                return livingEntity.m_21206_();
            }
        }
        return ItemStack.f_41583_;
    }

    public static List<Mob> findLeashedAnimals(Entity player) {
        return player.m_9236_().m_6443_(Mob.class, new AABB(player.m_20183_()).m_82400_(10.0), e -> player.equals((Object)e.m_21524_()));
    }

    public static List<Entity> doTeleport(IWaystoneTeleportContext context) {
        List<Entity> teleportedEntities = PlayerWaystoneManager.teleportEntityAndAttached(context.getEntity(), context);
        context.getAdditionalEntities().forEach(additionalEntity -> teleportedEntities.addAll(PlayerWaystoneManager.teleportEntityAndAttached(additionalEntity, context)));
        ServerLevel sourceWorld = (ServerLevel)context.getEntity().m_9236_();
        BlockPos sourcePos = context.getEntity().m_20183_();
        TeleportDestination destination = context.getDestination();
        ServerLevel targetLevel = destination.getLevel();
        BlockPos targetPos = BlockPos.m_274446_((Position)destination.getLocation());
        BlockEntity targetTileEntity = targetLevel.m_7702_(targetPos);
        if (targetTileEntity instanceof WarpPlateBlockEntity) {
            WarpPlateBlockEntity warpPlate = (WarpPlateBlockEntity)targetTileEntity;
            teleportedEntities.forEach(warpPlate::markEntityForCooldown);
        }
        if (context.playsSound()) {
            sourceWorld.m_5594_(null, sourcePos, SoundEvents.f_12287_, SoundSource.PLAYERS, 0.1f, 1.0f);
            targetLevel.m_5594_(null, targetPos, SoundEvents.f_12287_, SoundSource.PLAYERS, 0.1f, 1.0f);
        }
        if (context.playsEffect()) {
            teleportedEntities.forEach(additionalEntity -> Balm.getNetworking().sendToTracking(sourceWorld, sourcePos, (Object)new TeleportEffectMessage(sourcePos)));
            Balm.getNetworking().sendToTracking(targetLevel, targetPos, (Object)new TeleportEffectMessage(targetPos));
        }
        return teleportedEntities;
    }

    private static List<Entity> teleportEntityAndAttached(Entity entity, IWaystoneTeleportContext context) {
        ArrayList<Entity> teleportedEntities = new ArrayList<Entity>();
        TeleportDestination destination = context.getDestination();
        ServerLevel targetLevel = destination.getLevel();
        Vec3 targetLocation = destination.getLocation();
        Direction targetDirection = destination.getDirection();
        Entity mount = entity.m_20202_();
        Entity teleportedMount = null;
        if (mount != null) {
            teleportedMount = PlayerWaystoneManager.teleportEntity(mount, targetLevel, targetLocation, targetDirection);
            teleportedEntities.add(teleportedMount);
        }
        List<Mob> leashedEntities = context.getLeashedEntities();
        ArrayList teleportedLeashedEntities = new ArrayList();
        leashedEntities.forEach(leashedEntity -> {
            Entity teleportedLeashedEntity = PlayerWaystoneManager.teleportEntity((Entity)leashedEntity, targetLevel, targetLocation, targetDirection);
            teleportedEntities.add(teleportedLeashedEntity);
            teleportedLeashedEntities.add(teleportedLeashedEntity);
        });
        Entity teleportedEntity = PlayerWaystoneManager.teleportEntity(entity, targetLevel, targetLocation, targetDirection);
        teleportedEntities.add(teleportedEntity);
        teleportedLeashedEntities.forEach(teleportedLeashedEntity -> {
            if (teleportedLeashedEntity instanceof Mob) {
                Mob teleportedLeashedMob = (Mob)teleportedLeashedEntity;
                teleportedLeashedMob.m_21463_(teleportedEntity, true);
            }
        });
        if (teleportedMount != null) {
            // empty if block
        }
        return teleportedEntities;
    }

    private static Entity teleportEntity(Entity entity, ServerLevel targetWorld, Vec3 targetPos3d, Direction direction) {
        float yaw = direction.m_122435_();
        double x = targetPos3d.f_82479_;
        double y = targetPos3d.f_82480_;
        double z = targetPos3d.f_82481_;
        if (entity instanceof ServerPlayer) {
            ChunkPos chunkPos = new ChunkPos(BlockPos.m_274561_((double)x, (double)y, (double)z));
            targetWorld.m_7726_().m_8387_(TicketType.f_9448_, chunkPos, 1, (Object)entity.m_19879_());
            entity.m_8127_();
            if (((ServerPlayer)entity).m_5803_()) {
                ((ServerPlayer)entity).m_6145_(true, true);
            }
            if (targetWorld == entity.m_9236_()) {
                ((ServerPlayer)entity).f_8906_.m_9780_(x, y, z, yaw, entity.m_146909_(), Collections.emptySet());
            } else {
                ((ServerPlayer)entity).m_8999_(targetWorld, x, y, z, yaw, entity.m_146909_());
            }
            entity.m_5616_(yaw);
        } else {
            float pitch = Mth.m_14036_((float)entity.m_146909_(), (float)-90.0f, (float)90.0f);
            if (targetWorld == entity.m_9236_()) {
                entity.m_7678_(x, y, z, yaw, pitch);
                entity.m_5616_(yaw);
            } else {
                entity.m_19877_();
                Entity oldEntity = entity;
                entity = entity.m_6095_().m_20615_((Level)targetWorld);
                if (entity == null) {
                    return oldEntity;
                }
                entity.m_20361_(oldEntity);
                entity.m_7678_(x, y, z, yaw, pitch);
                entity.m_5616_(yaw);
                oldEntity.m_142467_(Entity.RemovalReason.CHANGED_DIMENSION);
                targetWorld.m_143334_(entity);
            }
        }
        if (!(entity instanceof LivingEntity) || !((LivingEntity)entity).m_21255_()) {
            entity.m_20256_(entity.m_20184_().m_82542_(1.0, 0.0, 1.0));
            entity.m_6853_(true);
        }
        if (entity instanceof PathfinderMob) {
            ((PathfinderMob)entity).m_21573_().m_26573_();
        }
        PlayerWaystoneManager.sendHackySyncPacketsAfterTeleport(entity);
        return entity;
    }

    public static void deactivateWaystone(Player player, IWaystone waystone) {
        PlayerWaystoneManager.getPlayerWaystoneData(player.m_9236_()).deactivateWaystone(player, waystone);
    }

    private static boolean canUseWarpMode(Entity entity, WarpMode warpMode, ItemStack heldItem, @Nullable IWaystone fromWaystone) {
        return switch (warpMode) {
            default -> throw new IncompatibleClassChangeError();
            case WarpMode.INVENTORY_BUTTON -> {
                if (entity instanceof Player && PlayerWaystoneManager.canUseInventoryButton((Player)entity)) {
                    yield true;
                }
                yield false;
            }
            case WarpMode.WARP_SCROLL -> {
                if (!heldItem.m_41619_() && heldItem.m_204117_(ModItemTags.WARP_SCROLLS)) {
                    yield true;
                }
                yield false;
            }
            case WarpMode.BOUND_SCROLL -> {
                if (!heldItem.m_41619_() && heldItem.m_204117_(ModItemTags.BOUND_SCROLLS)) {
                    yield true;
                }
                yield false;
            }
            case WarpMode.RETURN_SCROLL -> {
                if (!heldItem.m_41619_() && heldItem.m_204117_(ModItemTags.RETURN_SCROLLS)) {
                    yield true;
                }
                yield false;
            }
            case WarpMode.WARP_STONE -> {
                if (!heldItem.m_41619_() && heldItem.m_204117_(ModItemTags.WARP_STONES) && entity instanceof Player && PlayerWaystoneManager.canUseWarpStone((Player)entity, heldItem)) {
                    yield true;
                }
                yield false;
            }
            case WarpMode.WAYSTONE_TO_WAYSTONE -> {
                if (WaystonesConfig.getActive().restrictions.allowWaystoneToWaystoneTeleport && fromWaystone != null && fromWaystone.isValid() && fromWaystone.getWaystoneType().equals((Object)WaystoneTypes.WAYSTONE)) {
                    yield true;
                }
                yield false;
            }
            case WarpMode.SHARESTONE_TO_SHARESTONE -> {
                if (fromWaystone != null && fromWaystone.isValid() && WaystoneTypes.isSharestone(fromWaystone.getWaystoneType())) {
                    yield true;
                }
                yield false;
            }
            case WarpMode.WARP_PLATE -> {
                if (fromWaystone != null && fromWaystone.isValid() && fromWaystone.getWaystoneType().equals((Object)WaystoneTypes.WARP_PLATE)) {
                    yield true;
                }
                yield false;
            }
            case WarpMode.PORTSTONE_TO_WAYSTONE -> {
                if (fromWaystone != null && fromWaystone.isValid() && fromWaystone.getWaystoneType().equals((Object)WaystoneTypes.PORTSTONE)) {
                    yield true;
                }
                yield false;
            }
            case WarpMode.CUSTOM -> true;
        };
    }

    public static long getWarpStoneCooldownUntil(Player player) {
        return PlayerWaystoneManager.getPlayerWaystoneData(player.m_9236_()).getWarpStoneCooldownUntil(player);
    }

    public static long getWarpStoneCooldownLeft(Player player) {
        long cooldownUntil = PlayerWaystoneManager.getWarpStoneCooldownUntil(player);
        return Math.max(0L, cooldownUntil - System.currentTimeMillis());
    }

    public static void setWarpStoneCooldownUntil(Player player, long timeStamp) {
        PlayerWaystoneManager.getPlayerWaystoneData(player.m_9236_()).setWarpStoneCooldownUntil(player, timeStamp);
    }

    public static long getInventoryButtonCooldownUntil(Player player) {
        return PlayerWaystoneManager.getPlayerWaystoneData(player.m_9236_()).getInventoryButtonCooldownUntil(player);
    }

    public static long getInventoryButtonCooldownLeft(Player player) {
        long cooldownUntil = PlayerWaystoneManager.getInventoryButtonCooldownUntil(player);
        return Math.max(0L, cooldownUntil - System.currentTimeMillis());
    }

    public static void setInventoryButtonCooldownUntil(Player player, long timeStamp) {
        PlayerWaystoneManager.getPlayerWaystoneData(player.m_9236_()).setInventoryButtonCooldownUntil(player, timeStamp);
    }

    @Nullable
    public static IWaystone getNearestWaystone(Player player) {
        return PlayerWaystoneManager.getPlayerWaystoneData(player.m_9236_()).getWaystones(player).stream().filter(it -> it.getDimension() == player.m_9236_().m_46472_()).min((first, second) -> {
            double firstDist = first.getPos().m_203198_(player.m_20185_(), player.m_20186_(), player.m_20189_());
            double secondDist = second.getPos().m_203198_(player.m_20185_(), player.m_20186_(), player.m_20189_());
            return (int)Math.round(firstDist) - (int)Math.round(secondDist);
        }).orElse(null);
    }

    public static List<IWaystone> getWaystones(Player player) {
        return PlayerWaystoneManager.getPlayerWaystoneData(player.m_9236_()).getWaystones(player);
    }

    public static IPlayerWaystoneData getPlayerWaystoneData(@Nullable Level world) {
        return world == null || world.f_46443_ ? inMemoryPlayerWaystoneData : persistentPlayerWaystoneData;
    }

    public static IPlayerWaystoneData getPlayerWaystoneData(BalmEnvironment side) {
        return side.isClient() ? inMemoryPlayerWaystoneData : persistentPlayerWaystoneData;
    }

    public static boolean mayTeleportToWaystone(Player player, IWaystone waystone) {
        return true;
    }

    public static void swapWaystoneSorting(Player player, int index, int otherIndex) {
        PlayerWaystoneManager.getPlayerWaystoneData(player.m_9236_()).swapWaystoneSorting(player, index, otherIndex);
    }

    public static boolean mayEditGlobalWaystones(Player player) {
        return player.m_150110_().f_35937_ || !WaystonesConfig.getActive().restrictions.globalWaystoneSetupRequiresCreativeMode;
    }

    public static void activeWaystoneForEveryone(@Nullable MinecraftServer server, IWaystone waystone) {
        if (server == null) {
            return;
        }
        List players = server.m_6846_().m_11314_();
        for (ServerPlayer player : players) {
            if (PlayerWaystoneManager.isWaystoneActivated((Player)player, waystone)) continue;
            PlayerWaystoneManager.activateWaystone((Player)player, waystone);
        }
    }

    public static void removeKnownWaystone(@Nullable MinecraftServer server, IWaystone waystone) {
        if (server == null) {
            return;
        }
        List players = server.m_6846_().m_11314_();
        for (ServerPlayer player : players) {
            PlayerWaystoneManager.deactivateWaystone((Player)player, waystone);
            WaystoneSyncManager.sendActivatedWaystones((Player)player);
        }
    }
}

