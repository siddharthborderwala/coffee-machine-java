package com.siddharth;

import java.util.Scanner;

public class CoffeeMachine {
    public enum State {
        ACTION_MENU,
        BUY,
        FILL,
        TAKE,
        REMAINING,
        EXIT
    }

    private int money;
    private int numOfDisCups;
    private int coffeeBeanGrams;
    private int milkML;
    private int waterML;
    private State state;

    public CoffeeMachine(int money, int numOfDisCups, int coffeeBeanGrams, int milkML, int waterML) {
        this.money = money;
        this.numOfDisCups = numOfDisCups;
        this.coffeeBeanGrams = coffeeBeanGrams;
        this.milkML = milkML;
        this.waterML = waterML;
        this.state = State.ACTION_MENU;
    }

    private void setState(State newState) {
        this.state = newState;
    }

    private boolean areResourcesEnough(int itemNumber) {
        if (!checkWater(itemNumber)) {
            System.out.println("Sorry, not enough water!");
            return false;
        } else if (!checkMilk(itemNumber)) {
            System.out.println("Sorry, not enough milk!");
            return false;
        } else if (!checkCoffeeBeans(itemNumber)) {
            System.out.println("Sorry, not enough coffee!");
            return false;
        } else if (!checkCups()) {
            System.out.println("Sorry, not enough cups!");
            return false;
        } else {
            return true;
        }
    }

    private boolean checkCups() {
        return this.numOfDisCups > 0;
    }

    private boolean checkWater(int itemNumber) {
        return switch (itemNumber) {
            case 1 -> waterML >= 250;
            case 2 -> waterML >= 350;
            case 3 -> waterML >= 200;
            default -> false;
        };
    }

    private boolean checkCoffeeBeans(int itemNumber) {
        return switch (itemNumber) {
            case 1 -> coffeeBeanGrams >= 16;
            case 2 -> coffeeBeanGrams >= 20;
            case 3 -> coffeeBeanGrams >= 12;
            default -> false;
        };
    }

    private boolean checkMilk(int itemNumber) {
        return switch (itemNumber) {
            case 1 -> true;
            case 2 -> milkML >= 75;
            case 3 -> milkML >= 100;
            default -> false;
        };
    }

    private void printRemaining() {
        System.out.print("\n");
        System.out.println("The coffee machine has:");
        System.out.println(waterML+" ml of water");
        System.out.println(milkML+" ml of milk");
        System.out.println(coffeeBeanGrams +" gm of coffee beans");
        System.out.println(numOfDisCups+" no(s). of disposable cups");
        System.out.println("$"+money+" of money");
    }

    private void buy(String choice) {
        if (choice.equals("back")) {
            return;
        }
        int itemNumber = Integer.parseInt(choice);
        if (itemNumber > 3 || itemNumber < 1) {
            System.out.println("Invalid option");
            return;
        }

        if (areResourcesEnough(itemNumber)) {
            System.out.println("I have enough resources, making you a coffee!");
            if (itemNumber == 1) {
                money += 4;
                waterML -= 250;
                coffeeBeanGrams -= 16;
            } else if (itemNumber == 2) {
                money += 7;
                waterML -= 350;
                milkML -= 75;
                coffeeBeanGrams -= 20;
            } else {
                money += 6;
                waterML -= 200;
                milkML -= 100;
                coffeeBeanGrams -= 12;
            }
            numOfDisCups--;
        }
    }

    private void fill(String[] amounts) {
        this.waterML += Integer.parseInt(amounts[0]);
        this.milkML += Integer.parseInt(amounts[1]);
        this.coffeeBeanGrams += Integer.parseInt(amounts[2]);
        this.numOfDisCups += Integer.parseInt(amounts[3]);
    }

    private void take() {
        System.out.println("I gave you $"+money);
        money = 0;
    }

    public void operate(String input) {
        switch (this.state) {
            case BUY:
                buy(input);
                break;
            case FILL:
                fill(input.split(","));
                break;
            case TAKE:
                take();
                break;
            case REMAINING:
                printRemaining();
                break;
            case EXIT:
            default:
                break;
        }
        setState(State.ACTION_MENU);
    }

    public static void main() {
        Scanner scanner = new Scanner(System.in);
        String act;
        CoffeeMachine coffeeMachine = new CoffeeMachine(550, 9, 120, 540, 400);

        do {
            System.out.println("\nWrite action (buy, fill, take, remaining, exit):");
            act = scanner.nextLine();
            switch (act) {
                case "buy":
                    coffeeMachine.setState(State.BUY);
                    System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu");
                    coffeeMachine.operate(scanner.nextLine());
                    break;
                case "fill":
                    coffeeMachine.setState(State.FILL);
                    StringBuilder stringBuilder = new StringBuilder();
                    System.out.println("Write how many ml of water do you want to add:");
                    stringBuilder.append(scanner.nextLine()).append(",");
                    System.out.println("Write how many ml of milk do you want to add:");
                    stringBuilder.append(scanner.nextLine()).append(",");
                    System.out.println("Write how many grams of coffee beans do you want to add:");
                    stringBuilder.append(scanner.nextLine()).append(",");
                    System.out.println("Write how many disposable cups of coffee do you want to add:");
                    stringBuilder.append(scanner.nextLine());
                    coffeeMachine.operate(stringBuilder.toString());
                    break;
                case "take":
                    coffeeMachine.setState(State.TAKE);
                    coffeeMachine.operate("");
                    break;
                case "remaining":
                    coffeeMachine.setState(State.REMAINING);
                    coffeeMachine.operate("");
                    break;
                case "exit":
                    coffeeMachine.setState(State.EXIT);
                    coffeeMachine.operate("");
                default:
                    break;
            }
        } while (!act.equals("exit"));
    }
}
