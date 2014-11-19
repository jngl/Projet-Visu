package io;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;

public class Writer {

	public static void write(BufferedImage image, String name, double north, double south, double east, double west) {
		toJPEG(image, name);
		toKML(name, north, south, east, west);
	}
	
	private static void toJPEG(BufferedImage image, String name) {
		try {
			ImageIO.write(image, "PNG", new File(System.getProperty("user.dir") + File.separator + "Out" + File.separator + name + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void toKML(String name, double north, double south, double east, double west) {
		String ls = System.lineSeparator();
		String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ ls + "<kml xmlns=\"http://www.opengis.net/kml/2.2\">"
				+ ls + "  <GroundOverlay>"
				+ ls + "    <name>Overlay on terrain</name>"
				+ ls + "    <visibility>1</visibility>"
				+ ls + "    <description>Overlay</description>"
				+ ls + "    <Icon>"
				+ ls + "      <href>" + System.getProperty("user.dir") + File.separator + "Out" + File.separator + name + ".png</href>"
				+ ls + "    </Icon>"
				+ ls + "    <LatLonBox>"
				+ ls + "      <north>" + north + "</north>"
				+ ls + "      <south>" + south + "</south>"
				+ ls + "      <east>" + east + "</east>"
				+ ls + "      <west>" + west + "</west>"
				+ ls + "      <rotation>-0.1556640799496235</rotation>"
				+ ls + "    </LatLonBox>"
				+ ls + "  </GroundOverlay>"
				+ ls + "</kml>";
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir") + File.separator + "Out" + File.separator + name + ".kml"), "UTF-8"));
		    out.write(text);
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
