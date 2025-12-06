/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.blay09.mods.balm.api.recipe.BalmRecipes
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.minecraft.world.item.crafting.RecipeType
 */
package net.blay09.mods.waystones.recipe;

import net.blay09.mods.balm.api.recipe.BalmRecipes;
import net.blay09.mods.waystones.recipe.WarpPlateRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipes {
    public static final String WARP_PLATE_RECIPE_GROUP = "warp_plate";
    public static final ResourceLocation WARP_PLATE_RECIPE_TYPE = new ResourceLocation("waystones", "warp_plate");
    public static RecipeType<WarpPlateRecipe> warpPlateRecipeType;
    public static RecipeSerializer<WarpPlateRecipe> warpPlateRecipeSerializer;

    public static void initialize(BalmRecipes registry) {
        registry.registerRecipeType(() -> {
            warpPlateRecipeType = new RecipeType<WarpPlateRecipe>(){

                public String toString() {
                    return ModRecipes.WARP_PLATE_RECIPE_GROUP;
                }
            };
            return warpPlateRecipeType;
        }, () -> {
            warpPlateRecipeSerializer = new WarpPlateRecipe.Serializer();
            return warpPlateRecipeSerializer;
        }, WARP_PLATE_RECIPE_TYPE);
    }
}

