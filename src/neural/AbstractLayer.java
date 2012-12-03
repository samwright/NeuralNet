package neural;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sam Wright
 * Date: 02/12/2012
 * Time: 12:19
 */
public abstract class AbstractLayer implements Layer {
    private List<Neurone> neurones = new LinkedList<Neurone>();
    private Layer prev, next;

    @Override
    public List<Neurone> getNeurones() {
        return Collections.unmodifiableList(neurones);
    }

    @Override
    public void addNeurone(Neurone neurone) {
        int i = getNeurones().size();
        neurones.add(neurone);
        neurone.setBias(getInitialWeight(1,1));

        int j = 0;
        Link new_link;

        if (getPrev() != null) {
            for (Neurone prev_neurone : getPrev().getNeurones()) {
                if (isLinkBetween(i, j)) {
                    new_link = new LinkImpl(prev_neurone, neurone);
                    new_link.setWeight(getInitialWeight(i, j));
                }
                ++j;
            }
        }

        j = 0;
        if (getNext() != null) {
            for (Neurone next_neurone : getNext().getNeurones()) {
                if (isLinkBetween(i, j)) {
                    new_link = new LinkImpl(neurone, next_neurone);
                    new_link.setWeight(getInitialWeight(i, j));
                }
                ++j;
            }
        }
    }

    @Override
    public void removeNeurone(Neurone neurone) {
        neurones.remove(neurone);

        for (Link link : neurone.getInputLinks()) {
            link.delete();
        }

        for (Link link : neurone.getOutputLinks()) {
            link.delete();
        }
    }

    @Override
    public void setNext(Layer layer) {

        if (next != null) {
            List<Neurone> old_next_neurones = next.getNeurones();
            List<Link> links_to_remove = new LinkedList<Link>();

            for (Neurone neurone : neurones) {
                for (Link link : neurone.getOutputLinks()) {
                    if (old_next_neurones.contains(link.getOutput()))
                        links_to_remove.add(link);
                }

                for (Link link : links_to_remove) {
                    neurone.removeInputLink(link);
                }

                links_to_remove.clear();
            }

            next.setPrev(null);
        }

        next = layer;
        if (next != null) {
            if (next.getPrev() != this)
                next.setPrev(this);
            else
                buildLinksBetween(this, layer);
        }
    }

    @Override
    public Layer getNext() {
        return next;
    }

    @Override
    public void setPrev(Layer layer) {

        if (prev != null) {
            List<Neurone> old_prev_neurones = prev.getNeurones();
            List<Link> links_to_remove = new LinkedList<Link>();

            for (Neurone neurone : neurones) {
                for (Link link : neurone.getInputLinks()) {
                    if (old_prev_neurones.contains(link.getInput()))
                        links_to_remove.add(link);
                }

                for (Link link : links_to_remove) {
                    neurone.removeInputLink(link);
                }

                links_to_remove.clear();
            }

            prev.setNext(null);
        }
        prev = layer;
        if (prev != null) {
            if (prev.getNext() != this)
                prev.setNext(this);
            else
                buildLinksBetween(layer, this);
        }
    }

    @Override
    public Layer getPrev() {
        return prev;
    }

    @Override
    public void fireAll() {
        for (Neurone neurone : neurones) {
            neurone.fire();
        }
    }

    private void buildLinksBetween(Layer before, Layer after) {
        Link new_link;
        int i = 0;
        int j = 0;

        for (Neurone neurone : before.getNeurones()) {

            for (Neurone other : after.getNeurones()) {
                if (isLinkBetween(i, j)) {
                    new_link = new LinkImpl(neurone, other);
                    new_link.setWeight(getInitialWeight(i, j));
                }
                ++j;
            }
            j = 0;
            ++i;
        }
    }
}
