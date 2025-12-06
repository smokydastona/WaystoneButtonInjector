/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.DyeColor
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.waystones.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;

public class WaystoneTypes {
    public static final ResourceLocation WAYSTONE = new ResourceLocation("waystones", "waystone");
    public static final ResourceLocation WARP_PLATE = new ResourceLocation("waystones", "warp_plate");
    public static final ResourceLocation PORTSTONE = new ResourceLocation("waystones", "portstone");
    private static final ResourceLocation SHARESTONE = new ResourceLocation("waystones", "sharestone");

    public static ResourceLocation getSharestone(@Nullable DyeColor color) {
        if (color == null) {
            return SHARESTONE;
        }
        return new ResourceLocation("waystones", color.m_7912_() + "_sharestone");
    }

    public static boolean isSharestone(ResourceLocation waystoneType) {
        return waystoneType.equals((Object)SHARESTONE) || waystoneType.m_135815_().endsWith("_sharestone");
    }
}

