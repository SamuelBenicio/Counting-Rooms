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

**Pendente:**

## Adaptação, integração, testes, complexidade

**Pendente:**
