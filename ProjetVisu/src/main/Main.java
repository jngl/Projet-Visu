package main;

import io.Writer;

import java.awt.image.BufferedImage;

import datas.GazDatas;
import datas.Locations;
import exception.MyOutOfBoundException;
import utils.*;

public class Main {
	public static void main(String[] args) {
		Test.buildKML();
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
				gazDatas = new GazDatas("particulepm10aout2012.csv", Utils.getDate(8, 1, 2012, 4, 0, 0));
			} catch (MyOutOfBoundException e) {
				e.printStackTrace();
			}
			for(int i = 0; i < gazDatas.getDatas().size(); ++i) {
				System.out.println(gazDatas.getDatas().get(i).toString());
			}
		}
	}
}
