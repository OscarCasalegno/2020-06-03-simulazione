package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {

	public List<Player> listAllPlayers() {
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));

				result.add(player);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Action> listAllActions() {
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"), res.getInt("MatchID"), res.getInt("TeamID"),
						res.getInt("Starts"), res.getInt("Goals"), res.getInt("TimePlayed"), res.getInt("RedCards"),
						res.getInt("YellowCards"), res.getInt("TotalSuccessfulPassesAll"),
						res.getInt("totalUnsuccessfulPassesAll"), res.getInt("Assists"),
						res.getInt("TotalFoulsConceded"), res.getInt("Offsides"));

				result.add(action);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Player> listPlayersByAvgGoals(Double goals) {
		String sql = "SELECT a.PlayerID AS id, p.Name AS name " + "FROM players AS p, actions AS a "
				+ "WHERE p.PlayerID=a.PlayerID " + "GROUP BY a.PlayerID, p.Name " + "HAVING AVG(a.Goals)>? ";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, goals);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("id"), res.getString("name"));

				result.add(player);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static List<Conn> getConn() {
		String sql = "select distinct a1.PlayerID AS id1, a2.playerID AS id2, (sum(a1.TimePlayed) - sum(a2.TimePlayed)) AS diff "
				+ "FROM actions AS a1, actions AS a2 "
				+ "WHERE a1.PlayerID<a2.PlayerID AND a1.`Starts`=1 AND a2.`Starts`=1 AND a1.MatchID=a2.MatchID and a1.TeamID<>a2.TeamID "
				+ "group BY id1, id2";
		List<Conn> result = new ArrayList<Conn>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Conn con = new Conn(res.getInt("id1"), res.getInt("id2"), res.getInt("diff"));

				result.add(con);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
