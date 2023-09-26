/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2022 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
 *
 * Open Hospital is a free and open source software for healthcare data management.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.isf.telemetry.service.remote;

import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.stereotype.Component;

import feign.Feign;
import feign.slf4j.Slf4jLogger;

@Component
public class TelemetryDataCollectorGatewayService {

	private static final String SERVICE_NAME = "telemetry-gateway-service";
	private static final String RESPONSE_SUCCESS = "OK";

	private static final Logger LOGGER = LoggerFactory.getLogger(TelemetryDataCollectorGatewayService.class);

	@Resource(name = "telemetryProperties")
	private Properties properties;

	public boolean send(String data) {
		TelemetryGatewayRemoteService httpClient = buildHttlClient();
		LOGGER.debug(data);
		String result = httpClient.send(data);
		LOGGER.debug(result);
		boolean isSent = RESPONSE_SUCCESS.equals(result);
		return isSent;
	}

	private TelemetryGatewayRemoteService buildHttlClient() {
		String baseUrl = this.properties.getProperty(SERVICE_NAME + ".base-url").trim();
		// For debug remember to update log level to: feign.Logger.Level.FULL. Happy debugging!
		return Feign.builder()
						.logger(new Slf4jLogger(TelemetryGatewayRemoteService.class)).logLevel(feign.Logger.Level.BASIC).contract(new SpringMvcContract())
						.target(TelemetryGatewayRemoteService.class, baseUrl);
	}

}
