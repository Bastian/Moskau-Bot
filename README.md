# Moskau Bot

An simple bot that uses the [Javacord](https://github.com/Javacord/Javacord) library and
[Lavaplayer-Wrapper](https://github.com/Bastian/Lavaplayer-Wrapper) to play Dschinghis Khan's Moskau:

[![Dschinghis Khan - Moskau](http://img.youtube.com/vi/NvS351QKFV4/0.jpg)](http://www.youtube.com/watch?v=NvS351QKFV4 "Dschinghis Khan - Moskau")

## Usage

Just write any message that contains the word `moskau`. Ideally you are also in a voice channel.

## Host the bot yourself

### Running the bot for testing

To run the bot right from Gradle (just for testing, not for production) you can do `gradlew run --args your-bot-token-here`.

### Building the bot for production

To get a distributable package you run `gradlew distZip`. The created zip is located at
`build/distributions/moskau-bot-1.0.0-SNAPSHOT.zip` and contains all necessary things to run the bot, except the token.

### Running the bot for production

After you built the distributable package as described in the previous section, you can copy over the zip file to where
you want to run your bot. There you unzip it whereever you like and run one of the included start scripts.

```shell
unzip moskau-bot-1.0.0-SNAPSHOT.zip
cd moskau-bot-1.0.0-SNAPSHOT
bin/moskau-bot your-bot-token-here
```

The log file will be created in a `log` directory where you execute the last command.
