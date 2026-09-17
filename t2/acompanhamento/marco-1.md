# Marco 1

**Problema:** *Fox And Names* - dada uma lista de nomes, decidir se existe uma permutação do alfabeto latino que torne a lista ordenada lexicograficamente e, em caso afirmativo, exibir uma dessas permutações.

---

## 1. Entrada, saída e restrições

**Entrada**
- Primeira linha: inteiro `n`, a quantidade de nomes.
- Próximas `n` linhas: um nome por linha, apenas letras minúsculas de `a` a `z`.

**Saída**
- Uma permutação das 26 letras (a primeira letra do alfabeto modificado, depois a segunda, e assim por diante) que faça os nomes ficarem em ordem lexicográfica na ordem em que foram dados.
- Caso nenhuma permutação sirva, a palavra `Impossible`.
- Qualquer permutação válida é aceita (a resposta não é única).

**Restrições**
- `1 ≤ n ≤ 100`
- `1 ≤ |nome_i| ≤ 100`
- Todos os nomes são distintos.
- Alfabeto de tamanho fixo: 26 letras.

**Definição de ordem usada:** compara-se `s` e `t` na primeira posição em que diferem e decide-se pela ordem das letras no alfabeto; se uma string é prefixo da outra, a mais curta é a menor.

**Tamanho do problema:** no máximo 99 comparações entre nomes consecutivos, cada uma percorrendo no máximo 100 caracteres.

---

## 2. Modelagem em grafo

**Vértices:** 26 vértices, um para cada letra do alfabeto (`a` → 0, `b` → 1, …, `z` → 25).

**Arestas:** uma aresta direcionada `u → v` significa "a letra `u` precisa vir antes da letra `v` no alfabeto".

As arestas são extraídas comparando **apenas nomes consecutivos** `nome[i]` e `nome[i+1]` (a transitividade da ordem cuida dos pares não consecutivos):

1. Percorre-se as duas strings em paralelo até a primeira posição `k` em que `nome[i][k] != nome[i+1][k]`.
2. Se essa posição existe, cria-se a aresta `nome[i][k] → nome[i+1][k]` e **para-se a comparação** desse par — as posições seguintes não impõem nenhuma restrição.
3. Se não existe posição diferente, uma string é prefixo da outra:
   - se `|nome[i]| ≤ |nome[i+1]|`, o par já está correto e **nenhuma aresta é gerada**;
   - se `|nome[i]| > |nome[i+1]|` (ex.: `abc` antes de `ab`), a resposta é `Impossible` imediatamente — nenhuma reordenação do alfabeto conserta isso, pois o critério de prefixo não depende do alfabeto.

> Observação importante do grupo: esse caso de prefixo é o único que **não** se traduz em aresta. É uma condição de inviabilidade detectada antes de qualquer busca no grafo.

**Grafo resultante:** um conjunto de restrições de precedência entre letras. Letras que nunca aparecem em nenhuma comparação ficam isoladas (grau 0) e podem ser colocadas em qualquer posição da resposta.

---

## 3. Classificação do grafo

| Característica | Classificação                                                                                                                                                                        |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Direção | **Direcionado** (a precedência tem sentido: `u` antes de `v`)                                                                                                                        |
| Laços | **Sem laços**: só há aresta quando as letras são diferentes                                                                                                                          |
| Arestas paralelas | Podem surgir da coleta (dois pares de nomes gerando a mesma restrição); são tratadas como uma só, usando conjunto ou matriz de adjacência, de modo que o grafo é mantido **simples** |
| Peso | **Não ponderado**                                                                                                                                                                    |
| Conexidade | Pode ser **desconexo** (letras não citadas ficam isoladas)                                                                                                                           |
| Ciclos | Deve ser **acíclico** para haver resposta: a existência de um ciclo (ex.: `a → b` e `b → a`) é exatamente a contradição que leva a `Impossible`                                      |

**Conclusão:** grafo **simples, direcionado e não ponderado**, com 26 vértices e no máximo 25 arestas úteis após remoção de duplicatas; a resposta existe se e somente se ele for um **DAG** (grafo direcionado acíclico) — e o problema se reduz a uma **ordenação topológica** desse DAG.

