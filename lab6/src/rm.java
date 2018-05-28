/*
 * $Id: ls.java,v 1.6 2001/10/12 02:14:31 rayo Exp $
 */

/*
 * $Log: ls.java,v $
 * Revision 1.6  2001/10/12 02:14:31  rayo
 * better formatting
 *
 * Revision 1.5  2001/10/07 23:48:55  rayo
 * added author javadoc tag
 *
 */

/**
 * Unlink file.
 * <p>
 * Usage:
 * <pre>
 *   java rm <i>path-name</i> ...
 * </pre>
 *
 * @author Ray Ontko
 */
public class rm {
    /**
     * The name of this program.
     * This is the program name that is used
     * when displaying error messages.
     */
    public static String PROGRAM_NAME = "rm";

    /**
     * Lists information about named files or directories.
     *
     * @throws Exception if an exception is thrown
     *                   by an underlying operation
     */
    public static void main(String[] args) throws Exception {
        // initialize the file system simulator kernel
        Kernel.initialize();

        // for each path-name given
        for (int i = 0; i < args.length; i++) {
            String name = args[i];
            Kernel.unlink(name);
        }

        // exit with success if we process all the arguments
        Kernel.exit(0);
    }
}
