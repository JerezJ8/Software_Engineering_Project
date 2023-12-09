import java.util.*;

public class RandomForest{

    DataSet data_set;
    ArrayList<Data> InitialDS;
    int FeaturesNum;
    ArrayList<DecisionTree> forest = new ArrayList<>();


    public RandomForest(DataSet dataSet){
        data_set = dataSet;
        FeaturesNum = data_set.getNumOfFeat();
        InitialDS = data_set.getTrainingData();

    }

    public void makeRandomForest(int forestSize, int tree_depth){
        
        for (int i = 0 ; i < forestSize; i++){
            ArrayList<Data> randomSample = bootstrap(InitialDS, 100);
            DecisionTree dtree = new DecisionTree(randomSample, data_set.getCatInfo(), data_set.getRange());
            dtree.makeDecisionTree(dtree.getDTData(), tree_depth);
            forest.add(dtree);
        }

    }
    

    private ArrayList<Data> bootstrap(ArrayList<Data> originalDS, int samples){
        ArrayList<Data> sample = new ArrayList<>();
        
        //Make a copy of the original dataset into new sample dataset
        for (Data data: originalDS){
            sample.add(data);
        }
        // randomly select n samples and replace a data point from the original dataset
        for (int i = 0; i < samples; i++){
            Data replacement = originalDS.get(getRandomInt(0, originalDS.size() - 1));
            sample.set(getRandomInt(0,originalDS.size() - 1), replacement);
        }

        return sample;
    }

    private int getRandomInt(int min, int max){
        return (int) (Math.random() * ((max - min) + 1));
    }

    public double testData (ArrayList<Data> testSet){
        int correct = 0;
        int incorrect = 0;

        for (Data data : testSet){
            
            int ans = classifyData(data);
            
            if (ans == Integer.valueOf(data.column[data.column.length - 1])){
                correct++;
            }
            else{
                incorrect++;
            }
        }

        return (double)correct / (double)testSet.size();
    }

    public int classifyData(Data data){
        int approved = 0;
        int rejected = 0;
        for (DecisionTree tree : forest){
            int temp  = tree.classify(data);
            if (temp == 1) approved++;
            else rejected++;
        }

        int ans = 0;
        if (approved > rejected) ans = 1;
        return ans;
    }

    public static void main (String args[]) throws Exception{
        DataSet ds = new DataSet(args[0]);
        RandomForest rf = new RandomForest(ds);
        rf.makeRandomForest(10, 4);
        
    }

    
}