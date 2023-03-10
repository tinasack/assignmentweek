package com.accenture.theincredibles.assignment.tinasack;

import com.accenture.theincredibles.assignment.tinasack.commands.*;
import com.accenture.theincredibles.assignment.tinasack.commands.inputCommands.*;
import com.accenture.theincredibles.assignment.tinasack.repositories.CompanyRepository;
import com.accenture.theincredibles.assignment.tinasack.repositories.IndustryRepository;
import com.accenture.theincredibles.assignment.tinasack.repositories.StockRepository;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/* Class to run the REPL application */
public class StartApp {

    public void execute() throws SQLException, IOException {
        Connection connection;
        try {
            connection = new Connector().openConnection();
        } catch(SQLException sqlException) {
            System.out.println("Could not connect to database");
            sqlException.printStackTrace();
            return;
        }
        Scanner getInput = new Scanner(System.in);

        StockRepository stockRepo = new StockRepository(connection);
        IndustryRepository industryRepo = new IndustryRepository(connection);
        CompanyRepository companyRepo = new CompanyRepository(connection);

        List<Command> simpleCommands = createSimpleCommandList(industryRepo, stockRepo, companyRepo);
        List<InputCommand> inputCommands = createInputCommandList(industryRepo, stockRepo, companyRepo);

        System.out.println("Welcome to ISA-Stock-Application!");


        boolean run = true;
        while(run)  {
            System.out.println("What would you like to do?");
            System.out.print(">> ");

            String input = getInput.nextLine();
            String[] inputArguments = input.split(" ");

            if(inputArguments.length > 1) {
                for (InputCommand inputCommand : inputCommands) {
                    if (inputCommand.shouldExecute(input)) {
                        run = inputCommand.execute(input);
                    }
                }
            } else {
                for (Command simplecommand : simpleCommands) {
                    if (simplecommand.shouldExecute(input)) {
                        run = simplecommand.execute();
                    }
                }
            }
        }
    }
    public List<Command> createSimpleCommandList(IndustryRepository industryRepo, StockRepository stockRepo, CompanyRepository companyRepo){
        List<Command> simpleCommands = new ArrayList<>();
        simpleCommands.add(new ExitCommand());
        simpleCommands.add(new IndustryCommand(industryRepo, stockRepo));
        simpleCommands.add(new DeleteCommand(industryRepo, stockRepo, companyRepo));
        return simpleCommands;
    }

    public List<InputCommand> createInputCommandList(IndustryRepository industryRepo, StockRepository stockRepo, CompanyRepository companyRepo){
        List<InputCommand> inputCommands = new ArrayList<>();
        inputCommands.add(new ShowStockCommand(stockRepo, companyRepo));
        inputCommands.add(new ShowMaxCommand(stockRepo, companyRepo));
        inputCommands.add(new ShowLowCommand(stockRepo, companyRepo));
        inputCommands.add(new ShowGapCommand(stockRepo, companyRepo));
        inputCommands.add(new UpdateIndustryCommand(stockRepo, industryRepo));
        inputCommands.add(new AddPriceCommand(stockRepo, companyRepo));
        inputCommands.add(new SearchCommand(companyRepo));
        inputCommands.add(new ImportCommand(stockRepo, companyRepo, industryRepo));

        return inputCommands;
    }
}
