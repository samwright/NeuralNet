package neural;

import equation.Equation;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 01/12/2012
 * Time: 00:40
 */
public interface Layer {

    /**
     * Returns an immutable list of the neurones in the layer.
     *
     * @return The neurones in the layer.
     */
    List<Neurone> getNeurones();

    /**
     * Adds a neurone to the layer.
     *
     * This is where the concrete implementation will likely add links
     * between the new and existing neurones in this and neighbouring layers.
     *
     * @param neurone The neurone to add to the layer.
     */
    void addNeurone(Neurone neurone);

    /**
     * Removes a given neurone from the layer.
     *
     * This is where the concrete implementation will likely remove all links
     * associated with the neurone.
     *
     * @param neurone The neurone to remove.
     */
    void removeNeurone(Neurone neurone);

    /**
     * Sets the next layer.
     *
     * This is where the concrete implementation will likely create all links
     * between the neurones in this layer and the given layer.
     *
     * @param layer
     */
    void setNext(Layer layer);

    /**
     * Returns the next layer (null if not set).
     *
     * @return The next layer.
     */
    Layer getNext();

    /**
     * Sets the prev layer.
     *
     * This is where the concrete implementation will likely create all links
     * between the neurones in this layer and the given layer.
     *
     * @param layer
     */
    void setPrev(Layer layer);

    /**
     * Returns the previous layer (null if not set).
     *
     * @return The previous layer.
     */
    Layer getPrev();

    /**
     * Fires all the neurones in the layer.
     */
    void fireAll();

    /**
     * Returns true if a new link should be created between the neurones with indices layer_1_index
     * and layer_2_index in adjacent layers, false if not.
     *
     * @param layer_1_index Index of neurone in one layer.
     * @param layer_2_index Index of neurone in other layer.
     * @return Whether a link should be created between them.
     */
    public abstract boolean isLinkBetween(int layer_1_index, int layer_2_index);

    /**
     * Gets the initial weight of this link between neurones with the given indices.
     *
     * @param layer_1_index Index of neurone in one layer.
     * @param layer_2_index Index of neurone in other layer.
     * @return The initial weight of the link.
     */
    public abstract double getInitialWeight(int layer_1_index, int layer_2_index);

}
