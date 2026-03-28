package ru.job4j.bmb;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.services.BotCommandHandler;
import ru.job4j.bmb.services.TelegramBotService;
import ru.job4j.bmb.services.MoodService;

public class DIByDirectInjectMain {
    public static void main(String[] args) {
        System.out.println("⚠️ Ручная инъекция зависимостей без Spring контекста не рекомендуется.");
        System.out.println("✅ Для запуска бота используйте класс Main с Spring Boot.");

        // Используем Spring контекст даже для тестового запуска
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(Main.class);
            context.refresh();

            var handler = context.getBean(BotCommandHandler.class);
            var tg = context.getBean(TelegramBotService.class);

            tg.receive(new Content(999L).setText("Тестовое сообщение"));
        }
    }
}