package org.mogwai.wisp.message;

import discord4j.core.object.entity.Message;
import org.mogwai.wisp.nopepe.PepeTester;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {
    public static void parseMessage(Message message) {

        System.out.println("MESSAGE AUTHOR: " + message.getAuthor());
        System.out.println("MESSAGE CONTENT: " + message.getContent());
        final String content = message.getContent();
        final String emojiPattern = "<:[a-zA-Z0-9_]+:\\d+>";
        final String discordEmojiAddress = "https://cdn.discordapp.com/emojis/";

        Pattern pattern = Pattern.compile(emojiPattern);
        Matcher matcher = pattern.matcher(content);
        String extractedId = null;

        if (matcher.find()) {
            extractedId = matcher.group(1);
            String url = discordEmojiAddress + extractedId + ".png";
            System.out.println("FOUND CUSTOM EMOJI ID: " + url);
            if (PepeTester.testForPepe(url)) {
                punishPepe(message);
            }
        }
    }

    static void punishPepe(Message message) {
        message.getChannel().flatMap(channel -> channel.createMessage("Pepe detected, message rejected.")).subscribe();
        message.delete().subscribe();
    }
}
