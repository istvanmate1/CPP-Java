package model;

import bigcity.EducationLevel;
import bigcity.HighSchool;
import bigcity.Industry;
import bigcity.Person;
import bigcity.Police;
import bigcity.PrivateZone;
import bigcity.Residence;
import bigcity.Road;
import bigcity.Service;
import bigcity.Stadium;
import bigcity.University;
import bigcity.Workplace;
import bigcity.Zone;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import res.Assets;
import view.BigCityJframe;

public class Engine {

    private Zone[][] grid;
    private ArrayList<Person> residents;
    private List<HighSchool> highSchools;
    private List<University> universities;
    private List<Zone> buildings;

    private int width;
    private int height;

    private int money;
    private double yearlyIncome;
    private double expenses;
    private double combinedHappiness;
    private String date;
    private int timeSpeed;
    private int taxPercentage;
    private String name;

    private double disasterChance;
    private Random rnd;
    private int daysPassedWithoutDisaster;

    private static CursorSignal cursorSignal = CursorSignal.SELECT;

    private BufferedImage img;

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    private int fieldSize;

    private BigCityJframe bigCityJframe;

    private final int fixGroupOfPeopleCount = 10;

    private CitizenGenerator citizenGenerator;

    private int industryWorkersCount = 0;
    private int serviceWorkersCount = 0;

    private int yearsWithNegativeBudgetCount = 0;
    private int negativeBudgetStartYear = 0;
    private int negativeBudgetCurrentYear = 0;

