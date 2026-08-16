# CSES - Counting Rooms

## Participantes:
- Samuel Ribeiro Benicio
- Adriel Medeiro Lins
- Manoel Sergio Costa Lima Filho

# Marco 1 — Modelagem

## Enunciado
Dado um mapa em grade bidimensional de dimensões n × m composto por paredes (#) e chão (.), determine a quantidade total de cômodos distintos no edifício. Movimentos válidos ocorrem apenas horizontalmente e verticalmente (4 direções) (não pode andar na diagonal) sobre células transitáveis (.)

## Entrada
- Dois inteiros n (altura) e m (largura) do mapa
- Depois, n linhas de m caracteres. Cada caractere é um `.` (chão) ou `#` (parede)

## Saída
Apenas um número inteiro contendo o número de cômodos

## Restrições
- n não pode ser menor que 1 e nem maior que 1000
- m não pode ser menor que 1 e nem maior que 1000

## Vértices
Cada `.` é um vértice.

## Arestas
São arestas não direcionadas, sem peso e existem entre duas células transitáveis que sejam vizinhas horizontal ou verticalmente

## Tipo do grafo
Grafo não direcionado e não ponderado: cada célula vazia é um vértice e as adjacências entre células vizinhas formam as arestas

## Instância pequena e resultado esperado
```text
3 4
####
##.##
.#..#
###.
```

Resultado esperado:
```text
3
```

## Hipótese inicial de solução
A ideia é transformar o mapa em um grafo em que cada célula vazia (`.`) representa um vértice e cada par de células vazias adjacentes (horizontal ou vertical) representa uma aresta.
