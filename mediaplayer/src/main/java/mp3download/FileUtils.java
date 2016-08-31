package mp3download;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mp3parser.Mp3Info;

/**
 * Created by Administrator on 2016/4/5 0005.
 */
public class FileUtils {
    private String SDPATH = null;

    public String GetSDPath(){
        return SDPATH;
    }

    public FileUtils(){
        SDPATH = Environment.getExternalStorageDirectory() + "/";
    }

    public File CreateSDFile(String filename) throws IOException {
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
            int temp = 0;
            while((temp = Input.read(buffer)) != -1){
                output.write(buffer, 0, temp);
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
    public void GetMp3Infos(String path, List<Mp3Info> mp3Infos){
        File file = new File(SDPATH + File.separator + path);
        if (!file.exists()){
            return;
        }
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++){
            if (files[i].getName().endsWith(".mp3")){
                Mp3Info mp3Info = new Mp3Info();
                mp3Info.setName(files[i].getName());
                mp3Info.setSize(files[i].length() + "");
                mp3Infos.add(mp3Info);
            }
        }
        for (int i = 0; i < files.length; i++){
            if (files[i].getName().endsWith(".lrc")){
                for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext();){
                    Mp3Info mp3Info = null;
                    String Mp3Name = new String((mp3Info = (Mp3Info)iterator.next()).getName().substring(0, mp3Info.getName().length() - 4));
                    String TempLrcName = new String(files[i].getName().substring(0, files[i].getName().length() - 4));

                    if (TempLrcName.equals(Mp3Name)){
                        mp3Info.setLrcName(files[i].getName());
                        mp3Info.setLrcSize(files[i].length() + "");
                    }
                }
            }
        }
    }
}
