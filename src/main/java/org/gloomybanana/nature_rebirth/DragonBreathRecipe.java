package org.gloomybanana.nature_rebirth;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

/**
 * 龙首 + 玻璃瓶 = 龙息，龙首不消耗
 */
public class DragonBreathRecipe implements CraftingRecipe {

    private final CraftingBookCategory category;

    public DragonBreathRecipe(CraftingBookCategory category) {
        this.category = category;
    }

    @Override
    public CraftingBookCategory category() {
        return this.category;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        boolean hasDragonHead = false;
        boolean hasGlassBottle = false;
        
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            
            if (stack.getItem() == Items.DRAGON_HEAD) {
                hasDragonHead = true;
            } else if (stack.getItem() == Items.GLASS_BOTTLE) {
                hasGlassBottle = true;
            } else {
                return false;
            }
        }
        
        return hasDragonHead && hasGlassBottle;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return new ItemStack(Items.DRAGON_BREATH);
    }

    @Override
    public RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return ModRecipes.DRAGON_BREATH_SERIALIZER.get();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.getItem() == Items.DRAGON_HEAD) {
                remaining.set(i, stack.copy());
            }
        }
        
        return remaining;
    }
}