package ru.job4j.bmb.content;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class Content {
    private final Long chatId;
    private String text;
    private InputFile photo;
    private InputFile audio;
    private InputFile video;
    private InlineKeyboardMarkup markup;

    public Content(Long chatId) {
        this.chatId = chatId;
    }

    public Long getChatId() { return chatId; }
    public String getText() { return text; }
    public InputFile getPhoto() { return photo; }
    public InputFile getAudio() { return audio; }
    public InputFile getVideo() { return video; }
    public InlineKeyboardMarkup getMarkup() { return markup; }

    public Content setText(String text) {
        this.text = text;
        return this;
    }

    public Content setPhoto(InputFile photo) {
        this.photo = photo;
        return this;
    }

    public Content setAudio(InputFile audio) {
        this.audio = audio;
        return this;
    }

    public Content setVideo(InputFile video) {
        this.video = video;
        return this;
    }

    public Content setMarkup(InlineKeyboardMarkup markup) {
        this.markup = markup;
        return this;
    }
}

