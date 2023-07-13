package bigcity;

import java.util.ArrayList;
import res.Assets;

public class Industry extends Workplace {

    public static final int range = 3;
    
    public Industry(int topLeftX, int topLeftY, int price) {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.capacity = 16;
        this.level = 1;
        this.img = Assets.copperI;
        this.workers = new ArrayList<>();
        this.price = price;
    }
    
    
    @Override
    public int getSize() {
        return workers.size();
    }
    
    @Override
    public int upgrade() {
        if(getLevel() < 3) {
            capacity *= 2;
            level++;
        }
        return level;
    }
}
