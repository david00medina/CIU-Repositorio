package control.csv;

import control.kinect.KinectAnathomy;
import model.DancerData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import processing.core.PVector;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CSVTools {
    public static void writeCSV(Path path, CSVFormat format, String uuid, HashMap<KinectAnathomy, PVector> dd) {
        try {
            CSVPrinter p = new CSVPrinter(new FileWriter(path.toString(), true), format);
            for (KinectAnathomy ka :
                    KinectAnathomy.values()) {
                if (ka.equals(KinectAnathomy.LABEL) || ka.equals(KinectAnathomy.NOT_TRACKED)) continue;

                PVector v = dd.get(ka);
                if (v == null) p.printRecord(uuid, ka.getId(), "", "", "");

                p.printRecord(uuid, ka.getId(), v.x, v.y, v.z);
            }
            p.close(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, HashMap<String, List<String>>> readCSV(Path path, CSVFormat format, String... headers) {
        HashMap<String, HashMap<String, List<String>>> result = new HashMap<>();
        try {
            Iterable<CSVRecord> records = format.withFirstRecordAsHeader().parse(new FileReader(path.toString()));
            HashMap<String, List<String>> dict = new HashMap<>();
            for (CSVRecord record :
                    records) {

                List<String> data = new ArrayList<>();

                if (!result.containsKey(record.get(headers[0]))) dict = new HashMap<>();

                for (String header:
                        headers) {
                    if (header.equals(headers[0]) || header.equals(headers[1])) continue;

                    data.add(record.get(header));
                    dict.put(record.get(headers[1]), data);
                }
                result.put(record.get(headers[0]), dict);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
