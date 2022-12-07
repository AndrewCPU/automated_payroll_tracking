package net.andrewcpu.payroll.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.andrewcpu.payroll.model.PayrollStubModel;

import java.io.File;
import java.io.IOException;

public class StubUtil {
	public static PayrollStubModel loadStub(String name) {
		File file = new File(name);
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(file, PayrollStubModel.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
