package ru.bclib.interfaces;

import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.generator.BiomePicker;

public interface BiomeMap {
	void setChunkProcessor(TriConsumer<Integer, Integer, Integer> processor);
	BiomeChunk getChunk(int cx, int cz, boolean update);
	BiomePicker.Entry getBiome(double x, double y, double z);
	void clearCache();
}
