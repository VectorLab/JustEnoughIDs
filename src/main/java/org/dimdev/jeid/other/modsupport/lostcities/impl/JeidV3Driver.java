package org.dimdev.jeid.other.modsupport.lostcities.impl;

import java.util.Arrays;
import java.util.Objects;

import org.dimdev.jeid.other.modsupport.lostcities.ILostCitiesChunkPrimer;

import mcjty.lostcities.dimensions.world.driver.IIndex;
import mcjty.lostcities.dimensions.world.driver.IPrimerDriver;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.ChunkPrimer;

public class JeidV3Driver implements IPrimerDriver {
	
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
    	int cpzc=packIdToState((int)c);
        int s = getBlockIndex(x, y, z);
        int e = s + y2-y;
        Arrays.fill(primerm.getIntData(), s, e,cpzc);
    }

    @Override
    public void setBlockRangeSafe(int x, int y, int z, int y2, char c) {
        if (y2 <= y) {
            return;
        }
        int cpzc=packIdToState((int)c);
        int s = getBlockIndex(x, y, z);
        int e = s + y2-y;
        Arrays.fill(primerm.getIntData(), s, e,cpzc);
    }

    @Override
    public IPrimerDriver block(char c) {
        primerm.getIntData()[current] = packIdToState((int)c);
        return this;
    }

    @Override
    public IPrimerDriver block(IBlockState c) {
    	primerm.getIntData()[current] = Block.getStateId(c);
        return this;
    }

    @Override
    public IPrimerDriver add(char c) {
    	primerm.getIntData()[current++] = packIdToState((int)c);
        return this;
    }

    @Override
    public char getBlock() {
        return unpackStateToId(primerm.getIntData()[current]);
    }

    @Override    public char getBlockDown() {
        return unpackStateToId(primerm.getIntData()[current-1]);
    }

    @Override
    public char getBlockEast() {
        return unpackStateToId(primerm.getIntData()[current  + (1<<12)]);
    }

    @Override
    public char getBlockWest() {
        return unpackStateToId(primerm.getIntData()[current  - (1<<12)]);
    }

    @Override
    public char getBlockSouth() {
        return unpackStateToId(primerm.getIntData()[current  + (1<<8)]);
    }

    @Override
    public char getBlockNorth() {
        return unpackStateToId(primerm.getIntData()[current  - (1<<8)]);
    }

    @Override
    public char getBlock(int x, int y, int z) {
        return unpackStateToId(primerm.getIntData()[getBlockIndex(x, y, z)]);
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
    	JeidV3Driver driver = new JeidV3Driver();
        driver.current = current;
        driver.primero = primero;
        driver.primerm = primerm;
        return driver;
    }
/*
    private static int packIdMetaToState(int id,int meta) {
        if ((id & 0xfffff000) == 0) {
            // Use vanilla 4 bit meta + 12 bit ID
            return id + (meta << 12);
        } else {
            // Use JEID 28 bit ID + 4 bit meta
            return (id << 4) + meta;
        }
    }

    private static int unpackStateToIdMeta(int stateID) {
        if ((stateID & 0xffff0000) == 0) {
            // Use vanilla 4 bit meta + 12 bit ID
            int id = stateID & 4095;
            int meta = stateID >> 12 & 15;
            return (id << 4) | meta;
        } else {
            // Use JEID 28 bit ID + 4 bit meta
        	return stateID;
            int meta = stateID & 0xF;
            int id = stateID >> 4;
            return (id << 4) | meta;
        }
    }

	private static char unpackStateToId(int stateID) {
	    if ((stateID & 0xffff0000) == 0) {
	        // Use vanilla 4 bit meta + 12 bit ID
	        return (char) Math.min(255,stateID&4095);
	    } else {
	        // Use JEID 28 bit ID + 4 bit meta
	    	return (char) Math.min(255,stateID>>4);
	    }
	}
*/
    private static int packIdToState(int id) {
    	IBlockState state = Block.BLOCK_STATE_IDS.getByValue(id);
    	return Block.getStateId(state);
    }
    
	private static char unpackStateToId(int stateID) {
		IBlockState state = Block.BLOCK_STATE_IDS.getByValue(stateID);
		if(null==state) {
			return 0;
		}
		return (char) Block.BLOCK_STATE_IDS.get(state);
	}
	//private static final IBlockState DEFAULT_STATE = Blocks.AIR.getDefaultState();
	
}
