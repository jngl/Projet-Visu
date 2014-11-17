package main;

import io.Locations;

public class Main {
	public static void main(String[] args) {
		for(int i = 0; i < Locations.getInstance().indicatif.length; ++i) {
			String s = Locations.getInstance().indicatif[i] + "   " + Locations.getInstance().coord_lat[i] + "   " +
					Locations.getInstance().coord_long[i] + "   " + Locations.getInstance().nom[i];
			System.out.println(s);
		}
	}
}
