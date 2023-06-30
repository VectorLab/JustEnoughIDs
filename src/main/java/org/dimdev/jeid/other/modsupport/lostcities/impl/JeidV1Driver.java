package org.dimdev.jeid.other.modsupport.lostcities.impl;

import java.util.Arrays;
import java.util.Objects;

import org.dimdev.jeid.other.modsupport.lostcities.ILostCitiesChunkPrimer;

import mcjty.lostcities.dimensions.world.driver.IIndex;
import mcjty.lostcities.dimensions.world.driver.IPrimerDriver;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.ChunkPrimer;

public class JeidV1Driver implements IPrimerDriver {
	
	private ChunkPrimer primero;
    private ILostCitiesChunkPrimer primerm;
    private int current;

    @Override
    public void setPrimer(ChunkPrimer primer) {
        this.primero = primer;
        this.primerm = (ILostCitiesChunkPrimer)(Object)primer;
        
    }

    @Override
    public ChunkPrimer getPrimer() {
        return primero;
    }

    @Override
    public IPrimerDriver current(int x, int y, int z) {
        current = getBlockIndex(x, y, z);
        return this;
    }

    @Override
    public IPrimerDriver current(IIndex index) {
        current = ((Index) index).index;
        return this;
    }

    @Override
    public IIndex getCurrent() {
        return new Index(current);
    }

    @Override
    public void incY() {
        current++;
    }

    @Override
    public void incY(int amount) {
        current += amount;
    }

    @Override
    public void decY() {
        current--;
    }

    @Override
    public void incX() {
        current += 1<<12;
    }

    @Override
    public void incZ() {
        current += 1<<8;
    }

    @Override
    public int getX() {
        return (current >> 12) & 0xf;
    }

    @Override
    public int getY() {
        return current & 0xff;
    }

    @Override
    public int getZ() {
        return (current >> 8) & 0xf;
    }

    @Override
    public void setBlockRange(int x, int y, int z, int y2, char c) {
        int s = getBlockIndex(x, y, z);
        int e = s + y2-y;
        Arrays.fill(primerm.getIntData(), s, e,(int) c);
    }

    @Override
    public void setBlockRangeSafe(int x, int y, int z, int y2, char c) {
        if (y2 <= y) {
            return;
        }
        int s = getBlockIndex(x, y, z);
        int e = s + y2-y;
        Arrays.fill(primerm.getIntData(), s, e,(int) c);
    }

    @Override
    public IPrimerDriver block(char c) {
        primerm.getIntData()[current] = (int) c;
        return this;
    }

    @SuppressWarnings("deprecation")
	@Override
    public IPrimerDriver block(IBlockState c) {
    	primerm.getIntData()[current] = Block.BLOCK_STATE_IDS.get(c);
        return this;
    }

    @Override
    public IPrimerDriver add(char c) {
    	primerm.getIntData()[current++] = (int) c;
        return this;
    }

    @Override
    public char getBlock() {
        return (char) primerm.getIntData()[current];
    }

    @Override
    public char getBlockDown() {
        return (char) primerm.getIntData()[current-1];
    }

    @Override
    public char getBlockEast() {
        return (char) primerm.getIntData()[current  + (1<<12)];
    }

    @Override
    public char getBlockWest() {
        return (char) primerm.getIntData()[current  - (1<<12)];
    }

    @Override
    public char getBlockSouth() {
        return (char) primerm.getIntData()[current  + (1<<8)];
    }

    @Override
    public char getBlockNorth() {
        return (char) primerm.getIntData()[current  - (1<<8)];
    }

    @Override
    public char getBlock(int x, int y, int z) {
        return (char) primerm.getIntData()[getBlockIndex(x, y, z)];
    }

    @Override
    public IIndex getIndex(int x, int y, int z) {
        return new Index(getBlockIndex(x, y, z));
    }

    private static int getBlockIndex(int x, int y, int z) {
        return x << 12 | z << 8 | y;
    }

    private class Index implements IIndex {
        private final int index;

        Index(int index) {
            this.index = index;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Index index1 = (Index) o;
            return index == index1.index;
        }

        @Override
        public int hashCode() {
            return Objects.hash(index);
        }
    }

    @Override
    public IPrimerDriver copy() {
    	JeidV1Driver driver = new JeidV1Driver();
        driver.current = current;
        driver.primero = primero;
        driver.primerm = primerm;
        return driver;
    }
}