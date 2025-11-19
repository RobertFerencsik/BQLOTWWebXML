package xpathbqlotw;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class xPathQueryBQLOTW {
    public static void main(String[]args) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            try (InputStream xmlStream = xPathQueryBQLOTW.class.getClassLoader()
                    .getResourceAsStream("xpathbqlotw/studentBQLOTW.xml")) {
                if (xmlStream == null) {
                    System.err.println("Nem található a 'studentBQLOTW.xml' erőforrás a classpath-on.");
                    return;
                }

                Document document = documentBuilder.parse(xmlStream);

                document.getDocumentElement().normalize();

                XPath xPath = XPathFactory.newInstance().newXPath();

                //String expression = "/class/student";
                //String expression = "/class/student[@id='02']";
                //String expression = "//student";
                //String expression = "/class/student[2]";
                //String expression = "/class/student[last()]";
                //String expression = "/class/student[last()-1]";
                //String expression = "/class/student[position() <= 2]";
                //String expression = "/class/*";
                //String expression = "//student[@*]";
                //String expression = "//*";
                String expression = "/class/student[@kor > '20']";
                NodeList students = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);

                for (int i = 0; i < students.getLength(); i++) {
                    Node node = students.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        System.out.println("\nHallgató ID: " + element.getAttribute("id"));
                        System.out.println("Keresztnév: " + element.getElementsByTagName("keresztnev").item(0).getTextContent());
                        System.out.println("Vezetéknév: " + element.getElementsByTagName("vezeteknev").item(0).getTextContent());
                        System.out.println("Becenév: " + element.getElementsByTagName("becenev").item(0).getTextContent());
                        System.out.println("Kor: " + element.getElementsByTagName("kor").item(0).getTextContent());
                    }
                }
                
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.err.println("XML feldolgozási hiba: " + e.getMessage());
        } catch (XPathExpressionException e) {
            System.err.println("XPath kifejezés hiba: " + e.getMessage());
        }
    }
    
}
