import java.util.Random;

public class HugeInteger{
    int num[];
    boolean isPos;
    
    public HugeInteger(String val) throws IllegalArgumentException{
        //Error checking
        String regex = "\\d+";
        if (val.equals("")){ throw new IllegalArgumentException("Must be an integer."); } 
        
        //Determining if the value is positive or negative
        if (val.charAt(0) == '-'){
            this.isPos = false;
            val = val.substring(1);
            
        }else{
            this.isPos = true;
        }
        
        //Seeing if the input string matches specifications
        if(!(val.matches(regex))){
            throw new IllegalArgumentException("Must be an integer.");
        }
        
        this.num = new int[val.length()];
        
        //Filling each element of num array with each digit
        for (int i = 0; i < val.length(); i++){
            this.num[i] = Character.getNumericValue(val.charAt(i));
        }
    }
    
    public HugeInteger(int n) throws IllegalArgumentException{
        //Error checking 
        if (n < 1){ 
            throw new IllegalArgumentException ("Input must be larger than 0.");
        }
        
        isPos = true;
        Random rand = new Random();
        this.num = new int[n];
        
        //Filling array with random numbers
        for (int i = 0; i < n; i++){
            if(i == 0){
                this.num[i] = rand.nextInt(9) + 1; //Making sure it is never 0
            }else{
                this.num[i] = rand.nextInt(10);
            }
        }
    }
    
    public String addCall(HugeInteger h){
        int num1[] = this.getNum();
        int num2[] = h.getNum();
        String result = ""; //Storing result in a string

        if(num1.length == num2.length){ //If the numbers are same length
            result = addNum(num1, num2);
        }else if(num1.length > num2.length){ //Not equal length
            int tempNum[] = fillZero(num2, num1);
            result = addNum(num1, tempNum);
        }else{
            int tempNum[] = fillZero(num1, num2);
            result = addNum(num2, tempNum);
        }
        
        //Returning the string result
        return result;
    }
    
    public HugeInteger add(HugeInteger h){
        String result = ""; //Storing result in a string
        
        //Controlling function which determines what operation to do based on signage of the integers
        if((this.getIsPos() == false) && (h.getIsPos() == false)){
            result = this.addCall(h);
            result = "-" + result; //Add int's and place a negative sign in the front
        }else if((this.getIsPos() == true) && (h.getIsPos() == false)){
            result = this.subtractCall(h); //Subtract since one of them is negative
        }else if((this.getIsPos() == false) && (h.getIsPos() == true)){
            result = h.subtractCall(this); //Subtract since one of them is negative
        }else{
            result = this.addCall(h); //Case where both int's are positive
            
        }
        
        //Making a new HugeInteger object with the result string
        HugeInteger result1 = new HugeInteger(result);
        return result1;
    }
    
    public String subtractCall(HugeInteger h){
        int num1[] = this.getNum();
        int num2[] = h.getNum();
        String result = "";
        if(num1.length == num2.length){ //If the numbers are same length
            //Determining which number has larger magnitude, this let's us use our subtraction algorithm properly.
            if(compareMag(num1, num2) == 1){
                result = subtractNum(num1, num2);
            }else if(compareMag(num1, num2) == -1){
                result = subtractNum(num2, num1);
                result = "-" + result;
            }else{
                result = "0"; //Return 0 if they are equal
            }
        }else if(num1.length > num2.length){ //Not equal length
            int tempNum[] = fillZero(num2, num1);
            result = subtractNum(num1, tempNum);
        }else{
            int tempNum[] = fillZero(num1, num2);
            result = subtractNum(num2, tempNum);
            result = "-" + result; //Add a negative since we flipped the subtraction.
        }
        return result;
    }
    
    public HugeInteger subtract(HugeInteger h){
        String result = ""; //Storing result in a string
        
        //Controlling function which determines what operation to do based on signage of the integers
        if((this.getIsPos() == false) && (h.getIsPos() == false)){
            result = h.subtractCall(this);
        }else if((this.getIsPos() == true) && (h.getIsPos() == false)){
            result = this.addCall(h);
        }else if((this.getIsPos() == false) && (h.getIsPos() == true)){
            result = this.addCall(h);
            result = "-" + result;
        }else{
            result = this.subtractCall(h);
        }
        
        //Making a new HugeInteger object with the result string
        HugeInteger result1 = new HugeInteger(result);
        return result1;
    }
    
