package com.cp.ecommerce.adapter.mail.message;

import java.util.Locale;

import com.cp.ecommerce.domain.order.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * Class that create message for customer.
 */
@Component
@RequiredArgsConstructor
class CustomerMessageCreator extends AbstractMessageCreator {

    private static final String SUBJECT_MESSAGE = "mail.subject";
    @Autowired
    private transient MessageSource messageSource;
    @Value("${spring.mail.template.email-path}")
    private transient String templatePathNameEmail;
    @Value("${spring.mail.template.order-customer:}")
    private transient String templateFileNameEmailToCustomer;

    @Override
    String prepareRecipient(final Order order) {

        return order.getCustomer().getContact().getEmail();
    }

    @Override
    void prepareTemplateInformationForEmail() {

        setTemplateFileNameEmail(templateFileNameEmailToCustomer);
        setTemplatePathEmail(templatePathNameEmail);
    }

    @Override
    String createSubject(final Order order) {

        return messageSource.getMessage(SUBJECT_MESSAGE, new Object[] { order.getOrderNumber() }, Locale.getDefault());
    }

}
