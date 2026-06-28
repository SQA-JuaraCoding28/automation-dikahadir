package com.kelompok1.cucumber.exceptions;

/**
 * Thrown when a page element cannot be interacted with in the expected way.
 *
 * Named PageElementException (not ElementNotInteractableException) to avoid
 * shadowing org.openqa.selenium.ElementNotInteractableException, which would
 * make stack traces ambiguous and catch blocks error-prone.
 */
public class PageElementException extends RuntimeException {

    public PageElementException(String message) {
        super(message);
    }

    public PageElementException(String message, Throwable cause) {
        super(message, cause);
    }
}
