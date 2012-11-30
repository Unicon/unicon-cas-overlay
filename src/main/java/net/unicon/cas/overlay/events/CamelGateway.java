package net.unicon.cas.overlay.events;

import net.unicon.cas.addons.info.events.CasSsoSessionEstablishedEvent;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author Dmitriy Kopylenko
 */
@Component
public class CamelGateway {

	@Autowired
	protected ProducerTemplate camelProducerTemplate;

	@Component
	public static class SsoSessionEstablishedPublishToCamel extends CamelGateway implements ApplicationListener<CasSsoSessionEstablishedEvent> {

		@Override
		public void onApplicationEvent(CasSsoSessionEstablishedEvent event) {
			camelProducerTemplate.sendBody(String.format("User [%s] logged in at [%s]",
					event.getAuthentication().getPrincipal().getId(), event.getAuthentication().getAuthenticatedDate()));
		}
	}
}
