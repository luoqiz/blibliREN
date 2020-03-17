package top.luoqiz.file.rename;

import java.io.File;

public interface FileOperation {

    void readerMenu(String scanDir);

    boolean renameItem(File file);
}
