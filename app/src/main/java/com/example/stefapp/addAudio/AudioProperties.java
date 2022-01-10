package com.example.stefapp.addAudio;

public class AudioProperties {


    private String topic;
    private String chapter;
    private String name;
    private String audioUrl;

    public AudioProperties() {
    }

    public AudioProperties(String topic, String chapter, String name, String audioUrl) {
        this.topic = topic;
        this.chapter = chapter;
        this.name = name;
        this.audioUrl = audioUrl;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

}
