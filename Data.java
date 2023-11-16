public class Data{
    // Gender,Age,Debt,Married,BankCustomer,Industry,Ethnicity,YearsEmployed,PriorDefault,Employed,CreditScore,DriversLicense,Citizen,ZipCode,Income,Approved
    String [] column;
    public Data(String [] input){
        if (input.length < 14){
            System.out.println("Missing Values");
            return;
        }
        else{
            column = input;
        }
    }

    public void printData(){
        System.out.println(column[0] + " " + column[1] + " " + column[15]);
    }

}