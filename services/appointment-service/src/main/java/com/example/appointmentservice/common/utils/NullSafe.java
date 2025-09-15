package com.example.appointmentservice.common.utils;

import java.util.function.Supplier;

public final class NullSafe {

    private NullSafe() {
        // Previene ser instanciada
    }

    /**
     * Return the value if not null, otherwise return null
     */
    public static <T> T ifPresentOrNull(T value) {
        return value;
    }

    /**
     * Return string if not null neither blank, otherwise return null
     */
    public static String ifNotBlankOrNull(String value) {
        return (value != null && !value.isBlank()) ? value.trim() : null;
    }

    /**
     * Return given value if condition is true, otherwise return null
     */
    public static <T> T ifTrueOrNull(boolean condition, Supplier<T> valueSupplier) {
        return condition ? valueSupplier.get() : null;
    }
}
