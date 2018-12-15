package DiGraph_A5;

import java.util.*;

public class Vertex {
	
	public long idNum;
	public String label;
	public HashMap<String, Edge> ins;
	public HashMap<String, Edge> outs;
	public int cost = Integer.MAX_VALUE;
	public boolean known = false;
	
	
	public Vertex(long idNum, String label) {
		this.idNum = idNum;
		this.label = label;
		this.ins = new HashMap<String, Edge>();
		this.outs = new HashMap<String, Edge>();
	}
	
	public void addIn(String sLabel, Edge edge) { ins.put(sLabel, edge); }
	
	public void addOut(String dLabel, Edge edge) { outs.put(dLabel, edge); }
	
	public void removeOut(String dLabel) { outs.remove(dLabel); }
	
	public void removeIn(String sLabel) { ins.remove(sLabel); }

}
