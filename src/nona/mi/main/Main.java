package nona.mi.main;

import java.util.Arrays;
import java.util.HashMap;

public class Main{

	public static void main(String[] args) {


		//new Thorns(854, 480, "Thorns & Petals", Thorns.SMOOTH_GAME_LOOP).start();
		Thorns thorns = new Thorns(854, 480, "Thorns & Petals", Thorns.SMOOTH_GAME_LOOP);
		thorns.setShowScene(true);
		//thorns.setShowLoopLog(true);
		thorns.start();




	}
}
