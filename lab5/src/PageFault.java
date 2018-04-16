/* It is in this file, specifically the replacePage function that will
   be called by MemoryManagement when there is a page fault.  The 
   users of this program should rewrite PageFault to implement the 
   page replacement algorithm.
*/

// This PageFault file is an example of the FIFO Page Replacement
// Algorithm as described in the Memory Management section.

import java.util.*;

public class PageFault {

    public static CircularList clock;
    private static final int maxInMemTime = 80;

    /**
     * WSClock page replacement algorithm
     *
     * @param memory         is the vector which contains the contents of the pages
     *                       in memory being simulated.  mem should be searched to find the
     *                       proper page to remove, and modified to reflect any changes.
     * @param replacePageNum is the requested page which caused the
     *                       page fault.
     * @param controlPanel   represents the graphical element of the
     *                       simulator, and allows one to modify the current display.
     */
    public static void replacePageWSClock(Vector memory, int replacePageNum, ControlPanel controlPanel) {
        Page page = clock.currentPage();
        Page initial = page;
        boolean circleMade = false;
        while (true) {
            // Move forward
            if (page.R == 1) {
                page = clock.moveHand();
            }
            // Check M-bit
            else {
                // Page not in working set, replace it
                if ((circleMade || page.inMemTime > maxInMemTime) && page.M == 1) {
                    Page newPage = (Page) memory.elementAt(replacePageNum);
                    controlPanel.removePhysicalPage(page.id);
                    newPage.physical = page.physical;
                    controlPanel.addPhysicalPage(newPage.physical, replacePageNum);
                    page.inMemTime = 0;
                    page.lastTouchTime = 0;
                    page.R = 0;
                    page.M = 0;
                    page.physical = -1;
                    clock.replaceAtHand(newPage);
                    break;
                }
                // Schedule disk write
                else {
                    Page thisPage = page;
                    new Thread(() -> {
                        try {
                            Thread.sleep(new Random().nextInt(20));
                            thisPage.M = 1;

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                    page = clock.moveHand();
                }
            }
            if (page == initial) circleMade = true;
        }
    }
}
