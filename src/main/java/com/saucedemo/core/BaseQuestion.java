
package com.saucedemo.core;

import net.serenitybdd.screenplay.Question;

/**
 * Base class for all Screenplay {@link Question}s in this framework.
 *
 * <p>Marker base today, but kept so domain questions stay decoupled from the
 * underlying Serenity API, and we have a single extension point.
 */
public abstract class BaseQuestion<T> implements Question<T> {
}
