package exceptions;

public class NoSpecifiedFilenameException extends Exception
{
	private static final long serialVersionUID = -1230400983219759561L;

	public NoSpecifiedFilenameException()
	{
		super("Error: No output file name specified after -f");
	}
}
