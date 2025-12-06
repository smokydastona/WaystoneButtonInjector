/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.crafting.Recipe
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.minecraft.world.item.crafting.RecipeType
 */
package net.blay09.mods.balm.api.recipe;

import java.util.function.Function;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public interface BalmRecipes {
    public <T extends Recipe<?>> DeferredObject<RecipeType<T>> registerRecipeType(Function<ResourceLocation, RecipeType<T>> var1, ResourceLocation var2);

    public <T extends Recipe<?>> DeferredObject<RecipeSerializer<T>> registerRecipeSerializer(Supplier<RecipeSerializer<T>> var1, ResourceLocation var2);

    @Deprecated
    default public <T extends Recipe<?>> DeferredObject<RecipeType<T>> registerRecipeType(Supplier<RecipeType<T>> typeSupplier, Supplier<RecipeSerializer<T>> serializerSupplier, ResourceLocation identifier) {
        this.registerRecipeSerializer(serializerSupplier, identifier);
        return this.registerRecipeType(id -> (RecipeType)typeSupplier.get(), identifier);
    }
}

