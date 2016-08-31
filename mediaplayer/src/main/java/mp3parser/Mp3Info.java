package mp3parser;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/6 0006.
 */
public class Mp3Info implements Serializable{
    private String id;
    private String name;
    private String size;
    private String lrcName;
    private String lrcSize;

    @Override
    public String toString() {
        return "Mp3Info{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", lrcName='" + lrcName + '\'' +
                ", lrcSize='" + lrcSize + '\'' +
                '}';
    }

    public String getLrcName() {
        return lrcName;
    }

    public String getLrcSize() {
        return lrcSize;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLrcName(String lrcName) {
        this.lrcName = lrcName;
    }

    public void setLrcSize(String lrcSize) {
        this.lrcSize = lrcSize;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getId() {

        return id;

    }
}
