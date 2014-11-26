package datas;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import window.Window;

public class IsoValues {
	private Point[][] points;
	private int height;
	private int width;
	private double isoValue;
	private Arrete[][] horizontalArretes;
	private Arrete[][] verticalArretes;
	private List<Line> lines;
	
	public IsoValues(InterpolatedDatas interpolatedDatas, double isoValue, Window window) {
		this.isoValue = isoValue;
		height = interpolatedDatas.getDatas().length;
		width = interpolatedDatas.getDatas()[0].length;
		double pixelWidth = (interpolatedDatas.getMaxLongitude() - interpolatedDatas.getMinLongitude()) / (double) (width - 1);
		double pixelHeight = (interpolatedDatas.getMaxLatitude() - interpolatedDatas.getMinLatitude()) / (double) (height - 1);
		points = new Point[height][width];
		for(int i = 0; i < height; ++i) {
			for(int j = 0; j < width; ++j) {
				points[i][j] = new Point(interpolatedDatas.getMaxLatitude() - (double) i * pixelHeight, interpolatedDatas.getMinLongitude() + (double) j * pixelWidth, interpolatedDatas.getDatas()[i][j]);
			}
			window.setChargement((double) (i) / (double) (height) / 10.0);
		}
		horizontalArretes = new Arrete[height][width - 1];
		verticalArretes = new Arrete[height - 1][width];
		for(int i = 0; i < height; ++i) {
			for(int j = 0; j < width - 1; ++j) {
				horizontalArretes[i][j] = new Arrete(points[i][j], points[i][j + 1], i == 0 || i == height - 1);
			}
			window.setChargement((double) (i) / (double) (height) * 0.35 + 0.1);
		}
		for(int i = 0; i < height - 1; ++i) {
			for(int j = 0; j < width; ++j) {
				verticalArretes[i][j] = new Arrete(points[i][j], points[i + 1][j], j == 0 || j == width - 1);
			}
			window.setChargement((double) (i) / (double) (height) * 0.35 + 0.45);
		}
		lines = new ArrayList<Line>();
		for(int i = 0; i < height - 1; ++i) {
			for(int j = 0; j < width - 1; ++j) {
				int nbIsoValues = 0;
				Arrete[] involved = new Arrete[4];
				if(verticalArretes[i][j].getIsoLoc()) {
					involved[nbIsoValues] = verticalArretes[i][j];
					++nbIsoValues;
				}
				if(verticalArretes[i][j + 1].getIsoLoc()) {
					involved[nbIsoValues] = verticalArretes[i][j + 1];
					++nbIsoValues;
				}
				if(horizontalArretes[i][j].getIsoLoc()) {
					involved[nbIsoValues] = horizontalArretes[i][j];
					++nbIsoValues;
				}
				if(horizontalArretes[i + 1][j].getIsoLoc()) {
					involved[nbIsoValues] = horizontalArretes[i + 1][j];
					++nbIsoValues;
				}
				if(nbIsoValues == 2) {
					lines.add(new Line(involved[0], involved[1]));
				}
				if(nbIsoValues == 4) {
					double centerValue = (points[i][j].value + points[i + 1][j].value + points[i][j + 1].value + points[i + 1][j + 1].value) / 4.0;
					if((centerValue >= isoValue && points[i][j].value >= isoValue) || (centerValue < isoValue && points[i][j].value < isoValue)) {
						lines.add(new Line(involved[0], involved[3]));
						lines.add(new Line(involved[1], involved[2]));
					}
					else {
						lines.add(new Line(involved[0], involved[2]));
						lines.add(new Line(involved[1], involved[3]));
					}
				}
			}
			window.setChargement((double) (i) / (double) (height) / 5.0 + 0.8);
		}
		int id = 0;
		for(int i = 0; i < lines.size(); ++i) {
			if(lines.get(i).idLoop == -1) {
				lines.get(i).setIdLoop(id);
				++id;
			}
		}
		List<List<Point2D.Double>> resultat = new ArrayList<List<Point2D.Double>>();
		List<List<Line>> res = new ArrayList<List<Line>>();
		for(int i = 0; i < id; ++i) {
			resultat.add(new ArrayList<Point2D.Double>());
			res.add(new ArrayList<Line>());
		}
		for(int i = 0; i < lines.size(); ++i) {
			res.get(lines.get(i).idLoop).add(lines.get(i));
		}
		
		for(int i = 0; i < id; ++i) {
			if(res.get(i).size() > 1) {
				int indice = -1;
				int j = 0;
				while(indice == -1 && j < res.get(i).size()) {
					if(res.get(i).get(j).head == 1)
						indice = j;
					++j;
				}
				if(indice == -1) {
					indice = 0;
					Arrete nouv = new Arrete(res.get(i).get(indice).a1);
					Line otherHead = res.get(i).get(indice).next(res.get(i).get(indice).a1);
					otherHead.head = 1;
					if(res.get(i).get(indice).a1.l2.equals(otherHead))
						res.get(i).get(indice).a1.l1 = otherHead;
					res.get(i).get(indice).a1.l2 = null;
					res.get(i).get(indice).a1.isBord = true;
					res.get(i).get(indice).head = 1;
					if(nouv.l1.equals(otherHead))
						nouv.l1 = res.get(i).get(indice);
					nouv.l2 = null;
					nouv.isBord = true;
				}
				Arrete old;
				Arrete courent;
				if(res.get(i).get(indice).next(res.get(i).get(indice).a1) == null) {
					old = res.get(i).get(indice).a1;
					courent = res.get(i).get(indice).a2;
				}
				else {
					old = res.get(i).get(indice).a2;
					courent = res.get(i).get(indice).a1;
				}
				resultat.get(i).add(old.isoLoc);
				while(courent != null) {
					Arrete temp = courent;
					courent = courent.next(old);
					old = temp;
					resultat.get(i).add(old.isoLoc);
				}
			}
		}
		
		window.setIsoValue(resultat);
	}
	
