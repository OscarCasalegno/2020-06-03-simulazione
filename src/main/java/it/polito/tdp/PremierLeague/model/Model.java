package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.Conn;
import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {

	Graph<Player, DefaultWeightedEdge> graph;
	Map<Integer, Player> idPlayers;
	Set<Player> dream;
	Integer titolarita;

	public void creaGrafo(Double goals) {
		this.graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

		List<Player> players = PremierLeagueDAO.listPlayersByAvgGoals(goals);

		this.idPlayers = new HashMap<>();
		for (Player p : players) {
			this.idPlayers.put(p.getPlayerID(), p);
		}

		Graphs.addAllVertices(this.graph, players);

		List<Conn> con = PremierLeagueDAO.getConn();

		for (Conn c : con) {

			if (this.graph.containsVertex(this.idPlayers.get(c.getId1()))
					&& this.graph.containsVertex(this.idPlayers.get(c.getId2()))) {
				if (c.getDiff() < 0) {
					DefaultWeightedEdge e = this.graph.addEdge(this.idPlayers.get(c.getId2()),
							this.idPlayers.get(c.getId1()));
					this.graph.setEdgeWeight(e, -(c.getDiff().doubleValue()));
				} else if (c.getDiff() > 0) {
					DefaultWeightedEdge e = this.graph.addEdge(this.idPlayers.get(c.getId1()),
							this.idPlayers.get(c.getId2()));
					this.graph.setEdgeWeight(e, c.getDiff().doubleValue());
				}
			}

		}
	}

	public Player getTopPlayer() {
		if (this.graph == null) {
			return null;
		}
		Player best = null;
		Integer bestDeg = 0;
		for (Player p : this.graph.vertexSet()) {
			if (this.graph.outDegreeOf(p) > bestDeg) {
				bestDeg = this.graph.outDegreeOf(p);
				best = p;
			}
		}
		return best;
	}

	public List<PlayerMinutes> getBattuti(Player best) {
		List<PlayerMinutes> pm = new ArrayList<>();

		for (DefaultWeightedEdge e : this.graph.outgoingEdgesOf(best)) {
			pm.add(new PlayerMinutes(this.graph.getEdgeTarget(e), this.graph.getEdgeWeight(e)));
		}

		pm.sort(null);

		return pm;
	}

	public Set<Player> generaDreamTeam(Integer k) {
		dream = new HashSet<>();
		titolarita = 0;

		Set<Player> esclusi = new HashSet<>();
		Set<Player> parziale = new HashSet<>();

		this.cerca(parziale, esclusi, 0, k);

		return dream;

	}

	private void cerca(Set<Player> parziale, Set<Player> esclusi, Integer livello, Integer k) {

		if (parziale.size() == k) {
			Integer tito = this.calcolaTitolarita(parziale);

			if (tito > this.titolarita) {
				this.dream = new HashSet<>(parziale);
				this.titolarita = tito;
			}

		}

		for (Player p : this.graph.vertexSet()) {
			if (!parziale.contains(p) && !esclusi.contains(p)) {
				parziale.add(p);
				for (DefaultWeightedEdge e : this.graph.outgoingEdgesOf(p)) {
					esclusi.add(this.graph.getEdgeTarget(e));
				}
				this.cerca(parziale, esclusi, livello + 1, k);
				parziale.remove(p);
				for (DefaultWeightedEdge e : this.graph.outgoingEdgesOf(p)) {
					esclusi.remove(this.graph.getEdgeTarget(e));
				}
			}
		}
	}

	private Integer calcolaTitolarita(Set<Player> parziale) {
		Double somma = 0.0;

		for (Player p : parziale) {
			for (DefaultWeightedEdge e : this.graph.outgoingEdgesOf(p)) {
				somma += this.graph.getEdgeWeight(e);
			}
			for (DefaultWeightedEdge e : this.graph.incomingEdgesOf(p)) {
				somma -= this.graph.getEdgeWeight(e);
			}
		}

		return somma.intValue();
	}

	public Integer getTitolarita() {
		return this.titolarita;
	}

}
