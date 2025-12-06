/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.blay09.mods.balm.api.container.ImplementedContainer
 *  net.blay09.mods.balm.api.menu.BalmMenuProvider
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.NonNullList
 *  net.minecraft.core.RegistryAccess
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.util.Mth
 *  net.minecraft.world.Container
 *  net.minecraft.world.ContainerHelper
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.effect.MobEffectInstance
 *  net.minecraft.world.effect.MobEffects
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntitySelector
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.ContainerData
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.level.ServerLevelAccessor
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  net.minecraft.world.phys.AABB
 *  org.jetbrains.annotations.Nullable
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.blay09.mods.waystones.block.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.WeakHashMap;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.container.ImplementedContainer;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.waystones.api.IMutableWaystone;
import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.api.WaystoneOrigin;
import net.blay09.mods.waystones.api.WaystonesAPI;
import net.blay09.mods.waystones.block.WarpPlateBlock;
import net.blay09.mods.waystones.block.entity.ModBlockEntities;
import net.blay09.mods.waystones.block.entity.WaystoneBlockEntityBase;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.core.Waystone;
import net.blay09.mods.waystones.core.WaystoneSyncManager;
import net.blay09.mods.waystones.core.WaystoneTypes;
import net.blay09.mods.waystones.menu.WarpPlateContainer;
import net.blay09.mods.waystones.recipe.ModRecipes;
import net.blay09.mods.waystones.recipe.WarpPlateRecipe;
import net.blay09.mods.waystones.tag.ModItemTags;
import net.blay09.mods.waystones.worldgen.namegen.NameGenerationMode;
import net.blay09.mods.waystones.worldgen.namegen.NameGenerator;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WarpPlateBlockEntity
extends WaystoneBlockEntityBase
implements ImplementedContainer {
    private static final Logger logger = LoggerFactory.getLogger(WarpPlateBlockEntity.class);
    private final WeakHashMap<Entity, Integer> ticksPassedPerEntity = new WeakHashMap();
    private final Random random = new Random();
    private final ContainerData dataAccess;
    private final NonNullList<ItemStack> items = NonNullList.m_122780_((int)5, (Object)ItemStack.f_41583_);
    private int attunementTicks;
    private boolean readyForAttunement;
    private boolean completedFirstAttunement;
    private int lastAttunementSlot;

    public WarpPlateBlockEntity(BlockPos blockPos, BlockState blockState) {
        super((BlockEntityType)ModBlockEntities.warpPlate.get(), blockPos, blockState);
        this.dataAccess = new ContainerData(){

            public int m_6413_(int i) {
                if (i == 0) {
                    return WarpPlateBlockEntity.this.attunementTicks;
                }
                if (i == 1) {
                    return WarpPlateBlockEntity.this.getMaxAttunementTicks();
                }
                if (i == 2) {
                    return WarpPlateBlockEntity.this.isCompletedFirstAttunement() ? 1 : 0;
                }
                return 0;
            }

            public void m_8050_(int i, int j) {
                if (i == 0) {
                    WarpPlateBlockEntity.this.attunementTicks = j;
                } else if (i == 1 || i == 2) {
                    // empty if block
                }
            }

            public int m_6499_() {
                return 3;
            }
        };
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public ItemStack m_7407_(int slot, int count) {
        if (!this.completedFirstAttunement) {
            return ItemStack.f_41583_;
        }
        return super.m_7407_(slot, count);
    }

    public ItemStack m_8016_(int slot) {
        if (!this.completedFirstAttunement) {
            return ItemStack.f_41583_;
        }
        return super.m_8016_(slot);
    }

    @Override
    public void initializeFromExisting(ServerLevelAccessor world, Waystone existingWaystone, ItemStack itemStack) {
        super.initializeFromExisting(world, existingWaystone, itemStack);
        CompoundTag tag = itemStack.m_41783_();
        boolean bl = this.completedFirstAttunement = tag != null && tag.m_128471_("CompletedFirstAttunement");
        if (!this.completedFirstAttunement) {
            this.initializeInventory(world);
        }
    }

    @Override
    public void initializeWaystone(ServerLevelAccessor world, @Nullable LivingEntity player, WaystoneOrigin origin) {
        super.initializeWaystone(world, player, origin);
        IWaystone waystone = this.getWaystone();
        if (waystone instanceof IMutableWaystone) {
            String name = NameGenerator.get(world.m_7654_()).getName(waystone, world.m_213780_(), NameGenerationMode.RANDOM_ONLY);
            ((IMutableWaystone)((Object)waystone)).setName(name);
        }
        WaystoneSyncManager.sendWaystoneUpdateToAll(world.m_7654_(), waystone);
        this.initializeInventory(world);
    }

    private void initializeInventory(ServerLevelAccessor levelAccessor) {
        WarpPlateRecipe initializingRecipe = levelAccessor.m_6018_().m_7465_().m_44013_(ModRecipes.warpPlateRecipeType).stream().filter(recipe -> recipe.m_6423_().m_135827_().equals("waystones") && recipe.m_6423_().m_135815_().equals("attuned_shard")).findFirst().orElse(null);
        if (initializingRecipe == null) {
            logger.error("Failed to find Warp Plate recipe for initial attunement");
            this.completedFirstAttunement = true;
            return;
        }
        for (int i = 0; i < 5; ++i) {
            Ingredient ingredient = (Ingredient)initializingRecipe.m_7527_().get(i);
            ItemStack[] ingredientItems = ingredient.m_43908_();
            ItemStack ingredientItem = ingredientItems.length > 0 ? ingredientItems[0] : ItemStack.f_41583_;
            this.m_6836_(i, ingredientItem.m_41777_());
        }
    }

    @Override
    protected ResourceLocation getWaystoneType() {
        return WaystoneTypes.WARP_PLATE;
    }

    @Override
    public void m_183515_(CompoundTag tag) {
        super.m_183515_(tag);
        ContainerHelper.m_18973_((CompoundTag)tag, this.items);
        tag.m_128379_("ReadyForAttunement", this.readyForAttunement);
        tag.m_128379_("CompletedFirstAttunement", this.completedFirstAttunement);
        tag.m_128405_("LastAttunementSlot", this.lastAttunementSlot);
    }

    @Override
    public void m_142466_(CompoundTag compound) {
        super.m_142466_(compound);
        ContainerHelper.m_18980_((CompoundTag)compound, this.items);
        this.readyForAttunement = compound.m_128471_("ReadyForAttunement");
        this.completedFirstAttunement = compound.m_128471_("CompletedFirstAttunement");
        this.lastAttunementSlot = compound.m_128451_("LastAttunementSlot");
    }

    public BalmMenuProvider getMenuProvider() {
        return new BalmMenuProvider(){

            public Component m_5446_() {
                return Component.m_237115_((String)"container.waystones.warp_plate");
            }

            public AbstractContainerMenu m_7208_(int i, Inventory playerInventory, Player player) {
                WarpPlateBlockEntity.this.markReadyForAttunement();
                return new WarpPlateContainer(i, playerInventory, WarpPlateBlockEntity.this.getWaystone(), (Container)WarpPlateBlockEntity.this, WarpPlateBlockEntity.this.dataAccess);
            }

            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                Waystone.write(buf, WarpPlateBlockEntity.this.getWaystone());
            }
        };
    }

    @Override
    public MenuProvider getSettingsMenuProvider() {
        return null;
    }

    public void onEntityCollision(Entity entity) {
        Integer ticksPassed = this.ticksPassedPerEntity.putIfAbsent(entity, 0);
        if (ticksPassed == null || ticksPassed != -1) {
            WarpPlateBlock.WarpPlateStatus status = this.getTargetWaystone().filter(IWaystone::isValid).map(it -> WarpPlateBlock.WarpPlateStatus.ACTIVE).orElse(WarpPlateBlock.WarpPlateStatus.INVALID);
            this.f_58857_.m_7731_(this.f_58858_, (BlockState)((BlockState)this.m_58900_().m_61124_((Property)WarpPlateBlock.ACTIVE, (Comparable)Boolean.valueOf(true))).m_61124_(WarpPlateBlock.STATUS, (Comparable)((Object)status)), 3);
        }
    }

    private boolean isEntityOnWarpPlate(Entity entity) {
        return entity.m_20185_() >= (double)this.f_58858_.m_123341_() && entity.m_20185_() < (double)(this.f_58858_.m_123341_() + 1) && entity.m_20186_() >= (double)this.f_58858_.m_123342_() && entity.m_20186_() < (double)(this.f_58858_.m_123342_() + 1) && entity.m_20189_() >= (double)this.f_58858_.m_123343_() && entity.m_20189_() < (double)(this.f_58858_.m_123343_() + 1);
    }

    public void serverTick() {
        AABB boundsAbove;
        List entities;
        WarpPlateRecipe recipe = this.trySelectRecipe();
        if (recipe != null) {
            ++this.attunementTicks;
            if (this.attunementTicks >= this.getMaxAttunementTicks()) {
                this.attunementTicks = 0;
                ItemStack attunedShard = recipe.m_5874_((Container)this, (RegistryAccess)RegistryAccess.f_243945_);
                WaystonesAPI.setBoundWaystone(attunedShard, this.getWaystone());
                ItemStack centerStack = this.m_8020_(0);
                if (centerStack.m_41613_() > 1) {
                    centerStack = centerStack.m_255036_(centerStack.m_41613_() - 1);
                    if (!Minecraft.m_91087_().f_91074_.m_150109_().m_36054_(centerStack)) {
                        Minecraft.m_91087_().f_91074_.m_36176_(centerStack, false);
                    }
                }
                this.m_6836_(0, attunedShard);
                for (int i = 1; i <= 4; ++i) {
                    this.m_8020_(i).m_41774_(1);
                }
                this.completedFirstAttunement = true;
            }
        } else {
            this.attunementTicks = 0;
        }
        if (this.m_58900_().m_61143_(WarpPlateBlock.STATUS) != WarpPlateBlock.WarpPlateStatus.IDLE && (entities = this.f_58857_.m_6249_((Entity)null, boundsAbove = new AABB((double)this.f_58858_.m_123341_(), (double)this.f_58858_.m_123342_(), (double)this.f_58858_.m_123343_(), (double)(this.f_58858_.m_123341_() + 1), (double)(this.f_58858_.m_123342_() + 1), (double)(this.f_58858_.m_123343_() + 1)), EntitySelector.f_20402_)).isEmpty()) {
            this.f_58857_.m_7731_(this.f_58858_, (BlockState)((BlockState)this.m_58900_().m_61124_((Property)WarpPlateBlock.ACTIVE, (Comparable)Boolean.valueOf(false))).m_61124_(WarpPlateBlock.STATUS, (Comparable)((Object)WarpPlateBlock.WarpPlateStatus.IDLE)), 3);
            this.ticksPassedPerEntity.clear();
        }
        int useTime = this.getWarpPlateUseTime();
        Iterator<Map.Entry<Entity, Integer>> iterator = this.ticksPassedPerEntity.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Entity, Integer> entry = iterator.next();
            Entity entity = entry.getKey();
            Integer ticksPassed = entry.getValue();
            if (!entity.m_6084_() || !this.isEntityOnWarpPlate(entity)) {
                iterator.remove();
                continue;
            }
            if (ticksPassed > useTime) {
                ItemStack targetAttunementStack = this.getTargetAttunementStack();
                IWaystone targetWaystone = WaystonesAPI.getBoundWaystone(targetAttunementStack).orElse(null);
                if (targetWaystone != null && targetWaystone.isValid()) {
                    this.teleportToWarpPlate(entity, targetWaystone, targetAttunementStack);
                }
                if (entity instanceof Player) {
                    if (targetWaystone == null) {
                        chatComponent = Component.m_237115_((String)"chat.waystones.warp_plate_has_no_target");
                        chatComponent.m_130940_(ChatFormatting.DARK_RED);
                        ((Player)entity).m_5661_((Component)chatComponent, true);
                    } else if (!targetWaystone.isValid()) {
                        chatComponent = Component.m_237115_((String)"chat.waystones.warp_plate_has_invalid_target");
                        chatComponent.m_130940_(ChatFormatting.DARK_RED);
                        ((Player)entity).m_5661_((Component)chatComponent, true);
                    }
                }
                iterator.remove();
                continue;
            }
            if (ticksPassed == -1) continue;
            entry.setValue(ticksPassed + 1);
        }
    }

    private int getWarpPlateUseTime() {
        float useTimeMultiplier = 1.0f;
        for (int i = 0; i < this.m_6643_(); ++i) {
            ItemStack itemStack = this.m_8020_(i);
            if (itemStack.m_41720_() == Items.f_151049_) {
                useTimeMultiplier -= 0.016f * (float)itemStack.m_41613_();
                continue;
            }
            if (itemStack.m_41720_() != Items.f_42518_) continue;
            useTimeMultiplier += 0.016f * (float)itemStack.m_41613_();
        }
        int configuredUseTime = WaystonesConfig.getActive().cooldowns.warpPlateUseTime;
        return Mth.m_14045_((int)((int)((float)configuredUseTime * useTimeMultiplier)), (int)1, (int)(configuredUseTime * 2));
    }

    private void teleportToWarpPlate(Entity entity, IWaystone targetWaystone, ItemStack targetAttunementStack) {
        WaystonesAPI.createDefaultTeleportContext(entity, targetWaystone, WarpMode.WARP_PLATE, this.getWaystone()).flatMap(ctx -> {
            ctx.setWarpItem(targetAttunementStack);
            ctx.setConsumesWarpItem(targetAttunementStack.m_204117_(ModItemTags.SINGLE_USE_WARP_SHARDS));
            return PlayerWaystoneManager.tryTeleport(ctx);
        }).ifRight(PlayerWaystoneManager.informRejectedTeleport(entity)).ifLeft(entities -> entities.forEach(this::applyWarpPlateEffects)).left();
    }

    private void applyWarpPlateEffects(Entity entity) {
        int fireSeconds = 0;
        int poisonSeconds = 0;
        int blindSeconds = 0;
        int featherFallSeconds = 0;
        int fireResistanceSeconds = 0;
        int witherSeconds = 0;
        int potency = 1;
        ArrayList<ItemStack> curativeItems = new ArrayList<ItemStack>();
        for (int i = 0; i < this.m_6643_(); ++i) {
            ItemStack itemStack = this.m_8020_(i);
            if (itemStack.m_41720_() == Items.f_42593_) {
                fireSeconds += itemStack.m_41613_();
                continue;
            }
            if (itemStack.m_41720_() == Items.f_42675_) {
                poisonSeconds += itemStack.m_41613_();
                continue;
            }
            if (itemStack.m_41720_() == Items.f_42532_) {
                blindSeconds += itemStack.m_41613_();
                continue;
            }
            if (itemStack.m_41720_() == Items.f_42455_ || itemStack.m_41720_() == Items.f_42788_) {
                curativeItems.add(itemStack);
                continue;
            }
            if (itemStack.m_41720_() == Items.f_42415_) {
                potency = Math.min(4, potency + itemStack.m_41613_());
                continue;
            }
            if (itemStack.m_41720_() == Items.f_42402_) {
                featherFallSeconds = Math.min(8, featherFallSeconds + itemStack.m_41613_());
                continue;
            }
            if (itemStack.m_41720_() == Items.f_42542_) {
                fireResistanceSeconds = Math.min(8, fireResistanceSeconds + itemStack.m_41613_());
                continue;
            }
            if (itemStack.m_41720_() != Items.f_41951_) continue;
            witherSeconds += itemStack.m_41613_();
        }
        if (entity instanceof LivingEntity) {
            if (fireSeconds > 0) {
                entity.m_20254_(fireSeconds);
            }
            if (poisonSeconds > 0) {
                ((LivingEntity)entity).m_7292_(new MobEffectInstance(MobEffects.f_19614_, poisonSeconds * 20, potency));
            }
            if (blindSeconds > 0) {
                ((LivingEntity)entity).m_7292_(new MobEffectInstance(MobEffects.f_19610_, blindSeconds * 20, potency));
            }
            if (featherFallSeconds > 0) {
                ((LivingEntity)entity).m_7292_(new MobEffectInstance(MobEffects.f_19591_, featherFallSeconds * 20, potency));
            }
            if (fireResistanceSeconds > 0) {
                ((LivingEntity)entity).m_7292_(new MobEffectInstance(MobEffects.f_19607_, fireResistanceSeconds * 20, potency));
            }
            if (witherSeconds > 0) {
                ((LivingEntity)entity).m_7292_(new MobEffectInstance(MobEffects.f_19615_, witherSeconds * 20, potency));
            }
            for (ItemStack curativeItem : curativeItems) {
                Balm.getHooks().curePotionEffects((LivingEntity)entity, curativeItem);
            }
        }
    }

    @Nullable
    private WarpPlateRecipe trySelectRecipe() {
        if (!this.readyForAttunement || this.f_58857_ == null) {
            return null;
        }
        if (this.m_8020_(0).m_41613_() > 1) {
            return null;
        }
        return this.f_58857_.m_7465_().m_44015_(ModRecipes.warpPlateRecipeType, (Container)this, this.f_58857_).orElse(null);
    }

    public ItemStack getTargetAttunementStack() {
        boolean shouldRoundRobin = false;
        boolean shouldPrioritizeSingleUseShards = false;
        ArrayList<ItemStack> attunedShards = new ArrayList<ItemStack>();
        for (int i = 0; i < this.m_6643_(); ++i) {
            ItemStack itemStack = this.m_8020_(i);
            if (itemStack.m_204117_(ModItemTags.WARP_SHARDS)) {
                IWaystone waystoneAttunedTo = WaystonesAPI.getBoundWaystone(itemStack).orElse(null);
                if (waystoneAttunedTo == null || waystoneAttunedTo.getWaystoneUid().equals(this.getWaystone().getWaystoneUid())) continue;
                attunedShards.add(itemStack);
                continue;
            }
            if (itemStack.m_41720_() == Items.f_42692_) {
                shouldRoundRobin = true;
                continue;
            }
            if (itemStack.m_41720_() != Items.f_42591_) continue;
            shouldPrioritizeSingleUseShards = true;
        }
        if (shouldPrioritizeSingleUseShards && attunedShards.stream().anyMatch(stack -> stack.m_204117_(ModItemTags.SINGLE_USE_WARP_SHARDS))) {
            attunedShards.removeIf(stack -> !stack.m_204117_(ModItemTags.SINGLE_USE_WARP_SHARDS));
        }
        if (!attunedShards.isEmpty()) {
            this.lastAttunementSlot = (this.lastAttunementSlot + 1) % attunedShards.size();
            return shouldRoundRobin ? (ItemStack)attunedShards.get(this.lastAttunementSlot) : (ItemStack)attunedShards.get(this.random.nextInt(attunedShards.size()));
        }
        return ItemStack.f_41583_;
    }

    public Optional<IWaystone> getTargetWaystone() {
        return WaystonesAPI.getBoundWaystone(this.getTargetAttunementStack());
    }

    public int getMaxAttunementTicks() {
        return 30;
    }

    public void markReadyForAttunement() {
        this.readyForAttunement = true;
    }

    public void markEntityForCooldown(Entity entity) {
        this.ticksPassedPerEntity.put(entity, -1);
    }

    public boolean isCompletedFirstAttunement() {
        return this.completedFirstAttunement;
    }

    public ContainerData getContainerData() {
        return this.dataAccess;
    }

    public boolean m_7013_(int index, ItemStack stack) {
        if (index == 0 && !this.m_8020_(0).m_41619_()) {
            return false;
        }
        return super.m_7013_(index, stack);
    }

    public boolean m_6542_(Player player) {
        return Container.m_272074_((BlockEntity)this, (Player)player);
    }
}

