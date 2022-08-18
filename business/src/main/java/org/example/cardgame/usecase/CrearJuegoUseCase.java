package org.example.cardgame.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import org.example.cardgame.domain.*;
import org.example.cardgame.usecase.gateways.CartaMaestra;
import org.example.cardgame.usecase.gateways.ConsultarCartaMaestraService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Random;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class CrearJuegoUseCase implements Function<Mono<CrearJuegoCommand>, Flux<DomainEvent>> {

    private final ConsultarCartaMaestraService service;

    public CrearJuegoUseCase(ConsultarCartaMaestraService service){
        this.service = service;
    }

    @Override
    public Flux<DomainEvent> apply(Mono<CrearJuegoCommand> crearJuegoCommand) {
        return crearJuegoCommand.flatMapMany(command -> {
            return service.cosultarCartasPepsico().collectList().flatMapMany(cartas -> {
                var factory = new JugadoresFactory();
                command.getJugadores()
                        .forEach((id, alias) -> {
                            factory.agregarJugador(JugadorId.of(id), alias, generarMazo(cartas));
                        });
                var juego = new Juego(JuegoId.of(command.getJuegoId()), factory);

                return Flux.fromIterable(juego.getUncommittedChanges());
            });
        });
    }

    public Integer getRandomIntegerBetweenRango(Integer min, Integer max){
        Integer x = (int)(Math.random()*((max-min)+1))+min;
        return x;
    }
    private Mazo generarMazo(List<CartaMaestra> cartas) {
        //TODO: sacar 5 0 6 cartas para el jugador de manera aleatorio
        //TODO: ver como pasar este codigo a funcional
        Set<Carta> listaCartas = new HashSet<>();
        Integer tamanioLista = cartas.size();
        Integer numero = this.getRandomIntegerBetweenRango(0,tamanioLista);
        CartaMaestra primerCarta = cartas.get(numero);
        listaCartas.add(new Carta(primerCarta.getPoder(), primerCarta.getId(), true));
        Integer numero2 = this.getRandomIntegerBetweenRango(0, tamanioLista);
        CartaMaestra segundaCarta = cartas.get(numero2);
        listaCartas.add(new Carta(segundaCarta.getPoder(), segundaCarta.getId(), true));
        Integer numero3 = this.getRandomIntegerBetweenRango(0, tamanioLista);
        CartaMaestra terceraCarta = cartas.get(numero3);
        listaCartas.add(new Carta(terceraCarta.getPoder(), terceraCarta.getId(), true));
        Integer numero4 = this.getRandomIntegerBetweenRango(0, tamanioLista);
        CartaMaestra cuartaCarta = cartas.get(numero4);
        listaCartas.add(new Carta(cuartaCarta.getPoder(), cuartaCarta.getId(), true));
        Integer numero5 = this.getRandomIntegerBetweenRango(0, tamanioLista);
        CartaMaestra quintaCarta = cartas.get(numero5);
        listaCartas.add(new Carta(quintaCarta.getPoder(), quintaCarta.getId(), true));
        return new Mazo(listaCartas);
    }
}
