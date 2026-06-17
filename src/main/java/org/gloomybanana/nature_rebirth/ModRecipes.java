package org.gloomybanana.nature_rebirth;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = 
        DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, NatureRebirth.MOD_ID);
    
    // 龙首 + 玻璃瓶 = 龙息，龙首不消耗
    public static final net.neoforged.neoforge.registries.DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DragonBreathRecipe>> DRAGON_BREATH_SERIALIZER =
        RECIPE_SERIALIZERS.register("dragon_breath_recipe", () -> {
            MapCodec<DragonBreathRecipe> codec = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                    CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(DragonBreathRecipe::category)
                ).apply(instance, DragonBreathRecipe::new)
            );
            
            StreamCodec<RegistryFriendlyByteBuf, DragonBreathRecipe> streamCodec = StreamCodec.of(
                (buf, recipe) -> buf.writeEnum(recipe.category()),
                buf -> new DragonBreathRecipe(buf.readEnum(CraftingBookCategory.class))
            );
            
            return new RecipeSerializer<>(codec, streamCodec);
        });
    
    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}