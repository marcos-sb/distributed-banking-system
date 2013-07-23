package es.udc.fic.acs.infmsb01.atm.common.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class ConfigurationParametersManager {

	private static final String CONFIGURATION_FILE = "ConfigurationParameters.properties";

	@SuppressWarnings("rawtypes")
	private static Map parameters;

	private ConfigurationParametersManager() {
	}

	public static String getParameter(String name) throws IOException {

		return getParameters().get(name);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map<String, String> getParameters() throws IOException {

		if (parameters == null) {

			/* Read property file. */
			Class<ConfigurationParametersManager> configurationParametersManagerClass = ConfigurationParametersManager.class;
			ClassLoader classLoader = configurationParametersManagerClass
					.getClassLoader();
			InputStream inputStream = classLoader
					.getResourceAsStream(CONFIGURATION_FILE);
			Properties properties = new Properties();
			properties.load(inputStream);
			inputStream.close();

			/*
			 * We use a "HashMap" instead of a "Properties" because HashMap's
			 * methods are *not* synchronized (then, they are faster) and the
			 * parameters are read-only.
			 */

			parameters = new HashMap(properties);

		}

		return parameters;

	}

}
