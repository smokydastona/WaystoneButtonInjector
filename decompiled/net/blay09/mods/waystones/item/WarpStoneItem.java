/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.Balm
 *  net.blay09.mods.balm.api.menu.BalmMenuProvider
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.item.UseAnim
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.item;

import java.util.List;
import java.util.Random;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.waystones.api.IResetUseOnDamage;
import net.blay09.mods.waystones.compat.Compat;
import net.blay09.mods.waystones.config.WaystonesConfig;
import net.blay09.mods.waystones.core.PlayerWaystoneManager;
import net.blay09.mods.waystones.core.WarpMode;
import net.blay09.mods.waystones.menu.WaystoneSelectionMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class WarpStoneItem
extends Item
implements IResetUseOnDamage {
    private final Random random = new Random();
    private static final BalmMenuProvider containerProvider = new BalmMenuProvider(){

        public Component m_5446_() {
            return Component.m_237115_((String)"container.waystones.waystone_selection");
        }

        public AbstractContainerMenu m_7208_(int i, Inventory playerInventory, Player playerEntity) {
            return WaystoneSelectionMenu.createWaystoneSelection(i, playerEntity, WarpMode.WARP_STONE, null);
        }

        public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
            buf.writeByte(WarpMode.WARP_STONE.ordinal());
        }
    };

    public WarpStoneItem(Item.Properties properties) {
        super(properties.m_41503_(100));
    }

    public int m_8105_(ItemStack itemStack) {
        return WaystonesConfig.getActive().cooldowns.warpStoneUseTime;
    }

    public UseAnim m_6164_(ItemStack itemStack) {
        if (this.m_8105_(itemStack) <= 0 || Compat.isVivecraftInstalled) {
            return UseAnim.NONE;
        }
        return UseAnim.BOW;
    }

    public void m_5929_(Level level, LivingEntity entity, ItemStack itemStack, int remainingTicks) {
        if (level.f_46443_) {
            int i;
            int duration = this.m_8105_(itemStack);
            float progress = (float)(duration - remainingTicks) / (float)duration;
            boolean shouldMirror = entity.m_7655_() == InteractionHand.MAIN_HAND ^ entity.m_5737_() == HumanoidArm.RIGHT;
            Vec3 handOffset = new Vec3(shouldMirror ? (double)0.3f : (double)-0.3f, 1.0, (double)0.52f);
            handOffset = handOffset.m_82524_(-entity.m_146908_() * ((float)Math.PI / 180));
            handOffset = handOffset.m_82535_(entity.m_146909_() * ((float)Math.PI / 180));
            int maxParticles = Math.max(4, (int)(progress * 48.0f));
            if (remainingTicks % 5 == 0) {
                for (i = 0; i < Math.min(4, maxParticles); ++i) {
                    level.m_7106_((ParticleOptions)ParticleTypes.f_123789_, entity.m_20185_() + handOffset.f_82479_ + (this.random.nextDouble() - 0.5) * 0.5, entity.m_20186_() + handOffset.f_82480_ + this.random.nextDouble(), entity.m_20189_() + handOffset.f_82481_ + (this.random.nextDouble() - 0.5) * 0.5, 0.0, (double)0.05f, 0.0);
                }
                if (progress >= 0.25f) {
                    for (i = 0; i < maxParticles; ++i) {
                        level.m_7106_((ParticleOptions)ParticleTypes.f_123784_, entity.m_20185_() + (this.random.nextDouble() - 0.5) * 1.5, entity.m_20186_() + this.random.nextDouble(), entity.m_20189_() + (this.random.nextDouble() - 0.5) * 1.5, 0.0, this.random.nextDouble() * 0.5, 0.0);
                    }
                }
                if (progress >= 0.5f) {
                    for (i = 0; i < maxParticles; ++i) {
                        level.m_7106_((ParticleOptions)ParticleTypes.f_123789_, entity.m_20185_() + (this.random.nextDouble() - 0.5) * 1.5, entity.m_20186_() + this.random.nextDouble(), entity.m_20189_() + (this.random.nextDouble() - 0.5) * 1.5, 0.0, this.random.nextDouble(), 0.0);
                    }
                }
                if (progress >= 0.75f) {
                    for (i = 0; i < maxParticles / 3; ++i) {
                        level.m_7106_((ParticleOptions)ParticleTypes.f_123771_, entity.m_20185_() + (this.random.nextDouble() - 0.5) * 1.5, entity.m_20186_() + 0.5 + this.random.nextDouble(), entity.m_20189_() + (this.random.nextDouble() - 0.5) * 1.5, 0.0, this.random.nextDouble(), 0.0);
                    }
                }
            }
            if (remainingTicks == 1) {
                for (i = 0; i < maxParticles; ++i) {
                    level.m_7106_((ParticleOptions)ParticleTypes.f_123789_, entity.m_20185_() + (this.random.nextDouble() - 0.5) * 1.5, entity.m_20186_() + this.random.nextDouble() + 1.0, entity.m_20189_() + (this.random.nextDouble() - 0.5) * 1.5, (this.random.nextDouble() - 0.5) * 0.0, this.random.nextDouble(), (this.random.nextDouble() - 0.5) * 0.0);
                }
            }
        }
    }

    public ItemStack m_5922_(ItemStack stack, Level world, LivingEntity entity) {
        if (!world.f_46443_ && entity instanceof ServerPlayer) {
            Balm.getNetworking().openGui((Player)((ServerPlayer)entity), (MenuProvider)containerProvider);
        }
        return stack;
    }

    public InteractionResultHolder<ItemStack> m_7203_(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.m_21120_(hand);
        if (player.m_150110_().f_35937_) {
            PlayerWaystoneManager.setWarpStoneCooldownUntil(player, 0L);
        }
        if (PlayerWaystoneManager.canUseWarpStone(player, itemStack)) {
            if (!player.m_6117_() && !world.f_46443_) {
                world.m_6269_(null, (Entity)player, SoundEvents.f_12288_, SoundSource.PLAYERS, 0.1f, 2.0f);
            }
            if (this.m_8105_(itemStack) <= 0 || Compat.isVivecraftInstalled) {
                this.m_5922_(itemStack, world, (LivingEntity)player);
            } else {
                player.m_6672_(hand);
            }
            return new InteractionResultHolder(InteractionResult.SUCCESS, (Object)itemStack);
        }
        MutableComponent chatComponent = Component.m_237115_((String)"chat.waystones.warpstone_not_charged");
        chatComponent.m_130940_(ChatFormatting.RED);
        player.m_5661_((Component)chatComponent, true);
        return new InteractionResultHolder(InteractionResult.FAIL, (Object)itemStack);
    }

    public boolean m_142522_(ItemStack itemStack) {
        return this.m_142158_(itemStack) < 13;
    }

    public int m_142158_(ItemStack itemStack) {
        Player player = Balm.getProxy().getClientPlayer();
        if (player == null) {
            return 13;
        }
        long timeLeft = PlayerWaystoneManager.getWarpStoneCooldownLeft(player);
        int maxCooldown = WaystonesConfig.getActive().cooldowns.warpStoneCooldown * 1000;
        if (maxCooldown == 0) {
            return 13;
        }
        return Math.round(13.0f - (float)timeLeft * 13.0f / (float)maxCooldown);
    }

    public boolean m_5812_(ItemStack itemStack) {
        Player player = Balm.getProxy().getClientPlayer();
        return player != null ? PlayerWaystoneManager.canUseWarpStone(player, itemStack) || super.m_5812_(itemStack) : super.m_5812_(itemStack);
    }

    public void m_7373_(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        Player player = Balm.getProxy().getClientPlayer();
        if (player == null) {
            return;
        }
        long timeLeft = PlayerWaystoneManager.getWarpStoneCooldownLeft(player);
        int secondsLeft = (int)(timeLeft / 1000L);
        if (secondsLeft > 0) {
            MutableComponent secondsLeftText = Component.m_237110_((String)"tooltip.waystones.cooldown_left", (Object[])new Object[]{secondsLeft});
            secondsLeftText.m_130940_(ChatFormatting.GOLD);
            list.add((Component)secondsLeftText);
        }
    }
}

