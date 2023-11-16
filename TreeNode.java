import java.util.*;

public class TreeNode{

    boolean isLeafNode = false;
    String Branching_Condition;
    String data;
    double Total_Impurity;
    double Partial_Impurity;
    int Approval = 0;
    int Rejection = 0;
    ArrayList<Data> total;
    List<TreeNode> children;
    

    public TreeNode(ArrayList<Data> input){
        total = input;
        Total_Impurity = CalculateImpurity();
        
    }


    private double CalculateImpurity(){
        for (Data data : total){
            if (data.column[15].equals("1")) Approval++;
            else Rejection++;
        }
        int denom = Approval + Rejection;
        double a = (double)Approval / (double)denom;
        double r = (double)Rejection / (double)denom;
        
        return (a > r) ? a : r;
    }


    public static void main (String args[])throws Exception{
        DataSet data = new DataSet(args[0]);
        TreeNode test = new TreeNode(data.Set);
        
    }
}