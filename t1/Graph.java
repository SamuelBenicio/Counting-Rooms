/******************************************************************************
 *  Graph — representa um grafo não-direcionado, lista de adjacência.
 *  Adaptado de Graph.java (repositório da disciplina / algs4).
 *
 *  Adaptação de desempenho: a lista de adjacência usa IntBag (bag
 *  especializado para int primitivo) em vez de Bag<Integer>. Com grades de
 *  até 1000×1000 (até ~1.000.000 de vértices e ~2.000.000 de arestas), usar
 *  Bag<Integer> força autoboxing — cada int vira um objeto Integer — o que
 *  gera milhões de alocações pequenas e pressão desnecessária sobre o
 *  coletor de lixo, o suficiente para estourar o tempo limite (1.00s) do
 *  CSES. IntBag preserva a mesma estrutura e API (add, iterator, size),
 *  só armazenando int diretamente.
 *
 *  Também foram removidos, por dependerem de classes externas não usadas
 *  nesta solução (In, Stack): o construtor Graph(In in), o construtor de
 *  cópia Graph(Graph graph), toDot() e o main() de teste unitário.
 ******************************************************************************/
public class Graph {
    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;
    private int E;
    private IntBag[] adj;

    public Graph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");
        this.V = V;
        this.E = 0;
        adj = new IntBag[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new IntBag();
        }
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        E++;
        adj[v].add(w);
        adj[w].add(v);
    }

    public IntBag adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    public int degree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " vertices, " + E + " edges " + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (int w : adj[v]) {
                s.append(w + " ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
}
