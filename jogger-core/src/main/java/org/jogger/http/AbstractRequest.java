package org.jogger.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jogger.middleware.router.Route;


/**
 * Support class that acts as a base class for {@link org.jogger.http.Request} and {@link org.jogger.test.MockRequest}
 * providing common functionality.
 *
 * @author German Escobar
 */
public abstract class AbstractRequest implements Request {

	protected Route route;

	/**
	 * Holds the path variables of the request.
	 */
	protected Map<String,String> pathVariables = new HashMap<String,String>();

	@Override
	public Map<String, String> getPathVariables() {
		return pathVariables;
	}

	@Override
	public String getPathVariable(String name) {
		return pathVariables.get(name);
	}

	/**
	 * Helper method. Initializes the pathVariables property of this class.
	 *
	 * @param routePath the route path as defined in the routes.config file.
	 */
	protected void initPathVariables(String routePath) {
		pathVariables.clear();
		
		List<String> variables = getVariables(routePath);
		String regexPath = routePath.replaceAll(Path.VAR_REGEXP, Path.VAR_REPLACE);

		Matcher matcher = Pattern.compile("(?i)" + regexPath).matcher(getPath());
		matcher.matches();

		// start index at 1 as group(0) always stands for the entire expression
		for (int i=1; i <= variables.size(); i++) {
			String value = matcher.group(i);
			pathVariables.put(variables.get(i-1), value);
		}
	}

	/**
	 * Helper method. Retrieves all the variables defined in the path.
	 *
	 * @param routePath the route path as defined in the routes.config file.
	 *
	 * @return a List object with the names of the variables.
	 */
	private List<String> getVariables(String routePath) {
		List<String> variables = new ArrayList<String>();

		Matcher matcher = Pattern.compile(Path.VAR_REGEXP).matcher(routePath);
		while (matcher.find()) {
			// group(0) always stands for the entire expression and we only want what is inside the {}
			variables.add(matcher.group(1));
		}

		return variables;
	}

}
