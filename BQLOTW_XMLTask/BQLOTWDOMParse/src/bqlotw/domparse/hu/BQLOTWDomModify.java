package bqlotw.domparse.hu;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BQLOTWDomModify {

    public static void modifyXML(String filePath) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // 1. Add a new book
            System.out.println("1. Új könyv hozzáadása...");
            Element root = doc.getDocumentElement();
            
            Element newKonyv = doc.createElement("könyv");
            newKonyv.setAttribute("adószám", "12345678");
            newKonyv.setAttribute("isbn", "978-1-23-456789-0");
            newKonyv.setAttribute("leltárID", "1");

            Element cim = doc.createElement("cím");
            cim.appendChild(doc.createTextNode("Az XML-feldolgozás művészete"));
            newKonyv.appendChild(cim);

            Element mufaj = doc.createElement("műfaj");
            mufaj.appendChild(doc.createTextNode("Műszaki"));
            newKonyv.appendChild(mufaj);

            Element ar = doc.createElement("ár");
            ar.appendChild(doc.createTextNode("7990.00"));
            newKonyv.appendChild(ar);

            Element keszlet = doc.createElement("készlet");
            keszlet.appendChild(doc.createTextNode("25"));
            newKonyv.appendChild(keszlet);

            // A 'könyv' elemeket a 'szerzője' elemek elé kell beszúrni
            NodeList szerzojeList = doc.getElementsByTagName("szerzője");
            Node firstSzerzoje = szerzojeList.getLength() > 0 ? szerzojeList.item(0) : null;
            root.insertBefore(newKonyv, firstSzerzoje);
            System.out.println("   Új könyv hozzáadva.");

            // 2. Modify the price of '1984'
            System.out.println("\n2. Az '1984' című könyv árának módosítása...");
            NodeList konyvek = doc.getElementsByTagName("könyv");
            for (int i = 0; i < konyvek.getLength(); i++) {
                Node konyvNode = konyvek.item(i);
                if (konyvNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element konyvElement = (Element) konyvNode;
                    if ("978-0-14-028329-7".equals(konyvElement.getAttribute("isbn"))) {
                        NodeList arNodeList = konyvElement.getElementsByTagName("ár");
                        if (arNodeList.getLength() > 0) {
                            arNodeList.item(0).setTextContent("3999.00");
                            System.out.println("   Az '1984' ára frissítve.");
                        }
                        break; 
                    }
                }
            }

            // 3. Add a new author
            System.out.println("\n3. Új szerző hozzáadása...");
            Element newIro = doc.createElement("író");
            newIro.setAttribute("íróID", "6");

            Element nev = doc.createElement("név");
            Element vezeteknev = doc.createElement("vezetéknév");
            vezeteknev.setTextContent("Herbert");
            Element keresztnev = doc.createElement("keresztnév");
            keresztnev.setTextContent("Frank");
            nev.appendChild(vezeteknev);
            nev.appendChild(keresztnev);
            newIro.appendChild(nev);
            
            Element szuletesiDatum = doc.createElement("születésiDátum");
            szuletesiDatum.setTextContent("1920-10-08");
            newIro.appendChild(szuletesiDatum);

            Element nemzetiseg = doc.createElement("nemzetiség");
            nemzetiseg.setTextContent("Amerikai");
            newIro.appendChild(nemzetiseg);
            
            // Az 'író' elemeket a 'tartalmaz' elemek elé kell beszúrni
            NodeList tartalmazList = doc.getElementsByTagName("tartalmaz");
            Node firstTartalmaz = tartalmazList.getLength() > 0 ? tartalmazList.item(0) : null;
            root.insertBefore(newIro, firstTartalmaz);
            System.out.println("   Új szerző, 'Frank Herbert' hozzáadva.");

            // Write the content into a new xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("BQLOTW_XML_modified.xml"));
            transformer.transform(source, result);

            System.out.println("\nAz XML fájl sikeresen frissítve! Mentve BQLOTW_XML_modified.xml néven.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        modifyXML("BQLOTW_XML.xml");
    }
}