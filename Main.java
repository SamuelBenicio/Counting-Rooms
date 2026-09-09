/******************************************************************************
 *  CSES 1192 — Counting Rooms
 *
 *  Arquivo único para submissão no CSES (o juiz aceita apenas um arquivo
 *  por envio, com o nome batendo com a classe pública). Por isso, as classes
 *  Graph, Bag e DepthFirstSearch — normalmente em arquivos separados no
 *  repositório (src/) — foram reunidas aqui como classes não-públicas.
 *
 *  Em relação às implementações de referência (algs4 / repositório da
 *  disciplina), foram removidos apenas os métodos que dependem de classes
 *  externas não utilizadas pela solução (In, Stack, StdIn, StdOut):
 *    - Graph(In in), Graph(Graph graph) [cópia], toDot(), main() de teste;
 *    - main() de teste unitário do Bag.
 *  O restante de cada classe permanece igual ao original.
 *
 *  A adaptação funcional está em DepthFirstSearch: foi adicionado um
 *  construtor que recebe um vetor `marked[]` EXTERNO, compartilhado entre
 *  buscas, permitindo contar múltiplas componentes conexas (salas) a
 *  partir da varredura feita em Main.
 ******************************************************************************/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // leitura da entrada
        String[] primeira = br.readLine().split(" ");
        int n = Integer.parseInt(primeira[0]);
        int m = Integer.parseInt(primeira[1]);

        char[][] grid = new char[n][m];
        for (int i = 0; i < n; i++) {
            String linha = br.readLine();
            for (int j = 0; j < m; j++) {
                grid[i][j] = linha.charAt(j);
            }
        }

        // construção do grafo
        Graph g = new Graph(n * m);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == '#') continue; // parede não gera aresta

                int id = i * m + j;

                // vizinho da direita
                if (j + 1 < m && grid[i][j + 1] == '.') {
                    int idDireita = i * m + (j + 1);
                    g.addEdge(id, idDireita);
                }

                // vizinho de baixo
                if (i + 1 < n && grid[i + 1][j] == '.') {
                    int idBaixo = (i + 1) * m + j;
                    g.addEdge(id, idBaixo);
                }
            }
        }

        // contagem de salas via DFS (varredura externa + DFS adaptado)
        boolean[] visitado = new boolean[n * m];
        int[] pilhaBuffer = new int[n * m]; // reutilizado entre as buscas
        int salas = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == '.') {
                    int id = i * m + j;
                    if (!visitado[id]) {
                        salas++;
                        new DepthFirstSearch(g, id, visitado, pilhaBuffer); // marca toda a sala em `visitado`
                    }
                }
            }
        }

        System.out.println(salas);
    }
}


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
 ******************************************************************************/
class Graph {
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


/******************************************************************************
 *  Bag — bolsa genérica (multiset), implementada com lista ligada.
 *  Cópia de Bag.java (algs4), sem alteração além da remoção do main() de
 *  teste unitário (que dependia de StdIn/StdOut, não usados nesta solução).
 ******************************************************************************/
/******************************************************************************
 *  IntBag — bolsa especializada para int primitivo, adaptada a partir de
 *  Bag.java (algs4). Guarda os elementos num array redimensionável (int[])
 *  em vez de lista ligada de Integer, evitando autoboxing e uma alocação de
 *  objeto por elemento — essencial para grades grandes (até ~2.000.000 de
 *  arestas no pior caso do Counting Rooms).
 ******************************************************************************/
class IntBag implements Iterable<Integer> {
    private int[] items;
    private int n;

    public IntBag() {
        items = new int[4];
        n = 0;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void add(int item) {
        if (n == items.length) {
            int[] novo = new int[items.length * 2];
            System.arraycopy(items, 0, novo, 0, n);
            items = novo;
        }
        items[n++] = item;
    }

    public int get(int i) {
        return items[i];
    }

    public Iterator<Integer> iterator() {
        return new IntBagIterator();
    }

    private class IntBagIterator implements Iterator<Integer> {
        private int pos = 0;

        public boolean hasNext() {
            return pos < n;
        }

        public Integer next() {
            if (!hasNext()) throw new NoSuchElementException();
            return items[pos++];
        }
    }
}


/******************************************************************************
 *  DepthFirstSearch — adaptado a partir de DepthFirstSearch.java
 *  (Algorithms, 4th Edition / repositório da disciplina).
 *
 *  Duas alterações em relação à referência:
 *  1) Construtor com `marked[]` EXTERNO, compartilhado entre instâncias,
 *     permitindo reaproveitar o estado de visitados entre buscas diferentes
 *     — necessário para contar componentes conexas (salas).
 *  2) Busca ITERATIVA (pilha explícita, java.util.ArrayDeque) em vez de
 *     recursiva. A versão original usa recursão (uma chamada de método por
 *     vértice visitado); como uma sala pode ter até ~1.000.000 de células
 *     conectadas (grade até 1000×1000), a recursão estourava a pilha de
 *     chamadas (StackOverflowError) nos casos de maior componente. A pilha
 *     explícita evita esse limite, sem alterar o conjunto de vértices
 *     alcançados nem o resultado da busca — só a forma de percorrer.
 ******************************************************************************/
class DepthFirstSearch {
    private boolean[] marked;
    private int[] pilha;   // buffer de pilha reutilizável, compartilhado entre buscas
    private int count;

    // construtor original: cria vetores novos, isolados (uso avulso/didático)
    public DepthFirstSearch(Graph G, int s) {
        this(G, s, new boolean[G.V()], new int[G.V()]);
    }

    // construtor adaptado: recebe marked[] e um buffer de pilha externos,
    // ambos reutilizados entre chamadas (evita realocar um array de
    // tamanho V a cada nova busca, o que seria custoso quando há muitas
    // salas pequenas na mesma grade)
    public DepthFirstSearch(Graph G, int s, boolean[] marked, int[] pilhaBuffer) {
        this.marked = marked;
        this.pilha = pilhaBuffer;
        validateVertex(G, s);
        dfs(G, s);
    }

    // busca em profundidade iterativa, com pilha explícita (array de int, sem boxing)
    private void dfs(Graph G, int s) {
        int topo = 0;
        pilha[topo++] = s;
        marked[s] = true;
        count++;

        while (topo > 0) {
            int v = pilha[--topo];
            IntBag vizinhos = G.adj(v);
            int grau = vizinhos.size();
            for (int k = 0; k < grau; k++) {
                int w = vizinhos.get(k);
                if (!marked[w]) {
                    marked[w] = true;
                    count++;
                    pilha[topo++] = w;
                }
            }
        }
    }

    public boolean marked(int v) {
        return marked[v];
    }

    public int count() {
        return count;
    }

    private void validateVertex(Graph G, int v) {
        int V = G.V();
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }
}
