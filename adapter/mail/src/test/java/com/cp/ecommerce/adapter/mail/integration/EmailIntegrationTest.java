package com.cp.ecommerce.adapter.mail.integration;

import java.util.Locale;

import com.cp.ecommerce.adapter.MailTestConfiguration;
import com.cp.ecommerce.adapter.common.utils.OrderBuilder;
import com.cp.ecommerce.adapter.mail.SendEmailAdapter;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import jakarta.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests cases for {@link SendEmailAdapter}.
 */
@SpringBootTest
@Import(MailTestConfiguration.class)
public class EmailIntegrationTest {

    public static final String LOCALE_PL = "pl";

    @RegisterExtension
    private static final GreenMailExtension MAIL_SERVER = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("test", "secret"))
            .withPerMethodLifecycle(false);

    @Autowired
    private transient SendEmailAdapter sendEmailAdapter;

    @BeforeAll
    static void setUp() {

        Locale.setDefault(new Locale(LOCALE_PL));
    }

    @AfterAll
    static void tearDown() {

        Locale.setDefault(Locale.ENGLISH);
    }

    @Test
    void shouldSendEmailWithCorrectPayloadInPolish() throws Exception {

        this.sendEmailAdapter.send(OrderBuilder.mockOrder());

        final MimeMessage receivedMessage = MAIL_SERVER.getReceivedMessages()[0];
        assertThat(receivedMessage).isNotNull();
        assertThat(GreenMailUtil.getBody(receivedMessage)).contains("Pozdrawiamy");
        assertEquals(1, receivedMessage.getAllRecipients().length);
        assertEquals("test@test.com", receivedMessage.getAllRecipients()[0].toString());
    }

}
