# Marco 3 - Aplicação Básica de DFS

## 1. Mapeamento da Instância e Estrutura de Adjacência

### Instância de teste (4 x 4)

```text
Linha 0: # # # #
Linha 1: # # . #
Linha 2: . # . .
Linha 3: # # # .
```

As células transitáveis (`.`) recebem uma indexação linear exclusiva de 0 a
V - 1, para compatibilidade direta com a classe `Graph` da biblioteca `algs4`.

| Índice | Coordenada |
| --- | --- |
| 0 | (1, 2) |
| 1 | (2, 0) |
| 2 | (2, 2) |
| 3 | (2, 3) |
| 4 | (3, 3) |

O grafo possui V = 5 vértices e E = 3 arestas. Suas listas de adjacência são:

```text
adj[0]: [2]
adj[1]: []
adj[2]: [0, 3]
adj[3]: [2, 4]
adj[4]: [3]
```

## 2. Execução Manual Passo a Passo

O laço de busca itera pelos vértices na ordem
v = 0, 1, 2, 3, 4, aplicando a rotina recursiva clássica de exploração de
Trémaux da classe `DepthFirstPaths`.

### Vértice 0

Como `marked[0] == false`, é identificado o **cômodo 1**. A chamada `dfs(G, 0)`:

1. Define `marked[0] = true`.
2. Itera em `adj[0]` e encontra o vizinho `2`, que ainda não foi marcado.
3. Define `edgeTo[2] = 0` e chama `dfs(G, 2)`.
4. Define `marked[2] = true`.
5. Em `adj[2]`, encontra o vizinho `0` já marcado e o vizinho `3`, ainda não marcado.
6. Define `edgeTo[3] = 2` e chama `dfs(G, 3)`.
7. Define `marked[3] = true`.
8. Em `adj[3]`, encontra o vizinho `2` já marcado e o vizinho `4`, ainda não marcado.
9. Define `edgeTo[4] = 3` e chama `dfs(G, 4)`.
10. Define `marked[4] = true`. O vizinho `3` já está marcado.
11. Finaliza as chamadas de `4`, `3`, `2` e `0`, nessa ordem.

### Vértice 1

Como `marked[1] == false`, é identificado o **cômodo 2**. A chamada `dfs(G, 1)`:

1. Define `marked[1] = true`.
2. Como `adj[1]` está vazia, finaliza imediatamente.

Os vértices `2`, `3` e `4`, que já estão marcados, são ignorados pelo laço
principal.

## 3. Estados de Visita, Predecessores e Tempos de Execução

Os estados de visita são mapeados pelo vetor booleano `marked[]`:

- `false`: não visitado;
- `true`: visitado.

O vetor `edgeTo[]` registra o vértice que conduziu à descoberta de cada nó.
Os tempos de descoberta e término (`d/f`) representam, respectivamente, a
entrada e a saída de cada vértice na pilha de recursão.

| Vértice | Coordenada | marked[u] | Predecessor edgeTo[u] | Descoberta d[u] | Término f[u] | Intervalo |
| --- | --- | --- | --- | --- | --- | --- |
| 0 | (1, 2) | `true` | NIL (raiz) | 1 | 8 | [1, 8] |
| 2 | (2, 2) | `true` | 0 | 2 | 7 | [2, 7] |
| 3 | (2, 3) | `true` | 2 | 3 | 6 | [3, 6] |
| 4 | (3, 3) | `true` | 3 | 4 | 5 | [4, 5] |
| 1 | (2, 0) | `true` | NIL (raiz) | 9 | 10 | [9, 10] |

## 4. Árvore de Busca (Floresta DFS)

A execução gera uma floresta de profundidade composta por duas árvores
disjuntas.

### Árvore 1 (raiz: 0)

```text
0 --edgeTo[2]=0--> 2 --edgeTo[3]=2--> 3 --edgeTo[4]=3--> 4
```

### Árvore 2 (raiz: 1)

```text
1 (vértice isolado; edgeTo[1] = NIL)
```

## 5. Alcançabilidade

A DFS garante que todos os vértices conectados por caminhos de chão transitável
pertencem ao mesmo conjunto conexo maximal. O vértice `1` não possui caminho
para os demais, particionando o grafo nas seguintes classes de equivalência:

As classes de equivalência são:

- C1 = {0, 2, 3, 4}
- C2 = {1}

## 6. Aplicabilidade ao Problema e Adaptações

A contagem de chamadas externas de `dfs(G, v)` no laço principal totaliza
exatamente a quantidade de cômodos disjuntos no mapa:

O resultado da contagem é: `count = 2`.

O algoritmo adota a estrutura `DepthFirstPaths`, com `boolean[] marked` e
`int[] edgeTo`, conforme os slides. Assim, dispensa a alocação de matrizes
densas e garante tempo de execução linear O(V + E), considerando a
representação do grafo por listas de adjacência.