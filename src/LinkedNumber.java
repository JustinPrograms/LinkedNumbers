public class LinkedNumber {

    private int base;
    private DLNode<Digit> front;
    private DLNode<Digit> rear;

    public LinkedNumber(String num, int baseNum) {
        this.base = baseNum;
        if (num.isEmpty()) throw new LinkedNumberException("no digits given");
        Digit[] test;

        this.front = new DLNode<>(new Digit(num.charAt(0)));
        this.rear = this.front;

        DLNode<Digit> current = this.front;
        DLNode<Digit> nextNode = null;
        int i = 1;
        while (current != null) {
            try {
                nextNode = new DLNode<>(new Digit(num.charAt(i)));
            } catch (StringIndexOutOfBoundsException e) {
                this.rear = current;
                this.rear.setNext(null); // added this
                break;
            }

            current.setNext(nextNode);
            nextNode.setPrev(current);
            current = nextNode;
            i++;


        }

    }

    public LinkedNumber(int num) {
        this(String.valueOf(num), 10);
    }

    public boolean isValidNumber() {

        DLNode<Digit> current = this.front;
        while (current != null) {
            if (current.getElement().getValue() < new Digit('0').getValue()){
               // System.out.println("here1");
                return false;
            }
            if (current.getElement().getValue() > this.base-1) {
                //System.out.println("here2");
               // System.out.println(current.getElement().getValue());
               // System.out.println(new Digit((char) this.base).getValue());
                return false;
            }
            current = current.getNext();
        }


        return true;
    }

    public int getBase() {
        return this.base;
    }

    public DLNode<Digit> getFront() {
        return this.front;
    }

    public DLNode<Digit> getRear() {
        return this.rear;
    }

    public int getNumDigits() {
        int numOfNodes = 0;
        DLNode<Digit> current = this.front;
        while (current != null) {
            numOfNodes++;
            current = current.getNext();
        }
        return numOfNodes;
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        DLNode<Digit> current = this.front;
        while (current != null) {
            output.append(current.getElement());
            current = current.getNext();
        }
        return output.toString();
    }

    public boolean equals(LinkedNumber other) {

        if (this.base != other.base) return false;
        if (this.getNumDigits() != other.getNumDigits()) return false;
        if (!this.toString().equals(other.toString())) return false;

        return true;
    }

    public LinkedNumber convert(int newBase) {
        if (!this.isValidNumber()) {
            throw new LinkedNumberException("cannot convert invalid number");
        }

        String newString = "";

        if (this.base == 10) {
            newString = decimalToNewBase(newBase);
         } //else if (newBase == 10) {
//            newString = anyBaseToDecimal(newBase);
//        }


        return new LinkedNumber(newString, newBase);
    }

    private String decimalToNewBase(int newBase) {

        char[] hexChars = "0123456789ABCDEF".toCharArray(); // Hexadecimal characters

        StringBuilder output = new StringBuilder();
        int decimalNum = Integer.parseInt(this.toString());

        if (decimalNum == 0) {
            return "0"; // Special case for zero
        }

        while (decimalNum > 0) {
            int remainder = decimalNum % newBase;
            output.insert(0, hexChars[remainder]); // Insert remainder at the beginning
            decimalNum /= newBase;
        }

        System.out.println(output.toString());
        return output.toString();
    }

//    private String anyBaseToDecimal(int base) {
//
//    }


    public void addDigit(Digit digit, int position) {
        // make sure all cases considered
        if (position > this.getNumDigits() - 1) {
            throw new LinkedNumberException("invalid position");
        }

        DLNode<Digit> current = this.front;
        DLNode<Digit> newNode = new DLNode<>(digit);

        if (position == 0) {
            this.front.setPrev(newNode);
            newNode.setNext(this.front);
            this.front = newNode;
        }

        if (position == this.getNumDigits() - 1) {
            this.rear.setNext(newNode);
            newNode.setPrev(this.rear);
            this.rear = newNode;
        }

        int counter = 0;
        while (counter != position) {
            current = current.getNext();
            if (counter == position) {
                current.getPrev().setNext(newNode);
                current.setPrev(newNode);
            }
            counter++;

        }

    }

    public int removeDigit(int position) {
        // make sure all cases considered
        if (position > this.getNumDigits() - 1) {
            throw new LinkedNumberException("invalid position");
        }

        DLNode<Digit> current = this.rear;

        // remove last node
        if (position == 0) {
            DLNode<Digit> newRear = this.rear.getPrev();
            this.rear.getPrev().setNext(null);
            this.rear = newRear;
        }

        if (position == this.getNumDigits() - 1) {
            DLNode<Digit> newFront = this.front.getNext();
            this.front.getNext().setPrev(null);
            this.front.setNext(null);
            this.front = newFront;
        }

        int counter = 0;
        while (counter != position) {
            current = current.getPrev();
            if (counter == position) {
                current.getPrev().setNext(current.getNext());
                current.getNext().setPrev(current.getPrev());
            }
            counter++;

        }

//        Note that the value being returned will be the decimal equivalent of the value
//        regardless of the number system. For example, if we have binary number 1010
//        and call removeDigit(1), the resulting linked list will represent binary number 100
//        and the decimal number 2 will be returned (not the binary number 10).

        // idk what to return ngl
        return 1;

    }

    public static void main(String args[]) {
//        LinkedNumber ln1 = new LinkedNumber("11101101", 2);
//        LinkedNumber ln2 = new LinkedNumber("10210122", 3);
//        LinkedNumber ln3 = new LinkedNumber("32133101", 4);
//        System.out.println(ln3.convert(10).toString());
//        boolean b1 = ln1.convert(10).toString().equals("237");
//        boolean b2 = ln2.convert(10).toString().equals("2771");
//        boolean b3 = ln3.convert(10).toString().equals("59345");
//        System.out.println("b1 = " + b1);
//        System.out.println("b2 = " + b2);
//        System.out.println("b3 = " + b3);

        LinkedNumber lnTest = new LinkedNumber("2DC", 16);
        lnTest.convert(10);
    }

}