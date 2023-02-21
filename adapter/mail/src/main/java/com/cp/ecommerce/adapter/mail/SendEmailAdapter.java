package com.cp.ecommerce.adapter.mail;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.cp.ecommerce.adapter.common.annotation.WebAdapter;
import com.cp.ecommerce.adapter.mail.message.EmailMessageFactory;
import com.cp.ecommerce.domain.order.Order;
import com.cp.ecommerce.domain.order.port.outgoing.SendEmailOutPort;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.Address;
import jakarta.mail.MessagingException;
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

    private final JavaMailSender emailSender;

    private final EmailMessageFactory emailMessageFactory;

    @Override
    public void send(final Order order) {

        try {

            final MimeMessage messageToBeSent = emailMessageFactory.createEmailMessage(order);
            emailSender.send(messageToBeSent);
            log.info(
                    "Email with order request confirmation was send to: {}",
                    Arrays.stream(messageToBeSent.getAllRecipients()).map(Address::toString).collect(Collectors.joining(", ")));
        } catch (MailException | MessagingException ex) {

            log.error("Error while creating and sending emails for order: {}", order.getOrderNumber());
            throw new MailParseException(ex);
        }
    }

}
