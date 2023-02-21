package com.cp.ecommerce.adapter.mail.pdf.ftl;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import lombok.Builder;
import lombok.Getter;

/**
 * FTL representation of clickable email entry.
 */
@Getter
@Builder
public class EmailFtl implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String address;

    public static EmailFtl of(final String email) {

        if (StringUtils.isBlank(email)) {
            return EmailFtl.builder().build();
        }
        return EmailFtl.builder().address(email).build();
    }

}
