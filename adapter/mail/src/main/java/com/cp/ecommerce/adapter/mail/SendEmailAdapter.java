package com.cp.ecommerce.adapter.mail;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import com.cp.ecommerce.adapter.common.annotation.WebAdapter;
import com.cp.ecommerce.adapter.common.resilience.ResilientExecutor;
import com.cp.ecommerce.adapter.mail.message.EmailMessageFactory;
import com.cp.ecommerce.domain.order.Order;
import com.cp.ecommerce.domain.order.port.outgoing.SendEmailOutPort;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.Address;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Representation of {@link SendEmailAdapter} behavior.
 */
@Slf4j
@RequiredArgsConstructor
@WebAdapter
@ConditionalOnProperty(name = "service.mail.enabled", havingValue = "true")
public class SendEmailAdapter implements SendEmailOutPort {

    private static final String RESILIENCE_INSTANCE_NAME = "sendEmail";

    private final JavaMailSender emailSender;

    private final EmailMessageFactory emailMessageFactory;

    private final ResilientExecutor resilientExecutor;

    @Override
    public void send(final Order order) {

        try {

            final Callable<MimeMessage> action = () -> {
                final MimeMessage messageToBeSent = emailMessageFactory.createEmailMessage(order);
                emailSender.send(messageToBeSent);
                return messageToBeSent;
            };
            final MimeMessage messageToBeSent = resilientExecutor.callResilient(RESILIENCE_INSTANCE_NAME, action);
            log.info(
                    "Email with order request confirmation was send to: {}",
                    Arrays.stream(messageToBeSent.getAllRecipients()).map(Address::toString).collect(Collectors.joining(", ")));
        } catch (Exception ex) {

            log.error("Error while creating and sending emails for order: {}", order.getOrderNumber());
            throw new MailParseException(ex);
        }
    }

}
