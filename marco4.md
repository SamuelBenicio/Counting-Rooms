# Marco 4 - Aplicação básica de BFS e conclusão

---

## Instância utilizada

```
Linha 0: # # # #
Linha 1: # # . #
Linha 2: . # . .
Linha 3: # # # .
```

## Execução manual (BFS)

> A ser realizada em sala.

## Níveis, distâncias, predecessores

> A ser preenchido junto com a execução manual, em sala.

## Comparação entre DFS e BFS

Para este problema, DFS e BFS visitam o mesmo conjunto de vértices em cada execução, o que muda é a ordem de visitação (pilha/recursão na DFS, fila na BFS) e a informação extra que cada um naturalmente calcula:

- **BFS** calcula `dist` (nível/distância mínima em número de arestas a partir da raiz), útil quando o problema pede caminho mínimo.
- **DFS** calcula tempos de descoberta/término, mais úteis para detectar ciclos ou ordenação topológica.

Nenhuma dessas informações extras é necessária para _contar_ salas. o resultado (número de componentes conexos) depende apenas de quais vértices foram alcançados, não da ordem ou da distância percorrida.

## Escolha justificada

Vamos utilizar DFS (Depth-First Search), pois o objetivo do algoritmo é verificar a alcançabilidade dos vértices a partir de uma raiz e, consequentemente, identificar os componentes conexos do grafo.

Para cada sala ainda não visitada, iniciamos uma busca em profundidade. A DFS percorre todas as salas que podem ser alcançadas a partir daquela sala inicial. Quando a busca termina, todas essas salas pertencem ao mesmo componente conexo.

Assim, ao percorrer o grafo e iniciar uma nova DFS sempre que encontrarmos um vértice ainda não visitado, podemos contar quantos componentes conexos existem, o que corresponde ao número de grupos de salas conectadas.

## Complexidade


A complexidade da BFS é O(V + E), onde:

V é o número de vértices do grafo;
E é o número de arestas.
Isso ocorre porque cada vértice é visitado no máximo uma vez e cada aresta é analisada no máximo uma vez durante a busca.

