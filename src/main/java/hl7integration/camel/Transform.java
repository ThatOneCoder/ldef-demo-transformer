package hl7integration.camel;//package src.main.java;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Transform {

    public void transformXML() throws TransformerConfigurationException, TransformerException
    {
        try {
            String inputXSL = "C:\\!!Dev\\!!!!output\\xform\\in\\labid.xsl";
            String dataXML = "C:\\!!Dev\\!!!!output\\xform\\in\\source.xml";
            String outputHTML = "C:\\!!Dev\\!!!!output\\xform\\out\\labid.xml";

            TransformerFactory factory = TransformerFactory.newInstance();
            StreamSource xslStream = new StreamSource(inputXSL);
            Transformer transformer = factory.newTransformer(xslStream);
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","2");
            StreamSource in = new StreamSource(dataXML);
            StreamResult out = new StreamResult(outputHTML);
            transformer.transform(in, out);
            System.out.println("The generated XML file is:" + outputHTML);
            // String xmlString = out.getWriter().toString();
            //System.out.println(xmlString);

        } catch(Exception e) {
            System.out.println("Can't convert XML");
        }
    }

    public void transformXML(String inputXSL, String dataXML, String outputHTML ) throws TransformerConfigurationException, TransformerException
    {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            StreamSource xslStream = new StreamSource(inputXSL);
            Transformer transformer = factory.newTransformer(xslStream);
            transformer.setOutputProperty(OutputKeys.INDENT,"yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","2");
            StreamSource in = new StreamSource(dataXML);
            StreamResult out = new StreamResult(outputHTML);
            transformer.transform(in, out);
            System.out.println("The generated XML file is:" + outputHTML);
            // String xmlString = out.getWriter().toString();
            //System.out.println(xmlString);

        } catch(Exception e) {
            System.out.println("Can't convert XML");
        }
    }


}