/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.level.block.Block
 */
package net.blay09.mods.waystones.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
    public static final TagKey<Block> IS_TELEPORT_TARGET = TagKey.m_203882_((ResourceKey)Registries.f_256747_, (ResourceLocation)new ResourceLocation("waystones", "is_teleport_target"));
    public static final TagKey<Block> WAYSTONES = TagKey.m_203882_((ResourceKey)Registries.f_256747_, (ResourceLocation)new ResourceLocation("waystones", "waystones"));
    public static final TagKey<Block> SHARESTONES = TagKey.m_203882_((ResourceKey)Registries.f_256747_, (ResourceLocation)new ResourceLocation("waystones", "sharestones"));
    public static final TagKey<Block> DYED_SHARESTONES = TagKey.m_203882_((ResourceKey)Registries.f_256747_, (ResourceLocation)new ResourceLocation("waystones", "dyed_sharestones"));
}

