import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private HashMap<String, HashMap<Integer, HashMap<String, PageEntry>>> index = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) {
        try {
            for (File file : pdfsDir.listFiles()) {
                var doc = new PdfDocument(new PdfReader(file));
                HashMap<Integer, HashMap<String, PageEntry>> tempPage = new HashMap<>();
                for (int numberPage = 1; numberPage <= doc.getNumberOfPages(); numberPage++) {
                    var text = PdfTextExtractor.getTextFromPage(doc.getPage(numberPage));
                    var words = text.split("\\P{IsAlphabetic}+");
                    HashMap<String, PageEntry> tempWord = new HashMap<>();
                    for (String word : words) {
                        word = word.toLowerCase();
                        if (tempWord.containsKey(word)) {
                            tempWord.get(word).incrementCount();
                        } else {
                            tempWord.put(word, new PageEntry(file.getName(), numberPage));
                        }
                    }
                    tempPage.put(numberPage, tempWord);
                }
                index.put(file.getName(), tempPage);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        word = word.toLowerCase();
        List<PageEntry> pageEntries = new ArrayList<>();
        for (Map.Entry<String, HashMap<Integer,  HashMap<String, PageEntry>>> entryFile : index.entrySet()) {
            for (Map.Entry<Integer, HashMap<String, PageEntry>> entryPage : entryFile.getValue().entrySet()) {
                if (entryPage.getValue().containsKey(word)) {
                    pageEntries.add(entryPage.getValue().get(word));
                }
            }
        }
        Collections.sort(pageEntries);
        return pageEntries.size() > 0 ? pageEntries : Collections.emptyList();
    }
}
