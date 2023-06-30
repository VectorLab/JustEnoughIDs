package org.dimdev.jeid.other.modsupport.lostcities;

import java.util.HashMap;
import java.util.Map;

import org.dimdev.jeid.Utils;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public class VanillaIdTranslator {
	// vanilla
	public static final Map<Character,Integer> id_to_state=new HashMap<>();
	public static final Map<Integer,Character> state_to_id=new HashMap<>();
	
	public static void postInit() {
		Utils.LOGGER.info("Loading lostcities translator map ...");
		id_to_state.clear();
		state_to_id.clear();

		for(char i=0;i<=255;++i) {
			Block block=Block.REGISTRY.getObjectById((int)i);
			if(null==block) {
				continue;
			}
			ImmutableList<IBlockState> states=block.getBlockState().getValidStates();
			for(IBlockState state:states) {
				if(null==state) {
					continue;
				}
				state_to_id.put(Block.getStateId(state),i);
			}
			int defstate=Block.getStateId(block.getDefaultState());
			id_to_state.put(i,defstate);
			state_to_id.put(defstate,i);
		}
		Utils.LOGGER.info("Loaded lostcities translator map: {} ~ {}",id_to_state.size(),state_to_id.size());
	}

}
