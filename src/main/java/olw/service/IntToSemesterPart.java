package olw.service;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import olw.model.Semester;
import olw.model.Semester.Part;

@Component
public class IntToSemesterPart implements Converter<Integer, Semester.Part>{

	@Override
	public Part convert(Integer i) {
		
		Part p = Semester.Part.SoSe;
		
		if(i == 2)
			p = Semester.Part.WiSe;
		
		return p;
	}

}
