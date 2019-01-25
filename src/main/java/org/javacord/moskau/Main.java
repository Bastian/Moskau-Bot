package org.javacord.moskau;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.Messageable;
import org.javacord.api.entity.user.User;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;
import org.javacord.lavaplayerwrapper.YouTubeAudioSource;

public class Main {

    private static Logger logger = LogManager.getLogger(Main.class);

    /**
     * MOSKAU!
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Please provide a valid token as the first argument!");
            return;
        }

        FallbackLoggerConfiguration.setDebug(true);

        DiscordApi api = new DiscordApiBuilder()
                .setToken(args[0])
                .login()
                .join();

        logger.info(api.createBotInvite());

        // This listener is used to leave voice channels when the last user in the channel leaves
        api.addServerVoiceChannelMemberLeaveListener(event -> {
            event.getServer().getAudioConnection().ifPresent(connection -> {
                if (connection.getChannel() == event.getChannel()) {
                    if (event.getChannel().getConnectedUsers().size() <= 1) {
                        connection.close();
                    }
                }
            });
        });

        // Listen to "MOSKAU!" messages
        api.addMessageCreateListener(event -> {
            if (!event.getMessageContent().toLowerCase().contains("moskau")) {
                return;
            }
            if (event.getMessageAuthor().isYourself() || !event.getMessageAuthor().isUser()) {
                return;
            }
            if (!event.isServerMessage()) {
                sendMoskauMessage(event.getChannel());
                return;
            }

            ServerTextChannel channel = event.getServerTextChannel().orElseThrow(AssertionError::new);
            User author = event.getMessageAuthor().asUser().orElseThrow(AssertionError::new);

            ServerVoiceChannel voiceChannel = author.getConnectedVoiceChannel(channel.getServer()).orElse(null);

            // If the user is not in a voice channel or the bot is already playing, we just send a message
            if (voiceChannel == null || channel.getServer().getAudioConnection().isPresent()) {
                sendMoskauMessage(event.getChannel());
                return;
            }

            // Connect to the voice channel and play "Dschinghis Khan - Moskau"
            voiceChannel.connect().thenAccept(connection -> {
                sendMoskauMessage(event.getChannel());
                connection.queue(new YouTubeAudioSource(api, "https://youtu.be/NvS351QKFV4"));
                connection.addAudioSourceFinishedListener(e -> connection.close());
            });
        });
    }

    /**
     * MOSKAU MOSKAU MOSKAU IST EIN SCHÖNES LAND!
     */
    private static void sendMoskauMessage(Messageable receiver) {
        MessageBuilder messageBuilder = new MessageBuilder()
                .setContent("MOSKAU MOSKAU RUSSLAND IST EIN SCHÖNES LAND!");
        if (Math.random() < 0.3141592653589793238462643383279502884197169399375105820974944) {
            messageBuilder.addAttachment(Main.class.getResourceAsStream("/moskau.gif"), "moskau.gif");
        }
        messageBuilder.send(receiver);
    }

}
