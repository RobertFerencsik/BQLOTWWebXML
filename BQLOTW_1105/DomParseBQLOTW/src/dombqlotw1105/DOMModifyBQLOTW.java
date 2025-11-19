package dombqlotw1105;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class DOMModifyBQLOTW {
    public static void main(String argv[]) {
        try {
            // XML file megnyitása
            URL xmlUrl = DOMModifyBQLOTW.class.getResource("BQLOTWhallgato.xml");
            if (xmlUrl == null) {
                throw new IOException("File is not found");
            }
            
            File xmlFile = new File(xmlUrl.getPath());
            
            // DocumentBuilderFactory példányosítása
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = factory.newDocumentBuilder();
            
            // DOM fa előállítása
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            
            System.out.println("Root: " + doc.getDocumentElement().getNodeName());
            
            // Hallgató elemek keresése
            NodeList nList = doc.getElementsByTagName("hallgato");
            
            // id="01" hallgató keresése és módosítása
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) nNode;
                    String hid = elem.getAttribute("id");
                    
                    if ("01".equals(hid)) {
                        System.out.println("\nHallgato id=" + hid + " found!");
                        
                        // Régi név kiíratása
                        String oldKeresztnev = elem.getElementsByTagName("keresztnev").item(0).getTextContent();
                        String oldVezeteknev = elem.getElementsByTagName("vezeteknev").item(0).getTextContent();
                        System.out.println("Old name: " + oldVezeteknev + " " + oldKeresztnev);
                        
                        // Új név beállítása
                        elem.getElementsByTagName("keresztnev").item(0).setTextContent("Robert");
                        elem.getElementsByTagName("vezeteknev").item(0).setTextContent("Ferencsik");
                        
                        // Új név kiíratása
                        String newKeresztnev = elem.getElementsByTagName("keresztnev").item(0).getTextContent();
                        String newVezeteknev = elem.getElementsByTagName("vezeteknev").item(0).getTextContent();
                        System.out.println("New name: " + newVezeteknev + " " + newKeresztnev);
                        
                        break;
                    }
                }
            }
            
            
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
