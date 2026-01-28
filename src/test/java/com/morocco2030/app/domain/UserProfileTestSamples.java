package com.morocco2030.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserProfile getUserProfileSample1() {
        return new UserProfile().id(1L).phoneNumber("phoneNumber1").nationality("nationality1").preferredLanguage("preferredLanguage1");
    }

    public static UserProfile getUserProfileSample2() {
        return new UserProfile().id(2L).phoneNumber("phoneNumber2").nationality("nationality2").preferredLanguage("preferredLanguage2");
    }

    public static UserProfile getUserProfileRandomSampleGenerator() {
        return new UserProfile()
            .id(longCount.incrementAndGet())
            .phoneNumber(UUID.randomUUID().toString())
            .nationality(UUID.randomUUID().toString())
            .preferredLanguage(UUID.randomUUID().toString());
    }
}
