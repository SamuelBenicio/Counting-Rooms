import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

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