	private class Point {
		double longitude;
		double latitude;
		double value;
		
		Point(double lat, double lon, double val) {
			longitude = lon;
			latitude = lat;
			value = val;
		}
	}
	
	private class Line {
		int idLoop;
		Arrete a1;
		Arrete a2;
		int head;
		
		Line(Arrete a1, Arrete a2) {
			head = 0;
			if(a1.isBord)
				++head;
			if(a2.isBord)
				++head;
			idLoop = -1;
			this.a1 = a1;
			this.a2 = a2;
			a1.addLine(this);
			a2.addLine(this);
		}
		
		Line next(Arrete a) {
			if(this.equals(a.l2))
				return a.l1;
			return a.l2;
		}

		void setIdLoop(int id) {
			idLoop = id;
			Line[] next = new Line[2];
			int nbNext = 0;
			if(this.equals(a1.l2)) {
				next[nbNext] = a1.l1;
				++nbNext;
			}
			else if(this.equals(a1.l1) && a1.l2 != null) {
				next[nbNext] = a1.l2;
				++nbNext;
			}
			if(this.equals(a2.l2)) {
				next[nbNext] = a2.l1;
				++nbNext;
			}
			else if(this.equals(a2.l1) && a2.l2 != null) {
				next[nbNext] = a2.l2;
				++nbNext;
			}
			for(int i = 0; i < nbNext; ++i) {
				if(next[i].idLoop == -1) {
					next[i].setIdLoop(idLoop);
				}
			}
		}
	}
	
	private class Arrete {
		Point a;
		Point b;
		Point2D.Double isoLoc;
		Line l1;
		Line l2;
		boolean isBord;
		
		Arrete(Arrete ar) {
			a = ar.a;
			b = ar.b;
			isoLoc = ar.isoLoc;
			l1 = ar.l1;
			l2 = ar.l2;
		}
		
		Arrete(Point a, Point b, boolean isBord) {
			this.isBord = isBord;
			if(a.value <= b.value) {
				this.a = a;
				this.b = b;
			}
			else {
				this.b = a;
				this.a = b;
			}
			if(this.a.value <= isoValue && isoValue < this.b.value) {
				double percentageB = (isoValue - this.a.value) / (this.b.value - this.a.value);
				isoLoc = new Point2D.Double(this.a.longitude * (1.0 - percentageB) + this.b.longitude * percentageB, this.a.latitude * (1.0 - percentageB) + this.b.latitude * percentageB);
			}
		}
		
		boolean getIsoLoc() {
			return isoLoc != null;
		}
		
		void addLine(Line l) {
			if(l1 == null)
				l1 = l;
			else
				l2 = l;
		}
		
		Arrete next(Arrete exclu) {
			Arrete res = exclu;
			if(l2 != null) {
				if(l2.a1.equals(this))
					res = l2.a2;
				else
					res = l2.a1;
			}
			if(!res.equals(exclu))
				return res;
			if(l1.a1.equals(this))
				res = l1.a2;
			else
				res = l1.a1;
			if(!res.equals(exclu))
				return res;
			return null;
		}
	}
}
