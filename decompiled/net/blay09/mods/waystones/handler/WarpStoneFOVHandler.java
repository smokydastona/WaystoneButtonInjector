/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.event.client.FovUpdateEvent
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package net.blay09.mods.waystones.handler;

import net.blay09.mods.balm.api.event.client.FovUpdateEvent;
import net.blay09.mods.waystones.api.IFOVOnUse;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class WarpStoneFOVHandler {
    public static void onFOV(FovUpdateEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack activeItemStack = entity.m_21211_();
        if (WarpStoneFOVHandler.isScrollItem(activeItemStack)) {
            float fov = (float)entity.m_21212_() / 32.0f * 2.0f;
            event.setFov(Float.valueOf((float)Mth.m_14139_((double)((Double)Minecraft.m_91087_().f_91066_.m_231925_().m_231551_()), (double)1.0, (double)fov)));
        }
    }

    private static boolean isScrollItem(ItemStack activeItemStack) {
        return !activeItemStack.m_41619_() && activeItemStack.m_41720_() instanceof IFOVOnUse;
    }
}

