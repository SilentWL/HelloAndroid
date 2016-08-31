package mp3download;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/4/5 0005.
 */
public class HttpDownload {
    public String DownLoad(String urlStr){
        StringBuffer StrBuf = new StringBuffer();
        String line = null;
        BufferedReader Buffer = null;
        HttpURLConnection urlConnn = null;
        URL url = null;

        try{
            url = new URL(urlStr);

            urlConnn = (HttpURLConnection)url.openConnection();
            urlConnn.getInputStream();
            Buffer = new BufferedReader(new InputStreamReader(urlConnn.getInputStream(), "gb2312"));

            while ((line = Buffer.readLine()) != null){
                StrBuf.append(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                urlConnn.disconnect();
                Buffer.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return StrBuf.toString();
    }
    public int DownloadFile(String urlStr, String path, String fileName){
        InputStream inputStream = null;
        try{
            FileUtils fileUtils = new FileUtils();

            if (fileUtils.isFileExist(path + fileName)){
                return 1;
            }else{
                inputStream = getInputStreamFromUrl(urlStr);
                File resultFile = fileUtils.Write2SDFromInput(path, fileName, inputStream);
                if (resultFile == null)
                    return -1;
            }
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }finally {
            try{
                inputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    public InputStream getInputStreamFromUrl(String urlStr)throws MalformedURLException,IOException {
        URL url = new URL(urlStr);
        HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();

        InputStream inputStream = urlConn.getInputStream();
        return inputStream;
    }
}
