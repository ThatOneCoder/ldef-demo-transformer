package hl7integration.camel;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.parser.DefaultXMLParser;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.XMLParser;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mongodb.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.replace;
import static org.apache.commons.lang.StringUtils.replaceEachRepeatedly;

public class XMLBuilder {

    public void process() throws IOException {

        Multimap adtList = getADTList();

        try {
            appendORUsToADT(adtList);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    public Multimap getADTList() throws IOException {

//        // create cluster
//        Cluster cluster;
//
//        // create session
//        Session session = null;
//
//        String cassandraIp = getPropValues("cassandra-ip");
//        String keyspace = getPropValues("cassandra-keyspace");
//
//        cluster = Cluster.builder().addContactPoint(cassandraIp).build();
////        cluster = Cluster.builder().addContactPoint("10.32.227.87").build();
//        session = cluster.connect(keyspace);
////        cluster = Cluster.builder().addContactPoint("10.32.227.87").build();
//        System.out.println("connected to keyspace " + keyspace + " at IP " + cassandraIp);


        String mongoIp = (System.getenv("MONGODB_SERVICE_HOST")== null) ? getPropValues("mongo-ip") : System.getenv("MONGODB_SERVICE_HOST");
//
//        cluster = Cluster.builder().addContactPoint(cassandraIp).build();
////        cluster = Cluster.builder().addContactPoint("10.32.227.87").build();
//        session = cluster.connect(keyspace);
////        cluster = Cluster.builder().addContactPoint("10.32.227.87").build();
////        System.out.println("connected to keyspace " + keyspace + " at IP " + cassandraIp);


        // create mongo client
        MongoClient mongo = new MongoClient( mongoIp , 27017 );

        // get mongo db
        DB db = mongo.getDB("ldef_hl7_demo");

        // get mongo collection (aka table)
        DBCollection table = db.getCollection("message");

//        BasicDBObject mongoQuery = new BasicDBObject();
//        mongoQuery.put("status", "TRANSFORM");
//        mongoQuery.put("type", "ADT");

        DBObject mongoQuery = new QueryBuilder()
                .start()
                .and(new QueryBuilder().start().put("status").is("TRANSFORM").get(),
                        new QueryBuilder().start().put("type").is("ADT").get())
                .get();

        DBCursor cursor = table.find(mongoQuery);

        System.out.println(cursor.size());


        Multimap<String, String> adtList = ArrayListMultimap.create();


//        //TODO: query cassandra to get all IDs (episode) from ADT messages in XML form
//
//        String query = ("SELECT episode, message FROM message WHERE status = 'TRANSFORM' AND type= 'ADT' ALLOW FILTERING;");
//
//        ResultSet results = session.execute(query);

        while (cursor.hasNext()) {
            BasicDBObject obj = (BasicDBObject) cursor.next();
            String id = obj.getString("episode");
            String xml = obj.getString("message");
            adtList.put(id, xml);
            System.out.println(id);
        }

//        for (Row row : results) {
//            String id = row.getString("episode");
//            String xml = row.getString("message");
//            adtList.put(id, xml);
//            System.out.println(id);
//        }

        // close cluster
//        cluster.close();

        return adtList;
    }


    public void appendORUsToADT(Multimap adtList) throws DocumentException, IOException {

        // combined directory location
        String comb_dir = getPropValues("combined-xml-dir");

        // get multimap keys
        Set<String> ids = adtList.keySet();

        // create cluster
//        Cluster cluster;
//
//        // create session
//        Session session = null;
//
//        String cassandraIp = getPropValues("cassandra-ip");
//        String keyspace = getPropValues("cassandra-keyspace");
//
//        cluster = Cluster.builder().addContactPoint(cassandraIp).build();
////        cluster = Cluster.builder().addContactPoint("10.32.227.87").build();
//        try {
//            session = cluster.connect(keyspace);
//        } catch (Exception e) {
//            System.out.println("problem connecting to keyspace " + keyspace + " at IP " + cassandraIp + ": " + e);
//        }
////        cluster = Cluster.builder().addContactPoint("10.32.227.87").build();
//        System.out.println("connected to keyspace " + keyspace + " at IP " + cassandraIp);

        // create SAX Reader

        String mongoIp = (System.getenv("MONGODB_SERVICE_HOST")== null) ? getPropValues("mongo-ip") : System.getenv("MONGODB_SERVICE_HOST");

        // create mongo client
        MongoClient mongo = new MongoClient( mongoIp , 27017 );

        // get mongo db
        DB db = mongo.getDB("ldef_hl7_demo");

        // get mongo collection (aka table)
        DBCollection table = db.getCollection("message");



        SAXReader reader = new SAXReader();

        for (String id : ids) {

            // get collection of xmls (should always be 1 because we should always only get an ADT message with the same ID once)
            Collection<String> xmlList = adtList.get(id);

            // get ADT xml data
            String adtXml = null;
            try {
                adtXml = convertHL7ToXML(xmlList.iterator().next());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // create ADT Document to parse
            Document adtDocument = DocumentHelper.parseText(adtXml); //read(adtXml);

            // define ADT root element (A03)
            Element adtRoot = adtDocument.getRootElement();

            DBObject mongoQuery = new QueryBuilder()
                    .start()
                    .and(new QueryBuilder().start().put("status").is("TRANSFORM").get(),
                            new QueryBuilder().start().put("type").is("ORU").get(),
                            new QueryBuilder().start().put("id").is(id).get())
                    .get();

            DBCursor cursor = table.find(mongoQuery);

            //TODO: query cassandra for all ORU's relative to each ADT XML message

//            String query = ("SELECT * FROM message WHERE status = 'TRANSFORM' AND type= 'ORU' AND episode = '" + id + "' ALLOW FILTERING;");
//
//            ResultSet results = session.execute(query);


            while (cursor.hasNext()) {
                BasicDBObject obj = (BasicDBObject) cursor.next();
                String oruXml = null;
                try {
                    oruXml = convertHL7ToXML(obj.getString("message"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // create ORU Document to parse
                Document oruDocument = DocumentHelper.parseText(oruXml);

                // define ORU root element
                Element oruRoot = oruDocument.getRootElement();

//                System.out.println(oruRoot.toString());


                for (Iterator i = oruRoot.elementIterator(); i.hasNext(); ) {
                    Element element = (Element) i.next();
//                    System.out.println(element.toString());
                }

//                String el = oruRoot.elements("ORU_R01.PATIENT_RESULT").get(0).toString();
//                System.out.println(el);

                Element pr = oruRoot.element("ORU_R01.PATIENT_RESULT");
//                System.out.println(pr.toString());

                Element oo = pr.element("ORU_R01.ORDER_OBSERVATION");
//                System.out.println(oo);

//                Element oo2 = adtRoot.addElement("ORU_R01.ORDER_OBSERVATION");
//                System.out.println(oo.asXML());
                adtRoot.addText(oo.asXML());

//                System.out.println(oo.asXML());
//                adtRoot.element("ORU_R01.ORDER_OBSERVATION");

//                Node node = oruRoot.selectSingleNode("ORU_R01//ORU_R01.PATIENT_RESULT");
//                List<Node> nodes = oruDocument.selectNodes("MSH");// /ORU_R01.PATIENT_RESULT/ORU_R01.ORDER_OBSERVATION");

//                System.out.println(nodes.get(0).toString());

//                System.out.println(node.toString());

            }

            // html escape the combined XML because the addText dom4j function html encodes the text
            String combinedXml = StringEscapeUtils.unescapeHtml(adtDocument.asXML());

            // remove  xmlns="urn:hl7-org:v2xml" from the document because it breaks the XSL transformation
            String cleanCombinedXml = replaceEachRepeatedly(combinedXml, new String[]{" xmlns=\"urn:hl7-org:v2xml\""}, new String[]{""});

            String enclosedCombinedXml = replace(cleanCombinedXml, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<?xml-stylesheet type=\"text/xsl\" href=\"labid.xsl\"?>" +
                    "\n<rows>") + "\n</rows>";

            enclosedCombinedXml = replace(enclosedCombinedXml, "^~\\&", "^~\\&amp;");

            System.out.println(enclosedCombinedXml);
            try {
                FileUtils.writeStringToFile(new File(comb_dir +"/" + id + "-source.xml"), enclosedCombinedXml);
                System.out.println("wrote to " + comb_dir + "/" + id + ".xml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getPropValues(String property) throws IOException {
        String result = "";
        InputStream inputStream = null;

        try {
            Properties prop = new Properties();
            String propFileName = "endpoint.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // get the property value and print it out
            result = prop.getProperty(property);

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            inputStream.close();
        }
        return result;
    }

    public String convertHL7ToXML(String in) throws Exception {
        String xml = "";
        HapiContext context = new DefaultHapiContext();
        Parser pipeParser = context.getPipeParser();
        XMLParser xmlParser = new DefaultXMLParser();

        ca.uhn.hl7v2.model.Message message = pipeParser.parse(in);

        try {
            xml = xmlParser.encode(message);
        } catch (EncodingNotSupportedException e) {
            e.printStackTrace();
        } catch (HL7Exception e) {
            e.printStackTrace();
        }

        return xml;
    }
}
