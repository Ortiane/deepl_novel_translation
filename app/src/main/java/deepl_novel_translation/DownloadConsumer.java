package deepl_novel_translation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

public class DownloadConsumer implements Runnable {

    private ConcurrentLinkedQueue<String> toDownloadQueue;
    private ConcurrentLinkedQueue<String> doneQueue;
    private DbxClientV2 client;

    public DownloadConsumer(ConcurrentLinkedQueue<String> toDownloadQueue,
            ConcurrentLinkedQueue<String> doneQueue, DbxClientV2 client) {
        this.toDownloadQueue = toDownloadQueue;
        this.doneQueue = doneQueue;
        this.client = client;
        return;
    }

    private void downloadChapter(String chapterPath) {
        System.out.println("Downloading: " + chapterPath);
        ByteArrayOutputStream file_content = new ByteArrayOutputStream();
        try {
            DbxDownloader<FileMetadata> downloader = this.client.files().download(chapterPath);
            downloader.download(file_content);
            this.doneQueue.add(chapterPath + "\n" + file_content.toString());
        } catch (DbxException | IOException e) {
            System.out.println(String.format("Failed to download: %s", chapterPath));
            e.printStackTrace();
        }
        return;
    }

    //
    @Override
    public void run() {
        while (!this.toDownloadQueue.isEmpty()) {
            // Translate and add to doneQueue
            String chapterPath = this.toDownloadQueue.poll();
            downloadChapter(chapterPath);
        }
        return;
    }

}
