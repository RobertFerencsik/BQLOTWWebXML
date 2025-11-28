package bqlotw.domparse.hu;

import java.io.File;
import java.io.IOException;

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

public class BQLOTWDomQuery {

    public static void queryXML(String filePath) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            XPath xpath = XPathFactory.newInstance().newXPath();
            
            printHeader("Könyvesbolt XML lekérdezése");

            // Query 1: Get all book titles published by 'Magvető Kiadó'
            startBlock("1. A 'Magvető Kiadó' által kiadott könyvek");
            String query1 = "//könyv[@adószám = //kiadó[név='Magvető Kiadó']/@adószám]/cím";
            NodeList bookTitles = (NodeList) xpath.compile(query1).evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < bookTitles.getLength(); i++) {
                System.out.println("- " + bookTitles.item(i).getTextContent());
            }
            endBlock();

            // Query 2: Get authors of books in the 'Fantasy' genre
            startBlock("2. 'Fantasy' műfajú könyvek szerzői");
            String query2 = "//író[@íróID = //szerzője[@isbn = //könyv[műfaj='Fantasy']/@isbn]/@íróID]/név";
            NodeList authorNames = (NodeList) xpath.compile(query2).evaluate(doc, XPathConstants.NODESET);
             for (int i = 0; i < authorNames.getLength(); i++) {
                Element nameElement = (Element) authorNames.item(i);
                System.out.println("- " + getText(nameElement, "vezetéknév") + " " + getText(nameElement, "keresztnév"));
            }
            endBlock();

            // Query 3: List books with stock less than 10
            startBlock("3. Könyvek, amelyek készlete < 10");
            String query3 = "//könyv[készlet < 10]";
            NodeList lowStockBooks = (NodeList) xpath.compile(query3).evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < lowStockBooks.getLength(); i++) {
                Element bookElement = (Element) lowStockBooks.item(i);
                System.out.println("- " + getText(bookElement, "cím") + " (Készlet: " + getText(bookElement, "készlet") + ")");
            }
            endBlock();

            // Query 4: Get sales with a total value > 5000
            startBlock("4. Eladások, amelyek értéke > 5000");
            String query4 = "//eladás[érték > 5000]";
            NodeList highValueSales = (NodeList) xpath.compile(query4).evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < highValueSales.getLength(); i++) {
                Element saleElement = (Element) highValueSales.item(i);
                System.out.println("- Eladás ID: " + saleElement.getAttribute("eladásID") + ", Érték: " + getText(saleElement, "érték"));
            }
            endBlock();

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        queryXML("BQLOTW_XML.xml");
    }
    
    // ===== Helper Functions =====

    private static void printHeader(String title) {
        System.out.println("========================================");
        System.out.println(title);
        System.out.println("========================================");
        System.out.println();
    }

    private static void startBlock(String title) {
        System.out.println("-------------- " + title + " --------------");
    }

    private static void endBlock() {
        System.out.println("------------------------------------------\n");
    }
    
    private static String getText(Element parent, String tagName) {
        if (parent == null) return "";
        NodeList nl = parent.getElementsByTagName(tagName);
        return (nl == null || nl.getLength() == 0 || nl.item(0).getTextContent() == null) ? "" : nl.item(0).getTextContent().trim();
    }
}