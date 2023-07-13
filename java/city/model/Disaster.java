package model;

import GUI.DisasterDialog;
import bigcity.Person;
import bigcity.Zone;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import res.ResourceLoader;
import rightPanel.buildingStatPanel.BuildingStatPanel;

public enum Disaster {
    SMALL_METEOR(
            new Consumer<Engine>() {
        DisasterDialog dialog;
        Random rnd;

        @Override
        public void accept(Engine e) {
            
            if (rnd == null) {
                rnd = new Random();
            }
            List<Zone> buildings = e.getBuildingsList();
            if (buildings.isEmpty()) {
                return;
            }
            int fieldsize = e.getFieldsize();
            int index = rnd.nextInt(buildings.size());
            int row = buildings.get(index).getTopLeftY() / fieldsize;
            int col = buildings.get(index).getTopLeftX() / fieldsize;
            changeToBuildingPanelIfNeeded(row, col, e);
            BufferedImage img = null;
            try {
                img = ResourceLoader.loadBufferedImage("GUI/meteor2.png");
            } catch (IOException ex) {
                Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            e.setImg(row, col, img);
            e.destroyZone(row, col, fieldsize,
                    true);
            if (dialog == null) {
                dialog = new DisasterDialog(new JLabel("<html>"
                        + "Egy kis meteor csapódott be a városba." + "<br>"
                        + "Néhány épület megsemmisült, többen elköltöztek." + "<br>"
                        + "A boldogsági szint kis mértékben csökkent." + "</html>"),
                        e, row, col,null);
            }

            decreaseHappiness(e.getResidents(), -10);
            e.refreshHappiness();
            dialog.setActive();
        }
    }
    ),
    MEDIUM_METEOR(
            new Consumer<Engine>() {
        DisasterDialog dialog;
        Random rnd;

        @Override
        public void accept(Engine e) {
            
            if (rnd == null) {
                rnd = new Random();
            }
            List<Zone> buildings = e.getBuildingsList();
            if (buildings.isEmpty()) {
                return;
            }
            int fieldsize = e.getFieldsize();
            int index = rnd.nextInt(buildings.size());
            int row = buildings.get(index).getTopLeftY() / fieldsize;
            int col = buildings.get(index).getTopLeftX() / fieldsize;
            ArrayList<Coords> points = new ArrayList<>();
            changeToBuildingPanelIfNeeded(row, col, e);
            BufferedImage img = null;
            try {
                img = ResourceLoader.loadBufferedImage("GUI/meteor2.png");
            } catch (IOException ex) {
                Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            e.setImg(row, col, img);
            e.destroyZone(row, col, fieldsize,true);

            if (row + 1 < e.getHeight()) {
                changeToBuildingPanelIfNeeded(row + 1, col, e);
                try {
                    img = ResourceLoader.loadBufferedImage("GUI/burned.png");
                } catch (IOException ex) {
                    Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                e.setImg(row + 1, col, img);
                e.destroyZone(row + 1, col, fieldsize, true);
                points.add(new Coords(row + 1,col));
            }

            if (col + 1 < e.getWidth()) {
                changeToBuildingPanelIfNeeded(row, col + 1, e);
                try {
                    img = ResourceLoader.loadBufferedImage("GUI/burned.png");
                } catch (IOException ex) {
                    Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                e.setImg(row, col+1, img);
                e.destroyZone(row, col + 1, fieldsize, true);
                points.add(new Coords(row ,col+1));
            }

            if (row > 0) {
                changeToBuildingPanelIfNeeded(row - 1, col, e);
                try {
                    img = ResourceLoader.loadBufferedImage("GUI/burned.png");
                } catch (IOException ex) {
                    Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                e.setImg(row-1, col, img);
                e.destroyZone(row - 1, col, fieldsize, true);
                points.add(new Coords(row - 1,col));
            }

            if (col > 0) {
                changeToBuildingPanelIfNeeded(row, col - 1, e);
                try {
                    img = ResourceLoader.loadBufferedImage("GUI/burned.png");
                } catch (IOException ex) {
                    Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                e.setImg(row, col-1, img);
                e.destroyZone(row, col - 1, fieldsize, true);
                points.add(new Coords(row ,col-1));
            }

            if (dialog == null) {
                dialog = new DisasterDialog(new JLabel("<html>"
                        + "Egy közepes meteor csapódott be a városba." + "<br>"
                        + "Néhány épület megsemmisült, többen elköltöztek." + "<br>"
                        + "A boldogsági szint közepes mértékben csökkent." + "</html>"),
                        e, row, col,points);
            }

            decreaseHappiness(e.getResidents(), -20);
            e.refreshHappiness();
            dialog.setActive();
        }
    }
    ), BIG_METEOR(
            new Consumer<Engine>() {
        DisasterDialog dialog;
        Random rnd;

        @Override
        public void accept(Engine e) {
            
            if (rnd == null) {
                rnd = new Random();
            }
            List<Zone> buildings = e.getBuildingsList();
            if (buildings.isEmpty()) {
                return;
            }
            int fieldsize = e.getFieldsize();
            int index = rnd.nextInt(buildings.size());
            int row = buildings.get(index).getTopLeftY() / fieldsize;
            int col = buildings.get(index).getTopLeftX() / fieldsize;
            ArrayList<Coords> points = new ArrayList<>();
            changeToBuildingPanelIfNeeded(row, col, e);
            BufferedImage img = null;
            try {
                img = ResourceLoader.loadBufferedImage("GUI/meteor2.png");
            } catch (IOException ex) {
                Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            e.setImg(row, col, img);
            e.destroyZone(row, col, fieldsize, true);

            if (row + 1 < e.getHeight()) {
                changeToBuildingPanelIfNeeded(row + 1, col, e);
                e.destroyZone(row + 1, col, fieldsize, true);
                points.add(new Coords(row + 1,col));
                try {
                    img = ResourceLoader.loadBufferedImage("GUI/burned.png");
                    } catch (IOException ex) {
                        Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.setImg(row+1, col, img);
                if (col + 1 < e.getWidth()) {
                    changeToBuildingPanelIfNeeded(row + 1, col + 1, e);
                    e.destroyZone(row + 1, col + 1, fieldsize, true);
                    points.add(new Coords(row + 1,col+1));
                    try {
                    img = ResourceLoader.loadBufferedImage("GUI/burned.png");
                    } catch (IOException ex) {
                        Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.setImg(row+1, col+1, img);
                }
                if (col > 0) {
                    changeToBuildingPanelIfNeeded(row + 1, col - 1, e);
                    e.destroyZone(row + 1, col - 1, fieldsize, true);
                    points.add(new Coords(row + 1,col-1));
                    try {
                    img = ResourceLoader.loadBufferedImage("GUI/burned.png");
                    } catch (IOException ex) {
                        Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.setImg(row+1, col-1, img);
                }
            }
            if (col + 1 < e.getWidth()) {
                changeToBuildingPanelIfNeeded(row, col + 1, e);
                e.destroyZone(row, col + 1, fieldsize, true);
                points.add(new Coords(row ,col+1));
                    try {
                    img = ResourceLoader.loadBufferedImage("GUI/burned.png");
                    } catch (IOException ex) {
                        Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.setImg(row, col+1, img);
            }

            if (row > 0) {
                changeToBuildingPanelIfNeeded(row - 1, col, e);
                e.destroyZone(row - 1, col, fieldsize, true);
                points.add(new Coords(row -1,col));
                    try {
                    img = ResourceLoader.loadBufferedImage("GUI/burned.png");
                    } catch (IOException ex) {
                        Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.setImg(row-1, col, img);
                if (col + 1 < e.getWidth()) {
                    changeToBuildingPanelIfNeeded(row - 1, col + 1, e);
                    e.destroyZone(row - 1, col + 1, fieldsize, true);
                    points.add(new Coords(row - 1,col+1));
                    try {
                    img = ResourceLoader.loadBufferedImage("GUI/burned.png");
                    } catch (IOException ex) {
                        Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.setImg(row-1, col+1, img);
                }
                if (col > 0) {
                    changeToBuildingPanelIfNeeded(row - 1, col - 1, e);
                    e.destroyZone(row - 1, col - 1, fieldsize, true);
                    points.add(new Coords(row - 1,col-1));
                    try {
                    img = ResourceLoader.loadBufferedImage("GUI/burned.png");
                    } catch (IOException ex) {
                        Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.setImg(row-1, col-1, img);
                }
            }

            if (col > 0) {
                changeToBuildingPanelIfNeeded(row, col - 1, e);
                e.destroyZone(row, col - 1, fieldsize, true);
                points.add(new Coords(row ,col-1));
                    try {
                    img = ResourceLoader.loadBufferedImage("GUI/burned.png");
                    } catch (IOException ex) {
                        Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.setImg(row, col-1, img);
            }

            if (dialog == null) {
                dialog = new DisasterDialog(new JLabel("<html>"
                        + "Egy nagy meteor csapódott be a városba." + "<br>"
                        + "Néhány épület megsemmisült, többen elköltöztek." + "<br>"
                        + "A boldogsági szint nagy mértékben csökkent." + "</html>"),
                        e, row, col,points);
            }

            decreaseHappiness(e.getResidents(), -30);
            e.refreshHappiness();
            dialog.setActive();
            
        }
    }
    ), SMALL_TORNADO(
            new Consumer<Engine>() {
        DisasterDialog dialog;
        Random rnd;

        @Override
        public void accept(Engine e) {
            
            if (rnd == null) {
                rnd = new Random();
            }
            List<Zone> buildings = e.getBuildingsList();
            if (buildings.isEmpty()) {
                return;
            }
            int fieldsize = e.getFieldsize();
            int index = rnd.nextInt(buildings.size());
            int row = buildings.get(index).getTopLeftY() / fieldsize;
            int col = buildings.get(index).getTopLeftX() / fieldsize;
            ArrayList<Coords> points = new ArrayList<>();
            changeToBuildingPanelIfNeeded(row, col, e);
            BufferedImage img = null;
            try {
                img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
            } catch (IOException ex) {
                Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            e.setImg(row, col, img);
            e.destroyZone(row, col, fieldsize, true);

            if (row + 1 < e.getWidth()) {
                changeToBuildingPanelIfNeeded(row + 1, col, e);
                e.destroyZone(row + 1, col, fieldsize, true);
                points.add(new Coords(row+1 ,col));
                    try {
                    img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
                    } catch (IOException ex) {
                        Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.setImg(row+1, col, img);
            } else {
                changeToBuildingPanelIfNeeded(row - 1, col, e);
                e.destroyZone(row - 1, col, fieldsize, true);
                points.add(new Coords(row-1 ,col));
                    try {
                    img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
                    } catch (IOException ex) {
                        Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.setImg(row-1, col, img);
            }

            if (dialog == null) {
                dialog = new DisasterDialog(new JLabel("<html>"
                        + "Egy kis tornádó söpört végig a városon." + "<br>"
                        + "Néhány épület megsemmisült, többen elköltöztek." + "<br>"
                        + "A boldogsági szint kis mértékben csökkent." + "</html>"),
                        e, row, col,points);
            }

            decreaseHappiness(e.getResidents(), -10);
            e.refreshHappiness();
            dialog.setActive();
            
        }
    }), MEDIUM_TORNADO(
            new Consumer<Engine>() {
        DisasterDialog dialog;
        Random rnd;

        @Override
        public void accept(Engine e) {
            if (rnd == null) {
                rnd = new Random();
            }
            List<Zone> buildings = e.getBuildingsList();
            if (buildings.isEmpty()) {
                return;
            }
            int fieldsize = e.getFieldsize();
            int index = rnd.nextInt(buildings.size());
            int row = buildings.get(index).getTopLeftY() / fieldsize;
            int col = buildings.get(index).getTopLeftX() / fieldsize;
            ArrayList<Coords> points = new ArrayList<>();
            changeToBuildingPanelIfNeeded(row, col, e);
            BufferedImage img = null;
            try {
                img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
            } catch (IOException ex) {
                Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            e.setImg(row, col, img);
            e.destroyZone(row, col, fieldsize, true);

            if (row + 1 < e.getWidth()) {
                changeToBuildingPanelIfNeeded(row + 1, col, e);
                e.destroyZone(row + 1, col, fieldsize, true);
                points.add(new Coords(row+1 ,col));
                try {
                img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
                } catch (IOException ex) {
                    Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                e.setImg(row+1, col, img);
                if (row + 2 < e.getWidth()) {
                    changeToBuildingPanelIfNeeded(row + 2, col, e);
                    e.destroyZone(row + 2, col, fieldsize, true);
                    points.add(new Coords(row+2 ,col));
                    try {
                    img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
                    } catch (IOException ex) {
                        Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.setImg(row+2, col, img);
                } else if (row > 0) {
                    changeToBuildingPanelIfNeeded(row - 1, col, e);
                    e.destroyZone(row - 1, col, fieldsize, true);
                    points.add(new Coords(row-1 ,col));
                    try {
                    img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
                    } catch (IOException ex) {
                        Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.setImg(row-1, col, img);
                }
            } else if (row > 0) {
                changeToBuildingPanelIfNeeded(row - 1, col, e);
                e.destroyZone(row - 1, col, fieldsize, true);
                points.add(new Coords(row-1 ,col));
                try {
                img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
                } catch (IOException ex) {
                    Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                e.setImg(row-1, col, img);

                if (row - 1 > 0) {
                    changeToBuildingPanelIfNeeded(row - 2, col, e);
                    e.destroyZone(row - 2, col, fieldsize, true);
                    points.add(new Coords(row-2 ,col));
                    try {
                    img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
                    } catch (IOException ex) {
                        Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.setImg(row-2, col, img);
                } else if (row + 1 < e.getWidth()) {
                    changeToBuildingPanelIfNeeded(row + 1, col, e);
                    e.destroyZone(row + 1, col, fieldsize, true);
                    points.add(new Coords(row+1 ,col));
                    try {
                    img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
                    } catch (IOException ex) {
                        Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.setImg(row+1, col, img);
                    }
            }

            if (dialog == null) {
                dialog = new DisasterDialog(new JLabel("<html>"
                        + "Egy közepes tornádó söpört végig a városon." + "<br>"
                        + "Néhány épület megsemmisült, többen elköltöztek." + "<br>"
                        + "A boldogsági szint közepes mértékben csökkent." + "</html>"),
                        e, row, col,points);
            }

            decreaseHappiness(e.getResidents(), -20);
            e.refreshHappiness();
            dialog.setActive();
        }
    }), BIG_TORNADO(
            new Consumer<Engine>() {
        DisasterDialog dialog;
        Random rnd;

        @Override
        public void accept(Engine e) {
            if (rnd == null) {
                rnd = new Random();
            }
            List<Zone> buildings = e.getBuildingsList();
            if (buildings.isEmpty()) {
                return;
            }
            int fieldsize = e.getFieldsize();
            int index = rnd.nextInt(buildings.size());
            int row = buildings.get(index).getTopLeftY() / fieldsize;
            int col = buildings.get(index).getTopLeftX() / fieldsize;
            ArrayList<Coords> points = new ArrayList<>();
            System.out.println("" + row);
            System.out.println("" + col);

            changeToBuildingPanelIfNeeded(row, col, e);
            BufferedImage img = null;
            try {
                img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
            } catch (IOException ex) {
                Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            e.setImg(row, col, img);
            e.destroyZone(row, col, fieldsize, true);

            if (row + 1 < e.getWidth()) {
                changeToBuildingPanelIfNeeded(row + 1, col, e);
                e.destroyZone(row + 1, col, fieldsize, true);
                points.add(new Coords(row+1 ,col));
                    try {
                    img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
                    } catch (IOException ex) {
                        Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.setImg(row+1, col, img);

                if (row + 2 < e.getWidth()) {
                    changeToBuildingPanelIfNeeded(row + 2, col, e);
                    e.destroyZone(row + 2, col, fieldsize, true);
                    points.add(new Coords(row+2 ,col));
                    try {
                    img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
                    } catch (IOException ex) {
                        Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    e.setImg(row+2, col, img);
                    if (row + 3 < e.getWidth()) {
                        changeToBuildingPanelIfNeeded(row + 3, col, e);
                        e.destroyZone(row + 3, col, fieldsize, true);
                        points.add(new Coords(row + 3 ,col));
                        try {
                        img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
                        } catch (IOException ex) {
                            Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        e.setImg(row + 3, col, img);
                    } else if (row > 0) {
                        changeToBuildingPanelIfNeeded(row - 1, col, e);
                        e.destroyZone(row - 1, col, fieldsize, true);
                        points.add(new Coords(row -1 ,col));
                        try {
                        img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
                        } catch (IOException ex) {
                            Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        e.setImg(row -1, col, img);
                    }

                } else if (row > 0) {
                    changeToBuildingPanelIfNeeded(row - 1, col, e);
                    e.destroyZone(row - 1, col, fieldsize, true);
                    points.add(new Coords(row -1,col));
                        try {
                        img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
                        } catch (IOException ex) {
                            Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        e.setImg(row-1, col, img);

                    if (row - 1 > 0) {
                        changeToBuildingPanelIfNeeded(row - 2, col, e);
                        e.destroyZone(row - 2, col, fieldsize, true);
                        points.add(new Coords(row -2 ,col));
                        try {
                        img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
                        } catch (IOException ex) {
                            Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        e.setImg(row -2, col, img);
                    }
                }
            } else if (row > 0) {
                changeToBuildingPanelIfNeeded(row - 1, col, e);
                e.destroyZone(row - 1, col, fieldsize, true);
                points.add(new Coords(row -1 ,col));
                        try {
                        img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
                        } catch (IOException ex) {
                            Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        e.setImg(row -1, col, img);
                if (row - 1 > 0) {
                    changeToBuildingPanelIfNeeded(row - 2, col, e);
                    e.destroyZone(row - 2, col, fieldsize, true);
                    points.add(new Coords(row - 2 ,col));
                        try {
                        img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
                        } catch (IOException ex) {
                            Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        e.setImg(row -2, col, img);
                    if (row - 2 > 0) {
                        changeToBuildingPanelIfNeeded(row - 3, col, e);
                        e.destroyZone(row - 3, col, fieldsize, true);
                        points.add(new Coords(row - 3 ,col));
                        try {
                        img = ResourceLoader.loadBufferedImage("GUI/hurricane.jpg");
                        } catch (IOException ex) {
                            Logger.getLogger(BuildingStatPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        e.setImg(row - 3, col, img);
                    }
                }
            }

            if (dialog == null) {
                dialog = new DisasterDialog(new JLabel("<html>"
                        + "Egy nagy tornádó söpört végig a városon." + "<br>"
                        + "Néhány épület megsemmisült, többen elköltöztek." + "<br>"
                        + "A boldogsági szint nagy mértékben csökkent." + "</html>"),
                        e, row, col,points);
            }

            decreaseHappiness(e.getResidents(), -30);
            e.refreshHappiness();
            dialog.setActive();
        }
    });
    ;

    Consumer<Engine> action;

    private Disaster(Consumer<Engine> action) {
        this.action = action;
    }

    /**
     * Activates the action of the diasester
     *
     * @param e - Engine, the game engine this disaster is attached to
     */
    public void activate(Engine e) {
        action.accept(e);
    }

    private static void decreaseHappiness(List<Person> residents, int value) {
        for (Person resident : residents) {
            resident.changeHappinessBy(value);
        }
    }

    private static void changeToBuildingPanelIfNeeded(int row, int col, Engine e) {
        if (e.isZoneSelected(row, col)) {
            e.unselectZone();
        }
    }
}
