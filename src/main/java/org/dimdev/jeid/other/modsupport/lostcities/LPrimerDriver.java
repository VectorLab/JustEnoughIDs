package org.dimdev.jeid.other.modsupport.lostcities;

import org.dimdev.jeid.Utils;
import org.dimdev.jeid.other.modsupport.lostcities.impl.JeidV1Driver;
import org.dimdev.jeid.other.modsupport.lostcities.impl.JeidV2Driver;
import org.dimdev.jeid.other.modsupport.lostcities.impl.JeidV3Driver;
import org.dimdev.jeid.other.modsupport.lostcities.impl.JeidV4Driver;

import mcjty.lostcities.dimensions.world.driver.RPrimerDriver;

public class LPrimerDriver {
	
	public static void postInit() {
		try {
			VanillaIdTranslator.postInit();
		}catch(Throwable e) {
			Utils.LOGGER.fatal("lostcities Translator load error");
			Utils.LOGGER.catching(e);
		}
		
		RPrimerDriver.addDriver("JeidV1", JeidV1Driver.class);
		RPrimerDriver.addDriver("JeidV2", JeidV2Driver.class);
		RPrimerDriver.addDriver("JeidV3", JeidV3Driver.class);
		RPrimerDriver.addDriver("JeidV4", JeidV4Driver.class);
	}

}
