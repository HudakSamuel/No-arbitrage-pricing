package sk.stu.fei.project.service;

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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class Init {

    /**
     * Method initializes all services necessary to create asset tree and option binary tree.
     * Also initializes console interface to accept user inputs
     * @throws IOException because of option tree printer
     */
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

                BigDecimal value = callFloatingLookbackBinaryTree.getRoot().value.setScale(2, RoundingMode.HALF_EVEN);
                System.out.println();
                System.out.println("Present price of the option is: " + value);
                break;

            case 2:

                PutFloatingLookbackBinaryTree putFloatingLookbackBinaryTree = new PutFloatingLookbackBinaryTree();
                floatingLookbackBinaryTreeService.buildPutFloatingLookbackBinaryTree(putFloatingLookbackBinaryTree, assetTree);

                if(assetMovement.steps < 5){
                    System.out.println("Option tree: \n");
                    optionTreePrinter.print(putFloatingLookbackBinaryTree);
                }

                value = putFloatingLookbackBinaryTree.getRoot().value.setScale(2, RoundingMode.HALF_EVEN);
                System.out.println();
                System.out.println("Present price of the option is: " + value);
                break;

            case 3:
                CallFixedLookbackBinaryTree callFixedLookbackBinaryTree = new CallFixedLookbackBinaryTree(strikePrice);
                fixedLookbackBinaryTreeService.buildCallFixedLookbackBinaryTree(callFixedLookbackBinaryTree, assetTree);

                if(assetMovement.steps < 5){
                    System.out.println("Option tree: \n");
                    optionTreePrinter.print(callFixedLookbackBinaryTree);
                }

                value = callFixedLookbackBinaryTree.getRoot().value.setScale(2, RoundingMode.HALF_EVEN);
                System.out.println();
                System.out.println("Present price of the option is: " + value);
                break;


            case 4:
                PutFixedLookbackBinaryTree putFixedLookbackBinaryTree = new PutFixedLookbackBinaryTree(strikePrice);
                fixedLookbackBinaryTreeService.buildPutFixedLookbackBinaryTree(putFixedLookbackBinaryTree, assetTree);

                if(assetMovement.steps < 5){
                    System.out.println("Option tree: \n");
                    optionTreePrinter.print(putFixedLookbackBinaryTree);
                }

                value = putFixedLookbackBinaryTree.getRoot().value.setScale(2, RoundingMode.HALF_EVEN);
                System.out.println();
                System.out.println("Present price of the option is: " + value);
                break;

            case 5:
                CallAverageBinaryTree callAverageBinaryTree = new CallAverageBinaryTree(strikePrice);
                averagePriceBinaryTreeService.buildCallAveragePriceBinaryTree(callAverageBinaryTree, assetTree);

                if(assetMovement.steps < 5){
                    System.out.println("Option tree: \n");
                    optionTreePrinter.print(callAverageBinaryTree);
                }

                value = callAverageBinaryTree.getRoot().value.setScale(2, RoundingMode.HALF_EVEN);
                System.out.println();
                System.out.println("Present price of the option is: " + value);
                break;

            case 6:
                PutAverageBinaryTree putAverageBinaryTree = new PutAverageBinaryTree(strikePrice);
                averagePriceBinaryTreeService.buildPutAveragePriceBinaryTree(putAverageBinaryTree, assetTree);

                if(assetMovement.steps < 5){
                    System.out.println("Option tree: \n");
                    optionTreePrinter.print(putAverageBinaryTree);
                }

                value = putAverageBinaryTree.getRoot().value.setScale(2, RoundingMode.HALF_EVEN);
                System.out.println();
                System.out.println("Present price of the option is: " + value);
                break;
        }


    }

    /**
     * Creates prompt for strike price input from user. Validates input
     * @param input console scanner
     * @return returns strike price
     */
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


    /**
     * Creates prompt for starting asset price input from user. Validates input
     * @param input console scanner
     * @return returns starting asset price
     */
    private BigDecimal assetPriceInput(Scanner input){
        boolean validInput = false;
        BigDecimal result = null;

        while(!validInput){
            try{
                System.out.println("Enter starting price of underlying asset: ");
                double number = input.nextDouble();
                if (number < 0 || number > 9999){
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


    /**
     * Creates prompt with selection of options types to manufacture. Validates input from user
     * @param input console scanner
     * @return returns number which corresponds to option type
     */
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


    /**
     * Method calls all necessary methods to fill AssetMovement from user input.
     * Also checks if input meets no-arbitrage condition
     * @param input console scanner
     * @param assetMovementService needed to call method which inits movement params
     * @return returns initialized AssetMovement
     */
    private AssetMovement inputValidMovementParams(AssetMovementService assetMovementService, Scanner input) {
        while(true){
            BigDecimal timePeriod = this.timeInput(input);
            int steps = this.stepsInput(input);
            BigDecimal volatility =  this.volatilityInput(input);
            BigDecimal interest = this.interestInput(input);

            AssetMovement assetMovement = new AssetMovement(volatility, timePeriod, interest, steps);

            if (assetMovementService.initAssetMovementParameters(assetMovement)) {
                return assetMovement;
            }
        }

    }


    /**
     * Creates prompt for total time input from user. Validates input
     * @param input console scanner
     * @return returns total time
     */
    private BigDecimal timeInput(Scanner input){
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

        return BigDecimal.valueOf(result);

    }


    /**
     * Creates prompt for asset and option tree steps input from user. Validates input
     * @param input console scanner
     * @return returns steps
     */
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


    /**
     * Creates prompt for volatility input from user. Validates input
     * @param input console scanner
     * @return returns volatility
     */
    private BigDecimal volatilityInput(Scanner input){
        boolean validInput = false;
        double result = 0;

        while(!validInput){
            System.out.println("Input volatility: ");
            String volatility = input.next();
            try{

                double vola = Double.parseDouble(volatility);

                if (vola <= 0 || vola > 9){
                    throw new Exception();
                }
                result = vola;

                validInput = true;
                System.out.println();

            }catch(Exception e){
                System.out.println("Invalid input \n");
            }
        }

        return BigDecimal.valueOf(result);
    }


    /**
     * Creates prompt for interest input from user. Validates input
     * @param input console scanner
     * @return returns interest
     */
    private BigDecimal interestInput(Scanner input) {
        boolean validInput = false;
        double result = 0;

        while (!validInput) {
            System.out.println("Input interest: ");
            String interest = input.next();
            try {
                double inter = Double.parseDouble(interest);

                if (inter < 0){
                    throw new Exception();
                }

                result = inter;

                validInput = true;
                System.out.println();

            } catch (Exception e) {
                System.out.println("Invalid input \n");
            }
        }

        return BigDecimal.valueOf(result);
    }
}
