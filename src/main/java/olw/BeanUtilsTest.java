package olw;

import java.util.Arrays;

import olw.model.Area;
import olw.model.annotations.ContainedIn;

public class BeanUtilsTest {

	public static void main(String[] args) {
		
		Area a = new Area(1l, "Informatik");
		
		Arrays.stream(a.getClass().getDeclaredFields()).filter(x -> x.isAnnotationPresent(ContainedIn.class));
			  
	}

}
