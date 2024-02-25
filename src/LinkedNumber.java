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
                return false;
            }
            if (current.getElement().getValue() > this.base-1) {
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
         } else if (newBase == 10) {
            newString = anyBaseToDecimal();
        } else {
            LinkedNumber temp = new LinkedNumber(anyBaseToDecimal(), 10);
            newString = temp.decimalToNewBase(newBase);
        }


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

        return output.toString();
    }

    private String anyBaseToDecimal() {

        String currNum = this.toString();
        int total = 0;

        for (int i = 0; i < this.getNumDigits(); i++) {
            total += Integer.parseInt(String.valueOf(currNum.charAt(i))) * powerOf(this.base, this.getNumDigits() - 1 - i);
        }
        return String.valueOf(total);

    }

    private static int powerOf(int base, int exp) {
        int res = 1;
        for (int i = 0; i < exp; i++) {
            res *= base;
        }
        return res;
    }


    public void addDigit(Digit digit, int position) {
        if (position > this.getNumDigits()|| position < 0) {
            throw new LinkedNumberException("invalid position");
        }

        DLNode<Digit> current = this.rear;
        DLNode<Digit> newNode = new DLNode<>(digit);

        if (position == this.getNumDigits()) {
            this.front.setPrev(newNode);
            newNode.setNext(this.front);
            this.front = newNode;
        }

        else if (position == 0) {
            this.rear.setNext(newNode);
            newNode.setPrev(this.rear);
            this.rear = newNode;
        }
        else {
            int counter = 0;
            while (counter != position) {
                current = current.getPrev();
                counter++;
            }
            current.getNext().setPrev(newNode);
            newNode.setNext(current.getNext());
            newNode.setPrev(current);
            current.setNext(newNode);
        }


    }

    public int removeDigit(int position) {
        // everything works but need to figure out the return statement
        if (position > this.getNumDigits()|| position < 0) {
            throw new LinkedNumberException("invalid position");
        }

        DLNode<Digit> current = this.rear;

        // remove last node
        if (position == 0) {
            DLNode<Digit> newRear = this.rear.getPrev();
            this.rear.getPrev().setNext(null);
            this.rear = newRear;
        }

        else if (position == this.getNumDigits() - 1) {
            DLNode<Digit> newFront = this.front.getNext();
            this.front.getNext().setPrev(null);
            this.front.setNext(null);
            this.front = newFront;
        }

        else {
            int counter = 0;
            while (counter != position) {
                current = current.getPrev();
                counter++;
            }

            current.getNext().setPrev(current.getPrev());
            current.getPrev().setNext(current.getNext());
        }
        return 1;
    }

}