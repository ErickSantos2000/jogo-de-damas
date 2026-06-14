# Documentação da Arquitetura do Sistema de Peças

Esta documentação detalha a refatoração polimórfica das peças do jogo de damas, utilizando os padrões **Template Method** e **Strategy**.

---

## 1. Interface Peca
Define o contrato básico para todas as peças do jogo, garantindo que o `Jogo` interaja com elas de forma genérica.

```java
package com.damas.objetos;

public interface Peca {
    void mover(Casa destino);
    boolean isMovimentoValido(Casa destino);
    Cor getCor();
    boolean podeMover(Cor vezAtual);
    TipoPeca getTipo();
    boolean isCapturaObrigatoria(int distancia);
}
```

---

## 2. Classe Abstrata PecaBase (Template Method)
Centraliza a lógica comum a todas as peças (como movimentação diagonal básica e armazenamento de estado), evitando a repetição de código (Princípio DRY).

```java
package com.damas.objetos;

public abstract class PecaBase implements Peca {
    protected Casa casa;
    protected Cor cor;
    protected TipoPeca tipoPeca;

    public PecaBase(Casa casa, Cor cor, TipoPeca tipoPeca) {
        this.casa = casa;
        this.cor = cor;
        this.tipoPeca = tipoPeca;
        casa.colocarPeca(this);
    }

    @Override
    public void mover(Casa destino) {
        casa.removerPeca();
        destino.colocarPeca(this);
        casa = destino;
    }

    // Método Abstrato: Detalhe específico que cada filha deve preencher
    public abstract boolean validarRegrasDeDeslocamento(int sentidoY, int distancia);

    // Template Method: Define o esqueleto da validação diagonal
    @Override
    public boolean isMovimentoValido(Casa destino) {
        int distanciaX = Math.abs((destino.getX() - casa.getX()));
        int distanciaY = Math.abs((destino.getY() - casa.getY()));
        int diffY = (destino.getY() - casa.getY());

        // Validação Geométrica: Garante que o movimento seja diagonal e não estático
        if (distanciaX != distanciaY || distanciaX == 0) return false;

        // Normalização do sentido (1 para subir, -1 para descer)
        int sentidoY = diffY / distanciaY;

        // Delegação para o comportamento específico da subclasse
        return validarRegrasDeDeslocamento(sentidoY, distanciaX);
    }

    @Override
    public Cor getCor() { return cor; }

    @Override
    public TipoPeca getTipo() { return tipoPeca; }

    @Override
    public boolean podeMover(Cor vezAtual) {
        return vezAtual == this.cor;
    }
    
    @Override
    public abstract boolean isCapturaObrigatoria(int distancia);
}
```

---

## 3. Subclasses Concretas (Polimorfismo Comportamental)

### PedraBranca
```java
package com.damas.objetos;

public class PedraBranca extends PecaBase {
    public PedraBranca(Casa casa) {
        super(casa, Cor.BRANCA, TipoPeca.PEDRA);
    }

    @Override
    public boolean validarRegrasDeDeslocamento(int sentidoY, int distancia) {
        if (distancia == 2) return true; // Captura permitida
        return (distancia == 1 && sentidoY == 1); // Movimento simples apenas para frente
    }

    @Override
    public boolean isCapturaObrigatoria(int distancia) {
        return (distancia == 2);
    }
}
```

### PedraVermelha
```java
package com.damas.objetos;

public class PedraVermelha extends PecaBase {
    public PedraVermelha(Casa casa) {
        super(casa, Cor.VERMELHA, TipoPeca.PEDRA);
    }

    @Override
    public boolean validarRegrasDeDeslocamento(int sentidoY, int distancia) {
        if (distancia == 2) return true;
        return (distancia == 1 && sentidoY == -1); // Movimento simples apenas para baixo
    }

    @Override
    public boolean isCapturaObrigatoria(int distancia) {
        return (distancia == 2);
    }
}
```

### DamaBranca
```java
package com.damas.objetos;

public class DamaBranca extends PecaBase {
    public DamaBranca(Casa casa) {
        super(casa, Cor.BRANCA, TipoPeca.DAMA);
    }

    @Override
    public boolean validarRegrasDeDeslocamento(int sentidoY, int distancia) {
        return true; // Damas movem em qualquer sentido e distância diagonal
    }

    @Override
    public boolean isCapturaObrigatoria(int distancia) {
        return false;
    }
}
```

### DamaVermelha
```java
package com.damas.objetos;

public class DamaVermelha extends PecaBase {
    public DamaVermelha(Casa casa) {
        super(casa, Cor.VERMELHA, TipoPeca.DAMA);
    }

    @Override
    public boolean validarRegrasDeDeslocamento(int sentidoY, int distancia) {
        return true;
    }

    @Override
    public boolean isCapturaObrigatoria(int distancia) {
        return false;
    }
}
```

---

## 4. Enums Auxiliares

### Cor
```java
package com.damas.objetos;

public enum Cor {
    BRANCA, VERMELHA
}
```

### TipoPeca
```java
package com.damas.objetos;

public enum TipoPeca {
    PEDRA, DAMA
}
```
