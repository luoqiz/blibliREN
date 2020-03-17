package top.luoqiz.file.blibli.ren;

import top.luoqiz.file.blibli.ren.oper.BlibliFileOperation;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 在哔哩哔哩站下载的视频是基于blibli的软件播放的，若是使用其他播放器则无法直观的了解视频内容
 * 基于此做了移动和重命名处理
 * 注意：源文件会被删除。
 */
public class Main {
    public static void main(String[] args) throws IOException {

        BlibliFileOperation blibliFileOperation =new BlibliFileOperation();
        //哔哩哔哩下载的文件根目录
        File file = new File("E:\\85");
        List<String> rs = blibliFileOperation.menuDirList(file);
        rs.forEach(s -> {
            try {
                blibliFileOperation.renameItem(new File(s));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
