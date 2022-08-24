package org.example.cardgame.domain.values;

import co.com.sofka.domain.generic.ValueObject;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Mazo.
 */
public class Mazo implements ValueObject<Mazo.Props> {

    private final Set<Carta> cartas;
    private final Integer cantidad;

    /**
     * Instantiates a new Mazo.
     *
     * @param cartas the catas
     */
    public Mazo(Set<Carta> cartas) {
        this.cartas = cartas;
        this.cantidad = cartas.size();
    }

    @Override
    public Props value() {
        return new Props() {
            @Override
            public Set<Carta> cartas() {
                return cartas;
            }

            @Override
            public Integer cantidad() {
                return cantidad;
            }
        };
    }

    /**
     * Nueva carta mazo.
     *
     * @param carta the carta
     * @return the mazo
     */
    public Mazo nuevaCarta(Carta carta) {
        var catas = new HashSet<>(this.cartas);
        catas.add(carta);
        return new Mazo(catas);
    }

    /**
     * Retirar carta mazo.
     *
     * @param cartaRetirada the carta retirada
     * @return the mazo
     */
    public Mazo retirarCarta(Carta cartaRetirada) {
        var cartaId = cartaRetirada.value().cartaId().value();
        this.cartas.removeIf(
                carta -> cartaId.equals(carta.value().cartaId().value())
        );
        return new Mazo(this.cartas);
    }

    /**
     * The interface Props.
     */
    public interface Props {
        /**
         * Cartas set.
         *
         * @return the set
         */
        Set<Carta> cartas();

        /**
         * Cantidad integer.
         *
         * @return the integer
         */
        Integer cantidad();
    }
}
