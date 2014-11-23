package main;

import io.Writer;

import java.awt.image.BufferedImage;
import java.io.File;

import datas.GazDatas;
import datas.Locations;
import exception.MyOutOfBoundException;
import utils.*;
import window.Window;

public class Main {
	public static void main(String[] args) {
		Test.printGazDatas();
	}
	
	@SuppressWarnings("unused")
	private static class Test {
		public static void buildKML() {
			BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
			for(int i = 0; i < 200; ++i)
				for(int j = 0; j < 200; ++j)
					image.setRGB(i, j, ((255 - i) << 24) | (255 << 16));
			Writer.write(image, "Red", 37, 35, 16, 14);
		}
		
		public static void printLocations() {
			for(int i = 0; i < Locations.getInstance().indicatif.length; ++i) {
				String s = Locations.getInstance().indicatif[i] + "   " + Locations.getInstance().coord_lat[i] + "   " +
						Locations.getInstance().coord_long[i] + "   " + Locations.getInstance().nom[i];
				System.out.println(s);
			}
		}
		
		public static void printGazDatas() {
			GazDatas gazDatas = null;
			try {
				gazDatas = new GazDatas("In" + File.separator + "particulepm10aout2012.csv");
				for(int i = 0; i < gazDatas.getDatas(Utils.getDate(8, 1, 2012, 4, 0, 0)).size(); ++i) {
					System.out.println(gazDatas.getDatas(Utils.getDate(8, 1, 2012, 4, 0, 0)).get(i).toString());
				}
			} catch (MyOutOfBoundException e) {
				e.printStackTrace();
			}
		}
		
		public static void testWindow() {
			Window w = new Window();
		}
	}
}
