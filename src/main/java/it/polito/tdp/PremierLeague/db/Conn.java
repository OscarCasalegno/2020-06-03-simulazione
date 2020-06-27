package it.polito.tdp.PremierLeague.db;

public class Conn {

	private Integer id1;
	private Integer id2;
	private Integer diff;

	/**
	 * @param id1
	 * @param id2
	 * @param diff
	 */
	public Conn(Integer id1, Integer id2, Integer diff) {
		super();
		this.id1 = id1;
		this.id2 = id2;
		this.diff = diff;
	}

	public Integer getId1() {
		return id1;
	}

	public void setId1(Integer id1) {
		this.id1 = id1;
	}

	public Integer getId2() {
		return id2;
	}

	public void setId2(Integer id2) {
		this.id2 = id2;
	}

	public Integer getDiff() {
		return diff;
	}

	public void setDiff(Integer diff) {
		this.diff = diff;
	}

}
