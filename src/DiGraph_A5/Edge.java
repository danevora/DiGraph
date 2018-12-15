package DiGraph_A5;

public class Edge {
	
	public long idNum;
	public Vertex sLabel;
	public Vertex dLabel;
	public long weight;
	public String eLabel;
	
	public Edge(long idNum, Vertex sLabel, Vertex dLabel, long weight, String eLabel) {
		this.idNum = idNum;
		this.sLabel = sLabel;
		this.dLabel = dLabel;
		this.weight = weight;
		this.eLabel = eLabel;
	}
	
}