    /**
     * Constructor
     * @param width         - int, width of the field
     * @param height        - int, heigh of the field
     * @param fieldSize     - int, the pixel count of a single zone square
     * @param bigCityJframe - bigCityJframe, the owner of this engine
     */
    public Engine(int width, int height, int fieldSize, BigCityJframe bigCityJframe) {
        this.width = width;
        this.height = height;
        this.fieldSize = fieldSize;
        this.money = 1000;
        this.yearlyIncome = 0;
        this.expenses = 0;
        this.bigCityJframe = bigCityJframe;
        this.taxPercentage = 20;
        this.disasterChance = 0;
        this.rnd = new Random();
        this.daysPassedWithoutDisaster = 0;
        this.highSchools = new ArrayList<>();
        this.universities = new ArrayList<>();
        this.buildings = new ArrayList<>();
        this.name = bigCityJframe.getCityName();
        grid = new Zone[height][width];
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {
                grid[row][column] = null;
            }
        }
        this.residents = new ArrayList<>();
        this.citizenGenerator = new CitizenGenerator();
    }

    public Engine(int width, int height, int fieldSize, String cityName) {
        this.width = width;
        this.height = height;
        this.fieldSize = fieldSize;
        this.money = 1000;
        this.taxPercentage = 20;
        this.disasterChance = 0;
        this.rnd = new Random();
        this.daysPassedWithoutDisaster = 0;
        this.highSchools = new ArrayList<>();
        this.universities = new ArrayList<>();
        this.buildings = new ArrayList<>();
        this.name = cityName;
        grid = new Zone[height][width];
        for (int column = 0; column < width; column++) {
            for (int row = 0; row < height; row++) {
                grid[row][column] = null;
            }
        }
        this.residents = new ArrayList<>();
        this.citizenGenerator = new CitizenGenerator();
    }

    public void setImg(int row, int column, BufferedImage image){
        bigCityJframe.getGrid().placeImage(row,column,image);
    }
    public void removeImg(int row, int column){
        bigCityJframe.getGrid().removeImage(row,column);
    }
    
    /**
     * Constructor used for load an existing city
     * @param cityName      - Stirng, the name of the city
     * @param bigCityJframe - BigCityJframe, owner of this engine
     */
    public Engine(String cityName, BigCityJframe bigCityJframe) {
        try ( BufferedReader reader = new BufferedReader(new FileReader("savedGames/" + cityName + ".txt"))) {
            String line = reader.readLine();
            if (!line.equals(cityName)) {
                throw new IOException("Incorrect city name!");
            }

            line = reader.readLine();
            this.height = Integer.parseInt(line.split(";")[0]);
            this.width = Integer.parseInt(line.split(";")[1]);

            line = reader.readLine();
            this.fieldSize = Integer.parseInt(line);

            line = reader.readLine();
            int money = Integer.parseInt(line);

            line = reader.readLine();
            this.yearlyIncome = Double.parseDouble(line);

            line = reader.readLine();
            this.expenses = Double.parseDouble(line);

            line = reader.readLine();
            bigCityJframe.setDate(Long.parseLong(line));

            line = reader.readLine();
            this.taxPercentage = Integer.parseInt(line);

            line = reader.readLine();
            this.disasterChance = Double.parseDouble(line);

            line = reader.readLine();
            this.daysPassedWithoutDisaster = Integer.parseInt(line);

            this.bigCityJframe = bigCityJframe;
            this.rnd = new Random();
            this.highSchools = new ArrayList<>();
            this.universities = new ArrayList<>();
            this.buildings = new ArrayList<>();
            this.name = bigCityJframe.getCityName();
            grid = new Zone[height][width];
            for (int column = 0; column < width; column++) {
                for (int row = 0; row < height; row++) {
                    grid[row][column] = null;
                }
            }
            residents = new ArrayList<>();

            line = reader.readLine();
            while (line != null && line.split(";").length == 4) {
                int col = Integer.parseInt(line.split(";")[0]);
                int row = Integer.parseInt(line.split(";")[1]);
                String type = line.split(";")[2];
                int blvl = Integer.parseInt(line.split(";")[3]);

                if (type.equals("Residence")) {
                    cursorSignal = CursorSignal.RESIDENCE;
                } else if (type.equals("Industry")) {
                    cursorSignal = CursorSignal.INDUSTRY;
                } else if (type.equals("Service")) {
                    cursorSignal = CursorSignal.SERVICE;
                } else if (type.equals("Police")) {
                    cursorSignal = CursorSignal.POLICE;
                } else if (type.equals("Stadium")) {
                    cursorSignal = CursorSignal.STADIUM;
                } else if (type.equals("HighSchool")) {
                    cursorSignal = CursorSignal.HIGH_SCHOOL;
                } else if (type.equals("University")) {
                    cursorSignal = CursorSignal.UNIVERSITY;
                } else if (type.equals("Road")) {
                    cursorSignal = CursorSignal.ROAD;
                }

                build(row, col, fieldSize, true);

                if (grid[row][col] instanceof PrivateZone zone) {
                    while (zone.getLevel() < blvl) {
                        zone.upgrade();
                    }
                }
                line = reader.readLine();
            }

            while (line != null && line.split(";").length == 9) {
                String splitted[] = line.split(";");
                String pName = splitted[0];
                int pAge = Integer.parseInt(splitted[1]);
                boolean pMale = Boolean.parseBoolean(splitted[2]);
                double pHappiness = Double.parseDouble(splitted[3]);
                EducationLevel pEducation = EducationLevel.valueOf(splitted[4]);
                int homeCol = Integer.parseInt(splitted[5]);
                int homeRow = Integer.parseInt(splitted[6]);
                Zone pHome = grid[homeRow][homeCol];
                int jobCol = Integer.parseInt(splitted[7]);
                int jobRow = Integer.parseInt(splitted[8]);
                Zone pJob = null;
                if (jobCol != -1) {
                    pJob = grid[jobRow][jobCol];
                }

                Person p = new Person(pName, pAge, pHappiness,
                        pMale, pEducation, pHome, pJob);

                residents.add(p);

                ((Residence) pHome).addPerson(p);
                if (null != pJob) {
                    ((Workplace) pJob).addPerson(p);
                }

                line = reader.readLine();
            }

            cursorSignal = CursorSignal.SELECT;
            this.citizenGenerator = new CitizenGenerator();
            this.money = money;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public Zone getCell(int row, int column) {
        return grid[row][column];
    }

    public static void setCursorSignal(CursorSignal signal) {
        cursorSignal = signal;
    }

    /**
     * Generates and places a zone on the grid according the cursorSignal.
     *
     * @param rowStart The row of the top left field of the area we try to build
     * on.
     * @param columnStart The column of the top left field of the area we try to
     * build on.
     * @param fieldSize The size of a field in pixels.
     * @param load True if we try to load a saved game. False if the user
     * builds.
     * @return True if successfully built a zone.
     */
    public boolean build(int rowStart, int columnStart, int fieldSize, boolean load) {
        int rowEnd = rowStart + cursorSignal.getHeight() - 1;
        int columnEnd = columnStart + cursorSignal.getWidth() - 1;
        if (false == areaInsideGridAndFree(rowStart, rowEnd, columnStart, columnEnd)) {
            return false;
        }

        Zone zone = null;
        int topLeftX = columnStart * fieldSize;
        int topLeftY = rowStart * fieldSize;

        if (null != cursorSignal) {
            switch (cursorSignal) {
                case POLICE -> {
                    zone = new Police(topLeftX, topLeftY,
                            cursorSignal.getPriceL1());
                    setImg(Assets.police);
                }
                case STADIUM -> {
                    zone = new Stadium(topLeftX, topLeftY,
                            cursorSignal.getPriceL1());
                    setImg(Assets.stadium);
                }
                case HIGH_SCHOOL -> {
                    HighSchool tmp = new HighSchool(topLeftX, topLeftY,
                            cursorSignal.getPriceL1());
                    zone = tmp;
                    highSchools.add(tmp);
                    setImg(Assets.highSchool);
                }
                case UNIVERSITY -> {
                    University tmp = new University(topLeftX, topLeftY,
                            cursorSignal.getPriceL1());
                    zone = tmp;
                    universities.add(tmp);
                    setImg(Assets.university);
                }
                case ROAD -> {
                    zone = new Road(topLeftX, topLeftY,
                            cursorSignal.getPriceL1());
                    setImg(Assets.roadNS);
                }
                case RESIDENCE -> {
                    zone = new Residence(topLeftX, topLeftY,
                            cursorSignal.getPriceL1());
                    setImg(Assets.copperR);
                }
                case INDUSTRY -> {
                    zone = new Industry(topLeftX, topLeftY,
                            cursorSignal.getPriceL1());
                    setImg(Assets.copperI);
                }
                case SERVICE -> {
                    zone = new Service(topLeftX, topLeftY,
                            cursorSignal.getPriceL1());
                    setImg(Assets.copperS);
                }
                default -> {
                    setImg(null);
                    return false;
                }
            }
        }

        addMoney(-cursorSignal.getPriceL1());

        zone.setCursorSignal(cursorSignal);
        zone.setImg(img);

        for (int row = rowStart; row <= rowEnd; row++) {
            for (int column = columnStart; column <= columnEnd; column++) {
                grid[row][column] = zone;
            }
        }

        if (cursorSignal == CursorSignal.ROAD) {
            //new road
            refreshImgOfRoad(zone, rowStart, columnStart);
            //old roads
            refreshRoadImgsAround(rowStart, columnStart);
        }

        moveEveryOne();

        if (!load) {
            bigCityJframe.refreshGrid();
        }
        buildings.add(zone);

        return true;
    }

    public boolean buildForTesting(int rowStart, int columnStart, int fieldSize, boolean load) {
        int rowEnd = rowStart + cursorSignal.getHeight() - 1;
        int columnEnd = columnStart + cursorSignal.getWidth() - 1;
        if (false == areaInsideGridAndFree(rowStart, rowEnd, columnStart, columnEnd)) {
            return false;
        }

        Zone zone = null;
        int topLeftX = columnStart * fieldSize;
        int topLeftY = rowStart * fieldSize;

        if (null != cursorSignal) {
            switch (cursorSignal) {
                case POLICE -> {
                    zone = new Police(topLeftX, topLeftY,
                            cursorSignal.getPriceL1());
                    setImg(Assets.police);
                }
                case STADIUM -> {
                    zone = new Stadium(topLeftX, topLeftY,
                            cursorSignal.getPriceL1());
                    setImg(Assets.stadium);
                }
                case HIGH_SCHOOL -> {
                    HighSchool tmp = new HighSchool(topLeftX, topLeftY,
                            cursorSignal.getPriceL1());
                    zone = tmp;
                    highSchools.add(tmp);
                    setImg(Assets.highSchool);
                }
                case UNIVERSITY -> {
                    University tmp = new University(topLeftX, topLeftY,
                            cursorSignal.getPriceL1());
                    zone = tmp;
                    universities.add(tmp);
                    setImg(Assets.university);
                }
                case ROAD -> {
                    zone = new Road(topLeftX, topLeftY,
                            cursorSignal.getPriceL1());
                    setImg(Assets.roadNS);
                }
                case RESIDENCE -> {
                    zone = new Residence(topLeftX, topLeftY,
                            cursorSignal.getPriceL1());
                    setImg(Assets.copperR);
                }
                case INDUSTRY -> {
                    zone = new Industry(topLeftX, topLeftY,
                            cursorSignal.getPriceL1());
                    setImg(Assets.copperI);
                }
                case SERVICE -> {
                    zone = new Service(topLeftX, topLeftY,
                            cursorSignal.getPriceL1());
                    setImg(Assets.copperS);
                }
                default -> {
                    setImg(null);
                    return false;
                }
            }
        }

        addMoney(-cursorSignal.getPriceL1());

        zone.setCursorSignal(cursorSignal);
        zone.setImg(img);

        for (int row = rowStart; row <= rowEnd; row++) {
            for (int column = columnStart; column <= columnEnd; column++) {
                grid[row][column] = zone;
            }
        }

        if (cursorSignal == CursorSignal.ROAD) {
            //new road
            refreshImgOfRoad(zone, rowStart, columnStart);
            //old roads
            refreshRoadImgsAround(rowStart, columnStart);
        }

        moveEveryOne();

        buildings.add(zone);

        return true;
    }

    /**
     * Changes the image of a road according its surroundings(roads around it).
     *
     * @param row The road's y position on the grid.
     * @param column The road's x position on the grid.
     */
    private void refreshRoadImgsAround(int row, int column) {
        //Up
        if (true == roadAndInsideGrid(row - 1, column)) {
            refreshImgOfRoad(grid[row - 1][column], row - 1,
                    column);
        }
        //Down
        if (true == roadAndInsideGrid(row + 1, column)) {
            refreshImgOfRoad(grid[row + 1][column], row + 1,
                    column);
        }
        //Right
        if (true == roadAndInsideGrid(row, column + 1)) {
            refreshImgOfRoad(grid[row][column + 1], row,
                    column + 1);
        }
        //Left
        if (true == roadAndInsideGrid(row, column - 1)) {
            refreshImgOfRoad(grid[row][column - 1], row,
                    column - 1);
        }
    }

    public CursorSignal getCursorSignal() {
        return cursorSignal;
    }

    /**
     * Checks whether the specified area is inside the grid and no zones built
     * on it(free area) or not.
     *
     * @param rowStart The y position of the top left field of the area.
     * @param rowEnd The y position of the bottom right field of the area.
     * @param columnStart The x position of the top left field of the area.
     * @param columnEnd The x position of the bottom right field of the area.
     * @return True if the specified area if free and isn't outside of the grid.
     */
    public boolean areaInsideGridAndFree(int rowStart, int rowEnd,
            int columnStart, int columnEnd) {
        for (int row = rowStart; row <= rowEnd; row++) {
            for (int column = columnStart; column <= columnEnd; column++) {
                if (row >= height || column >= width || row < 0 || column < 0) {
                    //Out of grid.
                    return false;
                }
                if (null != grid[row][column]) {
                    //At least one field isn't free.
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Destroys a zone on the grid.
     *
     * @param argRow The y position where we want to destory a zone.
     * @param argColumn The x position where we want to destory a zone.
     * @param fieldSize The size of a field in pixels.
     * @param disasterHappened True if called from a disaster. False if the user
     * wants to destroy a zone.
     * @return True if successfully destroyed a zone.
     */
    public boolean destroyZone(int argRow, int argColumn, int fieldSize,
            boolean disasterHappened) {
        Zone target = grid[argRow][argColumn];
        if (null == target) {
            return false;
        }

        int zoneLevel = 1;
        CursorSignal type;

        boolean approvedConflictualDestruction = false;

        if (target instanceof Residence tmp) {
            zoneLevel = tmp.getLevel();
            type = CursorSignal.RESIDENCE;
            if (disasterHappened) {
                tmp.getResidents().forEach(person
                        -> person.changeHappinessBy(-1));
            } else if (peopleOnPrivateZone((PrivateZone) target)) {
                if (bigCityJframe.conflictualDestructionOkCancleDialog(
                        "People live on this residence. Destroying it will "
                        + "decrease the happiness of people living here. "
                        + "Are you sure you want to destroy it?")) {
                    //System.out.println("Approved");
                    tmp.getResidents().forEach(person
                            -> person.changeHappinessBy(-1));
                    approvedConflictualDestruction = true;
                } else {
                    //System.out.println("Canceled");
                    return false;
                }
            }
        } else if (target instanceof Industry tmp) {
            zoneLevel = tmp.getLevel();
            type = CursorSignal.INDUSTRY;
            if (disasterHappened) {
                tmp.getWorkers().forEach(person
                        -> person.changeHappinessBy(-1));
            } else if (peopleOnPrivateZone((PrivateZone) target)) {
                if (bigCityJframe.conflictualDestructionOkCancleDialog(
                        "People have a job on this zone. Destroying it will "
                        + "decrease the happiness of people working here. "
                        + "Are you sure you want to destroy it?")) {
                    //System.out.println("Approved");
                    tmp.getWorkers().forEach(person
                            -> person.changeHappinessBy(-1));
                    approvedConflictualDestruction = true;
                } else {
                    //System.out.println("Canceled");
                    return false;
                }
            }
        } else if (target instanceof Service tmp) {
            zoneLevel = tmp.getLevel();
            type = CursorSignal.SERVICE;
            if (disasterHappened) {
                tmp.getWorkers().forEach(person
                        -> person.changeHappinessBy(-1));
            } else if (peopleOnPrivateZone((PrivateZone) target)) {
                if (bigCityJframe.conflictualDestructionOkCancleDialog(
                        "People have a job on this zone. Destroying it will "
                        + "decrease the happiness of people working here. "
                        + "Are you sure you want to destroy it?")) {
                    //System.out.println("Approved");
                    tmp.getWorkers().forEach(person
                            -> person.changeHappinessBy(-1));
                    approvedConflictualDestruction = true;
                } else {
                    //System.out.println("Canceled");
                    return false;
                }
            }
        } else if (target instanceof Road tmp) {
            type = CursorSignal.ROAD;
            ArrayList<Person> angryPeople
                    = peopleWhoCantFindTheirJobAnymore(tmp);
            if (disasterHappened) {
                angryPeople.forEach(person
                        -> person.changeHappinessBy(-1));
            } else if (!angryPeople.isEmpty()) {
                //System.out.println(angryPeople.size());
                //dialog
                if (bigCityJframe.conflictualDestructionOkCancleDialog(
                        angryPeople.size()
                        + " people won't be able to reach their workplace if"
                        + " you destory this road. Destroying it will "
                        + "decrease the happiness of these people. "
                        + "Are you sure you want to destroy it?")) {
                    //System.out.println("Approved");
                    angryPeople.forEach(person
                            -> person.changeHappinessBy(-1));
                    approvedConflictualDestruction = true;
                } else {
                    //System.out.println("Canceled");
                    return false;
                }
            }
        } else if (target instanceof Police) {
            type = CursorSignal.POLICE;
        } else if (target instanceof Stadium) {
            type = CursorSignal.STADIUM;
        } else if (target instanceof HighSchool highSchool) {
            type = CursorSignal.HIGH_SCHOOL;
            highSchools.remove(highSchool);
        } else {
            type = CursorSignal.UNIVERSITY;
            universities.remove((University) target);
        }

        buildings.remove(target);

        CursorSignal targetSignal = target.getCursorSignal();
        int rowStart = target.getTopLeftY() / fieldSize;
        int rowEnd = target.getTopLeftY() / fieldSize
                + target.getCursorSignal().getHeight() - 1;
        int columnStart = target.getTopLeftX() / fieldSize;
        int columnEnd = target.getTopLeftX() / fieldSize
                + target.getCursorSignal().getWidth() - 1;
        for (int row = rowStart; row <= rowEnd; row++) {
            for (int column = columnStart; column <= columnEnd; column++) {
                grid[row][column] = null;
            }
        }

        if (CursorSignal.ROAD == targetSignal) {
            refreshRoadImgsAround(argRow, argColumn);
        }

        if (!disasterHappened && !approvedConflictualDestruction) {
            int returnMoney = type.getPriceL1()
                    + (zoneLevel > 1 ? type.getPriceL2() : 0)
                    + (zoneLevel > 2 ? type.getPriceL3() : 0);
            addMoney(returnMoney / 2);

        }
        if (approvedConflictualDestruction) {
            int compensationMoney = type.getPriceL1()
                    + (zoneLevel > 1 ? type.getPriceL2() : 0)
                    + (zoneLevel > 2 ? type.getPriceL3() : 0);
            addMoney(-1 * compensationMoney / 2);
        }

        moveEveryOne();

        bigCityJframe.refreshGrid();

        return true;
    }

    public boolean destroyZoneForTesting(int argRow, int argColumn, int fieldSize,
            boolean disasterHappened) {
        Zone target = grid[argRow][argColumn];
        if (null == target) {
            return false;
        }

        int zoneLevel = 1;
        CursorSignal type;

        if (target instanceof Residence tmp) {
            zoneLevel = tmp.getLevel();
            type = CursorSignal.RESIDENCE;
            if (disasterHappened) {
                tmp.getResidents().forEach(person
                        -> person.changeHappinessBy(-1));
            }
        } else if (target instanceof Industry tmp) {
            zoneLevel = tmp.getLevel();
            type = CursorSignal.INDUSTRY;
            if (disasterHappened) {
                tmp.getWorkers().forEach(person
                        -> person.changeHappinessBy(-1));
            } else if (peopleOnPrivateZone((PrivateZone) target)) {

            }
        } else if (target instanceof Service tmp) {
            zoneLevel = tmp.getLevel();
            type = CursorSignal.SERVICE;
            if (disasterHappened) {
                tmp.getWorkers().forEach(person
                        -> person.changeHappinessBy(-1));
            } else if (peopleOnPrivateZone((PrivateZone) target)) {

            }
        } else if (target instanceof Road tmp) {
            type = CursorSignal.ROAD;
            ArrayList<Person> angryPeople
                    = peopleWhoCantFindTheirJobAnymore(tmp);
            if (disasterHappened) {
                angryPeople.forEach(person
                        -> person.changeHappinessBy(-1));
            } else if (!angryPeople.isEmpty()) {

            }
        } else if (target instanceof Police) {
            type = CursorSignal.POLICE;
        } else if (target instanceof Stadium) {
            type = CursorSignal.STADIUM;
        } else if (target instanceof HighSchool highSchool) {
            type = CursorSignal.HIGH_SCHOOL;
            highSchools.remove(highSchool);
        } else {
            type = CursorSignal.UNIVERSITY;
            universities.remove((University) target);
        }

        buildings.remove(target);

        CursorSignal targetSignal = target.getCursorSignal();
        int rowStart = target.getTopLeftY() / fieldSize;
        int rowEnd = target.getTopLeftY() / fieldSize
                + target.getCursorSignal().getHeight() - 1;
        int columnStart = target.getTopLeftX() / fieldSize;
        int columnEnd = target.getTopLeftX() / fieldSize
                + target.getCursorSignal().getWidth() - 1;
        for (int row = rowStart; row <= rowEnd; row++) {
            for (int column = columnStart; column <= columnEnd; column++) {
                grid[row][column] = null;
            }
        }

        if (CursorSignal.ROAD == targetSignal) {
            refreshRoadImgsAround(argRow, argColumn);
        }

        int returnMoney = type.getPriceL1()
                + (zoneLevel > 1 ? type.getPriceL2() : 0)
                + (zoneLevel > 2 ? type.getPriceL3() : 0);
        addMoney(returnMoney / 2);

        moveEveryOne();
        return true;
    }

    /**
     * Finds the people who won't be able to go to their workplace if a road
     * gets destroyed.
     *
     * @param closedRoad The road the citizens can't use.
     * @return People who can't go to work without the given road.
     */
    private ArrayList<Person> peopleWhoCantFindTheirJobAnymore(Road closedRoad) {
        ArrayList<Person> angryPeople = new ArrayList<>();

        for (Person resident : residents) {
            if (null != resident.getJob()) {
                if (!foundTheWorkplaceOfAPersonWhileARoadIsClosed(resident,
                        closedRoad)) {
                    angryPeople.add(resident);
                }
            }
        }

        return angryPeople;
    }

    /**
     * Checks whether a citizen can go to their workplace if a road can't be
     * used.
     *
     * @param resident The citizen who tries to go to work.
     * @param closedRoad The road the citizen can't use.
     * @return True if the citizen can go to work without the given road.
     */
    private boolean foundTheWorkplaceOfAPersonWhileARoadIsClosed(Person resident,
            Road closedRoad) {
        Zone home = resident.getHome();
        Zone workplace = resident.getJob();

        HashSet<Zone> foundZones = new HashSet<>();
        //This road can't be used.
        foundZones.add(closedRoad);

        LinkedList<Zone> queue = new LinkedList<>();
        queue.addLast(home);

        while (!queue.isEmpty()) {
            Zone currentZone = queue.removeFirst();
            for (Road road : roadsAroundZone(currentZone)) {
                if (!foundZones.contains(road)) {
                    foundZones.add(road);
                    queue.addLast(road);
                }
            }
            for (Workplace workplaceFound
                    : workplacesAroundZone(currentZone)) {
                if (workplaceFound == workplace) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Finds the workplaces around the given zone.
     *
     * @param centerZone We are searching workplaces around it.
     * @return The workplaces around the given zone.
     */
    private ArrayList<Workplace> workplacesAroundZone(Zone centerZone) {
        ArrayList<Workplace> workplacesAround = new ArrayList<>();
        int rowOfCenterZone = centerZone.getTopLeftY() / fieldSize;
        int columnOfCenterZone = centerZone.getTopLeftX() / fieldSize;
        //up
        if (workplaceAndInsideGrid(rowOfCenterZone - 1, columnOfCenterZone)) {
            workplacesAround.add((Workplace) grid[rowOfCenterZone - 1][columnOfCenterZone]);
        }
        //right
        if (workplaceAndInsideGrid(rowOfCenterZone, columnOfCenterZone + 1)) {
            workplacesAround.add((Workplace) grid[rowOfCenterZone][columnOfCenterZone + 1]);
        }
        //down
        if (workplaceAndInsideGrid(rowOfCenterZone + 1, columnOfCenterZone)) {
            workplacesAround.add((Workplace) grid[rowOfCenterZone + 1][columnOfCenterZone]);
        }
        //left
        if (workplaceAndInsideGrid(rowOfCenterZone, columnOfCenterZone - 1)) {
            workplacesAround.add((Workplace) grid[rowOfCenterZone][columnOfCenterZone - 1]);
        }
        return workplacesAround;
    }

    /**
     * Finds the roads around the given zone.
     *
     * @param centerZone We are searching roads around it.
     * @return The roads around the given zone.
     */
    private ArrayList<Road> roadsAroundZone(Zone centerZone) {
        ArrayList<Road> roadsAround = new ArrayList<>();
        int rowOfCenterZone = centerZone.getTopLeftY() / fieldSize;
        int columnOfCenterZone = centerZone.getTopLeftX() / fieldSize;
        //up
        if (roadAndInsideGrid(rowOfCenterZone - 1, columnOfCenterZone)) {
            roadsAround.add((Road) grid[rowOfCenterZone - 1][columnOfCenterZone]);
        }
        //right
        if (roadAndInsideGrid(rowOfCenterZone, columnOfCenterZone + 1)) {
            roadsAround.add((Road) grid[rowOfCenterZone][columnOfCenterZone + 1]);
        }
        //down
        if (roadAndInsideGrid(rowOfCenterZone + 1, columnOfCenterZone)) {
            roadsAround.add((Road) grid[rowOfCenterZone + 1][columnOfCenterZone]);
        }
        //left
        if (roadAndInsideGrid(rowOfCenterZone, columnOfCenterZone - 1)) {
            roadsAround.add((Road) grid[rowOfCenterZone][columnOfCenterZone - 1]);
        }
        return roadsAround;
    }

    /**
     * Check whether people are located on the given private zone.
     *
     * @param zone The private zone which we check.
     * @return True if people can be found on the given zone.
     */
    private boolean peopleOnPrivateZone(PrivateZone zone) {
        if (zone instanceof Residence tmp) {
            return !tmp.getResidents().isEmpty();
        } else if (zone instanceof Workplace tmp) {
            return !tmp.getWorkers().isEmpty();
        }
        return false;
    }

    /**
     * Checks whether the given position is a valid position of the grid and a
     * road is located on it or not.
     *
     * @param row The y position of the grid.
     * @param column The x position of the grid.
     * @return True if the position is inside the grid and a road is on it.
     */
    private Boolean roadAndInsideGrid(int row, int column) {
        if (row < 0 || height <= row || column < 0 || width <= column) {
            return false;
        }
        if (null == grid[row][column]) {
            return false;
        }
        return CursorSignal.ROAD == grid[row][column].getCursorSignal();
    }

    /**
     * Checks whether the given position is a valid position of the grid and a
     * workplace is located on it or not.
     *
     * @param row The y position of the grid.
     * @param column The x position of the grid.
     * @return True if the position is inside the grid and a workplace is on it.
     */
    private Boolean workplaceAndInsideGrid(int row, int column) {
        if (row < 0 || height <= row || column < 0 || width <= column) {
            return false;
        }
        if (null == grid[row][column]) {
            return false;
        }
        return grid[row][column] instanceof Workplace;
    }

    /**
     * Change the image of a road according its surroundings.
     *
     * @param zone The road.
     * @param rowStart The y position of the road.
     * @param columnStart The x position of the road.
     */
    private void refreshImgOfRoad(Zone zone, int rowStart, int columnStart) {
        if (rowStart < 0 || height <= rowStart || columnStart < 0
                || width <= columnStart || null == zone || zone.getCursorSignal() != CursorSignal.ROAD) {
            return;
        }

        boolean roadAbove = roadAndInsideGrid(rowStart - 1, columnStart);
        boolean roadUnder = roadAndInsideGrid(rowStart + 1, columnStart);
        boolean roadOnRight = roadAndInsideGrid(rowStart, columnStart + 1);
        boolean roadOnLeft = roadAndInsideGrid(rowStart, columnStart - 1);

        int roadsAroundCount = 0;
        roadsAroundCount += roadAbove ? 1 : 0;
        roadsAroundCount += roadUnder ? 1 : 0;
        roadsAroundCount += roadOnRight ? 1 : 0;
        roadsAroundCount += roadOnLeft ? 1 : 0;
        if (0 == roadsAroundCount) {
            zone.setImg(Assets.roadNS);
            return;
        }
        if (4 == roadsAroundCount) {
            zone.setImg(Assets.roadNESW);
            return;
        }
        if (1 == roadsAroundCount) {
            if (roadAbove || roadUnder) {
                zone.setImg(Assets.roadNS);
                return;
            } else {
                zone.setImg(Assets.roadEW);
                return;
            }
        }
        if (3 == roadsAroundCount) {
            if (false == roadAbove) {
                zone.setImg(Assets.roadESW);
                return;
            }
            if (false == roadUnder) {
                zone.setImg(Assets.roadWNE);
                return;
            }
            if (false == roadOnLeft) {
                zone.setImg(Assets.roadNES);
            } else {
                //if (false == roadOnRight) {
                zone.setImg(Assets.roadSWN);
            }
        } else {
            //if (2 == roadsAroundCount)
            if (roadAbove && roadUnder) {
                zone.setImg(Assets.roadNS);
                return;
            }
            if (roadOnRight && roadOnLeft) {
                zone.setImg(Assets.roadEW);
                return;
            }
            if (roadAbove && roadOnRight) {
                zone.setImg(Assets.roadNE);
                return;
            }
            if (roadAbove && roadOnLeft) {
                zone.setImg(Assets.roadWN);
                return;
            }
            if (roadUnder && roadOnLeft) {
                zone.setImg(Assets.roadSW);
                return;
            }
            if (roadUnder && roadOnRight) {
                zone.setImg(Assets.roadES);
            }

        }
    }

    /**
     * People try to take the best places. People change their home and
     * workplace if possible.
     */
    public void moveEveryOne() {

        residents.forEach(resident -> {
            resident.setHome(null);
            resident.setJob(null);
        });

        int sumResidenceCapacity = 0;
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                if (null != grid[row][column]) {
                    if (grid[row][column] instanceof Residence residence) {
                        sumResidenceCapacity += residence.getCapacity();
                        residence.clearResidents();
                    } else if (grid[row][column] instanceof Workplace workplace) {
                        workplace.clearWorkers();
                    }
                }
            }
        }
        while (sumResidenceCapacity < residents.size()) {
            residents.remove(0);
        }

        //Find all residences.
        //Find all industries and services connected to a residence. Store every 
        //connections. Store the distances. Sort according the distances.
        //Residences with no connection are stored separately from those with
        //connections.
        ArrayList<Residence> residences = findResidences();

        ArrayList<ResidenceWorkplaceDistance> distances = new ArrayList<>();
        ArrayList<Residence> residencesWithNoWorkplace = new ArrayList<>();

        residences.forEach(residence -> {
            HashSet<Zone> foundZones = new HashSet<>();
            foundZones.add(residence);
            LinkedList<ZoneDistancePair> zoneDistancePairs = new LinkedList<>();
            int foundWorkplacesCount = 0;
            zoneDistancePairs.addLast(new ZoneDistancePair(residence,
                    0));

            while (!zoneDistancePairs.isEmpty()) {
                ZoneDistancePair currentZoneDistancePair
                        = zoneDistancePairs.removeFirst();
                int row = currentZoneDistancePair.getZone().getTopLeftY()
                        / fieldSize;
                int column = currentZoneDistancePair.getZone().getTopLeftX()
                        / fieldSize;

                //up
                checkGridCellToFindConnection(row - 1, column, foundZones,
                        zoneDistancePairs, currentZoneDistancePair,
                        foundWorkplacesCount, distances, residence);
                //right
                checkGridCellToFindConnection(row, column + 1, foundZones,
                        zoneDistancePairs, currentZoneDistancePair,
                        foundWorkplacesCount, distances, residence);
                //down
                checkGridCellToFindConnection(row + 1, column, foundZones,
                        zoneDistancePairs, currentZoneDistancePair,
                        foundWorkplacesCount, distances, residence);
                //left
                checkGridCellToFindConnection(row, column - 1, foundZones,
                        zoneDistancePairs, currentZoneDistancePair,
                        foundWorkplacesCount, distances, residence);
            }

            if (0 == foundWorkplacesCount) {
                residencesWithNoWorkplace.add(residence);
            }
        });

        Collections.sort(distances);

        industryWorkersCount = 0;
        serviceWorkersCount = 0;
        /*
        for (ResidenceWorkplaceDistance distance : distances) {
            System.out.println(
                    "distance.getDistance(): " + distance.getDistance()
                    + ", distance.getResidence(): " + distance.getResidence()
                    + ", distance.getWorkplace(): " + distance.getWorkplace());
        }
         */
        //The new residents try to take the best places.
        residents.forEach(resident -> {
            residentTriesToMoveIn(resident, distances,
                    residencesWithNoWorkplace);
        });

    }

    /**
     * One person tries to move in.
     *
     * @param resident This person tries to move in.
     * @param distances It is a collection of sorted(according distances)
     * objects containing every homes and workplaces connected together with
     * road.
     * @param residencesWithNoWorkplace Collection of homes with no workplace
     * connection.
     * @return True if the person can move in.
     */
    private boolean residentTriesToMoveIn(Person resident,
            ArrayList<ResidenceWorkplaceDistance> distances,
            ArrayList<Residence> residencesWithNoWorkplace) {
        boolean movedIn = false;
        for (ResidenceWorkplaceDistance distance : distances) {
            if (movedIn) {
                break;
            }
            //Find all industries and services with the same distance,
            //not those that are full.
            //The citizen will take the job according the industry/service 
            //worker counters.
            ArrayList<ResidenceWorkplaceDistance> industriesWithSameDistance
                    = new ArrayList<>();
            ArrayList<ResidenceWorkplaceDistance> servicesWithSameDistance
                    = new ArrayList<>();
            for (ResidenceWorkplaceDistance equalDistance : distances) {
                if (distance.getDistance() == equalDistance.getDistance()) {
                    Residence residence = equalDistance.getResidence();
                    Workplace workplace = equalDistance.getWorkplace();

                    int residenceSize = residence.getSize();
                    int residenceCapacity = residence.getCapacity();
                    int workplaceSize = workplace.getSize();
                    int workplaceCapacity = workplace.getCapacity();

                    if (residenceSize < residenceCapacity
                            && workplaceSize < workplaceCapacity) {
                        if (workplace instanceof Industry) {
                            industriesWithSameDistance.add(equalDistance);
                        } else {//workplace instanceof Service
                            servicesWithSameDistance.add(equalDistance);
                        }
                    }
                }
            }

            if (industryWorkersCount < serviceWorkersCount) {
                for (ResidenceWorkplaceDistance residenceWorkplaceDistance
                        : industriesWithSameDistance) {
                    Residence residence = residenceWorkplaceDistance
                            .getResidence();
                    Workplace workplace = residenceWorkplaceDistance
                            .getWorkplace();
                    int residenceSize = residence.getSize();
                    int residenceCapacity = residence.getCapacity();
                    int workplaceSize = workplace.getSize();
                    int workplaceCapacity = workplace.getCapacity();
                    if (residenceSize < residenceCapacity
                            && workplaceSize < workplaceCapacity) {
                        residence.addPerson(resident);
                        workplace.addPerson(resident);
                        resident.setHome(residence);
                        resident.setJob(workplace);
                        resident.setHomeJobDistance(
                                residenceWorkplaceDistance
                                        .getDistance());
                        movedIn = true;
                        //Moved in.
                        industryWorkersCount++;
                        break;
                    }
                }
            } else {
                for (ResidenceWorkplaceDistance residenceWorkplaceDistance
                        : servicesWithSameDistance) {
                    Residence residence = residenceWorkplaceDistance
                            .getResidence();
                    Workplace workplace = residenceWorkplaceDistance
                            .getWorkplace();
                    int residenceSize = residence.getSize();
                    int residenceCapacity = residence.getCapacity();
                    int workplaceSize = workplace.getSize();
                    int workplaceCapacity = workplace.getCapacity();
                    if (residenceSize < residenceCapacity
                            && workplaceSize < workplaceCapacity) {
                        residence.addPerson(resident);
                        workplace.addPerson(resident);
                        resident.setHome(residence);
                        resident.setJob(workplace);
                        resident.setHomeJobDistance(
                                residenceWorkplaceDistance
                                        .getDistance());
                        movedIn = true;
                        //Moved in.
                        serviceWorkersCount++;
                        break;
                    }
                }
            }

            //industryWorkersCount < serviceWorkersCount but found only services
            //with the same distance
            //or
            //industryWorkersCount >= serviceWorkersCount but found only
            //industries with the same distance
            if (!movedIn) {
                for (ResidenceWorkplaceDistance residenceWorkplaceDistance
                        : industriesWithSameDistance) {
                    Residence residence = residenceWorkplaceDistance
                            .getResidence();
                    Workplace workplace = residenceWorkplaceDistance
                            .getWorkplace();
                    int residenceSize = residence.getSize();
                    int residenceCapacity = residence.getCapacity();
                    int workplaceSize = workplace.getSize();
                    int workplaceCapacity = workplace.getCapacity();
                    if (residenceSize < residenceCapacity
                            && workplaceSize < workplaceCapacity) {
                        residence.addPerson(resident);
                        workplace.addPerson(resident);
                        resident.setHome(residence);
                        resident.setJob(workplace);
                        resident.setHomeJobDistance(
                                residenceWorkplaceDistance
                                        .getDistance());
                        movedIn = true;
                        //Moved in.
                        industryWorkersCount++;
                        break;
                    }
                }
            }
            if (!movedIn) {
                for (ResidenceWorkplaceDistance residenceWorkplaceDistance
                        : servicesWithSameDistance) {
                    Residence residence = residenceWorkplaceDistance
                            .getResidence();
                    Workplace workplace = residenceWorkplaceDistance
                            .getWorkplace();
                    int residenceSize = residence.getSize();
                    int residenceCapacity = residence.getCapacity();
                    int workplaceSize = workplace.getSize();
                    int workplaceCapacity = workplace.getCapacity();
                    if (residenceSize < residenceCapacity
                            && workplaceSize < workplaceCapacity) {
                        residence.addPerson(resident);
                        workplace.addPerson(resident);
                        resident.setHome(residence);
                        resident.setJob(workplace);
                        resident.setHomeJobDistance(
                                residenceWorkplaceDistance
                                        .getDistance());
                        movedIn = true;
                        //Moved in.
                        serviceWorkersCount++;
                        break;
                    }
                }
            }
        }
        if (false == movedIn) {
            for (Residence residence : residencesWithNoWorkplace) {
                int residenceSize = residence.getSize();
                int residenceCapacity = residence.getCapacity();
                if (residenceSize < residenceCapacity) {
                    residence.addPerson(resident);
                    resident.setHome(residence);
                    resident.setHomeJobDistance(-1);
                    movedIn = true;
                    //Moved in.
                    break;
                }
            }
        }
        //Residences with connections, but all connected workplaces are full
        //and there is still free place on the residential zone.
        if (false == movedIn) {
            for (ResidenceWorkplaceDistance distance : distances) {
                Residence residence = distance.getResidence();
                int residenceSize = residence.getSize();
                int residenceCapacity = residence.getCapacity();
                if (residenceSize < residenceCapacity) {
                    residence.addPerson(resident);
                    resident.setHome(residence);
                    resident.setHomeJobDistance(-1);
                    movedIn = true;
                    //Moved in.
                    break;
                }
            }
        }
        return movedIn;
    }

    /**
     * Calculates the maximum number of person who can move in according the
     * current population.
     *
     * @return The number of the people who may move in.
     */
    private int newResidentsCount(int daysPassed) {

        float percentageOfNewResidents = 0.01f;
        int newResidentsCount = (int) Math.ceil(
                residents.size() * percentageOfNewResidents) * daysPassed;
        if (residents.size() < fixGroupOfPeopleCount) {
            newResidentsCount += fixGroupOfPeopleCount - residents.size();
        }
        return newResidentsCount;
    }

    /**
     * Finds all residences on the grid.
     *
     * @return All residences on the grid.
     */
    private ArrayList<Residence> findResidences() {
        ArrayList<Residence> residences = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                if (null != grid[row][column]) {
                    if (grid[row][column] instanceof Residence) {
                        residences.add((Residence) grid[row][column]);
                    }
                }
            }
        }
        return residences;
    }

    /**
     * Tries to locate workplaces connected to a home. Checks whether the given
     * position is a workplace which can be reached from a specific residence.
     *
     * @param row The y position of a cell on the grid.
     * @param column The x position of a cell on the grid.
     * @param foundZones Zones which have already been found.
     * @param zoneDistancePairs Collection of roads and workplaces with the
     * distance from the residence.
     * @param currentZoneDistancePair The road or the source residence(aka home)
     * next to the given position and the distance from the source zone(aka
     * residence, home). The current position is distance+1 far from the source
     * residence.
     * @param foundWorkplacesCount The number of workplaces connected to the
     * source residence. Might change when we call this function.
     * @param distances It is a collection of sorted(according distances)
     * objects containing every homes and workplaces connected together with
     * road.
     * @param residence The source residence(aka home). We are looking for
     * workplaces connected by roads to this residence.
     */
    private void checkGridCellToFindConnection(int row, int column,
            HashSet<Zone> foundZones,
            LinkedList<ZoneDistancePair> zoneDistancePairs,
            ZoneDistancePair currentZoneDistancePair,
            Integer foundWorkplacesCount,
            ArrayList<ResidenceWorkplaceDistance> distances,
            Residence residence) {
        Zone foundZone;
        if (0 <= row && row < height && 0 <= column && column < width) {
            if (null != grid[row][column]) {
                foundZone = grid[row][column];
                if (!foundZones.contains(foundZone)) {
                    foundZones.add(foundZone);
                    if (foundZone instanceof Road) {
                        zoneDistancePairs.addLast(
                                new ZoneDistancePair(foundZone,
                                        currentZoneDistancePair
                                                .getDistance() + 1));
                    } else if (foundZone instanceof Workplace workplace) {
                        foundWorkplacesCount++;
                        distances.add(new ResidenceWorkplaceDistance(
                                residence, workplace,
                                currentZoneDistancePair.getDistance()
                                + 1));
                    }
                }

            }
        }
    }

    private void stockUp(ArrayList<Residence> residences, 
            ArrayList<ResidenceWorkplaceDistance> distances,
            ArrayList<Residence> residencesWithNoWorkplace) {
        
        residences.forEach(residence -> {
            HashSet<Zone> foundZones = new HashSet<>();
            foundZones.add(residence);
            LinkedList<ZoneDistancePair> zoneDistancePairs = new LinkedList<>();
            Integer foundWorkplacesCount = 0;
            zoneDistancePairs.addLast(new ZoneDistancePair(residence,
                    0));

            while (!zoneDistancePairs.isEmpty()) {
                ZoneDistancePair currentZoneDistancePair
                        = zoneDistancePairs.removeFirst();
                int row = currentZoneDistancePair.getZone().getTopLeftY()
                        / fieldSize;
                int column = currentZoneDistancePair.getZone().getTopLeftX()
                        / fieldSize;
                //up
                checkGridCellToFindConnection(row - 1, column, foundZones,
                        zoneDistancePairs, currentZoneDistancePair,
                        foundWorkplacesCount, distances, residence);
                //right
                checkGridCellToFindConnection(row, column + 1, foundZones,
                        zoneDistancePairs, currentZoneDistancePair,
                        foundWorkplacesCount, distances, residence);
                //down
                checkGridCellToFindConnection(row + 1, column, foundZones,
                        zoneDistancePairs, currentZoneDistancePair,
                        foundWorkplacesCount, distances, residence);
                //left
                checkGridCellToFindConnection(row, column - 1, foundZones,
                        zoneDistancePairs, currentZoneDistancePair,
                        foundWorkplacesCount, distances, residence);
            }

            if (0 == foundWorkplacesCount) {
                residencesWithNoWorkplace.add(residence);
            }
        });

        Collections.sort(distances);
    }
    
    private int tryTakeBestPlace(int newResidentsCount, 
            ArrayList<Residence> residencesWithNoWorkplace,
            ArrayList<ResidenceWorkplaceDistance> distances) {
        while (newResidentsCount > 0) {
            Person newPerson = citizenGenerator.createCitizen();
            boolean movedIn = residentTriesToMoveIn(newPerson, distances,
                    residencesWithNoWorkplace);
            if (false == movedIn) {
                //The city is full, there is no more residence with free place.
                break;
            } else {
                residents.add(newPerson);
            }

            newResidentsCount--;
        }
        return newResidentsCount;
    }
    
    private void changeHappinessByJob(double ratio) {
    residents.forEach(resident -> {
            if (null == resident.getJob()) {
                resident.changeHappinessBy(-1 * ratio);                                 
            } else {
                resident.changeHappinessBy(1 * ratio);                                  
                //distance between house and workplace:
                //  (distance/5==0) [0;4]-> +1,
                //  [5,...[ -> -1*2^n where n=distance/5
                int happinessChangeAccordingDistance
                        = resident.getHomeJobDistance() / 5 == 0
                        ? 1
                        : -1 * (int) Math.pow(2,
                                resident.getHomeJobDistance() / 5);
                resident.changeHappinessBy(happinessChangeAccordingDistance * ratio);     
            }
        });
    }
    
    private void changeHappinessByStadium(double ratio) {
        findAllStadiums().forEach(stadium -> {
            findCoordsInsideRange(stadium, Stadium.range)
                    .forEach(coords -> {
                        Zone zone = grid[coords.getY()][coords.getX()];
                        if (null != zone) {
                            if (zone instanceof Residence residence) {
                                residence.getResidents().forEach(
                                        resident -> {
                                            resident.changeHappinessBy(4 * ratio);  
                                        });
                            } else if (zone instanceof Workplace workplace) {
                                workplace.getWorkers().forEach(
                                        worker -> {
                                            worker.changeHappinessBy(4 * ratio);     
                                        });
                            }
                        }
                    });
        });
    }
    
    private void changeHappinessByIndusties(double ratio) {
        findAllIndustries().forEach(industry -> {
            if (industry.getSize() > 0) {
                findCoordsInsideRange(industry, Industry.range)
                        .forEach(coords -> {
                            Zone zone = grid[coords.getY()][coords.getX()];
                            if (null != zone) {
                                if (zone instanceof Residence residence) {
                                    residence.getResidents().forEach(
                                            resident -> {
                                                resident.changeHappinessBy(-1 * ratio); 
                                            });
                                } else if (zone instanceof Workplace workplace) {
                                    workplace.getWorkers().forEach(
                                            worker -> {
                                                worker.changeHappinessBy(-1 * ratio);  
                                            });
                                }
                            }
                        });
            }
        });
    }
    
    private void changeHappinessByPolices(double ratio) {
        ///Happiness change according saturation(home, job). If capacity==size
        //and there is no police nearby, then -3.
        ArrayList<Coords> coordsInPoliceRange = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                Zone zone = grid[row][column];
                if (null != zone) {
                    if (zone instanceof Police) {
                        for (Coords coords
                                : findCoordsInsideRange(zone,
                                        Police.range)) {
                            boolean alreadyIn = false;
                            for (Coords coordsInRange : coordsInPoliceRange) {
                                if (coordsInRange.getX() == coords.getX()
                                        && coordsInRange.getY()
                                        == coords.getY()) {
                                    alreadyIn = true;
                                    break;
                                }
                            }
                            if (false == alreadyIn) {
                                coordsInPoliceRange.add(coords);
                            }
                        }
                    }
                }
            }
        }
        //Decrease happiness of people who work or live on a zone with no police
        //nearby and the zone is full(capacity==size).
        ArrayList<Coords> coordsNotInPoliceRange = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                boolean coordsAffectedByPolice = false;
                for (Coords coords : coordsInPoliceRange) {
                    if (column == coords.getX() && row == coords.getY()) {
                        coordsAffectedByPolice = true;
                        break;
                    }
                }
                if (!coordsAffectedByPolice) {
                    coordsNotInPoliceRange.add(new Coords(column, row));
                }
            }
        }
        coordsNotInPoliceRange.forEach(coords -> {
            Zone zone = grid[coords.getY()][coords.getX()];
            if (null != zone) {
                if (zone instanceof Residence residence) {
                    residence.getResidents().forEach(resident -> {
                        if (residence.getCapacity() == residence.getSize()) {
                            resident.changeHappinessBy(-4 * ratio);             
                        }
                    });
                } else if (zone instanceof Workplace workplace) {
                    workplace.getWorkers().forEach(worker -> {
                        if (workplace.getCapacity() == workplace.getSize()) {
                            worker.changeHappinessBy(-4 * ratio);                    
                        }
                    });
                }
            }
        });
    }
    
    private void changeHappinessByNegativeMoney(double ratio) {
        if (money < 0) {

            Date currentDate = new Date(0);
            currentDate.setTime(bigCityJframe.getDate());
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);
            negativeBudgetCurrentYear = c.get(Calendar.YEAR);
            //System.out.println(negativeBudgetCurrentYear);
            if (0 == negativeBudgetStartYear) {
                negativeBudgetStartYear = negativeBudgetCurrentYear;
            }
            int yearsWithNegativeBudgetCount = negativeBudgetCurrentYear
                    - negativeBudgetStartYear;
            if (0 == yearsWithNegativeBudgetCount) {
                yearsWithNegativeBudgetCount = 1;
            }
            int happinessChange = -1 + money / 1000;

            for (Person resident : residents) {
                resident.changeHappinessBy(
                        yearsWithNegativeBudgetCount
                        * happinessChange
                        * ratio);

            }
        } else {
            yearsWithNegativeBudgetCount = 0;
            negativeBudgetStartYear = 0;
        }
    }
    
    private void changeHappinessByTax(double ratio) {
        int happinessChangeAccordingTaxRate = taxPercentage / 30;
        //System.out.println(-1 * happinessChangeAccordingTaxRate);
        if (0 != taxPercentage) {
            residents.forEach(person -> person
                    .changeHappinessBy(-1 * happinessChangeAccordingTaxRate * ratio)); 
        }
    }
    
    private void changeHappinessByWorkingProportion(double ratio) {
        int numberOfIndustryWorkers = 0;
        int numberOfServiceWorkers = 0;
        for (Person resident : residents) {
            if (null != resident.getJob()) {
                if (resident.getJob() instanceof Service) {
                    numberOfServiceWorkers++;
                } else if (resident.getJob() instanceof Industry) {
                    numberOfIndustryWorkers++;
                }
            }
        }
        if (0.5 < (double) Math.abs(
                numberOfIndustryWorkers - numberOfServiceWorkers)
                / (numberOfIndustryWorkers + numberOfServiceWorkers)) {
            //System.out.println("The difference is greater than 50%.");
            residents.forEach(resident -> resident.changeHappinessBy(-1 * ratio));    
        }/* else {
            System.out.println("The difference is less than 50%.");
        }*/
    }
    
    private void timePassedHelper(int newResidentsCount, int daysPassed) {

        double ratio = daysPassed / 30.0;

        ArrayList<Residence> residences = findResidences();
        ArrayList<ResidenceWorkplaceDistance> distances = new ArrayList<>();
        ArrayList<Residence> residencesWithNoWorkplace = new ArrayList<>();

        stockUp(residences, distances, residencesWithNoWorkplace);

        //The new residents try to take the best places.
        newResidentsCount = tryTakeBestPlace(newResidentsCount,
                residencesWithNoWorkplace,
                distances);

        changeHappinessByJob(ratio);
        changeHappinessByIndusties(ratio);
        changeHappinessByStadium(ratio);
        changeHappinessByPolices(ratio);
        changeHappinessByNegativeMoney(ratio);
        changeHappinessByTax(ratio);
        changeHappinessByWorkingProportion(ratio);
        
        //Residents with low happiness move out. (<10%)
        //The starter group won't move out unless there is not enough place.
        ArrayList<Person> peopleMoveOut = new ArrayList<>();

        residents.stream().forEach(resident -> {
            if (resident.getHappiness() < 10) {
                peopleMoveOut.add(resident);
            }
        });
        for (Person resident : peopleMoveOut) {
            if (residents.size() <= fixGroupOfPeopleCount) {
                break;
            }
            ((Residence) resident.getHome()).getResidents().remove(resident);
            if (null != resident.getJob()) {
                ((Workplace) resident.getJob()).getWorkers().remove(resident);
            }
            residents.remove(resident);
        }

        //Pay the expenses. 
        //(high school -20$, university -30$, police -30$, stadium -$40)
        for (Zone zone : buildings) {
            if (zone instanceof HighSchool) {
                expenses -= Math.floor(20 * ratio);
            } else if (zone instanceof University) {
                expenses -= Math.floor(30 * ratio);
            } else if (zone instanceof Police) {
                expenses -= Math.floor(30 * ratio);
            } else if (zone instanceof Stadium) {
                expenses -= Math.floor(40 * ratio);
            } else if (zone instanceof Road) {
                expenses -= Math.floor(5 * ratio);
            }
        }

        //Check whether the game is over or not. (average happiness < 20%)
        calculateHappieness();
        bigCityJframe.getHappy().setText(Math.round(combinedHappiness) + "%");

        checkGameOver((int) Math.round(combinedHappiness));

        double tmp = rnd.nextDouble();
        disasterChance += (daysPassedWithoutDisaster / 10000.0) * tmp;
        if ((int) disasterChance > 0) {
            makeDisaster();
        } else {
            daysPassedWithoutDisaster += daysPassed;
        }

        bigCityJframe.repaintStatPanelAndGrid();
    }

    /**
     * Recalculate everything like one day passed. - Educate people - Move in
     * and out people - Grow people older if a year passed - Stores the income
     * for the passed time - Subtracts the expenses for the passed time
     */
    public void dayPassed() {

        educatePeople(1);
        int newResidentsCount = newResidentsCount(1);
        timePassedHelper(newResidentsCount, 1);

        //Increase age. Old people die and changed to new people with
        //low education level.
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(bigCityJframe.getDate()));
        int day = c.get(Calendar.DAY_OF_YEAR);
        int month = c.get(Calendar.MONTH);

        if (day == 1 && month == 0) {
            for (Person resident : residents) {
                resident.growOlder();
                if (70 == resident.getAge()) {
                    resident.die();
                }
            }

            addMoney((int) Math.floor(yearlyIncome));
        }

        addMoney((int) Math.floor(expenses));
        expenses = 0;

        collectTax(1);
    }

    /**
     * Recalculate everything like ten day passed. - Educate people - Move in
     * and out people - Grow people older if a year passed - Stores the income
     * for the passed time - Subtracts the expenses for the passed time
     */
    public void daysPassed() {

        educatePeople(10);

        int newResidentsCount = newResidentsCount(10);

        timePassedHelper(newResidentsCount, 10);

        //Increase age. Old people die and changed to new people with
        //low education level.
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(bigCityJframe.getDate()));
        int day = c.get(Calendar.DAY_OF_YEAR);
        int month = c.get(Calendar.MONTH);

        if (day < 11 && month == 0) {
            for (Person resident : residents) {
                resident.growOlder();
                if (70 == resident.getAge()) {
                    resident.die();
                }
            }

            addMoney((int) Math.floor(yearlyIncome));
        }

        addMoney((int) Math.floor(expenses));
        expenses = 0;

        collectTax(10);
    }

    /**
     * Recalculate everything like one month passed. - Educate people - Move in
     * and out people - Grow people older if a year passed - Stores the income
     * for the passed time - Subtracts the expenses for the passed time
     *
     * @param daysPassed - int, the days amount of the month
     */
    public void monthPassed(int daysPassed) {

        educatePeople(daysPassed);

        int newResidentsCount = newResidentsCount(daysPassed);

        timePassedHelper(newResidentsCount, daysPassed);

        //Increase age. Old people die and changed to new people with
        //low education level.
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(bigCityJframe.getDate()));
        int month = c.get(Calendar.MONTH);

        if (month == 0) {
            for (Person resident : residents) {
                resident.growOlder();
                if (70 == resident.getAge()) {
                    resident.die();
                }
            }

            addMoney((int) Math.floor(yearlyIncome));
        }

        addMoney((int) Math.floor(expenses));
        expenses = 0;

        collectTax(daysPassed);

    }

    /**
     * Finds all industries on the grid.
     *
     * @return The industries on the grid.
     */
    private ArrayList<Industry> findAllIndustries() {
        ArrayList<Industry> industries = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                if (null != grid[row][column]) {
                    if (grid[row][column] instanceof Industry industry) {
                        industries.add(industry);
                    }
                }
            }
        }
        return industries;
    }

    /**
     * Finds all stadiums on the grid.
     *
     * @return The stadiums on the grid.
     */
    private ArrayList<Stadium> findAllStadiums() {
        ArrayList<Stadium> stadiums = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                Zone zone = grid[row][column];
                if (null != zone) {
                    if (zone instanceof Stadium) {
                        boolean alreadyIn = false;
                        for (Stadium stadium : stadiums) {
                            if (stadium == zone) {
                                alreadyIn = true;
                                break;
                            }
                        }
                        if (false == alreadyIn) {
                            stadiums.add((Stadium) grid[row][column]);
                        }
                    }
                }
            }
        }
        return stadiums;
    }

    /**
     * Finds all coordinates of the grid inside the given range around the give
     * zone.
     *
     * @param centerZone The coordinates we are looking for are around this
     * zone.
     * @param range The range of the zone(police, industry, stadium).
     * @return The coordinates inside the given range(except the centerZone's
     * position).
     */
    public ArrayList<Coords> findCoordsInsideRange(Zone centerZone, int range) {
        LinkedList<Coords> coordsInRange = new LinkedList<>();
        ArrayList<Coords> processedCoords = new ArrayList<>();

        int x = centerZone.getTopLeftX() / fieldSize;
        int y = centerZone.getTopLeftY() / fieldSize;

        Coords coords = new Coords(x, y);
        coords.setStep(0);
        coordsInRange.addLast(coords);
        processedCoords.add(coords);

        //The center zone is a 2x2 zone if it is a stadium.
        if (centerZone instanceof Stadium) {
            coords = new Coords(x + 1, y);
            coords.setStep(0);
            coordsInRange.addLast(coords);
            processedCoords.add(coords);

            coords = new Coords(x + 1, y + 1);
            coords.setStep(0);
            coordsInRange.addLast(coords);
            processedCoords.add(coords);

            coords = new Coords(x, y + 1);
            coords.setStep(0);
            coordsInRange.addLast(coords);
            processedCoords.add(coords);
        }

        ArrayList<Coords> result = new ArrayList<>();

        while (!coordsInRange.isEmpty()) {
            coords = coordsInRange.removeFirst();
            x = coords.getX();
            y = coords.getY();
            int nextStep = coords.getStep() + 1;
            //up
            Coords nextCoords = getNextCoords(x, y - 1, nextStep, range,
                    processedCoords);
            if (null != nextCoords) {
                processedCoords.add(nextCoords);
                result.add(nextCoords);
                coordsInRange.addLast(nextCoords);
            }
            //right
            nextCoords = getNextCoords(x + 1, y, nextStep, range,
                    processedCoords);
            if (null != nextCoords) {
                processedCoords.add(nextCoords);
                result.add(nextCoords);
                coordsInRange.addLast(nextCoords);
            }
            //down
            nextCoords = getNextCoords(x, y + 1, nextStep, range,
                    processedCoords);
            if (null != nextCoords) {
                processedCoords.add(nextCoords);
                result.add(nextCoords);
                coordsInRange.addLast(nextCoords);
            }
            //left
            nextCoords = getNextCoords(x - 1, y, nextStep, range,
                    processedCoords);
            if (null != nextCoords) {
                processedCoords.add(nextCoords);
                result.add(nextCoords);
                coordsInRange.addLast(nextCoords);
            }
        }

        return result;
    }

    /**
     * Checks if the given coordinates are valid and inside the given range.
     *
     * @param x The x position of a cell on the grid.
     * @param y The y position of a cell on the grid.
     * @param newStep The distance between the given coordinates and the zone
     * which has the range.
     * @param range The range of a zone.(police, industry, stadium)
     * @param processedCoords Already checked coordinates.
     * @return The coordinates of a field on the grid, inside the range which
     * has not been processed yet.
     */
    private Coords getNextCoords(int x, int y, int newStep, int range,
            ArrayList<Coords> processedCoords) {
        if (0 <= x && x < width && 0 <= y && y < height && newStep <= range) {
            for (Coords processedCoord : processedCoords) {
                //already found coords
                if (processedCoord.getX() == x && processedCoord.getY() == y) {
                    return null;
                }
            }
            Coords coords = new Coords(x, y);
            coords.setStep(newStep);
            return coords;
        } else {
            return null;
        }
    }

    public int getMoney() {
        return money;
    }

    public double getCombinedHappiness() {
        return combinedHappiness;
    }

    public String getDate() {
        return date;
    }

    public int getTimeSpeed() {
        return timeSpeed;
    }

    public int getTaxPercentage() {
        return taxPercentage;
    }

    public String getName() {
        return name;
    }

    /**
     * Add x amout of money to the savings of the city
     * @param value - int, the amout you want to add
     * @return      - int, the new amout of money
     */
    public int addMoney(int value) {
        money += value;
        return money;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setResidents(ArrayList<Person> residents) {
        this.residents = residents;
    }

    public void setTaxPercentage(int taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    /**
     *
     * @return The average happiness of the people.
     */
    public double calculateHappieness() {
        if (residents.isEmpty()) {
            combinedHappiness = 100.0;
            return combinedHappiness;
        }
        int sum = 0;
        for (Person p : residents) {
            sum += p.getHappiness();
        }
        combinedHappiness = (double) sum / residents.size();
        return combinedHappiness;
    }

    /**
     * Collect the right amout of money from residents
     * @param daysPassed - how many days passed sience last call
     */
    public void collectTax(int daysPassed) {
        for (Person p : residents) {
            yearlyIncome += (double) (0.2 * daysPassed * taxPercentage) / 100 * p.getEducationLevel().getLevel();
            if (null != p.getJob()) {
                yearlyIncome += (double) (0.3 * daysPassed * taxPercentage) / 100 * p.getEducationLevel().getLevel();
            }
        }
        bigCityJframe.refreshMoney();
    }

    public ArrayList<Person> getResidents() {
        return residents;
    }

    private void checkGameOver(int averageHappiness) {
        if (averageHappiness < 20) {
            /*System.out.println("Game over! "
                    + "The average happiness is below 20%");*/
            bigCityJframe.gameOver("The average happiness is below 20%.");
        }
    }

    /**
     * Educate the right amount of residents
     * @param daysPassed - how many days passed sience last call
     */
    public void educatePeople(int daysPassed) {
        int hsd = 0;
        int ud = 0;
        for (Person p : residents) {
            if (p.getEducationLevel() == EducationLevel.HIGH_SCHOOL) {
                hsd++;
            } else if (p.getEducationLevel() == EducationLevel.UNIVERSITY) {
                ud++;
            }
        }

        int possibeHighSchoolDegrees = Integer.min(highSchools.size() * 5,
                (int) (Math.round(residents.size() * 0.8) - hsd - ud)) * daysPassed;
        int possibeUniversityDegrees = Integer.min(universities.size() * 10,
                (int) (Math.round(residents.size() * 0.5) - ud)) * daysPassed;

        for (University u : universities) {
            int row = u.getTopLeftY() / fieldSize;
            int col = u.getTopLeftX() / fieldSize;

            Set<Residence> residences = findResidencesOnRoad(row, col, 2, 2);
            List<Person> educateables = new ArrayList<>();

            for (Residence residence : residences) {
                for (Person p : residence.getResidents()) {
                    if (p.getEducationLevel() == EducationLevel.HIGH_SCHOOL) {
                        educateables.add(p);
                    }
                }
            }

            for (Person p : educateables) {
                if (possibeUniversityDegrees > 0) {
                    p.educate();
                    possibeUniversityDegrees--;
                }
            }
        }
        for (HighSchool h : highSchools) {
            int row = h.getTopLeftY() / fieldSize;
            int col = h.getTopLeftX() / fieldSize;

            Set<Residence> residences = findResidencesOnRoad(row, col, 2, 1);
            List<Person> educateables = new ArrayList<>();

            for (Residence residence : residences) {
                for (Person p : residence.getResidents()) {
                    if (p.getEducationLevel() == EducationLevel.PRIMARY_SCHOOL) {
                        educateables.add(p);
                    }
                }
            }

            for (Person p : educateables) {
                if (possibeHighSchoolDegrees > 0) {
                    p.educate();
                    possibeHighSchoolDegrees--;
                }
            }
        }
    }

    /**
     * Finds all accessible residences
     * @param row           - int, row of the starting coordinates
     * @param col           - int, col of the starting coordinates
     * @param fieldWidth    - int, the width in zones of the starting zone
     * @param fieldHeight   - int, the height in zones of the starting zone
     * @return              - Set of residences that are accessible
     */
    public Set<Residence> findResidencesOnRoad(int row, int col, int fieldWidth, int fieldHeight) {
        Set<Residence> res = new HashSet<>();
        Set<Zone> checkedZones = new HashSet<>();
        for (int i = 0; i < fieldHeight; i++) {
            for (int j = 0; j < fieldWidth; j++) {
                checkZone(checkedZones, res, row + i, col + j);
            }
        }
        return res;
    }

    private void checkZone(Set<Zone> checkedZones, Set<Residence> res, int row, int col) {
        if (grid[row][col] != null) {

            Zone down = null;
            Zone up = null;
            Zone left = null;
            Zone right = null;

            if (row + 1 < height) {
                down = grid[row + 1][col];
            }
            if (row - 1 > -1) {
                up = grid[row - 1][col];
            }
            if (col - 1 > -1) {
                left = grid[row][col - 1];
            }
            if (col + 1 < width) {
                right = grid[row][col + 1];
            }

            if (down != null && down instanceof Road && !checkedZones.contains(down)) {
                checkedZones.add(down);
                checkZone(checkedZones, res, row + 1, col);
            } else if (down != null && down instanceof Residence residence) {
                res.add(residence);
            }

            if (up != null && up instanceof Road && !checkedZones.contains(up)) {
                checkedZones.add(up);
                checkZone(checkedZones, res, row - 1, col);
            } else if (up != null && up instanceof Residence residence) {
                res.add(residence);
            }

            if (left != null && left instanceof Road && !checkedZones.contains(left)) {
                checkedZones.add(left);
                checkZone(checkedZones, res, row, col - 1);
            } else if (left != null && left instanceof Residence residence) {
                res.add(residence);
            }

            if (right != null && right instanceof Road && !checkedZones.contains(right)) {
                checkedZones.add(right);
                checkZone(checkedZones, res, row, col + 1);
            } else if (right != null && right instanceof Residence residence) {
                res.add(residence);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFieldsize() {
        return fieldSize;
    }

    public int getDaysPassedWithoutDisaster() {
        return daysPassedWithoutDisaster;
    }

    public double getDisasterChance() {
        return disasterChance;
    }

    public void setDaysPassedWithoutDisaster(int daysPassedWithoutDisaster) {
        this.daysPassedWithoutDisaster = daysPassedWithoutDisaster;
    }

    public void setDisasterChance(double disasterChance) {
        this.disasterChance = disasterChance;
    }

    public List<Zone> getBuildingsList() {
        return buildings;
    }

    /**
     * Refreshes the happiness
     */
    public void refreshHappiness() {
        calculateHappieness();
        bigCityJframe.setHappiness(Math.round(combinedHappiness));
        bigCityJframe.repaintStatPanelAndGrid();
    }

    /**
     * Generates a random disaster
     */
    public void makeDisaster() {
        //int index=2;
        int index = rnd.nextInt(Disaster.values().length);
        Disaster.values()[index].activate(Engine.this);
        daysPassedWithoutDisaster = 0;
        disasterChance -= 1.0;
        if (disasterChance < 0) {
            disasterChance = 0.0;
        }
    }

    /**
     * Returns if a zone is selected and if the coordinates are equal with the givens
     * @param row   - int, row index
     * @param col   - int, column index
     * @return      - boolean, false if there is no selected zone or the coordinates
     * dont match, true if there is a selected zone with matching coordinates
     */
   public boolean isZoneSelected(int row, int col) {
        if (bigCityJframe.getStatPanel() != null) {
            return bigCityJframe.getStatPanel().getZone() == grid[row][col];
        }
        return false;
    }

    /**
    * Uncelect the selected zone
    */
    public void unselectZone() {
        bigCityJframe.changeRightPanelToBuildPanel();
    }

    /**
     * Saves the current game
     */
    public void saveGame() {
        try {
            var tmp = Files.lines(Path.of("savedGames", "savedGames.txt"));
            boolean alreadySaved = tmp
                    .anyMatch((n) -> n.equals(name));

            if (!alreadySaved) {
                try ( BufferedWriter writer = new BufferedWriter(new FileWriter(
                        "savedGames/savedGames.txt", true))) {
                    writer.append(name);
                    writer.append('\n');
                }
            }
        } catch (IOException e) {
            //System.out.println("Nem találom a filet, próbálj ne IDE-ből futtatni");
            try {
                File savedGames = new File("savedGames/savedGames.txt");
                savedGames.getParentFile().mkdirs();
                savedGames.createNewFile();
                try ( BufferedWriter writer = new BufferedWriter(new FileWriter(
                        savedGames, true))) {
                    writer.append(name);
                    writer.append('\n');
                }

            } catch (IOException e1) {
                System.out.println(e1.getMessage());
            }
        }

        try {
            File thisGame = new File("savedGames/" + name + ".txt");
            thisGame.getParentFile().mkdirs();
            thisGame.createNewFile();
            try ( BufferedWriter writer = new BufferedWriter(new FileWriter(
                    thisGame))) {
                writer.write(name + "\n");
                writer.write(height + ";" + width + "\n");
                writer.write(fieldSize + "\n");
                writer.write(money + "\n");
                writer.write(yearlyIncome + "\n");
                writer.write(expenses + "\n");
                writer.write(bigCityJframe.getDate() + "\n");
                writer.write(taxPercentage + "\n");
                writer.write(disasterChance + "\n");
                writer.write(daysPassedWithoutDisaster + "\n");

                for (Zone zone : buildings) {
                    writer.write(zone.getTopLeftX() / fieldSize + ";" + zone.getTopLeftY() / fieldSize + ";");
                    if (zone instanceof HighSchool) {
                        writer.write("HighSchool;1\n");
                    } else if (zone instanceof Industry i) {
                        writer.write("Industry;" + i.getLevel() + "\n");
                    } else if (zone instanceof Police) {
                        writer.write("Police;1\n");
                    } else if (zone instanceof Residence r) {
                        writer.write("Residence;" + r.getLevel() + "\n");
                    } else if (zone instanceof Road) {
                        writer.write("Road;1\n");
                    } else if (zone instanceof Service s) {
                        writer.write("Service;" + s.getLevel() + "\n");
                    } else if (zone instanceof Stadium) {
                        writer.write("Stadium;1\n");
                    } else if (zone instanceof University) {
                        writer.write("University;1\n");
                    }
                }

                for (Person p : residents) {
                    writer.write(p.getName() + ";" + p.getAge() + ";"
                            + p.isMale() + ";" + p.getHappiness() + ";"
                            + p.getEducationLevel().toString() + ";"
                            + p.getHome().getTopLeftX() / fieldSize + ";"
                            + p.getHome().getTopLeftY() / fieldSize + ";"
                            + (p.getJob() == null ? "-1;-1" : (p.getJob().getTopLeftX() / fieldSize + ";"
                            + p.getJob().getTopLeftY() / fieldSize)) + "\n");
                }
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Implements disaster without JFrame using
     */
    public void makeDisasterForTesting(){
        daysPassedWithoutDisaster = 0;
        disasterChance -= 1.0;
        if (disasterChance < 0) {
            disasterChance = 0.0;
        }
    }
    /**
     * Stops the time
     */
    public void stopTime() {
        bigCityJframe.stopTime();
    }

    /**
     * starts the time
     */
    public void startTime() {
        bigCityJframe.startTime();
    }
    
    public BigCityJframe getBigCityJframe(){
        return bigCityJframe;
    }
}
