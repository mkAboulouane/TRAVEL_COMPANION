package com.morocco2030.app.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class UserPermissionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserPermission getUserPermissionSample1() {
        return new UserPermission().id(1L);
    }

    public static UserPermission getUserPermissionSample2() {
        return new UserPermission().id(2L);
    }

    public static UserPermission getUserPermissionRandomSampleGenerator() {
        return new UserPermission().id(longCount.incrementAndGet());
    }
}
