import java.io.*;
import java.util.*;

// Data structure for a node in a linked list
class Item {
    int data;
    Item next;

    Item(int data, Item next) {
        this.data = data;
        this.next = next;
    }
}

// Data structure for representing a graph
class Graph {
    int n;  // # of nodes in the graph

    Item[] A;
    // For u in [0..n), A[u] is the adjacency list for u

    Graph(int n) {
        // initialize a graph with n vertices and no edges
        this.n = n;
        A = new Item[n];
    }

    void addEdge(int u, int v) {
        // add an edge u -> v to the graph

        A[u] = new Item(v, A[u]);
    }
}

// Data structure holding data computed by DFS
class DFSInfo {
    int k;
    // # of trees in DFS forest

    int[] T;
    // For u in [0..n), T[u] is initially 0, but when DFS discovers
    // u, T[u] is set to the index (which is in [1..k]) of the tree
    // in DFS forest in which u belongs.

    int[] L;
    // List of nodes in order of decreasing finishing time

    int count;
    // initially set to n, and is decremented every time
    // DFS finishes with a node and is recorded in L

    DFSInfo(Graph graph) {
        int n = graph.n;
        k = 1;
        T = new int[n];
        L = new int[n];
        count = n;
    }
}


// your "main program" should look something like this:

public class StronglyConnectedComponents {

    static void recDFS(int u, Graph graph, DFSInfo info) { //works
        // perform a recursive DFS, starting at u

        if(info.T[u] == 0){
            info.T[u] = info.k; //we discover a node, it is part of the tree that k currently is
        }

        Item successor = graph.A[u];

        while(successor != null){
            if(info.T[successor.data] == 0){ //if successor is undiscovered

                recDFS(successor.data, graph, info); //keep discovering its successors
            }
            successor = successor.next;
        }

        info.L[info.count - 1] = u; //done processing node, append to end of L

        if(info.count > 0) info.count--; //1 less node to process
    }

    static DFSInfo DFS(int[] order, Graph graph) { //works
        // performs a "full" DFS on given graph, processing
        // nodes in the order specified (i.e., order[0], order[1], ...)
        // in the main loop.

        DFSInfo info = new DFSInfo(graph);

        for(int i = 0; i < order.length; i++){
            int node = order[i];
            if(info.T[node] == 0){
                recDFS(node, graph, info);
                info.k++;
            }
        }

        return info;
    }


    static boolean[] computeSafeNodes(Graph graph, DFSInfo info) {
        // returns a boolean array indicating which nodes
        // are safe nodes.  The DFSInfo is that computed from the
        // second DFS.

        int[] components = info.T;

        boolean[] safe = new boolean[info.k];

        for(int i = 0; i < safe.length; i++){ //don't include dummy head node for index 0
            safe[i] = true;
        }

        for(int i = 0; i < graph.n; i++){
            int node = i;
            int scc = components[i];
            Item successor = graph.A[node];
            while(successor != null){
                if(components[successor.data] != scc){
                    safe[scc] = false;
                }
                successor = successor.next;
            }

        }

        return safe;
    }

    static Graph reverse(Graph graph) {
        Graph t = new Graph(graph.n);

        for(int u = 0; u < graph.n; u++){
            Item edge = graph.A[u];

            while(edge != null){
                t.addEdge(edge.data, u);
                edge = edge.next;
            }
        }

        return t;
    }

    public static void main(String[] args) throws IOException{

        Scanner scanner = new Scanner(System.in);
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out, "ASCII"), 4096);

        int n = scanner.nextInt();
        int m = scanner.nextInt();

        Graph graph = new Graph(n);

        for(int i = 0; i < m; i++){
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            graph.addEdge(u, v);
        }

        Graph t = reverse(graph); //compute G^T

        int[] order = new int[graph.n]; //dummy order array for regular DFS

        for(int i = 0; i < order.length; i++){
            order[i] = i;
        }

        DFSInfo infoT = DFS(order, t); //run DFS on G^T

        DFSInfo info = DFS(infoT.L, graph); //run DFS, processing in order of decreasing finish time on G

        boolean[] safe = computeSafeNodes(graph, info);

        ArrayList<Integer> a = new ArrayList<>();

        for(int i = 1; i < safe.length; i++){
            if(safe[i]){
                for(int j = 0; j < info.T.length; j++){
                    if(info.T[j] == i){
                        a.add(j);
                    }
                }
            }
        }

        Collections.sort(a);

        for(int i = 0; i < a.size(); i++){
            output.write(a.get(i) + " ");
        }

        output.close();

    }
}
