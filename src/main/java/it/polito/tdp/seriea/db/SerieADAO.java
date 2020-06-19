package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Annata;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public void listAllSeasons(Map<Integer, Season> stagioni) {
		String sql = "SELECT season, description FROM seasons";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				stagioni.put(res.getInt("season") ,new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Team(res.getString("team")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Annata> getAnnate(Team squadra, Map<Integer, Season> stagioni) {
		final String vittorie = "SELECT Season, COUNT(*) AS vittorie " + 
				"FROM matches " + 
				"WHERE (HomeTeam = ? AND FTR = 'H') OR " + 
				"(AwayTeam = ? AND FTR = 'A') " + 
				"GROUP BY Season ";
		final String pareggi = "SELECT Season, COUNT(*) AS pareggi " + 
				"FROM matches " + 
				"WHERE (HomeTeam = ? AND FTR = 'D') OR " + 
				"(AwayTeam = ? AND FTR = 'D') " + 
				"GROUP BY Season ";
		Map<Integer, Annata> result = new HashMap<>();
		
		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st1 = conn.prepareStatement(vittorie);
			PreparedStatement st2 = conn.prepareStatement(pareggi);
			st1.setString(1, squadra.getTeam());
			st1.setString(2, squadra.getTeam());
			st2.setString(1, squadra.getTeam());
			st2.setString(2, squadra.getTeam());
			ResultSet rs1 = st1.executeQuery();
			while(rs1.next()) {
				Annata a = new Annata(stagioni.get(rs1.getInt("Season")), 3*rs1.getInt("vittorie"));
				result.put(rs1.getInt("Season"), a);
			}
			ResultSet rs2 = st2.executeQuery();
			while(rs2.next()) {
				Annata x = result.get(rs2.getInt("Season"));
				x.addPunti(rs2.getInt("pareggi"));
			}
			List<Annata> lista = new ArrayList<>(result.values());
			conn.close();
			return lista;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}

