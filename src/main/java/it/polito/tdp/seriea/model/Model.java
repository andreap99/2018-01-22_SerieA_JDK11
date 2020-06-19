package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private SerieADAO dao;
	private Map<Integer, Season> stagioni;
	private Graph<Annata, DefaultWeightedEdge> grafo;
	private List<Annata> annate;
	
	public Model() {
		this.dao = new SerieADAO();
		this.stagioni = new HashMap<>();
		this.dao.listAllSeasons(stagioni);
		this.annate = new ArrayList<>();
	}

	public List<Team> getTeams() {
		return this.dao.listTeams();
	}

	public String selezionaSquadra(Team squadra) {
		String output = "Elenco annate squadra " + squadra.getTeam() + ":\n";
		annate = this.dao.getAnnate(squadra, stagioni);
		Collections.sort(annate);
		for(Annata a : annate) {
			output += a + "\n";
		}
		return output;
	}
	
	public String creaGrafo() {
		
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, annate);
		for(Annata x : annate) {
			for(Annata y : annate) {
				if(x.getPunti()<y.getPunti()) {
					Graphs.addEdge(grafo, x, y, y.getPunti()-x.getPunti());
				}
				if(x.getPunti()>y.getPunti()) {
					Graphs.addEdge(grafo, y, x, x.getPunti()-y.getPunti());
				}
			}
		}
		//System.out.println(this.grafo.vertexSet().size()+"   "+this.grafo.edgeSet().size());
		Annata oro = null;
		Double best = 0.0;
		for(Annata x : this.grafo.vertexSet()) {
			if(calcolaPunteggio(x)>best) {
				oro = x;
				best = calcolaPunteggio(x);
			}
		}
		if(oro == null) {
			return "\nNon ci sono annate d'oro!\n";
		}
		return "\nAnnata d'oro:\n"+oro+"\nDifferenza di pesi: "+best;
	}

	private Double calcolaPunteggio(Annata x) {
		Double pesoEntrante = 0.0;
		Double pesoUscente = 0.0;
		for(DefaultWeightedEdge d : this.grafo.incomingEdgesOf(x)) {
			pesoEntrante += this.grafo.getEdgeWeight(d);
		}
		for(DefaultWeightedEdge d : this.grafo.outgoingEdgesOf(x)) {
			pesoUscente += this.grafo.getEdgeWeight(d);
		}
		return pesoEntrante-pesoUscente;
	}

}
