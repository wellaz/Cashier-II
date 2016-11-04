package helpers;

/**
 *
 * @author Wellington
 */
public class ThisMonth {

	public ThisMonth() {

	}

	public static String thisMonth() {
		int monthint = new SetDateCreated().getMonth() - 1;
		return MonthsList.getMonths().get(monthint);
	}
}
