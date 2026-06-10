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
        // 方解石获取方式
        if (Config.CALCITE_GENERATION.get()) {
            List<Component> description = new ArrayList<>();
            description.add(Component.literal("§6获取方式："));
            description.add(Component.literal("§71. 将岩浆倒在骨块上方"));
            description.add(Component.literal("§72. 在岩浆周围放置蓝冰"));
            description.add(Component.literal("§73. 岩浆会转化为方解石"));
            
            registration.addIngredientInfo(
                    new ItemStack(Blocks.CALCITE),
                    VanillaTypes.ITEM_STACK,
                    description.toArray(new Component[0])
            );
        }
        
        // 凝灰岩获取方式
        if (Config.TUFF_GENERATION.get()) {
            List<Component> description = new ArrayList<>();
            description.add(Component.literal("§6获取方式："));
            description.add(Component.literal("§71. 将岩浆倒在安山岩上方"));
            description.add(Component.literal("§72. 在岩浆周围放置蓝冰"));
            description.add(Component.literal("§73. 岩浆会转化为凝灰岩"));
            
            registration.addIngredientInfo(
                    new ItemStack(Blocks.TUFF),
                    VanillaTypes.ITEM_STACK,
                    description.toArray(new Component[0])
            );
        }
        
        // 滴水石获取方式
        if (Config.DRIPSTONE_GENERATION.get()) {
            List<Component> description = new ArrayList<>();
            description.add(Component.literal("§6获取方式："));
            description.add(Component.literal("§71. 将岩浆倒在花岗岩上方"));
            description.add(Component.literal("§72. 在岩浆周围放置蓝冰"));
            description.add(Component.literal("§73. 岩浆会转化为滴水石"));
            
            registration.addIngredientInfo(
                    new ItemStack(Blocks.DRIPSTONE_BLOCK),
                    VanillaTypes.ITEM_STACK,
                    description.toArray(new Component[0])
            );
        }
        
        // 下界岩获取方式
        if (Config.NETHERRACK_GENERATION.get()) {
            List<Component> description = new ArrayList<>();
            description.add(Component.literal("§6获取方式："));
            description.add(Component.literal("§71. 让岩浆流动"));
            description.add(Component.literal("§72. 在岩浆周围同时放置蓝冰和岩浆块"));
            description.add(Component.literal("§73. 岩浆会转化为下界岩"));
            description.add(Component.literal("§e信标增强："));
            description.add(Component.literal("§7在生成位置下方放置信标，"));
            description.add(Component.literal("§7有概率生成下界矿石（石英、金、远古残骸）"));
            
            registration.addIngredientInfo(
                    new ItemStack(Blocks.NETHERRACK),
                    VanillaTypes.ITEM_STACK,
                    description.toArray(new Component[0])
            );
        }
        
        // 深板岩圆石获取方式
        List<Component> deepslateDesc = new ArrayList<>();
        deepslateDesc.add(Component.literal("§6获取方式："));
        deepslateDesc.add(Component.literal("§71. 在Y=" + Config.DEEPSLATE_Y_THRESHOLD.get() + "以下让岩浆与水相遇"));
        deepslateDesc.add(Component.literal("§72. 会生成深板岩圆石而非普通圆石"));
        deepslateDesc.add(Component.literal("§e信标增强："));
        deepslateDesc.add(Component.literal("§7在生成位置下方放置信标，"));
        deepslateDesc.add(Component.literal("§7有概率生成深板岩矿石"));
        
        registration.addIngredientInfo(
                new ItemStack(Blocks.COBBLED_DEEPSLATE),
                VanillaTypes.ITEM_STACK,
                deepslateDesc.toArray(new Component[0])
        );
        
        // 深板岩获取方式
        List<Component> deepslateStoneDesc = new ArrayList<>();
        deepslateStoneDesc.add(Component.literal("§6获取方式："));
        deepslateStoneDesc.add(Component.literal("§71. 在Y=" + Config.DEEPSLATE_Y_THRESHOLD.get() + "以下让岩浆源与水相遇"));
        deepslateStoneDesc.add(Component.literal("§72. 会生成深板岩而非普通石头"));
        deepslateStoneDesc.add(Component.literal("§e信标增强："));
        deepslateStoneDesc.add(Component.literal("§7在生成位置下方放置信标，"));
        deepslateStoneDesc.add(Component.literal("§7有概率生成深板岩矿石"));
        
        registration.addIngredientInfo(
                new ItemStack(Blocks.DEEPSLATE),
                VanillaTypes.ITEM_STACK,
                deepslateStoneDesc.toArray(new Component[0])
        );
        
        // 矿石获取方式（信标增强）
        addOreInfo(registration, Blocks.COAL_ORE, "煤矿石", "石制");
        addOreInfo(registration, Blocks.IRON_ORE, "铁矿石", "石制");
        addOreInfo(registration, Blocks.REDSTONE_ORE, "红石矿石", "石制");
        addOreInfo(registration, Blocks.LAPIS_ORE, "青金石矿石", "石制");
        addOreInfo(registration, Blocks.GOLD_ORE, "金矿石", "石制");
        addOreInfo(registration, Blocks.EMERALD_ORE, "绿宝石矿石", "石制");
        addOreInfo(registration, Blocks.DIAMOND_ORE, "钻石矿石", "石制");
        
        addOreInfo(registration, Blocks.DEEPSLATE_COAL_ORE, "深板岩煤矿石", "深板岩");
        addOreInfo(registration, Blocks.DEEPSLATE_IRON_ORE, "深板岩铁矿石", "深板岩");
        addOreInfo(registration, Blocks.DEEPSLATE_REDSTONE_ORE, "深板岩红石矿石", "深板岩");
        addOreInfo(registration, Blocks.DEEPSLATE_LAPIS_ORE, "深板岩青金石矿石", "深板岩");
        addOreInfo(registration, Blocks.DEEPSLATE_GOLD_ORE, "深板岩金矿石", "深板岩");
        addOreInfo(registration, Blocks.DEEPSLATE_EMERALD_ORE, "深板岩绿宝石矿石", "深板岩");
        addOreInfo(registration, Blocks.DEEPSLATE_DIAMOND_ORE, "深板岩钻石矿石", "深板岩");
        
        addOreInfo(registration, Blocks.NETHER_QUARTZ_ORE, "下界石英矿石", "下界");
        addOreInfo(registration, Blocks.NETHER_GOLD_ORE, "下界金矿石", "下界");
        addOreInfo(registration, Blocks.ANCIENT_DEBRIS, "远古残骸", "下界");
    }
    
    private void addOreInfo(IRecipeRegistration registration, Block ore, String name, String type) {
        List<Component> description = new ArrayList<>();
        description.add(Component.literal("§6信标增强获取："));
        
        if (type.equals("石制")) {
            description.add(Component.literal("§71. 在Y≥" + Config.DEEPSLATE_Y_THRESHOLD.get() + "让岩浆源与水相遇"));
            description.add(Component.literal("§72. 在生成位置下方放置信标"));
            description.add(Component.literal("§73. 有概率生成" + name));
        } else if (type.equals("深板岩")) {
            description.add(Component.literal("§71. 在Y<" + Config.DEEPSLATE_Y_THRESHOLD.get() + "让岩浆与水相遇"));
            description.add(Component.literal("§72. 在生成位置下方放置信标"));
            description.add(Component.literal("§73. 有概率生成" + name));
        } else if (type.equals("下界")) {
            description.add(Component.literal("§71. 让岩浆周围同时存在蓝冰和岩浆块"));
            description.add(Component.literal("§72. 在生成位置下方放置信标"));
            description.add(Component.literal("§73. 有概率生成" + name));
        }
        
        registration.addIngredientInfo(
                new ItemStack(ore),
                VanillaTypes.ITEM_STACK,
                description.toArray(new Component[0])
        );
    }
}
