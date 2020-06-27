package it.polito.tdp.PremierLeague.model;

public class PlayerMinutes implements Comparable<PlayerMinutes> {

	private Player player;
	private Double minutes;

	/**
	 * @param player
	 * @param d
	 */
	public PlayerMinutes(Player player, double d) {
		super();
		this.player = player;
		this.minutes = d;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Double getMinutes() {
		return minutes;
	}

	public void setMinutes(Double minutes) {
		this.minutes = minutes;
	}

	@Override
	public int compareTo(PlayerMinutes other) {
		return other.minutes.compareTo(this.minutes);
	}

}
