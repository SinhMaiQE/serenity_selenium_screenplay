
package com.saucedemo.core;

import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.Tasks;

/**
 * Base class for all Screenplay {@link Task}s in this framework.
 *
 * <p>Subclasses inherit a typed {@link #instrumented(Class, Object...)} helper
 * so they don't have to import {@link Tasks} directly.
 */
public abstract class BaseTask implements Task {

    protected static <T extends Task> T instrumented(Class<T> taskClass, Object... parameters) {
        return Tasks.instrumented(taskClass, parameters);
    }
}
