package net.yorksolutions;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

public class ModularityTests {
	ApplicationModules modules = ApplicationModules.of(Application.class);
	
	@Test
	void verifyModularity() {
		
		// DESC: Output "modulith" project structure and details
		System.out.println(modules.toString());
		
		// DESC: "Assert" that code-arrangement adheres to
		// ... intended constraints
		modules.verify();
	}

}
