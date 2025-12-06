/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  mezz.jei.api.constants.VanillaTypes
 *  mezz.jei.api.gui.builder.IRecipeLayoutBuilder
 *  mezz.jei.api.gui.drawable.IDrawable
 *  mezz.jei.api.helpers.IGuiHelper
 *  mezz.jei.api.ingredients.IIngredientType
 *  mezz.jei.api.recipe.IFocusGroup
 *  mezz.jei.api.recipe.RecipeIngredientRole
 *  mezz.jei.api.recipe.RecipeType
 *  mezz.jei.api.recipe.category.IRecipeCategory
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 */
package net.blay09.mods.waystones.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.blay09.mods.waystones.block.ModBlocks;
import net.blay09.mods.waystones.compat.jei.AttunedShardJeiRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class WarpPlateJeiRecipeCategory
implements IRecipeCategory<AttunedShardJeiRecipe> {
    public static final RecipeType<AttunedShardJeiRecipe> TYPE = RecipeType.create((String)"waystones", (String)"warp_plate", AttunedShardJeiRecipe.class);
    public static final ResourceLocation UID = new ResourceLocation("waystones", "warp_plate");
    private static final ResourceLocation texture = new ResourceLocation("waystones", "textures/gui/jei/warp_plate.png");
    private final IDrawable background;
    private final IDrawable icon;

    public WarpPlateJeiRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(texture, 0, 0, 128, 74);
        this.icon = guiHelper.createDrawableIngredient((IIngredientType)VanillaTypes.ITEM_STACK, (Object)new ItemStack((ItemLike)ModBlocks.warpPlate));
    }

    public RecipeType<AttunedShardJeiRecipe> getRecipeType() {
        return TYPE;
    }

    public Component getTitle() {
        return Component.m_237115_((String)UID.toString());
    }

    public IDrawable getBackground() {
        return this.background;
    }

    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, AttunedShardJeiRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 29, 29).addIngredient((IIngredientType)VanillaTypes.ITEM_STACK, (Object)recipe.getInputs().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 29, 1).addIngredient((IIngredientType)VanillaTypes.ITEM_STACK, (Object)recipe.getInputs().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 57, 29).addIngredient((IIngredientType)VanillaTypes.ITEM_STACK, (Object)recipe.getInputs().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 29, 57).addIngredient((IIngredientType)VanillaTypes.ITEM_STACK, (Object)recipe.getInputs().get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 29).addIngredient((IIngredientType)VanillaTypes.ITEM_STACK, (Object)recipe.getInputs().get(4));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 111, 29).addIngredient((IIngredientType)VanillaTypes.ITEM_STACK, (Object)recipe.getOutput());
    }
}

