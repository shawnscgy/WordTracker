package exceptions;

public class NotEnoughArgumentsException extends Exception
{
	private static final long serialVersionUID = -9208346096585638995L;

	public NotEnoughArgumentsException()
	{
		super("\"The command line should be: \"\r\n"
				+ "					+ \"java -jar WordTracker.jar <input.txt> -pf/-pl/-po [-f<output.txt>]\"");
	}
}
