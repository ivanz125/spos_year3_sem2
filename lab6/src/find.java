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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * A simple directory files and subdirectories listing program for a simulated file system.
 * <p>
 * Usage:
 * <pre>
 *   java find <i>path-name</i>
 * </pre>
 *
 * @author Ray Ontko
 */
public class find {
    /**
     * The name of this program.
     * This is the program name that is used
     * when displaying error messages.
     */
    public static String PROGRAM_NAME = "find";

    /**
     * Lists information about named files or directories.
     *
     * @throws Exception if an exception is thrown
     *                   by an underlying operation
     */
    public static void main(String[] args) throws Exception {
        // initialize the file system simulator kernel
        Kernel.initialize();

        if (args.length == 0) {
            System.err.println("Missing path name");
            Kernel.exit(1);
        }

        processFile(args[0]);

        // exit with success if we process all the arguments
        Kernel.exit(0);
    }

    private static void processFile(String name) throws Exception {
        // stat the name to get information about the file or directory
        Stat stat = new Stat();
        int status = Kernel.stat(name, stat);
        if (status < 0) {
            Kernel.perror(PROGRAM_NAME);
            Kernel.exit(1);
        }

        // mask the file type from the mode
        short type = (short) (stat.getMode() & Kernel.S_IFMT);

        // Show error if file is not a directory
        if (type != Kernel.S_IFDIR) {
            System.err.println(name + ": not a directory");
            Kernel.exit(1);
        }
        // if name is a directory open it and read the contents
        else {
            Queue<String> files = new LinkedList<>();
            files.add(name);

            while (!files.isEmpty()) {
                name = files.poll();
                // Print file name
                System.out.println(name);

                // Get file info
                status = Kernel.stat(name, stat);
                if (status < 0) {
                    Kernel.perror(PROGRAM_NAME);
                    Kernel.exit(1);
                }
                // mask the file type from the mode
                short currentType = (short) (stat.getMode() & Kernel.S_IFMT);
                // If just a file, printing is enough
                if (currentType != Kernel.S_IFDIR) continue;

                // open the directory
                int fd = Kernel.open(name, Kernel.O_RDONLY);
                if (fd < 0) {
                    Kernel.perror(PROGRAM_NAME);
                    System.err.println(PROGRAM_NAME + ": unable to open \"" + name + "\" for reading");
                    Kernel.exit(1);
                }

                // Trim slash in directory name for fancy output
                if (name.equals("/")) name = "";

                // create a directory entry structure to hold data as we read
                DirectoryEntry directoryEntry = new DirectoryEntry();

                // while we can read, add all files to queue
                while (true) {
                    // read an entry; quit loop if error or nothing read
                    status = Kernel.readdir(fd, directoryEntry);
                    if (status <= 0) break;

                    // get the name from the entry
                    String entryName = directoryEntry.getName();

                    // Don not print . and .. dirs
                    if (entryName.equals(".") || entryName.equals("..")) continue;

                    // Add file to queue
                    String entryFullName = name + "/" + entryName;
                    files.add(entryFullName);
                }

                // check to see if our last read failed
                if (status < 0) {
                    Kernel.perror("main");
                    System.err.println("main: unable to read directory entry from /");
                    Kernel.exit(2);
                }

                // close the directory
                Kernel.close(fd);
            }
        }
    }
}