    public HugeInteger multiply(HugeInteger h){
        int num1[] = this.getNum();
        int num2[] = h.getNum();
        int count = 0; 
        int carry = 0;
        String result;
        HugeInteger nums[] = new HugeInteger[num2.length]; //Array to store all numbers to be added
        
        for (int i = num2.length - 1; i >= 0; i--){
            result = ""; //Resetting result after each integer is done multiplying
            for (int j = num1.length - 1; j >= 0; j--){
                if(((num2[i]*num1[j]) + carry) < 10){
                    result = ((num2[i]*num1[j]) + carry) + result; //No carry case
                }else{ //If a carry is needed
                    if(j != 0){
                        result = (((num2[i]*num1[j]) + carry) % 10) + result; //Extracting least significant digit and adding it to result
                        carry = ((num2[i]*num1[j]) + carry) / 10; //The carry is equal to the most significant digit
                    }else{
                        result = ((num2[i]*num1[j]) + carry) + result; //Want to print product + carry for last iteration
                    }
                }
            }
            for(int k = num2.length - 1; k > i; k--){ result = result + "0"; } //Adding 0's based on depth to imitate multiplication technique
            carry = 0;
            nums[count++] = new HugeInteger(result); //Store the int in the array;
        }

        //Add together all the numbers in the array
        HugeInteger result1 = new HugeInteger("0");
        for(int i = 0; i < nums.length; i++){
            result1 = result1.add(nums[i]);
        }
        
        //Add a negative sign if only one of the numbers is negative
        if (((this.getIsPos() == false) && (h.getIsPos() == true)) || ((this.getIsPos() == true) && (h.getIsPos() == false))){
            HugeInteger temp = new HugeInteger("-" + result1.toString());
            result1 = temp;
        }
        return result1;
    }
    
    public int compareTo(HugeInteger h){
        //If one is positive and one is negative, we can automatically return a result
        if ((this.getIsPos() == false) && (h.getIsPos() == true)){
            return -1;
        }else if ((this.getIsPos() == true) && (h.getIsPos() == false)){
            return 1;
        }else{ //If they are equal signage, we need to determine value.
            //If they are different lengths, we can return a result based on which signs both integers have
            if(this.getNum().length > h.getNum().length){
                if (this.getIsPos() == false){
                    return -1;
                }else{
                    return 1;
                }
            }else if(this.getNum().length < h.getNum().length){
                if (this.getIsPos() == false){
                    return 1;
                }else{
                    return -1;
                }
            }else{ //If they are the same length and same size, we need to compare MSD's until a larger value is found.
                for(int i = 0; i < this.getNum().length; i++){
                    if(this.getNum()[i] > h.getNum()[i]){
                        if (this.getIsPos() == false){
                            return -1;
                        }else{
                            return 1;
                        }
                    }else if(this.getNum()[i] < h.getNum()[i]){
                        if (this.getIsPos() == false){
                            return 1;
                        }else{
                            return -1;
                        }
                    }
                }
                return 0; //Return 0 if they are equal
            }
        }
    }
    
    public String toString(){
        String output = "";

        //Output each element
        for (int i = 0; i < num.length; i++){
            output += num[i];
        }
        output = removeLeadingZeros(output);
        //Output negative sign if its not positive
        if (!isPos){
            output = "-" + output;
        }
        if (output.equals("-0")){ output = "0"; }
        return output;
    }
    
    //Getter for the num array
    public int[] getNum(){
        return this.num;
    }
    
    //Getter for signage of the number
    public boolean getIsPos(){
        return this.isPos;
    }
    
    public String addNum(int[] n1, int[] n2){
        int carry = 0;
        String sum = "";
        for (int i = n2.length - 1; i >= 0; i--){
            if((n1[i] + n2[i] + carry) < 10){ //No carry scenario
                sum = (n1[i] + n2[i] + carry) + sum; //Add the 1 digit num to sum string
                carry = 0;
            }else{ //Carry scenario
                if(i != 0){
                    sum = ((n1[i] + n2[i] + carry) % 10) + sum; //Extracting least significant digit and adding it to sum
                    carry = 1;
                }else{
                    sum = (n1[i] + n2[i] + carry) + sum; //Want to print sum + carry for last iteration
                }
            }
            
        }
        return sum;
    }
    
    public String subtractNum(int[] n1, int[] n2){
        String diff = "";
        for (int i = n2.length - 1; i >= 0; i--){
            if((n1[i] - n2[i]) >= 0){ //No borrow scenario
                diff = (n1[i] - n2[i]) + diff; //Add the 1 digit num to diff string
            }else{ //Borrow scenario
                n1[i] += 10; //Add 10 to current num
                n1[i - 1]--; //Borrow a digit from the next num
                diff = (n1[i] - n2[i]) + diff;
            }
        }
        return diff;
    }
    
    public int compareMag(int[] n1, int[] n2){
        //Compares magnitudes of 2 equal length values.
        for(int i = 0; i < n1.length; i++){
            if(n1[i] > n2[i]){
                return 1;
            }
            if(n1[i] < n2[i]){
                return -1;
            }
        }
        return 0;
    }
    
    public int[] fillZero(int[] n1, int[] n2){
        int tempNum[] = new int[n2.length];
        int counter = 0;
        
        //Fill zeros for difference of lengths
        for (int i = 0; i < n2.length - n1.length; i++){
            tempNum[i] = 0;
        }
        
        //Fill the rest with the contents of the smaller array
        for (int i = n2.length - n1.length; i < tempNum.length; i++){
            tempNum[i] = n1[counter];
            counter ++;
        }
        
        return tempNum;
    }
    
    public String removeLeadingZeros(String x){
        String output = x;
        //Iterate until a non-zero element is found, return a substring from that point
        for (int i = 0; i < x.length(); i++){
            if(x.charAt(i) != '0'){
                return x.substring(i);
            }
        }
        return "0"; //If all elements are zeroes, just return one zero.
    }
}