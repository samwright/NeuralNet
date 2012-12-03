package neural;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 30/11/2012
 * Time: 19:08
 */
public interface Link {

    /**
     * Gets the neurone that inputs to the link.
     *
     * @return The input neurone.
     */
    Neurone getInput();

    /**
     * Gets the neurone that outputs from the link.
     *
     * @return The output neurone.
     */
    Neurone getOutput();

    /**
     * Sets the link weight.  Normally between -1 and +1.
     *
     * @param weight The link's weight.
     */
    void setWeight(double weight);

    /**
     * Gets the weight of this link.  Normally between -1 and +1.
     *
     * @return The link's weight.
     */
    double getWeight();

    /**
     * Deletes this link from the graph (updating the neurones on either side).
     */
    void delete();

}
