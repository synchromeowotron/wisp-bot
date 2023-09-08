package org.mogwai.wisp.message;

import discord4j.core.object.entity.Message;
import org.mogwai.wisp.models.PepeResult;
import org.mogwai.wisp.nopepe.PepeTester;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageParser {
    public static void parseMessage(Message message) {

        System.out.println("MESSAGE AUTHOR: " + message.getAuthor().get().getUsername());
        System.out.println("MESSAGE CONTENT: " + message.getContent());

        final String content = message.getContent();
        final String emojiPattern = "<(a:|:)[a-zA-Z0-9_]+:(\\d+)>";
        final String discordEmojiAddress = "https://cdn.discordapp.com/emojis/";

        Pattern pattern = Pattern.compile(emojiPattern);
        Matcher matcher = pattern.matcher(content);
        String extractedId;

        if (matcher.find()) {
            String emojiPrefix = matcher.group(1);
            String emojiId = matcher.group(2);

            String url;
            if ("a:".equals(emojiPrefix)) {
                url = discordEmojiAddress + emojiId + ".gif";
            } else {
                url = discordEmojiAddress + emojiId + ".png";
            }
            System.out.println("FOUND CUSTOM EMOJI ID: " + url);
            PepeResult result = PepeTester.testForPepe(url);
            if (result.isPepe()) {
                punishPepe(message, result.getEvaluation());
            }
        }
    }

    static void punishPepe(Message message, String evaluation) {
        message.getChannel().flatMap(channel ->
        channel.createMessage("Pepe detected, message rejected. (AI evaluation score: " + evaluation +").")).subscribe();
        message.delete().subscribe();
    }
}
