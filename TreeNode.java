import java.util.*;

public class TreeNode{

    ArrayList<Data> startDataSet = new ArrayList<>();
    ArrayList<TreeNode> children = new ArrayList<>();
    ArrayList<HashMap<String, Integer>> Categories = new ArrayList<HashMap<String, Integer>>();

    int [] CategoricalFeat;
    int depth;
    int approved = 0;
    int rejected = 0;
    double accuracy;
    boolean isLeaf = true;
    int Branch_Index = -1;
    String BranchOn = "";
    // 1 indicates approved and 0 indicates rejection
    int classify;

    public TreeNode(ArrayList<Data> dataS, int [] CatFeat, ArrayList<HashMap<String, Integer>> Categ){
        CategoricalFeat = CatFeat;
        startDataSet = dataS;
        Categories = Categ;
        accuracy = CalcAccuracy();
        classify = (approved > rejected) ? 1 : 0;
    }

    public int getBranchIndex(){
        return Branch_Index;
    }

    public String getBranchValue(){
        return BranchOn;
    }

    public void split(int index){
        if (CategoricalFeat[index] != 1){
            // Continous values
            bestContSplit(index);
        }
        else{
            // Categorical Values
            categoricalSplit(index);
        }

        isLeaf = false;
    }

    private void bestContSplit(int index){
        sortOn(index);

        ArrayList <Double> possVal = findPossVal(index);
        ArrayList<TreeNode> bestC = new ArrayList<>();
        
        double bestA = 1.0;
        double bestVal = 0;
        
        for (Double val : possVal){
            continousSplit(index, val);
            double temp = CalcImpurity();

            if (temp < bestA){
                bestA = temp;
                bestVal = val;
                bestC.clear();
                for (TreeNode child : children){
                    bestC.add(child);
                }
            }
            children.clear();
        }

        children = bestC;
        BranchOn = Double.toString(bestVal);
        
    }

    private void continousSplit(int index, double val){

        ArrayList<Data> leftChild = new ArrayList<Data>();
        ArrayList<Data> rightChild = new ArrayList<Data>();
        for (Data data: startDataSet){
            if (Double.valueOf(data.column[index]) <= val){
                leftChild.add(data);
            }
            else{
                rightChild.add(data);
            }
        }

        TreeNode left = new TreeNode(leftChild, CategoricalFeat, Categories);
        TreeNode right = new TreeNode(rightChild, CategoricalFeat, Categories);
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
        int maxC = Categories.get(index).size();
        ArrayList<ArrayList<Data>> childDataSet = new ArrayList<ArrayList<Data>>();
        // Initlaize list for childDS
        for (int i = 0; i < maxC; i++) {
            ArrayList<Data> temp = new ArrayList<>();
            childDataSet.add(temp);
        }
        // Group based on the categories
        for (Data data : startDataSet){
            childDataSet.get(Integer.valueOf(data.column[index])).add(data);
        }
        // add child to children
        for (int i = 0; i < maxC; i++){
            TreeNode child = new TreeNode(childDataSet.get(i), CategoricalFeat, Categories);
            children.add(child);
        }

        // Add another case for when a list in childDataSet is empty
        
        BranchOn = "Categorical";
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

    public double getAccuracy(){
        return accuracy;
    }

    public void addChild(TreeNode child){
        children.add(child);
    }

    public boolean isPure(){
        return (approved == 0 || rejected == 0);
    }

    public void deleteChildren(){
        children.clear();
    }

    public void setBranchInfo(int index, String value){
        Branch_Index = index;
        BranchOn = value;
    }

    public boolean isLeafNode(){
        if (isLeaf == true) return true;
        if (children.size() <= 0) return true;
        return false;
    }

    public void checkTotal(){
        
        if (children.size() <= 0){
            System.out.println( "Approve: " + approved + " Rejected: " + rejected);
        }
    }

}