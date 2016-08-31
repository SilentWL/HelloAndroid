package xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

import mp3parser.Mp3Info;

/**
 * Created by Administrator on 2016/4/6 0006.
 */
public class Mp3ListContenthandler extends DefaultHandler{
    private List<Mp3Info> mp3Infos = null;
    private Mp3Info mp3Info = null;
    private String tagName = null;

    public Mp3ListContenthandler(List<Mp3Info> mp3Infos) {
        super();
        this.mp3Infos = mp3Infos;
    }

    public void setMp3Infos(List<Mp3Info> mp3Infos) {
        this.mp3Infos = mp3Infos;
    }

    public List<Mp3Info> getMp3Infos() {
        return mp3Infos;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tagName = localName;

        if (tagName.equals("resource")){
            mp3Info = new Mp3Info();
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        tagName = null;
        if (localName.equals("resource")){
            mp3Infos.add(mp3Info);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String temp = new String(ch, start, length);

        if (tagName != null) {
            if (tagName.equals("id")) {
                mp3Info.setId(temp);
            } else if (tagName.equals("mp3.name")) {
                mp3Info.setName(temp);
            } else if (tagName.equals("mp3.size")) {
                mp3Info.setSize(temp);
            } else if (tagName.equals("lrc.name")) {
                mp3Info.setLrcName(temp);
            } else if (tagName.equals("lrc.size")) {
                mp3Info.setLrcSize(temp);
            }
        }
    }
}
