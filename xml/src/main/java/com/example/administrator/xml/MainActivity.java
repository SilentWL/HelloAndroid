package com.example.administrator.xml;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ParserBtn = (Button)findViewById(R.id.Parser);

        ParserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                StringBuffer StrBuf = new StringBuffer();
                File file = new File("/data/offline.xml");

                try {
                    BufferedReader buf = new BufferedReader(new FileReader(file));
                    String line = null;

                    while ((line = buf.readLine()) != null){
                        StrBuf.append(line);
                    }
                    System.out.println(StrBuf.toString());
                    SAXParserFactory factory = SAXParserFactory.newInstance();
                    XMLReader reader = factory.newSAXParser().getXMLReader();
                    reader.setContentHandler(new MyContentHandler());
                    reader.parse(new InputSource(new StringReader(StrBuf.toString())));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
