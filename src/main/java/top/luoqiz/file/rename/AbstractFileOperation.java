package top.luoqiz.file.rename;

import java.io.File;

public abstract class AbstractFileOperation implements FileOperation {
    @Override
    public void readerMenu(String scanDir) {

    }

    @Override
    public boolean renameItem(File file) {
        return false;
    }
}
