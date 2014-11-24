package window;

import io.Reader;

import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

import utils.Strings;

public class ColorMapListCellRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 3L;
    private JLabel label;
    private Color textSelectionColor = Color.BLACK;
    private Color backgroundSelectionColor = Color.CYAN;
    private Color textNonSelectionColor = Color.BLACK;
    private Color backgroundNonSelectionColor = Color.WHITE;
    private static final int imageWidth = 53;
    private static final int imageHeight = 22;

    ColorMapListCellRenderer() {
        label = new JLabel();
        label.setOpaque(true);
    }
	
    public Component getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index,
            boolean selected,
            boolean expanded) {

            File file = (File)value;
            Strings datas = new Reader(file.getAbsolutePath()).datas;
            BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            for(int i = 0; i < imageWidth; ++i) {
            	image.setRGB(i, 0, Color.BLACK.getRGB());
            	image.setRGB(i, imageHeight - 1, Color.BLACK.getRGB());
            }
            for(int i = 1; i < imageHeight - 1; ++i) {
            	image.setRGB(0, i, Color.BLACK.getRGB());
            	image.setRGB(imageWidth - 1, i, Color.BLACK.getRGB());
            }
            String[] line = datas.getData(1, 0).split("\"");
            ColorMapColor lastColor = new ColorMapColor(line);
            int i = 2;
            line = datas.getData(i, 0).split("\"");
            int columnStart = 1;
            while(line.length > 1) {
                ColorMapColor newColor = new ColorMapColor(line);
                int columnEnd = (int) ((newColor.x + 1.0)/2.0 * (double) (imageWidth - 2) + 1.1);
                for(int column = columnStart; column < columnEnd; ++column) {
                	float percentageStart = 1.0f - (float) (column - columnStart) / (float) (columnEnd - columnStart);
                	float r = percentageStart * (float) lastColor.r + (1.0f - percentageStart) * (float) newColor.r;
                	float g = percentageStart * (float) lastColor.g + (1.0f - percentageStart) * (float) newColor.g;
                	float b = percentageStart * (float) lastColor.b + (1.0f - percentageStart) * (float) newColor.b;
                	float a = percentageStart * (float) lastColor.o + (1.0f - percentageStart) * (float) newColor.o;
                	int argb = new Color(r, g, b, a).getRGB();
                	for(int j = 1; j < imageHeight - 1; ++j) {
                		image.setRGB(column, j, argb);
                	}
                }
                lastColor = newColor;
                columnStart = columnEnd;
            	++i;
            	line = datas.getData(i, 0).split("\"");
            }
            label.setIcon(new ImageIcon(image));
            label.setText(datas.getData(0, 0).split("\"")[1]);
            label.setToolTipText(file.getPath());

            if (selected) {
                label.setBackground(backgroundSelectionColor);
                label.setForeground(textSelectionColor);
            } else {
                label.setBackground(backgroundNonSelectionColor);
                label.setForeground(textNonSelectionColor);
            }

            return label;
        }
    
    private class ColorMapColor {
    	public double x;
    	public double o;
    	public double r;
    	public double g;
    	public double b;
    	
    	ColorMapColor(String[] line) {
    		x = Double.parseDouble(line[1]);
    		o = Double.parseDouble(line[3]);
    		r = Double.parseDouble(line[5]);
    		g = Double.parseDouble(line[7]);
    		b = Double.parseDouble(line[9]);
    	}
    }
}
