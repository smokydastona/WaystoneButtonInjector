/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  net.minecraft.core.NonNullList
 *  net.minecraft.core.RegistryAccess
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.GsonHelper
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.player.StackedContents
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.crafting.Ingredient
 *  net.minecraft.world.item.crafting.Recipe
 *  net.minecraft.world.item.crafting.RecipeSerializer
 *  net.minecraft.world.item.crafting.RecipeType
 *  net.minecraft.world.item.crafting.ShapedRecipe
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 */
package net.blay09.mods.waystones.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.blay09.mods.waystones.block.ModBlocks;
import net.blay09.mods.waystones.recipe.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class WarpPlateRecipe
implements Recipe<Container> {
    private final ResourceLocation id;
    private final ItemStack resultItem;
    private final Ingredient primaryIngredient;
    private final NonNullList<Ingredient> secondaryIngredients;
    private final NonNullList<Ingredient> combinedIngredients;

    public WarpPlateRecipe(ResourceLocation id, ItemStack resultItem, Ingredient primaryIngredient, NonNullList<Ingredient> secondaryIngredients) {
        this.id = id;
        this.resultItem = resultItem;
        this.primaryIngredient = primaryIngredient;
        this.secondaryIngredients = secondaryIngredients;
        this.combinedIngredients = NonNullList.m_182647_((int)5);
        this.combinedIngredients.add((Object)primaryIngredient);
        this.combinedIngredients.addAll(secondaryIngredients);
    }

    public boolean m_5818_(Container inventory, Level level) {
        if (!this.primaryIngredient.test(inventory.m_8020_(0))) {
            return false;
        }
        StackedContents stackedContents = new StackedContents();
        int foundInputs = 0;
        for (int i = 0; i < this.combinedIngredients.size(); ++i) {
            ItemStack itemStack = inventory.m_8020_(i);
            if (itemStack.m_41619_()) continue;
            ++foundInputs;
            stackedContents.m_36468_(itemStack, 1);
        }
        return foundInputs == this.combinedIngredients.size() && stackedContents.m_36475_((Recipe)this, null);
    }

    public ItemStack m_5874_(Container inventory, RegistryAccess registryAccess) {
        return this.resultItem.m_41777_();
    }

    public ItemStack m_8043_(RegistryAccess registryAccess) {
        return this.resultItem;
    }

    public boolean m_8004_(int width, int height) {
        return true;
    }

    public NonNullList<Ingredient> m_7527_() {
        return this.combinedIngredients;
    }

    public boolean m_5598_() {
        return true;
    }

    public String m_6076_() {
        return "warp_plate";
    }

    public ItemStack m_8042_() {
        return new ItemStack((ItemLike)ModBlocks.warpPlate);
    }

    public RecipeSerializer<?> m_7707_() {
        return ModRecipes.warpPlateRecipeSerializer;
    }

    public RecipeType<?> m_6671_() {
        return ModRecipes.warpPlateRecipeType;
    }

    public ResourceLocation m_6423_() {
        return this.id;
    }

    static class Serializer
    implements RecipeSerializer<WarpPlateRecipe> {
        Serializer() {
        }

        public WarpPlateRecipe fromJson(ResourceLocation id, JsonObject jsonObject) {
            Ingredient primaryIngredient = Ingredient.m_43917_((JsonElement)GsonHelper.m_13930_((JsonObject)jsonObject, (String)"primary"));
            NonNullList<Ingredient> secondaryIngredients = Serializer.itemsFromJson(GsonHelper.m_13933_((JsonObject)jsonObject, (String)"secondary"));
            if (secondaryIngredients.isEmpty()) {
                throw new JsonParseException("No secondary ingredients for warp plate recipe");
            }
            if (secondaryIngredients.size() > 4) {
                throw new JsonParseException("Too many secondary ingredients for shapeless recipe");
            }
            ItemStack resultItem = ShapedRecipe.m_151274_((JsonObject)GsonHelper.m_13930_((JsonObject)jsonObject, (String)"result"));
            return new WarpPlateRecipe(id, resultItem, primaryIngredient, secondaryIngredients);
        }

        private static NonNullList<Ingredient> itemsFromJson(JsonArray jsonArray) {
            NonNullList ingredients = NonNullList.m_122779_();
            for (int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.m_288218_((JsonElement)jsonArray.get(i), (boolean)false);
                if (ingredient.m_43947_()) continue;
                ingredients.add((Object)ingredient);
            }
            return ingredients;
        }

        public WarpPlateRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            ItemStack resultItem = buf.m_130267_();
            Ingredient primaryIngredient = Ingredient.m_43940_((FriendlyByteBuf)buf);
            int secondaryCount = buf.m_130242_();
            NonNullList secondaryIngredients = NonNullList.m_182647_((int)secondaryCount);
            for (int i = 0; i < secondaryCount; ++i) {
                secondaryIngredients.add((Object)Ingredient.m_43940_((FriendlyByteBuf)buf));
            }
            return new WarpPlateRecipe(id, resultItem, primaryIngredient, (NonNullList<Ingredient>)secondaryIngredients);
        }

        public void toNetwork(FriendlyByteBuf buf, WarpPlateRecipe recipe) {
            buf.m_130055_(recipe.resultItem);
            recipe.primaryIngredient.m_43923_(buf);
            buf.m_130130_(recipe.secondaryIngredients.size());
            for (Ingredient ingredient : recipe.secondaryIngredients) {
                ingredient.m_43923_(buf);
            }
        }
    }
}

