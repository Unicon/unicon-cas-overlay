package net.unicon.cas.overlay.events;

import com.github.inspektr.common.web.ClientInfoHolder;
import net.unicon.cas.addons.authentication.support.Assertions;
import net.unicon.cas.addons.info.events.CasServiceTicketValidatedEvent;
import org.jasig.cas.authentication.principal.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


/**
 * Example Cas ST validated event listener that logs contextual data associated with <code>CasServiceTicketValidatedEvent</code>
 * to the slf4j appender's destination.
 *
 * @author Dmitriy Kopylenko
 * @author Unicon, inc.
 */
@Component
public class LogPublishingCasServiceTicketValidatedEventListener implements
        ApplicationListener<CasServiceTicketValidatedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(LogPublishingCasServiceTicketValidatedEventListener.class);

    @Override
    public void onApplicationEvent(CasServiceTicketValidatedEvent event) {
        final Principal principal = Assertions.getAuthenticatedPrincipalFrom(event.getAssertion());
        logger.info("==================== RELEASED SERVICE ATTRIBUTES =============================");
        logger.info("Request is received from the following IP address: [{}]", ClientInfoHolder.getClientInfo().getClientIpAddress());
        logger.info("Service [{}] will receive the following attributes [{}] for the authenticated principal [{}]", event.getService().getId(), principal.getAttributes(), principal.getId());
        logger.info("==================== RELEASED SERVICE ATTRIBUTES (END) =============================");
    }
}
