package org.gloomybanana.nature_rebirth.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.gloomybanana.nature_rebirth.Config;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class NatureRebirthJeiPlugin implements IModPlugin {
    
    @Override
    public Identifier getPluginUid() {
        return Identifier.parse("nature_rebirth:jei_plugin");
    }
    
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // Calcite obtain method
        if (Config.CALCITE_GENERATION.get()) {
            List<Component> description = new ArrayList<>();
            description.add(Component.translatable("nature_rebirth.jei.obtain"));
            description.add(Component.translatable("nature_rebirth.jei.calcite.step1"));
            description.add(Component.translatable("nature_rebirth.jei.calcite.step2"));
            description.add(Component.translatable("nature_rebirth.jei.calcite.step3"));
            
            registration.addIngredientInfo(
                    new ItemStack(Blocks.CALCITE),
                    VanillaTypes.ITEM_STACK,
                    description.toArray(new Component[0])
            );
        }
        
        // Tuff obtain method
        if (Config.TUFF_GENERATION.get()) {
            List<Component> description = new ArrayList<>();
            description.add(Component.translatable("nature_rebirth.jei.obtain"));
            description.add(Component.translatable("nature_rebirth.jei.tuff.step1"));
            description.add(Component.translatable("nature_rebirth.jei.tuff.step2"));
            description.add(Component.translatable("nature_rebirth.jei.tuff.step3"));
            
            registration.addIngredientInfo(
                    new ItemStack(Blocks.TUFF),
                    VanillaTypes.ITEM_STACK,
                    description.toArray(new Component[0])
            );
        }
        
        // Dripstone obtain method
        if (Config.DRIPSTONE_GENERATION.get()) {
            List<Component> description = new ArrayList<>();
            description.add(Component.translatable("nature_rebirth.jei.obtain"));
            description.add(Component.translatable("nature_rebirth.jei.dripstone.step1"));
            description.add(Component.translatable("nature_rebirth.jei.dripstone.step2"));
            description.add(Component.translatable("nature_rebirth.jei.dripstone.step3"));
            
            registration.addIngredientInfo(
                    new ItemStack(Blocks.DRIPSTONE_BLOCK),
                    VanillaTypes.ITEM_STACK,
                    description.toArray(new Component[0])
            );
        }
        
        // Netherrack obtain method
        if (Config.NETHERRACK_GENERATION.get()) {
            List<Component> description = new ArrayList<>();
            description.add(Component.translatable("nature_rebirth.jei.obtain"));
            description.add(Component.translatable("nature_rebirth.jei.netherrack.step1"));
            description.add(Component.translatable("nature_rebirth.jei.netherrack.step2"));
            description.add(Component.translatable("nature_rebirth.jei.netherrack.step3"));
            description.add(Component.translatable("nature_rebirth.jei.beacon.enhanced"));
            description.add(Component.translatable("nature_rebirth.jei.beacon.enhanced.desc"));
            description.add(Component.translatable("nature_rebirth.jei.nether.ore.desc"));
            
            registration.addIngredientInfo(
                    new ItemStack(Blocks.NETHERRACK),
                    VanillaTypes.ITEM_STACK,
                    description.toArray(new Component[0])
            );
        }
        
        // Cobbled Deepslate obtain method
        List<Component> deepslateDesc = new ArrayList<>();
        deepslateDesc.add(Component.translatable("nature_rebirth.jei.obtain"));
        deepslateDesc.add(Component.translatable("nature_rebirth.jei.deepslate.cobblestone.step1", Config.DEEPSLATE_Y_THRESHOLD.get()));
        deepslateDesc.add(Component.translatable("nature_rebirth.jei.deepslate.cobblestone.step2"));
        deepslateDesc.add(Component.translatable("nature_rebirth.jei.beacon.enhanced"));
        deepslateDesc.add(Component.translatable("nature_rebirth.jei.beacon.enhanced.desc"));
        deepslateDesc.add(Component.translatable("nature_rebirth.jei.deepslate.ore.desc"));
        
        registration.addIngredientInfo(
                new ItemStack(Blocks.COBBLED_DEEPSLATE),
                VanillaTypes.ITEM_STACK,
                deepslateDesc.toArray(new Component[0])
        );
        
        // Deepslate obtain method
        List<Component> deepslateStoneDesc = new ArrayList<>();
        deepslateStoneDesc.add(Component.translatable("nature_rebirth.jei.obtain"));
        deepslateStoneDesc.add(Component.translatable("nature_rebirth.jei.deepslate.stone.step1", Config.DEEPSLATE_Y_THRESHOLD.get()));
        deepslateStoneDesc.add(Component.translatable("nature_rebirth.jei.deepslate.stone.step2"));
        deepslateStoneDesc.add(Component.translatable("nature_rebirth.jei.beacon.enhanced"));
        deepslateStoneDesc.add(Component.translatable("nature_rebirth.jei.beacon.enhanced.desc"));
        deepslateStoneDesc.add(Component.translatable("nature_rebirth.jei.deepslate.ore.desc"));
        
        registration.addIngredientInfo(
                new ItemStack(Blocks.DEEPSLATE),
                VanillaTypes.ITEM_STACK,
                deepslateStoneDesc.toArray(new Component[0])
        );
        
        // Stone ore obtain methods (beacon enhanced)
        addOreInfo(registration, Blocks.COAL_ORE, "nature_rebirth.jei.ore.coal", "stone");
        addOreInfo(registration, Blocks.IRON_ORE, "nature_rebirth.jei.ore.iron", "stone");
        addOreInfo(registration, Blocks.REDSTONE_ORE, "nature_rebirth.jei.ore.redstone", "stone");
        addOreInfo(registration, Blocks.LAPIS_ORE, "nature_rebirth.jei.ore.lapis", "stone");
        addOreInfo(registration, Blocks.GOLD_ORE, "nature_rebirth.jei.ore.gold", "stone");
        addOreInfo(registration, Blocks.EMERALD_ORE, "nature_rebirth.jei.ore.emerald", "stone");
        addOreInfo(registration, Blocks.DIAMOND_ORE, "nature_rebirth.jei.ore.diamond", "stone");
        
        // Deepslate ore obtain methods (beacon enhanced)
        addOreInfo(registration, Blocks.DEEPSLATE_COAL_ORE, "nature_rebirth.jei.ore.deepslate_coal", "deepslate");
        addOreInfo(registration, Blocks.DEEPSLATE_IRON_ORE, "nature_rebirth.jei.ore.deepslate_iron", "deepslate");
        addOreInfo(registration, Blocks.DEEPSLATE_REDSTONE_ORE, "nature_rebirth.jei.ore.deepslate_redstone", "deepslate");
        addOreInfo(registration, Blocks.DEEPSLATE_LAPIS_ORE, "nature_rebirth.jei.ore.deepslate_lapis", "deepslate");
        addOreInfo(registration, Blocks.DEEPSLATE_GOLD_ORE, "nature_rebirth.jei.ore.deepslate_gold", "deepslate");
        addOreInfo(registration, Blocks.DEEPSLATE_EMERALD_ORE, "nature_rebirth.jei.ore.deepslate_emerald", "deepslate");
        addOreInfo(registration, Blocks.DEEPSLATE_DIAMOND_ORE, "nature_rebirth.jei.ore.deepslate_diamond", "deepslate");
        
        // Nether ore obtain methods (beacon enhanced)
        addOreInfo(registration, Blocks.NETHER_QUARTZ_ORE, "nature_rebirth.jei.ore.nether_quartz", "nether");
        addOreInfo(registration, Blocks.NETHER_GOLD_ORE, "nature_rebirth.jei.ore.nether_gold", "nether");
        addOreInfo(registration, Blocks.ANCIENT_DEBRIS, "nature_rebirth.jei.ore.ancient_debris", "nether");
    }
    
    private void addOreInfo(IRecipeRegistration registration, Block ore, String oreNameKey, String type) {
        List<Component> description = new ArrayList<>();
        description.add(Component.translatable("nature_rebirth.jei.beacon.obtain"));
        
        if (type.equals("stone")) {
            description.add(Component.translatable("nature_rebirth.jei.stone.ore.step1", Config.DEEPSLATE_Y_THRESHOLD.get()));
            description.add(Component.translatable("nature_rebirth.jei.stone.ore.step2"));
            description.add(Component.translatable("nature_rebirth.jei.stone.ore.desc", Component.translatable(oreNameKey)));
        } else if (type.equals("deepslate")) {
            description.add(Component.translatable("nature_rebirth.jei.deepslate.ore.step1", Config.DEEPSLATE_Y_THRESHOLD.get()));
            description.add(Component.translatable("nature_rebirth.jei.deepslate.ore.step2"));
            description.add(Component.translatable("nature_rebirth.jei.stone.ore.desc", Component.translatable(oreNameKey)));
        } else if (type.equals("nether")) {
            description.add(Component.translatable("nature_rebirth.jei.nether.ore.step1"));
            description.add(Component.translatable("nature_rebirth.jei.nether.ore.step2"));
            description.add(Component.translatable("nature_rebirth.jei.stone.ore.desc", Component.translatable(oreNameKey)));
        }
        
        registration.addIngredientInfo(
                new ItemStack(ore),
                VanillaTypes.ITEM_STACK,
                description.toArray(new Component[0])
        );
    }
}
