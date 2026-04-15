
package com.saucedemo.core;

import net.serenitybdd.screenplay.Tasks;
import net.serenitybdd.screenplay.Task;

public abstract class BaseTask implements Task {

	protected static <T extends Task> T instrumented(Class<T> taskClass, Object... parameters) {
		return Tasks.instrumented(taskClass, parameters);
	}
}
