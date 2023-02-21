package com.cp.ecommerce.adapter.mail.pdf.i18n;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Representation of translatable string used during FreeMarker template processing.
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class TranslatableString {

    @Getter
    private final String key;

    @Getter
    private final String defaultMessage;

    public static TranslatableString forKey(final String key, final String defaultMessage) {

        return new TranslatableString(key, defaultMessage);
    }

    @Override
    public String toString() {

        return defaultMessage;
    }

}
