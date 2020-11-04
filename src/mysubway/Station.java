package mysubway;

public class Station {
	private int staId;
	private String line;
	private String staName;
	
	public Station(int staId, String staName, String line) {
		this.staId = staId;
		this.staName = staName;
		this.line = line;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public int getStaId() {
		return staId;
	}

	public void setStaId(int staId) {
		this.staId = staId;
	}

	public String getStaName() {
		return staName;
	}

	public void setStaName(String staName) {
		this.staName = staName;
	}
}
