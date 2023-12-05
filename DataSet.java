import java.io.*;
import java.util.*;

public class DataSet{
    int DataCols;
    int NumData;
    String [] DataHeader;
    int [] CategoricalFeat;
    // Set is used for training of the tree, TestSet is the set used to testing of the tree
    ArrayList<Data> Set = new ArrayList<Data>();
    ArrayList<Data> TestSet = new ArrayList<Data>();

    public DataSet(String file) throws Exception{
        Scanner scanner = new Scanner (new FileReader(file));
        String line = scanner.nextLine();
        DataHeader = line.split(",");
        CategoricalFeat = new int [DataHeader.length];
    
        int count = 0;
        // Could remove this
        for (String data : DataHeader){
            count++;
        }
        DataCols = count;
        //
        count = 0;
        while (scanner.hasNext()){
            line = scanner.nextLine();
            String [] values = line.split(",");
            count++;
            Data data = new Data(values);
            Set.add(data);
        }
        NumData = count;
        //System.out.println("Total DataPoints = " + NumData); DataSet has 690 points.
        
        Split(100);

        FindCategoricalFeatures(10);

        /* Tells you which column is a Categorical Feature
        for (int i = 0; i < CategoricalFeat.length; i++){
            System.out.println(DataHeader[i] + ": " + CategoricalFeat[i]);
        }
        */
    }

    // Parameter is the how many datapoints we want for our testing dataset
    private void Split(int TestingSetSize){
        int max = NumData - 1;
        int min = 0;
        
        for (int i = 0; i < TestingSetSize; i++){
            int num = (int) (Math.random() * ((max - min) + 1));
            TestSet.add(Set.get(num));
            Set.remove(num);
            max--;
        }
        /*  Testing The Split
        System.out.println("Training Data");
        for (Data d : Set){
            d.printData();
        }
        System.out.println("Testing Data");
        for (Data d : TestSet){
            d.printData();
        }
        */
    }

    private void FindCategoricalFeatures(int threshold){
        ArrayList<HashMap<String,String>> counter = new ArrayList<HashMap<String,String>>();
        for (int i = 0; i < DataHeader.length; i++){
            HashMap<String, String> temp = new HashMap<String, String>();
            counter.add(temp);
        }

        for (Data d: Set){
            for(int i = 0; i < d.column.length; i++){
                counter.get(i).putIfAbsent(d.column[i], d.column[i]);
            }
        }

        for (int i = 0; i < counter.size(); i++){
            if (counter.get(i).size() <= threshold){
                CategoricalFeat[i] = 1;
            } else{
                CategoricalFeat[i] = 0;
            }
        }
    }

    public ArrayList<Data> getTrainingData(){
        return Set;
    }

    public int[] getCatInfo(){
        return CategoricalFeat;
    }


    public static void main (String [] args) throws Exception{
        DataSet data = new DataSet(args[0]);

    }


}