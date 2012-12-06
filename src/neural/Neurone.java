package neural;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 30/11/2012
 * Time: 18:55
 */
public interface Neurone {

    /**
     * Returns an immutable list of links connected to the back of this one.
     *
     * @return An immutable list of links connected to the back of this one.
     */
    List<Link> getInputLinks();

    /**
     * Returns an immutable list of links connected to the front of this one.
     *
     * @return An immutable list of links connected to the front of this one.
     */
    List<Link> getOutputLinks();

    /**
     * Adds a new input link.
     *
     * @param link The new input link.
     */
    void addInputLink(Link link);

    /**
     * Adds a new output link.
     *
     * @param link The new output link.
     */
    void addOutputLink(Link link);

    /**
     * Removes the given input link from this neurone.
     *
     * @param link The link to remove
     */
    void removeInputLink(Link link);

    /**
     * Removes the given output link from this neurone.
     *
     * @param link The link to remove
     */
    void removeOutputLink(Link link);

    /**
     * Fire the neurone.  It adds all the weighted values from the input neurones
     * and sets its own value as per the getTransitionValue method.
     */
    void fire();

    /**
     * Fires the neurone as per 'fire' method, but using output neurones as input.
     */
    void reverseFire();

    /**
     * Gets the current output value from the neurone (as calculated by the
     * "calculateTransitionValue" function when the neurone was "fire"d.
     *
     * @return This neurone's current output value.
     */
    double getValue();

    /**
     * Artificially sets the output value of the neurone (without considering input values etc...).
     * This is useful to set input neurone values.
     *
     * @param value New output value.
     */
    void setValue(double value);

    /**
     * Gets the sum of the weighted inputs to the neurone, as of the last time
     * the neurone was fired.
     *
     * @return The sum of the weighted inputs to the neurone.
     */
    double getWeightedInput();

    /**
     * Gets the input bias to this neurone.
     *
     * @return Bias.
     */
    public double getBias();

    /**
     * Sets the input bias to this neurone.
     *
     * @param bias Bias.
     */
    public void setBias(double bias);
}
