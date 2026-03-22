package ru.job4j.bmb;

import ru.job4j.bmb.content.Content;  // ← Верный пакет!
import ru.job4j.bmb.services.BotCommandHandler;
import ru.job4j.bmb.services.TelegramBotService;

public class DIByDirectInjectMain {
    public static void main(String[] args) {
        var handler = new BotCommandHandler();
        var tg = new TelegramBotService(handler);
        tg.receive(new Content());
    }
}
