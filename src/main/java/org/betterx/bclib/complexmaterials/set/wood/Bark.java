package org.betterx.bclib.complexmaterials.set.wood;

import org.betterx.bclib.blocks.StripableBarkBlock;
import org.betterx.bclib.complexmaterials.ComplexMaterial;
import org.betterx.bclib.complexmaterials.WoodenComplexMaterial;
import org.betterx.bclib.complexmaterials.entry.BlockEntry;
import org.betterx.bclib.complexmaterials.entry.SimpleMaterialSlot;
import org.betterx.bclib.recipes.BCLRecipeBuilder;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Bark extends SimpleMaterialSlot<WoodenComplexMaterial> {
    public Bark() {
        super("bark");
    }

    @Override
    protected @NotNull Block createBlock(
            WoodenComplexMaterial parentMaterial, BlockBehaviour.Properties settings
    ) {
        return new StripableBarkBlock(
                parentMaterial.woodColor,
                parentMaterial.getBlock(WoodSlots.STRIPPED_BARK)
        );
    }


    @Override
    protected void modifyBlockEntry(WoodenComplexMaterial parentMaterial, @NotNull BlockEntry entry) {
        entry
                .setBlockTags(
                        BlockTags.LOGS,
                        BlockTags.LOGS_THAT_BURN,
                        parentMaterial.getBlockTag(WoodenComplexMaterial.TAG_LOGS)
                )
                .setItemTags(
                        ItemTags.LOGS,
                        ItemTags.LOGS_THAT_BURN,
                        parentMaterial.getItemTag(WoodenComplexMaterial.TAG_LOGS)
                );
    }

    @Override
    protected @Nullable void makeRecipe(ComplexMaterial material, ResourceLocation id) {
        BCLRecipeBuilder
                .crafting(id, material.getBlock(suffix))
                .setShape("##", "##")
                .addMaterial('#', material.getBlock(WoodSlots.LOG))
                .setOutputCount(3)
                .setCategory(RecipeCategory.BUILDING_BLOCKS)
                .build();
    }
}
