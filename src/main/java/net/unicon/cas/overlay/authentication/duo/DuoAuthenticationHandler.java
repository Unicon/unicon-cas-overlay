package net.unicon.cas.overlay.authentication.duo;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.AuthenticationHandler;
import org.jasig.cas.authentication.principal.Credentials;
import com.duosecurity.DuoWeb;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DuoAuthenticationHandler
 * <p/>
 * Bean that implements AuthenticationHandler that can be added to deployerConfigContext.xml to
 * perform authentications on {@link DuoCredentials}.
 * <p/>
 * Derived from the fine work of @author Eric Pierce <epierce@usf.edu>
 * and @author Michael Kennedy <michael.kennedy@ucr.edu>
 */
public class DuoAuthenticationHandler implements AuthenticationHandler {

    public DuoAuthenticationHandler(DuoAuthenticationService duoAuthenticationService) {
        this.duoAuthenticationService = duoAuthenticationService;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(DuoAuthenticationHandler.class);

    private final DuoAuthenticationService duoAuthenticationService;

    /**
     * Returns a boolean indicating whether an authentication using a {@link DuoCredentials} credential
     * was successful.
     *
     * @param credentials a DuoCredentials object
     *
     * @return a boolean indicating whether an authentication was successful
     *
     * @throws AuthenticationException
     */
    @Override
    public boolean authenticate(Credentials credentials) throws AuthenticationException {
        final DuoCredentials duoCredentials = DuoCredentials.class.cast(credentials);

        // Do an out of band request using the DuoWeb api (encapsulated in DuoAuthenticationService) to the hosted duo service, if it is successful
        // it will return a String containing the username of the successfully authenticated user, but will
        // return a blank String otherwise.
        final String duoVerifyResponse = this.duoAuthenticationService.authenticate(duoCredentials.getSignedDuoResponse());

        LOGGER.debug("Response from Duo verify: [{}]", duoVerifyResponse);

        if (duoVerifyResponse != null && duoVerifyResponse.equals(duoCredentials.getPrincipal().getId())) {
            LOGGER.info("Successful Duo authentication for [{}]", duoCredentials.getPrincipal().getId());
            return true;
        }
        LOGGER.error("Duo authentication error! Login username: [{}], Duo response: [{}]",
                duoCredentials.getPrincipal().getId(),
                duoVerifyResponse);

        return false;
    }

    @Override
    public boolean supports(Credentials credentials) {
        return (DuoCredentials.class.isAssignableFrom(credentials.getClass()));
    }
}
