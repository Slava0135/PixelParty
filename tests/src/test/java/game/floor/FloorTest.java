package game.floor;

import com.slava0135.pixelparty.game.floor.Floor;
import com.slava0135.pixelparty.game.floor.Palette;
import org.junit.jupiter.api.Test;

import static com.slava0135.pixelparty.game.floor.Floor.SIZE;
import static org.junit.jupiter.api.Assertions.*;

public class FloorTest {

    @Test
    public void generateFloor() {
        Floor floor = new Floor();
        floor.generateFloor();
        Palette[][] grid = floor.getGrid();
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                assertNotNull(grid[y][x]);
            }
        }
    }

    @Test
    public void isOnTile() {
        Floor floor = new Floor();
        floor.generateFloor();
        assertTrue(floor.isOnTile(SIZE, SIZE, SIZE));
    }
}
