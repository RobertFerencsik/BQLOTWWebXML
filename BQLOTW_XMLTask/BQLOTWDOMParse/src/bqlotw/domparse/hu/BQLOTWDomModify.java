package bqlotw.domparse.hu;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BQLOTWDomModify {

    public static void modifyXML(String filePath) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // 1. Add a new book
            System.out.println("1. Adding a new book...");
            Element root = doc.getDocumentElement();
            
            Element newKonyv = doc.createElement("könyv");
            newKonyv.setAttribute("isbn", "978-1-23-456789-0");
            newKonyv.setAttribute("leltarID", "1");
            newKonyv.setAttribute("adoszam", "12345678");

            Element cim = doc.createElement("cím");
            cim.appendChild(doc.createTextNode("The Art of XML Parsing"));
            newKonyv.appendChild(cim);

            Element mufaj = doc.createElement("műfaj");
            mufaj.appendChild(doc.createTextNode("Technical"));
            newKonyv.appendChild(mufaj);

            Element ar = doc.createElement("ár");
            ar.appendChild(doc.createTextNode("7990.00"));
            newKonyv.appendChild(ar);

            Element keszlet = doc.createElement("készlet");
            keszlet.appendChild(doc.createTextNode("25"));
            newKonyv.appendChild(keszlet);

            root.appendChild(newKonyv);
            System.out.println("   New book added.");

            // 2. Modify the price of '1984'
            System.out.println("\n2. Modifying price for book '1984'...");
            NodeList konyvek = doc.getElementsByTagName("könyv");
            for (int i = 0; i < konyvek.getLength(); i++) {
                Node konyvNode = konyvek.item(i);
                if (konyvNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element konyvElement = (Element) konyvNode;
                    if ("978-0-14-028329-7".equals(konyvElement.getAttribute("isbn"))) {
                        NodeList arNodeList = konyvElement.getElementsByTagName("ár");
                        if (arNodeList.getLength() > 0) {
                            arNodeList.item(0).setTextContent("3999.00");
                            System.out.println("   Price updated for '1984'.");
                        }
                        break; 
                    }
                }
            }

            // 3. Add a new author
            System.out.println("\n3. Adding a new author...");
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
            nemzetiseg.setTextContent("American");
            newIro.appendChild(nemzetiseg);
            
            root.appendChild(newIro);
            System.out.println("   New author 'Frank Herbert' added.");

            // Write the content into a new xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("BQLOTW_XML_modified.xml"));
            transformer.transform(source, result);

            System.out.println("\nXML file updated successfully! Saved as BQLOTW_XML_modified.xml");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        modifyXML("BQLOTW_XML.xml");
    }
}