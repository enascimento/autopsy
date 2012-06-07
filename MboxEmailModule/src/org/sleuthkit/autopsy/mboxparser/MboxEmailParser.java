package org.sleuthkit.autopsy.mboxparser;

import java.io.*;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mbox.MboxParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class MboxEmailParser {
    
    
    private InputStream stream;
    //Tika object
    private Tika tika;
    private Metadata metadata;
    private ContentHandler contentHandler;
    private String mimeType;
    
    public MboxEmailParser(String filepath) 
    {
        this.tika = new Tika();
        this.stream = this.getClass().getResourceAsStream(filepath);
    }
    
    private void init() throws IOException
    {        
        this.metadata = new Metadata();        
        this.mimeType = tika.detect(this.stream);     
    }
    
    public void parse() throws FileNotFoundException, IOException, SAXException, TikaException
    {   
        init();
       // this.metadata = new Metadata();
        
        //String mimeType = tika.detect(this.stream);
        this.contentHandler = new BodyContentHandler();
        Parser parser = new MboxParser();
        ParseContext context = new ParseContext();
        
        //Set MIME Type
        //Seems like setting this causes the metadata not to output all of it.
        this.metadata.set(Metadata.CONTENT_TYPE, this.mimeType);
        
        parser.parse(this.stream,this.contentHandler, this.metadata, context);
    }
    
    public Metadata getMetadata()
    {
        return this.metadata;
    }
    
    //Returns message content, i.e. plain text or html
    public String getContent()
    {
        return this.contentHandler.toString();
    }
    
    public String detectEmailFileFormat(String filepath) throws IOException
    {
        return this.tika.detect(filepath);
    }
    
    //Detects the mime type from the first few bytes of the document
    public String detectMediaTypeFromBytes(byte[] firstFewBytes, String inDocName)
    {
        return this.tika.detect(firstFewBytes, inDocName);
    }
}
