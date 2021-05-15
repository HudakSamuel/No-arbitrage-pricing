package sk.stu.fei.project.service;

import jdk.nashorn.internal.codegen.CompilerConstants;
import sk.stu.fei.project.domain.AssetMovement;
import sk.stu.fei.project.domain.AssetTree;
import sk.stu.fei.project.domain.binary_tree.average_price.CallAverageBinaryTree;
import sk.stu.fei.project.domain.binary_tree.average_price.PutAverageBinaryTree;
import sk.stu.fei.project.domain.binary_tree.lookback.fixed_lookback.CallFixedLookbackBinaryTree;
import sk.stu.fei.project.domain.binary_tree.lookback.fixed_lookback.PutFixedLookbackBinaryTree;
import sk.stu.fei.project.domain.binary_tree.lookback.floating_lookback.CallFloatingLookbackBinaryTree;
import sk.stu.fei.project.domain.binary_tree.lookback.floating_lookback.PutFloatingLookbackBinaryTree;
import sk.stu.fei.project.service.asset_movement.AssetMovementImpl;
import sk.stu.fei.project.service.asset_movement.AssetMovementService;
import sk.stu.fei.project.service.binary_tree_builder.average_price_builder.AveragePriceBinaryTreeImpl;
import sk.stu.fei.project.service.binary_tree_builder.average_price_builder.AveragePriceBinaryTreeService;
import sk.stu.fei.project.service.binary_tree_builder.fixed_lookback_builder.FixedLookbackBinaryTreeImpl;
import sk.stu.fei.project.service.binary_tree_builder.fixed_lookback_builder.FixedLookbackBinaryTreeService;
import sk.stu.fei.project.service.binary_tree_builder.floating_lookback_builder.FloatingLookbackBinaryTreeImpl;
import sk.stu.fei.project.service.binary_tree_builder.floating_lookback_builder.FloatingLookbackBinaryTreeService;
import sk.stu.fei.project.service.tree_printer.AssetTreePrinter;
import sk.stu.fei.project.service.tree_printer.OptionTreePrinter;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

public class Init {

    public void init() throws IOException {
        AssetMovementService assetMovementService = new AssetMovementImpl();
        AssetBinaryTreeService assetBinaryTreeService = new AssetBinaryTreeImpl();
        AveragePriceBinaryTreeService averagePriceBinaryTreeService = new AveragePriceBinaryTreeImpl();
        AssetTreePrinter<AssetTree> assetTreePrinter = new AssetTreePrinter<AssetTree>();
        OptionTreePrinter optionTreePrinter = new OptionTreePrinter();
        FloatingLookbackBinaryTreeService floatingLookbackBinaryTreeService = new FloatingLookbackBinaryTreeImpl();
        FixedLookbackBinaryTreeService fixedLookbackBinaryTreeService = new FixedLookbackBinaryTreeImpl();

        Scanner input = new Scanner(System.in);
        BigDecimal strikePrice = null;

        AssetMovement assetMovement = this.inputValidMovementParams(assetMovementService, input);

        int numberOfOption = this.optionSelector(input);
        if (numberOfOption == 3 || numberOfOption == 4 || numberOfOption == 5 || numberOfOption == 6){
            strikePrice = this.strikePriceInput(input);
        }

        BigDecimal assetStartingPrice = this.assetPriceInput(input);

        for(int i = 0; i < 10; i++){
            System.out.println();
        }

        System.out.println("Underlying asset tree: \n");
        AssetTree assetTree = new AssetTree(assetStartingPrice, assetMovement);
        assetBinaryTreeService.buildTree(assetTree);
        assetTreePrinter.print(assetTree);

        switch (numberOfOption){
            case 1:

                CallFloatingLookbackBinaryTree callFloatingLookbackBinaryTree = new CallFloatingLookbackBinaryTree();
                floatingLookbackBinaryTreeService.buildCallFloatingLookbackBinaryTree(callFloatingLookbackBinaryTree, assetTree);

                if(assetMovement.steps < 5){
                    System.out.println("Option tree: \n");
                    optionTreePrinter.print(callFloatingLookbackBinaryTree);
                }

                System.out.println("Present price of the option is: " + callFloatingLookbackBinaryTree.getRoot().value);
                break;

            case 2:

                PutFloatingLookbackBinaryTree putFloatingLookbackBinaryTree = new PutFloatingLookbackBinaryTree();
                floatingLookbackBinaryTreeService.buildPutFloatingLookbackBinaryTree(putFloatingLookbackBinaryTree, assetTree);

                if(assetMovement.steps < 5){
                    System.out.println("Option tree: \n");
                    optionTreePrinter.print(putFloatingLookbackBinaryTree);
                }

                System.out.println("Present price of the option is: " + putFloatingLookbackBinaryTree.getRoot().value);
                break;

            case 3:
                CallFixedLookbackBinaryTree callFixedLookbackBinaryTree = new CallFixedLookbackBinaryTree(strikePrice);
                fixedLookbackBinaryTreeService.buildCallFixedLookbackBinaryTree(callFixedLookbackBinaryTree, assetTree);

                if(assetMovement.steps < 5){
                    System.out.println("Option tree: \n");
                    optionTreePrinter.print(callFixedLookbackBinaryTree);
                }

                System.out.println("Present price of the option is: " + callFixedLookbackBinaryTree.getRoot().value);


            case 4:
                PutFixedLookbackBinaryTree putFixedLookbackBinaryTree = new PutFixedLookbackBinaryTree(strikePrice);
                fixedLookbackBinaryTreeService.buildPutFixedLookbackBinaryTree(putFixedLookbackBinaryTree, assetTree);

                if(assetMovement.steps < 5){
                    System.out.println("Option tree: \n");
                    optionTreePrinter.print(putFixedLookbackBinaryTree);
                }

                System.out.println("Present price of the option is: " + putFixedLookbackBinaryTree.getRoot().value);
                break;

            case 5:
                CallAverageBinaryTree callAverageBinaryTree = new CallAverageBinaryTree(strikePrice);
                averagePriceBinaryTreeService.buildCallAveragePriceBinaryTree(callAverageBinaryTree, assetTree);

                if(assetMovement.steps < 5){
                    System.out.println("Option tree: \n");
                    optionTreePrinter.print(callAverageBinaryTree);
                }

                System.out.println("Present price of the option is: " + callAverageBinaryTree.getRoot().value);
                break;

            case 6:
                PutAverageBinaryTree putAverageBinaryTree = new PutAverageBinaryTree(strikePrice);
                averagePriceBinaryTreeService.buildPutAveragePriceBinaryTree(putAverageBinaryTree, assetTree);

                if(assetMovement.steps < 5){
                    System.out.println("Option tree: \n");
                    optionTreePrinter.print(putAverageBinaryTree);
                }

                System.out.println("Present price of the option is: " + putAverageBinaryTree.getRoot().value);
                break;
        }


    }

