package dombqlotw1029;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;


public class DomRead {
    public static void main(String argv[]) throws SAXException, IOException, ParserConfigurationException {
        //XML file megnyitása
        URL xmlUrl = DomRead.class.getResource("/dombqlotw1029/BQLOTWhallgato.xml");
        if (xmlUrl == null) {
            throw new FileNotFoundException("Nem található a BQLOTWhallgato.xml a classpath-on: /dombqlotw1029/BQLOTWhallgato.xml");
        }

        //Példányosítás a DocumentBuilderFactory osztályt a statikus newInstance() metódussal
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder dBuilder = factory.newDocumentBuilder();

        //A DocumentumBuilderFactoryból megkapjuk a DocumentumBuildert
        //A DocumentBuilder tartalmaza az API-t a DOMdokumentum példányok XML dokumentmból való beszerzéséhez

        //DOM fa előállítása
        Document neptunkod = dBuilder.parse(xmlUrl.openStream());
        //A parse() metódus elemzi az XML fájlt és a dokumentumot adja vissza

        neptunkod.getDocumentElement().normalize();
        //A dokumentum normalizálása sehít a helyes eredmények elérésében
        //Eltávolítja az üres szövegcsomópontokat, és összekapcsolja a szomszédos szövegcsomópontokat

        System.out.println("Gyökérelem: " + neptunkod.getDocumentElement().getNodeName());
        //DOkumentum gyökér elem kiírása

        NodeList nList = neptunkod.getElementsByTagName("hallgato");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            System.out.println("\nAktuális elem: " + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String hid = elem.getAttribute("id");

                Node node1 = elem.getElementsByTagName("keresztnev").item(0);
                String kname = node1.getTextContent();

                Node node2 = elem.getElementsByTagName("vezeteknev").item(0);
                String vname = node2.getTextContent();

                Node node3 = elem.getElementsByTagName("foglalkozas").item(0);
                String fname = node3.getTextContent();

                System.out.println("Hallgató azonosító: " + hid);
                System.out.println("Keresztnév: " + kname);
                System.out.println("Vezetéknév: " + vname);
                System.out.println("Foglalkozás: " + fname);
            }
        }
    }
}