package com.cos.person.config;

import org.springframework.context.annotation.Configuration;
import io.sentry.Sentry;

@Configuration
public class SentrySupport {

	public SentrySupport() {
		System.out.println("================================ SentrySupport init()");
		//Sentry.init("https://fc71f56ea6de44f694d86c1f429e917e@o4504627921616896.ingest.sentry.io/4504627926335488");
		Sentry.init(options -> {
			  options.setDsn("https://fc71f56ea6de44f694d86c1f429e917e@o4504627921616896.ingest.sentry.io/4504627926335488");
		});
	}
}