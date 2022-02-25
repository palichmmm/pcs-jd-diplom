import java.util.Objects;

public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    protected int count;

    public PageEntry(String pdfName, int page) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = 1;
    }

    public String getPdfName() {
        return pdfName;
    }

    public int getPage() {
        return page;
    }

    public int getCount() {
        return count;
    }

    public PageEntry incrementCount() {
        count += 1;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageEntry pageEntry = (PageEntry) o;
        return page == pageEntry.page && Objects.equals(pdfName, pageEntry.pdfName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pdfName, page);
    }

    @Override
    public int compareTo(PageEntry o) {
        if (this.count < o.count) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return "PageEntry{" +
                "pdfName='" + pdfName + '\'' +
                ", page=" + page +
                ", count=" + count +
                '}' + "\n";
    }
}
