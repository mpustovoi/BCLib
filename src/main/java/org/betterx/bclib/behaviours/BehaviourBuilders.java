package org.betterx.bclib.behaviours;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class BehaviourBuilders {
    public static BlockBehaviour.Properties createPlant() {
        return createPlant(MapColor.PLANT);
    }

    public static BlockBehaviour.Properties createPlant(MapColor color) {
        return createPlant(color, false);
    }

    //TODO: Remove this method, and call noCollision explicitly
    public static BlockBehaviour.Properties createPlant(MapColor color, boolean collission) {
        var p = BlockBehaviour.Properties.of()
                                         .mapColor(color)
                                         .instabreak()
                                         .pushReaction(PushReaction.DESTROY);
        if (!collission)
            p.noCollission();

        return p;
    }

    public static BlockBehaviour.Properties createGrass(MapColor color, boolean flammable) {
        final BlockBehaviour.Properties p = createPlant(color);
        if (flammable)
            p.ignitedByLava();
        return p;
    }

    public static BlockBehaviour.Properties createTickingPlant() {
        return createTickingPlant(MapColor.PLANT);
    }

    public static BlockBehaviour.Properties createTickingPlant(MapColor color) {
        return createPlant(color).randomTicks();
    }

    public static BlockBehaviour.Properties createReplaceablePlant() {
        return createReplaceablePlant(MapColor.PLANT);
    }

    public static BlockBehaviour.Properties createReplaceablePlant(MapColor color) {
        return createPlant(color).replaceable();
    }

    public static BlockBehaviour.Properties createWaterPlant() {
        return createWaterPlant(MapColor.WATER, false);
    }

    //TODO: Remove this method, and call noCollision explicitly
    public static BlockBehaviour.Properties createWaterPlant(MapColor color, boolean collission) {
        var p = BlockBehaviour.Properties.of()
                                         .mapColor(color)
                                         .instabreak()
                                         .pushReaction(PushReaction.DESTROY);
        if (!collission)
            p.noCollission();

        return p;
    }

    public static BlockBehaviour.Properties createReplaceableWaterPlant() {
        return createWaterPlant().replaceable();
    }

    public static BlockBehaviour.Properties createLeaves() {
        return createLeaves(MapColor.PLANT, true);
    }

    public static BlockBehaviour.Properties createLeaves(MapColor color, boolean flammable) {
        final BlockBehaviour.Properties p = BlockBehaviour.Properties
                .of()
                .mapColor(color)
                .strength(0.2f)
                .randomTicks()
                .noOcclusion()
                .isValidSpawn(Blocks::ocelotOrParrot)
                .isSuffocating(Blocks::never)
                .isViewBlocking(Blocks::never)
                .pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor(Blocks::never)
                .sound(SoundType.GRASS);
        if (flammable) {
            p.ignitedByLava();
        }
        return p;
    }

    public static BlockBehaviour.Properties createCactus(MapColor color, boolean flammable) {
        final BlockBehaviour.Properties p = BlockBehaviour.Properties
                .of()
                .mapColor(color)
                .randomTicks()
                .strength(0.4F)
                .sound(SoundType.WOOL)
                .pushReaction(PushReaction.DESTROY)
                .noOcclusion();
        if (flammable) {
            p.ignitedByLava();
        }
        return p;
    }

    public static BlockBehaviour.Properties createMetal() {
        return createMetal(MapColor.METAL);
    }

    public static BlockBehaviour.Properties createMetal(MapColor color) {
        return BlockBehaviour.Properties.of()
                                        .mapColor(color)
                                        .instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                                        .strength(5.0F, 6.0F)
                                        .sound(SoundType.METAL);
    }

    public static BlockBehaviour.Properties createStone() {
        return createStone(MapColor.STONE);
    }

    public static BlockBehaviour.Properties createStone(MapColor color) {
        return BlockBehaviour.Properties.of()
                                        .mapColor(color)
                                        .strength(1.5F, 6.0F)
                                        .instrument(NoteBlockInstrument.BASEDRUM);
    }

    public static BlockBehaviour.Properties createWood() {
        return createWood(MapColor.WOOD, true);
    }

    public static BlockBehaviour.Properties createWood(MapColor color, boolean flammable) {
        final BlockBehaviour.Properties p = BlockBehaviour.Properties
                .of()
                .mapColor(color)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(SoundType.WOOD);


        if (flammable) {
            p.ignitedByLava();
        }
        return p;
    }

    public static BlockBehaviour.Properties createSign(MapColor color, boolean flammable) {
        final BlockBehaviour.Properties p = BlockBehaviour.Properties
                .of()
                .mapColor(color)
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASS)
                .noCollission()
                .strength(1.0f);
        if (flammable) {
            p.ignitedByLava();
        }
        return p;
    }

    public static BlockBehaviour.Properties createWallSign(MapColor color, Block dropBlock, boolean flammable) {
        return createSign(color, flammable).dropsLike(dropBlock);
    }

    public static BlockBehaviour.Properties createTrapDoor(MapColor color, boolean flammable) {
        final BlockBehaviour.Properties p = BlockBehaviour.Properties
                .of()
                .mapColor(color)
                .instrument(NoteBlockInstrument.BASS)
                .strength(3.0F)
                .noOcclusion()
                .isValidSpawn(Blocks::never);
        if (flammable) {
            p.ignitedByLava();
        }
        return p;
    }

    public static BlockBehaviour.Properties createGlass() {
        return BlockBehaviour.Properties
                .of()
                .instrument(NoteBlockInstrument.HAT)
                .strength(0.3F)
                .sound(SoundType.GLASS)
                .noOcclusion()
                .isValidSpawn(Blocks::never)
                .isRedstoneConductor(Blocks::never)
                .isSuffocating(Blocks::never)
                .isViewBlocking(Blocks::never);
    }

    public static BlockBehaviour.Properties applyBasePlantSettings() {
        return applyBasePlantSettings(false, 0);
    }

    public static BlockBehaviour.Properties applyBasePlantSettings(int light) {
        return applyBasePlantSettings(false, light);
    }

    public static BlockBehaviour.Properties applyBasePlantSettings(boolean replaceable) {
        return applyBasePlantSettings(replaceable, 0);
    }

    public static BlockBehaviour.Properties applyBasePlantSettings(boolean replaceable, int light) {
        return applyBasePlantSettings(replaceable
                ? createReplaceablePlant()
                : createPlant(), light);
    }

    public static BlockBehaviour.Properties applyBasePlantSettings(BlockBehaviour.Properties props, int light) {
        props
                .sound(SoundType.GRASS)
                .offsetType(BlockBehaviour.OffsetType.XZ);
        if (light > 0) props.lightLevel(s -> light);
        return props;
    }

    public static BlockBehaviour.Properties createSnow() {
        return BlockBehaviour.Properties
                .of()
                .mapColor(MapColor.SNOW)
                .requiresCorrectToolForDrops()
                .strength(0.2F)
                .sound(SoundType.SNOW);
    }
}