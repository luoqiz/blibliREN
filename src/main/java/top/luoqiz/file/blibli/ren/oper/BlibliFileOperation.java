package top.luoqiz.file.blibli.ren.oper;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import top.luoqiz.file.rename.AbstractFileOperation;
import top.luoqiz.file.thread.FileThreadPool;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

public class BlibliFileOperation extends AbstractFileOperation {


    public List<String> menuDirList(File file) {
        List<String> list = new ArrayList<>();
        if (!file.exists()) {
            return list;
        }

        File[] files = file.listFiles();

        for (File child : files) {
            String[] menuFile = file.list((File dir, String name) -> {
                return name.equals("desktop.ini");
            });
            if (menuFile.length > 0) {
                list.add(file.getAbsolutePath());
                break;
            }
            if (child.isFile()) {
                list.add(child.getAbsolutePath());
            } else if (child.isDirectory()) {
                list.addAll(menuDirList(child));
            }
        }

        return list;
    }

    public boolean renameItem(File file) throws IOException, InterruptedException {
        System.out.println("目录：" + file.getAbsolutePath() + "\t 线程：" + Thread.currentThread().getName() + "\t 更名工作开始");
        File[] listFiles = file.listFiles();
        CountDownLatch countDownLatch = new CountDownLatch(listFiles.length);
        for (File listFile : listFiles) {


            FileThreadPool.getExecutorService().execute(() -> {
                        countDownLatch.countDown();
                        System.out.println("\t 目录：" + listFile.getAbsolutePath() + "\t 线程：" + Thread.currentThread().getName() + "\t 开始执行");
                        if (listFile.isDirectory()) {
                            File[] itemList = listFile.listFiles();
                            String rename = null;
                            File video = null;
                            for (File file1 : itemList) {
                                if (Pattern.matches("(.*?)info$", file1.getName())) {
                                    BufferedReader bufferedReader = null;
                                    try {
                                        bufferedReader = new BufferedReader(new FileReader(file1));
                                        StringBuilder stringBuilder = new StringBuilder();

                                        String bb = null;
                                        while (true) {
                                            if (!((bb = bufferedReader.readLine()) != null)) break;
                                            stringBuilder.append(bb);
                                        }

                                        JSONObject json = JSONObject.parseObject(stringBuilder.toString());
                                        rename = json.getString("PartName");

                                        bufferedReader.close();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }

                                if (!Pattern.matches("(.*?)info$", file1.getName())) {
                                    video = file1;
                                }
                            }

                            if (rename != null && video != null) {
                                video.renameTo(new File(video.getParentFile().getParentFile().getAbsolutePath() + "\\" + rename + "." + video.getName().split("\\.")[1]));
                            }
                            try {
                                FileUtils.deleteDirectory(listFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        System.out.println("\t 目录：" + listFile.getAbsolutePath() + "\t 线程：" + Thread.currentThread().getName() + "\t 结束");
                    }
            );
        }
        countDownLatch.await();
        System.out.println("目录：" + file.getAbsolutePath() + "\t 线程：" + Thread.currentThread().getName() + "\t 更名工作结束");
        java.awt.Desktop.getDesktop().open(file);
        return true;
    }

}
