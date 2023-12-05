import java.util.*;

public class TreeNode{

    DataSet info;

    ArrayList<Data> startDataSet = new ArrayList<>();
    ArrayList<TreeNode> children = new ArrayList<>();

    int depth;
    int approved = 0;
    int rejected = 0;
    double accuracy;

    public TreeNode(DataSet dataSet){
        info = dataSet;
        startDataSet = dataSet.getTrainingData();
        accuracy = CalcAccuracy();
    }

    public TreeNode(ArrayList<Data> dataS, DataSet dataset){
        info = dataset;
        startDataSet = dataS;
        accuracy = CalcAccuracy();
    }

    public void split(int index){

        if (info.getCatInfo()[index] != 1){
            // Continous values
            bestContSplit(index);
        }
        else{
            // Categorical Values
            categoricalSplit(index);
        }

    }

    private void bestContSplit(int index){
        sortOn(index);

        ArrayList <Double> possVal = findPossVal(index);
        ArrayList<TreeNode> bestC = new ArrayList<>();
        
        double bestA = 0;
        double bestVal = 0;
        
        for (Double val : possVal){
            continousSplit(index, val);
            double temp = CalcImpurity();

            if (temp > bestA){
                bestA = temp;
                bestVal = val;
                bestC = getChildNode();
            }
            children.clear();
        }

        children = bestC;
        
        //return bestVal;
    }

    private void continousSplit(int index, double val){

        ArrayList<Data> leftChild = new ArrayList<Data>();
        ArrayList<Data> rightChild = new ArrayList<Data>();
        for (Data data: startDataSet){
            if (Double.valueOf(data.column[index]) < val){
                leftChild.add(data);
            }
            else{
                rightChild.add(data);
            }
        }

        TreeNode left = new TreeNode(leftChild, getDS());
        TreeNode right = new TreeNode(rightChild, getDS());
        children.add(left);
        children.add(right);
    }

    private ArrayList<Double> findPossVal(int index){
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < startDataSet.size() - 1; i++){
            double p1 = Double.valueOf(startDataSet.get(i).column[index]);
            double p2 = Double.valueOf(startDataSet.get(i + 1).column[index]);
            double val;
            if (p1 != p2){
                val = (p1 + p2) / 2.0;
                result.add(val);
            }
        }

        return result;
    }

    private void sortOn(int index){
        Collections.sort(startDataSet, new Comparator<Data>(){
            public int compare(Data d1, Data d2){
                double v1 = Double.valueOf(d1.column[index]);
                double v2 = Double.valueOf(d2.column[index]);
                return  Double.compare(v1, v2);
            }
        });
    }

    public double CalcImpurity(){
        double result = 0.0;
        for (TreeNode child : children){
            double weight = (double)child.getMaxCount() / (double)getMaxCount();
            result += weight * child.getAccuracy();
        }
        
        return result;
    }

    private double CalcAccuracy(){
        int count = 0;

        for (Data data : startDataSet){
            if (Integer.valueOf(data.column[data.column.length - 1]) == 1){
                approved++;
            }
            else{
                rejected++;
            }
            count++;
        }
        double total = 1.0 - Math.pow(( (double)approved / (double)count), 2) - Math.pow(((double)rejected / (double)count), 2);

        return total;
    }


    private void categoricalSplit(int index){
        
    }


    public ArrayList<Data> getStartData(){
        return startDataSet;
    }

    public ArrayList<TreeNode> getChildNode(){
        return children;
    }

    public int getMaxCount(){
        return approved + rejected;
    }

    public DataSet getDS(){
        return info;
    }

    public double getAccuracy(){
        return accuracy;
    }


    public static void main (String args[])throws Exception{
        DataSet dataset = new DataSet(args[0]);
        TreeNode test = new TreeNode(dataset);
        test.split(1);

    }

}