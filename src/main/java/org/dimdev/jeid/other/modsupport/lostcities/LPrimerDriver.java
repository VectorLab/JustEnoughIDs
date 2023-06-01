package org.dimdev.jeid.other.modsupport.lostcities;

import mcjty.lostcities.dimensions.world.driver.RPrimerDriver;

public class LPrimerDriver {
	
	public static void init() {
		RPrimerDriver.addDriver("Jeid", JeidDriver.class);
	}

}
