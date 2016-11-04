package helpers;

/**
 *
 * @author Wellington
 */

public class CountRecords {

	public static String returnRecords(int rows) {
		return (rows > 1) ? rows + " Records found." : rows + " Record found";
	}

}
