package com.tripledrift.flooringmastery.View;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.tripledrift.flooringmastery.Service.FloorMasterService;
import com.tripledrift.flooringmastery.Service.FloorMasterServiceImpl;
import ch.qos.logback.core.boolex.Matcher;
import org.springframework.stereotype.Component;

@Component
public class UserIOImpl implements UserIO
{
    final private Scanner console = new Scanner(System.in);


    /**
     *
     * A very simple method that takes in a message to display on the console
     * and then waits for a integer answer from the user to return.
     *
     * @param msg - String of information to display to the user.
     *
     */
    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    /**
     *
     * A simple method that takes in a message to display on the console,
     * and then waits for an answer from the user to return.
     *
     * @param msgPrompt - String explaining what information you want from the user.
     * @return the answer to the message as string
     */
    @Override
    public String readString(String msgPrompt) {
        System.out.println(msgPrompt);
        return console.nextLine();
    }

    /**
     *
     * A simple method that takes in a message to display on the console,
     * and continually reprompts the user with that message until they enter an integer
     * to be returned as the answer to that message.
     *
     * @param msgPrompt - String explaining what information you want from the user.
     * @return the answer to the message as integer
     */
    @Override
    public int readInt(String msgPrompt) {
        boolean invalidInput = true;
        int num = 0;
        while (invalidInput) {
            try {
                // print the message msgPrompt (ex: asking for the # of cats!)
                String stringValue = this.readString(msgPrompt);
                // Get the input line, and try and parse
                num = Integer.parseInt(stringValue); // if it's 'bob' it'll break
                invalidInput = false; // or you can use 'break;'
            } catch (NumberFormatException e) {
                // If it explodes, it'll go here and do this.
                this.print("Input error. Please try again.");
            }
        }
        return num;
    }

    /**
     *
     * A slightly more complex method that takes in a message to display on the console,
     * and continually reprompts the user with that message until they enter an integer
     * within the specified min/max range to be returned as the answer to that message.
     *
     * @param msgPrompt - String explaining what information you want from the user.
     * @param min - minimum acceptable value for return
     * @param max - maximum acceptable value for return
     * @return an integer value as an answer to the message prompt within the min/max range
     */
    @Override
    public int readInt(String msgPrompt, int min, int max) {
        int result;
        do {
            result = readInt(msgPrompt);
        } while (result < min || result > max);

        return result;
    }

    /**
     *
     * A simple method that takes in a message to display on the console,
     * and continually reprompts the user with that message until they enter a long
     * to be returned as the answer to that message.
     *
     * @param msgPrompt - String explaining what information you want from the user.
     * @return the answer to the message as long
     */
    @Override
    public long readLong(String msgPrompt) {
        while (true) {
            try {
                return Long.parseLong(this.readString(msgPrompt));
            } catch (NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
    }

    @Override
    public long readLong(String msgPrompt, long min, long max) {
        long result;
        do {
            result = readLong(msgPrompt);
        } while (result < min || result > max);

        return result;
    }

    @Override
    public float readFloat(String msgPrompt) {
        while (true) {
            try {
                return Float.parseFloat(this.readString(msgPrompt));
            } catch (NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
    }


    @Override
    public float readFloat(String msgPrompt, float min, float max) {
        float result;
        do {
            result = readFloat(msgPrompt);
        } while (result < min || result > max);

        return result;
    }

    @Override
    public double readDouble(String msgPrompt) {
        while (true) {
            try {
                return Double.parseDouble(this.readString(msgPrompt));
            } catch (NumberFormatException e) {
                this.print("Input error. Please try again.");
            }
        }
    }

    @Override
    public double readDouble(String msgPrompt, double min, double max) {
        double result;
        do {
            result = readDouble(msgPrompt);
        } while (result < min || result > max);
        return result;
    }



    @Override
    public LocalDate readDate(String msgPrompt, boolean futureDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
        LocalDate date;
        LocalDate currentDate = LocalDate.now();
        while (true) {
            try {
               date =  LocalDate.parse(this.readString(msgPrompt),formatter);
               if (futureDate && date.compareTo(currentDate)>0)
                    return date;
                
                else if (futureDate) {
                    print("Please enter a future date in the format yyyy-mm-dd");
                    print("*************************************");
                }

                else return date;
            } catch (Exception e) {
                this.print("Invalid date. Please enter a valid date");
              }
        }
    }

    @Override
    public String readCustomerName(String msgPrompt) {
       String customerName;
       String regex ="^[a-zA-Z0-9 ]+$";
        do{
          customerName = readString(msgPrompt);
        } while(!customerName.matches(regex));
        return customerName;
    }

    @Override
    public BigDecimal readArea(String prompt) {
        BigDecimal userInput;
        do{ 
             userInput = readBigDecimal(prompt);
        } while (userInput.compareTo(new BigDecimal(100))<0);
        return userInput;
    }

    @Override
    public BigDecimal readBigDecimal(String prompt) {
        boolean invalidInput = true;
        BigDecimal num = new BigDecimal('0') ;
        while (invalidInput) {
            try {
                String stringValue = this.readString(prompt);
                // Get the input line, and try and parse
                num = new BigDecimal(stringValue); // if it's 'bob' it'll break
                invalidInput = false; // or you can use 'break;'
            } catch (NumberFormatException e) {
                // If it explodes, it'll go here and do this.
                this.print("Input error. Please try again.");
            }
        }
        return num;
    }

    @Override
    public String readYesOrNo(String msg) {
        String yesOrNo;
        while (true){
            yesOrNo = readString(msg);
            if (yesOrNo.toUpperCase().equals("YES")||yesOrNo.toUpperCase().equals("NO"))
               return yesOrNo.toUpperCase();  

            else print("Please enter Yes or No ");

        }

    }

}
