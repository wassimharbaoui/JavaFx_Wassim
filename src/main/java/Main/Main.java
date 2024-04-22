package Main;

import Entities.Evenement;
import Entities.Ticket;
import Entities.User;
import Services.EvenementService;
import Services.EvenementService;
import Services.UserService;


import java.text.SimpleDateFormat;


import java.util.List;

public class Main {

    public static void main(String[] args) {


        EvenementService us = new EvenementService();

        List<Evenement> xx =us.readAll();

        for (Evenement i: xx){

            System.out.println(i);
        }
        
        






    
}}
