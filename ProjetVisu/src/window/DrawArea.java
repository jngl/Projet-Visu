package window;

import io.Reader;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

import utils.Strings;
import datas.InterpolatedDatas;

public class DrawArea extends JPanel {
	private static final long serialVersionUID = 1L;
	private ColorMap colorMap;
	private double[][] datas;
	private double minValue;
	private double maxValue;
	private BufferedImage image;

	public DrawArea(InterpolatedDatas interpolatedDatas) {
		datas = interpolatedDatas.getDatas();
		minValue = interpolatedDatas.getMinValue();
		maxValue = interpolatedDatas.getMaxValue();
		image = new BufferedImage(datas[0].length, datas.length, BufferedImage.TYPE_INT_ARGB);
	}
	
	public void setColorMap(File colorMap) {
		this.colorMap = new ColorMap(colorMap, minValue, maxValue);
		for(int i = 0; i < datas.length; ++i) {
			for(int j = 0; j < datas[0].length; ++j) {
				image.setRGB(j, i, this.colorMap.getColor(datas[i][j]));
			}
		}
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		graphics.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}
	
	private class ColorMap {
		public ArrayList<MapColor> colors;
		
		public ColorMap(File colorMap, double minValue, double maxValue) {
			colors = new ArrayList<MapColor>();
            Strings lines = new Reader(colorMap.getAbsolutePath()).datas;
            int i = 1;
            String[] line = lines.getData(i, 0).split("\"");
            while(line.length > 1) {
                colors.add(new MapColor(line));
                colors.get(colors.size() - 1).x = (colors.get(colors.size() - 1).x + 1.0) / 2.0 * (maxValue - minValue) + minValue;
                ++i;
                line = lines.getData(i, 0).split("\"");
            }
		}
		
		public int getColor(double value) {
			int i = 0;
			while(value > colors.get(i + 1).x)
				++i;
			float percentageEnd = (float) ((value - colors.get(i).x) / (colors.get(i + 1).x - colors.get(i).x));
			float percentageStart = 1.0f - percentageEnd;
			float r = percentageStart * colors.get(i).r + percentageEnd * colors.get(i + 1).r;
			float g = percentageStart * colors.get(i).g + percentageEnd * colors.get(i + 1).g;
			float b = percentageStart * colors.get(i).b + percentageEnd * colors.get(i + 1).b;
			float a = percentageStart * colors.get(i).o + percentageEnd * colors.get(i + 1).o;
			return new Color(r, g, b, a).getRGB();
		}
	}
		
    private class MapColor {
    	public double x;
    	public float o;
    	public float r;
    	public float g;
    	public float b;
    	
    	MapColor(String[] line) {
    		x = Double.parseDouble(line[1]);
    		o = (float) Double.parseDouble(line[3]);
    		r = (float) Double.parseDouble(line[5]);
    		g = (float) Double.parseDouble(line[7]);
    		b = (float) Double.parseDouble(line[9]);
    	}
    }
}
