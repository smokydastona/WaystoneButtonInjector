/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.crafting.Recipe
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraftforge.registries.DeferredRegister
 *  net.minecraftforge.registries.ForgeRegistries
 *  net.minecraftforge.registries.RegistryObject
 */
package net.blay09.mods.balm.forge.recipe;

import java.util.function.Function;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.balm.forge.DeferredRegisters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ForgeBalmRecipes
implements BalmRecipes {
    @Override
    public <T extends Recipe<?>> DeferredObject<RecipeType<T>> registerRecipeType(Function<ResourceLocation, RecipeType<T>> supplier, ResourceLocation identifier) {
        DeferredRegister register = DeferredRegisters.get(ForgeRegistries.RECIPE_TYPES, identifier.m_135827_());
        RegistryObject registryObject = register.register(identifier.m_135815_(), () -> (RecipeType)supplier.apply(identifier));
        return new DeferredObject<RecipeType<T>>(identifier, registryObject, () -> ((RegistryObject)registryObject).isPresent());
    }

    @Override
    public <T extends Recipe<?>> DeferredObject<RecipeSerializer<T>> registerRecipeSerializer(Supplier<RecipeSerializer<T>> supplier, ResourceLocation identifier) {
        DeferredRegister register = DeferredRegisters.get(ForgeRegistries.RECIPE_SERIALIZERS, identifier.m_135827_());
        RegistryObject registryObject = register.register(identifier.m_135815_(), supplier);
        return new DeferredObject<RecipeSerializer<T>>(identifier, registryObject, () -> ((RegistryObject)registryObject).isPresent());
    }
}

