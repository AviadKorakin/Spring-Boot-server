package restR.logic.validTests;

import org.springframework.stereotype.Component;

@Component
public class ValidTestsString {
	/**
	 * Checks if the provided string is either null or blank.
	 *
	 * This method evaluates the input string and returns true if the string is
	 * either null or contains only whitespace characters. Otherwise, it returns
	 * false.
	 *
	 * @param string The string to be checked for null or blank.
	 * @return true if the string is null or is blank; false otherwise.
	 */
	public boolean isNullorBlank(String string) {
		return string == null || string.isBlank();
	}

	/**
	 * This method checks if the provided string matches the default value "string".
	 * 
	 * @param string The input string to be compared.
	 * @return Returns true if the input string is "string", otherwise returns
	 *         false.
	 */
	@Deprecated
	public boolean isDefault(String string) {
		return string.equals("string");
	}

}
