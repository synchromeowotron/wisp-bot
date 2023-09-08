package org.mogwai.wisp;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.mogwai.wisp.message.MessageParser;
import org.mogwai.wisp.security.TokenSecurity;
import reactor.core.publisher.Mono;

public class Main {
    public static void main(String[] args) {
        DiscordClient client = DiscordClient.create(TokenSecurity.getBotToken());
        Mono<Void> login = client.withGateway((GatewayDiscordClient gateway) -> {
            Mono<Void> printOnLogin = gateway.on(ReadyEvent.class, event ->
                Mono.fromRunnable(() -> {
                    final User self = event.getSelf();
                    System.out.println("\n - - - - - READY FOR WORK - - - - -");
                    System.out.println(" - - - Logged in as " + self.getUsername() + self.getDiscriminator() + " - - -");
                    System.out.println("  - - - - - - - - - - - - - - - - - \n");
                })).then();

            Mono<Void> handlePingCommand = gateway.on(MessageCreateEvent.class, event -> {
                Message message = event.getMessage();
                MessageParser.parseMessage(message);
                return Mono.empty();
            }).then();

            return printOnLogin.and(handlePingCommand);
        });
        login.block();
    }
}
