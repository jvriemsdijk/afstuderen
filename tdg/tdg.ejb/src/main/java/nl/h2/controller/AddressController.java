package nl.h2.controller;

import nl.h2.schema.AddressJPA;
import nl.h2.schema.BagJPA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by joeyvanriemsdijk on 23/06/16.
 */
public class AddressController {


    List<BagJPA> bagList = new ArrayList<BagJPA>();


    public List<BagJPA> getBagList() {

        if (bagList == null || bagList.size() == 0) {

            File file = new File("files/addresses/addresses.csv");
            String  line = null;
            FileReader fr = null;
            BufferedReader br = null;

            bagList = new ArrayList<BagJPA>();
            try {
                // ga nu lezen
                fr = new FileReader(file);
                br = new BufferedReader(fr);
                while ((line = br.readLine()) != null) {

                    StringTokenizer tokenizer = new StringTokenizer(line,",");
                    BagJPA bag = new BagJPA();
                    AddressJPA address = new AddressJPA();

                    // Read CSV
                    address.setCity(tokenizer.nextToken());
                    address.setStreet(tokenizer.nextToken());
                    address.setNumber(Integer.valueOf(tokenizer.nextToken()));
                    address.setZipcode(tokenizer.nextToken());
                    bag.setX(new Double(tokenizer.nextToken()));
                    bag.setY(new Double(tokenizer.nextToken()));

                    // Set general data
                    bag.setUsePurpose("Residence");
                    address.setCountry("Netherlands");
                    bag.setAddress(address);
                    address.setBag(bag);

                    bagList.add(bag);

                }
            }catch(IOException ioe) {
                // doe maar niets
            } finally {
                try{
                    br.close();
                    fr.close();
                } catch(Exception e) {
                    // Do nothing
                }
            }
        }

        return bagList;
    }


    public void setBagList(List<BagJPA> bagList) {
        this.bagList = bagList;
    }
}
