package io;

public class Locations {
	private final String p1 = "";
	private final String p2 = "";
	private final int nb_sites = 151;
	private final int begin_line = 20;
	private final int indicatif_colonne = 0;
	private final int nom_colonne = 1;
	private final int longitude_colonne = 5;
	private final int latitude_colonne = 6;
	
	public String[] nom;
	public int[] indicatif;
	public double[] coord_long;
	public double[] coord_lat;

	public Locations(String path1, String path2) {
		if(path1 == null)
			path1 = p1;
		if(path2 == null)
			path2 = p2;
		Reader file1 = new Reader(path1);
		Reader file2 = new Reader(path1);
		
		nom = new String[nb_sites];
		indicatif = new int[nb_sites];
		coord_long = new double[nb_sites];
		coord_lat = new double[nb_sites];
		for(int i = begin_line; i < begin_line + nb_sites; ++i) {
			indicatif[i - begin_line] = Integer.parseInt(file1.datas.getData(i, indicatif_colonne));
			nom[i - begin_line] = file1.datas.getData(i, nom_colonne);
			coord_long[i - begin_line] = Double.parseDouble(file1.datas.getData(i, longitude_colonne));
			coord_lat[i - begin_line] = Double.parseDouble(file1.datas.getData(i, latitude_colonne));
			
			int line = file2.datas.searchInColumn(file1.datas.getData(i, indicatif_colonne), 1);
			if(line != -1) {
				nom[i - begin_line] = file2.datas.getData(line, 0);
			}
		}
	}
}