    private BigDecimal strikePriceInput(Scanner input){
        boolean validInput = false;
        BigDecimal result = null;

        while(!validInput){
            try{
                System.out.println("Enter strike price: ");
                double number = input.nextDouble();
                if (number < 0){
                    throw new Exception();
                }

                result = BigDecimal.valueOf(number);
                validInput = true;
                System.out.println();

            }catch (Exception e){
                System.out.println("Invalid input \n");
            }
        }

        return result;
    }

    private BigDecimal assetPriceInput(Scanner input){
        boolean validInput = false;
        BigDecimal result = null;

        while(!validInput){
            try{
                System.out.println("Enter starting price of underlying asset: ");
                double number = input.nextDouble();
                if (number < 0){
                    throw new Exception();
                }

                result = BigDecimal.valueOf(number);
                validInput = true;
                System.out.println();

            }catch (Exception e){
                System.out.println("Invalid input \n");
            }
        }

        return result;
    }

    private int optionSelector(Scanner input){
        boolean validInput = false;
        int result = 0;

        while(!validInput){
            try{
                System.out.println("Choose an option to manufacture: \n");

                System.out.println( "--------------------------- \n" +
                        "Floating Lookback Option \n" +
                        "--------------------------- \n" +
                        "1) Call Floating Lookback \n" +
                        "2) Put Floating Lookback \n \n");

                System.out.println("--------------------------- \n" +
                        "Fixed Lookback Option \n" +
                        "--------------------------- \n" +
                        "3) Call Fixed Lookback \n" +
                        "4) Put Fixed Lookback \n \n");

                System.out.println("--------------------------- \n" +
                        "Average Price Option \n" +
                        "--------------------------- \n" +
                        "5) Call Average Price Option \n" +
                        "6) Put Average Price Option \n \n");

                System.out.println("Choose an option (1-6): ");

                int number = input.nextInt();
                if (number > 6 || number < 1){
                    throw new Exception();
                }

                result = number;
                validInput = true;
                System.out.println();

            }catch(Exception e){
                System.out.println("Invalid input \n");
            }

        }

        return result;
    }

    private AssetMovement inputValidMovementParams(AssetMovementService assetMovementService, Scanner input) {
        while(true){
            double timePeriod = this.timeInput(input);
            int steps = this.stepsInput(input);
            double volatility =  this.volatilityInput(input);
            double interest = this.interestInput(input);

            AssetMovement assetMovement = new AssetMovement(volatility, timePeriod, interest, steps);

            if (assetMovementService.initAssetMovementParameters(assetMovement)) {
                return assetMovement;
            }
        }

    }

    private double timeInput(Scanner input){
        boolean validInput = false;
        double result = 0;

        while(!validInput){
            try{

                System.out.println("Input total time of option duration (ex. 30/365): ");
                String string = input.nextLine();

                String[] time = string.split("/");
                double first = Double.parseDouble(time[0]);
                int second = Integer.parseInt(time[1]);

                if ((first > 365) || (first < 1) || (second != 365) || (time.length != 2)){
                    throw new Exception();
                }

                result = first / second;
                validInput = true;
                System.out.println();

            }catch(Exception e){
                System.out.println("Invalid input \n");
            }
        }

        return result;

    }

    private int stepsInput(Scanner input){
        boolean validInput = false;
        int result = 0;

        while(!validInput){
            System.out.println("Input number of steps (max 21): ");
            String steps = input.next();

            try{

                int a = Integer.parseInt(steps);

                if (a < 1 || a > 21){
                    throw new Exception();
                }

                result = a;
                validInput = true;
                System.out.println();

            }catch(Exception e){
                System.out.println("Invalid input \n");

            }
        }

        return result;
    }

    private double volatilityInput(Scanner input){
        boolean validInput = false;
        double result = 0;

        while(!validInput){
            System.out.println("Input volatility: ");
            String volatility = input.next();
            try{

                double vola = Double.parseDouble(volatility);
                result = vola;

                validInput = true;
                System.out.println();

            }catch(Exception e){
                System.out.println("Invalid input \n");
            }
        }

        return result;
    }

    private double interestInput(Scanner input){
        boolean validInput = false;
        double result = 0;

        while(!validInput){
            System.out.println("Input interest: ");
            String interest = input.next();
            try{
                double inter = Double.parseDouble(interest);
                result = inter;

                validInput = true;
                System.out.println();

            }catch(Exception e){
                System.out.println("Invalid input \n");
            }
        }

        return result;
    }
}
