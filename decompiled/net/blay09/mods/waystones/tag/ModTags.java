/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.block.Block
 */
package net.blay09.mods.waystones.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@Deprecated
public class ModTags {
    public static final TagKey<Item> BOUND_SCROLLS = TagKey.m_203882_((ResourceKey)Registries.f_256913_, (ResourceLocation)new ResourceLocation("waystones", "bound_scrolls"));
    public static final TagKey<Item> RETURN_SCROLLS = TagKey.m_203882_((ResourceKey)Registries.f_256913_, (ResourceLocation)new ResourceLocation("waystones", "return_scrolls"));
    public static final TagKey<Item> WARP_SCROLLS = TagKey.m_203882_((ResourceKey)Registries.f_256913_, (ResourceLocation)new ResourceLocation("waystones", "warp_scrolls"));
    public static final TagKey<Item> WARP_STONES = TagKey.m_203882_((ResourceKey)Registries.f_256913_, (ResourceLocation)new ResourceLocation("waystones", "warp_stones"));
    public static final TagKey<Block> IS_TELEPORT_TARGET = TagKey.m_203882_((ResourceKey)Registries.f_256747_, (ResourceLocation)new ResourceLocation("waystones", "is_teleport_target"));
    public static final TagKey<Block> WAYSTONES = TagKey.m_203882_((ResourceKey)Registries.f_256747_, (ResourceLocation)new ResourceLocation("waystones", "waystones"));
    public static final TagKey<Block> SHARESTONES = TagKey.m_203882_((ResourceKey)Registries.f_256747_, (ResourceLocation)new ResourceLocation("waystones", "sharestones"));
}

