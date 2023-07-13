package res;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Stores all images used on the grid.
 */
public class Assets {

    public static BufferedImage emptyField;
    public static BufferedImage highSchool;
    public static BufferedImage police;
    public static BufferedImage stadium;
    public static BufferedImage university;
    public static BufferedImage copperI;
    public static BufferedImage factories;
    public static BufferedImage factory;
    public static BufferedImage goldI;
    public static BufferedImage mine;
    public static BufferedImage mines;
    public static BufferedImage ranches;
    public static BufferedImage ranch;
    public static BufferedImage silverI;
    public static BufferedImage bigHouse;
    public static BufferedImage bigHouses;
    public static BufferedImage copperR;
    public static BufferedImage goldR;
    public static BufferedImage house;
    public static BufferedImage houses;
    public static BufferedImage panel;
    public static BufferedImage panels;
    public static BufferedImage silverR;
    public static BufferedImage roadES;
    public static BufferedImage roadESW;
    public static BufferedImage roadEW;
    public static BufferedImage roadNE;
    public static BufferedImage roadNES;
    public static BufferedImage roadNESW;
    public static BufferedImage roadNS;
    public static BufferedImage roadSWN;
    public static BufferedImage roadSW;
    public static BufferedImage roadWNE;
    public static BufferedImage roadWN;
    public static BufferedImage bigShop;
    public static BufferedImage bigShops;
    public static BufferedImage copperS;
    public static BufferedImage goldS;
    public static BufferedImage mall;
    public static BufferedImage malls;
    public static BufferedImage shop;
    public static BufferedImage shops;
    public static BufferedImage silverS;

    public Assets() {
        try {
            emptyField = ResourceLoader.loadBufferedImage("res/png/emptyField.png");
            highSchool = ResourceLoader.loadBufferedImage("res/png/high_school.png");
            police = ResourceLoader.loadBufferedImage("res/png/police2.png");
            stadium = ResourceLoader.loadBufferedImage("res/png/stadium.jpg");
            university = ResourceLoader.loadBufferedImage("res/png/university2.png");
            //industrys
            copperI = ResourceLoader.loadBufferedImage("res/png/industry/copperI.png");
            factories = ResourceLoader.loadBufferedImage("res/png/industry/factory_golds.png");
            factory = ResourceLoader.loadBufferedImage("res/png/industry/factory_gold.png");
            goldI = ResourceLoader.loadBufferedImage("res/png/industry/goldI.png");
            mine = ResourceLoader.loadBufferedImage("res/png/industry/mine2.jpg");
            mines = ResourceLoader.loadBufferedImage("res/png/industry/mines2.jpg");
            ranches = ResourceLoader.loadBufferedImage("res/png/industry/newranches.jpg");
            ranch = ResourceLoader.loadBufferedImage("res/png/industry/newranch.jpg");
            silverI = ResourceLoader.loadBufferedImage("res/png/industry/silverI.png");
            //residence
            panel = ResourceLoader.loadBufferedImage("res/png/residence/scyscrapers.png");
            panels = ResourceLoader.loadBufferedImage("res/png/residence/scyscraper.png");
            copperR = ResourceLoader.loadBufferedImage("res/png/residence/copperR.png");
            goldR = ResourceLoader.loadBufferedImage("res/png/residence/goldR.png");
            house = ResourceLoader.loadBufferedImage("res/png/residence/house.png");
            houses = ResourceLoader.loadBufferedImage("res/png/residence/houses.png");
            bigHouse = ResourceLoader.loadBufferedImage("res/png/residence/panel.png");
            bigHouses = ResourceLoader.loadBufferedImage("res/png/residence/panels.png");
            silverR = ResourceLoader.loadBufferedImage("res/png/residence/silverR.png");
            //road
            roadES = ResourceLoader.loadBufferedImage("res/png/road/roadES.png");
            roadESW = ResourceLoader.loadBufferedImage("res/png/road/roadESW.png");
            roadEW = ResourceLoader.loadBufferedImage("res/png/road/roadEW.png");
            roadNE = ResourceLoader.loadBufferedImage("res/png/road/roadNE.png");
            roadNES = ResourceLoader.loadBufferedImage("res/png/road/roadNES.png");
            roadNESW = ResourceLoader.loadBufferedImage("res/png/road/roadNESW.png");
            roadNS = ResourceLoader.loadBufferedImage("res/png/road/roadNS.png");
            roadSWN = ResourceLoader.loadBufferedImage("res/png/road/roadSWN.png");
            roadSW = ResourceLoader.loadBufferedImage("res/png/road/roadSW.png");
            roadWNE = ResourceLoader.loadBufferedImage("res/png/road/roadWNE.png");
            roadWN = ResourceLoader.loadBufferedImage("res/png/road/roadWN.png");
            //Service
            bigShop = ResourceLoader.loadBufferedImage("res/png/service/BIGSHOP2.png");
            bigShops = ResourceLoader.loadBufferedImage("res/png/service/BIGSHOPS2.png");
            copperS = ResourceLoader.loadBufferedImage("res/png/service/copperS.png");
            goldS = ResourceLoader.loadBufferedImage("res/png/service/goldS.png");
            mall = ResourceLoader.loadBufferedImage("res/png/service/malls2.png");
            malls = ResourceLoader.loadBufferedImage("res/png/service/mall2.png");
            shop = ResourceLoader.loadBufferedImage("res/png/service/newshop.jpg");
            shops = ResourceLoader.loadBufferedImage("res/png/service/newshops.jpg");
            silverS = ResourceLoader.loadBufferedImage("res/png/service/silverS.png");
        } catch (IOException e) {
        }
    }

}
