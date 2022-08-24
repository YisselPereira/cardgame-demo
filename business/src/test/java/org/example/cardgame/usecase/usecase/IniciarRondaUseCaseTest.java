package org.example.cardgame.usecase.usecase;

import co.com.sofka.domain.generic.DomainEvent;
import co.com.sofka.domain.generic.Identity;
import org.example.cardgame.domain.command.IniciarRondaCommand;
import org.example.cardgame.domain.events.JuegoCreado;
import org.example.cardgame.domain.events.JugadorAgregado;
import org.example.cardgame.domain.events.RondaIniciada;
import org.example.cardgame.domain.events.TableroCreado;
import org.example.cardgame.domain.values.Carta;
import org.example.cardgame.domain.values.CartaMaestraId;
import org.example.cardgame.domain.values.JugadorId;
import org.example.cardgame.domain.values.Mazo;
import org.example.cardgame.usecase.gateway.JuegoDomainEventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static reactor.core.publisher.Mono.when;

//TODO: hacer prueba
@ExtendWith(MockitoExtension.class)
class IniciarRondaUseCaseTest {
    @InjectMocks
    private IniciarRondaUseCase useCase;
    @Mock
    private JuegoDomainEventRepository repository;
    @Test
    void IniciarRondaTestSuccessfully(){

        String juegoId = "id";

        var command = new IniciarRondaCommand();
        command.setJuegoId(juegoId);

        when(repository.obtenerEventosPor("id")).thenReturn(eventos());
        useCase = new IniciarRondaUseCase(repository);

        var events = useCase.apply(Mono.just(command));

        StepVerifier.create(events).expectNextMatches(domainEvent -> {
            var event = domainEvent;
            return "id" == event.aggregateRootId();
        }).expectComplete()
                        .verify();
        Mockito.verify(repository).obtenerEventosPor("id");
    }

    private Flux<DomainEvent> eventos(){
        String juegoId = "id";
        JugadorId jugadorPrincipal = JugadorId.of("jugadorId");
        var event = new JuegoCreado(jugadorPrincipal);
        event.setAggregateRootId("id");
        Set<Carta> cartas = new HashSet<>();
        cartas.add(new Carta(CartaMaestraId.of("cartaId"), 100, false, false));
        var eventJugador = new JugadorAgregado(jugadorPrincipal, "chowie", new Mazo(cartas));
        return Flux.just(event, eventJugador);
    }
}