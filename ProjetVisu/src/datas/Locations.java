package datas;

import io.Reader;

public class Locations {
	private static Locations singleton = null;
	
	private final String path1 = "FichierDesSitesDeAirRhoneAlpes.csv";
	private final String path2 = "IndiceDesStationsDeMesure.csv";
	
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

	private Locations() {
		Reader file1 = new Reader(path1);
		Reader file2 = new Reader(path2);
		
		nom = new String[nb_sites];
		indicatif = new int[nb_sites];
		coord_long = new double[nb_sites];
		coord_lat = new double[nb_sites];
		for(int i = begin_line; i < begin_line + nb_sites; ++i) {
			indicatif[i - begin_line] = Integer.parseInt(file1.datas.getData(i, indicatif_colonne));
			nom[i - begin_line] = file1.datas.getData(i, nom_colonne);
			coord_long[i - begin_line] = Double.parseDouble(file1.datas.getData(i, longitude_colonne).replace(',', '.'));
			coord_lat[i - begin_line] = Double.parseDouble(file1.datas.getData(i, latitude_colonne).replace(',', '.'));
			
			int line = file2.datas.searchInColumn(file1.datas.getData(i, indicatif_colonne), 1);
			if(line != -1) {
				nom[i - begin_line] = file2.datas.getData(line, 0);
			}
		}
	}
	
	public static Locations getInstance() {
		if(singleton == null)
			singleton = new Locations();
		return singleton;
	}
	
	public int getIndex(String name) {
		int result = -1;
		int i = 0;
		while(result == -1 && i < nom.length) {
			if(name.contains(nom[i])) {
				result = i;
			}
			++i;
		}
		return result;
	}
}
