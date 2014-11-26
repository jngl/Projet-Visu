package io;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.imageio.ImageIO;

public class Writer {

	public static void write(BufferedImage image, String name, double north, double south, double east, double west) {
		toJPEG(image, name);
		toKML(name, north, south, east, west);
	}
	public static void write(BufferedImage image, String name, double north, double south, double east, double west, List<List<Point2D.Double>> isoLines) {
		toJPEG(image, name);
		toKML(name, north, south, east, west, isoLines);
	}
	
	private static void toJPEG(BufferedImage image, String name) {
		try {
			ImageIO.write(image, "PNG", new File(System.getProperty("user.dir") + File.separator + "Out" + File.separator + name + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void toKML(String name, double north, double south, double east, double west, List<List<Point2D.Double>> isoLines) {
		String ls = System.lineSeparator();
		String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ ls + "<kml xmlns=\"http://www.opengis.net/kml/2.2\">"
				+ ls + "  <Document>"
			    + ls + "    <name>KmlFile</name>"
			    + ls + "    <Style id=\"BlackLineBluePoly\">"
			    + ls + "      <LineStyle>"
				+ ls + "        <width>3</width>"
			    + ls + "        <color>ff000000</color>"
			    + ls + "      </LineStyle>"
			    + ls + "      <PolyStyle>"
			    + ls + "        <color>ff222222</color>"
			    + ls + "      </PolyStyle>"
			    + ls + "    </Style>"
			    + ls + "    <Placemark>"
				+ ls + "      <name>Overlay on terrain and isoLines</name>"
				+ ls + "      <visibility>1</visibility>"
				+ ls + "      <description>Overlay and isoLines</description>"
				+ ls + "      <styleUrl>#BlackLineBluePoly</styleUrl>"
				+ ls + "      <MultiGeometry>";
		for(int i = 0; i < isoLines.size(); ++i) {
			text += ls + "      <LineString>"
				+ ls + "        <extrude>1</extrude>"
				+ ls + "        <tessellate>1</tessellate>"
				+ ls + "        <altitudeMode>relativeToGround</altitudeMode>"
				+ ls + "        <coordinates>";
			for(int j = 0; j < isoLines.get(i).size(); ++j) {
				text += ls + "          " + isoLines.get(i).get(j).x + "," + isoLines.get(i).get(j).y + "," + (int) (Math.min(Math.abs(north - south), Math.abs(west - east)) * 2000.0);
			}
			text += ls + "        </coordinates>"
				+ ls + "      </LineString>";
		}
		text +=   ls + "      </MultiGeometry>"
				+ ls + "    </Placemark>"
				+ ls + "    <GroundOverlay>"
				+ ls + "      <Icon>"
				+ ls + "        <href>" + System.getProperty("user.dir") + File.separator + "Out" + File.separator + name + ".png</href>"
				+ ls + "      </Icon>"
				+ ls + "      <LatLonBox>"
				+ ls + "        <north>" + north + "</north>"
				+ ls + "        <south>" + south + "</south>"
				+ ls + "        <east>" + east + "</east>"
				+ ls + "        <west>" + west + "</west>"
				+ ls + "        <rotation>-0.1556640799496235</rotation>"
				+ ls + "      </LatLonBox>"
				+ ls + "    </GroundOverlay>"
				+ ls + "  </Document>"
				+ ls + "</kml>";
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir") + File.separator + "Out" + File.separator + name + ".kml"), "UTF-8"));
		    out.write(text);
		    out.close();
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
