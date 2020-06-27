package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.Conn;
import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {

	Graph<Player, DefaultWeightedEdge> graph;
	Map<Integer, Player> idPlayers;

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

}
