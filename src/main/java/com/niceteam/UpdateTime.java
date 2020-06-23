package com.niceteam;

import java.io.*;
import java.nio.charset.Charset;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class UpdateTime {

    private static String nowdatestr;
    private static String filepath;
    private static String targetpath;
    private static List<String> source_filenames = new ArrayList<String>();
    private static List<String> filedir_names = new ArrayList<String>();
    /**
     * 读取配置文件
     * @return
     */
    private static void readConfigFile() {
        try {
            //InputStream in = UpdateTime.class.getClassLoader().getResource("resources"+File.separator+"config.properties").openStream();
            InputStream in = new FileInputStream(new File("conf"+File.separator+"config.properties"));
            Properties prop = new Properties();
            prop.load(in);
            filepath = prop.getProperty("source_path");
            targetpath = prop.getProperty("target_path");
            SimpleDateFormat nowdf = new SimpleDateFormat("yyyyMMdd");
            Date now = new Date();
            nowdatestr = nowdf.format(now);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getFile(String path, int deep,List<String> filenames){
        // 获得指定文件对象
        File file = new File(path);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();
        System.out.println(path);
        assert array != null;
        System.out.println(array[0]+"    array");
        for (File value : array) {
            if (value.isFile())//如果是文件
            {
                for (int j = 0; j < deep; j++){
                    String temp = value.getName().split("/")[value.getName().split("/").length-1];
                    filenames.add(temp);
                }//输出前置空格
                // 只输出文件名字

                //System.out.println(filenames);
                // 输出当前文件的完整路径
                // System.out.println("#####" + array[i]);
                // 同样输出当前文件的完整路径   大家可以去掉注释 测试一下
                // System.out.println(array[i].getPath());
            } else if (value.isDirectory())//如果是文件夹
            {
                for (int j = 0; j < deep; j++)//输出前置空格
                    //System.out.print(" ");

                //System.out.println(value.getName());
                //System.out.println(array[i].getPath());
                //文件夹需要调用递归 ，深度+1
                getFile(value.getPath(), deep + 1,source_filenames);
            }
        }
    }

    private static void createParentDirIfNotExist(File file){
        createDirIfNotExist(file.getParentFile());
    }

    private static void createDirIfNotExist(String path){
        File file = new File(path);
        createDirIfNotExist(file);
    }

    private static void createDirIfNotExist(File file){
        if(!file.exists()){
            file.mkdirs();
        }
    }

    private static void createFileIfNotExist(File file) throws IOException {
        createParentDirIfNotExist(file);
        file.createNewFile();
    }

    public static void unZip(String sourceFilename, String targetDir) throws IOException {
        unZip(new File(sourceFilename), targetDir);
    }

    /**
     * 将sourceFile解压到targetDir
     * @param sourceFile
     * @param targetDir
     * @throws RuntimeException
     */
    private static void unZip(File sourceFile, String targetDir) throws IOException {
        long start = System.currentTimeMillis();
        if (!sourceFile.exists()) {
            throw new FileNotFoundException("cannot find the file = " + sourceFile.getPath());
        }
        try (ZipFile zipFile = new ZipFile(sourceFile)) {
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    String dirPath = targetDir + "/" + entry.getName();
                    createDirIfNotExist(dirPath);
                } else {
                    File targetFile = new File(targetDir + "/" + entry.getName());
                    createFileIfNotExist(targetFile);
                    try (InputStream is = zipFile.getInputStream(entry); FileOutputStream fos = new FileOutputStream(targetFile)) {
                        int len;
                        byte[] buf = new byte[1024];
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                    }
                }
            }
        }
    }
//    private static void unZipFiles(File zipFile, String descDir) throws IOException {
//
//        ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));//解决中文文件夹乱码
//        String name = zip.getName().substring(zip.getName().lastIndexOf('\\')+1, zip.getName().lastIndexOf('.'));
//
//        File pathFile = new File(descDir+name);
//        if (!pathFile.exists()) {
//            pathFile.mkdir();
//        }
//
//        for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
//            ZipEntry entry = (ZipEntry) entries.nextElement();
//            String zipEntryName = entry.getName();
//            InputStream in = zip.getInputStream(entry);
//            String outPath = (descDir + zipEntryName.substring(0,zipEntryName.length()-4) +File.separator+ zipEntryName).replaceAll("\\*", File.separator);
//            System.out.println(descDir);
//            System.out.println(name);
//            System.out.println(zipEntryName);
//            // 判断路径是否存在,不存在则创建文件路径
//            File file = new File(descDir+name);
//            if (!pathFile.exists()) {
//                pathFile.mkdir();
//            }
//            // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
//            if (new File(outPath).isDirectory()) {
//                continue;
//            }
//            // 输出文件路径信息
//			System.out.println(outPath);
//
//            FileOutputStream out = new FileOutputStream(outPath);
//            byte[] buf1 = new byte[1024];
//            int len;
//            while ((len = in.read(buf1)) > 0) {
//                out.write(buf1, 0, len);
//            }
//            out.close();
//            in.close();
//        }
//        zip.close();
//    }

    private static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        assert files != null;
        for (File file : files) {
            //删除子文件
            if (file.isFile()) {
                flag = deleteFile(file.getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(file.getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        return dirFile.delete();
    }

    private static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            boolean a = file.delete();
            flag = true;
        }
        return flag;
    }

    private static boolean DeleteFolder(String sPath) {//删除文件还是文件夹 0:文件和文件夹    1：文件    2：文件夹
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return false;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else{  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    private static void getFileDirname(){
        for(String sourcefile:source_filenames){
            filedir_names.add(sourcefile.substring(0,sourcefile.length()-4));
        }
    }

    private static void createFile(String filePath) {
        try {
            File file = new File(filePath);
            // 判断文件是否存在
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateTxt(String txtpath,String txtname){
        FileWriter fw = null;
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
            int flag = 0;
            createFile(txtpath+"1"+txtname);
            fw = new FileWriter(txtpath+"1"+txtname, true);
            /* 读入TXT文件 */
            //String pathname = ""; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
            File filename = new File(txtpath+txtname); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = null;
            while((line = br.readLine()) != null) {
                String[] sline = line.split(":\\[",2);
                fw.write("{\"Data\":[");
                if(sline.length == 2) {
                    line = sline[1];
                    String[] str = line.split("],");
                    for (String item : str) {        //开始循环读取每条Json
                        if(flag==1){
                            fw.write(",");
                        }
                        flag=1;
                        String s = item.replace("[", "").replace("\"", "").replace("]]}", "");
                        String[] indicatorData = s.split(",");
                        if (indicatorData.length == 4) {
                            String timeStr = indicatorData[2].trim();

                            double time1 = Double.valueOf(timeStr);

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                            Date date = new Date((long)time1*1000);
                            String tim = sdf.format(date);
                            SimpleDateFormat nowdf = new SimpleDateFormat("yyyyMMdd");
                            Date now = new Date();
                            String nowdate = nowdf.format(now);
                            nowdate = nowdate + tim.substring(8);
                            Date newdate = sdf.parse(nowdate);
                            long newtime1 = newdate.getTime();
                            double newtime = newtime1/1000.0;
                            String need = String.format("%.1f",newtime);

                            fw.write("[\""+indicatorData[0].trim()+"\",\""+indicatorData[1].trim()+"\",\""+need+"\",\""+indicatorData[3].trim()+"\"]");

                        }
                    }
                    fw.write("]}");
                }
            }
            br.close();
            reader.close();

            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateFilename(String path,String name){
        File oldFile = new File(path+name);
        String rootPath = oldFile.getParent();
        File newFile = new File(rootPath + File.separator + newName(name.substring(1))+".txt");
        oldFile.renameTo(newFile);
    }

    public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure) throws RuntimeException{
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null ;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile,zos,sourceFile.getName(),KeepDirStructure);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) +" ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils",e);
        }finally{
            if(zos != null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws Exception{
        byte[] buf = new byte[2 * 1024];
        if(sourceFile.isFile()){
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if(listFiles == null || listFiles.length == 0){
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if(KeepDirStructure){
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + File.separator));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            }else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + File.separator + file.getName(),KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(),KeepDirStructure);
                    }
                }
            }
        }
    }

    private static String newName(String oldname){
        return oldname.split("-")[0].trim()+"-"+nowdatestr+oldname.split("-")[1].trim().substring(8)+"-"+nowdatestr+oldname.split("-")[1].trim().substring(8);
    }

    public static void main(String[] args) throws IOException {
        readConfigFile();

        getFile(filepath,1,source_filenames);

        System.out.println("******************开始解压********************");
        for (String filename : source_filenames) {
            String zippath = filepath + File.separator + filename;
            unZip(zippath,filepath+ File.separator + filename.substring(0,filename.length()-4));
        }
        System.out.println("******************解压完毕********************");

        for (String filename : source_filenames) {
            String zippath = filepath + File.separator + filename;
            DeleteFolder(zippath);
        }
        System.out.println("******************删除原始压缩包完毕********************");

        getFileDirname();

        System.out.println("******************开始修改文件********************");
        List<String> temp_txt = new ArrayList<>();
        for (String filename : filedir_names){
            temp_txt.clear();

            getFile(filepath + File.separator + filename,1,temp_txt);
            for (String txtname : temp_txt){
                updateTxt(filepath+File.separator+filename+File.separator,txtname);
                DeleteFolder(filepath+File.separator+filename+File.separator+txtname);
                updateFilename(filepath+File.separator+filename+File.separator,"1"+txtname);
                System.out.println(filepath+File.separator+filename+File.separator+txtname+"更新完成");
            }
            FileOutputStream fos1 = new FileOutputStream(new File(targetpath+File.separator+newName(filename)+".zip"));
            toZip(filepath+File.separator+filename, fos1,false);
            fos1.close();
            deleteDirectory(filepath+File.separator+filename);
            System.out.println(filepath+File.separator+filename+File.separator+" "+"时间更新完成");
        }
        System.out.println("******************修改文件完毕********************");

    }

}
