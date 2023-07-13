package bigcity;

import java.util.ArrayList;
import res.Assets;

public class Service extends Workplace {
    public Service(int topLeftX, int topLeftY, int price) {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.capacity = 8;
        this.level = 1;
        this.img = Assets.copperS;
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
