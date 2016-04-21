package com.devignition.service;

import com.devignition.service.core.Speaker;
import de.svenjacobs.loremipsum.LoremIpsum;

import java.util.Random;

public final class TestHelpers {

    private static final Random RANDOM = new Random();
    private static final LoremIpsum LOREM_IPSUM = new LoremIpsum();

    private TestHelpers() {
    }

    public static String someLoremIpsumText() {
        int numWords = 20 + RANDOM.nextInt(20);
        int startWord = RANDOM.nextInt(16);
        return LOREM_IPSUM.getWords(numWords, startWord);
    }

    public static Speaker newSpeaker(String name, String twitterHandle) {
        return newSpeaker(null, name, twitterHandle);
    }

    public static Speaker newSpeaker(Long id, String name, String twitterHandle) {
        return Speaker.builder()
                .id(id)
                .name(name)
                .twitterHandle(twitterHandle)
                .bio(someLoremIpsumText())
                .talkTitle(someLoremIpsumText())
                .talkAbstract(someLoremIpsumText())
                .build();
    }

}
