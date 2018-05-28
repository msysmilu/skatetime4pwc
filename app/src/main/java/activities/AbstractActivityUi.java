package activities;

/**
 * Created by Emil
 * Used in every activity ui in order to control ActivityUi elements in an orderly fashion.
 * Almost encapsulating them.
 */
public abstract class AbstractActivityUi {

    //this method must be implemented in order to add event listeners to all ui elements
    abstract public void addEventListeners();

    //this method must be implemented in order to remove all event listeners from all ui elements
    abstract public void removeEventListeners();

    //this method resets all the positions of the ui elements to reflect "what the user left of"
    //typically, these are read from the shared preferences;
    abstract public void populateElementsFromSharedPreferences();

}
