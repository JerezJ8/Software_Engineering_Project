import java.io.*;
import java.util.*;

public class DataSet{
    int DataCols;
    int NumData;
    String [] DataHeader;
    int [] CategoricalFeat;

    ArrayList<HashMap<String,Integer>> possible = new ArrayList<HashMap<String, Integer>>(); 
    // Set is used for training of the tree, TestSet is the set used to testing of the tree
    ArrayList<Data> Set = new ArrayList<Data>();
    ArrayList<Data> TestSet = new ArrayList<Data>();

    public DataSet(String file) throws Exception{
        Scanner scanner = new Scanner (new FileReader(file));
        String line = scanner.nextLine();
        DataHeader = line.split(",");
        CategoricalFeat = new int [DataHeader.length];
        for (int i = 0; i < DataHeader.length; i++){
            possible.add(new HashMap<String, Integer>());
        }

        int count = 0;
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

        Split(100);
        FindCategoricalFeatures(15);
        changeCategory();
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
    }

    private void FindCategoricalFeatures(int threshold){
        ArrayList<HashMap<String,String>> counter = new ArrayList<HashMap<String,String>>();
        // Create a new hashmap to find the unique values of each column in data
        for (int i = 0; i < DataHeader.length; i++){
            HashMap<String, String> temp = new HashMap<String, String>();
            counter.add(temp);
        }

        //Iterate through all data in the set
        for (Data d: Set){
            // Add only unique values to the hashmap 
            for(int i = 0; i < d.column.length; i++){
                counter.get(i).putIfAbsent(d.column[i], d.column[i]);
            }
        }

        // if any column has many (more than threshold) unique values then label it as a continous value.
        for (int i = 0; i < counter.size(); i++){
            if (counter.get(i).size() <= threshold){
                CategoricalFeat[i] = 1;
            } else{
                CategoricalFeat[i] = 0;
            }
        }

    }


    public void changeCategory(){
        int [] counter  = new int [DataHeader.length];

        for (Data data : Set){
            for (int i = 0; i < data.column.length; i++){
                if (CategoricalFeat[i] == 1){
                    String val = data.column[i];
                    try{
                        Integer test = Integer.parseInt(data.column[i]);
                        possible.get(i).putIfAbsent(val, test);
                    } catch (NumberFormatException nfe){
                        // if categorical and not already in number format
                        if (!possible.get(i).containsKey(val)){
                            data.column[i] = String.valueOf(counter[i]);
                            possible.get(i).put(val,counter[i]);
                            counter[i]++;
                        }
                        else{
                            data.column[i] = String.valueOf(possible.get(i).get(val));
                        }
                    }


                }
            }
        }  

    }

    public ArrayList<Data> getTrainingData(){
        return Set;
    }

    public int[] getCatInfo(){
        return CategoricalFeat;
    }

    public ArrayList<HashMap<String,Integer>> getRange(){
        return possible;
    }

    public ArrayList<Data> getTestingData(){
        return TestSet;
    }

    public int getNumOfFeat(){
        return DataHeader.length -1;
    }

}