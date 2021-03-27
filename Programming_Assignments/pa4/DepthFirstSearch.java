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

    // node colors
    static final int WHITE = 0;
    static final int GRAY  = 1;
    static final int BLACK = 2;

    int[] color;  // variable storing the color
    // of each node during DFS
    // (WHITE, GRAY, or BLACK)

    int[] parent; // variable storing the parent
    // of each node in the DFS forest

    int d[];      // variable storing the discovery time
    // of each node in the DFS forest

    int f[];      // variable storing the finish time
    // of each node in the DFS forest

    int t;        // variable storing the current time


    DFSInfo(Graph graph) {
        int n = graph.n;
        color = new int[n];
        parent = new int[n];
        d = new int[n];
        f = new int[n];
        t = 0;
    }
}


// your "main program" should look something like this:

public class DepthFirstSearch {

    static void recDFS(int u, Graph graph, DFSInfo info) {
        // perform a recursive DFS, starting at u
        info.color[u] = info.GRAY;
        info.d[u] = ++info.t;

        Item successor = graph.A[u];
        while(successor != null){
            if(info.color[successor.data] == info.WHITE){
                info.parent[successor.data] = u;
                recDFS(successor.data, graph, info);
            }
            successor = successor.next;
        }

        info.color[u] = info.BLACK;
        info.f[u] = ++ info.t;
    }

    static DFSInfo DFS(Graph graph) {
        DFSInfo info = new DFSInfo(graph);

        for(int i = 0; i < graph.n; i++){
            info.color[i] = info.WHITE;
        }

        for(int i = 0; i < graph.n; i++){
            if(info.color[i] == info.WHITE){
                recDFS(i,graph, info);
            }
        }

        return info;
    }

    static Item findCycle(Graph graph, DFSInfo info) {
       
        for(int u = 0; u < graph.n; u++){

            Item successor = graph.A[u];

            while(successor != null){

                if(info.f[successor.data] > info.f[u]){ //this means exists cycle and back edge from u->v

                    //look at successor
                    int i = u;

                    Item dummy = new Item(i, null);

                    while(i != successor.data){
                        Item item = new Item(info.parent[i], dummy);

                        dummy = item;

                        i = info.parent[i];
                    }

                    return dummy;

                }
                successor = successor.next;
            }

        }

        return null;
    }

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(System.out, "ASCII"), 4096);

        int n = scanner.nextInt();
        int m = scanner.nextInt();

        Graph graph = new Graph(n);

        for(int i = 0; i < m; i++){
            int u = scanner.nextInt() - 1;
            int v = scanner.nextInt() - 1;
            graph.addEdge(u, v);
        }

        DFSInfo info = DFS(graph);

        Item head = findCycle(graph, info);

        Item temp = head;

        if(head == null){
            output.write('0');
        } else {
            output.write('1');
            output.newLine();

            while(temp != null){
                int edge = temp.data + 1;
                output.write(edge + " ");
                temp = temp.next;
            }
        }

        output.close();
    }

}
