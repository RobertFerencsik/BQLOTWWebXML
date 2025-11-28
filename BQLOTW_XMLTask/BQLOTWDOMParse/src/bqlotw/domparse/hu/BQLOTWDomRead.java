package bqlotw.domparse.hu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BQLOTWDomRead {

    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
        File xmlFile = new File("BQLOTW_XML.xml");
        File outFile = new File("BQLOTWDomRead_output.txt");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        try (PrintWriter pw = new PrintWriter(new FileWriter(outFile, false))) {

            String root = doc.getDocumentElement().getNodeName();
            printlnBoth(pw, "Gyökér elem: " + root);
            printlnBoth(pw, "========================================");

            // Pre-process data to build maps for relationships
            Map<String, String> authorNames = new HashMap<>();
            NodeList irok = doc.getElementsByTagName("író");
            for (int i = 0; i < irok.getLength(); i++) {
                Element e = (Element) irok.item(i);
                String id = e.getAttribute("íróID");
                String lastName = getTextFromGrandchild(e, "név", "vezetéknév");
                String firstName = getTextFromGrandchild(e, "név", "keresztnév");
                authorNames.put(id, firstName + " " + lastName);
            }

            Map<String, String> authorIdsByIsbn = new HashMap<>();
            NodeList szerzoje = doc.getElementsByTagName("szerzője");
            for (int i = 0; i < szerzoje.getLength(); i++) {
                Element e = (Element) szerzoje.item(i);
                authorIdsByIsbn.put(e.getAttribute("isbn"), e.getAttribute("íróID"));
            }
            
            Map<String, String> bookTitlesByIsbn = new HashMap<>();
            NodeList konyvekForMap = doc.getElementsByTagName("könyv");
             for (int i = 0; i < konyvekForMap.getLength(); i++) {
                Element e = (Element) konyvekForMap.item(i);
                bookTitlesByIsbn.put(e.getAttribute("isbn"), getText(e, "cím"));
            }

            // ====== Könyvesbolt (Bookstore) ======
            NodeList konyvesboltok = doc.getElementsByTagName("könyvesbolt");
            for (int i = 0; i < konyvesboltok.getLength(); i++) {
                Element e = (Element) konyvesboltok.item(i);
                printlnBoth(pw, "\nKönyvesbolt (ID: " + e.getAttribute("üzletID") + ")");
                printlnBoth(pw, "  Cím: " + getText(e, "irányítószám") + " " + getText(e, "város") + ", " + getText(e, "utca") + " " + getText(e, "házszám"));
                printMultiTag(pw, e, "telefon", "  Telefon: ");
            }
            printlnBoth(pw, "\n----------------------------------------");


            // ====== Könyvek (Books) with Authors ======
            printlnBoth(pw, "\nKészlet (Könyvek és Szerzők)");
            NodeList konyvek = doc.getElementsByTagName("könyv");
            for (int i = 0; i < konyvek.getLength(); i++) {
                Element e = (Element) konyvek.item(i);
                String isbn = e.getAttribute("isbn");
                String authorId = authorIdsByIsbn.get(isbn);
                String authorName = authorNames.getOrDefault(authorId, "Ismeretlen szerző");

                printlnBoth(pw, "\n  Könyv: " + getText(e, "cím") + " (ISBN: " + isbn + ")");
                printlnBoth(pw, "    Szerző: " + authorName);
                printMultiTag(pw, e, "műfaj", "    Műfaj: ");
                printlnBoth(pw, "    Ár: " + getText(e, "ár"));
                printlnBoth(pw, "    Készlet: " + getText(e, "készlet"));
            }
            printlnBoth(pw, "\n----------------------------------------");


            // ====== Eladások (Sales) with Books ======
            printlnBoth(pw, "\nEladási adatok");
            NodeList eladasok = doc.getElementsByTagName("eladás");
            Map<String, List<String>> booksInSale = new HashMap<>();
            NodeList tartalmaz = doc.getElementsByTagName("tartalmaz");
            for(int i=0; i<tartalmaz.getLength(); i++){
                Element e = (Element) tartalmaz.item(i);
                String saleId = e.getAttribute("eladásID");
                String isbn = e.getAttribute("isbn");
                booksInSale.computeIfAbsent(saleId, k -> new ArrayList<>()).add(bookTitlesByIsbn.get(isbn));
            }

            for (int i = 0; i < eladasok.getLength(); i++) {
                Element e = (Element) eladasok.item(i);
                String saleId = e.getAttribute("eladásID");
                printlnBoth(pw, "\n  Eladás (ID: " + saleId + ")");
                printlnBoth(pw, "    Dátum: " + getText(e, "dátum"));
                printlnBoth(pw, "    Érték: " + getText(e, "érték"));
                printlnBoth(pw, "    Kedvezmény: " + getText(e, "kedvezmény"));
                
                List<String> soldBooks = booksInSale.get(saleId);
                if(soldBooks != null && !soldBooks.isEmpty()){
                    printlnBoth(pw, "    Tételek:");
                    for(String title : soldBooks){
                        printlnBoth(pw, "      - " + title);
                    }
                }
            }
            printlnBoth(pw, "\n----------------------------------------");


            printlnBoth(pw, "\n--- Mentve a fájlba: " + outFile.getName() + " ---");
        }
    }

    private static String getText(Element parent, String tagName) {
        if (parent == null) return "";
        NodeList nl = parent.getElementsByTagName(tagName);
        return (nl == null || nl.getLength() == 0 || nl.item(0).getTextContent() == null) ? "" : nl.item(0).getTextContent().trim();
    }
    
    private static void printMultiTag(PrintWriter pw, Element parent, String tagName, String prefix){
         NodeList nl = parent.getElementsByTagName(tagName);
        for(int i=0; i<nl.getLength(); i++){
            printlnBoth(pw, prefix + nl.item(i).getTextContent().trim());
        }
    }

    private static String getTextFromGrandchild(Element parent, String childTagName, String grandchildTagName) {
        if (parent == null) return "";
        NodeList childNodeList = parent.getElementsByTagName(childTagName);
        if (childNodeList != null && childNodeList.getLength() > 0) {
            Node childNode = childNodeList.item(0);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                return getText((Element) childNode, grandchildTagName);
            }
        }
        return "";
    }

    private static void printlnBoth(PrintWriter pw, String line) {
        System.out.println(line);
        pw.println(line);
    }
}