---

## 4. Resultado de aprendizagem aferido

Modelar um problema cujo enunciado não menciona grafos como um problema de grafos, escolhendo vértices e arestas adequados, e resolvê-lo com travessia (DFS/BFS) — reconhecendo restrições de precedência como um DAG e a resposta como uma ordenação topológica, incluindo a detecção de inviabilidade (ciclo).

---

## 5. Participação de DFS/BFS na solução

A busca é o núcleo da solução: ela produz a ordem das letras e detecta a impossibilidade.

**Opção A - DFS (ordenação topológica por pós-ordem)**
- Executa-se DFS a partir de cada um dos 26 vértices ainda não visitados.
- Cada vértice recebe uma marcação de três estados: não visitado / em processamento (na pilha de recursão) / finalizado.
- Encontrar uma aresta que aponta para um vértice **em processamento** significa ciclo → `Impossible`.
- Ao finalizar um vértice, ele é empilhado. Ao final, esvaziar a pilha dá a ordem topológica, isto é, a permutação pedida.

**Opção B - BFS (algoritmo de Kahn)**
- Calcula-se o grau de entrada de cada letra e coloca-se em uma fila todas as de grau 0.
- Retira-se uma letra da fila, acrescenta-se à resposta e decrementa-se o grau de entrada dos seus vizinhos; os que chegam a 0 entram na fila.
- Se a resposta terminar com menos de 26 letras, restou um ciclo → `Impossible`.

Em ambos os casos, as letras isoladas entram naturalmente na ordem (têm grau de entrada 0 e nenhuma aresta de saída), o que já garante que a saída seja uma permutação completa de `a`–`z`.

---

## 6. Instância pequena

### 6.1 Instância viável (a do enunciado)

```
3
rivest
shamir
adleman
```

**Coleta de arestas:**

| Par comparado | 1ª posição diferente | Aresta |
|---|---|---|
| `rivest` × `shamir` | posição 0: `r` ≠ `s` | `r → s` |
| `shamir` × `adleman` | posição 0: `s` ≠ `a` | `s → a` |

**Grafo:** `r → s → a`, e as outras 23 letras isoladas.

**Restrição resultante:** `r` antes de `s` antes de `a`.

**Ordenação topológica (BFS/Kahn), percorrendo as letras em ordem alfabética ao inserir na fila:** todas as letras têm grau de entrada 0 exceto `s` (vem de `r`) e `a` (vem de `s`). Saindo da fila: `b, c, d, …, q, r`; ao remover `r`, o grau de `s` zera e `s` entra; ao remover `s`, o grau de `a` zera e `a` entra; seguem `t, u, …, z`.

**Saída:** `bcdefghijklmnopqrsatuvwxyz` — igual à do enunciado. Verificação: `r` (pos. 17) < `s` (pos. 18) < `a` (pos. 19). ✔

### 6.2 Instância inviável por ciclo

```
2
ba
ab
```

Arestas: `b → a` (posição 0). Como só há um par, o grafo é `b → a`… mas se acrescentarmos um terceiro nome:

```
3
ba
ab
ba
```
(não vale, pois os nomes são distintos). Um exemplo válido de ciclo com nomes distintos:

```
3
ab
ba
ac
```

| Par | 1ª diferença | Aresta |
|---|---|---|
| `ab` × `ba` | posição 0: `a` ≠ `b` | `a → b` |
| `ba` × `ac` | posição 0: `b` ≠ `a` | `b → a` |

Ciclo `a → b → a`: `a` teria que vir antes e depois de `b`. Saída: **`Impossible`**.

### 6.3 Instância inviável por prefixo

```
2
abc
ab
```

Nenhuma posição difere e `|abc| > |ab|`; pelo critério de prefixo a string mais curta é sempre a menor, independentemente do alfabeto. Nenhuma aresta é gerada e a saída é **`Impossible`** — este caso é detectado na própria coleta, antes da busca.
