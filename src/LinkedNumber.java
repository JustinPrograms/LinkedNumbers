public class LinkedNumber {

    private int base; // base of the number
    private DLNode<Digit> front; // reference to the front node of the linked list
    private DLNode<Digit> rear; // reference to the rear node of the linked list

    /**
     * Constructs a LinkedNumber object with the given number and base.
     *
     * @param num     the number to be represented
     * @param baseNum the base of the number
     */
    public LinkedNumber(String num, int baseNum) {
        this.base = baseNum;
        if (num.isEmpty()) throw new LinkedNumberException("no digits given");

        this.front = new DLNode<>(new Digit(num.charAt(0))); // create first node
        this.rear = this.front; // initialize rear with front

        DLNode<Digit> current = this.front;
        DLNode<Digit> nextNode;
        int i = 1;

        while (true) {
            try {
                nextNode = new DLNode<>(new Digit(num.charAt(i))); // create next node
            } catch (StringIndexOutOfBoundsException e) {
                this.rear = current; // update rear to current node
                this.rear.setNext(null); // set next of rear to null
                break;
            }

            current.setNext(nextNode); // link current node to next node
            nextNode.setPrev(current); // link next node to current node
            current = nextNode; // move current to next node
            i++;
        }
    }

    /**
     * Constructs a LinkedNumber object with the given integer number (Base 10) by default.
     *
     * @param num the integer number to be represented
     */
    public LinkedNumber(int num) {
        this(String.valueOf(num), 10); // call the constructor with the number converted to string and base 10
    }


    /**
     * Checks if the LinkedNumber represents a valid number.
     *
     * @return true if the number is valid, false otherwise
     */
    public boolean isValidNumber() {
        DLNode<Digit> current = this.front;

        // Iterate through each digit in the linked list
        while (current != null) {
            // Check if the digit is less than 0
            if (current.getElement().getValue() < new Digit('0').getValue()) {
                return false; // if less than 0, return false
            }
            // Check if the digit is greater than or equal to the base
            if (current.getElement().getValue() >= this.base) {
                return false; // if greater than or equal to base, return false
            }
            current = current.getNext(); // move to the next digit
        }
        return true; // return true if all digits are valid
    }


    /**
     * Retrieves the base of the LinkedNumber.
     *
     * @return the base of the LinkedNumber
     */
    public int getBase() {
        return this.base;
    }

    /**
     * Retrieves the reference to the front node of the LinkedNumber.
     *
     * @return the reference to the front node of the LinkedNumber
     */
    public DLNode<Digit> getFront() {
        return this.front;
    }

    /**
     * Retrieves the reference to the rear node of the LinkedNumber.
     *
     * @return the reference to the rear node of the LinkedNumber
     */
    public DLNode<Digit> getRear() {
        return this.rear;
    }


    /**
     * Retrieves the number of digits in the LinkedNumber.
     *
     * @return the number of digits in the LinkedNumber
     */
    public int getNumDigits() {
        int numOfNodes = 0;
        DLNode<Digit> current = this.front;

        // Iterate through each node and count the number of digits
        while (current != null) {
            numOfNodes++;
            current = current.getNext();
        }
        return numOfNodes; // return the total number of digits
    }


    /**
     * Returns a string representation of the LinkedNumber.
     *
     * @return a string representation of the LinkedNumber
     */
    public String toString() {
        StringBuilder output = new StringBuilder();
        DLNode<Digit> current = this.front;

        // Iterate through each node and append the digit to the output
        while (current != null) {
            output.append(current.getElement());
            current = current.getNext();
        }
        return output.toString(); // return the string representation
    }


    /**
     * Checks if this LinkedNumber is equal to another LinkedNumber.
     *
     * @param other the other LinkedNumber to compare with
     * @return true if the two LinkedNumbers are equal, false otherwise
     */
    public boolean equals(LinkedNumber other) {
        // Check if the bases are different
        if (this.base != other.base) return false;
        // Check if the number of digits are different
        if (this.getNumDigits() != other.getNumDigits()) return false;
        // Check if the string representations are equal
        return this.toString().equals(other.toString());
    }


    /**
     * Converts the LinkedNumber to a new base.
     *
     * @param newBase the base to convert the number to
     * @return a new LinkedNumber representing the converted number
     * @throws LinkedNumberException if the current number is invalid and cannot be converted
     */
    public LinkedNumber convert(int newBase) {
        if (!this.isValidNumber()) {
            throw new LinkedNumberException("cannot convert invalid number");
        }

        String newString;

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

    /**
     * Converts a decimal number to the specified base.
     *
     * @param newBase the base to convert to
     * @return the converted number as a string
     */
    private String decimalToNewBase(int newBase) {
        char[] baseChars = "0123456789ABCDEF".toCharArray(); // Characters for different bases
        StringBuilder output = new StringBuilder();
        int decimalNum = Integer.parseInt(this.toString());

        if (decimalNum == 0) {
            return "0"; // Special case for zero
        }

        while (decimalNum > 0) {
            int remainder = decimalNum % newBase;
            output.insert(0, baseChars[remainder]); // Insert remainder at the beginning
            decimalNum /= newBase;
        }

        return output.toString();
    }

    /**
     * Converts a number from any base to decimal.
     *
     * @return the decimal representation of the number as a string
     */
    private String anyBaseToDecimal() {
        String currNum = this.toString();
        int total = 0;

        for (int i = 0; i < this.getNumDigits(); i++) {
            total += Integer.parseInt(String.valueOf(currNum.charAt(i))) * powerOf(this.base, this.getNumDigits() - 1 - i);
        }
        return String.valueOf(total);
    }

    /**
     * Computes the power of a number.
     *
     * @param base the base number
     * @param exp  the exponent
     * @return the result of base raised to the power of exp
     */
    private int powerOf(int base, int exp) {
        int res = 1;
        for (int i = 0; i < exp; i++) {
            res *= base;
        }
        return res;
    }



    /**
     * Adds a digit at the specified position in the LinkedNumber.
     *
     * @param digit    the digit to add
     * @param position the position at which to add the digit
     * @throws LinkedNumberException if the position is invalid
     */
    public void addDigit(Digit digit, int position) {
        if (position > this.getNumDigits() || position < 0) {
            throw new LinkedNumberException("invalid position");
        }

        DLNode<Digit> current = this.rear;
        DLNode<Digit> newNode = new DLNode<>(digit);

        if (position == this.getNumDigits()) { // Add at the end
            this.front.setPrev(newNode);
            newNode.setNext(this.front);
            this.front = newNode;
            this.front.setPrev(null);
        } else if (position == 0) { // Add at the beginning
            this.rear.setNext(newNode);
            newNode.setPrev(this.rear);
            this.rear = newNode;
            this.rear.setNext(null);
        } else { // Add at the specified position
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


    /**
     * Removes and returns the digit at the specified position in the LinkedNumber.
     *
     * @param position the position of the digit to remove
     * @return the value of the removed digit multiplied by the appropriate power of the base
     * @throws LinkedNumberException if the position is invalid
     */
    public int removeDigit(int position) {
        if (position > this.getNumDigits() - 1 || position < 0) {
            throw new LinkedNumberException("invalid position");
        }

        DLNode<Digit> current = this.rear;
        int value;

        // Remove the last node
        if (position == 0) {
            value = this.rear.getElement().getValue();
            DLNode<Digit> newRear = this.rear.getPrev();
            if (newRear != null) {
                newRear.setNext(null);
                this.rear = newRear;
            } else {
                this.front = null;
                this.rear = null;
            }
        }
        // Remove the first node
        else if (position == this.getNumDigits() - 1) {
            value = this.front.getElement().getValue();
            DLNode<Digit> newFront = this.front.getNext();
            if (newFront != null) {
                newFront.setPrev(null);
                this.front.setNext(null);
                this.front = newFront;
            } else {
                this.front = null;
                this.rear = null;
            }
        }
        // Remove a node at a specific position
        else {
            int counter = 0;
            while (counter != position) {
                current = current.getPrev();
                counter++;
            }
            value = current.getElement().getValue();
            current.getNext().setPrev(current.getPrev());
            current.getPrev().setNext(current.getNext());
        }

        return value * powerOf(this.base, position);
    }


}