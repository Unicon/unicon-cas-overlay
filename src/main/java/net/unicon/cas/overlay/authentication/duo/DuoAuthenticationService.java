package net.unicon.cas.overlay.authentication.duo;

import com.duosecurity.DuoWeb;

/**
 * An abstraction that encapsulates interaction with Duo 2fa authentication service via its public API
 * <p/>
 * Derived from the fine work of @author Eric Pierce <epierce@usf.edu>
 * and @author Michael Kennedy <michael.kennedy@ucr.edu>
 */
public final class DuoAuthenticationService {

    private final String duoIntegrationKey;

    private final String duoSecretKey;

    private final String duoApplicationKey;

    private final String duoApiHost;



    public DuoAuthenticationService(String duoIntegrationKey, String duoSecretKey, String duoApplicationKey, String duoApiHost) {
        this.duoIntegrationKey = duoIntegrationKey;
        this.duoSecretKey = duoSecretKey;
        this.duoApplicationKey = duoApplicationKey;
        this.duoApiHost = duoApiHost;
    }

    public String getDuoApiHost() {
        return duoApiHost;
    }

    public String generateSignedRequestToken(String username) {
        return DuoWeb.signRequest(this.duoIntegrationKey, this.duoSecretKey, this.duoApplicationKey, username);
    }

    public String authenticate(final String signedRequestToken) {
        return DuoWeb.verifyResponse(this.duoIntegrationKey, this.duoSecretKey, this.duoApplicationKey, signedRequestToken);
    }
}
