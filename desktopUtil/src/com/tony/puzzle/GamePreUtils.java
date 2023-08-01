package com.tony.puzzle;

import com.kw.gdx.zip.PackZip;

public class GamePreUtils {
    static String basepath = "pre/";
    public static void main(String[] args) {
        PackZip zip = new PackZip();
        //原始 目录资源复制到genMd5目录
        for (int i = 148; i <= 150; i++) {
            zip.copyOutToTemp("origin/"+basepath,"genMd5Dir/");
        }
        //生成md5
        zip.genMd5("genMd5Dir/");
        //打包
        zip.step3("genMd5Dir/","mdZip/"+basepath);
        zip.delete("genMd5Dir");
//        //下载
//        //复制到temp目录
//        zip.copyOutToTemp("mdZip/","temp/");
//        zip.delete("mdZip");
//        //解压
//        zip.unpackZip("temp/","unzipFile/");
//        zip.delete("temp");
////        //检查
//        for (int i = 1; i <= 2; i++) {
//            zip.check("unzipFile/level"+i);
//        }
////        //复制到目标目录
//        zip.copyOutToTemp("unzipFile/","level/");
//        zip.delete("unzipFile");
    }
}
