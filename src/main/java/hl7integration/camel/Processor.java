package hl7integration.camel;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import static org.apache.commons.lang.StringUtils.replace;

@Component
public class Processor {

    public void generateCDAFiles() throws Exception {

        //TODO: query cassandra for ADT XML files and all related ORU files
        //TODO: append relative ORU data within each of ADT file
        //TODO: remove the namespace reference in the parent A03 tag
        //TODO: transform each combined XML (ADT+ORU+ORU+...) into a CDA

        /**/

        String stopwatchStart = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(new Date());

        // file/directory locations for the app
//        String labid_file = getPropValues("labid-file") + ts;
//        String comb_dir = getPropValues("combined-xml-dir") + ts;
//        String mal_dir = getPropValues("malformed-xml-dir") + ts;
//        String cda_dir = getPropValues("cda-dir") + ts;

        String labid_file = getPropValues("labid-file");
        String haidisplay_file = getPropValues("haidisplay-file");
        String comb_dir = getPropValues("combined-xml-dir");
        String mal_dir = getPropValues("malformed-xml-dir");
        String cda_dir = getPropValues("cda-dir") + "/" + stopwatchStart;

        // hai-display must be in the same directory as the final CDAs for them to be displayed properly
        File sourceHai = new File(haidisplay_file);
        File targetHai = new File(cda_dir + "/hai-display.xsl");
        FileUtils.copyFile(sourceHai, targetHai);

        // create new XML Builder
        XMLBuilder xmlBuilder = new XMLBuilder();

        // creates ADT + ORU XML files in src/main/resources/ directory
        xmlBuilder.process();

        // get list of combined XML (ADT) files
        File dir = new File(comb_dir);
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                // Transform XML files using an XSL file to CDA-like files

                //src/main/resources/combinedXML/[id]-source.xml
                String relPath = file.getPath();
                //[id]-source.xml
                String filename = file.getName();
                //2.16.840.1.114222.4.1.646522
                String id = filename.substring(0, filename.length() - 11);
                //                System.out.println(relPath);
                //                System.out.println(filename);
                //                System.out.println(id);


                String inputXSL = labid_file;
                String inputXML = relPath;
                String outputLabId = mal_dir + "/" + id + ".xml";
                Transform transform = new Transform();

         /**/

                /**
                 String inputXSL = "src/main/resources/labid.xsl";
                 String inputXML = "src/main/resources/combinedXML/2.16.840.1.114222.4.1.646516-source.xml";
                 String outputLabId = "src/main/resources/malformedCDA/2.16.840.1.114222.4.1.646516.xml";
                 Transform transform = new Transform();

                 /**/

                try {
                    transform.transformXML(inputXSL, inputXML, outputLabId);
                } catch (Exception e) {
                    System.out.println("well... that didn't work...");
                    e.printStackTrace();
                }
        /**/

            }
        }


        //TODO: clean up Silas's CDA-like file
        // get list of malformed CDA files
        File mcdaDir = new File(mal_dir);
        File[] mcdafiles = mcdaDir.listFiles();

        int count = 0;
        int badCount = 0;
        List<String> badList = new ArrayList<String>();

        if (files != null) {
            for (File file : mcdafiles) {

                //src/main/resources/malformedCDA/[id].xml
                String relPath = file.getPath();
                //[id].xml
                String filename = file.getName();
                //2.16.840.1.114222.4.1.646522
                String id = filename.substring(0, filename.length() - 4);
                System.out.println(relPath);
                System.out.println(filename);
                System.out.println(id);

                FileReader reader = null;
                String content = "";
                try {
                    reader = new FileReader(file);
                    char[] chars = new char[(int) file.length()];
                    try {
                        reader.read(chars);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    content = new String(chars);
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                //                System.out.println(content);


                int cdaRootIndex = content.indexOf("<ClinicalDocument");
                if (cdaRootIndex < 1) {
                    System.out.println("no clinical document tag found in malformed CDA with id: " + id);
                    badCount++;
                    badList.add(id);
                    continue;
                } else {
                    String clinicalDoc = content.substring(cdaRootIndex);


                    //System.out.println(clinicalDoc);
                    String cleanTop = replace(clinicalDoc, "<ClinicalDocument", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                            "<?xml-stylesheet type=\"text/xsl\" href=\"hai-display.xsl\"?>\n<ClinicalDocument");
                    String cleanCDA = replace(cleanTop, "</root>", "");
                    //System.out.println(cleanCDA);
                    String now = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                    try {
                        FileUtils.writeStringToFile(new File(cda_dir + "/" + id + "--" +  now + ".xml"), cleanCDA);
                        System.out.println("wrote to " + cda_dir + "/" + id +  "--" + now + ".xml");
                        count++;
                    } catch (IOException e) {
                        System.out.println("problem writing to " + cda_dir + "/" + id + "--" + now + ".xml");
                        e.printStackTrace();
                    }
                }

            }

            System.out.println("# CDA Files created: " + count);
            System.out.println("# bad malformed CDAs: " + badCount);
            for(String id: badList) {
                System.out.println("bad id:" + id);
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
}

