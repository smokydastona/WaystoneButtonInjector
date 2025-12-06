/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.IModPlugin
 *  mezz.jei.api.JeiPlugin
 *  mezz.jei.api.recipe.category.IRecipeCategory
 *  mezz.jei.api.registration.IRecipeCategoryRegistration
 *  mezz.jei.api.registration.IRecipeRegistration
 *  net.minecraft.resources.ResourceLocation
 */
package net.blay09.mods.waystones.compat.jei;

import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.blay09.mods.waystones.compat.jei.AttunedShardJeiRecipe;
import net.blay09.mods.waystones.compat.jei.WarpPlateJeiRecipeCategory;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEIAddon
implements IModPlugin {
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("waystones", "jei");
    }

    public void registerRecipes(IRecipeRegistration registry) {
        registry.addRecipes(WarpPlateJeiRecipeCategory.TYPE, List.of(new AttunedShardJeiRecipe()));
    }

    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new IRecipeCategory[]{new WarpPlateJeiRecipeCategory(registry.getJeiHelpers().getGuiHelper())});
    }
}

