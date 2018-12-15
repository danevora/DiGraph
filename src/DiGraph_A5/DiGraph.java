package DiGraph_A5;

import java.util.*;
import MinBinHeap_A3.*;

public class DiGraph implements DiGraphInterface {

	// in here go all your data and methods for the graph
	private HashMap<String, Vertex> adj;
	private Set<Long> vertexIds;
	private Set<Long> edgeIds;
	private long numVertices, numEdges;
	

	public DiGraph ( ) { // default constructor
		// explicitly include this
		// we need to have the default constructor
		// if you then write others, this one will still be there
		adj = new HashMap<String, Vertex>();
		vertexIds = new HashSet<Long>();
		edgeIds = new HashSet<Long>();
		numVertices = 0;
		numEdges = 0;
	}
	
	@Override
	public boolean addNode(long idNum, String label) {
		if (adj.containsKey(label) || vertexIds.contains(idNum)) { return false; } //vertex is unique?
		if (idNum < 0) { return false; } //idNum is valid?
		else {
			Vertex vertex = new Vertex(idNum, label);
			adj.put(label, vertex); //adds vertex to graph
			vertexIds.add(idNum); //adds vertex to id list
			numVertices++;
			return true; }
	}
	
	@Override
	public boolean addEdge(long idNum, String sLabel, String dLabel, long weight, String eLabel) {
		if (idNum < 0) { return false; } //idNum is valid?
		else if (edgeIds.contains(idNum)) { return false; } //idNum is unique?
		else if (!adj.containsKey(sLabel) || !adj.containsKey(dLabel)) { return false; } //nodes exist for edge to connect them
		else if (adj.get(sLabel).outs.containsKey(dLabel)) { return false; } //makes sure edge doesn't exist already
		else {
			Edge edge = new Edge(idNum, adj.get(sLabel), adj.get(dLabel), weight, eLabel); //new edge object
			edgeIds.add(idNum); //adds edge to id list
			adj.get(sLabel).addOut(dLabel, edge); //adds edge to start's outgoing edges
			adj.get(dLabel).addIn(sLabel, edge); //adds edge to destination's incoming edges
			numEdges++;
			return true; }
	}
	
	@Override
	public boolean delNode(String label) {
		if (!adj.containsKey(label)) { return false; } //makes sure graph contains vertex
		else { //removing edge
			Set<String> outiesSet = adj.get(label).outs.keySet();
			for (String key: outiesSet) { //looking through node's out edges
				Edge outieEdge = adj.get(label).outs.get(key);
				edgeIds.remove(outieEdge.idNum); //removes all node's out edges from id list
				outieEdge.dLabel.removeIn(label); //removes edges from destination node
				numEdges--; }
			Set<String> inniesSet = adj.get(label).ins.keySet();
			for (String key: inniesSet) { //looking through node's in edges
				Edge innieEdge = adj.get(label).ins.get(key);
				edgeIds.remove(innieEdge.idNum); //removes all node's in edges from id list
				innieEdge.sLabel.removeOut(label); //removes edges from source node
				numEdges--; }
			long vertexId = adj.get(label).idNum;
			vertexIds.remove(vertexId); //remove vertex from vertex list
			adj.remove(label); //remove vertex from graph
			numVertices--;
			return true; }
	}
	
	@Override
	public boolean delEdge(String sLabel, String dLabel) {
		if (!adj.containsKey(sLabel) || !adj.containsKey(dLabel)) { return false; } //makes sure nodes are valid
		else if (!adj.get(sLabel).outs.containsKey(dLabel)) { return false; } //makes sure edge exists between valid nodes
		else {
			long edgeId = adj.get(sLabel).outs.get(dLabel).idNum;
			edgeIds.remove(edgeId); //remove edge from id list
			Vertex start = adj.get(sLabel);
			Vertex destination = adj.get(dLabel);
			start.removeOut(dLabel); //remove out edge from start list
			destination.removeIn(sLabel); //remove in edge from destination list
			numEdges--; 
			return true; }
	}
	
	@Override
	public long numNodes() { return numVertices; }
	
	@Override
	public long numEdges() { return numEdges; }
	
	// test OG A5 oracle after finishing these methods
	// rest of your code to implement the various operations
	
	@Override
	public ShortestPathInfo[] shortestPath(String label) {
		ShortestPathInfo[] paths = new ShortestPathInfo[(int) numVertices]; //sets the array size to the number of vertices
		MinBinHeap pQ = new MinBinHeap(); 
		adj.get(label).cost = 0; //declares the path from start to itself is 0
		pQ.insert(new EntryPair(label, adj.get(label).cost)); //starts the priority queue up
		while(pQ.size() > 0) { //will run until all nodes possible have been visited
			Vertex start = adj.get(pQ.getMin().value); //makes start the best priority node that isn't known yet
			pQ.delMin(); //takes this node out of the queue since we will visit all of its children
			if(start.known) {continue;} else {start.known = true;} //makes sure node isn't already known for efficiency 
			for (String key: start.outs.keySet()) { //finds the cost of going to all the out nodes of current nodes
				int oldCost = adj.get(key).cost == Integer.MAX_VALUE ? Integer.MAX_VALUE : start.outs.get(key).dLabel.cost; //old cost is either arbitrarily large or a previously found value
				int newCost = start.cost + (int) start.outs.get(key).weight; //new cost of out node is the cost coming from current node
				if (oldCost > newCost) { //set cost to newCost if it's easier to come from current node
					start.outs.get(key).dLabel.cost = newCost;
					pQ.insert(new EntryPair(start.outs.get(key).dLabel.label, start.outs.get(key).dLabel.cost)); } } } //insert into queue if this way is faster
		int count = 0;
		for(String key: adj.keySet()) { //inserting vertex labels with cost into paths array
			if(adj.get(key).cost == Integer.MAX_VALUE) { paths[count] = new ShortestPathInfo(key, -1); } //if node can't be reached, cost is -1
			else { paths[count] = new ShortestPathInfo(key, adj.get(key).cost); } //insert nodes that can be reached into array
			count++; }
		return paths;
	}
	
}
