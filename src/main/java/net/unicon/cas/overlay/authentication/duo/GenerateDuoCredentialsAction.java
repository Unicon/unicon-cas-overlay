package net.unicon.cas.overlay.authentication.duo;

import net.unicon.cas.addons.authentication.AuthenticationSupport;
import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.execution.RequestContext;

/**
 * Derived from the fine work of @author Eric Pierce <epierce@usf.edu>
 * and @author Michael Kennedy <michael.kennedy@ucr.edu>
 */
public final class GenerateDuoCredentialsAction {

    private final AuthenticationSupport authenticationSupport;

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateDuoCredentialsAction.class);

    public GenerateDuoCredentialsAction(AuthenticationSupport authenticationSupport) {
        this.authenticationSupport = authenticationSupport;
    }

    public String createDuoCredentials(RequestContext context) {
        final String tgtId = WebUtils.getTicketGrantingTicketId(context);
        final Authentication originalAuthentication = this.authenticationSupport.getAuthenticationFrom(tgtId);
        final Principal principal = originalAuthentication == null ? null : originalAuthentication.getPrincipal();
        if (principal == null) {
            LOGGER.error("Failed to retrieve authentication principal from TGT {}", tgtId);
            return "error";
        }


        // If the user has previously authenticated and is accessing a Duo-protected server, the webflow doesn't 
        // contain the credential used during the inital login.  Copy the username stored in the TGT into a new 
        // UserPasswordCredentials object.
        UsernamePasswordCredentials origCredentials = UsernamePasswordCredentials.class.cast(context.getFlowScope().get("credentials"));
        if ((origCredentials == null) || (origCredentials.getUsername() == null)) {
            origCredentials = new UsernamePasswordCredentials();
            origCredentials.setUsername(principal.getId());
        }

        LOGGER.debug("Retrieved authentication context. Building Duo credentials...");
        DuoCredentials credentials = new DuoCredentials();

        credentials.setFirstAuthentication(originalAuthentication);
        LOGGER.debug("Added first authentication [{}] to the DuoCredential", originalAuthentication);

        credentials.setFirstCredentials(origCredentials);
        LOGGER.debug("Added first credential [{}] to the DuoCredential", origCredentials);

        //Make sure there is a UserPassword credential in Flow Scope (needed for LPPE, etc.)
        context.getFlowScope().put("credentials", origCredentials);

        //Add the DuoCredentials into Flow Scope 
        context.getFlowScope().put("duoCredentials", credentials);

        return "created";
    }
}
