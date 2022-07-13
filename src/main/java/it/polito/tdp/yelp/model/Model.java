package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	List<Business> locali;
	Graph<Business, DefaultWeightedEdge> grafo;
	YelpDao dao = new YelpDao();
	
	List<Business> percorso;
	
	public void creaGrafo(String city){
		
		this.locali = dao.getAllBusinessByCity(city);
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, locali);
		
		for(Business b1 : this.grafo.vertexSet()) {
			for(Business b2 : this.grafo.vertexSet()) {
				if(!this.grafo.containsEdge(b1, b2) && !b1.equals(b2)) {					
					LatLng latLng1 = new LatLng(b1.getLatitude(),b1.getLongitude());
					LatLng latLng2 = new LatLng(b2.getLatitude(),b2.getLongitude());
					double peso = LatLngTool.distance(latLng1, latLng2, LengthUnit.KILOMETER);
					Graphs.addEdge(this.grafo, b1, b2, peso);
				}
			}
		}
		
		System.out.println("#VERTICI: " + this.grafo.vertexSet().size());
		System.out.println("#ARCHI: " + this.grafo.edgeSet().size());
		
	}
	
	public Business getPiuDistante(Business b) {
		Business locale;
		double max = 0;
		for(Business b1 : Graphs.neighborListOf(this.grafo, b)) {
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(b, b1))>max) {
				max=this.grafo.getEdgeWeight(this.grafo.getEdge(b, b1));
			}
		}
		
		for(Business b1 : Graphs.neighborListOf(this.grafo, b)) {
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(b, b1))==max) {
				return b1;
			}
		}
		
		return null;
	}
	
	public List<String> getAllCity() {
		return dao.getAllCity();
	}
	
	public Graph<Business, DefaultWeightedEdge> getGrafo(){
		return this.grafo;
	}
	
	public List<Business> calcolaPercorso(Business partenza, Business arrivo, int soglia){
		percorso = new ArrayList<>();
		List<Business> parziale = new ArrayList<>();
		parziale.add(partenza);
		cerca(partenza, arrivo, parziale, soglia);
		percorso.add(arrivo);
		return percorso;
	}

	private void cerca(Business partenza, Business arrivo, List<Business> parziale, int soglia) {
		
		for(Business b1 : Graphs.neighborListOf(this.grafo, arrivo)) {
			if(parziale.get(parziale.size()-1).equals(b1) && parziale.size()>1){
				if(parziale.size()>percorso.size()) {
					this.percorso = new ArrayList<Business>(parziale);
				}
			}			
		}
		
		for(Business b : Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			if(b.getStars()>soglia && !parziale.contains(b) && !b.equals(arrivo)) {
				parziale.add(b);
				cerca(partenza,arrivo,parziale,soglia);
				parziale.remove(parziale.size()-1);
			}
		}
	}
	
	public List<Business> getPercorso(){
		return this.percorso;
	}
}
