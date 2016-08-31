package LrcProcessor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/4/9 0009.
 */
public class LrcProcessor {
    public ArrayList<Queue> process(InputStream inputStream){
        Queue<Long> timeMills = new LinkedList<Long>();
        Queue<String> lrcInfo = new LinkedList<String>();
        ArrayList<Queue> queue = new ArrayList<Queue>();

        try{
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "gb2312");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String result = "";
            String temp = null;

            int i  = 0;
            boolean b = true;
            Pattern p = Pattern.compile("^\\[(.*)\\]");
            while((temp = bufferedReader.readLine()) != null){
                i++;
                Matcher m = p.matcher(temp);
                if (m.find()){
                    String timeStr = m.group();
                    Long timeMill = time2Long(timeStr.substring(1, timeStr.length() - 1));
                    String msg = temp.substring(10);
                    result = result + msg + "\n";
                    if (b){
                        timeMills.add(timeMill);
                        lrcInfo.add(result);
                    }
                }
            }
            lrcInfo.add(result);
            queue.add(timeMills);
            queue.add(lrcInfo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return queue;
    }
    public Long time2Long(String timeString){
        String s[] = timeString.split(":");
        int min = Integer.parseInt(s[0]);
        String ss[] = s[1].split("\\.");
        int sec = Integer.parseInt(ss[0]);
        int mill = Integer.parseInt(ss[1]);
        return min * 60 * 1000 + sec * 1000 + mill * 10L;

    }
}
