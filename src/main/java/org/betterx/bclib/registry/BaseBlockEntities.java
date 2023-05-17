package org.betterx.bclib.registry;

import org.betterx.bclib.BCLib;
import org.betterx.bclib.blockentities.BaseBarrelBlockEntity;
import org.betterx.bclib.blockentities.BaseChestBlockEntity;
import org.betterx.bclib.blockentities.BaseFurnaceBlockEntity;
import org.betterx.bclib.blockentities.DynamicBlockEntityType;
import org.betterx.bclib.blockentities.DynamicBlockEntityType.BlockEntitySupplier;
import org.betterx.bclib.blocks.BaseBarrelBlock;
import org.betterx.bclib.blocks.BaseChestBlock;
import org.betterx.bclib.blocks.BaseFurnaceBlock;
import org.betterx.bclib.blocks.BaseSignBlock;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;

public class BaseBlockEntities {
    public static final DynamicBlockEntityType<BaseChestBlockEntity> CHEST = registerBlockEntityType(BCLib.makeID(
            "chest"), BaseChestBlockEntity::new);
    public static final DynamicBlockEntityType<BaseBarrelBlockEntity> BARREL = registerBlockEntityType(BCLib.makeID(
            "barrel"), BaseBarrelBlockEntity::new);
    public static final DynamicBlockEntityType<SignBlockEntity> SIGN = registerBlockEntityType(
            BCLib.makeID("sign"),
            SignBlockEntity::new
    );
    public static final DynamicBlockEntityType<BaseFurnaceBlockEntity> FURNACE = registerBlockEntityType(BCLib.makeID(
            "furnace"), BaseFurnaceBlockEntity::new);

    public static <T extends BlockEntity> DynamicBlockEntityType<T> registerBlockEntityType(
            ResourceLocation typeId,
            BlockEntitySupplier<? extends T> supplier
    ) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, typeId, new DynamicBlockEntityType<>(supplier));
    }

    public static void register() {
    }

    public static Block[] getChests() {
        return BuiltInRegistries.BLOCK
                .stream()
                .filter(block -> block instanceof BaseChestBlock)
                .toArray(Block[]::new);
    }

    public static Block[] getBarrels() {
        return BuiltInRegistries.BLOCK
                .stream()
                .filter(block -> block instanceof BaseBarrelBlock)
                .toArray(Block[]::new);
    }

    public static Block[] getSigns() {
        return BuiltInRegistries.BLOCK
                .stream()
                .filter(block -> block instanceof BaseSignBlock)
                .toArray(Block[]::new);
    }

    public static Block[] getFurnaces() {
        return BuiltInRegistries.BLOCK
                .stream()
                .filter(block -> block instanceof BaseFurnaceBlock)
                .toArray(Block[]::new);
    }
}
