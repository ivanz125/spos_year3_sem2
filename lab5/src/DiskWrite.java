import java.util.*;

public class DiskWrite {

    private List<PageWrite> pagesToWrite;
    private Random random;
    private static DiskWrite diskWrite;

    private class PageWrite {
        Page page;
        int timeUntilWrite;

        PageWrite(Page page, int timeUntilWrite) {
            this.page = page;
            this.timeUntilWrite = timeUntilWrite;
        }
    }

    public DiskWrite() {
        pagesToWrite = new ArrayList<>();
        random = new Random();
    }

    public static DiskWrite getInstance() {
        if (diskWrite == null) diskWrite = new DiskWrite();
        return diskWrite;
    }

    public void scheduleWrite(Page page) {
        int waitingTime = 1 + random.nextInt(5);
        PageWrite pageWrite = new PageWrite(page, waitingTime);
        for (PageWrite p : pagesToWrite) if (p.page == page) return;
        pagesToWrite.add(pageWrite);
    }

    public void runWrites() {
        for (Iterator<PageWrite> it = pagesToWrite.iterator(); it.hasNext();) {
            PageWrite pageWrite = it.next();
            pageWrite.timeUntilWrite--;
            if (pageWrite.timeUntilWrite == 0) {
                pageWrite.page.M = 1;
                it.remove();
            }
        }
    }
}
