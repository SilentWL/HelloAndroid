package DownloadService;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class FileUtils {
    private String SDPATH = null;

    public String GetSDPath(){
        return SDPATH;
    }

    public FileUtils(){
        SDPATH = Environment.getExternalStorageDirectory() + "/";
    }

    public File CreateSDFile(String filename) throws IOException{
        File file = new File(SDPATH + filename);
        file.createNewFile();
        return file;
    }
    public File CreateSDDir(String DirName) throws IOException{
        File dir = new File(SDPATH + DirName);
        dir.mkdir();
        return dir;
    }
    public boolean isFileExist(String FileName){
        File file = new File(SDPATH + FileName);
        return file.exists();
    }

    public File Write2SDFromInput(String Path, String FileName, InputStream Input){
        File file = null;
        OutputStream output = null;

        try{
            CreateSDDir(Path);
            file = CreateSDFile(Path + FileName);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4*1024];
            while(Input.read(buffer) != -1){
                output.write(buffer);
            }
            output.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try{
                output.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return  file;
    }

}
