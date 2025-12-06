/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.event.LivingDamageEvent
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 */
package net.blay09.mods.waystones.handler;

import net.blay09.mods.balm.api.event.LivingDamageEvent;
import net.blay09.mods.waystones.api.IResetUseOnDamage;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class WarpDamageResetHandler {
    public static void onDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player && entity.m_21211_().m_41720_() instanceof IResetUseOnDamage) {
            entity.m_5810_();
        }
    }
}

