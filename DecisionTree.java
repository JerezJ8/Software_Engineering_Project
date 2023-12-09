import java.util.*;

public class DecisionTree{

    TreeNode Root;
    double BaseLine;
    int treeHeight = 0;

    int branchInd = 0;
    String branchVal = "";

    ArrayList<Data> TrainingData = new ArrayList<>();
    ArrayList<HashMap<String, Integer>> Categories = new ArrayList<HashMap<String, Integer>>();
    int [] CategoricalFeat;

    public DecisionTree(ArrayList<Data> TData, int [] CateFeat, ArrayList<HashMap<String, Integer>> Cat){
        TrainingData = TData;
        CategoricalFeat = CateFeat;
        Categories = Cat;
    }

    public TreeNode makeDecisionTree(ArrayList<Data> DT_data, int max_depth){
        // Base case
        TreeNode DT_node = new TreeNode(DT_data, CategoricalFeat, Categories);
        if (max_depth == 0 || DT_node.isPure()) {
            if (DT_data.size() == TrainingData.size()) Root = DT_node;
            return DT_node;
        }
        else{
            //Recursive case
            //Randomly Select Features To make the tree
            LinkedList <Integer> list = new LinkedList<>();
            for (int i = 0; i < 5;){
                int num = getRandomInt(0, CategoricalFeat.length - 2);
                if (!list.contains(num)){
                    list.add(num);
                    i++;
                }
            }
            Integer [] nums = list.toArray(new Integer [list.size()]);
            
            ArrayList<TreeNode> splits = getSplits(DT_node, nums);
            DT_node.setBranchInfo(branchInd, branchVal);

            for (TreeNode split : splits){
                //recursive call
                
                TreeNode child = makeDecisionTree(split.getStartData(), max_depth - 1);
                DT_node.addChild(child);
            }

        }
        Root = DT_node;
        return DT_node;
    }

    private int getRandomInt(int min, int max){
        return (int) (Math.random() * ((max - min) + 1));
    }

    private ArrayList<TreeNode> getSplits(TreeNode node, Integer [] randomFeat){
        ArrayList<TreeNode> splits = new ArrayList<>();
        TreeNode bestsplit;
        double mostGain = 1.0;
        int ind = 0;
        String val = "";

        for (Integer index : randomFeat){

            TreeNode possNode = node;
            possNode.split(index);
            double gain = possNode.CalcImpurity();

            
            if (gain < mostGain){
                splits.clear();
                mostGain = gain;
                for (TreeNode child: possNode.getChildNode()){
                    splits.add(child);
                }
                ind = index;
                branchVal = possNode.getBranchValue();
                branchInd = ind;
            }
            possNode.deleteChildren();
        }
        
        return splits;
    }

    public TreeNode getRootNode(){
        return Root;
    }

    public void checkT(TreeNode r){
        if (r.getChildNode().size() <= 0){
            r.checkTotal();
        }
        else{
            for (TreeNode child: r.getChildNode()){
            checkT(child);
            }
        }
    }


    public int classify(Data data){
        TreeNode copy = Root;
        while (!copy.isLeafNode()){
            int ind = copy.getBranchIndex();
    
            if (CategoricalFeat[ind] != 1){
                double val = Double.valueOf(copy.getBranchValue());
                if (Double.valueOf(data.column[ind]) <= val){
                    copy = copy.getChildNode().get(0);
                }
                else{
                    copy = copy.getChildNode().get(1);
                }
            }
            else{
                int val = Categories.get(ind).get(data.column[ind]);
                copy = copy.getChildNode().get(val);
            }
        }
        
        return copy.classify;
    }

    public ArrayList<Data> getDTData(){
        return TrainingData;
    }

}