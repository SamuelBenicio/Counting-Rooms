/******************************************************************************
 *  DepthFirstSearch — adaptado a partir de DepthFirstSearch.java
 *  (Algorithms, 4th Edition / repositório da disciplina).
 *
 *  Duas alterações em relação à referência:
 *  1) Construtor com `marked[]` EXTERNO, compartilhado entre instâncias,
 *     permitindo reaproveitar o estado de visitados entre buscas diferentes
 *     — necessário para contar componentes conexas (salas).
 *  2) Busca ITERATIVA (pilha explícita, array de int) em vez de recursiva.
 *     A versão original usa recursão (uma chamada de método por vértice
 *     visitado); como uma sala pode ter até ~1.000.000 de células
 *     conectadas (grade até 1000×1000), a recursão estourava a pilha de
 *     chamadas (StackOverflowError, confirmado em teste local) nos casos
 *     de maior componente. A pilha explícita evita esse limite, sem
 *     alterar o conjunto de vértices alcançados nem o resultado da busca
 *     — só a forma de percorrer. O buffer de pilha também é recebido
 *     externamente e reutilizado entre buscas, evitando realocar um array
 *     de tamanho V a cada nova sala.
 ******************************************************************************/
public class DepthFirstSearch {
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
