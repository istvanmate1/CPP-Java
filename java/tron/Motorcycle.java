
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Motorcycle {

    private String playerName;
    private Color lineColor;
    private ArrayList<Point> coordinates;
    private Direction direction;
    private int playerScore;

    public Motorcycle(String playerName, Color lineColor, Point startPoint,
            Direction direction, int playerScore) {

        this.playerScore = playerScore;
        this.playerName = playerName;
        this.lineColor = lineColor;

        coordinates = new ArrayList<>();
        coordinates.add(0, new Point(startPoint.x, startPoint.y));
        //The last modified coordinate is on the 0 index.
        coordinates.add(0, new Point(startPoint.x, startPoint.y));

        this.direction = direction;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public ArrayList<Point> getCoordinates() {
        return coordinates;
    }

    public Color getLineColor() {
        return lineColor;
    }

    /**
     *
     * @param newDirection The motorcycle will go in this direction.
     */
    public void changeDirection(String newDirection) {
        newPoint(coordinates.get(0));
        if (newDirection == "right") {
            if (direction == Direction.UP || direction == Direction.DOWN) {
                direction = Direction.RIGHT;
            }
        } else if (newDirection == "up") {
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                direction = Direction.UP;
            }
        } else if (newDirection == "left") {
            if (direction == Direction.UP || direction == Direction.DOWN) {
                direction = Direction.LEFT;
            }
        } else if (newDirection == "down") {
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                direction = Direction.DOWN;
            }
        }
    }

    /**
     * Draws all the lines, where the motorcycle was.
     *
     * @param g2 The graphics where we draw the motorcycle's lines.
     */
    public void renderLines(Graphics2D g2) {
        int currentPointIndex = 0;
        int nextPointIndex = 1;

        while (nextPointIndex != coordinates.size()) {
            int cx = (int) coordinates.get(currentPointIndex).getX();
            int cy = (int) coordinates.get(currentPointIndex).getY();
            int nx = (int) coordinates.get(nextPointIndex).getX();
            int ny = (int) coordinates.get(nextPointIndex).getY();
            g2.drawLine(cx, cy, nx, ny);
            ++currentPointIndex;
            ++nextPointIndex;
        }
    }

    /**
     * Stores the motorcycle's current position.
     *
     * @param p is the current position.
     */
    private void newPoint(Point p) {
        coordinates.add(0, new Point(p.x, p.y));
    }

    //Motorcycle's velocity.
    private static final int VELOCITY = 5;

    /**
     * The motorcycle moves accoring this method. Changes the motorcycle's
     * current position.
     */
    public void moveTheCurrentPoint() {
        Point currentPoint = coordinates.remove(0);
        int velocityX = direction.x * VELOCITY;
        int velocityY = direction.y * VELOCITY;
        currentPoint.translate(velocityX, velocityY);
        newPoint(currentPoint);
    }

    /**
     *
     * Checks whether the two head lines collide. Checks whether the caller's
     * head lines collide with its own other lines or with the opponet's lines.
     *
     * headLine: The line that is still drawn. (the length of this line can
     * change) obstacleLine: Any line except the headLine. (the other
     * motorcycle's headline is also an obstacle) The board's edges are also
     * obstacles. startPoint: still, endPoint: changes (the motorcycle is here)
     *
     * @param otherMotorcycle is the opponent.
     * @return true if the caller motorcycle crashed (lost), false if didn't
     * crash.
     */
    public boolean crashing(Motorcycle otherMotorcycle) {
        if (outOfBoard()) {
            return true;
        }

        Point headLineEndPoint = coordinates.get(0);
        Point headLineStartPoint = coordinates.get(1);

        boolean thisHeadLineIsHorizontal = lineIsHorizontal(
                headLineEndPoint,
                headLineStartPoint);

        boolean ohterHeadLineIsHorizontal = lineIsHorizontal(
                otherMotorcycle.getCoordinates().get(0),
                otherMotorcycle.getCoordinates().get(1));

        if (thisHeadLineIsHorizontal && !ohterHeadLineIsHorizontal) {
            if (crossCollisionSmallerDistance(
                    coordinates.get(1),
                    coordinates.get(0),
                    otherMotorcycle.getCoordinates().get(1),
                    otherMotorcycle.getCoordinates().get(0)
            ).equals("horizontal")) {
                return true;
            }

        } else if (!thisHeadLineIsHorizontal && ohterHeadLineIsHorizontal) {
            if (crossCollisionSmallerDistance(
                    otherMotorcycle.getCoordinates().get(1),
                    otherMotorcycle.getCoordinates().get(0),
                    coordinates.get(1),
                    coordinates.get(0)).equals("vertical")) {
                return true;
            }
        }

        for (int pointIndex = 1; pointIndex < coordinates.size() - 1; pointIndex++) {
            Point obsticleLineEndPoint = coordinates.get(pointIndex);
            Point obsticleLineStartPoint = coordinates.get(pointIndex + 1);

            boolean obsticleIsHorizontal = lineIsHorizontal(
                    obsticleLineEndPoint,
                    obsticleLineStartPoint);

            if (thisHeadLineIsHorizontal && obsticleIsHorizontal) {
                if (twoHorizontalLinesCollide(headLineStartPoint,
                        headLineEndPoint,
                        obsticleLineStartPoint,
                        obsticleLineEndPoint)) {
                    System.out.print(playerName + "(collision with own line): ");
                    System.out.println("thisHeadLineIsHorizontal && obsticleIsHorizontal");
                    return true;
                }
            } else if (!thisHeadLineIsHorizontal && !obsticleIsHorizontal) {
                if (twoVerticalLinesCollide(headLineStartPoint,
                        headLineEndPoint,
                        obsticleLineStartPoint,
                        obsticleLineEndPoint)) {
                    System.out.print(playerName + "(collision with own line): ");
                    System.out.println("!thisHeadLineIsHorizontal && !obsticleIsHorizontal");
                    return true;
                }
            } else if (thisHeadLineIsHorizontal /*&& !obsticleIsHorizontal*/) {
                if (crossCollisionDetected(
                        headLineStartPoint,
                        headLineEndPoint,
                        obsticleLineStartPoint,
                        obsticleLineEndPoint,
                        false)) {
                    System.out.print(playerName + "(collision with own line): ");
                    System.out.println("thisHeadLineIsHorizontal && !obsticleIsHorizontal");
                    return true;
                }

            } else/*!thisHeadLineIsHorizontal && obsticleIsHorizontal*/ {
                if (crossCollisionDetected(obsticleLineStartPoint,
                        obsticleLineEndPoint,
                        headLineStartPoint,
                        headLineEndPoint,
                        false)) {
                    System.out.print(playerName + "(collision with own line): ");
                    System.out.println("!thisHeadLineIsHorizontal && obsticleIsHorizontal");
                    return true;
                }
            }
        }

        //Collision with the other motorcycle.        
        ArrayList<Point> otherMotorCycleCoordinates = otherMotorcycle.getCoordinates();
        for (int pointIndex = 1; pointIndex < otherMotorCycleCoordinates.size() - 1; pointIndex++) {
            Point obsticleLineEndPoint = otherMotorCycleCoordinates.get(pointIndex);
            Point obsticleLineStartPoint = otherMotorCycleCoordinates.get(pointIndex + 1);

            boolean obsticleIsHorizontal = lineIsHorizontal(
                    obsticleLineEndPoint,
                    obsticleLineStartPoint);

            if (thisHeadLineIsHorizontal && obsticleIsHorizontal) {
                if (twoHorizontalLinesCollide(headLineStartPoint,
                        headLineEndPoint,
                        obsticleLineStartPoint,
                        obsticleLineEndPoint)) {
                    System.out.print(playerName + "(collision with other's line): ");
                    System.out.println("thisHeadLineIsHorizontal && obsticleIsHorizontal");
                    return true;
                }
            } else if (!thisHeadLineIsHorizontal && !obsticleIsHorizontal) {
                if (twoVerticalLinesCollide(headLineStartPoint,
                        headLineEndPoint,
                        obsticleLineStartPoint,
                        obsticleLineEndPoint)) {
                    System.out.print(playerName + "(collision with other's line): ");
                    System.out.println("!thisHeadLineIsHorizontal && !obsticleIsHorizontal");
                    return true;
                }
            } else if (thisHeadLineIsHorizontal /*&& !obsticleIsHorizontal*/) {
                if (crossCollisionDetected(
                        headLineStartPoint,
                        headLineEndPoint,
                        obsticleLineStartPoint,
                        obsticleLineEndPoint,
                        false)) {
                    System.out.print(playerName + "(collision with other's line): ");
                    System.out.println("thisHeadLineIsHorizontal && !obsticleIsHorizontal");
                    return true;
                }

            } else/*!thisHeadLineIsHorizontal && obsticleIsHorizontal*/ {
                if (crossCollisionDetected(obsticleLineStartPoint,
                        obsticleLineEndPoint,
                        headLineStartPoint,
                        headLineEndPoint,
                        false)) {
                    System.out.print(playerName + "(collision with other's line): ");
                    System.out.println("!thisHeadLineIsHorizontal && obsticleIsHorizontal");
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks whether two horizontal lines cover each other.
     *
     * @param thisStartPoint is one of the first line's points.
     * @param thisEndPoint is one of the first line's points.
     * @param otherStartPoint is one of the second line's points.
     * @param otherEndPoint is one of the second line's points.
     * @return true if the two lines cover each other, otherwise false.
     */
    public boolean twoHorizontalLinesCollide(Point thisStartPoint,
            Point thisEndPoint, Point otherStartPoint, Point otherEndPoint) {
        if (thisStartPoint.y != otherEndPoint.y) {
            return false;
        }

        int thisHorizontalSmallerX;
        int thisHorizontalLargerX;
        int thisStartX = thisStartPoint.x;
        int thisEndX = thisEndPoint.x;

        if (thisEndX < thisStartX) {
            thisHorizontalSmallerX = thisEndX;
            thisHorizontalLargerX = thisStartX;
        } else {
            thisHorizontalSmallerX = thisStartX;
            thisHorizontalLargerX = thisEndX;
        }

        int otherHorizontalSmallerX;
        int otherHorizontalLargerX;
        int otherStartX = otherStartPoint.x;
        int otherEndX = otherEndPoint.x;

        if (otherEndX < otherStartX) {
            otherHorizontalSmallerX = otherEndX;
            otherHorizontalLargerX = otherStartX;
        } else {
            otherHorizontalSmallerX = otherStartX;
            otherHorizontalLargerX = otherEndX;
        }
        //collision:
        //this:      -----------------
        //other:              -----------------
        //or
        //this:               -----------------
        //other:     -----------------
        return (otherHorizontalSmallerX < thisHorizontalLargerX
                && thisHorizontalSmallerX < otherHorizontalLargerX)
                || (thisHorizontalSmallerX < otherHorizontalLargerX
                && otherHorizontalSmallerX < thisHorizontalLargerX);
    }

    /**
     * Checks whether two vertical lines cover each other.
     *
     * @param thisStartPoint is one of the first line's points.
     * @param thisEndPoint is one of the first line's points.
     * @param otherStartPoint is one of the second line's points.
     * @param otherEndPoint is one of the second line's points.
     * @return true if the two lines cover each other, otherwise false.
     */
    public boolean twoVerticalLinesCollide(Point thisStartPoint,
            Point thisEndPoint, Point otherStartPoint, Point otherEndPoint) {
        if (thisStartPoint.x != otherEndPoint.x) {
            return false;
        }
        int thisVerticalUpperY;
        int thisVerticalBottomY;
        int thisStartY = thisStartPoint.y;
        int thisEndY = thisEndPoint.y;

        if (thisEndY < thisStartY) {
            //Upper is the smaller.
            //The y value is increasing while we go down on the screen.
            thisVerticalUpperY = thisEndY;
            thisVerticalBottomY = thisStartY;
        } else {
            thisVerticalUpperY = thisStartY;
            thisVerticalBottomY = thisEndY;
        }

        //larger y
        int otherVerticalUpperY;
        //smaller y
        int otherVerticalBottomY;
        int otherStartY = otherStartPoint.y;
        int otherEndY = otherEndPoint.y;

        if (otherEndY < otherStartY) {
            otherVerticalUpperY = otherEndY;
            otherVerticalBottomY = otherStartY;
        } else {
            otherVerticalUpperY = otherStartY;
            otherVerticalBottomY = otherEndY;
        }
        //collision:
        /*      this
                    |          |
                    ||  or    ||
                    ||        ||
                     |        |
                      other
         */
        return (otherVerticalUpperY < thisVerticalBottomY
                && thisVerticalUpperY < otherVerticalBottomY)
                || (thisVerticalUpperY < otherVerticalBottomY
                && otherVerticalUpperY < thisVerticalBottomY);
    }

    /**
     * Checks whether the two motorcycles collided head on.
     *
     * @param otherMotorcycle
     * @return true if head on collision has happend.
     */
    public boolean draw(Motorcycle otherMotorcycle) {
        boolean thisHeadLineIsHorizontal = lineIsHorizontal(
                coordinates.get(0),
                coordinates.get(1));

        boolean ohterHeadLineIsHorizontal = lineIsHorizontal(
                otherMotorcycle.getCoordinates().get(0),
                otherMotorcycle.getCoordinates().get(1));

        //case: Two horizontal lines' collision.
        //head on collision
        if (thisHeadLineIsHorizontal && ohterHeadLineIsHorizontal) {
            return twoHorizontalLinesCollide(coordinates.get(1),
                    coordinates.get(0),
                    otherMotorcycle.getCoordinates().get(1),
                    otherMotorcycle.getCoordinates().get(0));
        }

        //case: Two vertical lines' collision.
        //head on collision
        if (!thisHeadLineIsHorizontal && !ohterHeadLineIsHorizontal) {
            return twoVerticalLinesCollide(coordinates.get(1),
                    coordinates.get(0),
                    otherMotorcycle.getCoordinates().get(1),
                    otherMotorcycle.getCoordinates().get(0));
        }

        //case: A horizontal and a vertical line's collision.
        //Calculate cross point.
        //Calculate distances between cross point and end points.
        //horizontal distance:
        //  distance between the points' x value (simple substraction)
        //  abs(crossPoint.x-horizontalEndPoint.x)
        //vertical distance:
        //  distance between the points' y value (simple substraction)
        //  abs(crossPoint.y-verticalEndPoint.y)
        //Equal distances means draw.
        //cross collision
        if (thisHeadLineIsHorizontal /*&& !ohterHeadLineIsHorizontal*/) {
            return crossCollisionDetected(
                    coordinates.get(1),
                    coordinates.get(0),
                    otherMotorcycle.getCoordinates().get(1),
                    otherMotorcycle.getCoordinates().get(0),
                    true);

        } else/*!thisHeadLineIsHorizontal && ohterHeadLineIsHorizontal*/ {
            return crossCollisionDetected(otherMotorcycle.getCoordinates().get(1),
                    otherMotorcycle.getCoordinates().get(0),
                    coordinates.get(1),
                    coordinates.get(0),
                    true);
        }
    }

    /**
     * Only used when two head lines collide. headLine: The line that is still
     * drawn. (the length of this line can change) If two lines collide (crosses
     * each other) calculates the distance between the cross point end the head
     * lines' end point. endPoint: changes (the motorcycle is here) The the
     * motorcycle which belongs to the smaller distance crashes.(loses)
     *
     * @param horizontalStartPoint
     * @param horizontalEndPoint
     * @param verticalStartPoint
     * @param verticalEndPoint
     * @return none" if the no collision detected, "vertical" if the vertical
     * distance is smaller, "horizontal" if the horizontal distance is smaller
     */
    public String crossCollisionSmallerDistance(Point horizontalStartPoint,
            Point horizontalEndPoint, Point verticalStartPoint, Point verticalEndPoint) {
        Point thisVerticalUpperY;
        Point thisVerticalBottomY;
        Point thisStartY = verticalStartPoint;
        Point thisEndY = verticalEndPoint;

        if (thisEndY.y < thisStartY.y) {
            thisVerticalUpperY = thisEndY;
            thisVerticalBottomY = thisStartY;
        } else {
            thisVerticalUpperY = thisStartY;
            thisVerticalBottomY = thisEndY;
        }

        Point otherHorizontalSmallerX;
        Point otherHorizontalLargerX;
        Point otherStartX = horizontalStartPoint;
        Point otherEndX = horizontalEndPoint;

        if (otherEndX.x < otherStartX.x) {
            otherHorizontalSmallerX = otherEndX;
            otherHorizontalLargerX = otherStartX;
        } else {
            otherHorizontalSmallerX = otherStartX;
            otherHorizontalLargerX = otherEndX;
        }

        if (lineCrossingDetected(otherHorizontalSmallerX,
                otherHorizontalLargerX,
                thisVerticalUpperY,
                thisVerticalBottomY)) {

            Point crossPoint = new Point(thisVerticalBottomY.x,
                    otherHorizontalLargerX.y);

            int verticalDistance = Math.abs(crossPoint.y - thisEndY.y);
            int horizontalDistance = Math.abs(crossPoint.x - otherEndX.x);
            return verticalDistance > horizontalDistance ? "horizontal" : "vertical";
        }
        return "none";
    }

    //True if collided.
    /**
     * Used when we want to decide whether a head line collide with an obstacle
     * line. obstacleLine: Any line except the caller's headLine. (the other
     * motorcycle's headline is also an obstacle) Also used when two head lines
     * collide and we checks wheter it is a draw. (the distances between the
     * cross point and the motorcycles' current position are equal)
     *
     * @param headLinesCrossDetection if true we want to know whether it's a
     * draw or not. False must be used if the two lines are not head lines.
     * @return true if the two lines collide. When the headLinesCrossDetection
     * is set to true it returns true if it is a draw. False if no collision was
     * detected.
     */
    public boolean crossCollisionDetected(Point horizontalStartPoint,
            Point horizontalEndPoint, Point verticalStartPoint, Point verticalEndPoint,
            //If true we must calculate the distances.
            //If false we just have to decide whether a collision happened or not.
            boolean headLinesCrossDetection) {
        Point thisVerticalUpperY;
        Point thisVerticalBottomY;
        Point thisStartY = verticalStartPoint;
        Point thisEndY = verticalEndPoint;

        if (thisEndY.y < thisStartY.y) {
            thisVerticalUpperY = thisEndY;
            thisVerticalBottomY = thisStartY;
        } else {
            thisVerticalUpperY = thisStartY;
            thisVerticalBottomY = thisEndY;
        }

        Point otherHorizontalSmallerX;
        Point otherHorizontalLargerX;
        Point otherStartX = horizontalStartPoint;
        Point otherEndX = horizontalEndPoint;

        if (otherEndX.x < otherStartX.x) {
            otherHorizontalSmallerX = otherEndX;
            otherHorizontalLargerX = otherStartX;
        } else {
            otherHorizontalSmallerX = otherStartX;
            otherHorizontalLargerX = otherEndX;
        }

        if (headLinesCrossDetection) {
            if (lineCrossingDetected(otherHorizontalSmallerX,
                    otherHorizontalLargerX,
                    thisVerticalUpperY,
                    thisVerticalBottomY)) {

                Point crossPoint = new Point(thisVerticalBottomY.x,
                        otherHorizontalLargerX.y);

                int verticalDistance = Math.abs(crossPoint.y - thisEndY.y);
                int horizontalDistance = Math.abs(crossPoint.x - otherEndX.x);
                return verticalDistance == horizontalDistance;
            }
            return false;
        } else {
            return lineCrossingDetected(otherHorizontalSmallerX,
                    otherHorizontalLargerX,
                    thisVerticalUpperY,
                    thisVerticalBottomY);
        }
    }

    /**
     * Use only for line collision where one line is horizontal, the other is
     * vertical.
     *
     * @return true if a vertical and a horizontal line cross each other.
     */
    private boolean lineCrossingDetected(
            Point horizontalLineLeftPoint, Point horizontalLineRightPoint,
            Point verticalLineUpperPoint, Point verticalLineBottomPoint) {

        if (horizontalLineLeftPoint.x < verticalLineBottomPoint.x
                && verticalLineBottomPoint.x < horizontalLineRightPoint.x
                && verticalLineUpperPoint.y < horizontalLineRightPoint.y
                && horizontalLineRightPoint.y < verticalLineBottomPoint.y) {
            return true;

        }

        return false;
    }

    /**
     * Decice wheter the line is horizontal or vertical.
     *
     * @param firstPoint a point of the line
     * @param secondPoint an other point of the same line
     * @return true if the line is horizontal
     */
    public boolean lineIsHorizontal(Point firstPoint, Point secondPoint) {
        if (firstPoint.y == secondPoint.y) {
            return true;
        }
        return false;
    }

    /**
     * Decide whether the caller's motorcycle is out of the board. The
     * motorcycle leaves the board, loses.
     *
     * @return true if the motorcycle is out of the board. False otherwise.
     */
    private boolean outOfBoard() {
        int headX = coordinates.get(0).x;
        int headY = coordinates.get(0).y;

        if (headX <= 0 || 500 <= headX || headY <= 0 || 500 <= headY) {
            System.out.println("outOfBoard");
            return true;
        }

        return false;
    }

}
