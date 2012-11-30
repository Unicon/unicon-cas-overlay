package net.unicon.cas.overlay.events;

import net.unicon.cas.addons.info.events.CasServiceTicketGrantedEvent;
import net.unicon.cas.addons.info.events.CasServiceTicketValidatedEvent;
import net.unicon.cas.addons.info.events.CasSsoSessionDestroyedEvent;
import net.unicon.cas.addons.info.events.CasSsoSessionEstablishedEvent;
import org.apache.camel.ProducerTemplate;
import org.jasig.cas.CasVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Sample aggregate for CAS event listeners which publishes those events as String messages to a pre-configured Camel route (configured in the external application context file).
 * <p/>
 * <strong>NOTE:</strong> this class is completely decoupled from the Camel route details e.g. endpoints, transports, external systems used. The Camel route could be freely reconfigured
 * to consume/publish these messages to completely different endpoints(external systems), add different EIP processors to it, etc. without the need to ever touch this class! True loose-coupleing in action,
 * demostrating the benefits of using integration broker frameworks such as Apache Camel!
 *
 * @author Dmitriy Kopylenko
 */
public abstract class CasEventsCamelGateway {

	@Autowired
	private ProducerTemplate camelProducerTemplate;

	protected void doPublishMessageToCamel(String message) {
		this.camelProducerTemplate.sendBody(message);
	}

	protected String getCasVersion() {
		return String.format("CAS version [%s] -->", CasVersion.getVersion());
	}

	@Component
	public static class SsoSessionEstablishedEventCamelMessagePublisher extends
			CasEventsCamelGateway implements ApplicationListener<CasSsoSessionEstablishedEvent> {

		@Override
		public void onApplicationEvent(CasSsoSessionEstablishedEvent event) {
			doPublishMessageToCamel(String.format("%s User [%s] logged in at [%s]", getCasVersion(),
					event.getAuthentication().getPrincipal().getId(), event.getAuthentication().getAuthenticatedDate()));
		}
	}

	@Component
	public static class SsoSessionDestroyedEventCamelMessagePublisher extends CasEventsCamelGateway implements
			ApplicationListener<CasSsoSessionDestroyedEvent> {

		@Override
		public void onApplicationEvent(CasSsoSessionDestroyedEvent event) {
			doPublishMessageToCamel(String.format("%s User [%s] logged out at [%s]", getCasVersion(),
					event.getAuthentication().getPrincipal().getId(), new Date()));
		}
	}

	@Component
	public static class ServiceTicketGrantedEventCamelMessagePublisher extends CasEventsCamelGateway implements
			ApplicationListener<CasServiceTicketGrantedEvent> {

		@Override
		public void onApplicationEvent(CasServiceTicketGrantedEvent event) {
			final String user = event.getAuthentication().getPrincipal().getId();
			final String service = event.getService().getId();
			final String st = event.getServiceTicketId();
			doPublishMessageToCamel(
					String.format("%s Service Ticket [%s] has been granted for user [%s] accessing service [%s]", getCasVersion(), st, user, service));
		}
	}

	@Component
	public static class ServiceTicketValidatedEventCamelMessagePublisher extends CasEventsCamelGateway implements
			ApplicationListener<CasServiceTicketValidatedEvent> {

		@Override
		public void onApplicationEvent(CasServiceTicketValidatedEvent event) {
			//Hack. Need to introduce 'Assertions' utility in cas-addons to hide this CAS implementiton detail of storing these chained authentications
			final String user = event.getAssertion().getChainedAuthentications().get(0).getPrincipal().getId();
			final String service = event.getService().getId();
			final String st = event.getServiceTicketId();
			doPublishMessageToCamel(
					String.format("%s Service Ticket [%s] has been validated for user [%s] accessing service [%s]", getCasVersion(), st, user, service));
		}
	}
}
