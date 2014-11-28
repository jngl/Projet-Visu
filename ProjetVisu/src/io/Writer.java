package io;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import utils.Utils;

public class Writer {

	public static void write(BufferedImage[] image, Date[] dates, String name, double north, double south, double east, double west) {
		File dir = new File(System.getProperty("user.dir") + File.separator + "Out" + File.separator + name + "_Images");
		if(!dir.exists()) {
			dir.mkdir();
		}
		else {
		    File[] files = dir.listFiles();
		    if(files!=null) {
		        for(File f: files) {
		            if(!f.isDirectory()) {
		                f.delete();
		            }
		        }
		    }
		}
		for(int i = 0; i < image.length; ++i) {
			toJPEG(image, name, i);
		}
		toKML(name, north, south, east, west, dates);
	}
	public static void write(BufferedImage[] image, Date[] dates, String name, double north, double south, double east, double west, List<List<List<Point2D.Double>>> isoLines) {
		File dir = new File(System.getProperty("user.dir") + File.separator + "Out" + File.separator + name + "_Images");
		if(!dir.exists()) {
			dir.mkdir();
		}
		else {
		    File[] files = dir.listFiles();
		    if(files!=null) {
		        for(File f: files) {
		            if(!f.isDirectory()) {
		                f.delete();
		            }
		        }
		    }
		}
		for(int i = 0; i < image.length; ++i) {
			toJPEG(image, name, i);
		}
		toKML(name, north, south, east, west, isoLines, dates);
	}
	
	private static void toJPEG(BufferedImage[] image, String name, int i) {
		try {
			ImageIO.write(image[i], "PNG", new File(System.getProperty("user.dir") + File.separator + "Out" + File.separator + name + "_Images" + File.separator + name + "_" + i + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void toKML(String name, double north, double south, double east, double west, List<List<List<Point2D.Double>>> isoLines, Date[] dates) {
		String ls = System.lineSeparator();
		Calendar cal = Calendar.getInstance();
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
				+ ls + "  <Folder>";
		for(int d = 0; d < dates.length; ++d) {
			cal.setTime(dates[d]);
			cal.add(Calendar.MINUTE, 59);
			cal.add(Calendar.SECOND, 59);
			Date endDate = cal.getTime();
			text += ls + "    <Placemark>"
					+ ls + "      <TimeSpan>"
					+ ls + "        <begin>" + Utils.getKMLString(dates[d]) + "</begin>"
					+ ls + "        <end>" + Utils.getKMLString(endDate) + "</end>"
					+ ls + "      </TimeSpan>"
					+ ls + "      <name>Overlay on terrain and isoLines</name>"
					+ ls + "      <visibility>1</visibility>"
					+ ls + "      <description>Overlay and isoLines</description>"
					+ ls + "      <styleUrl>#BlackLineBluePoly</styleUrl>"
					+ ls + "      <MultiGeometry>";
			for(int i = 0; i < isoLines.get(d).size(); ++i) {
				text += ls + "      <LineString>"
					+ ls + "        <extrude>1</extrude>"
					+ ls + "        <tessellate>1</tessellate>"
					+ ls + "        <altitudeMode>relativeToGround</altitudeMode>"
					+ ls + "        <coordinates>";
				for(int j = 0; j < isoLines.get(d).get(i).size(); ++j) {
					text += ls + "          " + isoLines.get(d).get(i).get(j).x + "," + isoLines.get(d).get(i).get(j).y + "," + (int) (Math.min(Math.abs(north - south), Math.abs(west - east)) * 2000.0);
				}
				text += ls + "        </coordinates>"
					+ ls + "      </LineString>";
			}
			text +=   ls + "      </MultiGeometry>"
					+ ls + "    </Placemark>"
					+ ls + "    <GroundOverlay>"
					+ ls + "      <TimeSpan>"
					+ ls + "        <begin>" + Utils.getKMLString(dates[d]) + "</begin>"
					+ ls + "        <end>" + Utils.getKMLString(endDate) + "</end>"
					+ ls + "      </TimeSpan>"
					+ ls + "      <Icon>"
					+ ls + "        <href>" + System.getProperty("user.dir") + File.separator + "Out" + File.separator + name + "_Images" + File.separator + name + "_" + d + ".png</href>"
					+ ls + "      </Icon>"
					+ ls + "      <LatLonBox>"
					+ ls + "        <north>" + north + "</north>"
					+ ls + "        <south>" + south + "</south>"
					+ ls + "        <east>" + east + "</east>"
					+ ls + "        <west>" + west + "</west>"
					+ ls + "        <rotation>-0.1556640799496235</rotation>"
					+ ls + "      </LatLonBox>"
					+ ls + "    </GroundOverlay>";
		}
		text += ls + "  </Folder>"
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
	
	private static void toKML(String name, double north, double south, double east, double west, Date[] dates) {
		String ls = System.lineSeparator();
		Calendar cal = Calendar.getInstance();
		String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ ls + "<kml xmlns=\"http://www.opengis.net/kml/2.2\">"
				+ ls + "  <Folder>";
		for(int i = 0; i < dates.length; ++i) {
			cal.setTime(dates[i]);
			cal.add(Calendar.MINUTE, 59);
			cal.add(Calendar.SECOND, 59);
			Date endDate = cal.getTime();
			text += ls + "    <GroundOverlay>"
				+ ls + "      <TimeSpan>"
				+ ls + "        <begin>" + Utils.getKMLString(dates[i]) + "</begin>"
				+ ls + "        <end>" + Utils.getKMLString(endDate) + "</end>"
				+ ls + "      </TimeSpan>"
				+ ls + "      <name>Overlay on terrain</name>"
				+ ls + "      <visibility>1</visibility>"
				+ ls + "      <description>Overlay</description>"
				+ ls + "      <Icon>"
				+ ls + "        <href>" + System.getProperty("user.dir") + File.separator + "Out" + File.separator + name + "_Images" + File.separator + name + "_" + i + ".png</href>"
				+ ls + "      </Icon>"
				+ ls + "      <LatLonBox>"
				+ ls + "        <north>" + north + "</north>"
				+ ls + "        <south>" + south + "</south>"
				+ ls + "        <east>" + east + "</east>"
				+ ls + "        <west>" + west + "</west>"
				+ ls + "        <rotation>-0.1556640799496235</rotation>"
				+ ls + "      </LatLonBox>"
				+ ls + "    </GroundOverlay>";
		}
		text += ls + "  </Folder>"
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
