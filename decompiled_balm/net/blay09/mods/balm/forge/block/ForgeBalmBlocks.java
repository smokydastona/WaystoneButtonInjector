/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.block.Block
 *  net.minecraftforge.registries.DeferredRegister
 *  net.minecraftforge.registries.RegistryObject
 *  org.jetbrains.annotations.Nullable
 */
package net.blay09.mods.balm.forge.block;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlocks;
import net.blay09.mods.balm.api.item.BalmItems;
import net.blay09.mods.balm.common.NamespaceResolver;
import net.blay09.mods.balm.common.StaticNamespaceResolver;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public record ForgeBalmBlocks(NamespaceResolver namespaceResolver, BalmItems items) implements BalmBlocks
{
    @Override
    public DeferredObject<Block> registerBlock(Function<ResourceLocation, Block> supplier, ResourceLocation identifier) {
        DeferredRegister register = DeferredRegisters.get(Registries.f_256747_, identifier.m_135827_());
        RegistryObject registryObject = register.register(identifier.m_135815_(), () -> (Block)supplier.apply(identifier));
        return new DeferredObject<Block>(identifier, (Supplier<Block>)registryObject, () -> ((RegistryObject)registryObject).isPresent());
    }

    @Override
    public DeferredObject<Item> registerBlockItem(Function<ResourceLocation, BlockItem> supplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        return this.items.registerItem(supplier::apply, identifier, creativeTab);
    }

    @Override
    public void register(Function<ResourceLocation, Block> blockSupplier, BiFunction<Block, ResourceLocation, BlockItem> blockItemSupplier, ResourceLocation identifier, @Nullable ResourceLocation creativeTab) {
        DeferredObject<Block> deferredBlock = this.registerBlock(blockSupplier, identifier);
        this.registerBlockItem((ResourceLocation id) -> (BlockItem)blockItemSupplier.apply((Block)deferredBlock.get(), (ResourceLocation)id), identifier, creativeTab);
    }

    @Override
    public BalmBlocks scoped(String modId) {
        return new ForgeBalmBlocks(new StaticNamespaceResolver(modId), this.items.scoped(modId));
    }
}

