package exception;

/**
 * Use this Exception when an integer is out of bound.
 * @author Jean
 *
 */
public class MyOutOfBoundException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Exception : </br>
	 * "The number is out of bounds."
	 */
	public MyOutOfBoundException() {
		System.err.println("The number is out of bounds.");
	}

	/**
	 * Exception : </br>
	 * name + " is out of bounds."
	 * @param name (String)	The name of the integer.
	 */
	public MyOutOfBoundException(String name) {
		System.err.println(name + " is out of bounds.");
	}

	/**
	 * Exception : </br>
	 * "The number " + ofbNumber+" is out of bounds."
	 * @param number (int) The value of the integer.
	 */
	public MyOutOfBoundException(int ofbNumber) {
		System.err.println("The number " + ofbNumber+" is out of bounds.");
	}

	/**
	 * Exception : </br>
	 * "The number " + ofbNumber + " is out of bounds. It should be between " + lowerLimit + " and " + higherLimit + "."
	 * @param ofbNumber (int) The value of the integer.
	 * @param lowerLimit (int) The minimum value of the integer. (include)
	 * @param higherLimit (int) The maximum value of the integer. (include)
	 */
	public MyOutOfBoundException(int ofbNumber, int lowerLimit, int higherLimit) {
		System.err.println("The number " + ofbNumber + " is out of bounds. It should be between " + lowerLimit + " and " + higherLimit + ".");
	}

	/**
	 * Exception : </br>
	 * name + " = " + ofbNumber + " is out of bounds. It should be between " + lowerLimit + " and " + higherLimit + "."
	 * @param name (String)	The name of the integer.
	 * @param ofbNumber (int) The value of the integer.
	 * @param lowerLimit (int) The minimum value of the integer. (include)
	 * @param higherLimit (int) The maximum value of the integer. (include)
	 */
	public MyOutOfBoundException(String name, int ofbNumber, int lowerLimit, int higherLimit) {
		System.err.println(name + " = " + ofbNumber + " is out of bounds. It should be between " + lowerLimit + " and " + higherLimit + ".");
	}
	
	/**
	 * Use this to test if an integer is out of bound and throw the exception if needed.
	 * @param name (String)	The name of the integer.
	 * @param ofbNumber (int) The value of the integer.
	 * @param lowerLimit (int) The minimum value of the integer. (include)
	 * @param higherLimit (int) The maximum value of the integer. (include)
	 * @throws MyOutOfBoundException
	 */
	public static void test(String name, int number, int lowerLimit, int higherLimit) throws MyOutOfBoundException {
		if(number < lowerLimit || number > higherLimit)
			throw new MyOutOfBoundException(name, number, lowerLimit, higherLimit);
	}
}
