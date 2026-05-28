package com.denfop.client.intro;

public class IntroFaqEntry {

    public final String main;
    public final String question;
    public final String answer;

    public IntroFaqEntry(String main, String question, String answer) {
        this.main = main;
        this.question = question;
        this.answer = answer;
    }

    public String main() {
        return main;
    }

    public String question() {
        return question;
    }

    public String answer() {
        return answer;
    }


}