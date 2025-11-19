package dombqlotw1105;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class DOMModify1BQLOTW {
    public static void main(String argv[]) {
        try {
            // XML file megnyitása - direkt file path használata a mentéshez
            // Ha a file a src mappában van, használd a relatív path-ot
            File xmlFile = new File("BQLOTW_orarend.xml");
            
            // Ellenőrzés, hogy a file létezik-e
            if (!xmlFile.exists()) {
                // Alternatíva: classpath-ból olvasás
                URL xmlUrl = DOMModify1BQLOTW.class.getResource("/dombqlotw1105/BQLOTW_orarend.xml");
                if (xmlUrl == null) {
                    throw new IOException("File is not found: src/dombqlotw1105/BQLOTW_orarend.xml");
                }
                xmlFile = new File(xmlUrl.getPath());
            }
            
            // DocumentBuilderFactory példányosítása
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            
            // DOM fa előállítása
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            
            System.out.println("Root: " + doc.getDocumentElement().getNodeName());
            
            // Ora elemek keresése
            NodeList nList = doc.getElementsByTagName("ora");
            
            // id="01" ora keresése és módosítása
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) nNode;
                    String oid = elem.getAttribute("id");
                    
                    if ("01".equals(oid)) {
                        System.out.println("\nOra id=" + oid + " found!");
                        
                        // m létrehozása (például megjegyzes)
                        Element uj_elem = doc.createElement("oraado");
                        uj_elem.setTextContent("Robert Ferencsik");
                        
                        // Új elem hozzáadása az ora elemhez
                        elem.appendChild(uj_elem);
                        
                        System.out.println("New element added: <oraado>Robert Ferencsik</oraado>");
                        
                        break;
                    }
                }
            }
            
            // Módosított dokumentum mentése
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);
            
            System.out.println("\nModified succesfully!");
            
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
