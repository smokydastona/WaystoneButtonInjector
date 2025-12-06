/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.effect.MobEffect
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.material.Fluid
 */
package net.blay09.mods.balm.api;

import java.util.Collection;
import java.util.function.Function;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public interface BalmRegistries {
    @Deprecated(forRemoval=true, since="1.21.5")
    default public ResourceLocation getKey(Item item) {
        return BuiltInRegistries.f_257033_.m_7981_((Object)item);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public ResourceLocation getKey(Block block) {
        return BuiltInRegistries.f_256975_.m_7981_((Object)block);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public ResourceLocation getKey(Fluid fluid) {
        return BuiltInRegistries.f_257020_.m_7981_((Object)fluid);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public ResourceLocation getKey(EntityType<?> entityType) {
        return BuiltInRegistries.f_256780_.m_7981_(entityType);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public ResourceLocation getKey(MenuType<?> menuType) {
        return BuiltInRegistries.f_256818_.m_7981_(menuType);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public Collection<ResourceLocation> getItemKeys() {
        return BuiltInRegistries.f_257033_.m_6566_();
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public Item getItem(ResourceLocation key) {
        return (Item)BuiltInRegistries.f_257033_.m_7745_(key);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public Block getBlock(ResourceLocation key) {
        return (Block)BuiltInRegistries.f_256975_.m_7745_(key);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public Fluid getFluid(ResourceLocation key) {
        return (Fluid)BuiltInRegistries.f_257020_.m_7745_(key);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public MobEffect getMobEffect(ResourceLocation key) {
        return (MobEffect)BuiltInRegistries.f_256974_.m_7745_(key);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public TagKey<Item> getItemTag(ResourceLocation key) {
        return TagKey.m_203882_((ResourceKey)Registries.f_256913_, (ResourceLocation)key);
    }

    @Deprecated(forRemoval=true, since="1.21.5")
    default public Attribute getAttribute(ResourceLocation key) {
        return (Attribute)BuiltInRegistries.f_256951_.m_7745_(key);
    }

    public void enableMilkFluid();

    public Fluid getMilkFluid();

    public <T> DeferredObject<T> register(Registry<T> var1, Function<ResourceLocation, T> var2, ResourceLocation var3);
}

