package ru.job4j.bmb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.job4j.bmb.model.*;
import ru.job4j.bmb.repository.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableAspectJAutoProxy
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner loadDatabase(MoodRepository moodRepository,
                                   MoodContentRepository moodContentRepository,
                                   AwardRepository awardRepository) {
        return args -> {
            var moods = moodRepository.findAll();
            if (!moods.isEmpty()) {
                System.out.println("✅ Данные уже загружены, пропускаем инициализацию");
                return;
            }

            System.out.println("🔄 Начинаем загрузку начальных данных...");

            var moodList = new ArrayList<Mood>();
            moodList.add(new Mood("Счастливейший на свете 😎", true));
            moodList.add(new Mood("Воодушевленное настроение 🌟", true));
            moodList.add(new Mood("Успокоение и гармония 🧘‍♂️", true));
            moodList.add(new Mood("В состоянии комфорта ☺️", true));
            moodList.add(new Mood("Легкое волнение 🎈", true));
            moodList.add(new Mood("Сосредоточенное настроение 🎯", true));
            moodList.add(new Mood("Тревожное настроение 😟", false));
            moodList.add(new Mood("Разочарованное настроение 😞", false));
            moodList.add(new Mood("Усталое настроение 😴", false));
            moodList.add(new Mood("Вдохновенное настроение 💡", true));
            moodList.add(new Mood("Раздраженное настроение 😠", false));

            Iterable<Mood> savedMoods = moodRepository.saveAll(moodList);
            List<Mood> savedMoodList = new ArrayList<>();
            savedMoods.forEach(savedMoodList::add);
            System.out.println("✅ Сохранено настроений: " + savedMoodList.size());

            var moodContentList = new ArrayList<MoodContent>();
            moodContentList.add(new MoodContent(
                    findMoodByName(savedMoodList, "Счастливейший на свете 😎"),
                    "Невероятно! Вы сияете от счастья, продолжайте радоваться жизни."));
            moodContentList.add(new MoodContent(
                    findMoodByName(savedMoodList, "Воодушевленное настроение 🌟"),
                    "Великолепно! Вы чувствуете себя на высоте. Продолжайте в том же духе."));
            moodContentList.add(new MoodContent(
                    findMoodByName(savedMoodList, "Успокоение и гармония 🧘‍♂️"),
                    "Потрясающе! Вы в состоянии внутреннего мира и гармонии."));
            moodContentList.add(new MoodContent(
                    findMoodByName(savedMoodList, "В состоянии комфорта ☺️"),
                    "Отлично! Вы чувствуете себя уютно и спокойно."));
            moodContentList.add(new MoodContent(
                    findMoodByName(savedMoodList, "Легкое волнение 🎈"),
                    "Замечательно! Немного волнения добавляет жизни краски."));
            moodContentList.add(new MoodContent(
                    findMoodByName(savedMoodList, "Сосредоточенное настроение 🎯"),
                    "Хорошо! Ваш фокус на высоте, используйте это время эффективно."));
            moodContentList.add(new MoodContent(
                    findMoodByName(savedMoodList, "Тревожное настроение 😟"),
                    "Не волнуйтесь, всё пройдет. Попробуйте расслабиться и найти источник вашего беспокойства."));
            moodContentList.add(new MoodContent(
                    findMoodByName(savedMoodList, "Разочарованное настроение 😞"),
                    "Бывает. Не позволяйте разочарованию сбить вас с толку, всё наладится."));
            moodContentList.add(new MoodContent(
                    findMoodByName(savedMoodList, "Усталое настроение 😴"),
                    "Похоже, вам нужен отдых. Позаботьтесь о себе и отдохните."));
            moodContentList.add(new MoodContent(
                    findMoodByName(savedMoodList, "Вдохновенное настроение 💡"),
                    "Потрясающе! Вы полны идей и энергии для их реализации."));
            moodContentList.add(new MoodContent(
                    findMoodByName(savedMoodList, "Раздраженное настроение 😠"),
                    "Попробуйте успокоиться и найти причину раздражения, чтобы исправить ситуацию."));

            moodContentRepository.saveAll(moodContentList);
            System.out.println("✅ Сохранено контентов настроения: " + moodContentList.size());

            var awards = new ArrayList<Award>();
            awards.add(new Award("Смайлик дня",
                    "За 1 день хорошего настроения. Награда: Веселый смайлик или стикер, отправленный пользователю в качестве поощрения.", 1));
            awards.add(new Award("Настроение недели",
                    "За 7 последовательных дней хорошего или отличного настроения. Награда: Специальный значок или иконка, отображаемая в профиле пользователя в течение недели.", 7));
            awards.add(new Award("Бонусные очки",
                    "За каждые 3 дня хорошего настроения. Награда: Очки, которые можно обменять на виртуальные предметы или функции внутри приложения.", 3));
            awards.add(new Award("Персонализированные рекомендации",
                    "После 5 дней хорошего настроения. Награда: Подборка контента или активности на основе интересов пользователя.", 5));
            awards.add(new Award("Достижение 'Солнечный луч'",
                    "За 10 дней непрерывного хорошего настроения. Награда: Разблокировка новой темы оформления или фона в приложении.", 10));
            awards.add(new Award("Виртуальный подарок",
                    "После 15 дней хорошего настроения. Награда: Возможность отправить или получить виртуальный подарок внутри приложения.", 15));
            awards.add(new Award("Титул 'Лучезарный'",
                    "За 20 дней хорошего или отличного настроения. Награда: Специальный титул, отображаемый рядом с именем пользователя.", 20));
            awards.add(new Award("Доступ к премиум-функциям",
                    "После 30 дней хорошего настроения. Награда: Временный доступ к премиум-функциям или эксклюзивному контенту.", 30));
            awards.add(new Award("Участие в розыгрыше призов",
                    "За каждую неделю хорошего настроения. Награда: Шанс выиграть призы в ежемесячных розыгрышах.", 7));
            awards.add(new Award("Эксклюзивный контент",
                    "После 25 дней хорошего настроения. Награда: Доступ к эксклюзивным статьям, видео или мероприятиям.", 25));
            awards.add(new Award("Награда 'Настроение месяца'",
                    "За поддержание хорошего или отличного настроения в течение целого месяца. Награда: Специальный значок, признание в сообществе или дополнительные привилегии.", 30));
            awards.add(new Award("Физический подарок",
                    "После 60 дней хорошего настроения. Награда: Возможность получить небольшой физический подарок, например, открытку или фирменный сувенир.", 60));
            awards.add(new Award("Коучинговая сессия",
                    "После 45 дней хорошего настроения. Награда: Бесплатная сессия с коучем или консультантом для дальнейшего улучшения благополучия.", 45));
            awards.add(new Award("Разблокировка мини-игр",
                    "После 14 дней хорошего настроения. Награда: Доступ к развлекательным мини-играм внутри приложения.", 14));
            awards.add(new Award("Персональное поздравление",
                    "За значимые достижения (например, 50 дней хорошего настроения). Награда: Персонализированное сообщение от команды приложения или вдохновляющая цитата.", 50));

            awardRepository.saveAll(awards);
            System.out.println("✅ Сохранено наград: " + awards.size());

            System.out.println("🎉 Инициализация данных завершена успешно!");
        };
    }

    private Mood findMoodByName(List<Mood> moodList, String name) {
        return moodList.stream()
                .filter(m -> name.equals(m.getName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Mood not found: " + name));
    }
}