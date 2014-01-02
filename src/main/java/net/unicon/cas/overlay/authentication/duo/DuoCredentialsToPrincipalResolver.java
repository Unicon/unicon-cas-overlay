package net.unicon.cas.overlay.authentication.duo;

import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.CredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.SimplePrincipal;

/**
 * DuoCredentialsToPrincipalResolver is responsible for converting a {@link DuoCredentials} to
 * a CAS {@link org.jasig.cas.authentication.principal.Principal}. This is used by the authentication process after an authentication succeeds.
 * <p/>
 * This creates a new {@link org.jasig.cas.authentication.principal.SimplePrincipal} and copies all of the attribute data from the {@link org.jasig.cas.authentication.principal.Principal} used
 * during the initial authentication.
 * <p/>
 * Derived from the fine work of @author Eric Pierce <epierce@usf.edu>
 * and @author Michael Kennedy <michael.kennedy@ucr.edu>
 */
public final class DuoCredentialsToPrincipalResolver implements CredentialsToPrincipalResolver {

    /**
     * Returns a {@link org.jasig.cas.authentication.principal.Principal} based on a {@link DuoCredentials} credential.
     *
     * @param credentials a DuoCredentials object
     *
     * @return a {@link org.jasig.cas.authentication.principal.Principal} based on a {@link DuoCredentials} credential
     */
    @Override
    public Principal resolvePrincipal(Credentials credentials) {
        final DuoCredentials duoCredentials = DuoCredentials.class.cast(credentials);
        if (duoCredentials.getPrincipal().getAttributes() != null) {
            return new SimplePrincipal(duoCredentials.getPrincipal().getId(), duoCredentials.getPrincipal().getAttributes());
        }
        return new SimplePrincipal(duoCredentials.getPrincipal().getId());
    }

    /**
     * Determines whether a particular credential is supported by this {@link org.jasig.cas.authentication.principal.CredentialsToPrincipalResolver}. This
     * only supports {@link DuoCredentials}.
     *
     * @param credentials any {@link org.jasig.cas.authentication.principal.Credentials} object
     *
     * @return a boolean indicating whether it is supported
     */
    @Override
    public boolean supports(Credentials credentials) {
        return (DuoCredentials.class.isAssignableFrom(credentials.getClass()));
    }
}
