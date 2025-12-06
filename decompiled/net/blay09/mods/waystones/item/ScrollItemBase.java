/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.UseAnim
 *  net.minecraft.world.level.Level
 */
package net.blay09.mods.waystones.item;

import java.util.Random;
import net.blay09.mods.waystones.compat.Compat;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class ScrollItemBase
extends Item {
    private static final Random random = new Random();

    public ScrollItemBase(Item.Properties properties) {
        super(properties);
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
            int maxParticles = Math.max(4, (int)(progress * 48.0f));
            if (remainingTicks % 5 == 0) {
                for (i = 0; i < maxParticles; ++i) {
                    level.m_7106_((ParticleOptions)ParticleTypes.f_123789_, entity.m_20185_() + (random.nextDouble() - 0.5) * 1.5, entity.m_20186_() + random.nextDouble(), entity.m_20189_() + (random.nextDouble() - 0.5) * 1.5, 0.0, random.nextDouble(), 0.0);
                }
                if (progress >= 0.25f) {
                    for (i = 0; i < maxParticles; ++i) {
                        level.m_7106_((ParticleOptions)ParticleTypes.f_123784_, entity.m_20185_() + (random.nextDouble() - 0.5) * 1.5, entity.m_20186_() + random.nextDouble(), entity.m_20189_() + (random.nextDouble() - 0.5) * 1.5, 0.0, random.nextDouble(), 0.0);
                    }
                }
                if (progress >= 0.5f) {
                    for (i = 0; i < maxParticles / 3; ++i) {
                        level.m_7106_((ParticleOptions)ParticleTypes.f_123771_, entity.m_20185_() + (random.nextDouble() - 0.5) * 1.5, entity.m_20186_() + 0.5 + random.nextDouble(), entity.m_20189_() + (random.nextDouble() - 0.5) * 1.5, 0.0, random.nextDouble(), 0.0);
                    }
                }
            }
            if (remainingTicks == 1) {
                for (i = 0; i < maxParticles; ++i) {
                    level.m_7106_((ParticleOptions)ParticleTypes.f_123760_, entity.m_20185_() + (random.nextDouble() - 0.5) * 1.5, entity.m_20186_() + random.nextDouble() + 1.0, entity.m_20189_() + (random.nextDouble() - 0.5) * 1.5, (random.nextDouble() - 0.5) * 0.0, random.nextDouble(), (random.nextDouble() - 0.5) * 0.0);
                }
            }
        }
    }
}

