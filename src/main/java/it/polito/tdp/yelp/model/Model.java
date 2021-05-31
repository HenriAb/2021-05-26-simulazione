package it.polito.tdp.yelp.model;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private Graph<Business, DefaultWeightedEdge> grafo;
	private List<Business> vertici; // per mantenere ordine (infatti ha gli stessi Business di grafo.vertexSet())
	private Map<String, Business> verticiIdMap;
	
	public Model() {
		this.dao = new YelpDao();
	}
	
	public List<String> getAllCities(){
		return dao.getAllCities();
	}
	
	public String creaGrafo(String citta, Year anno) {
		
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		// aggiungo i vertici
		this.vertici = this.dao.getBusinessByCityAndYear(citta, anno);
		this.verticiIdMap = new HashMap<>(); // così si aggiorna e butta via i vecchi vertici se cambio citta e anno
		for(Business b : vertici) {
			this.verticiIdMap.put(b.getBusinessId(), b);
		}
		
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		// aggiungo gli archi
		
		// Ipotesi 1: calcolare la media recensioni mentre leggo i Business
//		for(Business b1 : this.vertici) {
//			for(Business b2 : this.vertici) {
//				if(b1.getMediaRecensioni() < b2.getMediaRecensioni()) {
//					Graphs.addEdge(this.grafo, b1, b2, b2.getMediaRecensioni() - b1.getMediaRecensioni());
//				}
//			}
//		}
		
		// Ipotesi con mappa: non modifico oggetto Business, ma creo una mappa per ricordarmi le medie delle recensioni
//		Map<Business, Double> medieRecensioni = new HashMap<>();
//		// carica la mappa con il DAO
//		
//		for(Business b1 : this.vertici) {
//			for(Business b2 : this.vertici) {
//				if(medieRecensioni.get(b1) < medieRecensioni.get(b2)) {
//					Graphs.addEdge(this.grafo, b1, b2, medieRecensioni.get(b2) - medieRecensioni.get(b1));
//				}
//			}
//		}
		
		// Ipotesi 3: faccio calcolare gli archi al db
		List<Adiacenza> archi = this.dao.calcolaArchi(citta, anno);
		for(Adiacenza a : archi) {
			Graphs.addEdge(this.grafo, this.verticiIdMap.get(a.getbId1()), this.verticiIdMap.get(a.getbId2()), a.getPeso());
		}
		
		return String.format("Grafo creato con %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
		
	}
	
	public Business getLocaleMigliore() {
		
		Double max = 0.0;
		Business result = null;
		
		for(Business b : this.grafo.vertexSet()) {
			double val = 0.0;
			for(DefaultWeightedEdge e : this.grafo.incomingEdgesOf(b)) {
				val += this.grafo.getEdgeWeight(e);
			}
			
			for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(b)) {
				val -= this.grafo.getEdgeWeight(e);
			}
			
			if(val > max) {
				max = val;
				result = b;
			}
		}
		
		return result;
	}
	
	
}
