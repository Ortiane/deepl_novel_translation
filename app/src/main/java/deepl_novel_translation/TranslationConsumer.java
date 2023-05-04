package deepl_novel_translation;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.deepl.api.SentenceSplittingMode;
import com.deepl.api.TextResult;
import com.deepl.api.TextTranslationOptions;
import com.deepl.api.Translator;

public class TranslationConsumer implements Runnable {

    private ConcurrentLinkedQueue<String> toTranslateQueue;
    private ConcurrentLinkedQueue<String> doneQueue;
    private Translator translator = new Translator(System.getenv("DEEPL_AUTH_KEY"));
    private TextTranslationOptions textTranslationOptions = new TextTranslationOptions();
    private final String CHAPTER_DELIMITER = "\n\n\n";

    public TranslationConsumer(ConcurrentLinkedQueue<String> toTranslateQueue,
            ConcurrentLinkedQueue<String> doneQueue) {
        this.toTranslateQueue = toTranslateQueue;
        this.doneQueue = doneQueue;
        this.textTranslationOptions.setSentenceSplittingMode(SentenceSplittingMode.All);
    }

    private void translate(String chapters) {
        String title = chapters.substring(0, chapters.indexOf("\n"));
        System.out.println(String.format("Translating: %s", title));
        StringBuilder translatedBuilder = new StringBuilder();
        for (String chapter : chapters.split(CHAPTER_DELIMITER)) {
            try {
                TextResult translatedText = this.translator.translateText(chapter, null, "en-US",
                        this.textTranslationOptions);
                translatedBuilder.append(translatedText.getText());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(String.format("Failed to translate: %s", title));
                return;
            }
        }
        this.doneQueue.add(translatedBuilder.toString());
        System.out.println(String.format("Translated: %s", title));
        return;
    }

    @Override
    public void run() {
        while (!this.toTranslateQueue.isEmpty()) {
            // Translate and add to doneQueue
            String chapters = this.toTranslateQueue.poll();
            this.translate(chapters);
        }
    }

}
